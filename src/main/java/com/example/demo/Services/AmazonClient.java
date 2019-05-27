package com.example.demo.Services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.net.URL;

@Service
public class AmazonClient {

    //https://www.baeldung.com/aws-s3-multipart-upload

    private AmazonS3 s3client;

    private TransferManager transferManager;

    @Autowired
    private CSV csv;

    private String endpointUrl = "s3-control.eu-central-1.amazonaws.com";

    private String bucketName = "spectrofly";

    @PostConstruct
    private void initializeAmazon() {
        BasicAWSCredentials credentials = new BasicAWSCredentials
                (csv.AWSCredentials(false), csv.AWSCredentials(true));
        s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).
                withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        transferManager = TransferManagerBuilder.standard().withS3Client(s3client).
                withMultipartUploadThreshold((long) (5 * 1024 * 1025)).build();
    }

    private File convertMultiPartToFile (MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        InputStream inputStream = file.getInputStream();
        FileUtils.copyInputStreamToFile(inputStream, convertedFile);
        //FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
        //fileOutputStream.write(file.getBytes());
        //fileOutputStream.close();
        return convertedFile;
    }

    private void uploadFileTos3bucket (String fileName, File file) {
        try {
            transferManager.upload(bucketName, fileName, file).waitForUploadResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    public boolean downloadFile(String name) {
        String newName = name.substring(0, name.lastIndexOf('.'));
        String format = name.substring(name.lastIndexOf('.'));
        S3Object object = s3client.getObject(bucketName, name);
        return copyFiles(object, newName, format);
    }

    //https://www.baeldung.com/java-download-file?fbclid=IwAR3-vkX25H_8NTozIujY8UhNarbB80kQsYkKpdnOIZIwTThABGleO0Hqy8Y

    /*public void test(String fileURL, String fileName) {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL(fileURL).openStream())) {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }*/

    public void test2(String fileURL, String fileName) {
        try {
            FileUtils.copyURLToFile(new URL(fileURL), new File(fileName));

        } catch (MalformedURLException url) {
            url.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    // https://www.techcoil.com/blog/how-to-download-a-file-via-http-get-and-http-post-in-java-without-using-any-external-libraries/?fbclid=IwAR0oWDTLQQtS7W4b7GuYTj5GvCfoTUhGqDHnUjSMQucyLvIgesYjyzKz6Uk

    public void test3(String fileName) {
        ReadableByteChannel readableByteChannel = null;
        FileChannel fileChannel = null;

        try {

            URL url = new URL(endpointUrl + "/" + bucketName + "/" + fileName);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            readableByteChannel = Channels.newChannel(urlConnection.getInputStream());

            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            fileChannel = fileOutputStream.getChannel();

            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {

            if (readableByteChannel != null) {
                try {
                    readableByteChannel.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

            if (fileChannel != null) {
                try {
                    fileChannel.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

        }
    }


    private boolean copyFiles(S3Object object, String name, String format) {
        String home = System.getProperty("user.home");
        File localFile = new File(FilenameUtils.normalize(
                home + "/Downloads/" + name + format));

        int increment = 1;
        boolean exists = false;
        try {
            Files.copy(object.getObjectContent(), localFile.toPath());
            return true;
        } catch (IOException ioe) {
            exists = true;
        }
        while (exists) {
            try {
                File newFile = new File(home + "/Downloads/"
                        + name + "(" + increment + ")" + format);
                Files.copy(object.getObjectContent(), newFile.toPath());
                return true;
            } catch (IOException ioe) {
                increment++;
            }
        }
        return false;
    }

    public void deleteFileFromS3Bucket(String fileName) {
        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }

}