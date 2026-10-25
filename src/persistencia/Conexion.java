package src.persistencia;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static Conexion instancia;
    private final Connection conexion;
    
    private Conexion() throws SQLException {//constructor privado
        conexion = DriverManager.getConnection("jdbc:sqlite:debt_in_life.db");
    }
    public static Conexion getInstancia() throws SQLException {
        if (instancia == null || instancia.conexion.isClosed()) {
            instancia = new Conexion();
        }
        return instancia;
    }
    public Connection get() {
        return conexion;
    }
}