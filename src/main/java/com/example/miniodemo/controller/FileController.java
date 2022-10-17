package com.example.miniodemo.controller;

import com.example.miniodemo.http.dto.FileDto;
import com.example.miniodemo.service.MinioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.web.servlet.HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE;

@Slf4j
@RestController
@RequestMapping(value = "/file")
@Tag(name = "File Controller", description = "Quản lý thao tác với minio")
public class FileController {

    private final MinioService minioService;

    @Autowired
    public FileController(@Lazy MinioService minioService) {
        this.minioService = minioService;
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách file")
    public ResponseEntity<Object> getFiles() {
        return ResponseEntity.ok(minioService.getListObject());
    }

    @PostMapping(value = "/upload")
    @Operation(summary = "Đẩy file lên minio")
    public ResponseEntity<Object> upload(@ModelAttribute FileDto request) {
        log.info(String.valueOf(ResponseEntity.ok().body(minioService.uploadFile(request))));
        return ResponseEntity.ok().body(minioService.uploadFile(request));
    }

    @GetMapping(value = "/**")
    @Operation(summary = "Tải file về")
    public ResponseEntity<Object> getFile(HttpServletRequest request) throws IOException {
        String pattern = (String) request.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE);
        String filename = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(IOUtils.toByteArray(minioService.getObject(filename)));
    }
}
