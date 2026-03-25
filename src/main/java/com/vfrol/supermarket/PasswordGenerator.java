package com.vfrol.supermarket;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordGenerator {
    public static void main(String[] args) {
        String hash = BCrypt.hashpw("12345", BCrypt.gensalt());
        System.out.println(hash);
    }
}