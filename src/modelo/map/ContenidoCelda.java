package src.modelo.map;

import src.modelo.entities.Personaje;

public interface ContenidoCelda {
    boolean bloqueaPaso();

    default void aplicarDanio(Personaje personaje) { }
}