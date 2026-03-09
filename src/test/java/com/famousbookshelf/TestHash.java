package com.famousbookshelf;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = "$2a$10$wD81/B6Z5p9.0O6K99Jc4Ob5A/4J24.f.2w16m0vD6176.f5.Yn9y";
        System.out.println("Matches: " + encoder.matches("admin123", hash));
        System.out.println("New hash: " + encoder.encode("admin123"));
    }
}
