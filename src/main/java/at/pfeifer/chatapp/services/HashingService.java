package at.pfeifer.chatapp.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingService {
    public static byte[] hash(byte[]... messages) {
        try {
            MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
            for (byte[] message : messages) {
                sha512.update(message);
            }
            return sha512.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
