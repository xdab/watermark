package mini.xdab.utils;

import mini.xdab.constants.BitConstants;


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

    public static boolean goodParity(int of) {
        return (Integer.bitCount(of) & 1) == 0;
    }

    public static boolean isGoodByte(int candidate, int correctByte) {
        candidate &= 0x1ff;
        return goodParity(candidate) && ((candidate >>> 1) == correctByte);
    }

    public static boolean isGoodWord(int candidate, int correctWord) {
        if (!isGoodByte(candidate & 0x1ff, correctWord & 0xff))
            return false;

        candidate >>= 9;
        correctWord >>= 8;
        return isGoodByte(candidate & 0x1ff, correctWord & 0xff);
    }

    public static boolean isGoodSyncWord(Integer candidateSyncWord) {
        return isGoodWord(candidateSyncWord, BitConstants.SYNC_WORD);
    }

    public static boolean isEndWord(Integer candidateEndWord) {
        return (candidateEndWord & 0xffff) == BitConstants.END_WORD;
    }

}
