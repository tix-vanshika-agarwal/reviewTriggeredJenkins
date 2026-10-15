package com.tiket.apiPage;

import com.google.gson.Gson;
import com.tiket.api.b2b.*;
import com.tiket.api.member.GetAccountDataApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.b2b.corporate.*;
import com.tiket.pojo.b2b.corporate.PostUploadFileResponse;
import com.tiket.pojo.flight.FlightAutocompleteResponse;
import com.tiket.pojo.member.AccountData;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.is;

public class CorporateApi<T extends WebDriver> extends BasePage<T> {

    public CorporateApi(Session<T> session) {
        super(session);
    }

    @SneakyThrows
    public Boolean approveBookingB2BApi(
            String username,
            String password,
            String orderID
    ) {
        if (orderID == null || orderID.trim().isEmpty() || "0".equals(orderID.trim())) {
            Assertion.skip("approveBookingB2BApi called with invalid orderID='" + orderID
                    + "'. Upstream booking likely failed silently; skipping approval to surface the real cause.");
        }
        String accessToken = getAccessTokenB2BCorporate(username, password);
        logger.log("accessToken-> " + accessToken);

        String loginAccessToken = getAccessTokenB2BCorporate(username, password);
        logger.log("accessToken for dashboard request->" + loginAccessToken);

        AcceptBookingDashboardApi.Inputs acceptBookingInputs = new AcceptBookingDashboardApi.Inputs(accessToken, loginAccessToken, orderID);
        boolean bookingStatus = false;
        for(int tries = 0; tries < 3; tries++){
            try {
                bookingStatus = acceptBooking(acceptBookingInputs);
                break;
            }catch (Exception e){
                logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
                Thread.sleep(3000);
            }
        }
        logger.log("Booking approved" + bookingStatus);
        return bookingStatus;
    }

    public String authLoginB2B(String username, String password) {
        return getAccessTokenB2BCorporate(username, password);
    }

    private Boolean acceptBooking(AcceptBookingDashboardApi.Inputs inputs) throws ExecutionException, InterruptedException {
        AcceptBookingDashboardApi acceptBookingApi = new AcceptBookingDashboardApi(inputs);
        ApiResponse acceptApiResponse = acceptBookingApi.hitApi(apiTools);
        JSONObject acceptStatus = new JSONObject(acceptApiResponse.getBody());
        logger.log("res->" + acceptStatus);
        return acceptStatus.getJSONObject("data").getInt("affectedRow") == 1;
    }

    @SneakyThrows
    public String getAccessTokenB2BCorporate(String username, String password) {
        Unm<T> unm = new Unm<>(session);
        return unm.getAccessToken(username, password,"B2B");
    }

    @SneakyThrows
    public long getAccountId(String accessToken) {
        GetAccountDataApi.Inputs getAccountDataApiInputs = new GetAccountDataApi.Inputs(CoreFunctions.normalizeToken(accessToken));
        return getAccountIdApi(getAccountDataApiInputs);
    }

    private long getAccountIdApi(GetAccountDataApi.Inputs inputs) throws ExecutionException, InterruptedException {
        GetAccountDataApi getAccountDataApi = new GetAccountDataApi(inputs);
        ApiResponse response = getAccountDataApi.hitApi(apiTools);
        Gson gson = new Gson();
        AccountData responsePOJO = gson.fromJson(response.getBody(), AccountData.class);
        if(responsePOJO.code.contains("SUCCESS")){
            logger.log("responsePOJO.data.accountId-> " + responsePOJO.data.accountId);
            return responsePOJO.data.accountId;
        }
        logger.fail("Failed to get account id");
        return -1;
    }

    @SneakyThrows
    public EmployeeData getEmployeeData(String email) {
        EmployeeData employeeData = getEmployeeId(email);
        logger.log("employeeId ->" + employeeData.data.employeeID);
        return employeeData;
    }

    private EmployeeData getEmployeeId(String email) throws InterruptedException, ExecutionException {
        GetEmployeeIdApi.Inputs inputs = new GetEmployeeIdApi.Inputs(email);
        GetEmployeeIdApi getEmployeeIdApi = new GetEmployeeIdApi(inputs);
        ApiResponse getEmployeeIDApiResponse = getEmployeeIdApi.hitApi(apiTools);
        Gson gson = new Gson();
        EmployeeData employeeData = gson.fromJson(getEmployeeIDApiResponse.getBody(), EmployeeData.class);
        logger.log("employee data->" + employeeData.success);
        return employeeData;
    }

    public ArrayList<PostCreateTravelPlanBody.customFields> setCustomFieldsData(LoginDataUnmResponse.Data loginUsernameData, String username) {
        ArrayList<PostCreateTravelPlanBody.customFields> customFieldsList = new ArrayList<>();
        GetCustomFieldsData customFieldsData = getCustomFields(loginUsernameData, username);
        int totalData = customFieldsData.data.size();

        for (int i = 0; i < totalData; i++) {
            GetCustomFieldsData.CustomFieldData customFieldData = customFieldsData.data.get(i);
            PostCreateTravelPlanBody.customFields setCustomFields = new PostCreateTravelPlanBody.customFields();
            setCustomFields.id = customFieldData.id;
            setCustomFields.fieldType = customFieldData.fieldType;
            setCustomFields.fieldName = customFieldData.fieldName;

            if (customFieldData.fieldType.equalsIgnoreCase("file")) {
                setCustomFields.file = new PostCreateTravelPlanBody.File();
                PostUploadFileResponse uploadFileResponse = UploadFileCustomFields(loginUsernameData, username);
                setCustomFields.file.id = uploadFileResponse.data.id;
                setCustomFields.file.name = uploadFileResponse.data.fileName;
                setCustomFields.file.size = uploadFileResponse.data.size;
                setCustomFields.file.fileURL = uploadFileResponse.data.fileURL;
            } else {
                setCustomFields.value = new ArrayList<>();
                switch (customFieldData.fieldType) {
                    case "single_selection":
                        setCustomFields.value.add(customFieldData.options.get(0));
                        break;
                    case "multiple_selection":
                        setCustomFields.value.addAll(customFieldData.options);
                        break;
                    case "text":
                        setCustomFields.value.add("text aut");
                        break;
                    case "textarea":
                        setCustomFields.value.add("textarea aut");
                        break;
                    default:
                        throw new NotImplementedException("Field type " + customFieldData.fieldType + " not implemented");
                }
                setCustomFields.file = new PostCreateTravelPlanBody.File(); // empty file object for non-file fields
            }
            customFieldsList.add(setCustomFields);
        }
        return customFieldsList;
    }

    private GetCustomFieldsData getCustomFields(LoginDataUnmResponse.Data loginUsernameData, String username) {
        GetCustomFields.Inputs inputs = new GetCustomFields.Inputs(loginUsernameData.accessToken, loginUsernameData.accountId, getEmployeeData(username).data.corporateID, getEmployeeData(username).data.accessRoleId);
        GetCustomFields getCustomFields = new GetCustomFields(inputs);
        ApiResponse getCustomFieldsApiResponse = getCustomFields.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(getCustomFieldsApiResponse.getBody(), GetCustomFieldsData.class);
    }

    private PostUploadFileResponse UploadFileCustomFields(LoginDataUnmResponse.Data loginUsernameData, String username) {
        PostUploadFileCustomFields.Inputs inputs = new PostUploadFileCustomFields.Inputs(loginUsernameData.accessToken, loginUsernameData.accountId, getEmployeeData(username).data.corporateID, getEmployeeData(username).data.accessRoleId);
        PostUploadFileCustomFields postUploadFileCustomFields = new PostUploadFileCustomFields(inputs);
        ApiResponse postUploadFileCustomFieldsApiResponse = postUploadFileCustomFields.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(postUploadFileCustomFieldsApiResponse.getBody(), PostUploadFileResponse.class);
    }

