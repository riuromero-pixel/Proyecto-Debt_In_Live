package src.controlador;

import src.entities.Personaje;
import src.map.Mapa;
import src.vista.VistaJuego;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ControladorJuego {

    private final Mapa mapa;
    private final Personaje jugador;
    private final VistaJuego vista;

    private Timer timerMovimientoEnemigos;
    private Timer timerJuego;

    private boolean juegoActivo = true;

    // control de movimiento del jugador con velocidad
    private long ultimoMovimientoJugador = 0;
    private char teclaPulsada = ' '; //espacio = ninguna tecla

    public ControladorJuego(VistaJuego vista, Mapa mapa, Personaje jugador) {
        if (vista == null || mapa == null || jugador == null) {
            throw new IllegalArgumentException("vista, mapa y jugador son obligatorios");
        }
        this.vista = vista;
        this.mapa = mapa;
        this.jugador = jugador;

        // se registra como oyente del teclado a traves de la vista
        this.vista.registrarTeclado(new TecladoListener());

        iniciarTimers();
    }

    // ---------- TIMERS ----------
    private void iniciarTimers() {
        timerMovimientoEnemigos = new Timer(800, e -> {
            if (juegoActivo) {
                mapa.moverEnemigos();
                verificarPerdida();
                vista.repaint();
            }
        });
        timerMovimientoEnemigos.start();

        timerJuego = new Timer(50, e -> {
            if (juegoActivo && teclaPulsada != ' ') {
                moverJugadorConVelocidad();
            }
        });
        timerJuego.start();
    }

    // ---------- LOGICA DE MOVIMIENTO ----------
    private void moverJugadorConVelocidad() {
        long tiempoActual = System.currentTimeMillis();
        int intervaloMovimiento = 500 / Math.max(1, jugador.getVelocidad());

        if (tiempoActual - ultimoMovimientoJugador >= intervaloMovimiento) {
            int anchoMapa = mapa.getAncho();
            int altoMapa = mapa.getAlto();

            jugador.moverse(String.valueOf(teclaPulsada), anchoMapa, altoMapa);
            ultimoMovimientoJugador = tiempoActual;

            verificarPerdida();

            if (juegoActivo && mapa.noQuedaDinero()) {
                ganarJuego();
            }

            vista.repaint();
        }
    }

    // ---------- REGLAS DEL JUEGO ----------
    private void verificarPerdida() {
        if (jugador.getDinero() < 0) {
            juegoActivo = false;
            JOptionPane.showMessageDialog(vista, "¡PERDISTE! Te quedaste sin dinero.");
            System.exit(0);
        }
    }

    private void ganarJuego() {
        juegoActivo = false;
        JOptionPane.showMessageDialog(vista, "¡GANASTE! Recolectaste todo el dinero.");
        System.exit(0);
    }

    // ---------- OYENTE DEL TECLADO ----------
    private class TecladoListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            char tecla = e.getKeyChar();

            switch (tecla) {
                case 'w': case 'W': teclaPulsada = 'W'; break;
                case 's': case 'S': teclaPulsada = 'S'; break;
                case 'a': case 'A': teclaPulsada = 'A'; break;
                case 'd': case 'D': teclaPulsada = 'D'; break;
                case 'r': case 'R':
                    jugador.romperParedes();
                    vista.repaint();
                    break;
                case 'c': case 'C':
                    jugador.crearParedes();
                    vista.repaint();
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            char tecla = e.getKeyChar();
            if (Character.toUpperCase(tecla) == teclaPulsada) {
                teclaPulsada = ' ';
            }
        }
    }

    // ---------- CIERRE ----------
    public void detener() {
        juegoActivo = false;
        if (timerMovimientoEnemigos != null) timerMovimientoEnemigos.stop();
        if (timerJuego != null) timerJuego.stop();
    }
}