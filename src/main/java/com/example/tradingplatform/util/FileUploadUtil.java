package com.example.tradingplatform.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUploadUtil {

    public static String saveFile(String uploadDir, MultipartFile file) throws IOException {
        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdir();
        }

        String fileName = file.getOriginalFilename();
        String filePath = uploadDir + "/" + fileName;
        File dest = new File(filePath);
        file.transferTo(dest);
        return fileName;
    }

    private static String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    }
}
