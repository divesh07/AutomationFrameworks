package common.actions;

public class Constants {

    public static final long UI_IMPLICIT_WAIT = 2000;

    public static final String HOST_IP = "172.18.104.3";

    public static final String TS_CLUSTER = "http://" + HOST_IP + ":8088";

    public static final String CLUSTER_BASE_PATH = TS_CLUSTER + "/callosum/v1/";

    public static final String SNOWFLAKE_HOST_HEADER = "thoughtspot_partner.snowflakecomputing.com";

    public static final String SNOWFLAKE_ORIGIN_HEADER =
            "https://thoughtspot_partner" + ".snowflakecomputing.com";

    public static final String SNOWFLAKE_REFERER_HEADER =
            "https://thoughtspot_partner" + ".snowflakecomputing.com/console/login";

    public static final String TRIAL_HOST = "try.thoughtspot.com";

    public static final String TS_TRIAL_CLUSTER_URL = "https://" + TRIAL_HOST;

    public static final String TS_TRIAL_BASE = TS_TRIAL_CLUSTER_URL + "/callosum/v1/";

    public static final String TS_TRIAL_SESSION = TS_TRIAL_BASE + "session";

    public static final String TS_TRIAL_SYSTEM_CONFIG = TS_TRIAL_BASE + "system/config";

    public static final int MAX_FIND_ELEMENT_ATTEMPTS = 3;

    public static final int AUTO_REFRESH_UI_IMPLICIT_WAIT_SECONDS = 3;

    public static final String CUSTOMER_USERNAME = "tsadmin";

    public static final String CUSTOMER_PASSWORD = "admin";

    public static final String CUSTOMER_DISPLAY_NAME = "Administrator";

    public static final String CUSTOMER_TYPE = "LOCAL_USER";

    public static final String CUSTOMER_VISIBILITY = "DEFAULT";

    public static final String SECONDARY_CUSTOMER_USERNAME = "testUser";

    public static final String SECONDARY_CUSTOMER_PASSWORD = "th0ught$p0t";

    public static final String TRIAL_USERNAME = "sameer.satyam@thoughtspot.com";

    public static final String TRIAL_PASSWORD = "th0ught$p0t";

    public static final String TRIAL_ADMIN_USERNAME = "tsadmin";

    public static final String TRIAL_ADMIN_PASSWORD = "th0ughtSp0t!";

    public static final String TRIAL_USER_DISPLAYNAME = "SAMEER SATYAM";

    public static final String TRIAL_USER_EMAIL = "sameer.satyam@thoughtspot.com";

    public static final String TRIAL_USER_SEARCH_PATTERN ="%25sameer.satyam@thoughtspot.com%25";

    public static final String SNOWFLAKE_DATA_SOURCE = "RDBMS_SNOWFLAKE";

    public static final String SNOWFLAKE_ACCOUNT_NAME = "thoughtspot_partner";

    public static final String SNOWFLAKE_USER = "DIVESH_GANDHI";

    public static final String SNOWFLAKE_PASSWORD = "Embrace123";

    public static final String SNOWFLAKE_ROLE = "DEVELOPER";

    public static final String SNOWFLAKE_WAREHOUSE = "MEDIUM_WH";

    public static final String SNOWFLAKE_DATABASE = "Champagne_UAT";

    public static final String SNOWFLAKE_SCHEMA = "CS_Status_falcon_default_schema";

    public static final String SNOWFLAKE_DEMO_WAREHOUSE = "DEMO_WH";

    public static final String SNOWFLAKE_ACCOUNT_ADMIN = "ACCOUNTADMIN";

    public static final String SNOWFLAKE_CONNECTION_NAME = "testConn";

    public static final String SNOWFLAKE_DESRIPTION = "Desc";

    public static final String SESSION = CLUSTER_BASE_PATH + "session/";

    public static final String SESSION_LOGIN = SESSION + "login";

    public static final String SESSION_INFO = SESSION + "info";

    public static final String SESSION_LOGOUT = SESSION + "logout";

    public static final String SESSION_ISACTIVE = SESSION + "isactive";

    public static final String SESSION_USER_CREATE = SESSION + "user/create";

    public static final String SESSION_PRIVILEGES = SESSION + "privileges";

