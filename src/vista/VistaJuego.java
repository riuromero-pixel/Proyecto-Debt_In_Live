package src.vista;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import src.modelo.entities.Personaje;
import src.modelo.map.ContenidoCelda;
import src.modelo.map.Enemigo;
import src.modelo.map.EnemigoLetal;
import src.modelo.map.Mapa;

public class VistaJuego extends JFrame { // Clase que representa la ventana del juego y se encarga de dibujar el mapa, el personaje y los enemigos
    
    private Mapa mapa;
    private Personaje jugador;
    private int nivelActual;
    private int tamaño = 100;
    private static final int PANEL_ALTO = 60;
    private boolean gameOver = false; //bandera para saber si terminó el juego
    private long tiempoInicioPartida;
    private CargadorImagenesNivel imagenesNivel;
   

    //buffer para evitar parpadeo
    private Image bufferImagen;
    private Graphics bufferGraphics;

    private final Map<Personaje, EstadoVisualPersonaje> estadosJugador = new HashMap<>();
    private final Map<Enemigo, EstadoVisualPersonaje> estadosEnemigos = new HashMap<>();

    public void cargarImagenesNivel() {
    imagenesNivel.cargar(nivelActual);
}
    public VistaJuego(Mapa mapa, Personaje jugador, int nivelActual,
                  CargadorImagenesNivel imagenes) {
        this.mapa = mapa;
        this.jugador = jugador;
        this.nivelActual = nivelActual;
        this.imagenesNivel = imagenes;       
        
        setTitle("Juego");
        setSize(15 * tamaño + 45, 10 * tamaño + 120);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setFocusable(true);

         // Pantalla completa
         setUndecorated(true);

         // Fondo oscuro (por si algo queda sin cubrir)
         getContentPane().setBackground(new Color(30, 30, 40));

    }

    private final CamaraJuego camara = new CamaraJuego();
private final DibujanteMapa dibujanteMapa = new DibujanteMapa();
private final DibujanteEntidades dibujanteEntidades = new DibujanteEntidades();
private final DibujantePanelInfo dibujantePanelInfo = new DibujantePanelInfo();

    public void mostrarGameOver(){
        this.gameOver = true;
        this.repaint();
    }
    public void desactivarGameOver(){
        this.gameOver = false;
    }

    //Permite al controlador actualizar el mapa, el jugador y el nivel actual al cambiar de nivel
    public void actualizarModelo(Mapa mapa, Personaje jugador, int nivelActual,CargadorImagenesNivel imagenes) {
        this.mapa = mapa;
        this.jugador = jugador;
        this.nivelActual = nivelActual;
        this.imagenesNivel = imagenes;

        this.bufferImagen = null;
        this.estadosJugador.clear();
        this.estadosEnemigos.clear();
        this.reiniciarCamaraSuave();
    }
        public void setTiempoInicioPartida(long tiempoInicio) { // permite al controlador establecer el tiempo de inicio de la partida para calcular el tiempo transcurrido
        this.tiempoInicioPartida = tiempoInicio;
    } 

    public EstadoVisualPersonaje getEstadoVisual(Personaje jugador) {
        return estadosJugador.get(jugador);
    }

    public EstadoVisualPersonaje getEstadoVisual(Enemigo enemigo) {
        return estadosEnemigos.get(enemigo);
    }

