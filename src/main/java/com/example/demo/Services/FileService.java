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

    public void addFileToTask(int id, String name) {
        int indexOfBucket = name.indexOf(spectroDropBucket);
        int indexOfFileName = indexOfBucket + spectroDropBucket.length();
        String filteredName = name.substring(indexOfFileName);
        fileRepository.addFileToTask(id, filteredName);
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
