package com.was.admin.common.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsS3Utils {

    @Value("${aws.s3.buket}")
    private String bucket;

    @Value("${aws.s3.host}")
    private String host;

    private final AmazonS3Client amazonS3Client;


    public String fileUploadToS3(MultipartFile file, String ext) {
        String fileName = UUID.randomUUID().toString().concat(ext);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try(InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(IOException e) {
            log.error("[이미지 업로드 실패] ========>");
            return null;
        }

        return host + fileName;
    }

}
