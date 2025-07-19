package com.Steganography;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class SteganographyDecoder {

    public static String decode(File imageFile, String password) throws Exception {
        BufferedImage image = ImageIO.read(imageFile);

        int width = image.getWidth();
        int pixelIndex = 0;

        // Decode message length
        int msgLength = 0;
        for (int i = 0; i < 32; i++) {
            int x = pixelIndex % width;
            int y = pixelIndex / width;

            int bit = image.getRGB(x, y) & 1;
            msgLength = (msgLength << 1) | bit;
            pixelIndex++;
        }

        byte[] msgBytes = new byte[msgLength];
        for (int i = 0; i < msgLength; i++) {
            byte b = 0;
            for (int j = 0; j < 8; j++) {
                int x = pixelIndex % width;
                int y = pixelIndex / width;

                int bit = image.getRGB(x, y) & 1;
                b = (byte) ((b << 1) | bit);
                pixelIndex++;
            }
            msgBytes[i] = b;
        }

        String fullMessage = new String(msgBytes);
        if (!fullMessage.contains(":")) {
            return "Decryption failed or password missing.";
        }

        String[] parts = fullMessage.split(":", 2);
        if (!parts[0].equals(password)) {
            return "Wrong password!";
        }

        return parts[1]; // decoded message
    }
}
