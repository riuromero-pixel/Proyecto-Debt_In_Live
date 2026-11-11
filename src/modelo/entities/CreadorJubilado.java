package src.modelo.entities;
import src.modelo.map.Mapa;
public class CreadorJubilado implements CreadorPersonaje { //Clase concreta para crear jubilados
    @Override
    public Personaje crearPersonaje(int x, int y, Mapa mapa) {
        return new Jubilado(x, y, mapa);
    }
}
