package com.vfrol.supermarket.tools;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordManager {

    public static String generatePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}