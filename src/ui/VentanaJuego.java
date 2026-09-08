package src.ui;

import src.entities.Personaje;
import src.entities.Adulto;
import src.entities.Estudiante;
import src.map.Mapa;
import src.map.Pared;
import src.map.Dinero;
import src.map.Bonus;
import src.map.Villano;
import javax.swing.*;
import java.awt.*;   //swing y awt nos sirven para el diseño, unicamente como boceto y ver como va ser el juego.
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaJuego extends JFrame{
    private Mapa mapa;
    private int tamaño = 30; //tamaño indica cuantos pixeles ocupa cada celda, en este caso por ahora 30x30
    private Adulto jugador; // Variable para almacenar el jugador
    private Timer timerMovimiento;
    private Timer timerJuego;
    public VentanaJuego() {
        mapa = new Mapa(15, 10);
        mapa.cargarNivel(1);
        
jugador = new Adulto(5, 5, mapa); //creamos un objeto adulto que es el jugador y lo ubicamos en la celda 1,1 del mapa
mapa.setPersonaje(jugador, 5, 5); //colocamos al jugador en el mapa en la celda 2,2


        setTitle("Juego"); //es el titulo que aparece en la ventanita
        setSize(15 * tamaño + 25, 10 * tamaño + 45); //establece el tamaño de la ventana adaptada a el tamaño del mapa
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //indica que al cerrar la ventana se deja de ejecutar el juego
        setLocationRelativeTo(null); //coloca la ventana en medio de la pantalla
        setResizable(false);

    
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);  // Evita que Tab mueva el foco
        
        addKeyListener(new TecladoListener());
        setVisible(true);
}

    public void paint(Graphics g) { //funcion que se encarga de dibujar todo lo que tenemos por ahora (dinero, paredes, celdas, etc)
    super.paint(g);             //Graphics g es una herramienta que java nos da para dibujar 
     Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    // desplazamos todo el dibujo 35 pixeles hacia abajo y 15 hacia la derecha
    // asi la fila de arriba y la columna de la izquierda no quedan escondidas
    int desplazamientoX = 15;
    int desplazamientoY = 35;
    
    for (int y = 0; y < mapa.alto; y++) {
        for (int x = 0; x < mapa.ancho; x++) {      //recorre todas las celdas del mapa
            int px = x * tamaño + desplazamientoX;
            int py = y * tamaño + desplazamientoY;        //calcula donde se va dibujar cada celda en la pantalla
      

        setTitle("Juego");
        setSize(15 * tamaño + 45, 10 * tamaño + 65);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Temporizador para mover villanos en ms
        
        
        timerMovimiento = new Timer(10000, e -> {
            mapa.moverVillanos();
            //repaint();
        });
        
        timerMovimiento.start();
    

                g.setColor(Color.WHITE);
                g.fillRect(px, py, tamaño, tamaño);
                
                if (mapa.celdas[y][x].contenido != null) {
                    Object obj = mapa.celdas[y][x].contenido;

                    if (obj instanceof Pared) {
                        g.setColor(Color.BLACK);
                        g.fillRect(px, py, tamaño, tamaño);
                    } else if (obj instanceof Dinero) {
                        g.setColor(Color.YELLOW);
                        g.fillOval(px + 5, py + 5, tamaño - 10, tamaño - 10);
                        g.setColor(Color.BLACK);
                        g.drawString("$", px + 10, py + 20);
                    } else if (obj instanceof Villano) {
                        g.setColor(Color.RED);
                        g.fillRect(px + 4, py + 4, tamaño - 8, tamaño - 8);
                        g.setColor(Color.WHITE);
                        g.drawString("V", px + 11, py + 20);
                    }
                }

                g.setColor(Color.GRAY);
                g.drawRect(px, py, tamaño, tamaño);
            }
        }
    
      if (jugador != null) {
            int px = jugador.getX() * tamaño + desplazamientoX;  // ← Usamos jugador.x
            int py = jugador.getY() * tamaño + desplazamientoY;  // ← Usamos jugador.y
            dibujarPersonaje(g2d, px, py);
        }
    }

private void dibujarPersonaje(Graphics2D g2d, int px, int py) {
    int margen = tamaño / 10;           // 3px si tamaño=30
    int radio = tamaño / 2 - margen;    // 12px si tamaño=30
    
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
    int ojoTamaño = tamaño / 6;          // 5px si tamaño=30
    int ojoY = py + tamaño / 3;          // 10px desde arriba
    int ojoX1 = px + tamaño / 4;         // 7.5px desde la izquierda
    int ojoX2 = px + tamaño * 3 / 4 - ojoTamaño;  // 22.5px desde la izquierda
    
    g2d.setColor(Color.BLACK);
    g2d.fillOval(ojoX1, ojoY, ojoTamaño, ojoTamaño);
    g2d.fillOval(ojoX2, ojoY, ojoTamaño, ojoTamaño);
    
    // === REFLEJO EN OJOS ===
    int reflejo = ojoTamaño / 3;
    g2d.setColor(Color.WHITE);
    g2d.fillOval(ojoX1 + 2, ojoY + 1, reflejo, reflejo);
    g2d.fillOval(ojoX2 + 2, ojoY + 1, reflejo, reflejo);
    
    // === SONRISA ===
    int sonrisaX = px + tamaño / 5;       // 6px
    int sonrisaY = py + tamaño / 2 + 2;    // 17px desde arriba
    int sonrisaAncho = tamaño * 3 / 5;     // 18px
    int sonrisaAlto = tamaño / 4;          // 7.5px
    
    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke(2));
    g2d.drawArc(sonrisaX, sonrisaY, sonrisaAncho, sonrisaAlto, 0, -180);
    
    // === MEJILLAS ===
    int mejillaTamaño = tamaño / 6;
    g2d.setColor(new Color(255, 150, 150, 100));
    g2d.fillOval(px + margen + 2, py + tamaño / 2 + 2, mejillaTamaño, mejillaTamaño / 2);
    g2d.fillOval(px + tamaño - margen - mejillaTamaño - 2, py + tamaño / 2 + 2, mejillaTamaño, mejillaTamaño / 2);
}


 private class TecladoListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            char tecla = e.getKeyChar();
            boolean seMovio = false;
            switch (tecla) {
                case 'w': case 'W': 
                    jugador.moverse("W", mapa.getAncho(), mapa.getAlto());
                    repaint();
                    seMovio = true;
                    break;
                case 's': case 'S': 
                    jugador.moverse("S", mapa.getAncho(), mapa.getAlto());
                    repaint();
                    seMovio = true;
                    break;
                case 'a': case 'A': 
                    jugador.moverse("A", mapa.getAncho(), mapa.getAlto()); 
                    repaint();
                    seMovio = true;
                    break;
                case 'd': case 'D': 
                    jugador.moverse("D", mapa.getAncho(), mapa.getAlto()); 
                    repaint();
                    seMovio = true;
                    break;
                case 'r': case 'R': 
                    jugador.romperParedes();
                    repaint(); // Redibuja la ventana después de romper la pared 
                    break;
                case 'c': case 'C': 
                
                    jugador.crearParedes(mapa.conseguirCelda(tecla, tecla).x, mapa.conseguirCelda(tecla, tecla).y); 
                    break;
                        default: return;
            }
            // Mover villanos SOLO si el jugador se movió
            if (seMovio) {
                mapa.moverVillanos();
                repaint();
            }
    
            
        }
    }
    
}