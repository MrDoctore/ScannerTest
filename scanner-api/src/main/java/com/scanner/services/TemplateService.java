package com.scanner.services;

import com.scanner.models.Answer;
import com.scanner.models.Template;
import com.scanner.scanner.Scan;
import com.scanner.util.Disco;
import com.scanner.util.FormConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@Service
public class TemplateService {
    @Value("${scanner.disco.raizApi}")
    private String raizApi;

    private final Disco disco;

    public TemplateService(Disco disco) {
        this.disco = disco;
    }


    public void uploadTemplate(MultipartFile template) {
        disco.saveTemplate(template, raizApi + "template");
    }

    public List<String> binarizeTemplate() throws Exception {
        BufferedImage img = FormConverter.pdfToPng(this.raizApi + "template/template.pdf");
        BufferedImage resizedTemplate = FormConverter.resizeTemplate(img, 2480, 3508);
        BufferedImage grayTemplate = FormConverter.toGrayscale(resizedTemplate);
        BufferedImage binaryTemplate = FormConverter.toBinary(grayTemplate, 200);
        ImageIO.write(grayTemplate, "png", new File(this.raizApi + "template/grayTemplate.png"));
        ImageIO.write(binaryTemplate, "png", new File(this.raizApi + "template/binaryTemplate.png"));
        List<String> imgUrl = new ArrayList<>();
        imgUrl.add(toBase64(binaryTemplate));
        return imgUrl;
    }

    public String toBase64(BufferedImage binaryTemplate) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(binaryTemplate, "png", baos);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();
        return "data:image/png;base64," +
                Base64.getEncoder().encodeToString(bytes);
    }

    public List<Answer> mapearTemplate(Template template) throws IOException {
        BufferedImage binaryTemplate = ImageIO.read(new File(this.raizApi + "template/binaryTemplate.png"));
        int templateID = Integer.parseInt(Scan.readOCR(binaryTemplate, template.getCoordinate()));
        System.out.println(templateID);

        return Scan.scanAll(binaryTemplate, template.getQuestions(), templateID, this.raizApi);
    }


}

