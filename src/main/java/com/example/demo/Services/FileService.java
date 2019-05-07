package com.example.demo.Services;

import com.example.demo.Models.File;

public class FileService implements com.example.demo.Services.Service<File> {

    //Til at bekræfte en bestemt filtype

    @Override
    public boolean verify(File object) {
        return false;
    }
}
