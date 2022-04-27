package com.scanner.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FormConverter {
    public static BufferedImage convertTemplate(String path, int scaledWidth, int scaledHeight, int threshold) throws IOException {
        BufferedImage img = pdfToPng(path);
        BufferedImage resized = resizeTemplate(img, scaledWidth, scaledHeight);
        BufferedImage gray = toGrayscale(resized);
        return toBinary(gray, threshold);
    }

    public static BufferedImage pdfToPng(String path) throws IOException {
        PDDocument doc = PDDocument.load(new File(path));
        PDFRenderer pdfRenderer = new PDFRenderer(doc);
        BufferedImage outputImage = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
        doc.close();
        return outputImage;
    }

    public static BufferedImage resizeTemplate(BufferedImage template, int scaledWidth, int scaledHeight) {
        BufferedImage resizedImage = new BufferedImage(scaledWidth,
                scaledHeight, template.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(template, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        return resizedImage;
    }

    public static BufferedImage toGrayscale(BufferedImage image) {
        BufferedImage output = new BufferedImage(image.getWidth(),
                image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = output.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return output;
    }

    public static BufferedImage toBinary(BufferedImage image, int threshold) {
        int BLACK = Color.BLACK.getRGB();
        int WHITE = Color.WHITE.getRGB();

        BufferedImage output = new BufferedImage(image.getWidth(),
                image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        for (int y = 0; y < image.getHeight(); y++)
            for (int x = 0; x < image.getWidth(); x++) {
                Color pixel = new Color(image.getRGB(x, y));
                output.setRGB(x, y, pixel.getRed() < threshold ? BLACK : WHITE);
            }
        return output;
    }
}
