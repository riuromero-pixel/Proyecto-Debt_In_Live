package src.modelo.entities;
import src.modelo.map.Mapa;
public interface CreadorPersonaje { //Interfaz para el patrón factory
    Personaje crearPersonaje(int x, int y, Mapa mapa);
}
