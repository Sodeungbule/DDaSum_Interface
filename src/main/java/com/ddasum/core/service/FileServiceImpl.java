package com.ddasum.core.service;

import com.ddasum.core.constants.CommonConstants;
import com.ddasum.core.exception.BusinessException;
import com.ddasum.core.error.enums.ErrorCode;
import com.ddasum.core.logging.LogUtil;
import com.ddasum.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    
    private static final String UPLOAD_DIR = "uploads/";
    
    public FileServiceImpl() {
        // 업로드 디렉토리 생성
        createUploadDirectory();
    }
    
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        // 파일 검증
        validateFile(file);
        
        // 파일명 생성 (UUID + 원본 확장자)
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String filename = UUID.randomUUID().toString() + extension;
        
        // 파일 저장
        Path uploadPath = Paths.get(UPLOAD_DIR + filename);
        Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
        
        // 로깅
        LogUtil.logFileUpload(originalFilename, file.getSize(), uploadPath.toString());
        
        return filename;
    }
    
    @Override
    public List<String> uploadFiles(List<MultipartFile> files) throws IOException {
        List<String> uploadedFiles = new ArrayList<>();
        
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String filename = uploadFile(file);
                uploadedFiles.add(filename);
            }
        }
        
        return uploadedFiles;
    }
    
    @Override
    public boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(UPLOAD_DIR + filePath);
            boolean deleted = Files.deleteIfExists(path);
            
            if (deleted) {
                log.info("파일 삭제 완료: {}", filePath);
            }
            
            return deleted;
        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", filePath, e);
            return false;
        }
    }
    
    @Override
    public boolean fileExists(String filePath) {
        Path path = Paths.get(UPLOAD_DIR + filePath);
        return Files.exists(path);
    }
    
    @Override
    public boolean validateFileSize(MultipartFile file) {
        return file.getSize() <= CommonConstants.MAX_FILE_SIZE;
    }
    
    @Override
    public boolean validateFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return false;
        }
        
        String extension = getFileExtension(originalFilename).toLowerCase();
        
        for (String allowedExtension : CommonConstants.ALLOWED_FILE_EXTENSIONS) {
            if (allowedExtension.toLowerCase().equals(extension)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 파일 검증
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "파일이 비어있습니다");
        }
        
        if (!validateFileSize(file)) {
            throw new BusinessException(ErrorCode.FILE_TOO_LARGE, 
                "파일 크기가 너무 큽니다. 최대 " + (CommonConstants.MAX_FILE_SIZE / (1024 * 1024)) + "MB까지 허용됩니다");
        }
        
        if (!validateFileExtension(file)) {
            throw new BusinessException(ErrorCode.INVALID_FILE_TYPE, 
                "지원하지 않는 파일 형식입니다. 허용된 형식: " + String.join(", ", CommonConstants.ALLOWED_FILE_EXTENSIONS));
        }
    }
    
    /**
     * 파일 확장자 추출
     */
    private String getFileExtension(String filename) {
        if (StringUtil.isEmpty(filename)) {
            return "";
        }
        
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        
        return filename.substring(lastDotIndex);
    }
    
    /**
     * 업로드 디렉토리 생성
     */
    private void createUploadDirectory() {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("업로드 디렉토리 생성: {}", uploadPath.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("업로드 디렉토리 생성 실패", e);
        }
    }
} 