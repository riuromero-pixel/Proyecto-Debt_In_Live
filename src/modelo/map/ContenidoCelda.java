package src.modelo.map;

import src.modelo.entities.Personaje;

public interface ContenidoCelda {
    boolean bloqueaPaso();

    default boolean aplicarDanio(Personaje personaje) { return false; }

    default boolean recibirDanioDe(Enemigo enemigo) { return false; }

    default boolean recibirGolpe() { return false; }

    default boolean recolectar(Personaje personaje) { return false; }

    default String getTipoVisual() { return "DESCONOCIDO"; }

    default boolean esGolpeable() { return false; }

    default int contarComoDinero() { return 0; }

    default boolean puedeMoverse() { return false; }

    default void actualizarMovimiento(Mapa mapa, Personaje jugador) { }

    default Personaje asPersonaje() { return null; }

    default Enemigo asEnemigo() { return null; }
}