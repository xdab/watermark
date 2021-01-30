package mini.xdab.singleton;

import lombok.Getter;
import lombok.NonNull;
import mini.xdab.constants.LogConstants;
import mini.xdab.utils.TextUtils;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.ResourceBundle;

public class Log {

    @Getter
    private static Integer loggingLevel;

    @Getter
    private static boolean useStdErr;

    private static boolean isInitialized = false;
    private static SimpleDateFormat dateTimeFormat;


    public static void ultra(@NonNull String message, @Nullable Object... formatArgs) {
        initialize(); log(String.format(message, formatArgs), LogConstants.LEVEL_ULTRA);
    }

    public static void debug(@NonNull String message, @Nullable Object... formatArgs) {
        initialize(); log(String.format(message, formatArgs), LogConstants.LEVEL_DEBUG);
    }

    public static void info(@NonNull String message, @Nullable Object... formatArgs) {
        initialize(); log(String.format(message, formatArgs), LogConstants.LEVEL_INFO);
    }

    public static void warn(@NonNull String message, @Nullable Object... formatArgs) {
        initialize(); log(String.format(message, formatArgs), LogConstants.LEVEL_WARN);
    }

    public static void error(@NonNull String message, @Nullable Object... formatArgs) {
        initialize(); log(String.format(message, formatArgs), LogConstants.LEVEL_ERROR);
    }


    private static void initialize() {
        if (!isInitialized) {
            var properties =  ResourceBundle.getBundle(LogConstants.PROPERTIES_FILE);

            var loggingLevelStr = properties.getString("logging-level");
            parseLoggingLevelString(loggingLevelStr);

            var useStdErrStr = properties.getString("use-stderr");
            parseUseStdErrString(useStdErrStr);

            dateTimeFormat = new SimpleDateFormat(properties.getString("datetime-str-pattern"));
            isInitialized = true;
        }
    }


    private static void log(String message, Integer level) {
        if (level <= loggingLevel)
            return;

        String dateFormat = dateTimeFormat.format(Date.from(Instant.now()));
        String formatted = String.format("LOG LV %03d @ %s > %s", level, dateFormat, message);

        if (useStdErr)
            System.err.println(formatted);
        else
            System.out.println(formatted);
    }

    private static void parseLoggingLevelString(String loggingLevelStr) {
        loggingLevelStr = TextUtils.stripAndLower(loggingLevelStr);
        loggingLevel = LogConstants.levelStrToInt.getOrDefault(loggingLevelStr, LogConstants.LEVEL_INFO);
    }

    private static void parseUseStdErrString(String useStdErrStr) {
        useStdErrStr = TextUtils.stripAndLower(useStdErrStr);
        useStdErr = Boolean.parseBoolean(useStdErrStr);
    }

}
