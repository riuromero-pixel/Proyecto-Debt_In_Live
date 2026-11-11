package src.modelo.entities;
import src.modelo.map.Mapa;
public class CreadorEstudiante implements CreadorPersonaje { //Clase concreta para crear estudiantes
    @Override
    public Personaje crearPersonaje(int x, int y, Mapa mapa) {
        return new Estudiante(x, y, mapa);
    }
}
