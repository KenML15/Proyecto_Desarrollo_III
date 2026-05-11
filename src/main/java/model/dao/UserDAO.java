package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.entity.User;

/**
 * DAO for system user management (authentication + CRUD).
 */
public class UserDAO {

    /**
     * Validates credentials and returns the user role.
     *
     * @param username the username
     * @param password the plain-text password
     * @return "admin", "clerk", or null if credentials are invalid
     */
    public String authenticate(String username, String password) {
        String sql = "SELECT role FROM users WHERE username = ? AND password = ? AND active = 1";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserts a new system user.
     *
     * @param user the User entity to insert
     * @return true if inserted successfully
     */
    public boolean insert(User user) {
        String sql = "INSERT INTO users (username, password, role, active) VALUES (?, ?, ?, ?)";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setInt(4, user.getActive());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns all system users.
     *
     * @return list of User objects
     */
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, username, password, role, active FROM users ORDER BY id ASC";
        try (Connection con = DbConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                u.setActive(rs.getInt("active"));
                list.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error listing users: " + e.getMessage());
        }
        return list;
    }

    /**
     * Finds a single user by ID.
     *
     * @param id the user ID
     * @return the User object, or null if not found
     */
    public User findById(int id) {
        String sql = "SELECT id, username, password, role, active FROM users WHERE id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(rs.getString("password"));
                    u.setRole(rs.getString("role"));
                    u.setActive(rs.getInt("active"));
                    return u;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Updates an existing user. If the password field is blank, keeps the existing one.
     *
     * @param user the User entity with updated data
     * @return true if updated successfully
     */
    public boolean update(User user) {
        boolean changePassword = user.getPassword() != null && !user.getPassword().trim().isEmpty();
        String sql = changePassword
            ? "UPDATE users SET username = ?, password = ?, role = ?, active = ? WHERE id = ?"
            : "UPDATE users SET username = ?, role = ?, active = ? WHERE id = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (changePassword) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getRole());
                ps.setInt(4, user.getActive());
                ps.setInt(5, user.getId());
            } else {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getRole());
                ps.setInt(3, user.getActive());
                ps.setInt(4, user.getId());
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the user ID to delete
     * @return true if deleted successfully
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks whether a username already exists (used for duplicate validation).
     *
     * @param username the username to check
     * @param excludeId user ID to exclude from the check (pass 0 on insert)
     * @return true if the username is already taken
     */
    public boolean usernameExists(String username, int excludeId) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND id != ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setInt(2, excludeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
        }
        return false;
    }
}