    public static final String SESSION_MINDTOUCH_TOKEN = SESSION + "mindtouchtoken";

    public static final String SESSION_USER = SESSION + "user";

    public static final String SESSION_USER_DELETE = SESSION_USER + "/delete/";

    public static final String TRIAL_SESSION_LOGIN = TS_TRIAL_SESSION + "/login";

    public static final String TRIAL_SHORT_LIVED_LOGIN = TS_TRIAL_SESSION + "/shortlivedlogin";

    public static final String TRIAL_SESSION_INFO = TS_TRIAL_SESSION + "/info";

    public static final String TRIAL_SESSION_LOGOUT = TS_TRIAL_SESSION + "/logout";

    public static final String TRIAL_USER_ACTIVATE = TS_TRIAL_SESSION + "/user/activate";

    public static final String TRIAL_SESSION_USER_DELETE = TS_TRIAL_SESSION + "/user/deleteusers";

    public static final String TRIAL_SESSION_GROUP_DELETE =
            TS_TRIAL_SESSION + "/group" + "/deletegroups";

    public static final String TRIAL_SESSION_SEARCH_GROUP = TS_TRIAL_BASE + "metadata/list";

    public static final String GROUP_NAME = "testGroup";

    public static final String GROUP_TYPE = "LOCAL_GROUP";

    public static final String GROUP_VISIBILITY = "DEFAULT";

    public static final String GROUP = SESSION + "group/";

    public static final String SESSION_CREATE_GROUP = GROUP + "create";

    public static final String SESSION_GROUP_LIST_USER_HEADERS = GROUP + "listuserheaders";

    public static final String SESSION_USER_GROUP_DELETE = GROUP + "/delete/";

    public static final String CONNECTION = CLUSTER_BASE_PATH + "connection/";

    public static final String CONNECTION_TYPES = CONNECTION + "types";

    public static final String CONNECTION_LIST = CONNECTION + "list";

    public static final String CONNECTION_TABLE_STATS = CONNECTION + "tableStats";

    public static final String CONNECTION_STATUS = CONNECTION + "status";

    public static final String CONNECTION_CONNECT = CONNECTION + "connect";

    public static final String CONNECTION_CONFIG = CONNECTION + "config";

    public static final String CONNECTION_EXPORT = CONNECTION + "export";

    public static final String CONNECTION_REMOVEDATA = CONNECTION + "removedata";

    public static final String CONNECTION_GETSCHEDULEDJOB = CONNECTION + "getScheduledJob";

    public static final String CONNECTION_FETCHCONNECTION = CONNECTION + "fetchConnection";

    public static final String CONNECTION_DETAIL = CONNECTION + "detail";

    public static final String SNOWFLAKE_BASE = "https://thoughtspot_partner.snowflakecomputing.com/";

    public static final String SNOWFLAKE_SESSION = SNOWFLAKE_BASE + "session/v1/";

    public static final String SNOWFLAKE_LOGIN = SNOWFLAKE_SESSION + "login-request";

    public static final String SNOWFLAKE_CONSOLE = SNOWFLAKE_BASE + "console/";

    public static final String SNOWFLAKE_QUERY = SNOWFLAKE_BASE + "queries/v1/query-request";

    public static final String SNOWFLAKE_LOGOUT = SNOWFLAKE_BASE + "session/logout-request";

    public static final String SNOWFLAKE_ACTION = SNOWFLAKE_CONSOLE + "action-request";

    public static final String SNOWFLAKE_BOOTSTRAP = SNOWFLAKE_CONSOLE + "bootstrap-data-request";

    public static final String SNOWFLAKE_MFA_REENROLLMENT =
            SNOWFLAKE_BASE + "session/mfa" + "-enrollment-request";

    public static final String SNOWFLAKE_CLOSE_BUTTON =
            SNOWFLAKE_BASE + "assets/ui/resources/scripts/sleet/resources/images/button-close.svg";

    public static final String DRIVER_LOC = "/usr/local/Cellar/geckodriver/0.26.0/bin/geckodriver";

    public static final String INVALID_AUTH_TOKEN = "der23242mdbbfsnfhsfisad";

    public static final Boolean ENBALE_UI = false;
}
