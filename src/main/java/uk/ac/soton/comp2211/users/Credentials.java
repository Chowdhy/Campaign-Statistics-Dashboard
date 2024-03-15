package uk.ac.soton.comp2211.users;

import uk.ac.soton.comp2211.data.Database;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Credentials {
    private String hash(String password) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(password.getBytes(StandardCharsets.UTF_8));

        byte[] bytes = messageDigest.digest();
        StringBuilder stringBuilder = new StringBuilder();

        for (byte hashByte : bytes) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hex);
        }

        return stringBuilder.toString();
    }

    private String getStoredPassword(String username) {
        String query = "SELECT password FROM credentials WHERE username = (?)";

        try (Connection conn = Database.getConnection("user_info");
             PreparedStatement stat = conn.prepareStatement(query)) {
            stat.setString(1, username);
            ResultSet rs = stat.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Permissions getAuthentication(String username) {
        String query = "SELECT permission FROM credentials WHERE username = (?)";

        try (Connection conn = Database.getConnection("user_info");
             PreparedStatement stat = conn.prepareStatement(query)) {
            stat.setString(1, username);
            ResultSet rs = stat.executeQuery();

            if (rs.next()) {
                String permission = rs.getString(1);

                return Permissions.find(permission);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateCredentials(String username, String password) throws Exception {
        String storedPassword = getStoredPassword(username.toLowerCase());
        String hashedInput = hash(password);

        if (storedPassword == null) return false;

        return storedPassword.equals(hashedInput);
    }
}