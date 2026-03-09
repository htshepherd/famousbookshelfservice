package com.famousbookshelf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class InitDb {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:postgresql://aws-1-ap-northeast-2.pooler.supabase.com:5432/postgres";
        String user = "postgres.umvrczjvvoepmhqliztt";
        String pass = "nyKwi6-haxdyd-cakryt";

        System.out.println("Connecting to database...");
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "UPDATE sys_user SET password = ? WHERE username = 'admin'";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "$2a$10$.2D9w4d0WA1sYPGSGBzp7uX8UO8iyTUEynUzzAIi2I2/2Ugy9XOKK");
                int rows = pstmt.executeUpdate();
                System.out.println("Rows updated: " + rows);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
