package com.scanner.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FormConverterTest {

    private String path;
    private PDDocument doc;
    private BufferedImage img;
    private Integer scaledWidth;
    private Integer scaledHeight;
    private Integer threshold;


    @BeforeEach
    public void setUp() throws IOException {
        path = "/home/doctore/IdeaProjects/scanner-ui/files/template/template.pdf";
        doc = PDDocument.load(new File("/home/doctore/IdeaProjects/scanner-ui/files/template/template.pdf"));
        img = ImageIO.read(new File("/home/doctore/IdeaProjects/scanner-ui/files/template/grayTemplate.png"));
        scaledWidth = 2480;
        scaledHeight = 3508;
        threshold = 200;
    }

    @Test
    void convertPDF() {
        try {
            BufferedImage binaryImg = FormConverter.convertTemplate(path, scaledWidth, scaledHeight, threshold);
            assertNotNull(binaryImg);
            assertEquals(12, binaryImg.getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void pdfToPng() {
        try {
            assertNotNull(doc);
            PDFRenderer pdfRenderer = new PDFRenderer(doc);
            BufferedImage outputImage = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
            assertNotNull(outputImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void resizeTemplate() {
        assertNotNull(img);
        BufferedImage resizedImg = FormConverter.resizeTemplate(img, scaledWidth, scaledHeight);
        assertEquals(scaledWidth, resizedImg.getWidth());
        assertEquals(scaledHeight, resizedImg.getHeight());
    }

    @Test
    void toGrayscale() {
        assertNotNull(img);
        BufferedImage grayImg = FormConverter.toGrayscale(img);
        // 10 = TYPE_BYTE_GRAY
        assertEquals(10, grayImg.getType());
    }

    @Test
    void toBinary() {
        assertNotNull(img);
        BufferedImage binaryImg = FormConverter.toBinary(img, threshold);
        //TYPE_BYTE_BINARY
        assertEquals(12, binaryImg.getType());
    }
}