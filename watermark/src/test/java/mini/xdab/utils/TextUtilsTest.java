package mini.xdab.utils;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import mini.xdab.utils.TextUtils;
import org.junit.Test;
import org.w3c.dom.Text;

import java.nio.charset.StandardCharsets;


public class TextUtilsTest {

    @Test
    public void testIsPrintable() {
        assertTrue(TextUtils.isPrintable('a'));
        assertTrue(TextUtils.isPrintable('A'));
        assertTrue(TextUtils.isPrintable('0'));
        assertTrue(TextUtils.isPrintable('*'));
        assertTrue(TextUtils.isPrintable('\n'));

        assertFalse(TextUtils.isPrintable('\0'));
        assertFalse(TextUtils.isPrintable('\r'));
        assertFalse(TextUtils.isPrintable((char)0x7f));
        assertFalse(TextUtils.isPrintable((char)0x1f));
        assertFalse(TextUtils.isPrintable((char)0x07));
    }

    @Test
    public void testBytesToString() {
        byte[] testArray1 = new byte[] { 0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x20, 0x57, 0x6f, 0x72, 0x6c, 0x64, 0x21 };
        assertEquals(TextUtils.bytesToString(testArray1), "Hello World!");

        byte[] testArray2 = "Example 2".getBytes(StandardCharsets.UTF_8);
        assertEquals(TextUtils.bytesToString(testArray2), "Example 2");
    }

    @Test
    public void testAppendToFileName() {
        assertEquals(TextUtils.appendToFileName("test.png", "string"), "teststring.png");
        assertEquals(TextUtils.appendToFileName("a.png.jpg.tif", "_output"), "a.png.jpg_output.tif");
        assertEquals(TextUtils.appendToFileName("subdir/img.PNG", "xd"), "subdir/imgxd.PNG");
    }
}
