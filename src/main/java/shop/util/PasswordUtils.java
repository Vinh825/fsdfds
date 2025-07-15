/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for hashing passwords and checking password validity. This
 * class provides methods to hash passwords using BCrypt and to check if a plain
 * password matches a hashed one.
 *
 * @author Le Anh Khoa - CE190449
 */
public class PasswordUtils {

    /**
     * Hashes the given password using MD5.
     *
     * @param password the password to be hashed
     * @return the hashed password
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes("UTF-8"));  // Ensure UTF-8 encoding
            byte[] digest = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString().toUpperCase(); 
        } catch (Exception e) {
            throw new RuntimeException("MD5 hashing error", e);
        }
    }

    /**
     * Checks if the given plain password matches the hashed password using MD5.
     *
     * @param plainPassword the plain password to check
     * @param hashedPassword the hashed password to compare against
     * @return true if the plain password matches the hashed password, false
     * otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        // Hash the plain password and compare it with the stored hashed password
        String hashedPlainPassword = hashPassword(plainPassword);
        return hashedPlainPassword.equals(hashedPassword);
    }
}
