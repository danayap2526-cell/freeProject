package dades;

import modelo.Usuario;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDB {

    public void insertar(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, password) VALUES (?, ?)";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getPasswordHash());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) u.setId(rs.getInt(1));
            }
        }
    }


    public Usuario buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE nombre = ?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }


    public boolean existeNombre(String nombre) throws SQLException {
        return buscarPorNombre(nombre) != null;
    }

    public void actualizarEstadisticas(int id, int victorias,
                                       int derrotas, int empates) throws SQLException {
        String sql = "UPDATE usuarios SET victorias=?, derrotas=?, empates=? WHERE id=?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setInt(1, victorias);
            ps.setInt(2, derrotas);
            ps.setInt(3, empates);
            ps.setInt(4, id);
            ps.executeUpdate();
        }
    }

    public List<Usuario> obtenerRanking() throws SQLException {
        String sql = "SELECT * FROM usuarios ORDER BY victorias DESC, empates DESC LIMIT 20";
        List<Usuario> lista = new ArrayList<>();
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNombre(rs.getString("nombre"));
        u.setPasswordHash(rs.getString("password"));
        u.setVictorias(rs.getInt("victorias"));
        u.setDerrotas(rs.getInt("derrotas"));
        u.setEmpates(rs.getInt("empates"));
        Timestamp ts = rs.getTimestamp("creado_en");
        if (ts != null) u.setCreadoEn(ts.toLocalDateTime());
        return u;
    }

}
