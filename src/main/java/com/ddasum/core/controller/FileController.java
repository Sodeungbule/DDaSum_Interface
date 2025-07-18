package com.ddasum.core.controller;

import com.ddasum.core.api.response.ApiResponse;
import com.ddasum.core.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController extends BaseController {
    
    private final FileService fileService;
    
    /**
     * 단일 파일 업로드
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String filename = fileService.uploadFile(file);
            return success(filename, "파일 업로드가 완료되었습니다");
        } catch (IOException e) {
            log.error("파일 업로드 실패", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("파일 업로드에 실패했습니다", "FILE_UPLOAD_ERROR"));
        }
    }
    
    /**
     * 다중 파일 업로드
     */
    @PostMapping("/upload/multiple")
    public ResponseEntity<ApiResponse<List<String>>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        try {
            List<String> filenames = fileService.uploadFiles(files);
            return success(filenames, files.size() + "개의 파일 업로드가 완료되었습니다");
        } catch (IOException e) {
            log.error("다중 파일 업로드 실패", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("파일 업로드에 실패했습니다", "FILE_UPLOAD_ERROR"));
        }
    }
    
    /**
     * 파일 삭제
     */
    @DeleteMapping("/{filename}")
    public ResponseEntity<ApiResponse<Void>> deleteFile(@PathVariable String filename) {
        boolean deleted = fileService.deleteFile(filename);
        if (deleted) {
            return success(null, "파일이 삭제되었습니다");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 파일 존재 여부 확인
     */
    @GetMapping("/{filename}/exists")
    public ResponseEntity<ApiResponse<Boolean>> fileExists(@PathVariable String filename) {
        boolean exists = fileService.fileExists(filename);
        return success(exists);
    }
    
    /**
     * 파일 정보 조회
     */
    @GetMapping("/{filename}/info")
    public ResponseEntity<ApiResponse<Object>> getFileInfo(@PathVariable String filename) {
        boolean exists = fileService.fileExists(filename);
        if (!exists) {
            return ResponseEntity.notFound().build();
        }
        
        // TODO: 실제 파일 정보 조회 로직 구현
        var fileInfo = Map.of(
            "filename", filename,
            "exists", true,
            "size", "unknown", // 실제 파일 크기 조회 필요
            "uploadTime", "unknown" // 실제 업로드 시간 조회 필요
        );
        
        return success(fileInfo);
    }
} 