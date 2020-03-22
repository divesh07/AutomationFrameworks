package common.actions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class APIActions {
    private static final Logger LOG = LoggerFactory.getLogger(APIActions.class);

    private String userId = "";

    private String groupId = "";

    private String tableId = "";

    private String tableName = "";

    private String databaseName = "";

    private String connectionId = "";

    // TODO : Add logic to enable this only when UI validation flag is turned ON
    //private final WebDriver driver;

    /*public APIActions(SharedDriver driver) {
        this.driver = driver;
    }*/

    @Given("validate session login")
    public void sessionLogin() throws Exception {
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("username", Constants.CUSTOMER_USERNAME);
        formData.add("password", Constants.CUSTOMER_PASSWORD);

        Response sessionLogin = Util
                .sendPostRequestNoAuth(Constants.SESSION_LOGIN, formData, null);
        System.out.println(sessionLogin);
        Util.verifyExpectedResponse(sessionLogin, Response.Status.OK);
        Cookie sessionId = sessionLogin.getCookies().get("JSESSIONID");
        Assert.assertNotNull("Cookie cannot be empty", sessionId);
        System.out.println(sessionId);

        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser
                .parse(Util.readResponse(sessionLogin));
        System.out.println(responseObject);

        String username = (String) responseObject.get("userName");
        Assert.assertTrue(username.equals(Constants.CUSTOMER_USERNAME));
        String displayName = (String) responseObject.get("userDisplayName");
        Assert.assertTrue(displayName.equals(Constants.CUSTOMER_DISPLAY_NAME));
        String userGUID = (String) responseObject.get("userGUID");
        Assert.assertNotNull("User GUID cannot be empty", userGUID);

        System.out.println("User guid : "+ userGUID);
        userId = userGUID;
    }

    @Then("^validate session info$")
    public void validateSessionInfo() throws Exception {
        Response response = Util.sendGetRequestAuth(Constants.SESSION_INFO, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        System.out.println(responseObject);
    }

    @Then("^validate session isActive$")
    public void validateSessionIsActive() throws Exception {
        Response response = Util.sendGetRequestAuth(Constants.SESSION_ISACTIVE, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.NO_CONTENT);
    }

    @Then("^get session privileges$")
    public void getSessionPrivileges() throws Exception {
        Response response = Util.sendGetRequestAuth(Constants.SESSION_PRIVILEGES, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        JSONParser responseParser = new JSONParser();
        JSONArray responseObject = (JSONArray) responseParser.parse(Util.readResponse(response));
        System.out.println(responseObject.toString());
        Assert.assertTrue("", responseObject.toString().contains("RANALYSIS"));
        Assert.assertTrue("", responseObject.toString().contains("EXPERIMENTALFEATUREPRIVILEGE"));
        Assert.assertTrue("", responseObject.toString().contains("BYPASSRLS"));
        Assert.assertTrue("", responseObject.toString().contains("JOBSCHEDULING"));
        Assert.assertTrue("", responseObject.toString().contains("SYSTEMMANAGEMENT"));
        Assert.assertTrue("", responseObject.toString().contains("ADMINISTRATION"));
        Assert.assertTrue("", responseObject.toString().contains("AUTHORING"));
        Assert.assertTrue("", responseObject.toString().contains("DATAMANAGEMENT"));
        Assert.assertTrue("", responseObject.toString().contains("USERDATAUPLOADING"));
        Assert.assertTrue("", responseObject.toString().contains("SHAREWITHALL"));
        Assert.assertTrue("", responseObject.toString().contains("DATADOWNLOADING"));
    }

    @Then("^get user guid of the current session$")
    public void getUserGuid() throws Exception {
        Response response = Util.sendGetRequestAuth(Constants.SESSION_USER, null);
        System.out.println(response);
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
        System.out.println(response);
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
        formData.add("usertype", Constants.CUSTOMER_TYPE);
        formData.add("visibility", Constants.CUSTOMER_VISIBILITY);

        Response response = Util
                .sendPostRequestWithAuthCookie(Constants.SESSION_USER_CREATE, formData, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);

        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        JSONObject header = (JSONObject) responseObject.get("header");
        String username = (String) header.get("name");
        Assert.assertTrue(username.equals(Constants.SECONDARY_CUSTOMER_USERNAME));
        userId = (String) header.get("id");
        Assert.assertNotNull("ID cannot be null", userId);

        String type = (String) responseObject.get("type");
        Assert.assertTrue(type.equals(Constants.CUSTOMER_TYPE));
        String visibility = (String) responseObject.get("visibility");
        Assert.assertTrue(visibility.equals(Constants.CUSTOMER_VISIBILITY));
        String displayName = (String) responseObject.get("displayName");
        Assert.assertTrue(displayName.equals(Constants.SECONDARY_CUSTOMER_USERNAME));
        String tenantId = (String) responseObject.get("tenantId");
        Assert.assertNotNull("Tenant ID cannot be null", tenantId);
    }

    @Then("^create duplicate user$")
    public void validateFailureCreationOfDuplicateUser() throws Exception {
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("name", Constants.SECONDARY_CUSTOMER_USERNAME);
        formData.add("password", Constants.SECONDARY_CUSTOMER_PASSWORD);
        formData.add("displayname", Constants.SECONDARY_CUSTOMER_USERNAME);
        formData.add("usertype", Constants.CUSTOMER_TYPE);
        formData.add("visibility", Constants.CUSTOMER_VISIBILITY);

        Response response = Util
                .sendPostRequestWithAuthCookie(Constants.SESSION_USER_CREATE, formData, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.CONFLICT);
        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        String failureReason = (String) responseObject.get("debug");
        Assert.assertTrue("Duplicate username not found as a failure reason",failureReason.contains(
                "Duplicate username"));
    }

    @Then("^create a group$")
    public void createGroup() throws Exception {
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("name", Constants.GROUP_NAME);
        formData.add("display_name", Constants.GROUP_NAME);
        formData.add("usertype", Constants.GROUP_TYPE);
        formData.add("visibility", Constants.GROUP_VISIBILITY);

        Response response = Util
                .sendPostRequestWithAuthCookie(Constants.SESSION_CREATE_GROUP, formData, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        JSONObject header = (JSONObject) responseObject.get("header");
        String username = (String) header.get("name");
        Assert.assertTrue(username.equals(Constants.GROUP_NAME));
        groupId = (String) header.get("id");
        Assert.assertNotNull("Id cannot be null", groupId);

        String type = (String) responseObject.get("type");
        Assert.assertTrue(type.equals(Constants.GROUP_TYPE));
        String visibility = (String) responseObject.get("visibility");
        Assert.assertTrue(visibility.equals(Constants.GROUP_VISIBILITY));
        String displayName = (String) responseObject.get("displayName");
        Assert.assertTrue(displayName.equals(Constants.GROUP_NAME));
        String tenantId = (String) responseObject.get("tenantId");
        Assert.assertNotNull("Tenant ID cannot be null", tenantId);
    }

    @Then("^list member users for a group$")
    public void listGroupMembers() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("id", groupId);
        Response response = Util
                .sendGetRequestAuth(Constants.SESSION_GROUP_LIST_USER_HEADERS, params);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
    }

    @Then("^fetch mindtouch authentication token$")
    public void fetchMindTouchAuthToken() throws Exception {
        Response response = Util.sendGetRequestAuth(Constants.SESSION_MINDTOUCH_TOKEN, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
    }

    @Then("^delete an existing user$")
    public void deleteUser() throws Exception {
        String path = Constants.SESSION_USER_DELETE + userId;
        System.out.println(path);
        Response response = Util.sendDeleteRequestAuth(path, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.NO_CONTENT);
    }

    @Then("^delete existing user group$")
    public void deleteUserGroup() throws Exception {
        String path = Constants.SESSION_USER_GROUP_DELETE + groupId;
        System.out.println(path);
        Response response = Util.sendDeleteRequestAuth(path, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.NO_CONTENT);
    }

    @Then("^validate session logout$")
    public void validateSessionLogout() throws Exception {
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();

        Response response = Util
                .sendPostRequestWithAuthCookie(Constants.SESSION_LOGOUT, formData, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.NO_CONTENT);
    }

    @Given("^fetch Connection types$")
    public void fetchConnectionTypes() throws Exception {
        Response response = Util
                .sendGetRequestAuth(Constants.CONNECTION_TYPES, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);

        JSONParser responseParser = new JSONParser();
        JSONArray responseObject = (JSONArray) responseParser.parse(Util.readResponse(response));

        Assert.assertTrue("Expected support for four data warehouses", responseObject.size() == 4);

        Iterator<JSONObject> iterator = responseObject.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
            Assert.assertNotNull(iterator.next().get("name"));
            Assert.assertNotNull(iterator.next().get("displayName"));
            Assert.assertNotNull(iterator.next().get("enabled"));
        }
    }

    @Then("^Get the list of live query connections$")
    public void getTheListOfLiveQueryConnections() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("category", "ALL");
        params.put("sort", "DEFAULT");
        params.put("offset", "-1");

        Response response = Util
                .sendGetRequestAuth(Constants.CONNECTION_LIST, params);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);

        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        System.out.println(responseObject);

        Boolean value = (Boolean) responseObject.get("isLastBatch");
        JSONObject debugInfo = (JSONObject) responseObject.get("debugInfo");
        JSONObject memcacheAccessDetails = (JSONObject) responseObject.get("memcacheAccessDetails");
        JSONObject requestCacheAccessDetails = (JSONObject) responseObject.get("requestCacheAccessDetails");
        JSONObject objectCacheAccessDetails = (JSONObject) responseObject.get("objectCacheAccessDetails");

        JSONArray headers = (JSONArray) responseObject.get("headers");
        Assert.assertTrue("Expected headers", headers.size() > 0);

        Iterator<JSONObject> iterator = headers.iterator();
        while(iterator.hasNext()){
            JSONObject headerObject = iterator.next();
            System.out.println(headerObject);

            Assert.assertNotNull(headerObject.get("name"));
            Assert.assertNotNull(headerObject.get("id"));
            if (headerObject.get("name").toString().equalsIgnoreCase(Constants.SNOWFLAKE_CONNECTION_NAME) ) {
                connectionId = headerObject.get("id").toString();
            }
            Assert.assertNotNull(headerObject.get("indexVersion"));
            Assert.assertNotNull(headerObject.get("indexVersion"));

            Assert.assertNotNull(headerObject.get("author"));
            Assert.assertNotNull(headerObject.get("authorName"));
            Assert.assertNotNull(headerObject.get("authorDisplayName"));
            Assert.assertNotNull(headerObject.get("created"));
            Assert.assertNotNull(headerObject.get("modified"));
            Assert.assertNotNull(headerObject.get("modifiedBy"));
            Assert.assertNotNull(headerObject.get("owner"));
        }
        Assert.assertNotNull("Connection Id cannot be null", connectionId);
    }

    @Then("^Get connection Table Stats$")
    public void getConnectionTableStats() throws Exception {
        Response response = Util
                .sendGetRequestAuth(Constants.CONNECTION_TABLE_STATS, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);

        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));

        Assert.assertNotNull(responseObject.get("startEpoch"));
        Assert.assertNotNull(responseObject.get("endEpoch"));

        JSONArray statsList = (JSONArray) responseObject.get("tableStats");

        Iterator<JSONObject> iterator = statsList.iterator();
        while(iterator.hasNext()) {
            JSONObject statsObject = iterator.next();
            System.out.println(statsObject);

            Assert.assertNotNull(statsObject.get("dataSourceId"));
            Assert.assertNotNull(statsObject.get("dataSourceType"));
            Assert.assertNotNull(statsObject.get("dataSourceName"));
            Assert.assertNotNull(statsObject.get("tableId"));
            tableId = (String) statsObject.get("tableId");
            Assert.assertNotNull(statsObject.get("tableName"));
            tableName = (String) statsObject.get("tableName");
            Assert.assertNotNull(statsObject.get("numOfRows"));
            Assert.assertNotNull(statsObject.get("status"));

            JSONObject tableMappingInfo = (JSONObject) statsObject.get("tableMappingInfo");
            Assert.assertNotNull(tableMappingInfo.get("databaseName"));
            databaseName = (String) tableMappingInfo.get("databaseName");
            Assert.assertNotNull(tableMappingInfo.get("schemaName"));
            Assert.assertNotNull(tableMappingInfo.get("tableName"));
            Assert.assertNotNull(tableMappingInfo.get("tableType"));

            JSONObject cachingInfo = (JSONObject) tableMappingInfo.get("cachingInfo");
            Assert.assertNotNull(cachingInfo.get("isCached"));
            Assert.assertNotNull(cachingInfo.get("lastLoadTime"));
            Assert.assertNotNull(cachingInfo.get("type"));
        }

    }

    @Then("^Get connection status$")
    public void getConnectionStatus() throws Exception {
        Assert.assertNotNull(tableId);
        System.out.println(tableId);

        Map<String, String> params = new HashMap<>();
        params.put("tableguid", tableId);

        Response response = Util
                .sendGetRequestAuth(Constants.CONNECTION_STATUS, params);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);

        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        System.out.println(responseObject);
        Assert.assertNotNull(responseObject.get("lastLoadTime"));
        Assert.assertNotNull(responseObject.get("isCached"));
        Assert.assertNotNull(responseObject.get("type"));
        Assert.assertNotNull(responseObject.get("timelyJobsStatuses"));

    }

    @Then("^Connect and fetch metadata of Snowflake Connection$")
    public void connectAndFetchMetadataOfSnowflakeConnection() throws Exception {
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("type", Constants.SNOWFLAKE_DATA_SOURCE);
        formData.add("config", "{\"accountName\":\"" + Constants.SNOWFLAKE_ACCOUNT_NAME +"\","
                + "\"user\":\"" + Constants.SNOWFLAKE_USER + "\",\"password\":\"" + Constants.SNOWFLAKE_PASSWORD + "\","
                + "\"role\":\"" + Constants.SNOWFLAKE_ROLE + "\","
                + "\"warehouse\":\"" + Constants.SNOWFLAKE_WAREHOUSE + "\",\"database\":\"" + Constants.SNOWFLAKE_DATABASE + "\","
                + "\"schema\":\"" + Constants.SNOWFLAKE_SCHEMA + "\","
                + "\"connection_name\":\"" + Constants.SNOWFLAKE_CONNECTION_NAME + "\","
                + "\"description\":\"" + Constants.SNOWFLAKE_DESRIPTION + "\"}");

        Response response = Util
                .sendPostRequestWithAuthCookie(Constants.CONNECTION_CONNECT, formData, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        System.out.println(responseObject);
        JSONObject configuration = (JSONObject) responseObject.get("configuration");
        System.out.println(configuration);

        Assert.assertEquals(Constants.SNOWFLAKE_ACCOUNT_NAME, (String) configuration.get("accountName"));
        Assert.assertEquals(Constants.SNOWFLAKE_USER, (String) configuration.get("user"));
        Assert.assertEquals(Constants.SNOWFLAKE_PASSWORD, (String) configuration.get("password"));
        Assert.assertEquals(Constants.SNOWFLAKE_ROLE, (String) configuration.get("role"));
        Assert.assertEquals(Constants.SNOWFLAKE_WAREHOUSE, (String) configuration.get("warehouse"));
        Assert.assertEquals(Constants.SNOWFLAKE_DATABASE, (String) configuration.get("database"));
        Assert.assertEquals(Constants.SNOWFLAKE_CONNECTION_NAME, (String) configuration.get("connection_name"));
        Assert.assertEquals(Constants.SNOWFLAKE_DESRIPTION, (String) configuration.get("description"));
    }

    public void assertStringAndRequiredFields(JSONObject configObject){
        Assert.assertTrue(configObject.get("dataType").toString().equalsIgnoreCase(
                "STRING"));
        Assert.assertTrue(configObject.get("required").toString().equalsIgnoreCase(
                "true"));
        Assert.assertTrue(configObject.get("masked").toString().equalsIgnoreCase(
                "false"));
    }

    @Then("^Get the configuration properties for Snowflake Connection Type$")
    public void getTheConfigurationPropertiesForSnowflakeConnectionType() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("type", Constants.SNOWFLAKE_DATA_SOURCE);

        Response response = Util
                .sendGetRequestAuth(Constants.CONNECTION_CONFIG, params);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);

        JSONParser responseParser = new JSONParser();
        JSONArray responseList = (JSONArray) responseParser.parse(Util.readResponse(response));
        System.out.println(responseList);

        Iterator<JSONObject> iterator = responseList.iterator();
        while(iterator.hasNext()) {
            JSONObject configObject = iterator.next();
            System.out.println(configObject);

            Assert.assertNotNull(configObject.get("name"));
            if(configObject.get("name").toString().equalsIgnoreCase("accountName") ||
                    configObject.get("name").toString().equalsIgnoreCase("user") ||
                    configObject.get("name").toString().equalsIgnoreCase("role") ||
                    configObject.get("name").toString().equalsIgnoreCase("warehouse") ){
                assertStringAndRequiredFields(configObject);
            }
        }
    }

    @Then("^Export connection as YAML$")
    public void exportConnectionAsYAML() throws Exception {
        Assert.assertNotNull(connectionId);
        Map<String, String> params = new HashMap<>();
        params.put("id", connectionId);

        Response response = Util
                .sendGetRequestAuthWithContentType(Constants.CONNECTION_EXPORT, params,
                        "application/octet-stream");
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);

        Assert.assertTrue(response.getHeaderString("content-disposition").contains("connection"
                + ".yaml"));

    }

    @Then("^Remove data from falcon for a given external table$")
    public void removeDataFromFalconForAGivenExternalTable() throws Exception {
        Assert.assertNotNull("table id cannot be null", tableId);
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("id", tableId);

        Response response = Util
                .sendPostRequestWithAuthCookie(Constants.CONNECTION_REMOVEDATA, formData, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.NO_CONTENT);
    }

    @Then("^Get the scheduled timely job schedule$")
    public void getTheScheduledTimelyJobSchedule() throws Exception {
        Assert.assertNotNull("table id cannot be null", tableId);
        Map<String, String> params = new HashMap<>();
        params.put("tableguid", tableId);

        Response response = Util
                .sendGetRequestAuth(Constants.CONNECTION_GETSCHEDULEDJOB, params);

        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.NO_CONTENT);
    }

    @Then("^Fetch the connection details$")
    public void fetchTheConnectionDetails() throws Exception {
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("id", connectionId);
        formData.add("config", "{\"accountName\":\"" + Constants.SNOWFLAKE_ACCOUNT_NAME +"\","
                + "\"user\":\"" + Constants.SNOWFLAKE_USER + "\",\"password\":\"" + Constants.SNOWFLAKE_PASSWORD + "\","
                + "\"role\":\"" + Constants.SNOWFLAKE_ROLE + "\","
                + "\"warehouse\":\"" + Constants.SNOWFLAKE_WAREHOUSE + "\",\"database\":\"" + Constants.SNOWFLAKE_DATABASE + "\","
                + "\"schema\":\"" + Constants.SNOWFLAKE_SCHEMA + "\",");

        Response response = Util
                .sendPostRequestWithAuthCookie(Constants.CONNECTION_FETCHCONNECTION, formData, null);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);
        JSONParser responseParser = new JSONParser();
        JSONObject responseObject = (JSONObject) responseParser.parse(Util.readResponse(response));
        System.out.println(responseObject);
        JSONObject configuration = (JSONObject) responseObject.get("configuration");
        System.out.println(configuration);
        String password = (String) configuration.get("password");
        Assert.assertTrue("Mimstach password",
                password.equalsIgnoreCase(Constants.SNOWFLAKE_PASSWORD));
        String database = (String) configuration.get("database");
        Assert.assertTrue("Mimstach database",
                database.equalsIgnoreCase(Constants.SNOWFLAKE_DATABASE));
        String role = (String) configuration.get("role");
        Assert.assertTrue("Mimstach role",
                role.equalsIgnoreCase(Constants.SNOWFLAKE_ROLE));
        String accountName = (String) configuration.get("accountName");
        Assert.assertTrue("Mimstach accountName",
                accountName.equalsIgnoreCase(Constants.SNOWFLAKE_ACCOUNT_NAME));
        String warehouse = (String) configuration.get("warehouse");
        Assert.assertTrue("Mimstach warehouse",
                warehouse.equalsIgnoreCase(Constants.SNOWFLAKE_WAREHOUSE));
        String user = (String) configuration.get("user");
        Assert.assertTrue("Mimstach user",
                user.equalsIgnoreCase(Constants.SNOWFLAKE_USER));

        JSONArray externalDatabases = (JSONArray) responseObject.get("externalDatabases");
        System.out.println(configuration);
        Assert.assertTrue(externalDatabases.size() > 0);

    }

    @Then("^Get the list of logical tables that exists in the connection$")
    public void getTheListOfLogicalTablesThatExistsInTheConnection() {
        Assert.assertNotNull("connection id cannot be null", connectionId);
        Map<String, String> params = new HashMap<>();
        params.put("id", connectionId);
        params.put("id", connectionId);

        Response response = Util
                .sendGetRequestAuth(Constants.CONNECTION_CONFIG, params);
        System.out.println(response);
        Util.verifyExpectedResponse(response, Response.Status.OK);

        JSONParser responseParser = new JSONParser();
        JSONArray responseList = (JSONArray) responseParser.parse(Util.readResponse(response));
        System.out.println(responseList);

    }

}
