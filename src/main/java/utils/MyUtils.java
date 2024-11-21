package utils;

import java.security.MessageDigest;
import java.util.regex.Pattern;

public class MyUtils {
    public static boolean isValidUsername(String username) {
        return Pattern.matches("^[a-zA-Z0-9]{3,15}$", username);
    }

    public static  boolean isValidEmail(String email) {
        return Pattern.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",email);
    }

    public static String hashPassword(String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
