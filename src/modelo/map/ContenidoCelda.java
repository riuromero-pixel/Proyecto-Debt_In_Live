package src.modelo.map;

import src.modelo.entities.Personaje;

public interface ContenidoCelda {
    boolean bloqueaPaso();

    boolean interactuar(Personaje p);

    default boolean interactuarConEnemigo(Enemigo e) {
        return false;
    }

}