package common.actions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import org.json.simple.JSONArray;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class APIActions {
    private static final Logger LOG = LoggerFactory.getLogger(APIActions.class);

    //private final WebDriver driver;

    /*public APIActions(SharedDriver driver) {
        this.driver = driver;
    }*/

    @Given("validate session login")
    public void sessionLogin() throws Exception {
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("username", Constants.CUSTOMER_USERNAME);
        formData.add("password", Constants.CUSTOMER_PASSWORD);

        /*Map<String, String> params = new HashMap<>();
        params.put("id", Constants.Test);*/

        Response sessionLogin = Util
                .sendPostRequestNoAuth(Constants.SESSION_LOGIN, formData, null);

        Util.verifyExpectedResponse(sessionLogin, Response.Status.OK);
        Cookie sessionId = sessionLogin.getCookies().get("JSESSIONID");
        System.out.println(sessionId);

        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser
                .parse(Util.readResponse(sessionLogin));

        System.out.println(responseObject);

        /*Gson gson = new Gson();
        String response = gson.toJson(responseObject);
        SessionInfo info2 = gson.fromJson(String.valueOf(sessionLogin), SessionInfo.class);

        System.out.println(responseObject.toJSONString());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SessionInfo info = objectMapper.readValue(responseString, SessionInfo.class);*/

    }

    @Then("^validate session info$")
    public void validateSessionInfo() throws Exception {
        Response response = Util.sendGetRequestAuth(Constants.SESSION_INFO);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        System.out.println(responseObject);
    }

    @Then("^validate session isActive$")
    public void validateSessionIsActive() throws Exception {
        Response response = Util.sendGetRequestAuth(Constants.SESSION_ISACTIVE);
        Util.verifyExpectedResponse(response, Response.Status.NO_CONTENT);
    }

    @Then("^create a user with weak password$")
    public void validateUserCreationWeakPassword() throws Exception {
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("name", Constants.SECONDARY_CUSTOMER_USERNAME);
        formData.add("password", Constants.SECONDARY_CUSTOMER_USERNAME);
        formData.add("displayname", Constants.SECONDARY_CUSTOMER_USERNAME);
        formData.add("usertype", "LOCAL_USER");
        formData.add("visibility", "DEFAULT");

        Response response = Util
                .sendPostRequestWithAuthCookie(Constants.SESSION_USER_CREATE, formData, null);

        Util.verifyExpectedResponse(response, Response.Status.BAD_REQUEST);
        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        String failureReason = (String) responseObject.get("debug");
        Assert.assertTrue("Weak password not found as a failure reason",failureReason.contains(
                "Weak Password"));
    }

    @Then("^create a user with valid password$")
    public void validateUserCreationValidPassword() throws Exception {
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("name", Constants.SECONDARY_CUSTOMER_USERNAME);
        formData.add("password", Constants.SECONDARY_CUSTOMER_PASSWORD);
        formData.add("displayname", Constants.SECONDARY_CUSTOMER_USERNAME);
        formData.add("usertype", "LOCAL_USER");
        formData.add("visibility", "DEFAULT");

        Response response = Util
                .sendPostRequestWithAuthCookie(Constants.SESSION_USER_CREATE, formData, null);

        Util.verifyExpectedResponse(response, Response.Status.OK);
        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        JSONObject header = (JSONObject) responseObject.get("header");
        String username = (String) header.get("name");
        Assert.assertTrue(username.equals(Constants.SECONDARY_CUSTOMER_USERNAME));

        String type = (String) responseObject.get("type");
        Assert.assertTrue(type.equals(Constants.CUSTOMER_TYPE));
        String visibility = (String) responseObject.get("visibility");
        Assert.assertTrue(visibility.equals(Constants.CUSTOMER_VISIBILITY));
        String displayName = (String) responseObject.get("displayName");
        Assert.assertTrue(displayName.equals(Constants.SECONDARY_CUSTOMER_USERNAME));
        String tenantId = (String) responseObject.get("tenantId");
        Assert.assertNotNull("Tenant ID cannot be null", tenantId);
    }

    @Then("^validate session logout$")
    public void validateSessionLogout() {
    }

    //
    //
    //    @Then("^get card details$")
    //    public void getCardDetailsAPI() throws Exception {
    //        Map<String, String> params = new HashMap<>();
    //        params.put("store_id", Constants.STORE_ID);
    //
    //        Response response = Util.sendGetRequestWithParams(Constants.GET_SAVE_CARD_URL, access_token, params);
    //        Util.verifyExpectedResponse(response, Response.Status.OK);
    //
    //        JSONParser responseParser = new JSONParser();
    //        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
    //        JSONObject result = (JSONObject) responseObject.get("data");
    //        JSONArray saved_cards = (JSONArray) result.get("saved_cards");
    //        JSONObject cardDetails = (JSONObject) saved_cards.get(0);
    //        card_number = (String) cardDetails.get("card_number");
    //        card_brand = (String) cardDetails.get("card_brand");
    //        saved_card_id = (String) cardDetails.get("saved_card_id");
    //        expiry = (String) cardDetails.get("expiry");
    //    }

    //    @Then("^test item scan$")
    //    public void scanProductsAPI() throws Exception {
    //        Map<String, String> params = new HashMap<>();
    //        params.put("store_id", Constants.STORE_ID);
    //        params.put("customer_id", cust_id);
    //        params.put("product_identifier", Constants.PRODUCT_IDENTIFIER);
    //        params.put("product_identifier_type", "qr");
    //
    //        Response response = Util.sendGetRequestWithParams(Constants.GET_SAVE_CARD_URL, access_token, params);
    //        Util.verifyExpectedResponse(response, Response.Status.OK);
    //
    //
    //
    //    }

}
