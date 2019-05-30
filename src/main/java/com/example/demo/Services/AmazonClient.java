package com.example.demo.Services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;

@Service
public class AmazonClient {

    //https://www.baeldung.com/aws-s3-multipart-upload

    //https://medium.com/oril/uploading-files-to-aws-s3-bucket-using-spring-boot-483fcb6f8646

    private AmazonS3 s3client;

    private TransferManager transferManager;

    @Autowired
    private CSV csv;

    private String endpointUrl = "http://s3.eu-central-1.amazonaws.com";

    private String bucketName = "spectrofly";

    /**
     * Initialiserer en forbindelse til AWS gennem en S3-client
     * og en transfer-manager som begrænser upload til pakker af 5 megabyte
     */

    @PostConstruct
    private void initializeAmazon() {
        BasicAWSCredentials credentials = new BasicAWSCredentials
                (csv.AWSCredentials(false), csv.AWSCredentials(true));
        s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).
                withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        transferManager = TransferManagerBuilder.standard().withS3Client(s3client).
                withMultipartUploadThreshold((long) (5 * 1024 * 1025)).build();
    }

    /**
     * Konverterer en fil i flere stykker tii én fil
     *
     */

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        InputStream inputStream = file.getInputStream();
        FileUtils.copyInputStreamToFile(inputStream, convertedFile);
        //FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
        //fileOutputStream.write(file.getBytes());
        //fileOutputStream.close();
        return convertedFile;
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        try {
            transferManager.upload(bucketName, fileName, file).waitForUploadResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(MultipartFile multipartFile, String name) {
        try {
            File file = convertMultiPartToFile(multipartFile);
            uploadFileTos3bucket(name, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //https://www.javainuse.com/spring/boot-file-download
    //https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/examples-s3-objects.html

    /**
     * Henter et S3-object, som er den ønskede fil, som bliver kopieret over på en ny fil
     * Denne bliver sendt gennem browseren
     *
     */

    public void downloadFile(String fileName, HttpServletResponse response) {
        S3Object s3Object = s3client.getObject(bucketName, fileName);
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        try {
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3ObjectInputStream.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            s3ObjectInputStream.close();
            fos.close();
            if (file.exists()) {

                String mimeType = URLConnection.guessContentTypeFromName(file.getName());
                if (mimeType == null) {
                    mimeType = "application/octet-stream";
                }
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format
                        ("attachment; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = null;
                try {
                    inputStream = new BufferedInputStream(new FileInputStream(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    assert inputStream != null;
                    FileCopyUtils.copy(inputStream, response.getOutputStream());
                    inputStream.close();
                    file.delete();
                } catch (IOException e) {
                    inputStream.close();
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFileFromS3Bucket(String fileName) {
        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
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

    }

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

            URL url = new URL();
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

    public boolean downloadFile(String name) {
        String newName = name.substring(0, name.lastIndexOf('.'));
        String format = name.substring(name.lastIndexOf('.'));
        S3Object object = s3client.getObject(bucketName, name);
        return copyFiles(object, newName, format);
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

    File file = new File(System.getProperty("user.home") + "/Downloads/" + fileName);

        InputStream in = s3Object.getObjectContent();
        byte[] buf = new byte[1024];
        try {
            OutputStream out = new FileOutputStream(file);
            int count;
            while( (count = in.read(buf)) != -1)
            {
                if( Thread.interrupted() )
                {
                    throw new InterruptedException();
                }
                out.write(buf, 0, count);
            }
            out.close();
            in.close();
        } catch(FileNotFoundException fnf) {
            fnf.printStackTrace();
        } catch (InterruptedException inter) {
            inter.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    */

}