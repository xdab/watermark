package mini.xdab.singleton;

import lombok.Getter;
import lombok.NonNull;
import mini.xdab.consts.LogConsts;
import mini.xdab.utils.TextUtils;
import org.jetbrains.annotations.Nullable;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.ResourceBundle;

import static java.util.Objects.isNull;

public class Log {

    @Getter
    private static Integer loggingLevel;

    @Getter
    private static PrintStream printStream;

    @Getter
    private static boolean fastFlush;

    private static boolean isInitialized = false;
    private static SimpleDateFormat dateTimeFormat;


    public static void ultra(@Nullable Object caller, @NonNull String message, @Nullable Object... formatArgs) {
        initialize(); log(LogConsts.LEVEL_ULTRA, caller, message, formatArgs);
    }

    public static void debug(@Nullable Object caller, @NonNull String message, @Nullable Object... formatArgs) {
        initialize(); log(LogConsts.LEVEL_DEBUG, caller, message, formatArgs);
    }

    public static void info(@Nullable Object caller, @NonNull String message, @Nullable Object... formatArgs) {
        initialize(); log(LogConsts.LEVEL_INFO, caller, message, formatArgs);
    }

    public static void warn(@Nullable Object caller, @NonNull String message, @Nullable Object... formatArgs) {
        initialize(); log(LogConsts.LEVEL_WARN, caller, message, formatArgs);
    }

    public static void error(@Nullable Object caller, @NonNull String message, @Nullable Object... formatArgs) {
        initialize(); log(LogConsts.LEVEL_ERROR, caller, message, formatArgs);
    }


    private static void initialize() {
        if (!isInitialized) {
            var properties =  ResourceBundle.getBundle(LogConsts.PROPERTIES_FILE);

            parseLoggingLevelString(properties.getString("logging-level"));
            parseUseStdErrString(properties.getString("use-stderr"));
            parseFastFlushString(properties.getString("fast-flush"));

            dateTimeFormat = new SimpleDateFormat(properties.getString("datetime-str-pattern"));
            isInitialized = true;
        }
    }


    private static void log(Integer level, Object caller, String message, Object... formatArgs) {
        if (level < loggingLevel)
            return;

        String formattedCaller = isNull(caller) ? "" : caller.getClass().getSimpleName();
        String formattedDate = dateTimeFormat.format(Date.from(Instant.now()));

        String formattedMessage = String.format("LOG LV %03d @ %s > %s%s", level, formattedDate, formattedCaller,
                String.format(message, formatArgs));

        printStream.println(formattedMessage);
        if (fastFlush)
            printStream.flush();
    }

    private static void parseLoggingLevelString(String loggingLevelStr) {
        loggingLevelStr = TextUtils.stripAndLower(loggingLevelStr);
        loggingLevel = LogConsts.levelStrToInt.getOrDefault(loggingLevelStr, LogConsts.LEVEL_INFO);
    }

    private static void parseUseStdErrString(String useStdErrStr) {
        useStdErrStr = TextUtils.stripAndLower(useStdErrStr);
        printStream = Boolean.parseBoolean(useStdErrStr) ? System.err : System.out;
    }

    private static void parseFastFlushString(String fastFlushStr) {
        fastFlushStr = TextUtils.stripAndLower(fastFlushStr);
        fastFlush = Boolean.parseBoolean(fastFlushStr);
    }

}
