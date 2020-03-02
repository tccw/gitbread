package steganography;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

/*
Steganography class for construction shareable recipes and recipe collections. Uses LSB (Least Significant Bit)
steganography to embed recipe or recipe collection information into an image. This method was informed by many
stackoverflow posts as well as the following discussion on dream-to-code:
                        https://www.dreamincode.net/forums/topic/27950-steganography/
 */
public class Steganos {
    private static final OFFSET = 70; // 69 bytes is the important header for a PNG file.
    private ByteArrayOutputStream inputStream;
    private BufferedImage image;
    byte[] byteMessage;
    byte[] byteImageOriginal;
    byte[] byteImageEncoded;

    //EFFECTS: constructs a steganos with empty ImageBuffer
    public Steganos() {
        inputStream = new ByteArrayOutputStream();
        image = null;
        byteMessage = null;
        byteImageOriginal = null;
        byteImageEncoded = null;
    }

    //MODIFIES: this
    //EFFECTS: encodes
    public void encode(String message, File image, File output) {
        //stub
    }

    //EFFECTS: convert the message to a byte array
    private byte[] toByteMessage(String message) {
        return message.getBytes();
    }

    //EFFECTS:

}
