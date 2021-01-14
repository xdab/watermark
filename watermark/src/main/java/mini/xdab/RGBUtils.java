package mini.xdab;

public class RGBUtils {

    public static final int RGB_LSB_MASK = 0x010101;

    public static int setChannelLSBs(int rgb, int threeBits) {
        int setR = (threeBits & 0b100) << 14;
        int setG = (threeBits & 0b010) << 7;
        int setB = (threeBits & 0b001);

        rgb &= ~RGB_LSB_MASK; // Zero current values
        rgb |= (setR | setG | setB); // Set new ones

        return rgb;
    }

    public static int getChannelLSBs(int rgb) {
        return ((rgb & 0x010000) >> 14) | ((rgb & 0x000100) >> 7) | (rgb & 0x000001);
    }
}
