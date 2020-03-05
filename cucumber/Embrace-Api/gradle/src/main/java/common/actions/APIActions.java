package common.actions;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.sun.org.apache.bcel.internal.Const;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.thoughtspot.callosum.common.json.JSONUtil;
import com.thoughtspot.callosum.server.services.session.SessionInfo;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import gherkin.deps.com.google.gson.Gson;

import org.glassfish.json.JsonUtil;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class APIActions {
    private static final Logger LOG = LoggerFactory.getLogger(APIActions.class);

    /*private final WebDriver driver;

    public APIActions(SharedDriver driver) {
        if ( Constants.ENBALE_UI ) {
            this.driver = driver;
        }else{
            this.driver = null;
        }
    }*/

    @Given("validate session login API")
    public void sessionLogin() throws Exception{
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("username", Constants.CUSTOMER_USERNAME);
        formData.add("password", Constants.CUSTOMER_PASSWORD);

        Map<String, String> params = new HashMap<>();
        params.put("id", Constants.Test);

        Response sessionLogin = Util.sendPostRequestWithUrlEncodedForm(Constants.SESSION_LOGIN, formData , params);

        Util.verifyExpectedResponse(sessionLogin, Response.Status.OK);

        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(sessionLogin));

        System.out.println(responseObject);

        Gson gson = new Gson();
        String response = gson.toJson(responseObject);
    }

    @Then("^validate session info API$")
    public void validateSessionInfoAPI() throws Exception {
        Response response = Util.sendGetRequestNoAuth(Constants.SESSION_INFO);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        System.out.println(responseObject);

    }

    @Then("^validate session isActive API$")
    public void validateSessionIsActiveAPI() throws Exception {
        Response response = Util.sendGetRequestNoAuth(Constants.SESSION_INFO, "text/plain");
        Util.verifyExpectedResponse(response, Response.Status.OK);
        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        System.out.println(responseObject);
    }

    @Then("^validate session logout API$")
    public void validateSessionLogoutAPI() {
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
