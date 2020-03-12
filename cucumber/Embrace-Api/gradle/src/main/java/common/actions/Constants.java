package common.actions;

public class Constants {

    public static final long UI_IMPLICIT_WAIT = 2000;
    public static final String HOST_IP = "172.18.104.3";
    public static final String TS_CLUSTER = "http://" + HOST_IP + ":8088";
    public static final int MAX_FIND_ELEMENT_ATTEMPTS = 3;
    public static final int AUTO_REFRESH_UI_IMPLICIT_WAIT_SECONDS = 3;
    public static final String CUSTOMER_USERNAME = "tsadmin";
    public static final String CUSTOMER_PASSWORD = "admin";
    public static final String CUSTOMER_TYPE = "LOCAL_USER";
    public static final String CUSTOMER_VISIBILITY = "DEFAULT";
    public static final String SECONDARY_CUSTOMER_USERNAME = "admin";
    public static final String SECONDARY_CUSTOMER_PASSWORD = "th0ught$p0t";
    public static final String SESSION = TS_CLUSTER + "/callosum/v1/session/";
    public static final String SESSION_LOGIN = SESSION + "login";
    public static final String SESSION_INFO = SESSION + "info";
    public static final String SESSION_LOGOUT = SESSION + "logout";
    public static final String SESSION_ISACTIVE = SESSION + "isactive";
    public static final String SESSION_USER_CREATE = SESSION + "user/create";
    public static final String SESSION_CREATE_GROUP = SESSION + "group/create";
    public static final String GROUP_NAME = "testGroup";
    public static final String Test = "dummy";
    public static final String DRIVER_LOC="/usr/local/Cellar/geckodriver/0.26.0/bin/geckodriver";
    public static final Boolean ENBALE_UI = false;
}
