package mini.xdab.digital.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ShiftRegisterTest {

    @Test
    public void test() {
        var sr = new ShiftRegister();
        sr.shift(0b10101, 5);
        sr.shift(0b1, 2);
        sr.shift(0b1111, 2);

        var beforeGets = sr.getRegister();
        assertEquals(sr.getRegister().longValue(), 0b101010111L);

        assertEquals(sr.get(5).longValue(), 0b10111L);
        assertEquals(sr.get(1).longValue(), 0b1L);

        assertEquals(beforeGets, sr.getRegister());
    }

    @Test
    public void test2() {
        var sr = new ShiftRegister();
        //sr.shift(0x2dd,
    }

}