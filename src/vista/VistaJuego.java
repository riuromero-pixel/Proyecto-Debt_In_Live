package src.vista;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import src.modelo.entities.Adulto;
import src.modelo.entities.Estudiante;
import src.modelo.entities.Personaje;
import src.modelo.map.Bonus;
import src.modelo.map.Dinero;
import src.modelo.map.Enemigo;
import src.modelo.map.EnemigoLetal;
import src.modelo.map.Mapa;

public class VistaJuego extends JFrame {
    
    private Mapa mapa;
    private Personaje jugador;
    private int nivelActual;
    private int tamaño = 100;
    private static final int PANEL_ALTO = 60;
    private boolean gameOver = false; //bandera para saber si terminó el juego
   
    private static final int VIEW_COLS = 15; //defino los viewports para la ventana que seguirá al jugador
    private static final int VIEW_ROWS = 10;

    // coordenadas suavizadas de la camara (double para permitir subpixel) 
    // se actualizan cada frame en actualizarCamaraSuave() y se usan en paint().
    // -1 significa "todavia sin inicializar".
    private double camXSuave = -1;
    private double camYSuave = -1;
    private static final double SUAVIDAD_CAMARA = 0.15;

    //buffer para evitar parpadeo
    private Image bufferImagen;
    private Graphics bufferGraphics;
    //NACHO SI LEES ESTO ACA ESTAN LAS VARIABLES DE FONDO Y MURO CAMBIALAS SEGUN EL NIVEL
    private Image imagenPARED;
    private Image imagenPISO;
    private Image imagenSilla1, imagenSilla2, imagenSilla3, imagenSilla4; 
    private Image imagenDinero;
    private Image[] spritesEnemigos = new Image[5];

    public void cargarImagenesNivel() {
    String paredtemp;
    String pisotemp;

    try {
    imagenDinero = ImageIO.read(new File("images/sprites/dinero.png")); 
} catch (IOException e) {
    System.out.println("Error al cargar la imagen del dinero");
}

    switch (nivelActual) {
        case 1:
            paredtemp = "images/casillero.PNG";
            pisotemp = "images/baldosablanca.PNG";
            try {
                imagenSilla1 = ImageIO.read(new File("images/sprites/estudiante/paredes/sillaabajo.png"));
                imagenSilla2 = ImageIO.read(new File("images/sprites/estudiante/paredes/sillaarriba.png"));
                imagenSilla3 = ImageIO.read(new File("images/sprites/estudiante/paredes/silladerecha.png"));
                imagenSilla4 = ImageIO.read(new File("images/sprites/estudiante/paredes/sillaizquierda.png"));
                
                // Enemigos nivel 1 (4 variantes)
                    spritesEnemigos[0] = ImageIO.read(new File("images/sprites/estudiante/enemigos/alfajor.png"));
                    spritesEnemigos[1] = ImageIO.read(new File("images/sprites/estudiante/enemigos/futbol.png"));
                    spritesEnemigos[2] = ImageIO.read(new File("images/sprites/estudiante/enemigos/play5.png"));
                    spritesEnemigos[3] = ImageIO.read(new File("images/sprites/estudiante/enemigos/steam.png"));

            } catch (IOException e) {
                System.out.println("Error al cargar los sprites del nivel 1");
            }
            break;

        case 2:
            paredtemp = "images/sprites/adulto/paredes/bloqueladrillo.PNG";
            pisotemp = "images/baldosatierra.PNG";
            try {
                    // Enemigos nivel 2 (5 variantes)
                    spritesEnemigos[0] = ImageIO.read(new File("images/sprites/adulto/enemigos/agua.png"));
                    spritesEnemigos[1] = ImageIO.read(new File("images/sprites/adulto/enemigos/alquiler.png"));
                    spritesEnemigos[2] = ImageIO.read(new File("images/sprites/adulto/enemigos/asado.png"));
                    spritesEnemigos[3] = ImageIO.read(new File("images/sprites/adulto/enemigos/bebe.png"));
                    spritesEnemigos[4] = ImageIO.read(new File("images/sprites/adulto/enemigos/cerveza.png"));
                } catch (IOException e) { System.out.println("Error al cargar los sprites del nivel 2"); }
            break;
        case 3:
            paredtemp = "images/arbusto.PNG";
            pisotemp = "images/baldosapasto.PNG";
            try {
                    // Enemigos nivel 3 (4 variantes)
                    spritesEnemigos[0] = ImageIO.read(new File("images/sprites/jubilado/enemigos/casino.png"));
                    spritesEnemigos[1] = ImageIO.read(new File("images/sprites/jubilado/enemigos/compras.png"));
                    spritesEnemigos[2] = ImageIO.read(new File("images/sprites/jubilado/enemigos/luz.png"));
                    spritesEnemigos[3] = ImageIO.read(new File("images/sprites/jubilado/enemigos/pami.png"));
                } catch (IOException e) { System.out.println("Error al cargar los sprites del nivel 2"); }
                break;
        default:
            imagenPARED = null;
            imagenPISO = null;
            return;
    }

    try {
        imagenPARED = ImageIO.read(new File(paredtemp));
        imagenPISO = ImageIO.read(new File(pisotemp));
    } catch (IOException e) {
        System.out.println("Error al cargar imágenes del nivel " + nivelActual);
        e.printStackTrace();
    }
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
        //setFocusTraversalKeysEnabled(false);

        

         // Pantalla completa
         setUndecorated(true);
         GraphicsDevice gd = GraphicsEnvironment
          .getLocalGraphicsEnvironment()
          .getDefaultScreenDevice();
          gd.setFullScreenWindow(this);
         // Fondo oscuro (por si algo queda sin cubrir)
         getContentPane().setBackground(new Color(30, 30, 40));

    }
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

