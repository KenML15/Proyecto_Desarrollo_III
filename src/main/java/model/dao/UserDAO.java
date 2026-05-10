package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * DAO para autenticación de usuarios con rol.
 * Devuelve el rol ("admin" | "clerk") si las credenciales son válidas,
 * o null si no lo son.
 */
public class UserDAO {

    /**
     * Valida usuario y contraseña.
     *
     * @param username nombre de usuario
     * @param password contraseña (en producción usar hash SHA-256)
     * @return "admin", "clerk" o null si las credenciales son incorrectas
     */
    public String authenticate(String username, String password) {
        String sql = "SELECT role FROM users WHERE username = ? AND password = ? AND active = 1";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");   // "admin" o "clerk"
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;   // credenciales incorrectas o error
    }
}