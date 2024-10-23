import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.almasgali.util.ByteUtil;

public class ByteTests {

    @Test
    public void testByteEncoding() {
        Assertions.assertEquals("00", ByteUtil.byteToHex((byte) 0));
        Assertions.assertEquals("b3", ByteUtil.byteToHex((byte) 179));
        Assertions.assertNotEquals("b3", ByteUtil.byteToHex((byte) 178));
        Assertions.assertEquals("ff", ByteUtil.byteToHex((byte) 255));
    }

    @Test
    public void testByteDecoding() {
        Assertions.assertArrayEquals(new byte[]{0}, ByteUtil.decodeHexString("00"));
        Assertions.assertArrayEquals(new byte[]{(byte) 179}, ByteUtil.decodeHexString("b3"));
        Assertions.assertArrayEquals(new byte[]{(byte) 255, (byte) 178}, ByteUtil.decodeHexString("ffb2"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ByteUtil.decodeHexString("aba"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ByteUtil.decodeHexString("5g"));
    }
}