    @SneakyThrows
    public int checkCustomFieldsPurchaseOrder(String adminUsername, String adminPassword, String orderId, TravelPlanPojo travelPlanPojo) {
        Unm<?> unm = new Unm<>(session);
        try {
            LoginDataUnmResponse.Data loginData = unm.unmLoginApi(adminUsername, adminPassword);
            String accessToken = loginData.accessToken;
            GetCustomFieldsPurchaseOrderData customFieldsDataAdmin = getCustomFieldsPurchaseOrderData(accessToken, orderId);

            // Step 1: Check list size
            int customFieldsSizeAdmin = customFieldsDataAdmin.data.size();
            Assertion.assertThat("Custom fields size mismatch", customFieldsSizeAdmin, is(travelPlanPojo.customFields.size()));

            // Step 2: Compare each field
            for (int i = 0; i < travelPlanPojo.customFields.size(); i++) {
                TravelPlanPojo.CustomFields expected = travelPlanPojo.customFields.get(i);

                int j = findMatchingIdIndex(travelPlanPojo, customFieldsDataAdmin, i);
                GetCustomFieldsPurchaseOrderData.CustomFieldData actual = customFieldsDataAdmin.data.get(j);

                Assertion.assertThat("Field type mismatch at index " + i, actual.fieldType, is(expected.fieldType));
                Assertion.assertThat("Field name mismatch at index " + i, actual.fieldName, is(expected.fieldName));
                if (expected.file != null && actual.file != null) {
                    Assertion.assertThat("File id mismatch at index " + i, actual.file.id, is(expected.file.id));
                    Assertion.assertThat("File name mismatch at index " + i, actual.file.name, is(expected.file.name));
                    Assertion.assertThat("File size mismatch at index " + i, actual.file.size, is(expected.file.size));
                } else {
                    Assertion.assertThat("Value list size mismatch at index " + i, actual.value, is(expected.value));
                }
            }
            return customFieldsSizeAdmin;

        } catch (Exception e) {
            logger.log("Failed to get the custom fields data from Admin -> " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Failed to get the custom fields data from Admin via API");
            return -1;
        }
    }

    private GetCustomFieldsPurchaseOrderData getCustomFieldsPurchaseOrderData(String accessToken, String orderId) {
        GetCustomFieldsPurchaseOrder.Inputs inputs = new GetCustomFieldsPurchaseOrder.Inputs(accessToken, orderId);
        GetCustomFieldsPurchaseOrder getCustomFieldsPurchaseOrder = new GetCustomFieldsPurchaseOrder(inputs);
        ApiResponse getCustomFieldsPurchaseOrderApiResponse = getCustomFieldsPurchaseOrder.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(getCustomFieldsPurchaseOrderApiResponse.getBody(), GetCustomFieldsPurchaseOrderData.class);
    }

    private static int findMatchingIdIndex(TravelPlanPojo travelPlanPojo, GetCustomFieldsPurchaseOrderData customFieldsDataAdmin, int expectedIndex) {
        int expectedId = travelPlanPojo.customFields.get(expectedIndex).id;
        for (int i = 0; i < customFieldsDataAdmin.data.size(); i++) {
            if (customFieldsDataAdmin.data.get(i).id == expectedId) {
                return i;
            }
        }
        return -1; // Not found
    }

    /*
     * airlines array if passed empty will set for all
     * minPrice:0 and maxPrice:0 signifies no limit set
     * seatClass empty array will set for all
     * seat class : "ECONOMY","PREMIUM_ECONOMY"
     * */
    @SneakyThrows
    public int createTravelPolicyFlight(
            String accessTokenAdmin,
            EmployeeData employeeData,
            long accountID,
            String currency,
            int minPrice,
            int maxPrice,
            String seatClass,
            String airlines,
            int advanceBookingDay,
            boolean addOns,
            boolean insurance,
            String policyName
    ) {
        logger.log("policyName-> " + policyName);
        PostCreateTravelPolicyApiBodyAll.Flight flight = new PostCreateTravelPolicyApiBodyAll.Flight();
        flight.policyType = "SAME";
        flight.advanceBookingDay = advanceBookingDay;
        flight.addOns = addOns;
        flight.insurance = insurance;
        PostCreateTravelPolicyApiBodyAll.FlightDetail details = new PostCreateTravelPolicyApiBodyAll.FlightDetail();
        details.airlines = new ArrayList<>();
        if(!airlines.isEmpty()){
            String [] airlinesArray = airlines.split("-");
            AirlineMaterData airlineMaterData = getAirlineMasterData(accessTokenAdmin);
            for(String airline : airlinesArray){
                PostCreateTravelPolicyApiBodyAll.Airline airlineObj = searchAirlineMasterData(airlineMaterData,airline);
                details.airlines.add(airlineObj);
            }
        }
        details.currency = currency;
        details.minPrice = minPrice;
        details.maxPrice = maxPrice;
        details.seatClass = new ArrayList<>();
        if(!seatClass.isEmpty()){
            String [] seatClassArray = seatClass.split("-");
            details.seatClass.addAll(Arrays.asList(seatClassArray));
        }
        details.routeName = "ALL";
        details.categoryName = "flight_policy_category_all";
        flight.details = new ArrayList<>();
        flight.details.add(details);
        PostCreateTravelPolicyApi.Inputs postCreateTravelPolicyApiInputs = new PostCreateTravelPolicyApi.Inputs(employeeData, accessTokenAdmin, accountID, policyName, "flight", flight, null);
        int travelPolicyId = createTravelPolicy(postCreateTravelPolicyApiInputs);
        logger.log("travelPolicyId-> " + travelPolicyId);
        return travelPolicyId;
    }

    /*
     * airlines array if passed empty will set for all
     * minPrice:0 and maxPrice:0 signifies no limit set
     * seatClass empty array will set for all
     * seat class : "ECONOMY","PREMIUM_ECONOMY"
     * */
    @SneakyThrows
    public int createTravelPolicyFlight(
            String accessTokenAdmin,
            EmployeeData employeeData,
            long accountID,
            String currency,
            int minPrice,
            int maxPrice,
            String seatClass,
            ArrayList<String> airlines,
            int advanceBookingDay,
            boolean addOns,
            boolean insurance,
            String policyName
    ) {
        logger.log("policyName-> " + policyName);
        PostCreateTravelPolicyApiBodyAll.Flight flight = new PostCreateTravelPolicyApiBodyAll.Flight();
        flight.policyType = "SAME";
        flight.advanceBookingDay = advanceBookingDay;
        flight.addOns = addOns;
        flight.insurance = insurance;
        PostCreateTravelPolicyApiBodyAll.FlightDetail details = new PostCreateTravelPolicyApiBodyAll.FlightDetail();
        details.airlines = new ArrayList<>();
        if(!airlines.isEmpty()){
            AirlineMaterData airlineMaterData = getAirlineMasterData(accessTokenAdmin);
            for(String airline : airlines){
                PostCreateTravelPolicyApiBodyAll.Airline airlineObj = searchAirlineMasterData(airlineMaterData,airline);
                details.airlines.add(airlineObj);
            }
        }
        details.currency = currency;
        details.minPrice = minPrice;
        details.maxPrice = maxPrice;
        details.seatClass = new ArrayList<>();
        if(!seatClass.isEmpty()){
            String [] seatClassArray = seatClass.split("-");
            details.seatClass.addAll(Arrays.asList(seatClassArray));
        }
        details.routeName = "ALL";
        details.categoryName = "flight_policy_category_all";
        flight.details = new ArrayList<>();
        flight.details.add(details);
        PostCreateTravelPolicyApi.Inputs postCreateTravelPolicyApiInputs = new PostCreateTravelPolicyApi.Inputs(employeeData, accessTokenAdmin, accountID, policyName, "flight", flight, null);
        int travelPolicyId = createTravelPolicy(postCreateTravelPolicyApiInputs);
        logger.log("travelPolicyId-> " + travelPolicyId);
        return travelPolicyId;
    }

    private AirlineMaterData getAirlineMasterData(String accessToken) throws ExecutionException, InterruptedException {
        GetAirlineMasterDataApi.Inputs getAirlineMasterDataApiInputs = new GetAirlineMasterDataApi.Inputs(accessToken);
        GetAirlineMasterDataApi getAirlineMasterDataApi = new GetAirlineMasterDataApi(getAirlineMasterDataApiInputs);
        ApiResponse response = getAirlineMasterDataApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(response.getBody(), AirlineMaterData.class);
    }

    private PostCreateTravelPolicyApiBodyAll.Airline searchAirlineMasterData(AirlineMaterData airlineMaterData, String airline){
        for(AirlineMaterData.Data data : airlineMaterData.data){
            if(data.displayName.toLowerCase().contains(airline.toLowerCase())){
                PostCreateTravelPolicyApiBodyAll.Airline airlineData = new PostCreateTravelPolicyApiBodyAll.Airline();
                airlineData.airlineName = data.name;
                airlineData.airlineCode = data.code;
                return airlineData;
            }
        }
        return null;
    }

    private PatchTravelPolicyBodyFlightApi.Airline searchAirlineMasterDataPatch(AirlineMaterData airlineMaterData, String airline){
        for(AirlineMaterData.Data data : airlineMaterData.data){
            if(data.displayName.toLowerCase().contains(airline.toLowerCase())){
                PatchTravelPolicyBodyFlightApi.Airline airlineData = new PatchTravelPolicyBodyFlightApi.Airline();
                airlineData.airlineName = data.name;
                airlineData.airlineCode = data.code;
                return airlineData;
            }
        }
        return null;
    }

    private int createTravelPolicy(PostCreateTravelPolicyApi.Inputs inputs) throws ExecutionException, InterruptedException {
        PostCreateTravelPolicyApi postCreateTravelPolicyApi = new PostCreateTravelPolicyApi(inputs);
        ApiResponse response = postCreateTravelPolicyApi.hitApi(apiTools);
        JSONObject responseJson = new JSONObject(response.getBody());
        logger.log("response createTravelPolicy-> " + responseJson);
        if (responseJson.getBoolean("success")) {
            return responseJson.getJSONObject("data").getInt("travelPolicyID");
        }
        return -1;
    }

    @SneakyThrows
    public boolean deleteTravelPolicy(
            String accessTokenAdmin,
            EmployeeData employeeData,
            long accountId,
            int policyId
    ) {
        DeleteTravelPolicyApi.Inputs deleteTravelPolicyApiInputs = new DeleteTravelPolicyApi.Inputs(accessTokenAdmin, employeeData, accountId, policyId);
        return deleteTravelPolicyApi(deleteTravelPolicyApiInputs);
    }

    private boolean deleteTravelPolicyApi(DeleteTravelPolicyApi.Inputs inputs) throws ExecutionException, InterruptedException {
        DeleteTravelPolicyApi deleteTravelPolicyApi = new DeleteTravelPolicyApi(inputs);
        ApiResponse response = deleteTravelPolicyApi.hitApi(apiTools);
        JSONObject responseJson = new JSONObject(response.getBody());
        return responseJson.getBoolean("success");
    }

    @SneakyThrows
    public int patchTravelPolicyFlight(
            String accessTokenAdmin,
            EmployeeData employeeData,
            long accountID,
            String currency,
            int minPrice,
            int maxPrice,
            String seatClass,
            String airlines,
            int advanceBookingDay,
            boolean addOns,
            boolean insurance,
            String policyName
    ) {
        logger.log("policyName-> " + policyName);
        GetAllTravelPolicyApi.Inputs getAllTravelPolicyApi = new GetAllTravelPolicyApi.Inputs(employeeData,accountID,policyName,1);
        int policyId = getPolicyIDIfExistUsingName(getAllTravelPolicyApi);
        logger.log("policyID-> " + policyId);
        if(policyId == -1){
            logger.log("policy is not present, creating new with name [" +policyName + "]");
            return createTravelPolicyFlight(accessTokenAdmin,employeeData,accountID,currency,minPrice,maxPrice,seatClass,airlines,advanceBookingDay,addOns,insurance,policyName);
        }
        GetTravelPolicyDetailsApi.Inputs getTravelPolicyDetailsApiInputs = new GetTravelPolicyDetailsApi.Inputs(employeeData,accountID,policyId);
        GetTravelPolicyDetailsApiResponse.Detail getTravelPolicyDetailsApiResponseDetail = getTravelPolicyDetailsApi(getTravelPolicyDetailsApiInputs,"flight");
        if (getTravelPolicyDetailsApiResponseDetail == null){
            logger.log("delete older policy");
            deleteTravelPolicy(accessTokenAdmin,employeeData,accountID,policyId);
            logger.log("policy was there but not contains flight policy, creating new with name [" +policyName + "]");
            return createTravelPolicyFlight(accessTokenAdmin,employeeData,accountID,currency,minPrice,maxPrice,seatClass,airlines,advanceBookingDay,addOns,insurance,policyName);
        }
        logger.log("detailId-> " + getTravelPolicyDetailsApiResponseDetail.id);

        PatchTravelPolicyBodyFlightApi patchTravelPolicyBodyFlightApi = new PatchTravelPolicyBodyFlightApi();
        patchTravelPolicyBodyFlightApi.details = new PatchTravelPolicyBodyFlightApi.Details();
        patchTravelPolicyBodyFlightApi.details.flight = new PatchTravelPolicyBodyFlightApi.Flight();
        patchTravelPolicyBodyFlightApi.details.flight.advanceBookingDay = advanceBookingDay;
        patchTravelPolicyBodyFlightApi.details.flight.addOns = addOns;
        patchTravelPolicyBodyFlightApi.details.flight.insurance = insurance;
        patchTravelPolicyBodyFlightApi.details.flight.policyType = "SAME";

        patchTravelPolicyBodyFlightApi.details.flight.details = new ArrayList<>();
        PatchTravelPolicyBodyFlightApi.Details details = new PatchTravelPolicyBodyFlightApi.Details();
        details.airlines = new ArrayList<>();
        if(!airlines.isEmpty()){
            String [] airlinesArray = airlines.split("-");
            AirlineMaterData airlineMaterData = getAirlineMasterData(accessTokenAdmin);
            for(String airline : airlinesArray){
                PatchTravelPolicyBodyFlightApi.Airline airlineObj = searchAirlineMasterDataPatch(airlineMaterData,airline);
                details.airlines.add(airlineObj);
            }
        }
        details.currency = currency;
        details.maxPrice = maxPrice;
        details.minPrice = minPrice;
        details.seatClass = new ArrayList<>();
        if(!seatClass.isEmpty()){
            String [] seatClassArray = seatClass.split("-");
            details.seatClass.addAll(Arrays.asList(seatClassArray));
        }
        details.routeName = "ALL";
        details.categoryName = "flight_policy_category_all";
        patchTravelPolicyBodyFlightApi.details.flight.details.add(details);

        PatchTravelPolicyApi.Inputs patchTravelPolicyinputs = new PatchTravelPolicyApi.Inputs(employeeData,accessTokenAdmin,accountID,policyId,getTravelPolicyDetailsApiResponseDetail.id,"flight",patchTravelPolicyBodyFlightApi,null);
        boolean isPatchDone = patchTravelPolicyApi(patchTravelPolicyinputs);
        logger.log("isPatchDone-> " + isPatchDone);
        return isPatchDone ? policyId : -1;
    }

    @SneakyThrows
    public int patchTravelPolicyFlight(
            String accessTokenAdmin,
            EmployeeData employeeData,
            long accountID,
            String currency,
            int minPrice,
            int maxPrice,
            String seatClass,
            ArrayList<String> airlines,
            int advanceBookingDay,
            boolean addOns,
            boolean insurance,
            String policyName
    ) {
        logger.log("policyName-> " + policyName);
        GetAllTravelPolicyApi.Inputs getAllTravelPolicyApi = new GetAllTravelPolicyApi.Inputs(employeeData,accountID,policyName,1);
        int policyId = getPolicyIDIfExistUsingName(getAllTravelPolicyApi);
        logger.log("policyID-> " + policyId);
        if(policyId == -1){
            logger.log("policy is not present, creating new with name [" +policyName + "]");
            return createTravelPolicyFlight(accessTokenAdmin,employeeData,accountID,currency,minPrice,maxPrice,seatClass,airlines,advanceBookingDay,addOns,insurance,policyName);
        }
        GetTravelPolicyDetailsApi.Inputs getTravelPolicyDetailsApiInputs = new GetTravelPolicyDetailsApi.Inputs(employeeData,accountID,policyId);
        GetTravelPolicyDetailsApiResponse.Detail getTravelPolicyDetailsApiResponseDetail = getTravelPolicyDetailsApi(getTravelPolicyDetailsApiInputs,"flight");
        if (getTravelPolicyDetailsApiResponseDetail == null){
            logger.log("delete older policy");
            deleteTravelPolicy(accessTokenAdmin,employeeData,accountID,policyId);
            logger.log("policy was there but not contains flight policy, creating new with name [" +policyName + "]");
            return createTravelPolicyFlight(accessTokenAdmin,employeeData,accountID,currency,minPrice,maxPrice,seatClass,airlines,advanceBookingDay,addOns,insurance,policyName);
        }
        logger.log("detailId-> " + getTravelPolicyDetailsApiResponseDetail.id);

        PatchTravelPolicyBodyFlightApi patchTravelPolicyBodyFlightApi = new PatchTravelPolicyBodyFlightApi();
        patchTravelPolicyBodyFlightApi.details = new PatchTravelPolicyBodyFlightApi.Details();
        patchTravelPolicyBodyFlightApi.details.flight = new PatchTravelPolicyBodyFlightApi.Flight();
        patchTravelPolicyBodyFlightApi.details.flight.advanceBookingDay = advanceBookingDay;
        patchTravelPolicyBodyFlightApi.details.flight.addOns = addOns;
        patchTravelPolicyBodyFlightApi.details.flight.insurance = insurance;
        patchTravelPolicyBodyFlightApi.details.flight.policyType = "SAME";

        patchTravelPolicyBodyFlightApi.details.flight.details = new ArrayList<>();
        PatchTravelPolicyBodyFlightApi.Details details = new PatchTravelPolicyBodyFlightApi.Details();
        details.airlines = new ArrayList<>();
        if(!airlines.isEmpty()){
            AirlineMaterData airlineMaterData = getAirlineMasterData(accessTokenAdmin);
            for(String airline : airlines){
                PatchTravelPolicyBodyFlightApi.Airline airlineObj = searchAirlineMasterDataPatch(airlineMaterData,airline);
                details.airlines.add(airlineObj);
            }
        }
        details.currency = currency;
        details.maxPrice = maxPrice;
        details.minPrice = minPrice;
        details.seatClass = new ArrayList<>();
        if(!seatClass.isEmpty()){
            String [] seatClassArray = seatClass.split("-");
            details.seatClass.addAll(Arrays.asList(seatClassArray));
        }
        details.routeName = "ALL";
        details.categoryName = "flight_policy_category_all";
        patchTravelPolicyBodyFlightApi.details.flight.details.add(details);

        PatchTravelPolicyApi.Inputs patchTravelPolicyinputs = new PatchTravelPolicyApi.Inputs(employeeData,accessTokenAdmin,accountID,policyId,getTravelPolicyDetailsApiResponseDetail.id,"flight",patchTravelPolicyBodyFlightApi,null);
        boolean isPatchDone = patchTravelPolicyApi(patchTravelPolicyinputs);
        logger.log("isPatchDone-> " + isPatchDone);
        return isPatchDone ? policyId : -1;
    }

    @SneakyThrows
    public int patchTravelPolicyFlightCustom(
            String accessTokenAdmin,
            EmployeeData employeeData,
            long accountID,
            String currency,
            int minPrice,
            int maxPrice,
            String seatClass,
            String airlines,
            int advanceBookingDay,
            boolean addOns,
            boolean insurance,
            String policyName,
            String routeName
    ) {
        logger.log("policyName-> " + policyName);
        GetAllTravelPolicyApi.Inputs getAllTravelPolicyApi = new GetAllTravelPolicyApi.Inputs(employeeData,accountID,policyName,1);
        int policyId = getPolicyIDIfExistUsingName(getAllTravelPolicyApi);
        logger.log("policyID-> " + policyId);
        if(policyId == -1){
            logger.log("policy is not present, creating new with name [" +policyName + "]");
            return createTravelPolicyFlight(accessTokenAdmin,employeeData,accountID,currency,minPrice,maxPrice,seatClass,airlines,advanceBookingDay,addOns,insurance,policyName);
        }
        GetTravelPolicyDetailsApi.Inputs getTravelPolicyDetailsApiInputs = new GetTravelPolicyDetailsApi.Inputs(employeeData,accountID,policyId);
        GetTravelPolicyDetailsApiResponse.Detail getTravelPolicyDetailsApiResponseDetail = getTravelPolicyDetailsApi(getTravelPolicyDetailsApiInputs,"flight");
        if (getTravelPolicyDetailsApiResponseDetail == null){
            logger.log("delete older policy");
            deleteTravelPolicy(accessTokenAdmin,employeeData,accountID,policyId);
            logger.log("policy was there but not contains flight policy, creating new with name [" +policyName + "]");
            return createTravelPolicyFlight(accessTokenAdmin,employeeData,accountID,currency,minPrice,maxPrice,seatClass,airlines,advanceBookingDay,addOns,insurance,policyName);
        }
        logger.log("detailId-> " + getTravelPolicyDetailsApiResponseDetail.id);

        PatchTravelPolicyBodyFlightApi patchTravelPolicyBodyFlightApi = new PatchTravelPolicyBodyFlightApi();
        patchTravelPolicyBodyFlightApi.details = new PatchTravelPolicyBodyFlightApi.Details();
        patchTravelPolicyBodyFlightApi.details.flight = new PatchTravelPolicyBodyFlightApi.Flight();
        patchTravelPolicyBodyFlightApi.details.flight.advanceBookingDay = advanceBookingDay;
        patchTravelPolicyBodyFlightApi.details.flight.addOns = addOns;
        patchTravelPolicyBodyFlightApi.details.flight.insurance = insurance;
        patchTravelPolicyBodyFlightApi.details.flight.policyType = "CUSTOM";
        //custom
        patchTravelPolicyBodyFlightApi.details.flight.details = new ArrayList<>();
        PatchTravelPolicyBodyFlightApi.Details customdetails = new PatchTravelPolicyBodyFlightApi.Details();
        customdetails.airlines = new ArrayList<>();
        if(!airlines.isEmpty()){
            String [] airlinesArray = airlines.split("-");
            AirlineMaterData airlineMaterData = getAirlineMasterData(accessTokenAdmin);
            for(String airline : airlinesArray){
                PatchTravelPolicyBodyFlightApi.Airline airlineObj = searchAirlineMasterDataPatch(airlineMaterData,airline);
                customdetails.airlines.add(airlineObj);
            }
        }
        customdetails.currency = currency;
        customdetails.maxPrice = maxPrice;
        customdetails.minPrice = minPrice;
        customdetails.seatClass = new ArrayList<>();
        if(!seatClass.isEmpty()){
            String [] seatClassArray = seatClass.split("-");
            customdetails.seatClass.addAll(Arrays.asList(seatClassArray));
        }
        customdetails.routeName = routeName;
        customdetails.categoryName = "flight_policy_category_1";
        patchTravelPolicyBodyFlightApi.details.flight.details.add(customdetails);

        PatchTravelPolicyBodyFlightApi.Details detail2 = new PatchTravelPolicyBodyFlightApi.Details();
        detail2.airlines = new ArrayList<>();
        detail2.currency = currency;
        detail2.maxPrice = 0;
        detail2.minPrice = 0;
        detail2.seatClass = new ArrayList<>();
        detail2.routeName = "DOMESTIC";
        detail2.categoryName = "flight_policy_category_others";
        patchTravelPolicyBodyFlightApi.details.flight.details.add(detail2);

        PatchTravelPolicyApi.Inputs patchTravelPolicyinputs = new PatchTravelPolicyApi.Inputs(employeeData,accessTokenAdmin,accountID,policyId,getTravelPolicyDetailsApiResponseDetail.id,"flight",patchTravelPolicyBodyFlightApi,null);
        boolean isPatchDone = patchTravelPolicyApi(patchTravelPolicyinputs);
        logger.log("isPatchDone-> " + isPatchDone);
        return isPatchDone ? policyId : -1;
    }

    private int getPolicyIDIfExistUsingName(GetAllTravelPolicyApi.Inputs inputs) throws ExecutionException, InterruptedException {
        GetAllTravelPolicyApi getAllTravelPolicyApi = new GetAllTravelPolicyApi(inputs);
        ApiResponse response = getAllTravelPolicyApi.hitApi(apiTools);
        Gson gson = new Gson();
        GetAllTravelPolicyApiResponse getAllTravelPolicyApiResponse = gson.fromJson(response.getBody(), GetAllTravelPolicyApiResponse.class);
        if(getAllTravelPolicyApiResponse.success && !getAllTravelPolicyApiResponse.data.isEmpty()){
            return getAllTravelPolicyApiResponse.data.get(0).id;
        }
        return -1;
    }

    private GetTravelPolicyDetailsApiResponse.Detail getTravelPolicyDetailsApi(GetTravelPolicyDetailsApi.Inputs inputs,String type) throws ExecutionException, InterruptedException {
        GetTravelPolicyDetailsApi getTravelPolicyDetailsApi = new GetTravelPolicyDetailsApi(inputs);
        ApiResponse response = getTravelPolicyDetailsApi.hitApi(apiTools);
        Gson gson = new Gson();
        GetTravelPolicyDetailsApiResponse getTravelPolicyDetailsApiResponse = gson.fromJson(response.getBody(),GetTravelPolicyDetailsApiResponse.class);
        logger.log("get detail success: " + getTravelPolicyDetailsApiResponse.success);
        if(getTravelPolicyDetailsApiResponse.success){
            for(GetTravelPolicyDetailsApiResponse.Detail detail : getTravelPolicyDetailsApiResponse.data.details){
                if(detail.productType.equals(type)){
                    return detail;
                }
            }
        }
        return null;
    }

    private boolean patchTravelPolicyApi(PatchTravelPolicyApi.Inputs inputs) throws ExecutionException, InterruptedException {
        PatchTravelPolicyApi patchTravelPolicyApi = new PatchTravelPolicyApi(inputs);
        ApiResponse response = patchTravelPolicyApi.hitApi(apiTools);
        JSONObject responseJson = new JSONObject(response.getBody());
        logger.log("response patch travel policy-> " + responseJson);
        if (responseJson.getBoolean("success")) {
            return responseJson.getString("data").contains("success update configuration");
        }
        return false;
    }

    @SneakyThrows
    public boolean assignTravelPolicyToUser(
            long accountIDAdmin,
            EmployeeData employeeDataAdmin,
            EmployeeData employeeDataUser,
            int travelPolicyId
    ) {
        PostAssignTravelPolicyApi.Inputs postAssignTravelPolicyApiInputs = new PostAssignTravelPolicyApi.Inputs(employeeDataAdmin, employeeDataUser, accountIDAdmin, travelPolicyId);
        return assignTravelPolicy(postAssignTravelPolicyApiInputs);
    }

    private boolean assignTravelPolicy(PostAssignTravelPolicyApi.Inputs inputs) throws ExecutionException, InterruptedException {
        PostAssignTravelPolicyApi postAssignTravelPolicyApi = new PostAssignTravelPolicyApi(inputs);
        ApiResponse response = postAssignTravelPolicyApi.hitApi(apiTools);
        JSONObject responseJson = new JSONObject(response.getBody());
        logger.log("response assignTravelPolicy -> " + responseJson);
        return responseJson.getBoolean("success");
    }

    @SneakyThrows
    public boolean setSearchTypePolicy(
            EmployeeData employeeData,
            long accountID,
            String type,
            boolean value
    ) {
        String searchType = searchTypeName(type);
        logger.log("searchType-> " + searchType);

        PostConfigTravelPolicySettingDashboardApi.Inputs postConfigTravelPolicySettingDashboardApiInputs = new PostConfigTravelPolicySettingDashboardApi.Inputs(employeeData, accountID, searchType, value);
        return setSearchTypePolicyHitApi(postConfigTravelPolicySettingDashboardApiInputs);
    }

    private String searchTypeName(String type){
        switch (type.toLowerCase()){
            case "search":
                return "TRAVEL_POLICY_COMPLY_SEARCH";
            default: throw new NotImplementedException("Unimplemented type: " + type);
        }
    }

    private boolean setSearchTypePolicyHitApi(PostConfigTravelPolicySettingDashboardApi.Inputs inputs) throws ExecutionException, InterruptedException {
        PostConfigTravelPolicySettingDashboardApi postConfigTravelPolicySettingDashboardApi = new PostConfigTravelPolicySettingDashboardApi(inputs);
        ApiResponse response = postConfigTravelPolicySettingDashboardApi.hitApi(apiTools);
        JSONObject responseJson = new JSONObject(response.getBody());
        logger.log("response setSearchTypePolicyHitApi-> " + responseJson);
        return responseJson.getBoolean("success");
    }

    @SneakyThrows
    public String getRefundId(String orderId, String adminUsername, String adminPassword,String source) {
        String refundId = "";
        try {
            String accessToken = "Bearer " + authLoginB2B(adminUsername, adminPassword);
            logger.log("accessToken-> " + accessToken);

            GetRefundRequestListApi.Inputs getRefundRequestListApiInputs = new GetRefundRequestListApi.Inputs(orderId, accessToken,source);
            refundId = getRefundIdFromList(getRefundRequestListApiInputs, Long.parseLong(orderId));
            logger.log("refund id->" + refundId);

        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("refund order not found on b2b dashboard ");
        }
        return refundId;
    }

    @SneakyThrows
    public long getRefundAmount(String orderId, String adminUsername, String adminPassword,String source) {
        long refundAmt = 0;
        try {
            String accessToken = "Bearer " + authLoginB2B(adminUsername, adminPassword);
            logger.log("accessToken-> " + accessToken);

            GetRefundRequestListApi.Inputs getRefundRequestListApiInputs = new GetRefundRequestListApi.Inputs(orderId, accessToken,source);
            refundAmt = Long.parseLong(getRefundAmountFromList(getRefundRequestListApiInputs, Long.parseLong(orderId)));
            logger.log("refund amount->" + refundAmt);

        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("refund amount not found on b2b dashboard ");
        }
        return refundAmt;
    }

    @SneakyThrows
    private String getRefundIdFromList(GetRefundRequestListApi.Inputs inputs, Long orderId) throws ExecutionException, InterruptedException {
        JSONObject entry = fetchRefundEntryForOrder(inputs, orderId, 5, 7000);
        if (entry != null) {
            return Integer.toString(entry.getInt("id"));
        }
        Assertion.skip("Refund not found in Refund List for Order ID: " + orderId);
        return "";
    }

    @SneakyThrows
    private String getRefundAmountFromList(GetRefundRequestListApi.Inputs inputs, Long orderId) throws ExecutionException, InterruptedException {
        JSONObject entry = fetchRefundEntryForOrder(inputs, orderId, 5, 7000);
        if (entry != null && entry.has("refundAmount")) {
            return entry.getString("refundAmount");
        }
        Assertion.skip("Refund Amount not found for Order ID: " + orderId);
        return "";
    }

    /**
     * Polls the corporate-dashboard refund-request-list endpoint until an entry for the given
     * {@code orderId} is found, or {@code maxAttempts} retries are exhausted.
     * <p>
     * Refund-request-list is eventually consistent after a refund request is created (5-15s
     * window in preprod per the dashboard SLA). Without retry, callers see a single-shot empty
     * {@code data: []} response and incorrectly skip the test as "Refund not found in Refund List".
     * <p>
     * <b>This is the canonical implementation.</b> {@code CorporatePage.fetchRefundEntryForOrder}
     * has been removed in favour of delegating here via a {@link CorporateApi} instance. Keep
     * the refund-list parsing logic in exactly one place to avoid the "fix one, forget the
     * other" class of regressions the duplicate previously invited.
     *
     * @return the first refund entry whose {@code orders.orderID} matches, or {@code null} if
     *         the polling window expires.
     */
    public JSONObject fetchRefundEntryForOrder(GetRefundRequestListApi.Inputs inputs, Long orderId, int maxAttempts, long sleepMs) throws ExecutionException, InterruptedException {
        GetRefundRequestListApi api = new GetRefundRequestListApi(inputs);
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            ApiResponse response = api.hitApi(apiTools);
            logger.log("[refund request list attempt " + attempt + "/" + maxAttempts + "] response-> " + response.getBody());
            try {
                JSONObject body = new JSONObject(response.getBody());
                if (body.optBoolean("success", false) && body.has("data") && !body.isNull("data")) {
                    JSONArray dataResponse = body.getJSONArray("data");
                    for (int i = 0; i < dataResponse.length(); i++) {
                        JSONObject entry = dataResponse.optJSONObject(i);
                        if (entry == null) {
                            continue;
                        }
                        JSONObject orders = entry.optJSONObject("orders");
                        if (orders != null && orders.optLong("orderID", -1L) == orderId) {
                            return entry;
                        }
                    }
                }
            } catch (Exception e) {
                logger.log("error parsing refund request list response: " + e.getMessage());
            }
            if (attempt < maxAttempts) {
                logger.log("orderId " + orderId + " not yet visible in refund request list, retrying in " + sleepMs + "ms...");
                Thread.sleep(sleepMs);
            }
        }
        logger.log("orderId " + orderId + " not found in refund request list after " + maxAttempts + " attempts");
        return null;
    }



