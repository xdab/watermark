package mini.xdab;

import static org.junit.Assert.*;

import mini.xdab.utils.RGBUtils;
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

}
