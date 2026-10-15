package com.tiket.apiPage;

import com.google.gson.Gson;
import com.tiket.api.member.CreateProfileApi;
import com.tiket.api.member.DeleteProfileApi;
import com.tiket.api.member.GetSmartProfileApi;
import com.tiket.api.unm.GETunmCountryListApi;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.member.CreateProfileResponse;
import com.tiket.pojo.member.DeleteProfileResponse;
import com.tiket.pojo.member.SmartProfilePojo;
import com.tiket.pojo.member.SmartProfileResponse;
import com.tiket.pojo.unm.CountryListPojoResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;

public class SmartProfileApi <T extends WebDriver> extends BasePage<T> {

    private static final int PROFILE_UPSERT_ATTEMPTS = 3;
    private static final long PROFILE_RETRY_BACKOFF_MILLIS = 3000;
    private static final int PROFILE_VISIBILITY_ATTEMPTS = 5;
    private static final long PROFILE_VISIBILITY_POLL_MILLIS = 2000;
    private static final String DEFAULT_PASSPORT_ISSUING_COUNTRY = "ID";

    public SmartProfileApi(Session<T> session) {
        super(session);
    }

    @SneakyThrows
    public SmartProfileResponse.Data getSmartProfileData(
            String accessToken,
            String firstName
    ) {
        GetSmartProfileApi.Inputs inputs = new GetSmartProfileApi.Inputs(accessToken);
        SmartProfileResponse.Data smartProfile = getSmartProfiles(inputs,firstName);
        logger.log("name from smart profile: " + smartProfile.accountFirstName);
        return smartProfile;

    }

    private SmartProfileResponse.Data getSmartProfiles(GetSmartProfileApi.Inputs inputs,String firstName) throws ExecutionException, InterruptedException {
        GetSmartProfileApi getSmartProfileApi = new GetSmartProfileApi(inputs);
        ApiResponse smartProfileApiResponse = getSmartProfileApi.hitApi(apiTools);
        Gson gson = new Gson();
        SmartProfileResponse smartProfileApiResponseData = gson.fromJson(smartProfileApiResponse.getBody(), SmartProfileResponse.class);
        for (SmartProfileResponse.Data profile : smartProfileApiResponseData.data) {
            if (profile.accountFirstName.equals(firstName)) {
                return profile;
            }
        }
        //throw an exception if no profile is found
        Assertion.fail("No profile found with first name: " + firstName);
        return null; // This line will never be reached due to the exception above
    }

    private ArrayList<SmartProfileResponse.Data> getSmartProfiles(GetSmartProfileApi.Inputs inputs) throws ExecutionException, InterruptedException {
        GetSmartProfileApi getSmartProfileApi = new GetSmartProfileApi(inputs);
        ApiResponse smartProfileApiResponse = getSmartProfileApi.hitApi(apiTools);
        Gson gson = new Gson();
        SmartProfileResponse smartProfileApiResponseData = gson.fromJson(smartProfileApiResponse.getBody(), SmartProfileResponse.class);

        return smartProfileApiResponseData.data;
    }

    @SneakyThrows
    public SmartProfilePojo addProfileAPI(
            String username, String password
    ){
        Unm<T> unm = new Unm<>(session);
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String accessToken = authData.accessToken;
        boolean loginStatus = authData.accountUsername.equals(username);
        logger.log("loginStatus-> " + username + ", " + loginStatus);
        long accountId = authData.accountId;
        logger.log("Account ID: "+ accountId);
        String firstName = randomName();
        String lastName = randomLastName();
        String salutation = randomSalutation();
        CountryListPojoResponse.CountryData countryData = getNationId();
        logger.log("Country selected: "+countryData.getCountriesName()+" with ISD code: "+countryData.getCountriesIsoCode());

        CreateProfileApi.Inputs createProfileApiInputs = new CreateProfileApi.Inputs(accessToken,firstName,lastName,salutation,countryData.getCountriesIsoCode());
        CreateProfileResponse createProfileResponseData = createProfile(createProfileApiInputs);
        logger.log("Created smart profile status: "+createProfileResponseData.message);
        logger.log("Created smart profile with name: "+createProfileResponseData.data.accountSalutationName+" "+createProfileResponseData.data.accountFirstName+" "+createProfileResponseData.data.accountLastName);
        SmartProfilePojo smartProfilePojo = new SmartProfilePojo(firstName,lastName,countryData.getCountriesName(),countryData.getCountriesIsoCode(),createProfileResponseData.data.id,createProfileResponseData.data.accountSalutationName);
        logger.log("Smart profile data: "+smartProfilePojo.toString());
        return smartProfilePojo;
    }

    private CreateProfileResponse createProfile (CreateProfileApi.Inputs inputs) throws ExecutionException, InterruptedException {
        CreateProfileApi createProfileApi = new CreateProfileApi(inputs);
        ApiResponse createProfileResponse = createProfileApi.hitApi(apiTools);
        Gson gson = new Gson();

        return gson.fromJson(createProfileResponse.getBody(), CreateProfileResponse.class);
    }

