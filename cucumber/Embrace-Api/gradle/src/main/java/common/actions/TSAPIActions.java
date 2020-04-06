package common.actions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class TSAPIActions {

    private String userId;

    private String mixpanelAccessToken;

    private static Boolean isActivated = false;

    private static Boolean ifLoggedIn = false;

    private String groupId;

    private NewCookie sessionId;

    private NewCookie clientId;

    private NewCookie __cfduid;

    private String userAuthToken;

    @Given("^Login to trial account with short lived token$")
    public void loginToTrialAccountWithShortLivedToken() throws Exception {
        String id = SnowflakeAPIActions.getUserId();
        System.out.println(id);

        if ( null !=id ) {
            Assert.assertNotNull(id);
            userId = id;
            userAuthToken = SnowflakeAPIActions.getUserAuthToken();
            System.out.println(userAuthToken);
            Assert.assertNotNull(userAuthToken);

            MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
            formData.add("id", id);
            formData.add("shortlivedauthenticationtoken", userAuthToken);

            Response sessionLogin = Util
                    .sendPostRequestNoAuth(Constants.TRIAL_SHORT_LIVED_LOGIN, formData, null);
            System.out.println(sessionLogin);
            ifLoggedIn = true;
            Util.verifyExpectedResponse(sessionLogin, Response.Status.OK);

            sessionId = sessionLogin.getCookies().get("JSESSIONID");
            Assert.assertNotNull("Cookie cannot be empty", sessionId);
            System.out.println(sessionId);

            clientId = sessionLogin.getCookies().get("clientId");
            Assert.assertNotNull("Cookie cannot be empty", clientId);
            System.out.println(clientId);

            __cfduid = sessionLogin.getCookies().get("__cfduid");
            Assert.assertNotNull("Cookie cannot be empty", __cfduid);
            System.out.println(__cfduid);

            validateUserResponse(sessionLogin, "user", "INACTIVE", true);
        }else{
            isActivated = true;
        }
    }

    @Then("^Activate trial account user$")
    public void activateTrialAccountUser() throws Exception {
        if (!isActivated) {
            MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
            formData.add("userid", userId);
            formData.add("auth_token", userAuthToken);
            formData.add("password", Constants.TRIAL_PASSWORD);
            formData.add("properties", "Ih1zYW1lZXIuc2F0eWFtQHRob3VnaHRzcG90LmNvbQ");
            System.out.println(sessionId);
            System.out.println(clientId);
            System.out.println(userId);
            System.out.println(userAuthToken);

            Assert.assertNotNull(sessionId);
            Assert.assertNotNull(userId);
            Assert.assertNotNull(userAuthToken);
            Assert.assertNotNull(clientId);

            Response sessionLogin = Util
                    .sendPostRequestWithSessionId(Constants.TRIAL_USER_ACTIVATE, formData, null,
                            sessionId, clientId, __cfduid);

            Util.verifyExpectedResponse(sessionLogin, Response.Status.NO_CONTENT);
            sessionId = sessionLogin.getCookies().get("JSESSIONID");
            Assert.assertNotNull("Cookie cannot be empty", sessionId);
            System.out.println(sessionId);

            clientId = sessionLogin.getCookies().get("clientId");
            Assert.assertNotNull("clientId cannot be empty", clientId);
            System.out.println(clientId);
        }
    }

    public void validateUserResponse(Response response, String userType, String state,
            Boolean showWalkMe) throws IOException,
            ParseException {
        String username;
        if (userType.equalsIgnoreCase("admin")){
            username = Constants.TRIAL_ADMIN_USERNAME;
        }else{
            username = Constants.TRIAL_USERNAME;
        }

        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser
                .parse(Util.readResponse(response));
        System.out.println(responseObject);
        String fetchedusername = (String) responseObject.get("userName");
        Assert.assertTrue(fetchedusername.equals(username));
        if (userType.equalsIgnoreCase("user")) {
            String displayName = (String) responseObject.get("userDisplayName");
            Assert.assertTrue(displayName.equals(Constants.TRIAL_USER_DISPLAYNAME));
            String userEmail = (String) responseObject.get("userEmail");
            Assert.assertTrue(userEmail.equals(Constants.TRIAL_USER_EMAIL));
        }

        String userGUID = (String) responseObject.get("userGUID");
        Assert.assertNotNull("User GUID cannot be empty", userGUID);
        System.out.println("User guid : "+ userGUID);

        Boolean canChangePassword = (Boolean) responseObject.get("canChangePassword");
        Assert.assertTrue(canChangePassword);
        Boolean isSystemUser = (Boolean) responseObject.get("isSystemUser");
        Assert.assertFalse(isSystemUser);
        String userToken = (String) responseObject.get("authToken");
        Assert.assertNotNull("Auth Token cannot be empty", userToken);
        String locale = (String) responseObject.get("locale");
        Assert.assertTrue(locale.equals("en_US"));
        String timezone = (String) responseObject.get("timezone");
        Assert.assertTrue(timezone.equals("UTC"));

        JSONObject configInfo = (JSONObject) responseObject.get("configInfo");
        Assert.assertNotNull(configInfo);
        Boolean enableMixpanelService = (Boolean) configInfo.get("enableMixpanelService");
        Assert.assertTrue(enableMixpanelService);
        mixpanelAccessToken = (String) configInfo.get("mixpanelAccessToken");
        Assert.assertNotNull(mixpanelAccessToken);

        JSONObject trialVersionConfig = (JSONObject) configInfo.get("trialVersionConfig");
        Assert.assertNotNull(trialVersionConfig);
        Boolean enableTrialVersion = (Boolean) trialVersionConfig.get("enableTrialVersion");
        Assert.assertFalse(enableTrialVersion);
        Long trialAccountLifeDays = (Long) trialVersionConfig.get("trialAccountLifeDays");
        Assert.assertNotNull(trialAccountLifeDays);
        Long expiredAccountLifeDays = (Long) trialVersionConfig.get("expiredAccountLifeDays");
        Assert.assertNotNull(expiredAccountLifeDays);
        String selfClusterName = (String) configInfo.get("selfClusterName");
        Assert.assertTrue(selfClusterName.equalsIgnoreCase("TryThoughtSpotFreeTrial"));

        JSONObject userPreferences = (JSONObject) responseObject.get("userPreferences");
        Assert.assertNotNull(userPreferences);

        Boolean ActualShowWalkMe = (Boolean) userPreferences.get("showWalkMe");
        Assert.assertTrue(showWalkMe.compareTo(ActualShowWalkMe) == 0 );
        /*if (userType.equalsIgnoreCase("user")) {
            Assert.assertTrue(showWalkMe);
        }*/
        Boolean notifyOnShare = (Boolean) userPreferences.get("notifyOnShare");
        Assert.assertTrue(notifyOnShare);

        JSONObject userProperties = (JSONObject) responseObject.get("userProperties");
        Assert.assertNotNull(userProperties);
        String mail = (String) userProperties.get("mail");
        if (userType.equalsIgnoreCase("user")) {
            Assert.assertTrue(mail.equalsIgnoreCase(Constants.TRIAL_USER_EMAIL));
            String source = (String) userProperties.get("source");
            Assert.assertTrue(source.equalsIgnoreCase("snowflake_partner_connect"));
        }

        Boolean isFirstLogin = (Boolean) responseObject.get("isFirstLogin");
        // TODO : Remove the comment after testing
        //Assert.assertTrue(isFirstLogin);
        String ActualState = (String) responseObject.get("state");
        Assert.assertTrue(ActualState.equalsIgnoreCase(state));
        String license = (String) responseObject.get("license");
        Assert.assertTrue(license.equalsIgnoreCase("EAA"));
    }

    @Given("^Login to trial account as (admin|user)$")
    public void loginToTrialAccount(String userType) throws Exception {
        String username = "";
        String password = "";
        Boolean showWalkMe = false;

        if (userType.equalsIgnoreCase("admin")){
            username = Constants.TRIAL_ADMIN_USERNAME;
            password = Constants.TRIAL_ADMIN_PASSWORD;
        }else{
            username = Constants.TRIAL_USERNAME;
            password = Constants.TRIAL_PASSWORD;
            showWalkMe = true;
        }

        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("username", username);
        formData.add("password", password);

        Response sessionLogin = Util
                .sendPostRequestNoAuth(Constants.TRIAL_SESSION_LOGIN, formData, null);
        System.out.println(sessionLogin);
        Util.verifyExpectedResponse(sessionLogin, Response.Status.OK);

        sessionId = sessionLogin.getCookies().get("JSESSIONID");
        Assert.assertNotNull("Cookie cannot be empty", sessionId);
        System.out.println(sessionId);

        clientId = sessionLogin.getCookies().get("clientId");
        Assert.assertNotNull("Cookie cannot be empty", clientId);
        System.out.println(clientId);

        __cfduid = sessionLogin.getCookies().get("__cfduid");
        Assert.assertNotNull("Cookie cannot be empty", __cfduid);
        System.out.println(__cfduid);

        validateUserResponse(sessionLogin, userType, "ACTIVE", showWalkMe);
    }

    @Then("^validate trial session for (admin|user)$")
    public void validateTrialSession(String userType) throws Exception {
        /*String username = "";
        String password = "";

        if (userType.equalsIgnoreCase("admin")){
            username = Constants.TRIAL_ADMIN_USERNAME;
            password = Constants.TRIAL_ADMIN_PASSWORD;
        }else{
            username = Constants.TRIAL_USERNAME;
            password = Constants.TRIAL_PASSWORD;
        }
        System.out.println(username);
        System.out.println(password);*/
        Response response = Util.sendGetRequestAuth(Constants.TRIAL_SESSION_INFO, null, sessionId);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        System.out.println(responseObject);
    }

    @Then("^validate system config for (admin|user)$")
    public void validateSystemConfig(String userType) throws Exception {
        /*String username = "";
        String password = "";

        if (userType.equalsIgnoreCase("admin")){
            username = Constants.TRIAL_ADMIN_USERNAME;
            password = Constants.TRIAL_ADMIN_PASSWORD;
        }else{
            username = Constants.TRIAL_USERNAME;
            password = Constants.TRIAL_PASSWORD;
        }*/

        Response response =
                Util.sendGetRequestAuth(Constants.TS_TRIAL_SYSTEM_CONFIG, null, sessionId);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        System.out.println(responseObject);
    }

    @Given("^logout to trial account as (admin|user)$")
    public void logoutToTrialAccount(String userType) throws Exception {
        String username = "";
        String password = "";

        if (userType.equalsIgnoreCase("admin")){
            username = Constants.TRIAL_ADMIN_USERNAME;
            password = Constants.TRIAL_ADMIN_PASSWORD;
        }else{
            username = Constants.TRIAL_USERNAME;
            password = Constants.TRIAL_PASSWORD;
        }
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();

        /*Response response = Util
                .sendPostRequestWithSessionId(Constants.TRIAL_SESSION_LOGOUT, null, null, sessionId
                        , clientId, __cfduid);*/
        Response response =
                Util.sendPostRequestWithAuthCookie(Constants.TRIAL_SESSION_LOGOUT, formData,
                        username, password, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.NO_CONTENT);
    }

    @Then("^delete trial account user$")
    public void deleteTrialAccountUser() throws Exception {
        Assert.assertNotNull(userId);
        System.out.println(userId);

        String deleteId = "[" + userId + "]";
        System.out.println(deleteId);
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("ids", deleteId);

        Response response =
                Util.sendPostRequestWithAuthCookie(Constants.TRIAL_SESSION_USER_DELETE, formData,
                        Constants.TRIAL_ADMIN_USERNAME, Constants.TRIAL_ADMIN_PASSWORD, null);

        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.NO_CONTENT);
    }

    @Then("^delete trial account user group$")
    public void deleteTrialAccountUserGroup() throws Exception {
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("ids", "[" + groupId + "]");
        Assert.assertNotNull(groupId);

        Response response =
                Util.sendPostRequestWithAuthCookie(Constants.TRIAL_SESSION_GROUP_DELETE, formData,
                        Constants.TRIAL_ADMIN_USERNAME, Constants.TRIAL_ADMIN_PASSWORD, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.NO_CONTENT);
    }

    @Then("^fetch the group id$")
    public void fetchTheGroupId() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("pattern", "%25sameer.satyam@thoughtspot.com%25");
        params.put("showhidden", "false");
        params.put("sort", "NAME");
        params.put("sortascending", "true");
        params.put("type", "USER_GROUP");

        Response response = Util.sendGetRequestAuth(Constants.TRIAL_SESSION_SEARCH_GROUP, params,
                sessionId);

        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);

        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        System.out.println(responseObject);
        JSONArray headers = (JSONArray) responseObject.get("headers");
        JSONObject headerObject = (JSONObject) headers.get(0);
        groupId = (String) headerObject.get("id");
        Assert.assertNotNull(groupId);
        String group = (String) headerObject.get("type");
        System.out.println(group);
        //ToDO ; Remove after testing
        Assert.assertTrue(group.equalsIgnoreCase("LOCAL_GROUP"));
        Assert.assertNotNull(groupId);
    }

    @Then("^fetch the user id$")
    public void fetchTheUserId() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("pattern", "%25sameer.satyam@thoughtspot.com%25");
        params.put("showhidden", "false");
        params.put("sort", "NAME");
        params.put("sortascending", "true");
        params.put("type", "USER");

        Response response = Util.sendGetRequestAuth(Constants.TRIAL_SESSION_SEARCH_GROUP, params,
                sessionId);

        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);

        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        System.out.println(responseObject);
        JSONArray headers = (JSONArray) responseObject.get("headers");
        try {
            JSONObject headerObject = (JSONObject) headers.get(0);
            userId = (String) headerObject.get("id");
            Assert.assertNotNull(userId);
            String group = (String) headerObject.get("type");
            System.out.println(group);
            //ToDO ; Remove after testing
            Assert.assertTrue(group.equalsIgnoreCase("LOCAL_USER"));
        } catch (Exception ex){
            System.out.println("User not found");
        }
    }
}
