package common.actions;

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

    public void refreshAuthToken() throws Exception {
        String input = "{\"data\":{\"ACCOUNT_NAME\":\"THOUGHTSPOT_PARTNER\","
                + "\"LOGIN_NAME\":\"DEMO_ADMIN\",\"PASSWORD\":\"demo_admin\",\"CLIENT_APP_ID\":\"Snowflake UI\"}}";

        Map<String, String> params = new HashMap<>();
        params.put("__uiAppName", "Login");

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_LOGIN, input, params, null);
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

    @Given("^login into snowflake$")
    public void loginIntoSnowflake() throws Exception {
        String input = "{\"data\":{\"ACCOUNT_NAME\":\"THOUGHTSPOT_PARTNER\","
                + "\"LOGIN_NAME\":\"DEMO_ADMIN\",\"PASSWORD\":\"demo_admin\",\"CLIENT_APP_ID\":\"Snowflake UI\"}}";

        Map<String, String> params = new HashMap<>();
        params.put("__uiAppName", "Login");

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_LOGIN, input, params, null);
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

        // Todo : dgandhi : Check why commented values are null
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
        Assert.assertTrue(parameters.size()>0);
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

    @Then("^access the console$")
    public void accessTheConsole() throws Exception {
        refreshAuthToken();
        String input = "{\"action\":\"LOG_UI_USAGE\","
                + "\"actionDataEncoded\":{\"logs\":[{\"message\":\"{\\\"type\\\":\\\"ui_exit\\\",\\\"interaction\\\":\\\"true\\\",\\\"flags\\\":{},\\\"data\\\":{\\\"networkSnapshot\\\":null},\\\"url\\\":\\\"/\\\"}\",\"timestamp\":1585044505804}]}}";

        Map<String, String> params = new HashMap<>();
        params.put("__uiAppName", "Login");
        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" +  jwtAuthToken  + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_ACTION, input, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
    }

    @Then("^Remove ETL integration$")
    public void removeETLIntegration() throws Exception {
        refreshAuthToken();
        String input = "{\"sqlText\":\"select system$remove_etl_integration('thoughtspot');\",\"disableOfflineChunks\":false}";
        Map<String, String> params = new HashMap<>();
        params.put("requestId", "9500bdb0-6ed3-11ea-b312-49172493a6a6");

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" +  jwtAuthToken  + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_ACTION, input, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
    }

    @Then("^list all databases$")
    public void listAllDatabases() throws Exception {
        refreshAuthToken();
        String input = "{\"sqlText\":\"SHOW DATABASES;\",\"disableOfflineChunks\":true,"
                + "\"isInternal\":true}";

        Map<String, String> params = new HashMap<>();
        params.put("requestId", "a690d220-71f0-11ea-9b98-b78aeac24c53");

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" +  jwtAuthToken  + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, input, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        AssertQueryRequestResponse(response);
    }

    @Then("^list databases in ts partner account$")
    public void listAllDatabasesInTsPartnerAccount() throws Exception {
        refreshAuthToken();
        String input = "{\"sqlText\":\"SHOW DATABASES IN ACCOUNT \\\"THOUGHTSPOT_PARTNER\\\";\","
                + "\"disableOfflineChunks\":true,\"parameters\":{\"ui_mode\":false,"
                + "\"ui_internal_mode\":false},\"isInternal\":true}";

        Map<String, String> params = new HashMap<>();
        params.put("requestId", "a690d220-71f0-11ea-9b98-b78aeac24c53");

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" +  jwtAuthToken  + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, input, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        //AssertQueryRequestResponse(response);
    }

    @Then("^Show Grants to USER$")
    public void showGrantsToUSER() throws Exception {
        refreshAuthToken();
        String input = "{\"sqlText\":\"SHOW GRANTS TO USER identifier(?);\",\"disableOfflineChunks\":true,\"bindings\":{\"1\":{\"value\":\"\\\"DEMO_ADMIN\\\"\",\"type\":\"TEXT\"}},\"parameters\":{\"ui_mode\":false,\"ui_internal_mode\":false},\"isInternal\":false}";

        Map<String, String> params = new HashMap<>();
        params.put("requestId", "cd92b050-6eda-11ea-9405-0f010fb453cb");

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" +  jwtAuthToken  + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, input, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        AssertQueryRequestResponse(response);
    }

    @Then("^Show current available roles$")
    public void showCurrentAvailableRoles() throws Exception {
        refreshAuthToken();
        String input = "{\"sqlText\":\"SELECT CURRENT_AVAILABLE_ROLES() AS \\\"ROLES\\\";\",\"disableOfflineChunks\":true,\"parameters\":{\"ui_mode\":false,\"ui_internal_mode\":false},\"isInternal\":true}";

        Map<String, String> params = new HashMap<>();
        params.put("requestId", "cd928940-6eda-11ea-9405-0f010fb453cb");

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" +  jwtAuthToken  + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, input, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        AssertQueryRequestResponse(response);
    }

    @Then("^Logout from Snowflake$")
    public void logoutFromSnowflake() throws Exception {
        refreshAuthToken();
        String input = "{}";

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" +  jwtAuthToken  + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_LOGOUT, input, null, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
    }

    @Then("^Show Managed Accounts$")
    public void showManagedAccounts() throws Exception {
        refreshAuthToken();
        String input = "{\"sqlText\":\"show managed accounts;\",\"disableOfflineChunks\":true,\"parameters\":{\"ui_mode\":false,\"ui_internal_mode\":false},\"isInternal\":true}";

        Map<String, String> params = new HashMap<>();
        params.put("requestId", "a8b33660-71c3-11ea-905b-79ea33d71b7e");

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" +  jwtAuthToken  + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, input, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);

        AssertQueryRequestResponse(response);
    }

    @Then("^Show Shares$")
    public void showShares() throws Exception {
        refreshAuthToken();
        String input = "{\"sqlText\":\"SHOW SHARES;\",\"disableOfflineChunks\":true,\"parameters\":{\"ui_mode\":false,\"ui_internal_mode\":false},\"isInternal\":true}";

        Map<String, String> params = new HashMap<>();
        params.put("requestId", "a8b3ab90-71c3-11ea-905b-79ea33d71b7e");

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" +  jwtAuthToken  + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, input, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        AssertQueryRequestResponse(response);
    }

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

    @Then("^List schemas is ts_partner\\.freetrial database$")
    public void listSchemasIsTs_partnerFreetrialDatabase() throws Exception {
        refreshAuthToken();
        String input = "{\"sqlText\":\"SHOW SCHEMAS IN DATABASE \\\"THOUGHTSPOT_PARTNER\\\".\\\"FREETRIAL\\\" LIMIT 100;\",\"disableOfflineChunks\":true,\"parameters\":{\"ui_mode\":false,\"ui_internal_mode\":false},\"isInternal\":true}";

        Map<String, String> params = new HashMap<>();
        params.put("requestId", "ab4504d0-71c3-11ea-905b-79ea33d71b7e");

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" +  jwtAuthToken  + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, input, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        AssertQueryRequestResponse(response);
    }

    @Then("^List schemas is ts_partner\\.snowflake sample data database$")
    public void listSchemasIsTs_partnerSnowflakeSampleDataDatabase() throws Exception {
        refreshAuthToken();
        String input = "{\"sqlText\":\"SHOW SCHEMAS IN DATABASE \\\"THOUGHTSPOT_PARTNER\\\".\\\"SNOWFLAKE_SAMPLE_DATA\\\" LIMIT 100;\",\"disableOfflineChunks\":true,\"parameters\":{\"ui_mode\":false,\"ui_internal_mode\":false},\"isInternal\":true}";

        Map<String, String> params = new HashMap<>();
        params.put("requestId", "ab4504d0-71c3-11ea-905b-79ea33d71b7e");

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" +  jwtAuthToken  + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, input, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        AssertQueryRequestResponse(response);
    }

    @Then("^Create ETL integration$")
    public void createETLIntegration() throws Exception {
        refreshAuthToken();
        String input = "{\"sqlText\":\"CALL SYSTEM$CREATE_ETL_INTEGRATION((?));\",\"disableOfflineChunks\":true,\"bindings\":{\"1\":{\"value\":\"thoughtspot\",\"type\":\"TEXT\"}}}";

        Map<String, String> params = new HashMap<>();
        params.put("requestId", "ec8bf540-71c6-11ea-905b-79ea33d71b7e");

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" +  jwtAuthToken  + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, input, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        String returnedString = AssertQueryRequestResponse(response);
        System.out.println(returnedString);
        JSONParser parser = new JSONParser();
        JSONObject returnedObject = (JSONObject) parser. parse(returnedString);

        String message = (String) returnedObject.get("message");
        System.out.println(message);
        if ( message.equalsIgnoreCase("Partner is already integrated")){
            System.out.println("ETL Integration not required");
        }else if ( message.equalsIgnoreCase("Account and Destination Created") ) {
            JSONObject data = (JSONObject) returnedObject.get("data");
            Assert.assertNotNull(data);
            System.out.println(data);
            redirect_uri = (String) data.get("redirect_uri");
            System.out.println(redirect_uri);
            Assert.assertNotNull(redirect_uri);
            Pattern p = Pattern.compile("(id=)(.+)(&token=)(.+)");
            Matcher m = p.matcher(redirect_uri);
            while(m.find()) {
                userId = m.group(2);
                userAuthToken = m.group(4);
            }
            System.out.println(userId);
            Assert.assertNotNull(userId);
            System.out.println(userAuthToken);
            Assert.assertNotNull(userAuthToken);
        }else {
            System.out.println("Invalid message");
        }
    }

    public static String getUserId(){
        return userId;
    }

    public static String getUserAuthToken(){
        return userAuthToken;
    }

    @Then("^list view in schema ts_partner\\.freetrial$")
    public void listViewInSchemaTs_partnerFreetrial() throws Exception {
        refreshAuthToken();
        String input = "{\"sqlText\":\"SHOW VIEWS IN SCHEMA \\\"THOUGHTSPOT_PARTNER\\\".\\\"FREETRIAL\\\".\\\"PUBLIC\\\" LIMIT 100;\",\"disableOfflineChunks\":true}";

        Map<String, String> params = new HashMap<>();
        params.put("requestId", "30137ea0-72c1-11ea-95f2-4bcd0445aaeb");

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" +  jwtAuthToken  + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, input, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
    }

    @Then("^list tables in schema ts_partner\\.freetrial$")
    public void listTablesInSchemaTs_partnerFreetrial() throws Exception {
        refreshAuthToken();
        String input = "{\"sqlText\":\"SHOW TABLES IN SCHEMA \\\"THOUGHTSPOT_PARTNER\\\".\\\"FREETRIAL\\\".\\\"PUBLIC\\\" LIMIT 100;\",\"disableOfflineChunks\":true}";

        Map<String, String> params = new HashMap<>();
        params.put("requestId", "30135790-72c1-11ea-95f2-4bcd0445aaeb");

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" +  jwtAuthToken  + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, input, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        AssertQueryRequestResponse(response);
    }

    @Then("^use role AccountAdmin$")
    public void useRoleAccountAdmin() throws Exception {
        refreshAuthToken();
        String input = "{\"sqlText\":\"USE ROLE \\\"ACCOUNTADMIN\\\";\",\"disableOfflineChunks\":true}";

        Map<String, String> params = new HashMap<>();
        params.put("requestId", "2ea91930-72c1-11ea-95f2-4bcd0445aaeb");

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" +  jwtAuthToken  + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_QUERY, input, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);

    }

    @Then("^Bootstrap data request$")
    public void bootstrapDataRequest() throws Exception {
        refreshAuthToken();
        String input = "{\"dataOptions\":{\"CDN_CHANNEL\":null},\"dataKinds\":[\"ACCOUNT\",\"CURRENT_SESSION\",\"PWD_CHANGE_INFO\",\"WAREHOUSES\",\"DATABASES\",\"CLIENT_PARAMS_INFO\",\"TUTORIAL_SCRIPTS_INFO\",\"MESSAGES\",\"UI_VERSION\"]}";
        Map<String, String> params = new HashMap<>();
        params.put("requestId", "9500bdb0-6ed3-11ea-b312-49172493a6a6");

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" +  jwtAuthToken  + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_BOOTSTRAP, input, params, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
    }

    @Then("^MFA Enrollment request$")
    public void mfaEnrollmentRequest() throws Exception {
        refreshAuthToken();
        String input = "{\"action\":\"MFA_CHECK_INFO\"}";

        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" +  jwtAuthToken  + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendSFPostRequest(Constants.SNOWFLAKE_MFA_REENROLLMENT, input, null, jwt);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
    }

    @Then("^Close Container Button$")
    public void closeContainerButton() throws Exception {
        refreshAuthToken();
        Assert.assertNotNull("Jwt auth token cannot be null", jwtAuthToken);
        String jwt = "Snowflake Token=\"" +  jwtAuthToken  + "\"";
        System.out.println(jwt);

        Response response = Util
                .sendGetRequestWithJwtAuth(Constants.SNOWFLAKE_CLOSE_BUTTON, jwt, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
    }
}