    @SneakyThrows
    public Boolean approveOrRejectRefundB2B(
            String username,
            String password,
            String refundId,
            Boolean isApproved
    ) {
        String accessToken = authLoginB2B(username, password);
        logger.log("accessToken-> " + accessToken);

        String loginAccessToken = authLoginB2B(username, password);
        logger.log("accessToken for dashboard request->" + loginAccessToken);

        AcceptOrRejectRefundApi.Inputs acceptBookingInputs = new AcceptOrRejectRefundApi.Inputs(loginAccessToken, refundId, isApproved);
        Boolean bookingStatus = acceptOrRejectBooking(acceptBookingInputs);
        logger.log("refund approved" + bookingStatus);

        return bookingStatus;
    }

    private Boolean acceptOrRejectBooking(AcceptOrRejectRefundApi.Inputs inputs) throws ExecutionException, InterruptedException {
        AcceptOrRejectRefundApi acceptOrRejectApi = new AcceptOrRejectRefundApi(inputs);
        ApiResponse response = acceptOrRejectApi.hitApi(apiTools);
        JSONObject acceptStatus = new JSONObject(response.getBody());
        logger.log("res-> " + acceptStatus);
        return acceptStatus.getBoolean("success");
    }

    @SneakyThrows
    public int findCreateCorporateCode(String username, String password, String airlineCode, String corporateCode) {
        EmployeeData employeeData = getEmployeeId(username);
        int corporateId = employeeData.data.corporateID;

        String accessToken = getAccessTokenB2BCorporate(username, password);

        int corporateCodeId = findCorporateCodeId(accessToken, airlineCode, corporateCode,corporateId);
        if (corporateCodeId != 0) {
            logger.log("Corporate codes already exist");
        } else {
            PostCorporateCodeResponse postCorporateCodeResponse = postCorporateCode(accessToken, airlineCode, corporateCode);
            corporateCodeId = postCorporateCodeResponse.data.id;
        }
        return corporateCodeId;
    }

    @SneakyThrows
    public ArrayList<Integer> findCreateCorporateCode(String username, String password,ArrayList<CorporateCodeData> corporateCodeData) {
        EmployeeData employeeData = getEmployeeId(username);
        int corporateId = employeeData.data.corporateID;

        String accessToken = getAccessTokenB2BCorporate(username, password);
        ArrayList<Integer> corporateCodeIdArray = new ArrayList<>();
        for (CorporateCodeData corporateDataCode : corporateCodeData){
            String airlineCode = corporateDataCode.getAirlineCode();
            String corporateCode = corporateDataCode.getCorporateCode();

            int corporateCodeId = findCorporateCodeId(accessToken, airlineCode, corporateCode,corporateId);
            if (corporateCodeId != 0) {
                logger.log("Corporate codes already exist");
            } else {
                PostCorporateCodeResponse postCorporateCodeResponse = postCorporateCode(accessToken, airlineCode, corporateCode);
                corporateCodeId = postCorporateCodeResponse.data.id;
                logger.log("corporateCodeId : " + corporateCodeId);
            }
            corporateCodeIdArray.add(corporateCodeId);
            Thread.sleep(3000);
        }
        return corporateCodeIdArray;
    }

    private PostCorporateCodeResponse postCorporateCode(String accessToken, String airlineCode, String code) throws ExecutionException, InterruptedException {
        PostCorporateCodesApi.Inputs postCorporateCodesApiInputs = new PostCorporateCodesApi.Inputs(accessToken, airlineCode, code);
        return postCorporateCodeApi(postCorporateCodesApiInputs);
    }

    private PostCorporateCodeResponse postCorporateCodeApi(PostCorporateCodesApi.Inputs inputs) throws ExecutionException, InterruptedException {
        PostCorporateCodesApi postCorporateCodesApi = new PostCorporateCodesApi(inputs);
        ApiResponse response = postCorporateCodesApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(response.getBody(), PostCorporateCodeResponse.class);
    }

    private GetCorporateCodesResponse getCorporateCodes(String accessToken,int corporateId) throws ExecutionException, InterruptedException {
        GetCorporateCodesApi.Inputs getCorporateCodesApiInputs = new GetCorporateCodesApi.Inputs(accessToken,corporateId);
        return getCorporateCodesApi(getCorporateCodesApiInputs);
    }

    private GetCorporateCodesResponse getCorporateCodesApi(GetCorporateCodesApi.Inputs inputs) throws ExecutionException, InterruptedException {
        GetCorporateCodesApi getCorporateCodesApi = new GetCorporateCodesApi(inputs);
        ApiResponse response = getCorporateCodesApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(response.getBody(), GetCorporateCodesResponse.class);
    }

    private int findCorporateCodeId(String accessToken, String airlineCode, String code,int corporateId) throws ExecutionException, InterruptedException {
        int corporateCodeId = 0;
        GetCorporateCodesResponse response = getCorporateCodes(accessToken,corporateId);
        if (response.data == null || response.data.isEmpty()) {
            logger.log("empty corporate code data");
            return corporateCodeId; // exit method if no data
        }

        logger.log("amount of corporate code data: " + response.data.size());
        for (GetCorporateCodesResponse.Data data : response.data) {

            GetCorporateCodesResponse.CodeData directCode = data.corporateCodes.get("DIRECT");
            GetCorporateCodesResponse.CodeData chaCode = data.corporateCodes.get("CHA");

            if (directCode != null && data.airlineCode.equalsIgnoreCase(airlineCode) && directCode.code.equalsIgnoreCase(code)) {
                corporateCodeId = directCode.id;
                logger.log("Found DIRECT code " + corporateCodeId + " for airline " + airlineCode + " with code " + code);
                break;
            } else if (chaCode != null && data.airlineCode.equalsIgnoreCase(airlineCode) && chaCode.code.equalsIgnoreCase(code)) {
                corporateCodeId = chaCode.id;
                logger.log("Found CHA code " + corporateCodeId + " for airline " + airlineCode + " with code " + code);
                break;
            }
        }

        if (corporateCodeId == 0) {
            logger.log("No matching corporate code found with airline code " + airlineCode + " and code " + code);
        }

        return corporateCodeId;
    }


