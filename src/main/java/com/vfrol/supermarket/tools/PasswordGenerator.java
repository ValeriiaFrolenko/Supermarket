package com.vfrol.supermarket.tools;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordGenerator {
    public static void main(String[] args) {
        generatePassword("12345");
    }

    public static String generatePassword(String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("Hashed password: " + hashedPassword);
        return hashedPassword;
    }
}