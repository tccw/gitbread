package ui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;

public class Main {
    private static final File readFile = new File("C:/Users/Thomas Cowan/IdeaProjects/CPSC210/assignments/project_q4v2b/data/recipephotos/iconCiabatta.png");
    private static final File writeFile = new File("C:/Users/Thomas Cowan/IdeaProjects/CPSC210/assignments/project_q4v2b/data/recipephotos/iconEditOut.png");

    // TODO: determine how to get Jackson Modules working without Maven. No .jar appears to be available. Necessary for
    //       deserialization of LocalDateTime fields.
    public static void main(String[] args) throws IOException {
//        new GitBreadApp();

        // aided in part by https://www.dreamincode.net/forums/topic/27950-steganography/

        int startByte = 1104;
        int step = 4 + 4 + 3;
        String recipeData = "Test of writing a string to a PNG alpha layer.";
        byte[] recipeBytes = recipeData.getBytes();
        BufferedImage image = ImageIO.read(readFile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
//        outputStream.write(recipeData.getBytes());
        outputStream.flush();
        byte[] out = outputStream.toByteArray();
        outputStream.close();
//        System.out.println((out.length - 32) / 8);
//        System.out.println(recipeBytes.length);
        //writing the text into the image
        for (int i = startByte, j = 0; j < recipeBytes.length; i += step, j++) {
            out[i] = recipeBytes[j];
        }
//        addBorderImage(image);
        //need to set the startbyte based on where the pixel data chunks are, not the info chunks.
        ByteArrayInputStream inputStream = new ByteArrayInputStream(out);
        BufferedImage imageOut = ImageIO.read(inputStream);
        ImageIO.write(imageOut, "png", writeFile);
        byte[] translated = new byte[recipeBytes.length];
        for (int i = startByte, j = 0; j < recipeBytes.length; i += step, j++) {
            translated[j] = out[i];
        }
        System.out.println(Arrays.toString(recipeBytes));
        System.out.println(Arrays.toString(translated));
        System.out.println(new String(translated));

    }

    private static void addBorderImage(BufferedImage image) {
        int borderColor = new Color(252, 186, 3).getRGB();
        try {
            for (int j = 0; j <= 25; j++) {
                for (int i = 0; i < image.getHeight(); i++) {
                    System.out.println(j + ", " + i);
                    System.out.println(image.getWidth());
                    System.out.println(image.getHeight());
                    image.setRGB(i, (image.getHeight() - 1) - j, borderColor);
                    image.setRGB(i, j, borderColor);
                    image.setRGB(j, i, borderColor);
                    image.setRGB((image.getWidth() - 1) - j, i, borderColor);
                }
            }
            ImageIO.write(image, "png", writeFile);
        } catch (IOException e) {
            System.out.println("whoops!");
        }

    }
}
