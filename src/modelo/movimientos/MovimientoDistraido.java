package src.modelo.movimientos;

import java.util.Random;

import src.modelo.entities.Personaje;
import src.modelo.map.Enemigo;
import src.modelo.map.Mapa;

public class MovimientoDistraido implements EstrategiaMovimiento {

    private static final long DURACION_DISTRACCION_MS = 7000;
    private static final long PERSECUCION_MIN_MS = 1000;
    private static final long PERSECUCION_MAX_MS = 5000;

    private final EstrategiaMovimiento perseguidor = new MovimientoPerseguidor();
    private final EstrategiaMovimiento errante = new MovimientoErrante();
    private final Random random = new Random();

    private boolean distraido = false;
    private long inicioFase = System.currentTimeMillis();
    private long duracionFase = nuevaDuracionPersecucion();

    @Override
    public void mover(Enemigo enemigo, Personaje jugador, Mapa mapa) {
        actualizarFase();
        EstrategiaMovimiento elegida = distraido ? errante : perseguidor;
        elegida.mover(enemigo, jugador, mapa);
    }

    private void actualizarFase() {
        long ahora = System.currentTimeMillis();
        if (ahora - inicioFase < duracionFase) return;

        distraido = !distraido;
        inicioFase = ahora;
        duracionFase = distraido ? DURACION_DISTRACCION_MS : nuevaDuracionPersecucion();
    }

    private long nuevaDuracionPersecucion() {
        return PERSECUCION_MIN_MS + (long) (random.nextDouble() * (PERSECUCION_MAX_MS - PERSECUCION_MIN_MS));
    }
}