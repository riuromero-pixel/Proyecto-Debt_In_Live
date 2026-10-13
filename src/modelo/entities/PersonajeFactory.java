package src.modelo.entities;

import src.modelo.map.Mapa;

//Patron factory Method para crear personajes según el nivel
public class PersonajeFactory {
      public static Personaje crearPersonaje(int nivel, int x, int y, Mapa mapa) {
        switch (nivel) {
            case 1: return new Estudiante(x, y, mapa);
            case 2: return new Adulto(x, y, mapa);
            case 3: return new Jubilado(x, y, mapa);
            default: return new Estudiante(x, y, mapa); //Personaje por defecto
        }
    }
}
