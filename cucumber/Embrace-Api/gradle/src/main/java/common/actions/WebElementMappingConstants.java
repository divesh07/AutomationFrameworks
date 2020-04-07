package common.actions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WebElementMappingConstants {
    public static final Map<String, String> uiToTest;
    static {
        Map<String, String> uiToTestTemp = new HashMap<>();
        uiToTestTemp.put("Login", "//button[contains(text(),'')]");
        uiToTest = Collections.unmodifiableMap(uiToTestTemp);
    }
}
