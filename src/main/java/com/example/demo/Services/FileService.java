package com.example.demo.Services;

import com.example.demo.Models.File;
import com.example.demo.Repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class FileService implements com.example.demo.Services.Service<File> {

    @Autowired
    FileRepository fileRepository;

    private String spectroDropBucket = "spectrodrop-bucket/";

    public String addFileToTask(int id, String name) {
        String original = name.substring(0, name.lastIndexOf('.'));
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
                return fileRepository.addFileToTask(id, name);
            } else {
                return fileRepository.addFileToTask(id, name + format);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    @Override
    public boolean verify(File object) {
        return false;
    }
}
