package src.vista;

import src.entities.Personaje;
import src.entities.Adulto;
import src.entities.Estudiante;
import src.map.Mapa;
import src.map.Pared;
import src.map.Dinero;
import src.map.Enemigo;
import javax.swing.*;
import java.awt.*;

public class VistaJuego extends JFrame {

    private final Mapa mapa;
    private final Personaje jugador;
    private final int nivelActual;
    private final int tamaño = 30;

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

        //dibujar el mapa (ocupa la parte superior)
        for (int y = 0; y < mapa.alto; y++) {
            for (int x = 0; x < mapa.ancho; x++) {
                int px = x * tamaño + desplazamientoX;
                int py = y * tamaño + desplazamientoY;

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
                    } else if (obj instanceof Enemigo) {
                        g2d.setColor(Color.RED);
                        g2d.fillRect(px + 4, py + 4, tamaño - 8, tamaño - 8);
                        g2d.setColor(Color.WHITE);
                        g2d.drawString("V", px + 11, py + 20);
                    }
                }

                //dibujar dinero debajo (mismo estilo)
                if (mapa.celdas[y][x].dineroDebajo != null) {
                    g2d.setColor(Color.YELLOW);
                    g2d.fillOval(px + 5, py + 5, tamaño - 10, tamaño - 10);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString("$", px + 10, py + 20);
                }

                g2d.setColor(Color.GRAY);
                g2d.drawRect(px, py, tamaño, tamaño);
            }
        }

        //dibujar el personaje
        if (jugador != null) {
            int px = jugador.getX() * tamaño + desplazamientoX;
            int py = jugador.getY() * tamaño + desplazamientoY;
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