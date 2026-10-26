package src.controlador;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

import src.modelo.entities.*;
import src.modelo.map.Mapa;
import src.modelo.map.Reproductor_audio;
import src.vista.VistaJuego;
import src.modelo.entities.Record;
import src.modelo.entities.Personaje;

public class ControladorJuego {

    private Mapa mapa;
    private Personaje jugador;
    private final VistaJuego vista;

    private Timer timerMovimientoEnemigos;
    private Timer timerJuego;

    private Runnable accionVolverMenu;

    // timer de animación para interpolación suave 
    // solo actualiza posiciones visuales y la camara; no modifica la logica del juego
    private Timer timerAnimacion;
    private static final int MS_POR_FRAME_ANIMACION = 16;

    private boolean juegoActivo = true;

    //control de niveles y rondas de monedas
    private int nivelActual = 1;

    // control de movimiento del jugador con velocidad
    private long ultimoMovimientoJugador = 0;
    private char teclaPulsada = ' '; //espacio = ninguna tecla

    private long tiempoInicioPartida;
    private RecordDAO recordDAO;
    private String nombreJugador;

    public ControladorJuego(VistaJuego vista, Mapa mapa, Personaje jugador, long tiempoInicioPartida, RecordDAO recordDAO, String nombreJugador) {
        if (vista == null || mapa == null || jugador == null) {
        }
        this.vista = vista;
        this.mapa = mapa;
        this.jugador = jugador;
        this.tiempoInicioPartida = tiempoInicioPartida; // guarda el tiempo
        this.recordDAO = recordDAO; // guarda el DAO para guardar el récord luego
        this.nombreJugador = nombreJugador;
        Reproductor_audio.getInstancia().reproducirSonido(1); // arranca con la música del nivel 1

        // se registra como oyente del teclado a traves de la vista
        this.vista.registrarTeclado(new TecladoListener());

        iniciarTimers();
    }
    // ---------- TIMERS ----------

private void iniciarTimers() {
    if (timerMovimientoEnemigos != null) timerMovimientoEnemigos.stop();
    if (timerJuego != null) timerJuego.stop();
    if (timerAnimacion != null) timerAnimacion.stop();

    timerMovimientoEnemigos = new Timer(800, e -> {
        if (juegoActivo) {
            mapa.moverEnemigos(jugador);
            verificarPerdida();
            vista.repaint();
        }
    });
    timerMovimientoEnemigos.start();

    timerJuego = new Timer(100, e -> { //--------------------------
        if (juegoActivo && teclaPulsada != ' ') {
            moverJugadorConVelocidad();
        }
    });
    timerJuego.start();

    timerAnimacion = new Timer(MS_POR_FRAME_ANIMACION, e -> {
        if (juegoActivo) {
            jugador.actualizarPosicionVisual();
            jugador.actualizarBonus();

// === Animación del sprite del jugador ===
        boolean moviendo = (teclaPulsada != ' ');
        jugador.setEnMovimiento(moviendo);

        if (moviendo) {
            long ahora = System.currentTimeMillis();
            if (ahora - jugador.ultimoFrameAnimacion > 100) { // 100ms por frame
                jugador.frameAnimacion++;
                if (jugador.frameAnimacion >= 22) { // 11 cols x 2 filas
                    jugador.frameAnimacion = 0;
                }
                jugador.ultimoFrameAnimacion = ahora;
            }
        } else {
            jugador.frameAnimacion = 0; // idle: vuelve al primer frame
        }

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

            // LOGICA DE NIVELES Y DINERO
            if (juegoActivo && jugador.getDinero() >= 3000) {
                    if (nivelActual < 3) {
                        // MODIFICADO: Detenemos el juego antes de pasar de nivel.
                        detener();
                        nivelActual++;
                        iniciarNivel(nivelActual);
                        return;
                    } else {
                        ganarJuego("¡Bien ahi! Ganaste el juego! Lograste sobrevivir a las deudas de la vida.");
                        return;
                    }
            }
        }
        if (juegoActivo && mapa.noQuedaDinero()){
                mapa.generarDinero();
            vista.repaint();
        }
    }

    // prepara todo para el siguiente nivel
    private void iniciarNivel(int nivel) {
        if (nivel == 2) {
            JOptionPane.showMessageDialog(vista, "¡NIVEL 2!\n Ahora sos un Adulto.\n A agarrar la pala.");
        } else if (nivel == 3) {
            JOptionPane.showMessageDialog(vista, "¡NIVEL 3!\n Ahora sos un Jubilado.\n Ahora cagaste.");
        }
        // Crear nuevo mapa y generar nivel
        this.mapa = new Mapa(40, 40);
        this.mapa.cargarNivel(nivel);
      Reproductor_audio.getInstancia().reproducirSonido(nivel); // Arranca con la música del Nivel 1
        this.mapa.generarDinero();

        // crear el personaje correspondiente al nivel
        this.jugador = PersonajeFactory.crearPersonaje(nivel, 5, 5, mapa);

        // limpiar la celda de inicio por si hay paredes o enemigos de casualidad
        this.mapa.celdas[5][5].contenido = null; 
        this.mapa.setPersonaje(this.jugador, 5, 5);

        // actualizar la vista con los nuevos datos
        this.vista.actualizarModelo(this.mapa, this.jugador, nivel);
        this.vista.desactivarGameOver();

        // reiniciamos el juego (timers) despues de que el jugador acepte el mensaje.
        this.juegoActivo = true;
        iniciarTimers();
        this.vista.repaint();

    }

    // ---------- REGLAS DEL JUEGO ----------
    private void verificarPerdida() {
        if (jugador.getDinero() < 0) {
            guardarRecordActual(nivelActual);
             Reproductor_audio.getInstancia().detenerSonido();
             vista.mostrarGameOver();
            detener();

            String mensaje;
            switch (nivelActual) {
                case 1: mensaje = "¡PERDISTE! Te quedaste sin plata. Chau play 5:(."; break;
                case 2: mensaje = "¡PERDISTE! Te quedaste sin plata. No llegaste a fin de mes. Pinchó mal."; break;
                case 3: mensaje = "¡PERDISTE! Te quedaste sin plata. Perdiste toda la plata de la jubilación. Cagamo."; break;
                default: mensaje = "¡PERDISTE! Te quedaste sin plata."; break;
            }
            int opcion = JOptionPane.showOptionDialog(
            vista, mensaje +  "¿Y ahora?", 
            "¿Que hacemo?",
            JOptionPane.YES_NO_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE, 
            null, 
            new Object[] {"Reintentar","Volver al Menú","Salir"},
            "Reintentar");
           
            if (opcion == 0) { //Reintentar
                // se reinicia el juego desde el nivel 1
                nivelActual = 1;
                iniciarNivel(nivelActual);
            } else if (opcion == 1){ //Volver al menu
                vista.dispose(); //se cierra la ventana del juego
                if(accionVolverMenu != null){
                    accionVolverMenu.run(); //se ejecuta la accion para volver al menu
                }
            } else { //salir
                 System.exit(0);
            }
        }
    }
    public void setAccionVolverMenu(Runnable accion){
        this.accionVolverMenu = accion;
    }

    private void ganarJuego(String mensaje) {
        juegoActivo = false;
        guardarRecordActual(3); // Guarda récord de nivel 3
        long tiempoTotal = System.currentTimeMillis() - tiempoInicioPartida;
        JOptionPane.showMessageDialog(vista, mensaje);
        System.exit(0);
    }

    // ---------- OYENTE DEL TECLADO ----------
    private class TecladoListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            //Menu de Pausa
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                juegoActivo = false;
             
                JLabel mensaje = new JLabel("Juego pausado.");
                mensaje.setHorizontalAlignment(SwingConstants.CENTER);
                int opcion = JOptionPane.showOptionDialog(
                    vista, 
                    mensaje,
                    "Pausa",
                    JOptionPane.YES_NO_CANCEL_OPTION, 
                    JOptionPane.PLAIN_MESSAGE, 
                    null, 
                    new Object[] {"Continuar", "Volver al Menú", "Salir"},
                    "Continuar");
                if (opcion == 0) { //continuar
                    juegoActivo = true;
                    vista.requestFocusInWindow(); //le devuelvo el foco a la ventana del juego
                 
                } else if (opcion == 1){ //volver al menu
                    detener();
                    vista.dispose(); //se cierra la ventana del juego
                    if(accionVolverMenu != null){
                    accionVolverMenu.run(); //se ejecuta la accion para volver al menu
                } 
                }
                else {
                    System.exit(0); //se cierra el juego
                }
                return;
            }

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
                    if (jugador instanceof CreadorParedes){
                        CreadorParedes creador = (CreadorParedes) jugador;
                    creador.crearParedes();
                    vista.repaint();
                }
                
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

      private void guardarRecordActual(int nivelAlcanzado) {
        if (recordDAO == null) return;
        long tiempoTotal = System.currentTimeMillis() - tiempoInicioPartida;
        Record nuevoRecord = new Record(this.nombreJugador, tiempoTotal, nivelAlcanzado);
        recordDAO.guardar(nuevoRecord);
    }
   
    // ---------- CIERRE ----------
    public void detener() {
        juegoActivo = false;
         Reproductor_audio.getInstancia().detenerSonido(); // ¡Para que frene la música al perder!Mapa.detenerSonido();
        if (timerMovimientoEnemigos != null) timerMovimientoEnemigos.stop();
        if (timerJuego != null) timerJuego.stop();
        // === NUEVO: detener también el timer de animación ===
        if (timerAnimacion != null) timerAnimacion.stop();
    }
}