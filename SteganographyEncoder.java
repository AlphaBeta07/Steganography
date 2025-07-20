//package com.Steganography;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class SteganographyEncoder {

    public static void encode(File sourceFile, File ignoredOutputFile, String message, String password) throws Exception {
        BufferedImage image = ImageIO.read(sourceFile);

        String encryptedMsg = password + ":" + message;
        byte[] msgBytes = encryptedMsg.getBytes();
        int msgLength = msgBytes.length;

        if ((msgLength * 8) + 32 > image.getWidth() * image.getHeight()) {
            throw new IllegalArgumentException("Message is too long to hide in this image.");
        }

        // Encode message length first (32 bits)
        int width = image.getWidth();
        int height = image.getHeight();
        int pixelIndex = 0;

        for (int i = 0; i < 32; i++) {
            int x = pixelIndex % width;
            int y = pixelIndex / width;
            int bit = (msgLength >> (31 - i)) & 1;

            int rgb = image.getRGB(x, y);
            int newRGB = (rgb & 0xFFFFFFFE) | bit; // replace LSB
            image.setRGB(x, y, newRGB);
            pixelIndex++;
        }

        // Encode message bytes
        for (byte b : msgBytes) {
            for (int i = 7; i >= 0; i--) {
                int x = pixelIndex % width;
                int y = pixelIndex / width;
                int bit = (b >> i) & 1;

                int rgb = image.getRGB(x, y);
                int newRGB = (rgb & 0xFFFFFFFE) | bit;
                image.setRGB(x, y, newRGB);
                pixelIndex++;
            }
        }

        // Create output directory if not exists
        File outputDir = new File("D:\\java project\\encoded");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        // Get original file name
        String fileName = sourceFile.getName();
        File outputFile = new File(outputDir, fileName);

        // Write the encoded image
        ImageIO.write(image, "png", outputFile);
    }
}
