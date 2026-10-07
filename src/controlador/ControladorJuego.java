package src.controlador;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import src.entities.Adulto;
import src.entities.Jubilado;
import src.entities.Personaje;
import src.map.Mapa;
import src.vista.VistaJuego;

public class ControladorJuego {

    private Mapa mapa;
    private Personaje jugador;
    private final VistaJuego vista;

    private Timer timerMovimientoEnemigos;
    private Timer timerJuego;

    // timer de animación para interpolación suave 
    // solo actualiza posiciones visuales y la camara; no modifica la logica del juego
    private Timer timerAnimacion;
    private static final int MS_POR_FRAME_ANIMACION = 16;

    private boolean juegoActivo = true;

    //control de niveles y rondas de monedas
    private int nivelActual = 1;
    private int rondaActual = 1;
    private int rondasNecesarias = 1;

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
                mapa.moverEnemigos(jugador);
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

        
        timerAnimacion = new Timer(MS_POR_FRAME_ANIMACION, e -> {
            if (juegoActivo) {
                jugador.actualizarPosicionVisual();
                mapa.actualizarPosicionesVisualesEnemigos();
                vista.actualizarCamaraSuave();
                vista.repaint();
            }
        });
        timerAnimacion.start();
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

            // LOGICA DE NIVELES Y RONDAS DE MONEDAS
            if (juegoActivo && mapa.noQuedaDinero()) {
                if (rondaActual < rondasNecesarias) {
                    // MODIFICADO: Ya no se muestra el cartel avisando que aparecen mas monedas.
                    // Simplemente se incrementa la ronda y se generan nuevas monedas.
                    rondaActual++;
                    mapa.generarDinero();
                } else {
                    // Se completaron las rondas de este nivel
                    if (nivelActual < 3) {
                        // MODIFICADO: Detenemos el juego antes de pasar de nivel.
                        detener();
                        nivelActual++;
                        iniciarNivel(nivelActual);
                    } else {
                        ganarJuego("¡Felicidades! Has completado TODOS los niveles del juego.");
                    }
                }
            }

            vista.repaint();
        }
    }

    // prepara todo para el siguiente nivel
    private void iniciarNivel(int nivel) {
        this.rondaActual = 1;

        if (nivel == 2) {
            this.rondasNecesarias = 2;
            JOptionPane.showMessageDialog(vista, "¡NIVEL 2!\nJuegas con el Adulto.\nDebes recolectar todas las monedas 2 veces.");
        } else if (nivel == 3) {
            this.rondasNecesarias = 3;
            JOptionPane.showMessageDialog(vista, "¡NIVEL 3!\nJuegas con el Jubilado.\nDebes recolectar todas las monedas 3 veces.");
        }

        // Crear nuevo mapa y generar nivel
        this.mapa = new Mapa(25, 25);
        this.mapa.cargarNivel(nivel);
        this.mapa.generarDinero();

        // crear el personaje correspondiente al nivel
        switch (nivel) {
            case 2: this.jugador = new Adulto(5, 5, this.mapa); break;
            case 3: this.jugador = new Jubilado(5, 5, this.mapa); break;
        }
        
        // limpiar la celda de inicio por si hay paredes o enemigos de casualidad
        this.mapa.celdas[5][5].contenido = null; 
        this.mapa.setPersonaje(this.jugador, 5, 5);

        // actualizar la vista con los nuevos datos
        this.vista.actualizarModelo(this.mapa, this.jugador, nivel);
        
        // reiniciamos el juego (timers) despues de que el jugador acepte el mensaje.
        this.juegoActivo = true;
        iniciarTimers();
        
        this.vista.repaint();
    }

    // ---------- REGLAS DEL JUEGO ----------
    private void verificarPerdida() {
        if (jugador.getDinero() < 0) {
            juegoActivo = false;
            JOptionPane.showMessageDialog(vista, "¡PERDISTE! Te quedaste sin dinero.");
            System.exit(0);
        }
    }

    private void ganarJuego(String mensaje) {
        juegoActivo = false;
        JOptionPane.showMessageDialog(vista, mensaje);
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
        // === NUEVO: detener también el timer de animación ===
        if (timerAnimacion != null) timerAnimacion.stop();
    }
}