package src.controlador;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

import src.modelo.entities.*;
import src.modelo.map.Mapa;
import src.modelo.map.Reproductor_audio;
import src.vista.VistaCargando;
import src.vista.VistaDialogoEstilo;
import src.vista.VistaJuego;
import src.modelo.entities.Record;
import java.util.concurrent.ExecutionException;

public class ControladorJuego { // Controlador principal del juego, maneja la lógica del juego, los eventos de teclado y la interacción con la vista y el modelo

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

    timerMovimientoEnemigos = new Timer(800, e -> { //timer de movimiento de enemigos (cada 800ms) 
        if (juegoActivo) {
            mapa.moverEnemigos(jugador);
            verificarPerdida();
            vista.repaint();
        }
    });
    timerMovimientoEnemigos.start();

    timerJuego = new Timer(100, e -> { // timer del jugador (cada 100ms)
        if (juegoActivo && teclaPulsada != ' ') {
            moverJugadorConVelocidad();
        }
    });
    timerJuego.start();

    timerAnimacion = new Timer(MS_POR_FRAME_ANIMACION, e -> { //timer de animación para interpolación suave (cada 16ms ~ 60fps)
        if (juegoActivo) {
            jugador.actualizarPosicionVisual();
            jugador.actualizarBonus();

// === Animación del sprite del jugador ===
        jugador.actualizarAnimacion(teclaPulsada != ' ');

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
            VistaDialogoEstilo.mostrarMensaje(vista, "¡NIVEL 2!", "Ahora sos un Adulto", "A agarrar la pala.");
        } else if (nivel == 3) {
            VistaDialogoEstilo.mostrarMensaje(vista, "¡NIVEL 3!", "Ahora sos un Jubilado", "Ahora cagaste.");
        }

    //diálogo de carga
    VistaCargando cargando = new VistaCargando(vista, "Generando nivel " + nivel + "...");

    //genera el nivel en un HILO DE FONDO
    SwingWorker<Object[], Void> worker = new SwingWorker<Object[], Void>() {
        @Override
        protected Object[] doInBackground() throws Exception {

    Mapa nuevoMapa = new Mapa(40, 40);
    nuevoMapa.cargarNivel(nivel);
    nuevoMapa.generarDinero();

    Personaje nuevoJugador = PersonajeFactory.crearPersonaje(nivel, 5, 5, nuevoMapa);
    nuevoMapa.getCeldas()[5][5].setContenido(null);
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
                vista.setTiempoInicioPartida(tiempoInicioPartida);
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

            int opcion = VistaDialogoEstilo.mostrarOpciones(
            vista,
            "¡PERDISTE!",
            "¿Que hacemo?",
            mensaje,
            new String[]{"Reintentar", "Volver al Menú", "Salir"}
            );
           
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
        guardarRecordActual(3); 
        long tiempoTotal = System.currentTimeMillis() - tiempoInicioPartida;
        long segundosTotal = tiempoTotal/1000;
        long minutos = segundosTotal/60;
        long segundos = segundosTotal % 60;
        
        // NUEVO: Mensaje de victoria con estilo
        VistaDialogoEstilo.mostrarMensaje(
                vista, 
                "¡GANASTE!", 
                "Sobreviviste a las deudas de la vida", 
                mensaje + "\n\n¡Tu tiempo total fue de: " + minutos + " minutos y " + segundos + " segundos!"
        );
        
        vista.dispose(); 
        if (accionVolverMenu != null) { 
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
             
                // NUEVO: Menu de pausa con estilo
                int opcion = VistaDialogoEstilo.mostrarOpciones(
                        vista,
                        "Juego pausado",
                        "Tomate un respiro",
                        "¿Querés continuar jugando?",
                        new String[]{"Continuar", "Volver al Menú", "Salir"}
                );

                if (opcion == 0) { //continuar
                    juegoActivo = true;
                    vista.requestFocusInWindow(); 
                } else if (opcion == 1){ //volver al menu
                    detener();
                    vista.dispose(); 
                    if(accionVolverMenu != null){
                        accionVolverMenu.run(); 
                    } 
                } else {
                    System.exit(0); 
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

    new SwingWorker<Boolean, Void>() {
        @Override
        protected Boolean doInBackground() {
            // HILO DE FONDO: guardar en SQLite sin bloquear el EDT.
            return recordDAO.guardar(nuevoRecord);
        }

        @Override
        protected void done() {
            try {
                if (!get()) {
                    System.err.println("No se pudo guardar el récord.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }.execute();
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