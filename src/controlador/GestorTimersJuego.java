package src.controlador;

import javax.swing.Timer;

public class GestorTimersJuego {

    private Timer timerMovimientoEnemigos;
    private Timer timerJuego;
    private Timer timerAnimacion;

    private static final int MS_POR_FRAME_ANIMACION = 16;

    public void iniciar(ControladorJuego controlador) {
        detener();

        timerMovimientoEnemigos = new Timer(800, e -> controlador.moverEnemigos());
        timerMovimientoEnemigos.start();

        timerJuego = new Timer(100, e -> controlador.moverJugadorConVelocidad());
        timerJuego.start();

        timerAnimacion = new Timer(MS_POR_FRAME_ANIMACION, e -> controlador.animar());
        timerAnimacion.start();
    }

    public void detener() {
        if (timerMovimientoEnemigos != null) timerMovimientoEnemigos.stop();
        if (timerJuego != null) timerJuego.stop();
        if (timerAnimacion != null) timerAnimacion.stop();
    }
}