    /**
     * Creates or replaces a non-primary saved passenger whose Indonesian passport expires
     * {@code expiryOffsetDays} from today (Jakarta). Matching first+last name is deleted first
     * so each run lands on a known expiry window.
     * <p>The whole exchange is retried: it spans a login, a read, a delete and a create, and a
     * transient failure on any of them would otherwise fail the test that depends on it.
     */
    @SneakyThrows
    public void upsertIndonesianPassportProfile(
            String username,
            String password,
            String firstName,
            String lastName,
            String salutation,
            String passportNumber,
            String passportIssuedDate,
            String passportIssuingCountry,
            int expiryOffsetDays
    ) {
        for (int attempt = 1; attempt <= PROFILE_UPSERT_ATTEMPTS; attempt++) {
            try {
                createIndonesianPassportProfile(username, password, firstName, lastName, salutation,
                        passportNumber, passportIssuedDate, passportIssuingCountry, expiryOffsetDays);
                return;
            } catch (Exception e) {
                if (attempt == PROFILE_UPSERT_ATTEMPTS) {
                    Assertion.fail("Failed to upsert Indonesian passport profile after "
                            + PROFILE_UPSERT_ATTEMPTS + " attempts: " + e.getMessage());
                }
                logger.log("Attempt " + attempt + " to upsert the passport profile failed: "
                        + e.getMessage() + ". Retrying...");
                Thread.sleep(PROFILE_RETRY_BACKOFF_MILLIS);
            }
        }
    }

    /**
     * Removes the saved passengers a passport test created. Any failure is logged and swallowed
     * so that a cleanup problem cannot mask the result of the test that is finishing.
     */
    public void deletePassportProfileQuietly(String username, String password, String firstName, String lastName) {
        deleteProfileByNameQuietly(username, password, firstName, lastName);
    }

    /**
     * Removes the saved passengers matching a name, swallowing any failure so that a cleanup
     * problem cannot mask the result of the test that is finishing. Pass an empty last name to
     * clean up a mononym profile.
     */
    public void deleteProfileByNameQuietly(String username, String password, String firstName, String lastName) {
        try {
            Unm<T> unm = new Unm<>(session);
            LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
            deleteMatchingProfiles(authData.accessToken, authData.accountId, firstName, lastName);
        } catch (Exception e) {
            logger.log("Could not clean up the passport profile for " + firstName + " " + lastName
                    + ": " + e.getMessage());
        }
    }

    private void createIndonesianPassportProfile(
            String username,
            String password,
            String firstName,
            String lastName,
            String salutation,
            String passportNumber,
            String passportIssuedDate,
            String passportIssuingCountry,
            int expiryOffsetDays
    ) throws ExecutionException, InterruptedException {
        Unm<T> unm = new Unm<>(session);
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String accessToken = authData.accessToken;
        logger.log("Upserting Indonesian passport profile on account " + authData.accountId);

        deleteMatchingProfiles(accessToken, authData.accountId, firstName, lastName);

        String expiryDate = getJakartaDateYYYY_MM_DD(expiryOffsetDays);
        String issuingCountry = passportIssuingCountry == null || passportIssuingCountry.isBlank()
                ? DEFAULT_PASSPORT_ISSUING_COUNTRY : passportIssuingCountry;
        CreateProfileApi.Inputs createInputs = new CreateProfileApi.Inputs(
                accessToken, firstName, lastName, salutation, issuingCountry,
                passportNumber, expiryDate, passportIssuedDate, issuingCountry);
        CreateProfileResponse createProfileResponseData = createProfile(createInputs);
        logger.log("Upserted passport profile status: " + createProfileResponseData.message);
        if (createProfileResponseData.data == null) {
            throw new IllegalStateException("Create profile returned no data: "
                    + createProfileResponseData.message + " errors=" + createProfileResponseData.errors);
        }
        waitUntilProfileIsReadable(
                accessToken,
                createProfileResponseData.data.id,
                firstName,
                lastName,
                passportNumber,
                expiryDate);
    }

    private void deleteMatchingProfiles(String accessToken, long accountId, String firstName, String lastName)
            throws ExecutionException, InterruptedException {
        GetSmartProfileApi.Inputs inputs = new GetSmartProfileApi.Inputs(accessToken);
        for (SmartProfileResponse.Data profile : matchingProfiles(getSmartProfiles(inputs), firstName, lastName)) {
            logger.log("Deleting existing profile " + profile.id + " for " + firstName + " " + lastName);
            deleteProfile(accessToken, accountId, profile.id);
        }
    }

