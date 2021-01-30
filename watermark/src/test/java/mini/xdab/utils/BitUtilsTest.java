package mini.xdab.utils;

import static org.junit.Assert.*;

import mini.xdab.utils.BitUtils;
import org.junit.Test;


public class BitUtilsTest {

    @Test
    public void testGetLSB()
    {
        assertEquals(BitUtils.getLSB(0b01), 1);
        assertEquals(BitUtils.getLSB(0b11), 1);
        assertEquals(BitUtils.getLSB(0b00), 0);
        assertEquals(BitUtils.getLSB(0b10), 0);
    }


    @Test
    public void testSetLSB()
    {
        assertEquals(BitUtils.setLSB(0b01, 0), 0b00);
        assertEquals(BitUtils.setLSB(0b01, 1), 0b01);
        assertEquals(BitUtils.setLSB(0b10, 1), 0b11);
        assertEquals(BitUtils.setLSB(0b11, 0), 0b10);
    }

    @Test
    public void testParityBit() {
        assertEquals(BitUtils.parityBit(0b11), 0);
        assertEquals(BitUtils.parityBit(0b111), 1);
        assertEquals(BitUtils.parityBit(0b00), 0);
        assertEquals(BitUtils.parityBit(0b100000), 1);
    }

    @Test
    public void testGoodParity() {
        assertTrue(BitUtils.goodParity(0b11));
        assertTrue(BitUtils.goodParity(0b0001100110));
        assertTrue(BitUtils.goodParity(0b1010100001));
        assertTrue(BitUtils.goodParity(0b00));
        assertFalse(BitUtils.goodParity(0b1));
        assertFalse(BitUtils.goodParity(0b00010110));
        assertFalse(BitUtils.goodParity(0b11111110));
        assertFalse(BitUtils.goodParity(0b01100000110000010));
    }
}
