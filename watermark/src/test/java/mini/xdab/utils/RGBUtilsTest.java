package mini.xdab.utils;

import static org.junit.Assert.*;

import mini.xdab.consts.Consts;
import org.junit.Test;


public class RGBUtilsTest {

    @Test
    public void testSetChannelLSBs()
    {
        assertEquals(RGBUtils.setChannelLSBs(0x000000, 0b111), 0x010101);
        assertEquals(RGBUtils.setChannelLSBs(0xffffff, 0x001), 0xfefeff);
        assertEquals(RGBUtils.setChannelLSBs(0xabcdef, 0b000), 0xaaccee);
        assertEquals(RGBUtils.setChannelLSBs(0xbeef12, 0b110), 0xbfef12);
    }

    @Test
    public void testGetChannelLSBs()
    {
        assertEquals(RGBUtils.getChannelLSBs(0x000000), 0b000);
        assertEquals(RGBUtils.getChannelLSBs(0x123456), 0b000);
        assertEquals(RGBUtils.getChannelLSBs(0x654321), 0b111);
        assertEquals(RGBUtils.getChannelLSBs(0xa1b0c1), 0b101);
    }

    @Test
    public void testGetChannelMask() {
        assertEquals(RGBUtils.getChannelMask(Consts.CHANNEL_R), 0xff0000);
        assertEquals(RGBUtils.getChannelMask(Consts.CHANNEL_G), 0x00ff00);
        assertEquals(RGBUtils.getChannelMask(Consts.CHANNEL_B), 0x0000ff);
    }

    @Test
    public void testGetChannelLSBMask() {
        assertEquals(RGBUtils.getChannelLSBMask(Consts.CHANNEL_R), 0x010000);
        assertEquals(RGBUtils.getChannelLSBMask(Consts.CHANNEL_G), 0x000100);
        assertEquals(RGBUtils.getChannelLSBMask(Consts.CHANNEL_B), 0x000001);
    }

    @Test
    public void testGetChannel() {
        assertEquals(RGBUtils.getChannel(0xa1b0c1, Consts.CHANNEL_R), 0xa1);
        assertEquals(RGBUtils.getChannel(0x123456, Consts.CHANNEL_G), 0x34);
        assertEquals(RGBUtils.getChannel(0x654321, Consts.CHANNEL_B), 0x21);
    }

    @Test
    public void testGetChannelLSB() {
        assertEquals(RGBUtils.getChannelLSB(0x123456, Consts.CHANNEL_R), 0);
        assertEquals(RGBUtils.getChannelLSB(0x654321, Consts.CHANNEL_G), 1);
        assertEquals(RGBUtils.getChannelLSB(0xa1b0c1, Consts.CHANNEL_B), 1);
        assertEquals(RGBUtils.getChannelLSB(0xa1b0c1, Consts.CHANNEL_G), 0);
    }

    @Test
    public void testZeroChannel() {
        assertEquals(RGBUtils.zeroChannel(0xffffff, Consts.CHANNEL_R), 0x00ffff);
        assertEquals(RGBUtils.zeroChannel(0xabcdef, Consts.CHANNEL_G), 0xab00ef);
        assertEquals(RGBUtils.zeroChannel(0xbeef12, Consts.CHANNEL_B), 0xbeef00);
    }

    @Test
    public void testZeroChannelLSB() {
        assertEquals(RGBUtils.zeroChannelLSB(0xffffff, Consts.CHANNEL_R), 0xfeffff);
        assertEquals(RGBUtils.zeroChannelLSB(0xabcdef, Consts.CHANNEL_G), 0xabccef);
        assertEquals(RGBUtils.zeroChannelLSB(0xbeef12, Consts.CHANNEL_B), 0xbeef12);
    }

    @Test
    public void testSetChannel() {
        assertEquals(RGBUtils.setChannel(0xa1b0c1, Consts.CHANNEL_R, 0x33), 0x33b0c1);
        assertEquals(RGBUtils.setChannel(0x123456, Consts.CHANNEL_G, 0x55), 0x125556);
        assertEquals(RGBUtils.setChannel(0x654321, Consts.CHANNEL_B, 0x77), 0x654377);
    }

    @Test
    public void testSetChannelLSB() {
        assertEquals(RGBUtils.setChannelLSB(0x123456, Consts.CHANNEL_R, 1), 0x133456);
        assertEquals(RGBUtils.setChannelLSB(0xa1b0c1, Consts.CHANNEL_G, 0), 0xa1b0c1);
        assertEquals(RGBUtils.setChannelLSB(0x654321, Consts.CHANNEL_B, 1), 0x654321);
        assertEquals(RGBUtils.setChannelLSB(0xbeef12, Consts.CHANNEL_R, 0), 0xbeef12);
    }

}
