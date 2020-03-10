package persistence.steganography;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.*;
import java.net.URL;

/*
Steganography class for construction shareable recipes and recipe collections. Uses LSB (Least Significant Bit)
steganography to embed recipe or recipe collection information into an image. This method was informed by many
stack-overflow posts as well as the following discussion on dream-to-code:
                        https://www.dreamincode.net/forums/topic/27950-steganography/
 */
public class Steganos {
    private static final int OFFSET = 32; // using 32 bits to store the length of the embedded message
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
        byte[] length = intToByteArray(this.byteMessage.length);

        if (byteMessage.length + OFFSET > originalPixels.length) {
            throw new IllegalArgumentException("Image to small to embed.");
        }
        System.arraycopy(length, 0, encodedPixels, 0, offset / 8);
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
    public String decode(File file) {
        byte[] byteImage = imageToByteArray(file);
        int offset = OFFSET;
        byte[] byteLength = new byte[4];
        System.arraycopy(byteImage, 0, byteLength, 0, offset / 8);
        int length = byteArrayToInt(byteLength);
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

    //EFFECTS: convert the image linked in the given file to a byte array.
    private byte[] imageToByteArray(File file) {
        BufferedImage image;
        try {
            URL path = file.toURI().toURL();
            image = ImageIO.read(path);
            return ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        } catch (IOException e) {
            System.err.println("Problem reading file.");
        }
        return null;
    }

    //https://stackoverflow.com/questions/8996105/best-method-for-saving-a-java-image-object-with-a-custom-palette-to-a-gif-file
    // EFFECTS: save the encoded encoded image to a PNG.
    public void save(File outPath) throws IOException {
        DataBufferByte buffer = new DataBufferByte(encodedPixels, encodedPixels.length);
        WritableRaster raster = Raster.createWritableRaster(image.getSampleModel(), buffer, null);
        BufferedImage finalImage = new BufferedImage(image.getColorModel(),
                raster,
                image.getColorModel().isAlphaPremultiplied(),
                null);
        ImageIO.write(finalImage, "png", outPath);
    }

    //https://stackoverflow.com/questions/5399798/byte-array-and-int-conversion-in-java/11419863
    //EFFECTS: converts an integer into a 4-byte byte array.
    private byte[] intToByteArray(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Message length is negative!");
        } else if (i == 0) {
            throw new IllegalArgumentException("Cannot encode a collection with length zero.");
        }
        byte byte3 = (byte) ((i & 0xFF000000) >> 24);
        byte byte2 = (byte) ((i & 0x00FF0000) >> 16);
        byte byte1 = (byte) ((i & 0x0000FF00) >> 8);
        byte byte0 = (byte) ((i & 0x000000FF));
        return (new byte[]{byte3, byte2, byte1, byte0});
    }

    //EFFECTS: converts a 4-byte byte array into an integer
    private int byteArrayToInt(byte[] b) throws IllegalArgumentException {
        if (b.length > OFFSET / 8) {
            throw new IllegalArgumentException();
        } else {
            return b[3] & 0xFF
                    | (b[2] & 0xFF) << 8
                    | (b[1] & 0xFF) << 16
                    | (b[0] & 0xFF) << 24;
        }
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
