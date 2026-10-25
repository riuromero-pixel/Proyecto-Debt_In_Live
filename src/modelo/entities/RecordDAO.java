package src.modelo.entities;
import java.util.List;

public interface RecordDAO {
    boolean guardar(Record record);
    List<Record> obtenerMejores(); //lista de mejores records
    
}