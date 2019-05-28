package com.example.demo.Services;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class HashingService {

    private MessageDigest messageDigest;

    {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String hash(String toHash) {
        byte[] hashBytes = messageDigest.digest(toHash.getBytes
                (StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hashBytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }

}