    // suaviza el desplazamiento de la camara hacia la posicion objetivo 
    // sigue al jugador usando su posicion VISUAL (no la logica) para que el paneo
    // sea continuo incluso entre celdas.
    public void actualizarCamaraSuave() {
        if (jugador == null || mapa == null) return;

        // Objetivo: centrar al jugador en el viewport
        double camXObjetivo = jugador.getPosXVisual() - VIEW_COLS / 2.0;
        double camYObjetivo = jugador.getPosYVisual() - VIEW_ROWS / 2.0;

        // clamp para no salir del mapa
        camXObjetivo = Math.max(0, Math.min(camXObjetivo, mapa.ancho - VIEW_COLS));
        camYObjetivo = Math.max(0, Math.min(camYObjetivo, mapa.alto - VIEW_ROWS));

        // primera vez: inicializar sin interpolar (evita un "salto" inicial desde 0,0)
        if (camXSuave < 0 || camYSuave < 0) {
            camXSuave = camXObjetivo;
            camYSuave = camYObjetivo;
            return;
        }

        // interpolacion exponencial hacia el objetivo
        camXSuave += (camXObjetivo - camXSuave) * SUAVIDAD_CAMARA;
        camYSuave += (camYObjetivo - camYSuave) * SUAVIDAD_CAMARA;
    }

    // reinicia el estado de la camara suave (util al cambiar de nivel)
    public void reiniciarCamaraSuave() {
        camXSuave = -1;
        camYSuave = -1;
    }

    //el controlador llama a este metodo para mostrar la ventana
    public void mostrar() {
         GraphicsDevice gd = GraphicsEnvironment
        .getLocalGraphicsEnvironment()
        .getDefaultScreenDevice();
        gd.setFullScreenWindow(this);
        setVisible(true);
        this.requestFocus();
        this.requestFocusInWindow();
    }

    //el controlador se registra como listener del teclado a traves de la vista
    public void registrarTeclado(java.awt.event.KeyListener listener) {
        addKeyListener(listener);
    }

