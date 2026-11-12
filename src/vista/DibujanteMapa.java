package src.vista;

import java.awt.*;
import src.modelo.map.*;

public class DibujanteMapa {

    public void dibujar(Graphics2D g2d, Mapa mapa, double camX, double camY,
                        int tamaño, int desplazamientoX, int desplazamientoY,
                        CargadorImagenesNivel imagenes, int nivelActual) {

        int inicioX = (int) Math.floor(camX);
        int inicioY = (int) Math.floor(camY);

        for (int y = inicioY; y <= inicioY + CamaraJuego.VIEW_ROWS; y++) {
            for (int x = inicioX; x <= inicioX + CamaraJuego.VIEW_COLS; x++) {

                if (!mapa.estaDentro(x, y)) continue;

                int px = (int) ((x - camX) * tamaño) + desplazamientoX;
                int py = (int) ((y - camY) * tamaño) + desplazamientoY;

                g2d.fillRect(px, py, tamaño, tamaño);

                if (imagenes.getImagenPISO() != null) {
                    g2d.drawImage(imagenes.getImagenPISO(), px, py, tamaño, tamaño, null);
                } else {
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(px, py, tamaño, tamaño);
                }

                Object obj = mapa.getContenido(x, y);
                if (obj != null) {

                    if (obj instanceof Pared) {
                        Pared pared = (Pared) obj;
                        Image imgDibujar = imagenes.getImagenPARED();

                        if (nivelActual == 1) {
                            switch (pared.getVariante()) {
                                case 1: if (imagenes.getImagenSilla1() != null) imgDibujar = imagenes.getImagenSilla1(); break;
                                case 2: if (imagenes.getImagenSilla2() != null) imgDibujar = imagenes.getImagenSilla2(); break;
                                case 3: if (imagenes.getImagenSilla3() != null) imgDibujar = imagenes.getImagenSilla3(); break;
                                case 4: if (imagenes.getImagenSilla4() != null) imgDibujar = imagenes.getImagenSilla4(); break;
                            }
                        }

                        if (imgDibujar != null) {
                            g2d.drawImage(imgDibujar, px, py, tamaño, tamaño, null);
                        } else {
                            g2d.setColor(Color.BLACK);
                            g2d.fillRect(px, py, tamaño, tamaño);
                        }

                    } else if (obj instanceof Dinero) {
                        if (imagenes.getImagenDinero() != null) {
                            g2d.drawImage(imagenes.getImagenDinero(), px, py, tamaño, tamaño, null);
                        } else {
                            g2d.setColor(Color.YELLOW);
                            g2d.fillOval(px + tamaño / 4, py + tamaño / 4, tamaño / 2, tamaño / 2);
                        }

                    } else if (obj instanceof Bonus) {
                        Bonus unBonus = (Bonus) obj;
                        g2d.drawImage(unBonus.getImagenBonus(), px + 5, py + 5, tamaño - 10, tamaño - 10, null);
                        g2d.setColor(Color.BLACK);
                        g2d.drawString("", px + 10, py + 20);
                    }

                    g2d.setColor(Color.GRAY);
                    g2d.drawRect(px, py, tamaño, tamaño);
                }
            }
        }
    }
}