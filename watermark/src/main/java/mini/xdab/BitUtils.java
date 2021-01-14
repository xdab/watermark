package mini.xdab;

public class BitUtils {

    public static int getLSB(int of) {
        return (of & 1);
    }

    public static int setLSB(int of, int toValue) {
        //          Clear             Set
        return (of ^ (of & 1)) | (toValue & 1);
    }

    public static int parityBit(int of) {
        return Integer.bitCount(of) & 1;
    }
}
