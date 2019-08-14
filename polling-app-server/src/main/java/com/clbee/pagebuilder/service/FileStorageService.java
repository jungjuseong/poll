package com.clbee.pagebuilder.service;

import com.clbee.pagebuilder.exception.FileStorageException;
import com.clbee.pagebuilder.exception.MyFileNotFoundException;
import com.clbee.pagebuilder.FileStorageProperties;
import com.clbee.pagebuilder.security.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import  com.clbee.pagebuilder.util.UUIDGenerator;

import static com.clbee.pagebuilder.util.UUIDGenerator.NAMESPACE_URL;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final Path storageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.storageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.storageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("업로드 파일을 저장할 폴터를 만들 수 없습니다.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("파일 이름에 잘못된 경로 시퀀스가 들어 있습니다. " + fileName);
            }

            final String uniquePrefix = UUIDGenerator.generateType5UUID(NAMESPACE_URL, fileName).toString();
            logger.info("uniquePrefix: " + uniquePrefix);

            final String uniqueFileName = uniquePrefix + "-" + fileName;
            // Copy file to the target location
            Path targetLocation = this.storageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.storageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}