package src.vista;

import java.awt.*;
import javax.swing.*;
import src.modelo.entities.Personaje;
import src.modelo.map.Mapa;

public class VistaJuego extends JFrame { // Clase que representa la ventana del juego y se encarga de dibujar el mapa, el personaje y los enemigos
    
    private Mapa mapa;
    private Personaje jugador;
    private int nivelActual;
    private int tamaño = 100;
    private static final int PANEL_ALTO = 60;
    private boolean gameOver = false; //bandera para saber si terminó el juego
    private long tiempoInicioPartida;
   

    //buffer para evitar parpadeo
    private Image bufferImagen;
    private Graphics bufferGraphics;

    public void cargarImagenesNivel() {
    imagenesNivel.cargar(nivelActual);
}
    public VistaJuego(Mapa mapa, Personaje jugador, int nivelActual) {
        this.mapa = mapa;
        this.jugador = jugador;
        this.nivelActual = nivelActual;
      
        cargarImagenesNivel();
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
private final CargadorImagenesNivel imagenesNivel = new CargadorImagenesNivel();
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
    public void actualizarModelo(Mapa mapa, Personaje jugador, int nivelActual) {
        this.mapa = mapa;
        this.jugador = jugador;
        this.nivelActual = nivelActual;
        
        this.bufferImagen = null; //reiniciar buffer para evitar problemas de renderizado al cambiar de nivel
        //reiniciar la cámara suave para que arranque centrada en el nuevo nivel ===
        this.reiniciarCamaraSuave();
        cargarImagenesNivel();
    }
        public void setTiempoInicioPartida(long tiempoInicio) { // permite al controlador establecer el tiempo de inicio de la partida para calcular el tiempo transcurrido
        this.tiempoInicioPartida = tiempoInicio;
    } 

    // suaviza el desplazamiento de la camara hacia la posicion objetivo 
    // sigue al jugador usando su posicion VISUAL (no la logica) para que el paneo
    // sea continuo incluso entre celdas.
    public void actualizarCamaraSuave() {
    camara.actualizar(jugador, mapa);
}

    // reinicia el estado de la camara suave (util al cambiar de nivel)
    public void reiniciarCamaraSuave() {
    camara.reiniciar();
}

    //el controlador llama a este metodo para mostrar la ventana
    public void mostrar() {
        setUndecorated(true);
         GraphicsDevice gd = GraphicsEnvironment
        .getLocalGraphicsEnvironment()
        .getDefaultScreenDevice();
        gd.setFullScreenWindow(this);
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

    double camX = camara.getCamX(jugador, mapa);
    double camY = camara.getCamY(jugador, mapa);

    dibujanteMapa.dibujar(g2d, mapa, camX, camY, tamaño, desplazamientoX, desplazamientoY, imagenesNivel, nivelActual);
    dibujanteEntidades.dibujarEnemigos(g2d, mapa, camX, camY, tamaño, desplazamientoX, desplazamientoY, imagenesNivel, jugador);
    dibujanteEntidades.dibujarJugador(g2d, jugador, camX, camY, tamaño, desplazamientoX, desplazamientoY);
    dibujantePanelInfo.dibujar(g2d, anchoPantalla, altoPantalla, jugador, mapa, nivelActual, tiempoInicioPartida, gameOver);

    g.drawImage(bufferImagen, 0, 0, this);
}
        
         
 }
