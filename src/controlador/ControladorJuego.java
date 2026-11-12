package src.controlador;

import src.modelo.entities.*;
import src.modelo.map.Mapa;
import src.modelo.map.Reproductor_audio;
import src.vista.VistaJuego;

public class ControladorJuego {

    private Mapa mapa;
    private Personaje jugador;
    private final VistaJuego vista;

    private final GestorTimersJuego gestorTimers = new GestorTimersJuego();
    private final GestorNivelesJuego gestorNiveles = new GestorNivelesJuego();
    private final GestorReglasJuego gestorReglas = new GestorReglasJuego();
    private final GuardadorRecordsJuego guardadorRecords = new GuardadorRecordsJuego();

    private Runnable accionVolverMenu;
    private boolean juegoActivo = true;
    private int nivelActual = 1;
    private long ultimoMovimientoJugador = 0;
    private char teclaPulsada = ' ';
    private long tiempoInicioPartida;
    private RecordDAO recordDAO;
    private String nombreJugador;

    public ControladorJuego(VistaJuego vista, Mapa mapa, Personaje jugador,
                            long tiempoInicioPartida, RecordDAO recordDAO, String nombreJugador) {
        this.vista = vista;
        this.mapa = mapa;
        this.jugador = jugador;
        this.tiempoInicioPartida = tiempoInicioPartida;
        this.recordDAO = recordDAO;
        this.nombreJugador = nombreJugador;

        Reproductor_audio.getInstancia().reproducirSonido(1);

        this.vista.registrarTeclado(new EntradaTecladoJuego(this));
        iniciarTimers();
    }

    public void iniciarTimers() {
        gestorTimers.iniciar(this);
    }

    public void detener() {
        juegoActivo = false;
        Reproductor_audio.getInstancia().detenerSonido();
        gestorTimers.detener();
    }

    void moverEnemigos() {
        if (juegoActivo) {
            mapa.moverEnemigos(jugador);
            gestorReglas.verificarPerdida(this);
            vista.repaint();
        }
    }

    void animar() {
        if (juegoActivo) {
            jugador.actualizarPosicionVisual();
            jugador.actualizarBonus();
            jugador.actualizarAnimacion(teclaPulsada != ' ');
            mapa.actualizarPosicionesVisualesEnemigos();
            vista.actualizarCamaraSuave();
            vista.repaint();
        }
    }

    public void moverJugadorConVelocidad() {
        if (!juegoActivo || teclaPulsada == ' ') return;

        long tiempoActual = System.currentTimeMillis();
        int intervaloMovimiento = 500 / Math.max(1, jugador.getVelocidad());

        if (tiempoActual - ultimoMovimientoJugador >= intervaloMovimiento) {
            int anchoMapa = mapa.getAncho();
            int altoMapa = mapa.getAlto();

            jugador.moverse(String.valueOf(teclaPulsada), anchoMapa, altoMapa);
            ultimoMovimientoJugador = tiempoActual;

            gestorReglas.verificarPerdida(this);

            if (juegoActivo && jugador.getDinero() >= 3000) {
                if (nivelActual < 3) {
                    detener();
                    nivelActual++;
                    iniciarNivel(nivelActual);
                    return;
                } else {
                    gestorReglas.ganarJuego(this, "¡Bien ahi! Ganaste el juego! Lograste sobrevivir a las deudas de la vida.");
                    return;
                }
            }
        }

        if (juegoActivo && mapa.noQuedaDinero()) {
            mapa.generarDinero();
            vista.repaint();
        }
    }

    public void iniciarNivel(int nivel) {
        gestorNiveles.iniciarNivel(this, nivel);
    }

    public void cambiarNivel(Mapa nuevoMapa, Personaje nuevoJugador, int nivel) {
        this.mapa = nuevoMapa;
        this.jugador = nuevoJugador;
        this.nivelActual = nivel;

        Reproductor_audio.getInstancia().reproducirSonido(nivel);
        vista.actualizarModelo(mapa, jugador, nivel);
        vista.setTiempoInicioPartida(tiempoInicioPartida);
        vista.desactivarGameOver();

        juegoActivo = true;
        iniciarTimers();
        vista.repaint();
    }

    public void mostrarPausa() {
        gestorReglas.mostrarPausa(this);
    }

    public void guardarRecordActual(int nivel) {
        guardadorRecords.guardar(this, nivel);
    }

    public void volverAlMenu() {
        if (accionVolverMenu != null) {
            accionVolverMenu.run();
        }
    }

    public void setAccionVolverMenu(Runnable accion) {
        this.accionVolverMenu = accion;
    }

    public Mapa getMapa() { return mapa; }
    public Personaje getJugador() { return jugador; }
    public VistaJuego getVista() { return vista; }
    public boolean isJuegoActivo() { return juegoActivo; }
    public void setJuegoActivo(boolean juegoActivo) { this.juegoActivo = juegoActivo; }
    public int getNivelActual() { return nivelActual; }
    public void setNivelActual(int nivelActual) { this.nivelActual = nivelActual; }
    public char getTeclaPulsada() { return teclaPulsada; }
    public void setTeclaPulsada(char teclaPulsada) { this.teclaPulsada = teclaPulsada; }
    public long getTiempoInicioPartida() { return tiempoInicioPartida; }
    public RecordDAO getRecordDAO() { return recordDAO; }
    public String getNombreJugador() { return nombreJugador; }
}