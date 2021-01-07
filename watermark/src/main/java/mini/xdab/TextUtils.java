package mini.xdab;

public class TextUtils {

    public static boolean isPrintable(char c) {
        return (c == '\n') || ((c >= 32) && (c < 127));
    }


    public static String bytesToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            if (TextUtils.isPrintable((char)b)) {
                sb.append((char)b);
            } else {
                sb.append("\\x");
                sb.append(Integer.toHexString(b));
            }
        }
        return sb.toString();
    }

}
