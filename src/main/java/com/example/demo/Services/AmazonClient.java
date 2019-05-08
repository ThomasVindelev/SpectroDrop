package com.example.demo.Services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class AmazonClient {
    private AmazonS3 s3client;

    //private String endpointUrl = "spectrodb.cbiha888el7r.eu-central-1.rds.amazonaws.com";
    private String endpointUrl = "https://s3.eu-central-1.rds.amazonaws.com";

    //private String accessKey = "AKIAI5RVXDZUDYVJDHJA";
    private String accessKey = "AKIA3SMPZQJFSRNMYQ7A";

    //private String secretKey = "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";
    private String secretKey = "QRR7qRgUmiX2OBx0F5AUbLHfsYon7CWf8EOGdncs";

    private String bucketName = "spectrofly";

    @PostConstruct
        private void initializeAmazon() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }

    private File convertMultiPartToFile (MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() +"-"+
        multiPart.getOriginalFilename().replace("","_" );
    }

    private void uploadFileTos3bucket (String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName,file)
                .withCannedAcl(CannedAccessControlList.AuthenticatedRead));
    }

    public String uploadFile (MultipartFile multipartFile) {
        String fileUrl ="";
        try{
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    public String deleteFileFromS3Bucket (String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") +1);
        s3client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
            return "Successfully deleted";
    }

    public void downloadFile(String keyName) {

        try {

            System.out.println("Downloading an object");
            S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, keyName));
            System.out.println("Content-Type: "  + s3object.getObjectMetadata().getContentType());
            //Utility.displayText(s3object.getObjectContent());


        } catch (AmazonServiceException ase) {

            System.out.println(ase.getMessage());

//            logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
//            logger.info("Error Message:    " + ase.getMessage());
//            logger.info("HTTP Status Code: " + ase.getStatusCode());
//            logger.info("AWS Error Code:   " + ase.getErrorCode());
//            logger.info("Error Type:       " + ase.getErrorType());
//            logger.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {

            System.out.println(ace.getMessage());
//            logger.info("Caught an AmazonClientException: ");
//            logger.info("Error Message: " + ace.getMessage());
        }
    }

}