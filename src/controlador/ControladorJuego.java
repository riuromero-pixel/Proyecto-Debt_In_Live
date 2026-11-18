package src.controlador;

import javax.sound.sampled.Clip;

import src.modelo.entities.*;
import src.modelo.map.Mapa;
import src.modelo.map.Reproductor_audio;
import src.vista.CargadorImagenesNivel;
import src.vista.VistaJuego;

public class ControladorJuego {

    private Mapa mapa;
    private Personaje jugador;
    private final VistaJuego vista;
    private final Reproductor_audio reproductorAudio;

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
                            long tiempoInicioPartida, RecordDAO recordDAO, String nombreJugador,
                            Reproductor_audio reproductorAudio, int nivelInicial) {
        this.vista = vista;
        this.mapa = mapa;
        this.jugador = jugador;
        this.tiempoInicioPartida = tiempoInicioPartida;
        this.recordDAO = recordDAO;
        this.nombreJugador = nombreJugador;
        this.reproductorAudio = reproductorAudio;
        this.nivelActual = nivelInicial;


        this.vista.registrarTeclado(new EntradaTecladoJuego(this));
        iniciarTimers();
    }

    public void iniciarTimers() {
        gestorTimers.iniciar(this);
    }

    public void detener() {
        juegoActivo = false;
        this.reproductorAudio.detenerSonido();
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
            jugador.actualizarBonus();
            vista.actualizarEstadoVisual(mapa, jugador, teclaPulsada != ' ');
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
                if (!RegistroNiveles.esUltimoNivel(nivelActual)) {
                    detener();
                    nivelActual++;
                    iniciarNivel(nivelActual);
                    return;
                } else {
                    String mensajeVictoria = RegistroNiveles.paraNivel(nivelActual).getMensajeVictoria();
                    gestorReglas.ganarJuego(this, mensajeVictoria);
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

    public void cambiarNivel(Mapa nuevoMapa, Personaje nuevoJugador, int nivel, CargadorImagenesNivel imagenes, Clip clipMusica) {
        this.mapa = nuevoMapa;
        this.jugador = nuevoJugador;
        this.nivelActual = nivel;

        reproductorAudio.reproducirClip(clipMusica);  
        vista.actualizarModelo(mapa, jugador, nivel, imagenes);
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
    void setJuegoActivo(boolean juegoActivo) { this.juegoActivo = juegoActivo; }
    public int getNivelActual() { return nivelActual; }
    void setNivelActual(int nivelActual) { this.nivelActual = nivelActual; }
    public char getTeclaPulsada() { return teclaPulsada; }
    void setTeclaPulsada(char teclaPulsada) { this.teclaPulsada = teclaPulsada; }
    public long getTiempoInicioPartida() { return tiempoInicioPartida; }
    public RecordDAO getRecordDAO() { return recordDAO; }
    public String getNombreJugador() { return nombreJugador; }
    public Reproductor_audio getReproductorAudio() { return reproductorAudio; }
}