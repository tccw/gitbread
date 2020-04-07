package persistence.steganography;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;

import javax.imageio.ImageIO;
import javax.naming.SizeLimitExceededException;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/*
Steganography class for construction shareable recipes and recipe collections. Uses LSB (Least Significant Bit)
steganography to embed recipe or recipe collection information into an image. This method was informed by many
stack-overflow posts as well as the following discussion on dream-to-code:
                        https://www.dreamincode.net/forums/topic/27950-steganography/
 */
public class Steganos {
    private static final int OFFSET = 40; // firs 8 bits stores Recipe/Collection flag, 32 bits stores message length
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;
    private BufferedImage image;
    private byte[] byteMessage;
    private byte[] originalPixels;
    private byte[] encodedPixels;
    private boolean encodeCollection;
    private boolean decodedCollection;


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
    public void encode(String message, File image, boolean encodeCollection)
            throws IOException, SizeLimitExceededException {
        this.encodeCollection = encodeCollection;
        toByteMessage(message);
        toByteImageOriginal(image);
        writeMessageToImage();
    }

    //EFFECTS:
    //https://stackoverflow.com/questions/36407866/converting-byte-array-to-png
    //https://stackoverflow.com/questions/27389249/what-type-of-array-required-in-writableraster-method-setpixels
    //https://stackoverflow.com/questions/6524196/java-get-pixel-array-from-image
    private void toByteImageOriginal(File f) throws IOException {
        if (f != null) {
            this.image = ImageIO.read(f.toURI().toURL());
            ImageIO.write(image, "png", outputStream);
            this.originalPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        } else {
            throw new IOException();
        }
    }

    //EFFECTS: convert the message to a byte array
    private void toByteMessage(String message) {
        this.byteMessage = message.getBytes();
    }

    //EFFECTS: embed the message within the original image byte array. Indicate if the image is a RecipeDevHistory or
    //         a RecipeDevCollection using 99 for collection, and 104 for history.
    private void writeMessageToImage() throws SizeLimitExceededException {
        int offset = OFFSET;
        encodedPixels = originalPixels;
        byte[] length = intToByteArray(this.byteMessage.length);

        if (byteMessage.length + OFFSET > originalPixels.length) {
            throw new IllegalArgumentException("Image to small to embed.");
        }
        if (encodeCollection) {
            System.arraycopy(new byte[]{(byte) 99}, 0, encodedPixels, 0, 1);
        } else {
            System.arraycopy(new byte[]{(byte) 114}, 0, encodedPixels, 0, 1);
        }
        System.arraycopy(length, 0, encodedPixels, 1, (offset / 8) - 1);
        for (int msgByte : byteMessage) {
            for (int bit = 7; bit >= 0; --bit, ++offset) {
                int b = (msgByte >> bit) & 1;
                encodedPixels[offset] = (byte) ((encodedPixels[offset] & 0xFE) | b);
            }
        }
    }

    //EFFECTS: decode a byte array
    /*
    The first five bytes of the array contain a one-byte type flag and a four-byte message length flag
     */
    public String decode(File file) throws IOException {
        byte[] byteImage = imageToByteArray(file);
        return getStringFromBytes(byteImage);
    }

    //EFFECTS: Overloaded method for directly taking images rather than file locations
    //https://stackoverflow.com/questions/24038524/how-to-get-byte-from-javafx-imageview
    //https://stackoverflow.com/questions/32028783/how-do-i-use-pixelreaders-getpixels-method
    //https://www.reddit.com/r/javahelp/comments/9njz5k/easiest_way_to_convert_a_javafxsceneimageimage_to/
    //http://www.java-gaming.org/index.php/topic,31609
    //https://www.powerbot.org/community/topic/963142-looking-for-a-faster-way-to-convert-bufferedimage-to-byte-array/
    //https://stackoverflow.com/questions/3312853/how-does-bitshifting-work-in-java
    //https://stackoverflow.com/questions/29226704/image-getraster-getdatabuffer-returns-array-of-negative-values
    //https://stackoverflow.com/questions/27054672/writing-javafx-scene-image-image-to-file
    //https://stackoverflow.com/questions/27457517/how-to-change-the-image-type-of-a-bufferedimage-which-is-loaded-from-file ****
    /*
    TODO: Fix this to allow drag and drop images from browser.
    The problem here is that SwingFXUtils.fromFXImage returns a buffered image with raster data in a DataBufferInt
    but I need a DataBufferByte and they cannot be cast to each other.
     */
    public String decode(Image image) {
        // HACKY SOLUTION TODO: solve this in a less hacky way, perhaps without the use of Swing
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        BufferedImage buffImageFinal = new BufferedImage(width, height, 6);
        BufferedImage buffImageTemp = SwingFXUtils.fromFXImage(image, null);
        Graphics2D g = buffImageFinal.createGraphics();
        g.drawImage(buffImageTemp, 0, 0, width, height, null);
        g.dispose();
        return getStringFromBytes(((DataBufferByte) buffImageFinal.getRaster().getDataBuffer()).getData());
    }

