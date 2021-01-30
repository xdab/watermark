package mini.xdab.utils;

public class RGBUtils {

    private static final int RGB_LSB_MASK = 0x010101;


    public static int getChannelMask(int channel) {
        return (0xff << ((2-channel) * 8));
    }

    public static int getChannelLSBMask(int channel) {
        return (0x01 << ((2-channel) * 8));
    }

    public static int getChannel(int rgb, int channel) {
        return (rgb >> ((2-channel) * 8)) & 0xff;
    }

    public static int getChannelLSB(int rgb, int channel) {
        return getChannel(rgb, channel) & 1;
    }

    public static int getChannelLSBs(int rgb) {
        return ((rgb & 0x010000) >> 14) | ((rgb & 0x000100) >> 7) | (rgb & 0x000001);
    }

    public static int zeroChannel(int rgb, int channel) {
        return rgb & (~getChannelMask(channel));
    }

    public static int zeroChannelLSB(int rgb, int channel) {
        return rgb & (~getChannelLSBMask(channel));
    }

    public static int setChannel(int rgb, int channel, int value) {
        value &= 0xff;
        return zeroChannel(rgb, channel) | (value << ((2-channel) * 8));
    }

    public static int setChannelLSB(int rgb, int channel, int value) {
        value = (value > 0) ? 1 : 0;
        return zeroChannelLSB(rgb, channel) | (value << ((2-channel) * 8));
    }

    public static int setChannelLSBs(int rgb, int threeBits) {
        int setR = (threeBits & 0b100) << 14;
        int setG = (threeBits & 0b010) << 7;
        int setB = (threeBits & 0b001);

        rgb &= ~RGB_LSB_MASK; // Zero current values
        rgb |= (setR | setG | setB); // Set new ones

        return rgb;
    }

}
