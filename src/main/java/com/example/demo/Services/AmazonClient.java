package com.example.demo.Services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Service
public class AmazonClient {

    private AmazonS3 s3client;

    @Autowired
    CSV csv;

    private String endpointUrl = "s3-control.eu-central-1.amazonaws.com";

    private String bucketName = "spectrodrop-bucket";

    @PostConstruct
    private void initializeAmazon() {
        BasicAWSCredentials creds = new BasicAWSCredentials(csv.AWSCredentials(false), csv.AWSCredentials(true));
        s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).withCredentials(new AWSStaticCredentialsProvider(creds)).build();
    }

    private File convertMultiPartToFile (MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        InputStream inputStream = file.getInputStream();
        FileUtils.copyInputStreamToFile(inputStream, convFile);
        //FileOutputStream fos = new FileOutputStream(convFile);
        //fos.write(file.getBytes());
        //fos.close();
        return convFile;
    }

    private void uploadFileTos3bucket (String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.AuthenticatedRead));
    }

    public String uploadFile (MultipartFile multipartFile, String name) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + name;
            uploadFileTos3bucket(name, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    public void downloadFile(String name) {
        String newName = name.substring(0, name.lastIndexOf('.'));
        String format = name.substring(name.lastIndexOf('.'));
        S3Object object = s3client.getObject(bucketName, name);
        File localFile = new File("D:\\Overførsler\\" + newName + format);
        int increment = 1;
        boolean exists = false;
        try {
            Files.copy(object.getObjectContent(), localFile.toPath());
        } catch (IOException ioe) {
            System.out.println("Exception1 = " + ioe);
            exists = true;
        }
        while (exists) {
            try {
                File newFile = new File("D:\\Overførsler\\" + newName + "(" + increment + ")" + format);
                Files.copy(object.getObjectContent(), newFile.toPath());
                exists = false;
            } catch (IOException ioe) {
                System.out.println("Exception2 = " + ioe);
                increment++;
            }
        }
    }

    public String deleteFileFromS3Bucket(String fileName) {
        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        return "Successfully deleted";
    }

}