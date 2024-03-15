package uk.ac.soton.comp2211.users;

import uk.ac.soton.comp2211.App;
import uk.ac.soton.comp2211.data.Database;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Credentials {
    private static final String databaseName = "user_info";

    private Connection connect() {
        return Database.getConnection(databaseName);
    }

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

    private boolean isAlreadyUser(String username) {
        if (username == null || username.isEmpty()) throw new RuntimeException();

        String query = "SELECT EXISTS (SELECT 1 FROM credentials WHERE username = (?))";

        try (Connection conn = connect();
             PreparedStatement prep = conn.prepareStatement(query)) {
            prep.setString(1, username.toLowerCase());
            ResultSet rs = prep.executeQuery();

            if (rs.next()) {
                return (rs.getBoolean(1));
            } else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addUser(String username, String password, Permissions permissions) {
        if (!App.getUser().getPermissions().equals(Permissions.ADMIN)) return false;
        if (username == null || username.isEmpty()) return false;
        if (password == null || password.isEmpty()) return false;
        if (isAlreadyUser(username)) return false;

        String insertionStatement = "INSERT INTO credentials(username, password, permission) VALUES(?,?,?)";

        try (Connection conn = connect();
             PreparedStatement prep = conn.prepareStatement(insertionStatement)) {
            if (permissions == null) {
                permissions = Permissions.VIEWER;
            }

            prep.setString(1, username.toLowerCase());
            prep.setString(2, hash(password));
            prep.setString(3, permissions.name().toUpperCase());

            return prep.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteUser(String username) {
        if (!App.getUser().getPermissions().equals(Permissions.ADMIN)) return false;
        if (username == null || username.isEmpty()) return false;
        if (!isAlreadyUser(username)) return false;
        if (username.equalsIgnoreCase(App.getUser().getUsername())) return false;

        String deleteSql = """
                                DELETE FROM credentials
                                WHERE username = (?)
                                """;

        try (Connection conn = connect();
             PreparedStatement prep = conn.prepareStatement(deleteSql)) {
            prep.setString(1, username.toLowerCase());

            return prep.execute();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updatePermissions(String username, Permissions permissions) {
        if (!App.getUser().getPermissions().equals(Permissions.ADMIN)) return false;
        if (username == null || username.isEmpty()) return false;
        if (username.equalsIgnoreCase(App.getUser().getUsername())) return false;
        if (!isAlreadyUser(username)) return false;

        String updateSql = """
                              UPDATE OR ROLLBACK credentials
                              SET permissions = (?)
                              WHERE username = (?);
                              """;

        if (permissions == null) {
            permissions = Permissions.VIEWER;
        }

        try (Connection conn = connect();
             PreparedStatement stat = conn.prepareStatement(updateSql)) {
            stat.setString(1, permissions.name().toUpperCase());
            stat.setString(2, username.toLowerCase());

            return stat.execute();
        } catch (Exception e) {
            return false;
        }
    }

    private String getStoredPassword(String username) {
        if (username == null || username.isEmpty()) throw new RuntimeException();

        String query = "SELECT password FROM credentials WHERE username = (?)";

        try (Connection conn = connect();
             PreparedStatement prep = conn.prepareStatement(query)) {
            prep.setString(1, username);
            ResultSet rs = prep.executeQuery();

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
        if (username == null || username.isEmpty()) throw new RuntimeException();

        String query = "SELECT permission FROM credentials WHERE username = (?)";

        try (Connection conn = connect();
             PreparedStatement prep = conn.prepareStatement(query)) {
            prep.setString(1, username);
            ResultSet rs = prep.executeQuery();

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
        if (username == null || username.isEmpty()) return false;

        String storedPassword = getStoredPassword(username.toLowerCase());
        String hashedInput = hash(password);

        if (storedPassword == null) return false;

        return storedPassword.equals(hashedInput);
    }
}