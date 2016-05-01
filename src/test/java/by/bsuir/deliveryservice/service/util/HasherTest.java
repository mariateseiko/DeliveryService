package by.bsuir.deliveryservice.service.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class HasherTest {
    @Test
    public void emptyStringTest() {
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", Hasher.md5Hash(""));
    }

    @Test
    public void randomStringTest() {
        assertEquals("0cc175b9c0f1b6a831c399e269772661", Hasher.md5Hash("a"));
        assertEquals("d174ab98d277d9f5a5611c2c9f419d9f",
                Hasher.md5Hash("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"));
        assertEquals("fd85e62d9beb45428771ec688418b271", Hasher.md5Hash("12345678901234567890"));
    }

    @Test
    public void nullTest() {
        assertNull(Hasher.md5Hash(null));
    }
}
