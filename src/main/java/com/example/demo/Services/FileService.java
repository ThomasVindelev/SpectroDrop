package com.example.demo.Services;

import com.example.demo.Models.File;
import com.example.demo.Repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class FileService {

    @Autowired
    FileRepository fileRepository;

    @Autowired
    AmazonClient amazonClient;

    // private String spectroDropBucket = "spectrodrop-bucket/";

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
            if (increment > 0) {
                amazonClient.uploadFile(file, name);
                return fileRepository.addFileToTask(id, name);
            } else {
                amazonClient.uploadFile(file, name + format);
                return fileRepository.addFileToTask(id, name + format);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public void deleteFile(String name) {
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
            return files;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
