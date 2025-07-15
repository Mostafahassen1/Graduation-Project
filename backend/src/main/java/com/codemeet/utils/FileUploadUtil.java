package com.codemeet.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Pattern;

@UtilityClass
public class FileUploadUtil {

    public static final long MAX_FILE_SIZE = 1024 * 1024;

    public static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|jpeg|png|svg|gif|bmp))$)";

    public static boolean isAllowedExtension(String fileName, String pattern) {
        return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)
            .matcher(fileName)
            .matches();
    }

    public static void assertAllowed(MultipartFile file, String pattern) {
        long size = file.getSize();

        if (size > MAX_FILE_SIZE) {
            throw new RuntimeException("Max file size is 1MB");
        }

        String fileName = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(fileName);

        if (!isAllowedExtension(fileName, pattern)) {
            throw new RuntimeException("Only jpg, png, gif, svg, and bmp files are allowed.");
        }
    }
}
