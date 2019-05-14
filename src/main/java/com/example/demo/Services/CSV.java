package com.example.demo.Services;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSV {

    public String AWSCredentials(boolean isSecret) {
        String credentials = readCredsFromCSV("D:\\credentials.csv");
        return credentials;
    }

    private String readCredsFromCSV(String filePath) {
        List<String> credentials = new ArrayList<>();
        Path path = Paths.get(filePath);

        try (BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.US_ASCII)){
            String line = bufferedReader.readLine();

            while (line != null) {
                String[] attributes = line.split(",");
                String csvLine = new String();
                csvLine = attributes;
            }
        }
    }

}
