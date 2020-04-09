package common.actions;

import static common.actions.SFConstants.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class SnowflakeAPIActions {

    private String jwtAuthToken;

    private String redirect_uri;

    private static String userId;

    private static String userAuthToken;

    /**
     * @throws Exception
     */
    public void refreshAuthToken() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("__uiAppName", "Login");

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_LOGIN, REFRESH_AUTH_TOKEN, params, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);

        JSONParser responseParser = new JSONParser();

        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        System.out.println(responseObject);

        JSONObject data = (JSONObject) responseObject.get("data");
        String masterToken = (String) data.get("masterToken");
        Assert.assertNotNull(masterToken);
        jwtAuthToken = masterToken;
        String token = (String) data.get("token");
        Assert.assertNotNull(token);
        jwtAuthToken = token;
    }

    /**
     * @throws Exception
     */
    @Given("^login into snowflake$")
    public void loginIntoSnowflake() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("__uiAppName", "Login");

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_LOGIN, SF_LOGIN, params, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);

        JSONParser responseParser = new JSONParser();

        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        System.out.println(responseObject);

        JSONObject data = (JSONObject) responseObject.get("data");
        String masterToken = (String) data.get("masterToken");
        Assert.assertNotNull(masterToken);
        jwtAuthToken = masterToken;
        String token = (String) data.get("token");
        Assert.assertNotNull(token);

        Long validityInSeconds = (Long) data.get("validityInSeconds");
        Assert.assertNotNull(validityInSeconds);
        Long masterValidityInSeconds = (Long) data.get("masterValidityInSeconds");
        Assert.assertNotNull(masterValidityInSeconds);
        String displayUserName = (String) data.get("displayUserName");
        Assert.assertNotNull(displayUserName);
        String serverVersion = (String) data.get("serverVersion");
        Assert.assertNotNull(serverVersion);
        Boolean firstLogin = (Boolean) data.get("firstLogin");
        Assert.assertNotNull(firstLogin);

        // Todo : Validate and add Assertion
        String remMeToken = (String) data.get("remMeToken");
        /*
        Assert.assertNotNull(remMeToken);
        */
        Long remMeValidityInSeconds = (Long) data.get("remMeValidityInSeconds");
        Assert.assertNotNull(remMeValidityInSeconds);
        Long healthCheckInterval = (Long) data.get("healthCheckInterval");
        Assert.assertNotNull(healthCheckInterval);
        String newClientForUpgrade = (String) data.get("newClientForUpgrade");
        /*
        Assert.assertNotNull(newClientForUpgrade);
        */
        Long sessionId = (Long) data.get("sessionId");
        Assert.assertNotNull(sessionId);
        JSONArray parameters = (JSONArray) data.get("parameters");
        Assert.assertTrue(parameters.size() > 0);
        JSONObject sessionInfo = (JSONObject) data.get("sessionInfo");
        Assert.assertNotNull(sessionInfo);

        String databaseName = (String) sessionInfo.get("databaseName");
        String schemaName = (String) sessionInfo.get("schemaName");
        /*
        Assert.assertNotNull(databaseName);
        Assert.assertNotNull(schemaName);
        */
        String warehouseName = (String) sessionInfo.get("warehouseName");
        Assert.assertNotNull(warehouseName);
        String roleName = (String) sessionInfo.get("roleName");
        Assert.assertNotNull(roleName);
        String idToken = (String) data.get("idToken");
        /*
        Assert.assertNotNull(idToken);
        */
        Long idTokenValidityInSeconds = (Long) data.get("idTokenValidityInSeconds");
        Assert.assertNotNull(idTokenValidityInSeconds);

        String responseData = (String) data.get("responseData");
        String message = (String) responseObject.get("message");
        String code = (String) responseObject.get("code");
        /*
        Assert.assertNotNull(responseData);
        Assert.assertNotNull(message);
        Assert.assertNotNull(code);
        */
        Boolean success = (Boolean) responseObject.get("success");
        Assert.assertNotNull(success);
    }

    /**
     * @throws Exception
     */
    @Then("^access the console$")
    public void accessTheConsole() throws Exception {
        refreshAuthToken();

        Map<String, String> params = new HashMap<>();
        params.put("__uiAppName", "Login");
        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" + jwtAuthToken + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_ACTION, ACCESS_CONSOLE, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
    }

    /**
     * @throws Exception
     */
    @Then("^Remove ETL integration$")
    public void removeETLIntegration() throws Exception {
        refreshAuthToken();
        Map<String, String> params = new HashMap<>();

        params.put("requestId", ETL_REQUEST_ID);

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" + jwtAuthToken + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, REMOVE_ETL_INTEGRATION, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
    }

    /**
     * @throws Exception
     */
    @Then("^list all databases$")
    public void listAllDatabases() throws Exception {
        refreshAuthToken();

        Map<String, String> params = new HashMap<>();
        params.put("requestId", LIST_DB_REQUEST_ID);

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" + jwtAuthToken + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, LIST_ALL_DATABASES, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        AssertQueryRequestResponse(response);
    }

    /**
     * @throws Exception
     */
    @Then("^list databases in ts partner account$")
    public void listAllDatabasesInTsPartnerAccount() throws Exception {
        refreshAuthToken();

        Map<String, String> params = new HashMap<>();
        params.put("requestId", LIST_DB_REQUEST_ID);

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" + jwtAuthToken + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, LIST_DATABASES_TS_PARTNER, params,
                        jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
    }

    /**
     * @throws Exception
     */
    @Then("^Show Grants to USER$")
    public void showGrantsToUSER() throws Exception {
        refreshAuthToken();

        Map<String, String> params = new HashMap<>();
        params.put("requestId", SHOW_GRANTS_REQUEST_ID);

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" + jwtAuthToken + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, SHOW_GRANTS_USER, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        AssertQueryRequestResponse(response);
    }

    /**
     * @throws Exception
     */
    @Then("^Show current available roles$")
    public void showCurrentAvailableRoles() throws Exception {
        refreshAuthToken();

        Map<String, String> params = new HashMap<>();
        params.put("requestId", SHOW_GRANTS_REQUEST_ID);

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" + jwtAuthToken + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, CURRENT_AVAILABLE_ROLES, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        AssertQueryRequestResponse(response);
    }

    @Then("^Logout from Snowflake$")
    public void logoutFromSnowflake() throws Exception {
        refreshAuthToken();
        String input = "{}";

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" + jwtAuthToken + "\"";
        System.out.println(jwt);

        Response response = Util.sendSFPostRequest(Constants.SNOWFLAKE_LOGOUT, input, null, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
    }

    /**
     * @throws Exception
     */
    @Then("^Show Managed Accounts$")
    public void showManagedAccounts() throws Exception {
        refreshAuthToken();

        Map<String, String> params = new HashMap<>();
        params.put("requestId", MANAGED_ACCOUNTS_REQUEST_ID);

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" + jwtAuthToken + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, MANAGED_ACCOUNTS, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);

        AssertQueryRequestResponse(response);
    }

    /**
     * @throws Exception
     */
    @Then("^Show Shares$")
    public void showShares() throws Exception {
        refreshAuthToken();

        Map<String, String> params = new HashMap<>();
        params.put("requestId", SHOW_SHARES_REQUEST_ID);

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" + jwtAuthToken + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, SHOW_SHARES, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        AssertQueryRequestResponse(response);
    }

    /**
     * @throws Exception
     */
    @Then("^List schemas is ts_partner\\.freetrial database$")
    public void listSchemasIsTs_partnerFreetrialDatabase() throws Exception {
        refreshAuthToken();

        Map<String, String> params = new HashMap<>();
        params.put("requestId", SHOW_SCHEMA_REQUEST_ID);

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" + jwtAuthToken + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, SHOW_SCHEMA, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        AssertQueryRequestResponse(response);
    }

    /**
     * @throws Exception
     */
    @Then("^List schemas is ts_partner\\.snowflake sample data database$")
    public void listSchemasIsTs_partnerSnowflakeSampleDataDatabase() throws Exception {
        refreshAuthToken();

        Map<String, String> params = new HashMap<>();
        params.put("requestId", SHOW_SCHEMA_REQUEST_ID);

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" + jwtAuthToken + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, SHOW_SCHEMA_SF_SAMPLE, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        AssertQueryRequestResponse(response);
    }

    /**
     * @throws Exception
     */
    @Then("^Create ETL integration$")
    public void createETLIntegration() throws Exception {
        refreshAuthToken();

        Map<String, String> params = new HashMap<>();
        params.put("requestId", ETL_INTEGRATION_REQUEST_ID);

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" + jwtAuthToken + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, CREATE_ETL_INTEGRATION, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        String returnedString = AssertQueryRequestResponse(response);
        System.out.println(returnedString);
        JSONParser parser = new JSONParser();
        JSONObject returnedObject = (JSONObject) parser.parse(returnedString);

        String message = (String) returnedObject.get("message");
        System.out.println(message);
        if (message.equalsIgnoreCase("Partner is already integrated")) {
            System.out.println("ETL Integration not required");
        } else if (message.equalsIgnoreCase("Account and Destination Created")) {
            JSONObject data = (JSONObject) returnedObject.get("data");
            Assert.assertNotNull(data);
            System.out.println(data);
            redirect_uri = (String) data.get("redirect_uri");
            System.out.println(redirect_uri);
            Assert.assertNotNull(redirect_uri);
            Pattern p = Pattern.compile("(id=)(.+)(&token=)(.+)");
            Matcher m = p.matcher(redirect_uri);
            while (m.find()) {
                userId = m.group(2);
                userAuthToken = m.group(4);
            }
            System.out.println(userId);
            Assert.assertNotNull(userId);
            System.out.println(userAuthToken);
            Assert.assertNotNull(userAuthToken);
        } else {
            System.out.println("Invalid message");
        }
    }

    /**
     * @return
     */
    public static String getUserId() {
        return userId;
    }

    /**
     * @return
     */
    public static String getUserAuthToken() {
        return userAuthToken;
    }

    /**
     * @throws Exception
     */
    @Then("^list view in schema ts_partner\\.freetrial$")
    public void listViewInSchemaTs_partnerFreetrial() throws Exception {
        refreshAuthToken();

        Map<String, String> params = new HashMap<>();
        params.put("requestId", VIEWS_TABLES_REQUEST_ID);

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" + jwtAuthToken + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, LIST_VIEWS, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
    }

    /**
     * @throws Exception
     */
    @Then("^list tables in schema ts_partner\\.freetrial$")
    public void listTablesInSchemaTs_partnerFreetrial() throws Exception {
        refreshAuthToken();

        Map<String, String> params = new HashMap<>();
        params.put("requestId", VIEWS_TABLES_REQUEST_ID);

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" + jwtAuthToken + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, LIST_TABLES, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        AssertQueryRequestResponse(response);
    }

    /**
     * @throws Exception
     */
    @Then("^use role AccountAdmin$")
    public void useRoleAccountAdmin() throws Exception {
        refreshAuthToken();

        Map<String, String> params = new HashMap<>();
        params.put("requestId", ROLES_REQUEST_ID);

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" + jwtAuthToken + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, USE_ROLE, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
    }

    /**
     * @throws Exception
     */
    @Then("^Bootstrap data request$")
    public void bootstrapDataRequest() throws Exception {
        refreshAuthToken();

        Map<String, String> params = new HashMap<>();
        params.put("requestId", ETL_REQUEST_ID);

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" + jwtAuthToken + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_BOOTSTRAP, BOOTSTRAP, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
    }

    /**
     * @throws Exception
     */
    @Then("^MFA Enrollment request$")
    public void mfaEnrollmentRequest() throws Exception {
        refreshAuthToken();

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" + jwtAuthToken + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_MFA_REENROLLMENT, MFA_CHECK_INFO, null, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
    }

    /**
     * @throws Exception
     */
    @Then("^Close Container Button$")
    public void closeContainerButton() throws Exception {
        refreshAuthToken();
        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" + jwtAuthToken + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendGetRequestWithJwtAuth(Constants.SNOWFLAKE_CLOSE_BUTTON, jwt, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
    }

    /**
     * @param response
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public String AssertQueryRequestResponse(Response response) throws IOException, ParseException {
        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        System.out.println(responseObject);
        JSONObject dataObject = (JSONObject) responseObject.get("data");
        System.out.println(dataObject);
        Assert.assertNotNull(dataObject);
        JSONArray parameters = (JSONArray) dataObject.get("parameters");
        Assert.assertTrue(parameters.size() > 0);
        JSONArray rowtype = (JSONArray) dataObject.get("rowtype");
        Assert.assertTrue(rowtype.size() > 0);

        JSONArray rowset = (JSONArray) dataObject.get("rowset");
        Assert.assertTrue(rowset.size() > 0);
        JSONArray rowtypeNestedObj = (JSONArray) rowset.get(0);
        String rowtypeObj = (String) rowtypeNestedObj.get(0);
        System.out.println(rowtypeObj);
        Assert.assertNotNull(rowtypeObj);
        String finalWarehouseName = (String) dataObject.get("finalWarehouseName");
        Assert.assertTrue(finalWarehouseName.equalsIgnoreCase(Constants.SNOWFLAKE_DEMO_WAREHOUSE));
        String finalRoleName = (String) dataObject.get("finalRoleName");
        Assert.assertTrue(finalRoleName.equalsIgnoreCase(Constants.SNOWFLAKE_ACCOUNT_ADMIN));
        return rowtypeObj;
    }
}
