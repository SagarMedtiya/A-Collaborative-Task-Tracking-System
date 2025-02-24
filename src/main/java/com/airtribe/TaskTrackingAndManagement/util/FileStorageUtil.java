package com.airtribe.TaskTrackingAndManagement.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileStorageUtil {
    // Directory where files will be stored
    private static final String UPLOAD_DIR ="uploads";

    /**
     *Saves an upload file tothe filesystem and returns the file path.
     *
     * @param file the file to be saved
     * @return  The path to the saved file
     * @throws IOException If an I/O error occurs.
     */
    public String saveFile(MultipartFile file) throws IOException {
        //create the upload directory if it doesnt exist
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        //generate a unique file name
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        //save the file to the upload directory
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath);

        return filePath.toString();
    }
}
