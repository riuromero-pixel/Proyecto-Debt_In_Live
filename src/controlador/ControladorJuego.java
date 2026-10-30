package src.controlador;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

import src.modelo.entities.*;
import src.modelo.map.Mapa;
import src.modelo.map.Reproductor_audio;
import src.vista.VistaCargando;
import src.vista.VistaJuego;
import src.modelo.entities.Record;

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
        Reproductor_audio.getInstancia().reproducirSonido(1);

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

    timerJuego = new Timer(100, e -> { 
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
            jugador.frameAnimacion = 0; 
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
                        ganarJuego("¡Bien amigo! Ganaste el juego! Lograste sobrevivir a las deudas de la vida.");
                        return;
                    }
            }
        }
        if (juegoActivo && mapa.noQuedaDinero()){
                mapa.generarDinero();
            vista.repaint();
        }
    }

    private void iniciarNivel(int nivel) {
    //mensajes de cambio de nivel
    if (nivel == 2) {
        JOptionPane.showMessageDialog(vista, "¡NIVEL 2!\n Ahora sos un Adulto.\n  A agarrar la pala.");
    } else if (nivel == 3) {
        JOptionPane.showMessageDialog(vista, "¡NIVEL 3!\n Ahora sos un Jubilado.\n  Ahora cagaste.");
    }

    //diálogo de carga
    VistaCargando cargando = new VistaCargando(vista, "Generando nivel " + nivel + "...");

    //genera el nivel en un HILO DE FONDO
    SwingWorker<Object[], Void> worker = new SwingWorker<Object[], Void>() {
        @Override
        protected Object[] doInBackground() throws Exception {
           
            Thread.sleep(700); // simula la carga para que no parpadee el escritorio

            Mapa nuevoMapa = new Mapa(40, 40);
            nuevoMapa.cargarNivel(nivel);
            nuevoMapa.generarDinero();

            Personaje nuevoJugador = PersonajeFactory.crearPersonaje(nivel, 5, 5, nuevoMapa);
            nuevoMapa.celdas[5][5].contenido = null;
            nuevoMapa.setPersonaje(nuevoJugador, 5, 5);

            return new Object[]{nuevoMapa, nuevoJugador};
        }

        @Override
        protected void done() {
            // === EDT ===
            try {
                Object[] modelo = get();
                mapa = (Mapa) modelo[0];
                jugador = (Personaje) modelo[1];

                Reproductor_audio.getInstancia().reproducirSonido(nivel);
                vista.actualizarModelo(mapa, jugador, nivel);
                vista.desactivarGameOver();

                juegoActivo = true;
                iniciarTimers();
                vista.repaint();

                cargando.dispose();
            } catch (Exception ex) {
                cargando.dispose();
                JOptionPane.showMessageDialog(vista,
                    "Error al cargar el nivel: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    };

    worker.execute();
    cargando.setVisible(true);
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
        long segundosTotal = tiempoTotal/1000;
        long minutos = segundosTotal/60;
        long segundos = segundosTotal % 60;
        JOptionPane.showMessageDialog(vista, mensaje + "\n\n¡Tu tiempo total fue de: " + minutos + " minutos y " + segundos + "segundos!");
        vista.dispose(); //se cierra la ventana
        if (accionVolverMenu != null) { //se vuelve al menu
            accionVolverMenu.run();
        }
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
        if (nivelAlcanzado < 3) return;
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