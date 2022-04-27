package com.scanner.controllers;

import com.scanner.models.Template;
import com.scanner.services.TemplateService;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@CrossOrigin
@RestController
@RequestMapping(value = "template")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }


    @PostMapping(value = "/upload")
    public void uploadTemplate(@RequestParam MultipartFile template) {
        templateService.uploadTemplate(template);
    }


    @GetMapping(value = "/binarize")
    public ResponseEntity<?> binarizeTemplate() throws Exception {
        return new ResponseEntity<>(templateService.binarizeTemplate(), HttpStatus.OK);
    }

    @PostMapping(path = "/mapear")
    public ResponseEntity<?>mapearTemplate(@RequestBody Template template) throws Exception {
        return new ResponseEntity<>(templateService.mapearTemplate(template), HttpStatus.OK);
    }


}
