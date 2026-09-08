package src.ui;

import src.map.Mapa;
import src.map.Pared;
import src.map.Dinero;
import src.map.Bonus;
import src.map.Villano;
import javax.swing.*;
import java.awt.*;

public class VentanaJuego extends JFrame {
    private Mapa mapa;
    private int tamano = 30; // Nombre de variable sin 'ñ' para evitar errores de Encoding
    private Timer timerMovimiento;

    public VentanaJuego() {
        mapa = new Mapa(15, 10);
        mapa.cargarNivel(1);

        setTitle("Juego");
        setSize(15 * tamano + 45, 10 * tamano + 65);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Temporizador para mover villanos cada 500 ms
        timerMovimiento = new Timer(500, e -> {
            mapa.moverVillanos();
            repaint();
        });
        timerMovimiento.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int desplazamientoX = 15;
        int desplazamientoY = 35;

        for (int y = 0; y < mapa.alto; y++) {
            for (int x = 0; x < mapa.ancho; x++) {
                int px = x * tamano + desplazamientoX;
                int py = y * tamano + desplazamientoY;

                g.setColor(Color.WHITE);
                g.fillRect(px, py, tamano, tamano);

                if (mapa.celdas[y][x].contenido != null) {
                    Object obj = mapa.celdas[y][x].contenido;

                    if (obj instanceof Pared) {
                        g.setColor(Color.BLACK);
                        g.fillRect(px, py, tamano, tamano);
                    } else if (obj instanceof Dinero) {
                        g.setColor(Color.YELLOW);
                        g.fillOval(px + 5, py + 5, tamano - 10, tamano - 10);
                        g.setColor(Color.BLACK);
                        g.drawString("$", px + 10, py + 20);
                    } else if (obj instanceof Villano) {
                        g.setColor(Color.RED);
                        g.fillRect(px + 4, py + 4, tamano - 8, tamano - 8);
                        g.setColor(Color.WHITE);
                        g.drawString("V", px + 11, py + 20);
                    }
                }

                g.setColor(Color.GRAY);
                g.drawRect(px, py, tamano, tamano);
            }
        }
    }
}