    @Override
    public void paint(Graphics g) {
        int anchoPantalla = getWidth();
        int altoPantalla  = getHeight();
        // Usar doble buffer para evitar parpadeo
        if (bufferImagen == null
        || bufferImagen.getWidth(null) != anchoPantalla
        || bufferImagen.getHeight(null) != altoPantalla)
        {
            bufferImagen = createImage(getWidth(), getHeight());
            bufferGraphics = bufferImagen.getGraphics();
        }

        Graphics2D g2d = (Graphics2D) bufferGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(gameOver){
            g2d.setColor(new Color(150, 0 ,0 )); // fondo rojo del game over
        } else {
            g2d.setColor(new Color(30, 30, 40)); // fondo negro por defecto
        }
        g2d.fillRect(0, 0, anchoPantalla, altoPantalla);
       
        // === Calcular tamaño de celda dinámicamente ===
       int tamañoX = anchoPantalla / VIEW_COLS;
       int tamañoY = (altoPantalla - PANEL_ALTO) / VIEW_ROWS;
       this.tamaño = Math.min(tamañoX, tamañoY);

       // === Calcular desplazamiento para centrar ===
       int anchoMapa = VIEW_COLS * tamaño;
       int altoMapa  = VIEW_ROWS * tamaño;
       int desplazamientoX = (anchoPantalla - anchoMapa) / 2;
       int desplazamientoY = (altoPantalla - PANEL_ALTO - altoMapa) / 2;

        // === CAMARA SUAVIZADA ===
        // se usan las coordenadas suavizadas (double) para permitir desplazamiento subpixel.
        // si todavia no se llamo a actualizarCamaraSuave() (por ej primer paint)
        // se calcula directamente como respaldo a partir de la posición visual del jugador.
        double camX, camY;
        if (camXSuave < 0 || camYSuave < 0) {
            double camXCalc = jugador != null ? jugador.getPosXVisual() - VIEW_COLS / 2.0 : 0;
            double camYCalc = jugador != null ? jugador.getPosYVisual() - VIEW_ROWS / 2.0 : 0;
            camXCalc = Math.max(0, Math.min(camXCalc, mapa.ancho - VIEW_COLS));
            camYCalc = Math.max(0, Math.min(camYCalc, mapa.alto - VIEW_ROWS));
            camX = camXCalc;
            camY = camYCalc;
        } else {
            camX = camXSuave;
            camY = camYSuave;
        }

        // rango de celdas a dibujar considerando desplazamiento subpixel 
        // se agrega 1 celda extra a cada borde para no dejar huecos cuando la cámara
        // esta entre dos celdas.
        int inicioX = (int) Math.floor(camX);
        int inicioY = (int) Math.floor(camY);

        //dibujar el mapa 
        for (int y = inicioY; y <= inicioY + VIEW_ROWS; y++) {
            for (int x = inicioX; x <= inicioX + VIEW_COLS; x++) {
                // Evitar dibujar fuera de los límites del mapa
                if (y < 0 || y >= mapa.alto || x < 0 || x >= mapa.ancho) continue;

                // Posición en pantalla usando las coordenadas double de la cámara
                int px = (int) ((x - camX) * tamaño) + desplazamientoX;
                int py = (int) ((y - camY) * tamaño) + desplazamientoY;
                //g2d.setColor(Color.WHITE);
                g2d.fillRect(px, py, tamaño, tamaño);

               // dibujamos el piso 
if (imagenPISO != null) {
    g2d.drawImage(imagenPISO, px, py, tamaño, tamaño, null);
} else {
    g2d.setColor(Color.WHITE); // Por si no carga la imagen
    g2d.fillRect(px, py, tamaño, tamaño);

}

// dibujamos el contenido
if (mapa.celdas[y][x].contenido != null) {
    Object obj = mapa.celdas[y][x].contenido;
    
    // 1. SOLO si es Pared, dibujamos la imagen de pared
    if (obj instanceof src.modelo.map.Pared) {
        src.modelo.map.Pared pared = (src.modelo.map.Pared) obj;
        Image imgDibujar = imagenPARED; // Imagen por defecto (casillero)

        if (nivelActual == 1) {
            switch(pared.getVariante()) {
                case 1: if (imagenSilla1 != null) imgDibujar = imagenSilla1; break;
                case 2: if (imagenSilla2 != null) imgDibujar = imagenSilla2; break;
                case 3: if (imagenSilla3 != null) imgDibujar = imagenSilla3; break;
                case 4: if (imagenSilla4 != null) imgDibujar = imagenSilla4; break;
            }
        }

        if (imgDibujar != null) {
            g2d.drawImage(imgDibujar, px, py, tamaño, tamaño, null);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(px, py, tamaño, tamaño);
        }
    } 
    // 2. Si es Dinero
    else if (obj instanceof Dinero) {
       if (imagenDinero != null) {
        // Dibuja la imagen ocupando toda la celda (o podés sumarle un margen si la querés más chica)
        g2d.drawImage(imagenDinero, px, py, tamaño, tamaño, null);
    } else {
        // Fallback: si por algún motivo no carga la imagen, dibuja el círculo amarillo
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(px + tamaño/4, py + tamaño/4, tamaño/2, tamaño/2);
    }
}
    // 3. Si es un Bonus
    else if (obj instanceof Bonus) {
        Bonus unBonus = (Bonus) obj;
        g2d.drawImage(unBonus.getImagenBonus(), px + 5, py + 5, tamaño - 10, tamaño - 10, null);
        g2d.setColor(Color.BLACK);
        g2d.drawString("B", px + 10, py + 20);
    }

                g2d.setColor(Color.GRAY);
                g2d.drawRect(px, py, tamaño, tamaño);
            }
        
        }}
        for (int y = 0; y < mapa.alto; y++) {
            for (int x = 0; x < mapa.ancho; x++) {
                if (mapa.celdas[y][x].contenido instanceof Enemigo) {
                    Enemigo en = (Enemigo) mapa.celdas[y][x].contenido;

            if (en.posXVisual + 1 < camX || en.posXVisual > camX + VIEW_COLS) continue;
            if (en.posYVisual + 1 < camY || en.posYVisual > camY + VIEW_ROWS) continue;

                    int ex = (int) ((en.posXVisual - camX) * tamaño) + desplazamientoX;
                    int ey = (int) ((en.posYVisual - camY) * tamaño) + desplazamientoY;

                    if (en instanceof EnemigoLetal){
                        //dibujo enemigo letal (por ahora es negro)
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(ex + 4, ey + 4, tamaño - 8, tamaño - 8);
                    g2d.setColor(Color.RED);
                    g2d.drawString("V", ex + 11, ey + 20);

                    } else {
                    // Dibujamos el sprite real del enemigo
                     int var = en.getVariante();
                     Image imgEnemigo = (var >= 1 && var <= spritesEnemigos.length) 
                        ? spritesEnemigos[var - 1] : null;
    
                    if (imgEnemigo != null) {
                     g2d.drawImage(imgEnemigo, ex, ey, tamaño, tamaño, null);
                    } else {
                    // Fallback por si no cargó la imagen
                    g2d.setColor(Color.RED);
                    g2d.fillRect(ex + 4, ey + 4, tamaño - 8, tamaño - 8);
                    g2d.setColor(Color.WHITE);
                    g2d.drawString("V", ex + 11, ey + 20);
    }
}
                    
                }
            }
        }

        //dibujar el personaje
        if (jugador != null) {
            // se usa la posicion visual (double) del jugador para que se mueva suave 
            // Cuando agreguemos sprites, aqui se dibujara la imagen del personaje:
            // g2d.drawImage(spriteJugador, px, py, tamaño, tamaño, null);
            int px = (int) ((jugador.getPosXVisual() - camX) * tamaño) + desplazamientoX;
            int py = (int) ((jugador.getPosYVisual() - camY) * tamaño) + desplazamientoY;
            dibujarPersonaje(g2d, px, py);
        }

        // === DIBUJAR PANEL DE INFORMACIÓN ABAJO ===
        int panelY = altoPantalla - PANEL_ALTO; //justo debajo del mapa
        int panelHeight = 60;

        //fondo del panel
        g2d.setColor(new Color(50, 50, 60));
        g2d.fillRect(0, panelY, getWidth(), panelHeight);

        //texto de dinero (grande y amarillo)
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        String dineroTexto = "💰 Dinero: $" + (jugador != null ? jugador.getDinero() : 0);
        g2d.drawString(dineroTexto, 20, panelY + 35);

        // NUEVO: Mostrar monedas restantes en el mapa (en color cyan para que resalte)
        g2d.setColor(Color.CYAN);
        String monedasRestantes = "Monedas restantes: " + (mapa != null ? mapa.contarDineroRestante() : 0);
        g2d.drawString(monedasRestantes, 250, panelY + 35);

        //texto de información (blanco)
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String tipo = "";
        if (jugador instanceof Adulto) tipo = "Adulto";
        else if (jugador instanceof Estudiante) tipo = "Estudiante";
        else if (jugador != null) tipo = "Jubilado";

        String infoTexto = String.format("Personaje: %s | Fuerza: %d | Velocidad: %d | Nivel: %d",
                tipo,
                jugador != null ? jugador.getFuerza() : 0,
                jugador != null ? jugador.getVelocidad() : 0,
                nivelActual);
        g2d.drawString(infoTexto, 20, panelY + 15);

        if (gameOver){
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 80));
            String texto = "PINCHÓ";
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(texto, (anchoPantalla - fm.stringWidth(texto)) / 2, (altoPantalla -PANEL_ALTO)/ 2 - 50);
        }

        //copiar el buffer a la pantalla
        g.drawImage(bufferImagen, 0, 0, this);
    }
        
         // NUEVO: dibujamos estudiante
        private void dibujarPersonaje(Graphics2D g2d, int px, int py) {
        
        // MODIFICADO: Se elimina el if(jugador instanceof Estudiante) y todo el casting engorroso.
        // Ahora le pedimos a GestorSprites que nos dé la imagen correspondiente de manera limpia.
        java.awt.Image sprite = GestorSprites.getInstance().obtenerSpritePersonaje(jugador);
        
        if (sprite != null) {
            // Obtenemos el ancho y alto original de la imagen
            int anchoOriginal = sprite.getWidth(null);
            int altoOriginal = sprite.getHeight(null);

            // Calculamos la escala para que la imagen entre en la celda
            double margen = 1.3; // 1.3 veces el tamaño de la celda
            double escala = (double) tamaño / Math.max(anchoOriginal, altoOriginal) * margen;

            // Calculamos las nuevas dimensiones manteniendo la proporción
            int anchoDibujo = (int) (anchoOriginal * escala);
            int altoDibujo  = (int) (altoOriginal * escala);

            // Calculamos la posición para que quede centrado horizontalmente y pegado al piso
            int dibujoX = px + (tamaño - anchoDibujo) / 2;
            int dibujoY = py + (tamaño - altoDibujo); 

            // Dibujamos con las medidas calculadas
            g2d.drawImage(sprite, dibujoX, dibujoY, anchoDibujo, altoDibujo, null);
            
        } else {
            // Se mantiene el círculo de sombra por defecto si la imagen no llegara a cargar
            int margen = tamaño / 10;
            int radio = tamaño / 2 - margen;
            
            g2d.setColor(new Color(0, 0, 0, 60));
            g2d.fillOval(px + margen + 2, py + margen + 4, radio * 2, radio * 2);
        }
    }

        
     
 }
