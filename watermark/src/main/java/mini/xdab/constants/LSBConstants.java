package mini.xdab.constants;

public class LSBConstants {

    public static final Integer SYNC_WORD = 0x5b5b; // '[['
    public static final Integer END_WORD  = 0x2424; // '$$' (almost ~SYNC_WORD)

    public static final Integer NUM_SYNC_WORDS = 1;
    public static final Integer NUM_END_WORDS = 1;

    public static final Integer BYTE_SIZE_PIXELS = 3;
    public static final Integer WORD_SIZE_PIXELS = 2 * BYTE_SIZE_PIXELS;
    public static final Integer THIRD_BIT_TRIPLET_MASK = 0b111000000;

}