    public void actualizarEstadoVisual(Mapa mapa, Personaje jugador, boolean moviendo) {
    if (jugador != null) {
        EstadoVisualPersonaje estado = estadosJugador.get(jugador);
        if (estado == null) {
            estado = new EstadoVisualPersonaje(jugador.getX(), jugador.getY());
            estadosJugador.put(jugador, estado);
        }
        String direccion = jugador.getDireccionActual();
        if (direccion != null) {
            switch (direccion) {
                case "W": estado.setDireccionVisual("N"); break;
                case "S": estado.setDireccionVisual("S"); break;
                case "A": estado.setDireccionVisual("O"); break;
                case "D": estado.setDireccionVisual("E"); break;
            }
        }
        estado.actualizarPosicionVisual(jugador.getX(), jugador.getY());
        estado.actualizarAnimacion(moviendo);
    }

    if (mapa != null) {
        for (int y = 0; y < mapa.getAlto(); y++) {
            for (int x = 0; x < mapa.getAncho(); x++) {
                ContenidoCelda contenido = mapa.getContenido(x, y);
                if (!(contenido instanceof Enemigo enemigo)) continue;

                EstadoVisualPersonaje estado = estadosEnemigos.get(enemigo);
                if (estado == null) {
                    estado = new EstadoVisualPersonaje(enemigo.getX(), enemigo.getY());
                    estadosEnemigos.put(enemigo, estado);
                }
                double distX = Math.abs(estado.getPosXVisual() - enemigo.getX());
                double distY = Math.abs(estado.getPosYVisual() - enemigo.getY());
                if (enemigo instanceof EnemigoLetal && (distX > 1.5 || distY > 1.5)) {
                    estado.teletransportar(enemigo.getX(), enemigo.getY());
                } else {
                    estado.actualizarPosicionVisual(enemigo.getX(), enemigo.getY());
                }
                estado.actualizarAnimacion(false);
            }
        }
    }
}

    // suaviza el desplazamiento de la camara hacia la posicion objetivo 
    // sigue al jugador usando su posicion VISUAL (no la logica) para que el paneo
    // sea continuo incluso entre celdas.
    public void actualizarCamaraSuave() {
    camara.actualizar(jugador, mapa, getEstadoVisual(jugador));
}

    // reinicia el estado de la camara suave (util al cambiar de nivel)
    public void reiniciarCamaraSuave() {
    camara.reiniciar();
}

    //el controlador llama a este metodo para mostrar la ventana
    public void mostrar() {
         Rectangle pantalla = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration()
            .getBounds();
            setBounds(pantalla);
            setVisible(true);
            this.toFront();
            this.requestFocusInWindow();
    }

    //el controlador se registra como listener del teclado a traves de la vista
    public void registrarTeclado(java.awt.event.KeyListener listener) {
        addKeyListener(listener);
    }

    @Override
public void paint(Graphics g) {
    int anchoPantalla = getWidth();
    int altoPantalla = getHeight();

    if (bufferImagen == null
            || bufferImagen.getWidth(null) != anchoPantalla
            || bufferImagen.getHeight(null) != altoPantalla) {
        bufferImagen = createImage(getWidth(), getHeight());
        bufferGraphics = bufferImagen.getGraphics();
    }

    Graphics2D g2d = (Graphics2D) bufferGraphics;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    if (gameOver) {
        g2d.setColor(new Color(150, 0, 0));
    } else {
        g2d.setColor(new Color(30, 30, 40));
    }
    g2d.fillRect(0, 0, anchoPantalla, altoPantalla);

    int tamañoX = anchoPantalla / CamaraJuego.VIEW_COLS;
    int tamañoY = (altoPantalla - PANEL_ALTO) / CamaraJuego.VIEW_ROWS;
    this.tamaño = Math.min(tamañoX, tamañoY);

    int anchoMapa = CamaraJuego.VIEW_COLS * tamaño;
    int altoMapa = CamaraJuego.VIEW_ROWS * tamaño;
    int desplazamientoX = (anchoPantalla - anchoMapa) / 2;
    int desplazamientoY = (altoPantalla - PANEL_ALTO - altoMapa) / 2;

    EstadoVisualPersonaje estadoJugador = getEstadoVisual(jugador);
    double camX = camara.getCamX(jugador, mapa, estadoJugador);
    double camY = camara.getCamY(jugador, mapa, estadoJugador);

    dibujanteMapa.dibujar(g2d, mapa, camX, camY, tamaño, desplazamientoX, desplazamientoY, imagenesNivel, nivelActual);
    dibujanteEntidades.dibujarEnemigos(g2d, mapa, camX, camY, tamaño, desplazamientoX, desplazamientoY, imagenesNivel, jugador, this);
    dibujanteEntidades.dibujarJugador(g2d, jugador, camX, camY, tamaño, desplazamientoX, desplazamientoY, this);
    dibujantePanelInfo.dibujar(g2d, anchoPantalla, altoPantalla, jugador, mapa, nivelActual, tiempoInicioPartida, gameOver);

    g.drawImage(bufferImagen, 0, 0, this);
}
        
        
 }