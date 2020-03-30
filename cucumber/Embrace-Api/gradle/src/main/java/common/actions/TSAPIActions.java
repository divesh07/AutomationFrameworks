package common.actions;

import java.io.IOException;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class TSAPIActions {

    private String userId;

    private String mixpanelAccessToken;

    private String authToken;
    
    @Given("^Login to trial account with short lived token$")
    public void loginToTrialAccountWithShortLivedToken() throws Exception {
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("id", "aa0e7f89-1838-4a6b-a905-128298c80ed9");
        formData.add("shortlivedauthenticationtoken", "JHNoaXJvMSRTSEEtMjU2JDUwMDAwMCRvdXFuZkdLMlBIcWlhb3VSRXovUEN3PT0kRTNNV0NrbWJxVEVaNlNLSWNWazJmNzQzdy80czVxdmVNM1pJZDFtQUlEOD0");

        Response sessionLogin = Util
                .sendPostRequestNoAuth(Constants.SHORT_LIVED_LOGIN, formData, null);
        System.out.println(sessionLogin);
        Util.verifyExpectedResponse(sessionLogin, Response.Status.OK);
        Cookie sessionId = sessionLogin.getCookies().get("JSESSIONID");
        Assert.assertNotNull("Cookie cannot be empty", sessionId);
        System.out.println(sessionId);
        validateUserResponse(sessionLogin);
    }

    @Then("^Activate trail account user$")
    public void activateTrailAccountUser() throws Exception {

        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("userid", "aa0e7f89-1838-4a6b-a905-128298c80ed9");
        formData.add("auth_token", "JHNoaXJvMSRTSEEtMjU2JDUwMDAwMCRvdXFuZkdLMlBIcWlhb3VSRXovUEN3PT0kRTNNV0NrbWJxVEVaNlNLSWNWazJmNzQzdy80czVxdmVNM1pJZDFtQUlEOD0");
        formData.add("password", "th0ught$p0t");
        formData.add("properties", "Igt0aG91Z2h0c3BvdA");

        Response sessionLogin = Util
                .sendPostRequestNoAuth(Constants.TRIAL_USER_ACTIVATE, formData, null);
        System.out.println(sessionLogin);
        Util.verifyExpectedResponse(sessionLogin, Response.Status.OK);
        Cookie sessionId = sessionLogin.getCookies().get("JSESSIONID");
        Assert.assertNotNull("Cookie cannot be empty", sessionId);
        System.out.println(sessionId);

        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser
                .parse(Util.readResponse(sessionLogin));
        System.out.println(responseObject);
    }

    public void validateUserResponse(Response response) throws IOException, ParseException {
        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser
                .parse(Util.readResponse(response));
        System.out.println(responseObject);
        String username = (String) responseObject.get("userName");
        Assert.assertTrue(username.equals(Constants.TRIAL_USERNAME));
        String displayName = (String) responseObject.get("userDisplayName");
        Assert.assertTrue(displayName.equals(Constants.TRIAL_USER_DISPLAYNAME));
        String userEmail = (String) responseObject.get("userEmail");
        Assert.assertTrue(userEmail.equals(Constants.TRIAL_USER_EMAIL));

        String userGUID = (String) responseObject.get("userGUID");
        Assert.assertNotNull("User GUID cannot be empty", userGUID);

        System.out.println("User guid : "+ userGUID);
        userId = userGUID;

        Boolean canChangePassword = (Boolean) responseObject.get("canChangePassword");
        Assert.assertTrue(canChangePassword);
        Boolean isSystemUser = (Boolean) responseObject.get("isSystemUser");
        Assert.assertFalse(isSystemUser);
        authToken = (String) responseObject.get("authToken");
        Assert.assertNotNull("Auth Token cannot be empty", authToken);
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

        JSONObject userPreferences = (JSONObject) configInfo.get("userPreferences");
        Assert.assertNotNull(userPreferences);
        Boolean showWalkMe = (Boolean) userPreferences.get("showWalkMe");
        Assert.assertTrue(showWalkMe);
        Boolean notifyOnShare = (Boolean) userPreferences.get("notifyOnShare");
        Assert.assertTrue(notifyOnShare);

        JSONObject userProperties = (JSONObject) configInfo.get("userProperties");
        Assert.assertNotNull(userProperties);
        String mail = (String) userPreferences.get("mail");
        Assert.assertTrue(mail.equalsIgnoreCase(Constants.TRIAL_USER_EMAIL));
        String source = (String) userPreferences.get("source");
        Assert.assertTrue(mail.equalsIgnoreCase("snowflake_partner_connect"));

        Boolean isFirstLogin = (Boolean) configInfo.get("isFirstLogin");
        Assert.assertFalse(isFirstLogin);
        String state = (String) configInfo.get("state");
        Assert.assertTrue(state.equalsIgnoreCase("INACTIVE"));
        String license = (String) configInfo.get("license");
        Assert.assertTrue(license.equalsIgnoreCase("EAA"));
    }

    @Given("^Login to trial account$")
    public void loginToTrialAccount() throws Exception {
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("username", Constants.TRIAL_USERNAME);
        formData.add("password", Constants.TRIAL_PASSWORD);

        Response sessionLogin = Util
                .sendPostRequestNoAuth(Constants.TRIAL_SESSION_LOGIN, formData, null);
        System.out.println(sessionLogin);
        Util.verifyExpectedResponse(sessionLogin, Response.Status.OK);
        Cookie sessionId = sessionLogin.getCookies().get("JSESSIONID");
        Assert.assertNotNull("Cookie cannot be empty", sessionId);
        System.out.println(sessionId);

        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser
                .parse(Util.readResponse(sessionLogin));
        System.out.println(responseObject);
        validateUserResponse(sessionLogin);
    }

    @Then("^validate trial account session info$")
    public void validateTrialAccountSessionInfo() throws Exception {
        Response response = Util.sendGetRequestAuth(Constants.SESSION_INFO, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        System.out.println(responseObject);
    }
}
