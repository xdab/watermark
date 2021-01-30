package mini.xdab.constants;

import java.util.HashMap;
import java.util.Map;

public class LogConstants {

    public static final Integer LEVEL_ALL   =   0;
    public static final Integer LEVEL_ULTRA =  99;
    public static final Integer LEVEL_DEBUG = 100;
    public static final Integer LEVEL_INFO  = 111;
    public static final Integer LEVEL_WARN  = 888;
    public static final Integer LEVEL_ERROR = 999;

    public static final String PROPERTIES_FILE = "log";


    public static Map<String, Integer> levelStrToInt;
    static {
        levelStrToInt = new HashMap<>();
        levelStrToInt.put("all",   LEVEL_ALL);
        levelStrToInt.put("ultra", LEVEL_ULTRA);
        levelStrToInt.put("debug", LEVEL_DEBUG);
        levelStrToInt.put("info",  LEVEL_INFO);
        levelStrToInt.put("warn",  LEVEL_WARN);
        levelStrToInt.put("error", LEVEL_ERROR);
    }
}
