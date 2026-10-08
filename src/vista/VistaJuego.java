package src.vista;

import src.modelo.entities.Personaje;
import src.modelo.entities.Adulto;
import src.modelo.entities.Estudiante;
import src.modelo.map.Mapa;
import src.modelo.map.Pared;
import src.modelo.map.Dinero;
import src.modelo.map.Enemigo;
import src.modelo.map.Bonus;

import javax.swing.*;
import java.awt.*;

public class VistaJuego extends JFrame {

    private Mapa mapa;
    private Personaje jugador;
    private int nivelActual;
    private final int tamaño = 60; // tamaño de cada celda en píxeles
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

    public VistaJuego(Mapa mapa, Personaje jugador, int nivelActual) {
        this.mapa = mapa;
        this.jugador = jugador;
        this.nivelActual = nivelActual;

        setTitle("Juego");
        setSize(15 * tamaño + 45, 10 * tamaño + 120);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    //Permite al controlador actualizar el mapa, el jugador y el nivel actual al cambiar de nivel
    public void actualizarModelo(Mapa mapa, Personaje jugador, int nivelActual) {
        this.mapa = mapa;
        this.jugador = jugador;
        this.nivelActual = nivelActual;
        this.bufferImagen = null; //reiniciar buffer para evitar problemas de renderizado al cambiar de nivel
        //reiniciar la cámara suave para que arranque centrada en el nuevo nivel ===
        this.reiniciarCamaraSuave();
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
        setVisible(true);
    }

    //el controlador se registra como listener del teclado a traves de la vista
    public void registrarTeclado(java.awt.event.KeyListener listener) {
        addKeyListener(listener);
    }

    @Override
    public void paint(Graphics g) {
        // Usar doble buffer para evitar parpadeo
        if (bufferImagen == null) {
            bufferImagen = createImage(getWidth(), getHeight());
            bufferGraphics = bufferImagen.getGraphics();
        }

        Graphics2D g2d = (Graphics2D) bufferGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //limpiar fondo
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());

        int desplazamientoX = 15;
        int desplazamientoY = 35;

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

                g2d.setColor(Color.WHITE);
                g2d.fillRect(px, py, tamaño, tamaño);

                if (mapa.celdas[y][x].contenido != null) {
                    Object obj = mapa.celdas[y][x].contenido;
                    if (obj instanceof Pared) {
                        g2d.setColor(Color.BLACK);
                        g2d.fillRect(px, py, tamaño, tamaño);
                    } else if (obj instanceof Dinero) {
                        g2d.setColor(Color.YELLOW);
                        g2d.fillOval(px + 5, py + 5, tamaño - 10, tamaño - 10);
                        g2d.setColor(Color.BLACK);
                        g2d.drawString("$", px + 10, py + 20);
                    } else if (obj instanceof Bonus) {
                        g2d.setColor(Color.GREEN);
                        g2d.fillOval(px + 5, py + 5, tamaño - 10, tamaño - 10);
                        g2d.setColor(Color.BLACK);
                        g2d.drawString("B", px + 10, py + 20);
                    }
                    // === NUEVO: los ENEMIGOS ya NO se dibujan acá ===
                    // Se dibujan más abajo, en su propio bucle, usando su posición VISUAL
                    // (posXVisual, posYVisual) para que la interpolación funcione.
                    // Cuando agreguemos sprites, el sprite del enemigo se dibujará en ese bucle.
                }

                //dibujar dinero debajo (mismo estilo)
                if (mapa.celdas[y][x].dineroDebajo != null) {
                    g2d.setColor(Color.YELLOW);
                    g2d.fillOval(px + 5, py + 5, tamaño - 10, tamaño - 10);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString("$", px + 10, py + 20);
                }
                //dibujar bonus
                if (mapa.celdas[y][x].bonusDebajo != null) {
                    g2d.setColor(Color.GREEN);
                    g2d.fillOval(px + 5, py + 5, tamaño - 10, tamaño - 10);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString("B", px + 10, py + 20);
                }

                g2d.setColor(Color.GRAY);
                g2d.drawRect(px, py, tamaño, tamaño);
            }
        }

        // dibujar los enemigos con su posicion visual interpolada 
        // aca es donde, cuando tengamos sprites, se dibujara la imagen del enemigo
        for (int y = 0; y < mapa.alto; y++) {
            for (int x = 0; x < mapa.ancho; x++) {
                if (mapa.celdas[y][x].contenido instanceof Enemigo) {
                    Enemigo en = (Enemigo) mapa.celdas[y][x].contenido;

                    // Culling: dibujar solo si su posición visual cae dentro del viewport ampliado
                    if (en.posXVisual + 1 < camX || en.posXVisual > camX + VIEW_COLS) continue;
                    if (en.posYVisual + 1 < camY || en.posYVisual > camY + VIEW_ROWS) continue;

                    int ex = (int) ((en.posXVisual - camX) * tamaño) + desplazamientoX;
                    int ey = (int) ((en.posYVisual - camY) * tamaño) + desplazamientoY;

                    // --- placeholder actual: rectángulo rojo con "V" ---
                    g2d.setColor(Color.RED);
                    g2d.fillRect(ex + 4, ey + 4, tamaño - 8, tamaño - 8);
                    g2d.setColor(Color.WHITE);
                    g2d.drawString("V", ex + 11, ey + 20);
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
        int panelY = 10 * tamaño + 45; //justo debajo del mapa
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

        //copiar el buffer a la pantalla
        g.drawImage(bufferImagen, 0, 0, this);
    }

    private void dibujarPersonaje(Graphics2D g2d, int px, int py) {
        int margen = tamaño / 10;
        int radio = tamaño / 2 - margen;

        // === SOMBRA ===
        g2d.setColor(new Color(0, 0, 0, 60));
        g2d.fillOval(px + margen + 2, py + margen + 4, radio * 2, radio * 2);

        // === CARA ===
        g2d.setColor(new Color(255, 220, 50));
        g2d.fillOval(px + margen, py + margen, radio * 2, radio * 2);

        // === BORDE ===
        g2d.setColor(new Color(200, 170, 0));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(px + margen, py + margen, radio * 2, radio * 2);

        // === OJOS ===
        int ojoTamaño = tamaño / 6;
        int ojoY = py + tamaño / 3;
        int ojoX1 = px + tamaño / 4;
        int ojoX2 = px + tamaño * 3 / 4 - ojoTamaño;

        g2d.setColor(Color.BLACK);
        g2d.fillOval(ojoX1, ojoY, ojoTamaño, ojoTamaño);
        g2d.fillOval(ojoX2, ojoY, ojoTamaño, ojoTamaño);

        // === REFLEJO EN OJOS ===
        int reflejo = ojoTamaño / 3;
        g2d.setColor(Color.WHITE);
        g2d.fillOval(ojoX1 + 2, ojoY + 1, reflejo, reflejo);
        g2d.fillOval(ojoX2 + 2, ojoY + 1, reflejo, reflejo);

        // === SONRISA ===
        int sonrisaX = px + tamaño / 5;
        int sonrisaY = py + tamaño / 2 + 2;
        int sonrisaAncho = tamaño * 3 / 5;
        int sonrisaAlto = tamaño / 4;

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawArc(sonrisaX, sonrisaY, sonrisaAncho, sonrisaAlto, 0, -180);

        // === MEJILLAS ===
        int mejillaTamaño = tamaño / 6;
        g2d.setColor(new Color(255, 150, 150, 100));
        g2d.fillOval(px + margen + 2, py + tamaño / 2 + 2, mejillaTamaño, mejillaTamaño / 2);
        g2d.fillOval(px + tamaño - margen - mejillaTamaño - 2, py + tamaño / 2 + 2, mejillaTamaño, mejillaTamaño / 2);
    }
}