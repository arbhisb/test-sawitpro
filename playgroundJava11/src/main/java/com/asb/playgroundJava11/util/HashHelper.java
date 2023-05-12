package com.asb.playgroundJava11.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashHelper {

    public static String hashString(String stringToHash) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(stringToHash.getBytes());
            return new String(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return stringToHash;
        }
    }
}
