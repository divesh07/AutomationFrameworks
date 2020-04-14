package common.actions;

public class SFConstants {

    public static final String REFRESH_AUTH_TOKEN =
            "{\"data\":{\"ACCOUNT_NAME" + "\":\"THOUGHTSPOT_PARTNER\","
                    + "\"LOGIN_NAME\":\"DEMO_ADMIN\",\"PASSWORD\":\"demo_admin\","
                    + "\"CLIENT_APP_ID\":\"Snowflake UI\"}}";

    public static final String SF_LOGIN = "{\"data\":{\"ACCOUNT_NAME\":\"THOUGHTSPOT_PARTNER\","
            + "\"LOGIN_NAME\":\"DEMO_ADMIN\",\"PASSWORD\":\"demo_admin\","
            + "\"CLIENT_APP_ID\":\"Snowflake UI\"}}";

    public static final String ACCESS_CONSOLE = "{\"action\":\"LOG_UI_USAGE\","
            + "\"actionDataEncoded\":{\"logs\":[{\"message\":\"{\\\"type\\\":\\\"ui_exit\\\","
            + "\\\"interaction\\\":\\\"true\\\",\\\"flags\\\":{},\\\"data\\\":{\\\"networkSnapshot\\\":null},\\\"url\\\":\\\"/\\\"}\",\"timestamp\":1585044505804}]}}";

    public static final String REMOVE_ETL_INTEGRATION = "{\"sqlText\":\"select "
            + "system$remove_etl_integration('thoughtspot');\",\"disableOfflineChunks\":false}";

    public static final String LIST_ALL_DATABASES =
            "{\"sqlText\":\"SHOW DATABASES;\",\"disableOfflineChunks\":true,"
                    + "\"isInternal\":true}";

    public static final String LIST_DATABASES_TS_PARTNER =
            "{\"sqlText\":\"SHOW DATABASES IN ACCOUNT \\\"THOUGHTSPOT_PARTNER\\\";\","
                    + "\"disableOfflineChunks\":true}";

    public static final String SHOW_GRANTS_USER = "{\"sqlText\":\"SHOW GRANTS TO USER identifier"
            + "(?);\",\"disableOfflineChunks\":true,\"bindings\":{\"1\":{\"value\":\"\\\"DEMO_ADMIN\\\"\",\"type\":\"TEXT\"}}}";

    public static final String CURRENT_AVAILABLE_ROLES =
            "{\"sqlText\":\"SELECT CURRENT_AVAILABLE_ROLES"
                    + "() AS \\\"ROLES\\\";\",\"disableOfflineChunks\":true}";

    public static final String MANAGED_ACCOUNTS =
            "{\"sqlText\":\"show managed accounts;" + "\",\"disableOfflineChunks\":true}";

    public static final String SHOW_SHARES =
            "{\"sqlText\":\"SHOW SHARES;\"," + "\"disableOfflineChunks\":true}";

    public static final String SHOW_SCHEMA = "{\"sqlText\":\"SHOW SCHEMAS IN DATABASE "
            + "\\\"THOUGHTSPOT_PARTNER\\\".\\\"FREETRIAL\\\" LIMIT 100;\","
            + "\"disableOfflineChunks\":true}";

    public static final String SHOW_SCHEMA_SF_SAMPLE = "{\"sqlText\":\"SHOW SCHEMAS IN DATABASE "
            + "\\\"THOUGHTSPOT_PARTNER\\\".\\\"SNOWFLAKE_SAMPLE_DATA\\\" LIMIT 100;\","
            + "\"disableOfflineChunks\":true}";

    public static final String CREATE_ETL_INTEGRATION = "{\"sqlText\":\"CALL "
            + "SYSTEM$CREATE_ETL_INTEGRATION((?));\",\"disableOfflineChunks\":true,"
            + "\"bindings\":{\"1\":{\"value\":\"thoughtspot\",\"type\":\"TEXT\"}}}";

    public static final String LIST_VIEWS = "{\"sqlText\":\"SHOW VIEWS IN SCHEMA "
            + "\\\"THOUGHTSPOT_PARTNER\\\".\\\"FREETRIAL\\\".\\\"PUBLIC\\\" LIMIT 100;\",\"disableOfflineChunks\":true}";

    public static final String LIST_TABLES = "{\"sqlText\":\"SHOW TABLES IN SCHEMA "
            + "\\\"THOUGHTSPOT_PARTNER\\\".\\\"FREETRIAL\\\".\\\"PUBLIC\\\" LIMIT 100;\",\"disableOfflineChunks\":true}";

    public static final String USE_ROLE =
            "{\"sqlText\":\"USE ROLE \\\"ACCOUNTADMIN\\\";\"," + "\"disableOfflineChunks\":true}";

    public static final String BOOTSTRAP = "{\"dataOptions\":{\"CDN_CHANNEL\":null},"
            + "\"dataKinds\":[\"ACCOUNT\",\"CURRENT_SESSION\",\"PWD_CHANGE_INFO\",\"WAREHOUSES\",\"DATABASES\",\"CLIENT_PARAMS_INFO\",\"TUTORIAL_SCRIPTS_INFO\",\"MESSAGES\",\"UI_VERSION\"]}";

    public static final String MFA_CHECK_INFO = "{\"action\":\"MFA_CHECK_INFO\"}";

    public static final String ETL_REQUEST_ID = "9500bdb0-6ed3-11ea-b312-49172493a6a6";

    public static final String LIST_DB_REQUEST_ID = "a690d220-71f0-11ea-9b98-b78aeac24c53";

    public static final String SHOW_GRANTS_REQUEST_ID = "cd92b050-6eda-11ea-9405-0f010fb453cb";

    public static final String MANAGED_ACCOUNTS_REQUEST_ID = "a8b33660-71c3-11ea-905b-79ea33d71b7e";

    public static final String SHOW_SHARES_REQUEST_ID = "a8b3ab90-71c3-11ea-905b-79ea33d71b7e";

    public static final String SHOW_SCHEMA_REQUEST_ID = "ab4504d0-71c3-11ea-905b-79ea33d71b7e";

    public static final String ETL_INTEGRATION_REQUEST_ID = "ec8bf540-71c6-11ea-905b-79ea33d71b7e";

    public static final String VIEWS_TABLES_REQUEST_ID = "30137ea0-72c1-11ea-95f2-4bcd0445aaeb";

    public static final String ROLES_REQUEST_ID = "2ea91930-72c1-11ea-95f2-4bcd0445aaeb";

}
