package src.modelo.map;

import src.modelo.entities.Personaje;

public interface ContenidoCelda {
    boolean bloqueaPaso();

    default boolean interactuar(Personaje p) { return false; }

}