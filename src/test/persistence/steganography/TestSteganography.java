package persistence.steganography;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestSteganography {
    private String message;
    private File fileIn;
    private File fileOut;
    private Steganos encoder;

    @BeforeEach
    void setUp() {
        message = "Test message for easy usage.";
        fileIn = new File("./data/icons/collectionsharing/collectionsharingbynikitagolubev.png");
        fileOut = new File("./data/icons/collectionsharing/sharing/testCollection.png");
        encoder = new Steganos();
    }

    @Test
    void TestConstructor() {
        assertNull(encoder.getImage());
        assertNull(encoder.getByteImageEncoded());
        assertNull(encoder.getByteImageOriginal());
        assertNull(encoder.getByteMessage());
        assertNull(encoder.getInputStream());
    }

    @Test
    void TestEncode() {
        encoder.encode(message, fileIn);
        try {
            encoder.save(fileOut);
        } catch (IOException e) {
            fail("Unexpected IOException.");
        }
        assertNotNull(encoder.getByteImageEncoded());
    }
}
