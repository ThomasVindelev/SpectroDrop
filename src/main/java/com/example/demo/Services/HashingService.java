package com.example.demo.Services;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//Lavet af Marco Pedersen

@Service
public class HashingService {

    //https://www.mkyong.com/java/java-md5-hashing-example/?fbclid=IwAR1yE9AU4oHzFOuhCaAxw_1HGdQseYgLJutm0FFX9IrwS1_rUGC6Bb-1mvk

    private MessageDigest messageDigest;

    {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hasher kodeord
     *
     */

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

