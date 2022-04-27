package com.scanner.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Component
public class Disco {
    public String path;
    @Value("${scanner.disco.raizApi}")
    private String raizApi;

    public void saveTemplate(MultipartFile template, String directoryPath) {
        this.save( directoryPath, template, "template.pdf");

    }

    public void save(String directory, MultipartFile file , String fileName) {
        Path directoryPath = Paths.get(directory);
        Path filePath = directoryPath.resolve(Objects.requireNonNull(fileName));
        try {
            Files.createDirectories(directoryPath);
            file.transferTo(filePath.toFile());
            this.path =  directory + "/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Não foi possível salvar o arquivo.", e);
        }

    }
}
