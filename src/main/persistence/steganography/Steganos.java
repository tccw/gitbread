package persistence.steganography;

import persistence.Saveable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/*
Steganography class for construction shareable recipes and recipe collections. Uses LSB (Least Significant Bit)
steganography to embed recipe or recipe collection information into an image. This method was informed by many
stack-overflow posts as well as the following discussion on dream-to-code:
                        https://www.dreamincode.net/forums/topic/27950-steganography/
 */
public class Steganos {
    private static final int OFFSET = 1000; // 69 bytes is the important header for a PNG file.
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;
    private BufferedImage image;
    private byte[] byteMessage;
    private byte[] byteImageOriginal;
    private byte[] byteImageEncoded;


    //EFFECTS: constructs a steganos object with empty ImageBuffer
    public Steganos() {
        outputStream = new ByteArrayOutputStream();
        inputStream = null;
        image = null;
        byteMessage = null;
        byteImageOriginal = null;
        byteImageEncoded = null;
    }

    //MODIFIES: this
    //EFFECTS: encode the PNG byte array.
    public void encode(String message, File image) {
        toByteMessage(message);
        toByteImageOriginal(image);
        writeMessageToImage();
    }

    //EFFECTS: embed the message within the original image byte array.
    private void writeMessageToImage() {
        int offset = OFFSET;
        byteImageEncoded = byteImageOriginal;
        if (byteMessage.length + OFFSET > byteImageOriginal.length) {
            throw new IllegalArgumentException("Image to small to embed.");
        }
        for (int msgByte : byteMessage) {
            for (int bit = 7; bit >= 0; --bit, ++offset) {
                int b = (msgByte >> bit) & 1;
                byteImageEncoded[offset] = (byte) ((byteImageEncoded[offset] & 0xFE) | b);
            }
        }
    }

    //EFFECTS: decode a byte array
    public void decode(byte[] byteImage) {
        //stub
    }

    //EFFECTS: convert the message to a byte array
    private void toByteMessage(String message) {
        this.byteMessage = message.getBytes();
    }

    //EFFECTS:
    private void toByteImageOriginal(File f) {
        try {
            this.image = ImageIO.read(f.toURI().toURL());
            ImageIO.write(image, "png", outputStream);
            byteImageOriginal = outputStream.toByteArray();
            outputStream.close();
        } catch (IOException e) {
            System.out.println("IOException for " + "'" + f.toString() + "'");
        }
    }

    public void save(File file) throws IOException {
//        inputStream = new ByteArrayInputStream(byteImageEncoded);
        inputStream = new ByteArrayInputStream(byteImageOriginal);
        BufferedImage imageOut = ImageIO.read(inputStream);
        ImageIO.write(imageOut, "png", file);
    }

    // getters
    public BufferedImage getImage() {
        return image;
    }

    public byte[] getByteMessage() {
        return byteMessage;
    }

    public byte[] getByteImageOriginal() {
        return byteImageOriginal;
    }

    public byte[] getByteImageEncoded() {
        return byteImageEncoded;
    }

    public ByteArrayInputStream getInputStream() {
        return inputStream;
    }
}
