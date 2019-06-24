package com.example.demo.Services;

import com.example.demo.Models.File;
import com.example.demo.Repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Lavet af Marco Pedersen og Thomas Vindelev

@org.springframework.stereotype.Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private AmazonClient amazonClient;

    // private String spectroDropBucket = "spectrodrop-bucket/";

    /**
     * Tilføjer fil til en opgave, men tjekker også, om en fil af samme navn eksisterer, hvorefter filen bliver omdøbt
     *
     */

    public boolean addFileToTask(int id, String name, MultipartFile file) {
        String original;
        try {
            original = name.substring(0, name.lastIndexOf('.'));
        } catch (StringIndexOutOfBoundsException oub) {
            return true;
        }
        String format = name.substring(name.lastIndexOf('.'));
        name = name.substring(0, name.lastIndexOf('.'));
        ResultSet resultSet = fileRepository.findDuplicate(name + format);
        int increment = 0;
        try {
            while (resultSet.next()) {
                increment++;
                name = original;
                name += "(" + increment + ")" + format;
                resultSet = fileRepository.findDuplicate(name);
            }
            fileRepository.closeConnections(resultSet);
            if (increment > 0) {
                amazonClient.uploadFile(file, name);
                return fileRepository.addFileToTask(id, name);
            } else {
                amazonClient.uploadFile(file, name + format);
                return fileRepository.addFileToTask(id, name + format);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fileRepository.closeConnections(resultSet);
            return true;
        }
    }

    public void downloadFile(String name, HttpServletResponse response) {
        amazonClient.downloadFile(name, response);
    }

    public void deleteFile(String name) {
        amazonClient.deleteFileFromS3Bucket(name);
        fileRepository.deleteFile(name);
    }

    public List<File> getFilesByTask(int id) {
        ResultSet resultSet = fileRepository.getFilesByTask(id);
        List<File> files = new ArrayList<>();
        try {
            while (resultSet.next()) {
                File file = new File();
                file.setName(resultSet.getString("name"));
                files.add(file);
            }
            fileRepository.closeConnections(resultSet);
            return files;
        } catch (SQLException e) {
            fileRepository.closeConnections(resultSet);
            e.printStackTrace();
        }
        return null;
    }

}