    private String getStringFromBytes(byte[] byteImage) {
        int offset = OFFSET;
        byte[] classType = new byte[1];
        byte[] byteLength = new byte[4];
        System.arraycopy(byteImage, 0, classType, 0, 1);
        System.arraycopy(byteImage, 1, byteLength, 0, (offset / 8) - 1);
        int length = byteArrayToInt(byteLength);
        byte[] result = new byte[length];

        for (int b = 0; b < length; ++b) {
            for (int i = 0; i < 8; ++i, ++offset) {
                result[b] = (byte) ((result[b] << 1) | (byteImage[offset] & 1));
            }
        }

        this.decodedCollection = classType[0] == (byte) 99;
        return new String(result);
    }


    //EFFECTS: converts an int[] to a byte[]
    /*
    TODO: figure out how each DataBufferInt entry represents the underlying bits/bytes.
    I'm still not understanding something about how DataBufferInt is storing data. The first two entries give me the
    correct first value for (number >> 24) & 0xFF, but the other three bytes are all wrong.
     */
    private byte[] toDataBufferByte(DataBuffer db, int width, int height) {
        byte[] result = new byte[width * height * 4];
        int[] dbIntArray = ((DataBufferInt) db).getData();
        for (int i = 0; i < dbIntArray.length; i += 4) {
            result[i] = (byte) ((dbIntArray[i] >> 24) & 0xFF);
            result[i + 1] = (byte) ((dbIntArray[i] >> 16) & 0xFF);
            result[i + 2] = (byte) ((dbIntArray[i] >> 8) & 0xFF);
            result[i + 3] = (byte) (dbIntArray[i] & 0xFF);
//            for (int j = 3; j >= 0; j--) {
//                result[i + j] = (byte)((dbIntArray[i] & (8 * j)) >> 0xFF);
//            }
        }
        return result; //stub
    }


    //EFFECTS: convert the image linked in the given file to a byte array.
    private byte[] imageToByteArray(File file) throws IOException {
        BufferedImage image;
        URL path = file.toURI().toURL();
        image = ImageIO.read(path);
        return ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
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
    private byte[] intToByteArray(int i) throws SizeLimitExceededException {
        if (i < 0) {
            throw new IllegalArgumentException("Message length is negative!");
        } else if (i == 0) {
            throw new IllegalArgumentException("Cannot encode a collection with length zero.");
        }
        if (i > Math.pow(2, 24)) { // greater than the largest positive int we can store in 3 bytes
            throw new SizeLimitExceededException();
        }
        byte byte3 = (byte) ((i & 0xFF000000) >> 24); // discard the rightmost 24 bits and keep the first 8
        byte byte2 = (byte) ((i & 0x00FF0000) >> 16); // discard the rightmost 16 bits and keep the second 8
        byte byte1 = (byte) ((i & 0x0000FF00) >> 8); // etc.
        byte byte0 = (byte) ((i & 0x000000FF));
        return (new byte[]{byte3, byte2, byte1, byte0});
    }

    //EFFECTS: converts a 4-byte byte array into an integer
    private int byteArrayToInt(byte[] b) throws IllegalArgumentException {
//        if (b.length > OFFSET / 8) {
//            throw new IllegalArgumentException();
//        } else {
        return b[3] & 0xFF
                | (b[2] & 0xFF) << 8
                | (b[1] & 0xFF) << 16
                | (b[0] & 0xFF) << 24;
//        }
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

    public boolean isEncodeCollection() {
        return encodeCollection;
    }

    public boolean isDecodedCollection() {
        return decodedCollection;
    }
}