    /**
     * Polls until the created passenger is readable with the expected passport fields, and any
     * same-name row from a delayed delete has gone. Matching on name alone can succeed on stale
     * data while the profile service is still catching up.
     */
    private void waitUntilProfileIsReadable(
            String accessToken,
            int profileId,
            String firstName,
            String lastName,
            String passportNumber,
            String expiryDate
    ) throws ExecutionException, InterruptedException {
        GetSmartProfileApi.Inputs inputs = new GetSmartProfileApi.Inputs(accessToken);
        for (int attempt = 1; attempt <= PROFILE_VISIBILITY_ATTEMPTS; attempt++) {
            List<SmartProfileResponse.Data> sameName =
                    matchingProfiles(getSmartProfiles(inputs), firstName, lastName);
            boolean createdPassportIsVisible = sameName.stream()
                    .anyMatch(profile -> isCreatedPassport(profile, profileId, passportNumber, expiryDate));
            boolean staleSameNameProfilesGone = sameName.stream()
                    .allMatch(profile -> profile.id == profileId);
            if (createdPassportIsVisible && staleSameNameProfilesGone) {
                return;
            }
            Thread.sleep(PROFILE_VISIBILITY_POLL_MILLIS);
        }
        throw new IllegalStateException("Created passport profile id " + profileId + " for "
                + firstName + " " + lastName + " is still not readable from the profile service");
    }

    private boolean isCreatedPassport(
            SmartProfileResponse.Data profile,
            int profileId,
            String passportNumber,
            String expiryDate) {
        return profile.id == profileId
                && passportNumber.equalsIgnoreCase(profile.passportNumber)
                && expiryDate.equals(profile.passportExpiryDate);
    }

    /**
     * The non-primary saved passengers whose first and last name match, ignoring case. A missing
     * last name and an empty one are treated as the same thing, so that a mononym profile - which
     * the service stores with a null last name - is still found by a cleanup asking for "".
     */
    private List<SmartProfileResponse.Data> matchingProfiles(
            ArrayList<SmartProfileResponse.Data> profiles, String firstName, String lastName) {
        if (profiles == null) {
            return List.of();
        }
        return profiles.stream()
                .filter(profile -> !profile.isPrimary
                        && firstName.equalsIgnoreCase(profile.accountFirstName)
                        && blankSafe(lastName).equalsIgnoreCase(blankSafe(profile.accountLastName)))
                .toList();
    }

    private static String blankSafe(String value) {
        return value == null ? "" : value.trim();
    }

    private CountryListPojoResponse.CountryData getNationId() {
        GETunmCountryListApi.Inputs inputs = new GETunmCountryListApi.Inputs();
        GETunmCountryListApi getunmCountryListApi = new GETunmCountryListApi(inputs);
        ApiResponse getunmCountryListApiResponse = getunmCountryListApi.hitApi(apiTools);
        Gson gson = new Gson();
        CountryListPojoResponse countryListPojoResponse = gson.fromJson(getunmCountryListApiResponse.getBody(), CountryListPojoResponse.class);
        return countryListPojoResponse.getRandomCountryName();
    }

    /**
     * Delete smart profile by ID. If ID is 0, delete all smart profiles except the first one.
     * @param username UNM username
     * @param password UNM password
     * @param id Smart profile ID to delete, or 0 to delete all except the first one
     */
    @SneakyThrows
    public void deleteProfileAPI(
            String username, String password, long id
    ){
        Unm<T> unm = new Unm<>(session);
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String accessToken = authData.accessToken;
        boolean loginStatus = authData.accountUsername.equals(username);
        logger.log("loginStatus-> " + username + ", " + loginStatus);
        long accountId = authData.accountId;
        logger.log("Account ID: "+ accountId);

        if(id==0){
            GetSmartProfileApi.Inputs getSmartProfilesInputs = new GetSmartProfileApi.Inputs(accessToken);
            ArrayList<SmartProfileResponse.Data> getSmartProfilesResponse = getSmartProfiles(getSmartProfilesInputs);
            if(getSmartProfilesResponse == null ){
                return;
            }

            int noOfSmartProfile = getSmartProfilesResponse.size();
            logger.log("No of smart profile: "+noOfSmartProfile);
            for(int x =1; x<noOfSmartProfile && x<100; x++){
                SmartProfileResponse.Data profileData = getSmartProfilesResponse.get(x);
                logger.log("Deleting smart profile name: "+profileData.accountFirstName+" "+profileData.accountLastName);
                id = profileData.id;
                DeleteProfileResponse deleteResponse = deleteProfile(accessToken,accountId,id);
                logger.log(deleteResponse.message);
                Thread.sleep(100);
            }
        }else {
            DeleteProfileResponse deleteResponse = deleteProfile(accessToken,accountId,id);
            logger.log(deleteResponse.message);
        }
    }

    private DeleteProfileResponse deleteProfile(String accessToken,long accountId,long profileId) throws ExecutionException, InterruptedException{
        DeleteProfileApi.Inputs deleteProfileApiInputs = new DeleteProfileApi.Inputs(accessToken, accountId, profileId);
        DeleteProfileApi deleteProfileApi = new DeleteProfileApi(deleteProfileApiInputs);
        ApiResponse deleteProfileResponse = deleteProfileApi.hitApi(apiTools);

        Gson gson = new Gson();

        return gson.fromJson(deleteProfileResponse.getBody(), DeleteProfileResponse.class);
    }
}
