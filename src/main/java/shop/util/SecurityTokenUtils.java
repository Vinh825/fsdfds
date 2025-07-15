/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.util;

import java.security.SecureRandom;

/**
 *
 * @author Le Anh Khoa - CE190449
 */
public class SecurityTokenUtils {
    private static final SecureRandom random = new SecureRandom();

    // Generate a numeric 6-digit OTP
    public static String generateOTP(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // digits 0–9
        }
        return sb.toString();
    }
}
