package persistence.steganography;

import javax.imageio.ImageIO;
import javax.sound.midi.Soundbank;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/*
Steganography class for construction shareable recipes and recipe collections. Uses LSB (Least Significant Bit)
steganography to embed recipe or recipe collection information into an image. This method was informed by many
stack-overflow posts as well as the following discussion on dream-to-code:
                        https://www.dreamincode.net/forums/topic/27950-steganography/
 */
public class Steganos {
    private static final int OFFSET = 0;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;
    private BufferedImage image;
    private byte[] byteMessage;
    private byte[] originalPixels;
    private byte[] encodedPixels;


    //EFFECTS: constructs a steganos object with empty ImageBuffer
    public Steganos() {
        outputStream = new ByteArrayOutputStream();
        inputStream = null;
        image = null;
        byteMessage = null;
        originalPixels = null;
        encodedPixels = null;
    }

    //MODIFIES: this
    //EFFECTS: encode the PNG byte array.
    public void encode(String message, File image) {
        toByteMessage(message);
        toByteImageOriginal(image);
        writeMessageToImage();
    }

    //EFFECTS:
    //https://stackoverflow.com/questions/36407866/converting-byte-array-to-png
    //https://stackoverflow.com/questions/27389249/what-type-of-array-required-in-writableraster-method-setpixels
    //https://stackoverflow.com/questions/6524196/java-get-pixel-array-from-image
    private void toByteImageOriginal(File f) {
        try {
            this.image = ImageIO.read(f.toURI().toURL());
            ImageIO.write(image, "png", outputStream);
//            originalPixels = outputStream.toByteArray();
            originalPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        } catch (IOException e) {
            System.out.println("IOException for " + "'" + f.toString() + "'");
        }
    }

    //EFFECTS: convert the message to a byte array
    private void toByteMessage(String message) {
        this.byteMessage = message.getBytes();
    }

    //EFFECTS: embed the message within the original image byte array.
    private void writeMessageToImage() {
        int offset = OFFSET;
        encodedPixels = originalPixels;
        if (byteMessage.length + OFFSET > originalPixels.length) {
            throw new IllegalArgumentException("Image to small to embed.");
        }
        for (int msgByte : byteMessage) {
            for (int bit = 7; bit >= 0; --bit, ++offset) {
                int b = (msgByte >> bit) & 1;
                encodedPixels[offset] = (byte) ((encodedPixels[offset] & 0xFE) | b);
            }
        }
    }

    //EFFECTS: decode a byte array
    /*
    put the byte array length in the first 4 bytes of the message so decode() can know how long the message is.
     */
    public String decode(byte[] byteImage, int length) {
        int offset = OFFSET;
        byte[] result = new byte[length];
        try {
            for (int b = 0; b < length; ++b) {
                for (int i = 0; i < 8; ++i, ++offset) {
                    result[b] = (byte) ((result[b] << 1) | (byteImage[offset] & 1));
                }
            }
        } catch (RuntimeException e) {
            System.out.println("Unexpected RuntimeException");
        }
        return new String(result);
    }

    //https://stackoverflow.com/questions/8996105/best-method-for-saving-a-java-image-object-with-a-custom-palette-to-a-gif-file
    public void save(File file) throws IOException {
        DataBufferByte buffer = new DataBufferByte(encodedPixels, encodedPixels.length);
        WritableRaster raster = Raster.createWritableRaster(image.getSampleModel(), buffer, null);
        BufferedImage finalImage = new BufferedImage(image.getColorModel(),
                raster,
                image.getColorModel().isAlphaPremultiplied(),
                null);
        ImageIO.write(finalImage, "png", file);
    }

    // getters
    public BufferedImage getImage() {
        return image;
    }

    public byte[] getByteMessage() {
        return byteMessage;
    }

    public byte[] getOriginalPixels() {
        return originalPixels;
    }

    public byte[] getEncodedPixels() {
        return encodedPixels;
    }

    public ByteArrayInputStream getInputStream() {
        return inputStream;
    }
}
