package src.persistencia;

import src.modelo.entities.Record;
import src.modelo.entities.RecordDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecordDAOSQLite implements RecordDAO {
    private final Connection conexion;

    public RecordDAOSQLite(Connection conexion) {
        this.conexion = conexion;
        crearTablaSiNoExiste();
    }

    private void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS records (" +
                     "jugador TEXT PRIMARY KEY," + 
                     "tiempo INTEGER NOT NULL," +
                     "nivel INTEGER NOT NULL," +
                     "fecha TEXT NOT NULL)";
        try (Statement stmt = conexion.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla: " + e.getMessage());
        }
    }

    @Override
    public boolean guardar(Record record) {
        String sql = "INSERT INTO records (jugador, tiempo, nivel, fecha) VALUES (?, ?, ?, ?) " +
                     "ON CONFLICT(jugador) DO UPDATE SET " +
                     "tiempo = MAX(tiempo, excluded.tiempo), " + 
                     "nivel = CASE WHEN excluded.tiempo > records.tiempo THEN excluded.nivel ELSE records.nivel END, " +
                     "fecha = CASE WHEN excluded.tiempo > records.tiempo THEN excluded.fecha ELSE records.fecha END";
                     
        try (PreparedStatement s = conexion.prepareStatement(sql)) {
            s.setString(1, record.getNombreJugador());
            s.setLong(2, record.getTiempoMilisegundos());
            s.setInt(3, record.getNivelAlcanzado());
            s.setString(4, record.getFecha());
            return s.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Record> obtenerMejores() {
        List<Record> lista = new ArrayList<>();
        // ORDER BY tiempo DESC: El tiempo mayor (más aguantó) sale primero
        String sql = "SELECT jugador, tiempo, nivel FROM records ORDER BY tiempo DESC LIMIT 10";
        
        try (PreparedStatement s = conexion.prepareStatement(sql);
             ResultSet filas = s.executeQuery()) {
            while (filas.next()) {
                lista.add(new Record(
                    filas.getString("jugador"),
                    filas.getLong("tiempo"),
                    filas.getInt("nivel")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}