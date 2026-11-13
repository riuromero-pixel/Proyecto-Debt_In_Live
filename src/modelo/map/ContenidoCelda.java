package src.modelo.map;

import src.modelo.entities.Personaje;

public interface ContenidoCelda {
    boolean bloqueaPaso();

    default void aplicarDanio(Personaje personaje) { }

    default boolean recibirDanioDe(Enemigo enemigo) { return false; }

    default boolean recibirGolpe() { return false; }

    default boolean recolectar(Personaje personaje) { return false; }

    default String getTipoVisual() { return "DESCONOCIDO"; }

    default Personaje asPersonaje() { return null; }

    default Enemigo asEnemigo() { return null; }

    default Dinero asDinero() { return null; }

    default boolean esPared() { return false; }

    default boolean esDinero() { return false; }

    default boolean esEnemigo() { return false; }
}