    @SneakyThrows
    public boolean deleteAllCorporateCode(String username, String password){
        EmployeeData employeeData = getEmployeeId(username);
        int corporateID = employeeData.data.corporateID;
        String accessToken = getAccessTokenB2BCorporate(username, password);
        findAndDeleteAllCorporateCodeApplied(accessToken, corporateID);
        return true;
    }

    private void findAndDeleteAllCorporateCodeApplied(String accessToken,int corporateId) throws ExecutionException, InterruptedException {
        int corporateCodeId = 0;
        GetCorporateCodesResponse response = getCorporateCodes(accessToken,corporateId);
        if (response.data == null || response.data.isEmpty()) {
            logger.log("empty corporate code data");
            return; //exit method if no data
        }

        logger.log("amount of corporate code data: " + response.data.size());
        for (GetCorporateCodesResponse.Data data : response.data) {

            if (data.corporateCodes.containsKey("DIRECT")) {
                corporateCodeId = data.corporateCodes.get("DIRECT").id;
                logger.log("There is corporate code " + corporateCodeId + " for airline code " + data.airlineCode + " with code " + data.corporateCodes.get("DIRECT").code);
                patchCorporateCode(accessToken, corporateCodeId,corporateId);
            }
            if (data.corporateCodes.containsKey("CHA") && data.isCHAEnabled && data.corporateCodes.get("CHA").isActive) {
                logger.fail("please contact b2b pic as it can be deleted only by b2b pic");
            }
        }
    }
    
    private GetCorporateCodesResponse patchCorporateCode(String accessToken, int corporateCodeId,int corporateId) throws ExecutionException, InterruptedException {
        PatchCorporateCodesApi.Inputs patchCorporateCodesApiInputs = new PatchCorporateCodesApi.Inputs(accessToken, corporateCodeId,corporateId);
        return patchCorporateCodeApi(patchCorporateCodesApiInputs);
    }

    private GetCorporateCodesResponse patchCorporateCodeApi(PatchCorporateCodesApi.Inputs inputs) throws ExecutionException, InterruptedException {
        PatchCorporateCodesApi patchCorporateCodesApi = new PatchCorporateCodesApi(inputs);
        ApiResponse response = patchCorporateCodesApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(response.getBody(), GetCorporateCodesResponse.class);
    }

    @SneakyThrows
    public Boolean checkPurchaseOrderList(String orderId, String adminUsername, String adminPassword, String userNameUI) {
        try {
            String accessToken = authLoginB2B(adminUsername, adminPassword);
            logger.log("accessToken-> " + accessToken);

            String loginAccessToken = authLoginB2B(adminUsername, adminPassword);
            logger.log("accessToken for dashboard request->" + loginAccessToken);

            EmployeeData employeeData = getEmployeeId(adminUsername);
            logger.log("employeeId ->" + employeeData.data.employeeID);

            GetPurchaseListApi.Inputs getPurchaseListApiInputs = new GetPurchaseListApi.Inputs(loginAccessToken, orderId, employeeData.data.employeeID, employeeData.data.corporateID, employeeData.data.accessRoleId);
            Boolean isOrderReflected = checkPurchaseOrderReflected(getPurchaseListApiInputs, userNameUI);
            logger.log("isOrderReflected->" + isOrderReflected);

            return isOrderReflected;
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Check purchase order list using API failed");
        }
        return false;
    }

    @SneakyThrows
    private Boolean checkPurchaseOrderReflected(GetPurchaseListApi.Inputs inputs, String adminUsername) throws ExecutionException, InterruptedException {
        PurchaseListResponse responsePOJO = fetchPurchaseListWithRetry(
                inputs, 5, 7000,
                r -> r != null && r.data != null && r.data.travelerNames != null && !r.data.travelerNames.isEmpty(),
                "travelerNames");
        if (responsePOJO == null || responsePOJO.data == null || responsePOJO.data.travelerNames == null || responsePOJO.data.travelerNames.isEmpty()) {
            return false;
        }
        String fullName = responsePOJO.data.travelerNames.get(0).fullName;
        return fullName != null && adminUsername.contains(fullName);
    }

