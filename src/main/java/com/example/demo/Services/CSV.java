package com.example.demo.Services;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CSV {

    public String AWSCredentials(boolean isSecret) {
        String credentials = readCredsFromCSV("D:\\credentials.csv", isSecret);
        return credentials;
    }

    private String readCredsFromCSV(String filePath, boolean isSecret) {
        Path path = Paths.get(filePath);
        String key = "";
        try (BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.US_ASCII)) {
            String line = bufferedReader.readLine();

            while (line != null) {
                String[] attributes = line.split(",");
                key = createCSV(attributes, isSecret);
                line = bufferedReader.readLine();
            }
        } catch (IOException ieo) {
            ieo.printStackTrace();
        }
        return key;
    }

    private String createCSV(String[] CSVData, boolean isSecret) {
        String key;
        if (!isSecret) {
            key = CSVData[0];
        } else {
            key = CSVData[1];
        }
        return key;
    }

}
