package src.modelo.entities;
import src.modelo.map.Mapa;
public class CreadorAdulto implements CreadorPersonaje { //Clase concreta para crear adultos
    @Override
    public Personaje crearPersonaje(int x, int y, Mapa mapa) {
        return new Adulto(x, y, mapa);
    }
    
}
