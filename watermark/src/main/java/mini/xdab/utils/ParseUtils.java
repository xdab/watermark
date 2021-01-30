package mini.xdab.utils;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public class ParseUtils {

    public static Boolean boolOrDefault(@NonNull String str, @Nullable Boolean def) {
        return Boolean.parseBoolean(str);
    }

    public static Integer intOrDefault(@NonNull String str, @NonNull Integer def) {
        try { return Integer.parseInt(str); }
        catch (NumberFormatException nfe) { return def; }
    }

    public static Integer uintOrDefault(@NonNull String str, @NonNull Integer def) {
        try { return Integer.parseUnsignedInt(str); }
        catch (NumberFormatException nfe) { return def; }
    }

}
