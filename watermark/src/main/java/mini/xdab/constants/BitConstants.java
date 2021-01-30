package mini.xdab.constants;

public class BitConstants {

    public static final Integer SYNC_WORD = 0x5b5b; // '[['
    public static final Integer  END_WORD = 0x2424; // '$$' (almost ~SYNC_WORD)

    // with parities
    public static final Integer GOOD_SYNC_WORD = 0x16eb7;
    public static final Integer  GOOD_END_WORD = 0x09048;

    public static final Integer  THIRD_BIT_TRIPLET_MASK = 0x1c0;
    public static final Integer SECOND_BIT_TRIPLET_MASK = 0x038;
    public static final Integer  FIRST_BIT_TRIPLET_MASK = 0x007;

}
