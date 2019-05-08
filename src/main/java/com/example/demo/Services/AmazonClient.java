package com.example.demo.Services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class AmazonClient {
    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

@PostConstruct
    private void initializeAmazon() {
    AWSCredentials credentials =new
            BasicAWSCredentials(this.accessKey,this.secretKey);
    this.s3client = new  AmazonS3ClientBuilder.withCredentials(credentials);
}
}
private File convertMultiPartToFile (MultipartFile file) throws IOException {
    File convFile
}