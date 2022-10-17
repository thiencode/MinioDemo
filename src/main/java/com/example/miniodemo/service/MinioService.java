package com.example.miniodemo.service;

import com.example.miniodemo.config.MinioConfigurationProperties;
import com.example.miniodemo.http.dto.FileDto;
import io.minio.*;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MinioService {

    private final MinioClient minioClient;
    private final MinioConfigurationProperties minioConfigurationProperties;

    @Autowired
    public MinioService(MinioClient minioClient, MinioConfigurationProperties minioConfigurationProperties) {
        this.minioClient = minioClient;
        this.minioConfigurationProperties = minioConfigurationProperties;
    }


    public List<FileDto> getListObject() {
        List<FileDto> objects = new ArrayList<>();

        try {
            Iterable<Result<Item>> result = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(minioConfigurationProperties.getBucket())
                    .recursive(true)
                    .build());
            for (Result<Item> item : result) {
                objects.add(FileDto.builder()
                        .filename(item.get().objectName())
                        .size(item.get().size())
                        .url(getPreSignedObjectUrl(item.get().objectName()))
                        .build());
            }
        } catch (Exception e) {
            log.error("Happened error when get list objects from minio: ", e);
        }
        return objects;
    }

    private String getPreSignedObjectUrl(String filename) {
        return "http://localhost:8080/file/".concat(filename);
    }

    public FileDto uploadFile(FileDto request) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioConfigurationProperties.getBucket())
                    .object(request.getFile().getOriginalFilename())
                    .stream(request.getFile().getInputStream(), request.getFile().getSize(), -1)
                    .build());
        } catch (Exception e) {
            log.error("Happened error when upload file: ", e);
        }
        return FileDto.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .size(request.getFile().getSize())
                .url(getPreSignedObjectUrl(request.getFile().getOriginalFilename()))
                .filename(request.getFile().getOriginalFilename())
                .build();
    }

    public InputStream getObject(String filename) {
        InputStream stream;
        try {
            stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioConfigurationProperties.getBucket())
                    .object(filename)
                    .build());
        } catch (Exception e) {
            log.error("Happened error when get list objects from minio: ", e);
            return null;
        }
        return stream;
    }

}
