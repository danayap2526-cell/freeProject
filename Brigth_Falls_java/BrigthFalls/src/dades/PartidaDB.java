package dades;

import modelo.Partida;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartidaDB {

    public void insertar(Partida p) throws SQLException {
        String sql = """
            INSERT INTO partidas (jugador1_id, jugador2_id, ganador_id, duracion_seg)
            VALUES (?, ?, ?, ?)
            """;
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getJugador1Id());
            ps.setInt(2, p.getJugador2Id());
            if (p.getGanadorId() != null) ps.setInt(3, p.getGanadorId());
            else                          ps.setNull(3, Types.INTEGER);
            ps.setInt(4, p.getDuracionSegundos());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getInt(1));
            }
        }
    }

    public List<Partida> obtenerHistorial(int limite) throws SQLException {
        String sql = """
            SELECT p.*,
                   u1.nombre AS j1, u2.nombre AS j2,
                   g.nombre  AS ganador
            FROM partidas p
            JOIN usuarios u1 ON p.jugador1_id = u1.id
            JOIN usuarios u2 ON p.jugador2_id = u2.id
            LEFT JOIN usuarios g ON p.ganador_id = g.id
            ORDER BY p.jugada_en DESC
            LIMIT ?
            """;
        List<Partida> lista = new ArrayList<>();
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Partida par = new Partida();
                    par.setId(rs.getInt("id"));
                    par.setJugador1Id(rs.getInt("jugador1_id"));
                    par.setJugador2Id(rs.getInt("jugador2_id"));
                    int gId = rs.getInt("ganador_id");
                    par.setGanadorId(rs.wasNull() ? null : gId);
                    par.setDuracionSegundos(rs.getInt("duracion_seg"));
                    Timestamp ts = rs.getTimestamp("jugada_en");
                    if (ts != null) par.setJugadaEn(ts.toLocalDateTime());
                    par.setNombreJugador1(rs.getString("j1"));
                    par.setNombreJugador2(rs.getString("j2"));
                    par.setNombreGanador(rs.getString("ganador"));
                    lista.add(par);
                }
            }
        }
        return lista;
    }
}
