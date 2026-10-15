package com.tiket.page.member.smart_profile;

import com.google.gson.Gson;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.member.CreateProfileApi;
import com.tiket.api.member.DeleteProfileApi;
import com.tiket.api.member.GetSmartProfileApi;
import com.tiket.apiPage.Unm;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.member.CreateProfileResponse;
import com.tiket.pojo.member.DeleteProfileResponse;
import com.tiket.pojo.member.SmartProfileResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public abstract class SmartProfilePage<T extends WebDriver> extends BasePage<T> {
    public SmartProfilePage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> SmartProfilePage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new SmartProfilePageAndroid<>(session);}
            case IOS -> {return new SmartProfilePageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public enum ActionType {
        ADD,
        DELETE,
        EDIT
    }

    public record PassportInfo(String passportNumber, String issueDate, String expiryDate, String country) {}

    public abstract String addPassengerData();
    public abstract Boolean isSnackbarShown(ActionType action);
    public abstract String getSnackbarText();
    public abstract void openSmartProfile(String name);
    public abstract void openSmartProfile(int profileIndex);
    public abstract void deleteSmartProfile();
    public abstract void editSmartProfile(String newName);
    public abstract boolean findProfile(String name);
    public abstract String getOwnerName();
    public abstract String getProfileName(int profileIndex);
    public abstract PassportInfo getProfilePassportInfo(int profileIndex);
    public abstract PassportInfo changeProfilePassportInfo(int profileIndex);
    public abstract void selectRandomCountry();

    @SneakyThrows
    public void deleteProfileAPI(
            String username, String password, int delay, int limit
    ){
        Unm<T> unm = new Unm<>(session);
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String accessToken = authData.accessToken;
        boolean loginStatus = authData.accountUsername.equals(username);
        logger.log("loginStatus-> " + username + ", " + loginStatus);
        long accountId = authData.accountId;
        logger.log("Account ID: "+ accountId);

        GetSmartProfileApi.Inputs getSmartProfilesInputs = new GetSmartProfileApi.Inputs(accessToken);
        ArrayList<SmartProfileResponse.Data> getSmartProfilesResponse = getSmartProfiles(getSmartProfilesInputs);
        if(getSmartProfilesResponse == null ){
            return;
        }
        int noOfSmartProfile = getSmartProfilesResponse.size();
        logger.log("No of smart profile: "+noOfSmartProfile);
        for(int x =1; x<noOfSmartProfile && x<limit; x++){
            SmartProfileResponse.Data profileData = getSmartProfilesResponse.get(x);
            logger.log("Deleting smart profile name: "+profileData.accountFirstName+" "+profileData.accountLastName);
            long id = profileData.id;
            DeleteProfileResponse deleteResponse = deleteProfile(accessToken,accountId,id);
            logger.log(deleteResponse.message);
            Thread.sleep(delay);
        }
    }

    @SneakyThrows
    public void addProfileAPI(
            String username, String password, String firstName, String lastName, String salutation
    ){
        Unm<T> unm = new Unm<>(session);
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String accessToken = authData.accessToken;
        boolean loginStatus = authData.accountUsername.equals(username);
        logger.log("loginStatus-> " + username + ", " + loginStatus);
        long accountId = authData.accountId;
        logger.log("Account ID: "+ accountId);

        CreateProfileApi.Inputs createProfileApiInputs = new CreateProfileApi.Inputs(accessToken,firstName,lastName,salutation,"");
        CreateProfileResponse createProfileResponseData = createProfile(createProfileApiInputs);
        logger.log("Created smart profile status: "+createProfileResponseData.message);
        logger.log("Created smart profile with name: "+createProfileResponseData.data.accountSalutationName+" "+createProfileResponseData.data.accountFirstName+" "+createProfileResponseData.data.accountLastName);
    }

    private CreateProfileResponse createProfile (CreateProfileApi.Inputs inputs) throws ExecutionException, InterruptedException {
        CreateProfileApi createProfileApi = new CreateProfileApi(inputs);
        ApiResponse createProfileResponse = createProfileApi.hitApi(apiTools);
        Gson gson = new Gson();
        CreateProfileResponse createProfileResponseData = gson.fromJson(createProfileResponse.getBody(), CreateProfileResponse.class);

        return createProfileResponseData;
    }

    private DeleteProfileResponse deleteProfile(String accessToken,long accountId,long profileId) throws ExecutionException, InterruptedException{
        DeleteProfileApi.Inputs deleteProfileApiInputs = new DeleteProfileApi.Inputs(accessToken, accountId, profileId);
        DeleteProfileApi deleteProfileApi = new DeleteProfileApi(deleteProfileApiInputs);
        ApiResponse deleteProfileResponse = deleteProfileApi.hitApi(apiTools);

        Gson gson = new Gson();
        DeleteProfileResponse deleteProfileApiResponseData = gson.fromJson(deleteProfileResponse.getBody(), DeleteProfileResponse.class);

        return deleteProfileApiResponseData;
    }

    private String getToken() throws ExecutionException, InterruptedException {
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer "+accessToken;
    }

    private ArrayList<SmartProfileResponse.Data> getSmartProfiles(GetSmartProfileApi.Inputs inputs) throws ExecutionException, InterruptedException {
        GetSmartProfileApi getSmartProfileApi = new GetSmartProfileApi(inputs);
        ApiResponse smartProfileApiResponse = getSmartProfileApi.hitApi(apiTools);
        Gson gson = new Gson();
        SmartProfileResponse smartProfileApiResponseData = gson.fromJson(smartProfileApiResponse.getBody(), SmartProfileResponse.class);

        return smartProfileApiResponseData.data;
    }
}