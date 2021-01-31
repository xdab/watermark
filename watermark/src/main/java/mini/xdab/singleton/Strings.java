package mini.xdab.singleton;

import java.util.Locale;
import java.util.ResourceBundle;


public final class Strings {

    private static volatile ResourceBundle stringsResourceBundle;

    private static ResourceBundle getStringsResourceBundle() {
        synchronized(Strings.class) {
            if (stringsResourceBundle == null)
                stringsResourceBundle = ResourceBundle.getBundle("strings", Locale.getDefault());
        }
        return stringsResourceBundle;
    }

    public static String get(String key) {
        return getStringsResourceBundle().getString(key);
    }

}