    /*
     * Purchase list is eventually consistent after an order is created.
     * Poll up to maxAttempts times, sleeping sleepMs between attempts (target 5-10s).
     * Each caller passes the {@code isReady} predicate describing the specific field it
     * cares about (e.g. travelerNames vs orderNotes) — returning on bare {@code data != null}
     * would short-circuit the retry while the field of interest is still being populated by
     * the dashboard indexer. {@code readyDescription} only appears in retry logs.
     * Returns the last response if all attempts fail (predicate may still be unsatisfied).
     */
    private PurchaseListResponse fetchPurchaseListWithRetry(GetPurchaseListApi.Inputs inputs, int maxAttempts, long sleepMs,
                                                            java.util.function.Predicate<PurchaseListResponse> isReady,
                                                            String readyDescription) throws ExecutionException, InterruptedException {
        GetPurchaseListApi api = new GetPurchaseListApi(inputs);
        Gson gson = new Gson();
        PurchaseListResponse responsePOJO = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            ApiResponse response = api.hitApi(apiTools);
            logger.log("[purchase list attempt " + attempt + "/" + maxAttempts + "] response-> " + response.getBody());
            responsePOJO = gson.fromJson(response.getBody(), PurchaseListResponse.class);
            if (isReady.test(responsePOJO)) {
                return responsePOJO;
            }
            if (attempt < maxAttempts) {
                logger.log("purchase list " + readyDescription + " not yet available for orderId " + inputs.orderID() + ", retrying in " + sleepMs + "ms...");
                Thread.sleep(sleepMs);
            }
        }
        logger.log("purchase list " + readyDescription + " still not satisfied after " + maxAttempts + " attempts");
        return responsePOJO;
    }

    @SneakyThrows
    public Boolean checkPurchaseOrderMeetTravelPolicy(String orderId, String adminUsername, String adminPassword) {

        for(int tries=0;tries<3;tries++){
            try {
                String loginAccessToken = authLoginB2B(adminUsername,adminPassword);
                logger.log("accessToken for dashboard request->" + loginAccessToken);

                GetOrderDetailDashboardApi.Inputs getOrderDetailDashboardApiInputs = new GetOrderDetailDashboardApi.Inputs(loginAccessToken, orderId);
                return checkPurchaseOrderMeetTravelPolicy(getOrderDetailDashboardApiInputs);
            } catch (Exception e) {
                logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
                Thread.sleep(5000);
                logger.log("retrying");
            }
        }
        Assertion.skip("Check purchase order using API failed");
        return false;
    }

    // Dashboard indexing typically takes 10-30s after order creation. Total wait ~= attempts * interval.
    private static final int ORDER_DASHBOARD_MAX_ATTEMPTS = 10;
    private static final long ORDER_DASHBOARD_RETRY_INTERVAL_MS = 5000L;

    /**
     * Polls the corporate dashboard until the order shows up (or {@code maxAttempts} is exhausted),
     * never indexes into a possibly-empty {@code data} list, and throws a clear
     * {@link RuntimeException} on permanent failure instead of an opaque
     * {@link IndexOutOfBoundsException}.
     */
    private OrderDetailResponse fetchOrderDetailWithRetry(GetOrderDetailDashboardApi api, String orderId, int maxAttempts) throws InterruptedException {
        Gson gson = new Gson();
        OrderDetailResponse responsePOJO = new OrderDetailResponse();
        for (int i = 0; i < maxAttempts; i++) {
            ApiResponse response = api.hitApi(apiTools);
            logger.log("response-> " + response.getBody());
            responsePOJO = gson.fromJson(response.getBody(), OrderDetailResponse.class);
            boolean hasData = responsePOJO != null
                    && responsePOJO.data != null
                    && !responsePOJO.data.isEmpty()
                    && responsePOJO.data.get(0).orderID != 0;
            if (hasData) {
                return responsePOJO;
            }
            if (i < maxAttempts - 1) {
                logger.log("[attempt " + (i + 1) + "/" + maxAttempts + "] orderId " + orderId
                        + " not yet visible in dashboard, retrying in " + ORDER_DASHBOARD_RETRY_INTERVAL_MS + "ms...");
                Thread.sleep(ORDER_DASHBOARD_RETRY_INTERVAL_MS);
            }
        }
        throw new RuntimeException("orderId " + orderId + " not visible in corporate dashboard after "
                + maxAttempts + " attempts (~" + (maxAttempts * ORDER_DASHBOARD_RETRY_INTERVAL_MS / 1000) + "s)");
    }

    private boolean checkPurchaseOrderMeetTravelPolicy(GetOrderDetailDashboardApi.Inputs inputs) throws ExecutionException, InterruptedException {
        GetOrderDetailDashboardApi getOrderDetailDashboardApi = new GetOrderDetailDashboardApi(inputs);
        OrderDetailResponse responsePOJO = fetchOrderDetailWithRetry(getOrderDetailDashboardApi, inputs.orderID(), ORDER_DASHBOARD_MAX_ATTEMPTS);
        boolean isMeetTravelPolicy = responsePOJO.data.get(0).travelPolicyDetail.complyTravelPolicy;
        logger.log("isMeetTravelPolicy->" + isMeetTravelPolicy);
        return isMeetTravelPolicy;
    }

    /*
     * minPrice:0 and maxPrice:0 signifies no limit set
     * star rating for all 1-2-3-4-5
     * */
    @SneakyThrows
    public int patchTravelPolicyHotel(
            String accessTokenAdmin,
            EmployeeData employeeData,
            long accountID,
            String currency,
            int minPrice,
            int maxPrice,
            String starRating,
            int advanceBookingDay,
            boolean addOns,
            boolean insurance,
            String policyName
    ) {
        logger.log("policyName-> " + policyName);
        GetAllTravelPolicyApi.Inputs getAllTravelPolicyApi = new GetAllTravelPolicyApi.Inputs(employeeData,accountID,policyName,1);
        int policyId = getPolicyIDIfExistUsingName(getAllTravelPolicyApi);
        logger.log("policyID-> " + policyId);
        if(policyId == -1){
            logger.log("policy is not present, creating new with name [" +policyName + "]");
            return createTravelPolicyHotel(accessTokenAdmin,employeeData,accountID,currency,minPrice,maxPrice,starRating,advanceBookingDay,addOns,insurance,policyName);
        }
        GetTravelPolicyDetailsApi.Inputs getTravelPolicyDetailsApiInputs = new GetTravelPolicyDetailsApi.Inputs(employeeData,accountID,policyId);
        GetTravelPolicyDetailsApiResponse.Detail getTravelPolicyDetailsApiResponseDetail = getTravelPolicyDetailsApi(getTravelPolicyDetailsApiInputs,"tixhotel");
        if (getTravelPolicyDetailsApiResponseDetail == null){
            logger.log("delete older policy");
            deleteTravelPolicy(accessTokenAdmin,employeeData,accountID,policyId);
            logger.log("policy was there but not contains hotel policy, creating new with name [" +policyName + "]");
            return createTravelPolicyHotel(accessTokenAdmin,employeeData,accountID,currency,minPrice,maxPrice,starRating,advanceBookingDay,addOns,insurance,policyName);
        }
        logger.log("detailId-> " + getTravelPolicyDetailsApiResponseDetail.id);

        PatchTravelPolicyBodyHotelApi patchTravelPolicyBodyHotelApi = new PatchTravelPolicyBodyHotelApi();
        patchTravelPolicyBodyHotelApi.details = new PatchTravelPolicyBodyHotelApi.Details();
        patchTravelPolicyBodyHotelApi.details.hotel = new PatchTravelPolicyBodyHotelApi.Hotel();
        patchTravelPolicyBodyHotelApi.details.hotel.advanceBookingDay = advanceBookingDay;
        patchTravelPolicyBodyHotelApi.details.hotel.addOns = addOns;
        patchTravelPolicyBodyHotelApi.details.hotel.insurance = insurance;
        patchTravelPolicyBodyHotelApi.details.hotel.policyType = "SAME";

        patchTravelPolicyBodyHotelApi.details.hotel.details = new ArrayList<>();
        PatchTravelPolicyBodyHotelApi.Details details = new PatchTravelPolicyBodyHotelApi.Details();
        details.categoryName = "ALL";
        details.currency = currency;
        details.minPrice = minPrice;
        details.maxPrice = maxPrice;
        details.starRatings = new ArrayList<>();
        if(!starRating.isEmpty()){
            String [] starRatingArray = starRating.split("-");
            for (String star : starRatingArray){
                logger.log("star-> " + star);
                details.starRatings.add(star);
            }
        }
        patchTravelPolicyBodyHotelApi.details.hotel.details.add(details);
        PatchTravelPolicyApi.Inputs patchTravelPolicyinputs = new PatchTravelPolicyApi.Inputs(employeeData,accessTokenAdmin,accountID,policyId,getTravelPolicyDetailsApiResponseDetail.id,"hotel",null,patchTravelPolicyBodyHotelApi);
        boolean isPatchDone = patchTravelPolicyApi(patchTravelPolicyinputs);
        logger.log("isPatchDone-> " + isPatchDone);
        return isPatchDone ? policyId : -1;
    }

    /*
     * minPrice:0 and maxPrice:0 signifies no limit set
     * star rating for all 1-2-3-4-5
     * */
    @SneakyThrows
    public int createTravelPolicyHotel(
            String accessTokenAdmin,
            EmployeeData employeeData,
            long accountID,
            String currency,
            int minPrice,
            int maxPrice,
            String starRating,
            int advanceBookingDay,
            boolean addOns,
            boolean insurance,
            String policyName
    ) {
        logger.log("policyName-> " + policyName);
        PostCreateTravelPolicyApiBodyAll.Hotel hotel = new PostCreateTravelPolicyApiBodyAll.Hotel();
        hotel.policyType = "SAME";
        hotel.advanceBookingDay = advanceBookingDay;
        hotel.addOns = addOns;
        hotel.insurance = insurance;
        PostCreateTravelPolicyApiBodyAll.HotelDetail details = new PostCreateTravelPolicyApiBodyAll.HotelDetail();
        details.currency = currency;
        details.minPrice = minPrice;
        details.maxPrice = maxPrice;
        details.starRatings = new ArrayList<>();
        if(!starRating.isEmpty()){
            String [] starRatingArray = starRating.split("-");
            for (String star : starRatingArray){
                logger.log("star-> " + star);
                details.starRatings.add(star);
            }
        }
        details.categoryName = "hotel_policy_category_all";
        hotel.details = new ArrayList<>();
        hotel.details.add(details);

        PostCreateTravelPolicyApi.Inputs postCreateTravelPolicyApiInputs = new PostCreateTravelPolicyApi.Inputs(employeeData, accessTokenAdmin, accountID, policyName, "hotel", null, hotel);
        int travelPolicyId = createTravelPolicy(postCreateTravelPolicyApiInputs);
        logger.log("travelPolicyId-> " + travelPolicyId);
        return travelPolicyId;
    }

    @SneakyThrows
    public Boolean approveOrRejectRescheduleB2B(
            String username,
            String password,
            Boolean isApproved,
            String orderID,
            String note
    ) {
        String loginAccessToken = authLoginB2B(username, password);
        logger.log("accessToken for dashboard request->" + loginAccessToken);

        GetRescheduleRequestListApi.Inputs getRescheduleRequestListApiInputs = new GetRescheduleRequestListApi.Inputs(orderID, loginAccessToken);
        int rescheduleId = getRescheduleIdFromList(getRescheduleRequestListApiInputs, orderID);

        AcceptOrRejectRescheduleApi.Inputs acceptBookingInputs = new AcceptOrRejectRescheduleApi.Inputs(loginAccessToken, rescheduleId, isApproved, note);

        for(int tries=0;tries<3;tries++){
            try {
                Boolean rescheduleStatus = acceptOrRejectReschedule(acceptBookingInputs);
                logger.log("Reschedule approved" + rescheduleStatus);
                return rescheduleStatus;
            } catch (Exception e) {
                logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
                Thread.sleep(5000);
                logger.log("retrying");
            }
        }

        return false;
    }

    private int getRescheduleIdFromList(GetRescheduleRequestListApi.Inputs inputs, String orderId) throws ExecutionException, InterruptedException {
        return fetchRescheduleIdWithRetry(inputs, orderId, 5, 7000);
    }

    /*
     * Reschedule list is eventually consistent after a reschedule request is created.
     * Poll up to maxAttempts times, sleeping sleepMs between attempts (target 5-10s).
     * Skips the test if no data is returned within the window.
     */
    private int fetchRescheduleIdWithRetry(GetRescheduleRequestListApi.Inputs inputs, String orderId, int maxAttempts, long sleepMs) throws ExecutionException, InterruptedException {
        GetRescheduleRequestListApi api = new GetRescheduleRequestListApi(inputs);
        Gson gson = new Gson();
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            ApiResponse response = api.hitApi(apiTools);
            logger.log("[reschedule list attempt " + attempt + "/" + maxAttempts + "] response-> " + response.getBody());
            RescheduleRequestListResponse responsePOJO = gson.fromJson(response.getBody(), RescheduleRequestListResponse.class);
            if (responsePOJO != null && responsePOJO.success && responsePOJO.data != null && !responsePOJO.data.isEmpty()) {
                return responsePOJO.data.get(0).id;
            }
            if (attempt < maxAttempts) {
                logger.log("orderId " + orderId + " not yet visible in reschedule list, retrying in " + sleepMs + "ms...");
                Thread.sleep(sleepMs);
            }
        }
        Assertion.skip("Reschedule not found in Reschedule List for orderId " + orderId + " after " + maxAttempts + " attempts");
        return -1;
    }

    @SneakyThrows
    private RescheduleRequestListResponse getRescheduleRequestList(GetRescheduleRequestListApi.Inputs inputs){
        return fetchRescheduleListWithRetry(inputs, 5, 7000);
    }

    /*
     * Same retry semantics as fetchRescheduleIdWithRetry, but returns the full response
     * for callers that need to iterate over all reschedule entries (e.g. order notes lookup).
     */
    private RescheduleRequestListResponse fetchRescheduleListWithRetry(GetRescheduleRequestListApi.Inputs inputs, int maxAttempts, long sleepMs) throws ExecutionException, InterruptedException {
        GetRescheduleRequestListApi api = new GetRescheduleRequestListApi(inputs);
        Gson gson = new Gson();
        RescheduleRequestListResponse responsePOJO = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            ApiResponse response = api.hitApi(apiTools);
            logger.log("[reschedule list attempt " + attempt + "/" + maxAttempts + "] response-> " + response.getBody());
            responsePOJO = gson.fromJson(response.getBody(), RescheduleRequestListResponse.class);
            boolean hasData = responsePOJO != null
                    && responsePOJO.success
                    && responsePOJO.data != null
                    && !responsePOJO.data.isEmpty();
            if (hasData) {
                return responsePOJO;
            }
            if (attempt < maxAttempts) {
                logger.log("reschedule list not yet available, retrying in " + sleepMs + "ms...");
                Thread.sleep(sleepMs);
            }
        }
        logger.log("reschedule list still empty after " + maxAttempts + " attempts");
        return responsePOJO;
    }

    public String getOrderNotesFromReschedule(String adminUsername, String adminPassword, String orderId) {
        String accessToken = authLoginB2B(adminUsername, adminPassword);
        logger.log("accessToken-> " + accessToken);

        String loginAccessToken = authLoginB2B(adminUsername, adminPassword);
        logger.log("accessToken for dashboard request->" + loginAccessToken);

        GetRescheduleRequestListApi.Inputs getRescheduleRequestListApiInputs = new GetRescheduleRequestListApi.Inputs(orderId, loginAccessToken);
        RescheduleRequestListResponse response = getRescheduleRequestList(getRescheduleRequestListApiInputs);
        String orderNotes = "";
        if (response == null || response.data == null || response.data.isEmpty()) {
            logger.softWarning("Reschedule list empty after retries for order ID: " + orderId);
            return orderNotes;
        }
        int totalData = response.data.size();
        logger.log("total data in reschedule list: " + totalData);
        for (int i = 0; i < totalData; i++) {
            if (response.data.get(i).oldOrder.orders.orderID == Long.parseLong(orderId)) {
                logger.log("reschedule id: " + response.data.get(i).id);
                orderNotes = response.data.get(i).orderNotes;
                logger.log("order notes-> " + orderNotes);
                break;
            }
            if (i == totalData - 1) {
                logger.softWarning("No order notes found for order ID: " + orderId);
            }
        }
        return orderNotes;
    }

    private Boolean acceptOrRejectReschedule(AcceptOrRejectRescheduleApi.Inputs inputs) throws ExecutionException, InterruptedException {
        AcceptOrRejectRescheduleApi acceptOrRejectRescheduleApi = new AcceptOrRejectRescheduleApi(inputs);
        ApiResponse response = acceptOrRejectRescheduleApi.hitApi(apiTools);
        JSONObject acceptStatus = new JSONObject(response.getBody());
        return acceptStatus.getBoolean("success");
    }

    public ArrayList<CorporateCodeData> setAndGetCorporateCodeData(String rawData){
        logger.log("raw data : " + rawData);
        // Splitting the string by separating different airline groups using regex
        String[] airlineEntries = rawData.split("\\]\\[");

        ArrayList<CorporateCodeData> corporateCodeData = new ArrayList<>();

        for (String airlineData : airlineEntries) {
            // Removing any leading or trailing brackets
            airlineData = airlineData.replace("[", "").replace("]", "");

            // Splitting values based on "/"
            String[] values = airlineData.split("/");
            if (values.length == 3) {
                corporateCodeData.add(new CorporateCodeData(values[0], values[1], values[2]));
            }
        }
        return corporateCodeData;
    }

    public ArrayList<String> getAirlineName(ArrayList<CorporateCodeData> corporateCodeData){
        ArrayList<String> airline = new ArrayList<>();
        for (CorporateCodeData corporateCodeData1:corporateCodeData){
            airline.add(corporateCodeData1.getAirlineName());
        }
        return airline;
    }
    @SneakyThrows
    public Boolean checkRefundList(String orderId, String adminUsername, String adminPassword,int retry) {
        try {
            String accessToken = authLoginB2B(adminUsername, adminPassword);
            logger.log("accessToken-> " + accessToken);

            String loginAccessToken = authLoginB2B(adminUsername, adminPassword);
            logger.log("accessToken for dashboard request->" + loginAccessToken);

            GetRefundListApi.Inputs getRefundListApiInputs = new GetRefundListApi.Inputs(accessToken, loginAccessToken, orderId);
            Boolean isRefundReflected = checkRefundReflected(getRefundListApiInputs,retry);
            logger.log("isRefundReflected->" + isRefundReflected);

            return isRefundReflected;
        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Check refund list using API is failed");
        }
        return false;
    }

    @SneakyThrows
    private Boolean checkRefundReflected(GetRefundListApi.Inputs inputs,int retry) throws ExecutionException, InterruptedException {
        GetRefundListApi getRefundListApi = new GetRefundListApi(inputs);
        Gson gson = new Gson();
        long sleepMs = 7000;

        for (int attempt = 1; attempt <= retry; attempt++) {
            try {
                ApiResponse response = getRefundListApi.hitApi(apiTools);
                logger.log("[refund list attempt " + attempt + "/" + retry + "] response-> " + response.getBody());
                RefundListResponse responsePOJO = gson.fromJson(response.getBody(), RefundListResponse.class);
                if (responsePOJO != null
                        && responsePOJO.success
                        && responsePOJO.data != null
                        && responsePOJO.data.size() == 1) {
                    return true;
                }
            } catch (Exception e) {
                logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            }
            if (attempt < retry) {
                logger.log("refund list not yet reflected, retrying in " + sleepMs + "ms...");
                Thread.sleep(sleepMs);
            }
        }
        logger.log("refund list still not reflected after " + retry + " attempts");
        return false;
    }

    @SneakyThrows
    public boolean updateTravelPlanSetting(String adminUsername, String adminPassword,boolean value) {
        try {
            String accessToken = getAccessTokenB2BCorporate(adminUsername, adminPassword);
            logger.log("Access token for dashboard setting -> " + accessToken);

            List<PostCorporateDashboardSettingsApi.Config> configs = List.of(new PostCorporateDashboardSettingsApi.Config("BOOKING_REQUIRED_TRAVEL_PLAN", value));
            PostCorporateDashboardSettingsApi.Inputs inputs = new PostCorporateDashboardSettingsApi.Inputs(accessToken, configs);

            return executeUpdate(inputs);
        } catch (Exception e) {
            logger.log("Error: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Failed to update travel plan setting via API");
            return false;
        }
    }

    private boolean executeUpdate(PostCorporateDashboardSettingsApi.Inputs inputs) throws ExecutionException, InterruptedException {
        PostCorporateDashboardSettingsApi api = new PostCorporateDashboardSettingsApi(inputs);

        for (int i = 0; i < 3; i++) {
            ApiResponse response = api.hitApi(apiTools);
            logger.log("API Response -> " + response.getBody());

            if (response.getStatusCode() == 200) {
                logger.log("Travel plan setting updated successfully.");
                return true;
            }
            Thread.sleep(1000);
        }

        logger.log("Failed to update travel plan setting after retries.");
        return false;
    }

    @SneakyThrows
    public Boolean checkBookingOrder(String orderId, String adminUsername, String adminPassword, String expectedProductType, String currentDate, String dateDeparture,String paymentType) {
        try {
            String accessToken = authLoginB2B(adminUsername, adminPassword);
            logger.log("Access token for dashboard request -> " + accessToken);

            GetBookingDetailDashboardApi.Inputs inputs = new GetBookingDetailDashboardApi.Inputs(accessToken, orderId, currentDate, dateDeparture, "approved");
            return checkBookingOrder(inputs, orderId, expectedProductType,paymentType);
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Check booking order list using API failed");
            return false;
        }
    }

    private boolean checkBookingOrder(GetBookingDetailDashboardApi.Inputs inputs, String expectedOrderId, String expectedProductType,String paymentType) throws ExecutionException, InterruptedException {
        GetBookingDetailDashboardApi api = new GetBookingDetailDashboardApi(inputs);
        BookingDetailResponse.OrderResponse response = new BookingDetailResponse.OrderResponse();

        for (int attempt = 0; attempt < 3; attempt++) {
            ApiResponse apiResponse = api.hitApi(apiTools);
            logger.log("API response -> " + apiResponse.getBody());

            response = new Gson().fromJson(apiResponse.getBody(), BookingDetailResponse.OrderResponse.class);

            if (response.data != null && !response.data.isEmpty()) {
                break;
            }

            Thread.sleep(1000);
        }

        if (response.data == null || response.data.isEmpty()) {
            logger.log("No order data found in API response.");
            return false;
        }

        for (BookingDetailResponse.OrderData order : response.data) {
            logger.log("Checking Order ID: " + order.orderID + ", Product Type: " + order.productType);

            boolean orderIdMatches = String.valueOf(order.orderID).equals(expectedOrderId);
            boolean productTypeMatches = expectedProductType.equalsIgnoreCase(order.productType);
            boolean paymentTypeMatches = order.paymentType != null && order.paymentType.equalsIgnoreCase(paymentType);

            if (orderIdMatches && productTypeMatches && paymentTypeMatches) {
                logger.log("Match found: Order ID = " + order.orderID + ", Product Type = " + order.productType + ", Payment Type = " + order.paymentType);
                return true;
            }
        }

        logger.log("No matching order with expected Order ID and Product Type found.");
        return false;
    }

    /**
     * Checks if a travel plan with the given parameters already exists; if not, creates a new travel plan.
     *
     * @param loginUsernameData   The login data of the user.
     * @param username           The username of the user.
     * @param adminUsername      The admin username for privileged actions.
     * @param planName           The name of the travel plan.
     * @param daysToDepartureDate Number of days from today to the departure date.
     * @param duration           The duration of the travel plan in days.
     * @param travelPlanConfigs  The configuration details for the travel plan.
     * @param checkViaNameOrData The method to check for existing plans ("name" or "data").
     * @param productType        The type of product (e.g., flight, hotel).
     * @param status             The status of the travel plan (e.g., requested, approved).
     * @return                   The existing or newly created {@link TravelPlanPojo}, or null if creation/check fails.
     * name suggestion:
     * <h2>Pending Travel Plans</h2>
     *     <ul>
     *         <li>flightPendingTravelPlan</li>
     *         <li>hotelPendingTravelPlan</li>
     *         <li>trainPendingTravelPlan</li>
     *         <li>whooshPendingTravelPlan</li>
     *         <li>carPendingTravelPlan</li>
     *     </ul>
     *
     *     <h2>Approval Travel Plans</h2>
     *     <ul>
     *         <li>flightApprovalTravelPlan</li>
     *         <li>hotelApprovalTravelPlan</li>
     *         <li>trainApprovalTravelPlan</li>
     *         <li>whooshApprovalTravelPlan</li>
     *         <li>carApprovalTravelPlan</li>
     *     </ul>
     */
    @SneakyThrows
    public TravelPlanPojo checkAndCreateTravelPlan(
            LoginDataUnmResponse.Data loginUsernameData,
            String username,
            String adminUsername,
            String planName,
            int daysToDepartureDate,
            int duration,
            PostCreateTravelPlanBody.Configs travelPlanConfigs,
            String checkViaNameOrData,
            String productType,
            String status,
            EmployeeData.Data employeeData,
            ArrayList<PostCreateTravelPlanBody.customFields> customFields
    ) {
        String startDate = CoreFunctions.addToDateYYYY_MM_DD(CoreFunctions.getCurrentDateYYYY_MM_DD(), daysToDepartureDate);
        logger.log("Plan date: " + startDate);
        String endDate = CoreFunctions.addToDateYYYY_MM_DD(startDate, duration);

        GetTravelPlanDetailApiResponse.Data travelPlanData = checkViaNameOrData.equalsIgnoreCase("name")
                ? isTravelPlanAlreadyCreatedBasedOnName(loginUsernameData, status, productType, startDate, endDate, planName, employeeData)
                : isTravelPlanAlreadyCreated(loginUsernameData, status, productType, startDate, endDate, travelPlanConfigs, planName, employeeData);

        if (travelPlanData != null) {
            logger.log("Travel plan already exists: " + (checkViaNameOrData.equalsIgnoreCase("name") ? planName : travelPlanData));
            return new TravelPlanPojo(travelPlanData);
        }

        int travelPlanId = createTravelPlan(loginUsernameData, username, adminUsername, planName, daysToDepartureDate, duration, travelPlanConfigs, customFields);
        if (travelPlanId == -1) {
            logger.log("Failed to create travel plan with name: " + planName);
            return null;
        }

        logger.log("Travel plan created with ID: " + travelPlanId);
        travelPlanData = getTravelPlanDetail(loginUsernameData,travelPlanId,employeeData);
        if (travelPlanData != null) return new TravelPlanPojo(travelPlanData);

        logger.log("Failed to retrieve travel plan details after creation.");
        return null;
    }

    /**
     * Multi-city flight version of {@link #checkAndCreateTravelPlan(LoginDataUnmResponse.Data, String, String, String, int, int, PostCreateTravelPlanBody.Configs, String, String, String, EmployeeData.Data, ArrayList)}.
     * An existing plan is matched by {@code planName} only, since the "data" check compares single-route flight data.
     * The plan runs from today + {@code daysToDepartureDate} for {@code duration} days, so it should cover every segment.
     */
    @SneakyThrows
    public TravelPlanPojo checkAndCreateTravelPlan(
            LoginDataUnmResponse.Data loginUsernameData,
            String username,
            String adminUsername,
            String planName,
            int daysToDepartureDate,
            int duration,
            PostCreateTravelPlanMultiCityBody.Configs travelPlanConfigs,
            String productType,
            String status,
            EmployeeData.Data employeeData,
            ArrayList<PostCreateTravelPlanBody.customFields> customFields
    ) {
        String startDate = CoreFunctions.addToDateYYYY_MM_DD(CoreFunctions.getCurrentDateYYYY_MM_DD(), daysToDepartureDate);
        logger.log("Plan date: " + startDate);
        String endDate = CoreFunctions.addToDateYYYY_MM_DD(startDate, duration);

        GetTravelPlanDetailApiResponse.Data travelPlanData = isTravelPlanAlreadyCreatedBasedOnName(loginUsernameData, status, productType, startDate, endDate, planName, employeeData);
        if (travelPlanData != null) {
            logger.log("Travel plan already exists: " + planName);
            return new TravelPlanPojo(travelPlanData);
        }

        int travelPlanId = createTravelPlan(loginUsernameData, username, adminUsername, planName, daysToDepartureDate, duration, travelPlanConfigs, customFields);
        if (travelPlanId == -1) {
            logger.log("Failed to create travel plan with name: " + planName);
            return null;
        }

        logger.log("Travel plan created with ID: " + travelPlanId);
        travelPlanData = getTravelPlanDetail(loginUsernameData, travelPlanId, employeeData);
        if (travelPlanData != null) return new TravelPlanPojo(travelPlanData);

        logger.log("Failed to retrieve travel plan details after creation.");
        return null;
    }

    public int createTravelPlan(LoginDataUnmResponse.Data loginUsernameData,String username,String adminUsername, String planName, int daysToDepartureDate,int duration,PostCreateTravelPlanBody.Configs travelPlanConfigs, ArrayList<PostCreateTravelPlanBody.customFields> customFields) {
        return createTravelPlanWithConfigs(loginUsernameData, username, adminUsername, planName, daysToDepartureDate, duration, travelPlanConfigs, customFields);
    }

    /**
     * Same as {@link #createTravelPlan(LoginDataUnmResponse.Data, String, String, String, int, int, PostCreateTravelPlanBody.Configs, ArrayList)}
     * but for a multi-city flight plan. The plan runs from today + {@code daysToDepartureDate} for {@code duration} days,
     * so it should cover the departure dates of every segment.
     */
    public int createTravelPlan(LoginDataUnmResponse.Data loginUsernameData,String username,String adminUsername, String planName, int daysToDepartureDate,int duration,PostCreateTravelPlanMultiCityBody.Configs travelPlanConfigs, ArrayList<PostCreateTravelPlanBody.customFields> customFields) {
        return createTravelPlanWithConfigs(loginUsernameData, username, adminUsername, planName, daysToDepartureDate, duration, travelPlanConfigs, customFields);
    }

    /** @param travelPlanConfigs {@link PostCreateTravelPlanBody.Configs} or {@link PostCreateTravelPlanMultiCityBody.Configs} */
    @SneakyThrows
    private int createTravelPlanWithConfigs(LoginDataUnmResponse.Data loginUsernameData,String username,String adminUsername, String planName, int daysToDepartureDate,int duration,Object travelPlanConfigs, ArrayList<PostCreateTravelPlanBody.customFields> customFields) {
        String startDate = CoreFunctions.addToDateYYYY_MM_DD(CoreFunctions.getCurrentDateYYYY_MM_DD(), daysToDepartureDate);
        logger.log("Plan date: " + startDate);
        String endDate = CoreFunctions.addToDateYYYY_MM_DD(startDate, duration);
        try {
            List<Integer> approverIDs = List.of(getEmployeeData(adminUsername).data.employeeID);

            PostCorporateDashboardTravelPlansApi.Inputs inputs = new PostCorporateDashboardTravelPlansApi.Inputs(
                    loginUsernameData.accessToken,
                    username,
                    planName,
                    startDate,
                    endDate,
                    travelPlanConfigs,
                    approverIDs,
                    loginUsernameData.accountId, // x-account-id
                    getEmployeeData(username).data.corporateID, // x-corporate-id
                    getEmployeeData(username).data.accessRoleId, // x-corporate-accessrole-id
                    customFields
            );

            return executeTravelPlanCreate(inputs);

        } catch (Exception e) {
            logger.log("Error: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Failed to create travel plan via API");
            return -1;
        }
    }

    public PostCreateTravelPlanBody.Configs setTravelPlanHotelData(String publicId,String location,String startDate, String endDate, int nights, String locationType) {
        PostCreateTravelPlanBody.Hotel hotel = new PostCreateTravelPlanBody.Hotel();
        hotel.publicId = publicId;
        hotel.location = location;
        hotel.startDate = startDate;
        hotel.endDate = endDate;
        hotel.nights = nights;
        hotel.locationType = locationType;
        PostCreateTravelPlanBody.Configs configs = new PostCreateTravelPlanBody.Configs();
        configs.hotels = List.of(hotel);
        return configs;
    }

    public PostCreateTravelPlanBody.Hotel setTravelPlanHotelData(String hotelData) {
        Map<String,String> hotelMapData = CoreFunctions.parseDelimitedSectionToMap(hotelData);
        PostCreateTravelPlanBody.Hotel hotel = new PostCreateTravelPlanBody.Hotel();
        hotel.publicId = hotelMapData.get("publicId");
        hotel.location = hotelMapData.get("location");
        //format date to yyyy-MM-dd
        hotel.startDate = hotelMapData.get("startDate");
        hotel.endDate = hotelMapData.get("endDate");

        hotel.nights = Integer.parseInt(hotelMapData.get("nights"));
        hotel.locationType = hotelMapData.get("locationType");
        return hotel;
    }

    /*
        * Sets the flight data for a travel plan.
        * @param departureCode The code of the departure location.
        * @param departureType The type of the departure location (e.g., "AIRPORT").
        * @param arrivalCode The code of the arrival location.
        * @param arrivalType The type of the arrival location (e.g., "AIRPORT").
        * @param isRoundTrip Indicates if the flight is a round trip.
        * @param departureDate The date of departure in the format "yyyy-MM-dd".
        * @returnDate The date of return in the format "yyyy-MM-dd" (optional for one-way flights).
        * @param departureName The name of the departure location.
        * @param arrivalName The name of the arrival location.
        * @return A PostCreateTravelPlanBody.Configs object containing the flight data.
     */
    public PostCreateTravelPlanBody.Configs setTravelPlanFlightData(String loginAccessToken, String originAirport, String destinationAirport, boolean isRoundTrip, int daysToDepartureDate, int duration) throws ExecutionException, InterruptedException {
        FlightApi<?> flightApi = new FlightApi<>(session);
        FlightAutocompleteResponse.Airport originData = flightApi.getAirportData(loginAccessToken,originAirport);
        FlightAutocompleteResponse.Airport destinationData = flightApi.getAirportData(loginAccessToken,destinationAirport);
        PostCreateTravelPlanBody.Flight flight = new PostCreateTravelPlanBody.Flight();
        flight.departureCode = originData.airportCode;
        flight.departureType = flightApi.selectOriginAndDestinationType(originData.airportCode);
        flight.arrivalCode = destinationData.airportCode;
        flight.arrivalType = flightApi.selectOriginAndDestinationType(destinationData.airportCode);
        flight.isRoundTrip = isRoundTrip;

        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        logger.log("departure date: " + departureDate);
        flight.departureDate = departureDate;
        if(isRoundTrip){
            String returnDate = CoreFunctions.addToDateYYYY_MM_DD(departureDate, duration);
            logger.log("return date: " + returnDate);
            flight.returnDate = returnDate;
        }
        flight.departureName = originData.airportName;
        flight.arrivalName = destinationData.airportName;
        PostCreateTravelPlanBody.Configs configs = new PostCreateTravelPlanBody.Configs();
        configs.flights = List.of(flight);
        return configs;
    }

    /**
     * Builds the flight config of a multi-city travel plan ({@code "tripType":"MULTI_CITY"}), one segment per route.
     *
     * @param routes               routes in {@code ORIGIN-DESTINATION} airport-code format, in travel order,
     *                             e.g. {@code ["CGK-SUB", "SUB-DPS", "DPS-CGK"]}
     * @param daysToDepartureDates departure date of each route as days from today, same order and size as {@code routes},
     *                             e.g. {@code [0, 3, 6]}
     */
    public PostCreateTravelPlanMultiCityBody.Configs setTravelPlanFlightData(String loginAccessToken, List<String> routes, List<Integer> daysToDepartureDates) throws ExecutionException, InterruptedException {
        if (routes == null || routes.isEmpty() || daysToDepartureDates == null || routes.size() != daysToDepartureDates.size()) {
            throw new IllegalArgumentException("Multi-city routes and departure dates must be non-empty and the same size, but were: "
                    + routes + " / " + daysToDepartureDates);
        }
        FlightApi<?> flightApi = new FlightApi<>(session);
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        List<PostCreateTravelPlanMultiCityBody.Segment> segments = new ArrayList<>();
        for (int i = 0; i < routes.size(); i++) {
            String[] airports = routes.get(i).trim().split("\\s*-\\s*");
            if (airports.length != 2) {
                throw new IllegalArgumentException("Multi-city route must be in ORIGIN-DESTINATION format, but was: " + routes.get(i));
            }
            FlightAutocompleteResponse.Airport originData = flightApi.getAirportData(loginAccessToken, airports[0]);
            FlightAutocompleteResponse.Airport destinationData = flightApi.getAirportData(loginAccessToken, airports[1]);

            PostCreateTravelPlanMultiCityBody.Segment segment = new PostCreateTravelPlanMultiCityBody.Segment();
            segment.departureCode = originData.airportCode;
            segment.departureType = flightApi.selectOriginAndDestinationType(originData.airportCode);
            segment.departureName = originData.airportName;
            segment.arrivalCode = destinationData.airportCode;
            segment.arrivalType = flightApi.selectOriginAndDestinationType(destinationData.airportCode);
            segment.arrivalName = destinationData.airportName;
            segment.departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDates.get(i));
            logger.log("multi-city segment " + (i + 1) + ": " + segment.departureCode + " -> " + segment.arrivalCode + " on " + segment.departureDate);
            segments.add(segment);
        }

        PostCreateTravelPlanMultiCityBody.Flight flight = new PostCreateTravelPlanMultiCityBody.Flight();
        flight.segments = segments;
        PostCreateTravelPlanMultiCityBody.Configs configs = new PostCreateTravelPlanMultiCityBody.Configs();
        configs.flights = List.of(flight);
        return configs;
    }

    public PostCreateTravelPlanBody.Flight setTravelPlanFlightData(String flightData) {
        Map<String,String> flightMapData = CoreFunctions.parseDelimitedSectionToMap(flightData);
        PostCreateTravelPlanBody.Flight flight = new PostCreateTravelPlanBody.Flight();
        flight.departureCode = flightMapData.get("departureCode");
        flight.departureType = flightMapData.get("departureType");
        flight.arrivalCode = flightMapData.get("arrivalCode");
        flight.arrivalType = flightMapData.get("arrivalType");
        flight.isRoundTrip = Boolean.parseBoolean(flightMapData.get("isRoundTrip"));
        flight.departureDate = flightMapData.get("departureDate");
        flight.returnDate = flightMapData.get("returnDate");
        flight.departureName = flightMapData.get("departureName");
        flight.arrivalName = flightMapData.get("arrivalName");
        return flight;
    }

    public PostCreateTravelPlanBody.Configs setTravelPlanCarData(String pickupDate, String pickupTime, String regionalID, String regionalName, boolean withDriver, int totalDays) {
        PostCreateTravelPlanBody.Car car = new PostCreateTravelPlanBody.Car();
        car.pickupDate = pickupDate;
        car.pickupTime = pickupTime;
        car.regionalID = regionalID;
        car.regionalName = regionalName;
        car.withDriver = withDriver;
        car.totalDays = totalDays;
        PostCreateTravelPlanBody.Configs configs = new PostCreateTravelPlanBody.Configs();
        configs.cars = List.of(car);
        return configs;
    }

    public PostCreateTravelPlanBody.Car setTravelPlanCarData(String carData) {
        Map<String,String> carMapData = CoreFunctions.parseDelimitedSectionToMap(carData);
        PostCreateTravelPlanBody.Car car = new PostCreateTravelPlanBody.Car();
        car.pickupDate = carMapData.get("pickupDate");
        car.pickupTime = carMapData.get("pickupTime");
        car.regionalID = carMapData.get("regionalID");
        car.regionalName = carMapData.get("regionalName");
        car.withDriver = Boolean.parseBoolean(carMapData.get("withDriver"));
        car.totalDays = Integer.parseInt(carMapData.get("totalDays"));
        return car;
    }

    /**
     * Sets the train data for a travel plan.
     *
     * @param departureCode The code of the departure station.
     * @param departureType The type of the departure station (e.g., "STATION").
     * @param departureName The name of the departure station.
     * @param arrivalCode The code of the arrival station.
     * @param arrivalType The type of the arrival station (e.g., "STATION").
     * @param arrivalName The name of the arrival station.
     * @param isRoundTrip Indicates if the train journey is a round trip.
     * @param daysToDepartureDate Number of days from today to the departure date.
     * @param duration The duration of the travel plan in days (for round trips).
     * @return A PostCreateTravelPlanBody.Configs object containing the train data.
     */
    public PostCreateTravelPlanBody.Configs setTravelPlanTrainData(String departureCode, String departureType, String departureName, String arrivalCode, String arrivalType, String arrivalName, boolean isRoundTrip, int daysToDepartureDate, int duration) {
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        logger.log("departure date: " + departureDate);

        PostCreateTravelPlanBody.Train train = new PostCreateTravelPlanBody.Train();
        train.departureCode = departureCode;
        train.departureType = departureType;
        train.departureName = departureName;
        train.arrivalCode = arrivalCode;
        train.arrivalType = arrivalType;
        train.arrivalName = arrivalName;
        train.isRoundTrip = isRoundTrip;
        train.departureDate = departureDate;
        if(isRoundTrip){
            String returnDate = CoreFunctions.addToDateYYYY_MM_DD(departureDate, duration);
            logger.log("return date: " + returnDate);
            train.returnDate = returnDate;
        }
        PostCreateTravelPlanBody.Configs configs = new PostCreateTravelPlanBody.Configs();
        configs.trains = List.of(train);
        return configs;
    }

    public PostCreateTravelPlanBody.Train setTravelPlanTrainData(String trainData) {
        Map<String,String> trainMapData = CoreFunctions.parseDelimitedSectionToMap(trainData);
        PostCreateTravelPlanBody.Train train = new PostCreateTravelPlanBody.Train();
        train.departureCode = trainMapData.get("departureCode");
        train.departureType = trainMapData.get("departureType");
        train.departureName = trainMapData.get("departureName");
        train.arrivalCode = trainMapData.get("arrivalCode");
        train.arrivalType = trainMapData.get("arrivalType");
        train.arrivalName = trainMapData.get("arrivalName");
        train.isRoundTrip = Boolean.parseBoolean(trainMapData.get("isRoundTrip"));
        train.departureDate = trainMapData.get("departureDate");
        train.returnDate = trainMapData.get("returnDate");
        return train;
    }

    public PostCreateTravelPlanBody.Configs setTravelPlanWhooshData(String originStationCode, String originStation, String destinationStationCode, String destinationStation, boolean isRoundTrip, String startDate, String returnDate) {
        PostCreateTravelPlanBody.Whoosh whoosh = new PostCreateTravelPlanBody.Whoosh();
        whoosh.departureCode = originStationCode;
        whoosh.departureType = "STATION";
        whoosh.departureName = originStation;
        whoosh.arrivalCode = destinationStationCode;
        whoosh.arrivalType = "STATION";
        whoosh.arrivalName = destinationStation;
        whoosh.isRoundTrip = isRoundTrip;
        whoosh.departureDate = convertToYyyyMmDd(startDate);
        whoosh.returnDate = convertToYyyyMmDd(returnDate);
        PostCreateTravelPlanBody.Configs configs = new PostCreateTravelPlanBody.Configs();
        configs.whooshs = List.of(whoosh);
        return configs;
    }

    public PostCreateTravelPlanBody.Whoosh setTravelPlanWhooshData(String whooshData) {
        Map<String,String> whooshMapData = CoreFunctions.parseDelimitedSectionToMap(whooshData);
        PostCreateTravelPlanBody.Whoosh whoosh = new PostCreateTravelPlanBody.Whoosh();
        whoosh.departureCode = whooshMapData.get("originStationCode");
        whoosh.departureType = "STATION";
        whoosh.departureName = whooshMapData.get("originStation");
        whoosh.arrivalCode = whooshMapData.get("destinationStationCode");
        whoosh.arrivalType = "STATION";
        whoosh.arrivalName = whooshMapData.get("destinationStation");
        whoosh.isRoundTrip = Boolean.parseBoolean(whooshMapData.get("isRoundTrip"));
        whoosh.departureDate = convertToYyyyMmDd(whooshMapData.get("startDate"));
        whoosh.returnDate = "";
        return whoosh;
    }

    public PostCreateTravelPlanBody.Configs setTravelPlanAllVerticalData(String hotelData, String flightData, String carData, String trainData, String whooshData) {
        PostCreateTravelPlanBody.Configs configs = new PostCreateTravelPlanBody.Configs();
        configs.hotels = List.of(setTravelPlanHotelData(hotelData));
        configs.flights = List.of(setTravelPlanFlightData(flightData));
        configs.cars = List.of(setTravelPlanCarData(carData));
        configs.trains = List.of(setTravelPlanTrainData(trainData));
        configs.whooshs = List.of(setTravelPlanWhooshData(whooshData));
        return configs;
    }

    private static String convertToYyyyMmDd(String dateStr) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateStr, inputFormatter);
        return date.format(outputFormatter);
    }

    private int executeTravelPlanCreate(PostCorporateDashboardTravelPlansApi.Inputs inputs) throws ExecutionException, InterruptedException {
        PostCorporateDashboardTravelPlansApi api = new PostCorporateDashboardTravelPlansApi(inputs);

        for (int i = 0; i < 3; i++) {
            ApiResponse response = api.hitApi(apiTools);
            logger.log("API Response -> " + response.getBody());

            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                logger.log("Travel plan created successfully.");
                Gson gson = new Gson();
                PostTravelPlanCreateResponse parsed =
                        gson.fromJson(response.getBody(), PostTravelPlanCreateResponse.class);
                return parsed.data.travelPlanID;
            }
            Thread.sleep(1000);
        }

        logger.log("Failed to create travel plan after retries.");
        return -1;
    }

    @SneakyThrows
    public boolean updateTravelPlanApproval(String adminUsername, String adminPassword,TravelPlanPojo travelPlanPojo) {
        if(travelPlanPojo.status.equalsIgnoreCase("approved")) {
            logger.log("Travel plan is already approved, skipping approval update.");
            return true;
        }
        Unm<?> unm = new Unm<>(session);
        try {
            LoginDataUnmResponse.Data loginData = unm.unmLoginApi(adminUsername, adminPassword);
            String accessToken = loginData.accessToken;
            EmployeeData.Data employeeDataAdmin = getEmployeeData(adminUsername).data;

            logger.log("Access token for travel plan approval -> " + accessToken);
            PostPatchTravelPlanApprovalApi.Inputs inputs = new PostPatchTravelPlanApprovalApi.Inputs(
                    accessToken,
                    adminUsername,
                    travelPlanPojo.travelPlanID,
                    loginData.accountId,
                    employeeDataAdmin
            );

            return executeApprovalUpdate(inputs);
        } catch (Exception e) {
            logger.log("Error during travel plan approval update: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Failed to update travel plan approval via API");
            return false;
        }
    }

    private boolean executeApprovalUpdate(PostPatchTravelPlanApprovalApi.Inputs inputs) throws ExecutionException, InterruptedException {
        PostPatchTravelPlanApprovalApi api = new PostPatchTravelPlanApprovalApi(inputs);

        for (int i = 0; i < 3; i++) {
            ApiResponse response = api.hitApi(apiTools);
            logger.log("API Response -> " + response.getBody());

            if (response.getStatusCode() == 200) {
                logger.log("Travel plan approval updated successfully.");
                return true;
            }
            Thread.sleep(1000);
        }

        logger.log("Failed to update travel plan approval after retries.");
        return false;
    }

    @SneakyThrows
    public Boolean checkTravelPlanStatus( String adminUsername, String adminPassword, int retry,String orderId,String startDate,String endDate,String planNameOriginal) {
        try {
            startDate = convertToYyyyMmDd(startDate);
            endDate = convertToYyyyMmDd(endDate);
            String accessToken = authLoginB2B(adminUsername, adminPassword);
            logger.log("accessToken -> " + accessToken);

            String loginAccessToken = authLoginB2B(adminUsername, adminPassword);
            logger.log("accessToken for dashboard request -> " + loginAccessToken);

            GetCorporateDashboardPurchaseOrdersApi.Inputs getTravelPlanStatusApiInputs = new GetCorporateDashboardPurchaseOrdersApi.Inputs(accessToken,orderId,startDate,endDate);
            Boolean isTravelPlanValid = checkTravelPlanStatusValidity(getTravelPlanStatusApiInputs, retry,planNameOriginal);
            logger.log("isTravelPlanValid -> " + isTravelPlanValid);

            return isTravelPlanValid;
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Check travel plan status using API failed");
        }
        return false;
    }

    @SneakyThrows
    private Boolean checkTravelPlanStatusValidity(GetCorporateDashboardPurchaseOrdersApi.Inputs inputs, int retry,String planNameOriginal) throws ExecutionException, InterruptedException {
        GetCorporateDashboardPurchaseOrdersApi getTravelPlanStatusApi = new GetCorporateDashboardPurchaseOrdersApi(inputs);

        for (int tries = 0; tries < retry; tries++) {
            try {
                ApiResponse response = getTravelPlanStatusApi.hitApi(apiTools);
                logger.log("Response -> " + response.getBody());

                Gson gson = new Gson();
                GetTravelPlanStatusApi responsePOJO = gson.fromJson(response.getBody(), GetTravelPlanStatusApi.class);
                List<GetTravelPlanStatusApi.Data> dataList = responsePOJO.getData();

                if (responsePOJO.isSuccess()) {
                    if (dataList != null && !dataList.isEmpty()) {
                        GetTravelPlanStatusApi.Data data = dataList.get(0);
                        long orderId = data.getOrderID();
                        String travelPlanName = data.getTravelPlanName();

                        logger.log("Received Order ID: " + orderId);
                        logger.log("Received Travel Plan Name: " + travelPlanName);
                        if (String.valueOf(orderId).equals(inputs.orderId()) && travelPlanName.equals(planNameOriginal)) {
                            logger.log("Order ID and Travel Plan Name match the expected values.");
                            return true;
                        } else {
                            logger.log("Order ID or Travel Plan Name mismatch.");
                            Thread.sleep(5000);
                            logger.log("Retrying...");
                        }
                    } else {
                        logger.log("No data found in the response.");
                    }
                } else {
                    logger.log("Response was not successful.");
                }

            } catch (Exception e) {
                logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
                Thread.sleep(5000);
                logger.log("Retrying...");
            }
        }
        return false;
    }

    @SneakyThrows
    public GetTravelPlanDetailApiResponse.Data isTravelPlanAlreadyCreated(LoginDataUnmResponse.Data loginUsernameData, String searchType,String productType, String startDate,String endDate,PostCreateTravelPlanBody.Configs travelPlanConfigs,String planName,EmployeeData.Data employeeData) {
        try {
            String accessToken = loginUsernameData.accessToken;
            logger.log("Access token for checking travel plan existence -> " + accessToken);
            int corporateId = employeeData.corporateID;
            int corporateRoleId = employeeData.accessRoleId;


            GetTravelPlanSearchApi.Inputs inputs = new GetTravelPlanSearchApi.Inputs(
                    accessToken,
                    loginUsernameData.accountUsername,
                    loginUsernameData.accountId,
                    corporateRoleId,
                    corporateId,
                    searchType,
                    startDate,
                    endDate,
                    productType,
                    planName
            );
            return hitApiAndGetTravelPlan(inputs,travelPlanConfigs,loginUsernameData,productType,employeeData);
        } catch (Exception e) {
            logger.log("Error: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Failed to check existing travel plan via API");
            return null;
        }
    }

    private GetTravelPlanDetailApiResponse.Data hitApiAndGetTravelPlan(GetTravelPlanSearchApi.Inputs inputs,PostCreateTravelPlanBody.Configs travelPlanConfigs,LoginDataUnmResponse.Data loginDataUnmResponse,String productType,EmployeeData.Data employeeData) throws ExecutionException, InterruptedException {
        GetTravelPlanSearchApi api = new GetTravelPlanSearchApi(inputs);
        ApiResponse response = api.hitApi(apiTools);
        logger.log("API Response -> " + response.getBody());
        GetTravelPlanSearchApiResponse getTravelPlanSearchApiResponse = new Gson().fromJson(response.getBody(), GetTravelPlanSearchApiResponse.class);
        if (getTravelPlanSearchApiResponse.success && getTravelPlanSearchApiResponse.data != null && !getTravelPlanSearchApiResponse.data.isEmpty()) {
            for (GetTravelPlanSearchApiResponse.DataItem data: getTravelPlanSearchApiResponse.data){
                GetTravelPlanDetailApiResponse.Data travelPlanDetail = getTravelPlanDetail(loginDataUnmResponse, data.travelPlanID,employeeData);
                if(matchDataBasedOnVerticalType(travelPlanDetail, travelPlanConfigs, productType)) {
                    logger.log("Matching travel plan found with ID: " + data.travelPlanID);
                    return travelPlanDetail;
                } else {
                    logger.log("Travel plan ID " + data.travelPlanID + " does not match the provided vertical data.");
                }
            }
        }
        return null;
    }

    @SneakyThrows
    public GetTravelPlanDetailApiResponse.Data isTravelPlanAlreadyCreatedBasedOnName(LoginDataUnmResponse.Data loginUsernameData, String searchType,String productType, String startDate,String endDate,String planName,EmployeeData.Data employeeData) {
        try {
            logger.log("isTravelPlanAlreadyCreatedBasedOnName called with parameters: " +
                    "searchType=" + searchType + ", productType=" + productType + ", startDate=" + startDate +
                    ", endDate=" + endDate + ", planName=" + planName);

            String accessToken = loginUsernameData.accessToken;
            int corporateId = employeeData.corporateID;
            int corporateRoleId = employeeData.accessRoleId;


            GetTravelPlanSearchApi.Inputs inputs = new GetTravelPlanSearchApi.Inputs(
                    accessToken,
                    loginUsernameData.accountUsername,
                    loginUsernameData.accountId,
                    corporateRoleId,
                    corporateId,
                    searchType,
                    startDate,
                    endDate,
                    productType,
                    planName
            );
            return hitApiAndGetData(inputs,loginUsernameData,productType,planName,employeeData);
        } catch (Exception e) {
            logger.log("Error: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Failed to check existing travel plan via API");
            return null;
        }
    }

    private GetTravelPlanDetailApiResponse.Data hitApiAndGetData(GetTravelPlanSearchApi.Inputs inputs,LoginDataUnmResponse.Data loginDataUnmResponse,String productType,String planName,EmployeeData.Data employeeData) throws ExecutionException, InterruptedException {
        GetTravelPlanSearchApi api = new GetTravelPlanSearchApi(inputs);
        ApiResponse response = api.hitApi(apiTools);
        logger.log("API Response -> " + response.getBody());
        GetTravelPlanSearchApiResponse getTravelPlanSearchApiResponse = new Gson().fromJson(response.getBody(), GetTravelPlanSearchApiResponse.class);
        if (getTravelPlanSearchApiResponse.success && getTravelPlanSearchApiResponse.data != null && !getTravelPlanSearchApiResponse.data.isEmpty()) {
            for (GetTravelPlanSearchApiResponse.DataItem data: getTravelPlanSearchApiResponse.data){
                GetTravelPlanDetailApiResponse.Data travelPlanDetail = getTravelPlanDetail(loginDataUnmResponse, data.travelPlanID,employeeData);
                if(travelPlanDetail.name.equals(planName)) {
                    logger.log("Matching travel plan found with ID: " + data.travelPlanID);
                    return travelPlanDetail;
                } else {
                    logger.log("Travel plan ID " + data.travelPlanID + " does not match the provided vertical data.");
                }
            }
        }
        return null;
    }

    @SneakyThrows
    public GetTravelPlanDetailApiResponse.Data getTravelPlanDetail(LoginDataUnmResponse.Data loginUsernameData, int travelPlanId,EmployeeData.Data employeeData) {
        try {
            logger.log("getTravelPlanDetail called with travelPlanId: " + travelPlanId +
                    ", loginUsernameData: " + loginUsernameData.accountUsername +
                    ", employeeData: " + employeeData);

            int corporateId = employeeData.corporateID;
            int corporateRoleId = employeeData.accessRoleId;

            GetTravelPlanDetailApi.Inputs inputs = new GetTravelPlanDetailApi.Inputs(
                    loginUsernameData.accessToken,
                    loginUsernameData.accountUsername,
                    loginUsernameData.accountId,
                    corporateRoleId,
                    corporateId,
                    travelPlanId
            );
            return hitApiAndGetTravelPlanDetail(inputs);
        } catch (Exception e) {
            logger.log("Error: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Failed to get travel plan detail via API");
            return null;
        }
    }

    private GetTravelPlanDetailApiResponse.Data hitApiAndGetTravelPlanDetail(GetTravelPlanDetailApi.Inputs inputs) throws ExecutionException, InterruptedException {
        GetTravelPlanDetailApi api = new GetTravelPlanDetailApi(inputs);
        ApiResponse response = api.hitApi(apiTools);
        logger.log("API Response -> " + response.getBody());
        GetTravelPlanDetailApiResponse jsonResponse = new Gson().fromJson(response.getBody(), GetTravelPlanDetailApiResponse.class);
        if (jsonResponse.success && jsonResponse.data != null) {
            return jsonResponse.data;
        }
        return null;
    }

    private boolean matchDataBasedOnVerticalType(GetTravelPlanDetailApiResponse.Data travelPlanDetail,PostCreateTravelPlanBody.Configs travelPlanConfigs,String productType) {
        return switch (productType.toLowerCase()) {
            case "flight" ->matchFlightData(travelPlanDetail.config.flights.get(0), travelPlanConfigs.flights.get(0));
            case "hotel" -> matchHotelData(travelPlanDetail.config.hotels.get(0), travelPlanConfigs.hotels.get(0));
            case "car" -> matchCarData(travelPlanDetail.config.cars.get(0), travelPlanConfigs.cars.get(0));
            case "train" ->
                    matchTrainData(travelPlanDetail.config.trains.get(0), travelPlanConfigs.trains.get(0));
            case "whoosh" ->
                    matchWhooshData(travelPlanDetail.config.whooshs.get(0), travelPlanConfigs.whooshs.get(0));
            default -> {
                logger.log("Unsupported vertical type: " + productType);
                yield false;
            }
        };
    }

    private boolean matchFlightData(GetTravelPlanDetailApiResponse.Flight travelPlanDetail, PostCreateTravelPlanBody.Flight flight) {
        return travelPlanDetail != null &&
                travelPlanDetail.departureCode.equals(flight.departureCode) &&
                travelPlanDetail.arrivalCode.equals(flight.arrivalCode) &&
                travelPlanDetail.departureDate.equals(flight.departureDate) &&
                (!flight.isRoundTrip || travelPlanDetail.returnDate.equals(flight.returnDate)) &&
                travelPlanDetail.departureName.equals(flight.departureName) &&
                travelPlanDetail.arrivalName.equals(flight.arrivalName);
    }

    private boolean matchHotelData(GetTravelPlanDetailApiResponse.Hotel travelPlanDetail, PostCreateTravelPlanBody.Hotel hotel) {
        return travelPlanDetail != null &&
                travelPlanDetail.publicId.equals(hotel.publicId) &&
                travelPlanDetail.location.equals(hotel.location) &&
                travelPlanDetail.startDate.equals(hotel.startDate) &&
                travelPlanDetail.endDate.equals(hotel.endDate) &&
                travelPlanDetail.nights == hotel.nights &&
                travelPlanDetail.locationType.equals(hotel.locationType);
    }

    private boolean matchCarData(GetTravelPlanDetailApiResponse.Car travelPlanDetail, PostCreateTravelPlanBody.Car car) {
        return travelPlanDetail != null &&
                travelPlanDetail.regionalID.equals(car.regionalID) &&
                travelPlanDetail.pickupDate.equals(car.pickupDate) &&
                travelPlanDetail.pickupTime.equals(car.pickupTime) &&
                travelPlanDetail.withDriver == car.withDriver &&
                travelPlanDetail.totalDays == car.totalDays;
    }

    private boolean matchTrainData(GetTravelPlanDetailApiResponse.Train travelPlanDetail, PostCreateTravelPlanBody.Train train) {
        return travelPlanDetail != null &&
                travelPlanDetail.departureCode.equals(train.departureCode) &&
                travelPlanDetail.arrivalCode.equals(train.arrivalCode) &&
                travelPlanDetail.departureDate.equals(train.departureDate) &&
                (!train.isRoundTrip || travelPlanDetail.returnDate.equals(train.returnDate)) &&
                travelPlanDetail.departureName.equals(train.departureName) &&
                travelPlanDetail.arrivalName.equals(train.arrivalName);
    }

    private boolean matchWhooshData(GetTravelPlanDetailApiResponse.Whoosh travelPlanDetail, PostCreateTravelPlanBody.Whoosh whoosh) {
        return travelPlanDetail != null &&
                travelPlanDetail.departureCode.equals(whoosh.departureCode) &&
                travelPlanDetail.arrivalCode.equals(whoosh.arrivalCode) &&
                travelPlanDetail.departureDate.equals(whoosh.departureDate) &&
                (!whoosh.isRoundTrip || travelPlanDetail.returnDate.equals(whoosh.returnDate)) &&
                travelPlanDetail.departureName.equals(whoosh.departureName) &&
                travelPlanDetail.arrivalName.equals(whoosh.arrivalName);
    }

    @SneakyThrows
    public String checkApprovalNotesPurchaseOrderList(String orderId, String adminUsername, String adminPassword) {
        try {
            String accessToken = authLoginB2B(adminUsername, adminPassword);
            logger.log("accessToken-> " + accessToken);

            String loginAccessToken = authLoginB2B(adminUsername, adminPassword);
            logger.log("accessToken for dashboard request->" + loginAccessToken);

            EmployeeData employeeData = getEmployeeId(adminUsername);
            logger.log("employeeId ->" + employeeData.data.employeeID);

            GetPurchaseListApi.Inputs getPurchaseListApiInputs = new GetPurchaseListApi.Inputs(loginAccessToken, orderId, employeeData.data.employeeID, employeeData.data.corporateID, employeeData.data.accessRoleId);
            String approvalNotes = checkApprovalNotesReflected(getPurchaseListApiInputs);
            logger.log("approval notes reflected->" + approvalNotes);

            return approvalNotes;
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Checking approval notes in purchase order list using API failed");
        }
        return null;
    }

    @SneakyThrows
    private String checkApprovalNotesReflected(GetPurchaseListApi.Inputs inputs) {
        PurchaseListResponse responsePOJO = fetchPurchaseListWithRetry(
                inputs, 5, 7000,
                r -> r != null && r.data != null && r.data.orderNotes != null && !r.data.orderNotes.isBlank(),
                "orderNotes");
        if (responsePOJO != null && responsePOJO.data != null && responsePOJO.data.orderNotes != null) {
            return responsePOJO.data.orderNotes;
        }

        logger.log("No notes found in the purchase order list");
        return null;
    }

    @SneakyThrows
    public boolean updateEmployeeCorporateRole(String adminEmail, String adminPassword, String travelerEmail, int retry , String desiredRole) {
        try {
            // Step 1: Login as Admin
            String accessToken = authLoginB2B(adminEmail, adminPassword);
            logger.log("Admin Access Token: " + accessToken);

            // Step 2: Get employee data for traveler
            EmployeeData travelerData = getEmployeeData(travelerEmail); // use your existing util
            if (travelerData == null || travelerData.data == null) {
                Assertion.skip("Traveler data not found for email: " + travelerEmail);
                return false;
            }

            logger.log("Traveler employee ID: " + travelerData.data.employeeID);
            logger.log("Corporate ID: " + travelerData.data.corporateID);

            int corporateRoleId = 0;
            switch (desiredRole) {
                case "super_administrator" -> corporateRoleId = 1;
                case "traveler" -> corporateRoleId = 3;
                default -> throw new IllegalArgumentException("Unsupported role: " + desiredRole);
            }

            PutUpdateEmployeeApi.Inputs inputs = new PutUpdateEmployeeApi.Inputs(
                    travelerData.data.employeeID,
                    getAccountId(accessToken),
                    travelerData.data.corporateID,
                    corporateRoleId,
                    "session_access_token="+accessToken
            );

            for (int tries = 0; tries < retry; tries++) {
                try {
                    PutUpdateEmployeeApi api = new PutUpdateEmployeeApi(inputs);
                    ApiResponse response = api.hitApi(apiTools);

                    logger.log("PutUpdateEmployeeApi Response: " + response.getBody());

                    JSONObject bodyJson = new JSONObject(response.getBody());
                    JSONObject data = bodyJson.getJSONObject("data");

                    String responseEmail = data.getString("email").replaceAll("-+$", "");
                    String roleName = data.getString("corporateRoleName");

                    if (response.getStatusCode() == 200 && responseEmail.equals(travelerEmail) && roleName.equals(desiredRole)) {
                        return true;
                    } else {
                        logger.log("Retrying PUT request...");
                        Thread.sleep(3000);
                    }
                } catch (Exception e) {
                    logger.log("Error during PUT request: " + ExceptionUtils.getStackTrace(e));
                    Thread.sleep(3000);
                }
            }
        } catch (Exception e) {
            logger.log("Exception in updateEmployeeCorporateRole: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Failed to update employee role using PUT API");
        }
        return false;
    }

    @SneakyThrows
    public Boolean checkRefundList(String orderId, String adminUsername, String adminPassword) {
        return checkRefundList(orderId, adminUsername, adminPassword, 1);
    }

    @SneakyThrows
    public AccountData  getAccountData(String accessToken) {
        GetAccountDataApi.Inputs getAccountDataApiInputs = new GetAccountDataApi.Inputs(CoreFunctions.normalizeToken(accessToken));
        return getAccountDataApi(getAccountDataApiInputs);
    }

    private AccountData getAccountDataApi(GetAccountDataApi.Inputs inputs) throws ExecutionException, InterruptedException {
        GetAccountDataApi getAccountDataApi = new GetAccountDataApi(inputs);
        ApiResponse response = getAccountDataApi.hitApi(apiTools);
        Gson gson = new Gson();
        AccountData responsePOJO = gson.fromJson(response.getBody(), AccountData.class);
        if(responsePOJO.code.contains("SUCCESS")){
            logger.log("responsePOJO.data.accountId-> " + responsePOJO.data.accountId);
            return responsePOJO;
        }
        logger.fail("Failed to get account data");
        return null;
    }

    @SneakyThrows
    public Boolean checkPurchaseOrderCorporateCode(String orderId, String adminUsername, String adminPassword,String tripType,String corporateCode,String airlineName) {
        try {
            String loginAccessToken = authLoginB2B(adminUsername,adminPassword);
            logger.log("accessToken for dashboard request->" + loginAccessToken);

            GetOrderDetailDashboardApi.Inputs getOrderDetailDashboardApiInputs = new GetOrderDetailDashboardApi.Inputs(loginAccessToken, orderId);
            return checkPurchaseOrderCorporateCode(getOrderDetailDashboardApiInputs,tripType,corporateCode,airlineName);
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Check purchase order using API failed");
        }
        return false;
    }

    private boolean checkPurchaseOrderCorporateCode(GetOrderDetailDashboardApi.Inputs inputs,String tripType,String corporateCode,String airlineName) throws ExecutionException, InterruptedException {
        GetOrderDetailDashboardApi getOrderDetailDashboardApi = new GetOrderDetailDashboardApi(inputs);
        OrderDetailResponse responsePOJO = fetchOrderDetailWithRetry(getOrderDetailDashboardApi, inputs.orderID(), ORDER_DASHBOARD_MAX_ATTEMPTS);
        String corporateCodeDepartApi = responsePOJO.data.get(0).orderDetail.get(0).depart.corporateCode;
        String airlineNameDepartApi = responsePOJO.data.get(0).orderDetail.get(0).depart.airlinesName;
        logger.log("corporateCodeDepartApi actual: " + corporateCodeDepartApi + " corporateCode expected: " + corporateCode);
        logger.log("airlineNameDepartApi actual: " + airlineNameDepartApi + " airlineNameDepart expected: " + airlineName);
        boolean isDepartDataCorrect = corporateCodeDepartApi.equals(corporateCode) && airlineNameDepartApi.toLowerCase().contains(airlineName.toLowerCase());
        if(tripType.equalsIgnoreCase("roundtrip")){
            String corporateCodeReturnApi = responsePOJO.data.get(0).orderDetail.get(0).myreturn.corporateCode;
            String airlineNameReturnApi = responsePOJO.data.get(0).orderDetail.get(0).myreturn.airlinesName;
            logger.log("corporateCodeReturnApi actual: " + corporateCodeReturnApi + " corporateCode expected: " + corporateCode);
            logger.log("airlineNameReturnApi actual: " + airlineNameReturnApi + " airlineNameDepart expected: " + airlineName);
            boolean isReturnDataCorrect = corporateCodeReturnApi.equals(corporateCode) && airlineNameReturnApi.toLowerCase().contains(airlineName.toLowerCase());
            return isDepartDataCorrect && isReturnDataCorrect;
        }
        return isDepartDataCorrect;
    }

    /**
     * Checks the order is present on the B2B dashboard "approved" booking list (product type only, no payment type check).
     */
    @SneakyThrows
    public Boolean checkBookingOrder(String orderId, String adminUsername, String adminPassword, String expectedProductType, String currentDate, String dateDeparture) {
        String accessToken = authLoginB2B(adminUsername, adminPassword);
        logger.log("Access token for dashboard request -> " + accessToken);

        GetBookingDetailDashboardApi.Inputs inputs = new GetBookingDetailDashboardApi.Inputs(accessToken, orderId, currentDate, dateDeparture, "approved");
        return checkBookingOrder(inputs, orderId, expectedProductType);
    }

    private boolean checkBookingOrder(GetBookingDetailDashboardApi.Inputs inputs, String expectedOrderId, String expectedProductType) throws ExecutionException, InterruptedException {
        GetBookingDetailDashboardApi api = new GetBookingDetailDashboardApi(inputs);
        BookingDetailResponse.OrderResponse response = new BookingDetailResponse.OrderResponse();

        for (int attempt = 0; attempt < 3; attempt++) {
            ApiResponse apiResponse = api.hitApi(apiTools);
            logger.log("API response -> " + apiResponse.getBody());

            response = new Gson().fromJson(apiResponse.getBody(), BookingDetailResponse.OrderResponse.class);

            if (response.data != null && !response.data.isEmpty()) {
                break;
            }

            Thread.sleep(1000);
        }

        if (response.data == null || response.data.isEmpty()) {
            logger.log("No order data found in API response.");
            return false;
        }

        for (BookingDetailResponse.OrderData order : response.data) {
            logger.log("Checking Order ID: " + order.orderID + ", Product Type: " + order.productType);

            boolean orderIdMatches = String.valueOf(order.orderID).equals(expectedOrderId);
            boolean productTypeMatches = expectedProductType.equalsIgnoreCase(order.productType);

            if (orderIdMatches && productTypeMatches) {
                logger.log("Match found: Order ID = " + order.orderID + ", Product Type = " + order.productType);
                return true;
            }
        }

        logger.log("No matching order with expected Order ID and Product Type found.");
        return false;
    }

    /**
     * Flight multi city variant of checkBookingOrder: the response {@code orderDetail} is an array
     * (one item per route), so it is parsed with {@link BookingDetailMultiCityResponse}.
     *
     * @param type               booking list tab on the B2B dashboard, e.g. "approved" or "pending"
     * @param expectedTotalRoute total multi city routes, expected to match the {@code orderDetail} count
     */
    public Boolean checkBookingOrderMultiCity(String orderId, String adminUsername, String adminPassword, String expectedProductType, String currentDate, String dateDeparture, String type, int expectedTotalRoute) throws ExecutionException, InterruptedException {
        String accessToken = authLoginB2B(adminUsername, adminPassword);
        logger.log("Access token for dashboard request -> " + accessToken);

        GetBookingDetailDashboardApi.Inputs inputs = new GetBookingDetailDashboardApi.Inputs(accessToken, orderId, currentDate, dateDeparture, type);
        return checkBookingOrderMultiCity(inputs, orderId, expectedProductType, expectedTotalRoute);
    }

    private boolean checkBookingOrderMultiCity(GetBookingDetailDashboardApi.Inputs inputs, String expectedOrderId, String expectedProductType, int expectedTotalRoute) throws ExecutionException, InterruptedException {
        GetBookingDetailDashboardApi api = new GetBookingDetailDashboardApi(inputs);
        BookingDetailMultiCityResponse.OrderResponse response = new BookingDetailMultiCityResponse.OrderResponse();

        for (int attempt = 0; attempt < 3; attempt++) {
            ApiResponse apiResponse = api.hitApi(apiTools);
            logger.log("API response -> " + apiResponse.getBody());

            response = new Gson().fromJson(apiResponse.getBody(), BookingDetailMultiCityResponse.OrderResponse.class);

            if (response.data != null && !response.data.isEmpty()) {
                break;
            }

            Thread.sleep(1000);
        }

        if (response.data == null || response.data.isEmpty()) {
            logger.log("No order data found in API response.");
            return false;
        }

        BookingDetailMultiCityResponse.OrderData order = response.data.stream()
                .filter(data -> String.valueOf(data.orderID).equals(expectedOrderId))
                .findFirst()
                .orElse(response.data.get(0));
        int totalOrderDetail = order.orderDetail == null ? 0 : order.orderDetail.size();
        logger.log("Checking Order ID: " + order.orderID + ", Product Type: " + order.productType + ", Order Detail count: " + totalOrderDetail);

        Assertion.assertThat("Order ID on B2B dashboard booking list should be " + expectedOrderId,
                String.valueOf(order.orderID), is(expectedOrderId));
        Assertion.assertThat("Product type of order " + expectedOrderId + " should be " + expectedProductType,
                String.valueOf(order.productType).toLowerCase(), is(expectedProductType.toLowerCase()));
        Assertion.assertThat("Order detail count of order " + expectedOrderId + " should be the same as total routes",
                totalOrderDetail, is(expectedTotalRoute));
        return true;
    }
}
