package com.ddasum.core.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    
    /**
     * 단일 파일 업로드
     */
    String uploadFile(MultipartFile file) throws IOException;
    
    /**
     * 다중 파일 업로드
     */
    List<String> uploadFiles(List<MultipartFile> files) throws IOException;
    
    /**
     * 파일 삭제
     */
    boolean deleteFile(String filePath);
    
    /**
     * 파일 존재 여부 확인
     */
    boolean fileExists(String filePath);
    
    /**
     * 파일 크기 검증
     */
    boolean validateFileSize(MultipartFile file);
    
    /**
     * 파일 확장자 검증
     */
    boolean validateFileExtension(MultipartFile file);
} 