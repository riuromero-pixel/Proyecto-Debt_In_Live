package src.ui;

import src.map.Mapa;
import src.map.Pared;
import src.map.Dinero;
import src.map.Bonus;
import javax.swing.*;
import java.awt.*;

public class VentanaJuego extends JFrame {
    private Mapa mapa;
    private int tamaño = 30;
    
    public VentanaJuego() {
        mapa = new Mapa(15, 10); // Mapa de 15x10
        mapa.cargarNivel(1);
        
        setTitle("Juego");
        setSize(15 * tamaño + 15, 10 * tamaño + 40);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        
        // Recorrer todo el mapa
        for (int y = 0; y < mapa.alto; y++) {
            for (int x = 0; x < mapa.ancho; x++) {
                int px = x * tamaño;
                int py = y * tamaño;
                
                // Fondo blanco
                g.setColor(Color.WHITE);
                g.fillRect(px, py, tamaño, tamaño);
                
                // Dibujar contenido
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
                    }
                }
                
                // Borde de la celda
                g.setColor(Color.GRAY);
                g.drawRect(px, py, tamaño, tamaño);
            }
        }
    }
    
}