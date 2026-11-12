package src.vista;

import java.awt.*;
import src.modelo.entities.*;
import src.modelo.map.*;

public class DibujanteEntidades {

    public void dibujarEnemigos(Graphics2D g2d, Mapa mapa, double camX, double camY,
                                int tamaño, int desplazamientoX, int desplazamientoY,
                                CargadorImagenesNivel imagenes, Personaje jugador) {

        for (int y = 0; y < mapa.getAlto(); y++) {
            for (int x = 0; x < mapa.getAncho(); x++) {
                Object contenido = mapa.getContenido(x, y);
                if (contenido instanceof Enemigo) {
                    Enemigo en = (Enemigo) contenido;

                    if (en.getPosXVisual() + 1 < camX || en.getPosXVisual() > camX + CamaraJuego.VIEW_COLS) continue;
                    if (en.getPosYVisual() + 1 < camY || en.getPosYVisual() > camY + CamaraJuego.VIEW_ROWS) continue;

                    int ex = (int) ((en.getPosXVisual() - camX) * tamaño) + desplazamientoX;
                    int ey = (int) ((en.getPosYVisual() - camY) * tamaño) + desplazamientoY;

                    if (en instanceof EnemigoLetal) {
                        String dirMuerte = "abajo";
                        double difX = en.getX() - en.getPosXVisual();
                        double difY = en.getY() - en.getPosYVisual();

                        if (Math.abs(difX) > 0.01 || Math.abs(difY) > 0.01) {
                            if (Math.abs(difX) > Math.abs(difY)) {
                                dirMuerte = difX > 0 ? "derecha" : "izquierda";
                            } else {
                                dirMuerte = difY > 0 ? "abajo" : "arriba";
                            }
                        } else if (jugador != null) {
                            double distJugadorX = jugador.getX() - en.getX();
                            double distJugadorY = jugador.getY() - en.getY();
                            if (Math.abs(distJugadorX) > Math.abs(distJugadorY)) {
                                dirMuerte = distJugadorX > 0 ? "derecha" : "izquierda";
                            } else {
                                dirMuerte = distJugadorY > 0 ? "abajo" : "arriba";
                            }
                        }

                        Image spriteLetal = GestorSprites.getInstance().obtenerSpriteEnemigoLetal(dirMuerte);

                        if (spriteLetal != null) {
                            int anchoOriginal = spriteLetal.getWidth(null);
                            int altoOriginal = spriteLetal.getHeight(null);
                            double margen = 1.3;
                            double escala = (double) tamaño / Math.max(anchoOriginal, altoOriginal) * margen;
                            int anchoDibujo = (int) (anchoOriginal * escala);
                            int altoDibujo = (int) (altoOriginal * escala);
                            int dibujoX = ex + (tamaño - anchoDibujo) / 2;
                            int dibujoY = ey + (tamaño - altoDibujo);
                            g2d.drawImage(spriteLetal, dibujoX, dibujoY, anchoDibujo, altoDibujo, null);
                        } else {
                            g2d.setColor(Color.BLACK);
                            g2d.fillRect(ex + 4, ey + 4, tamaño - 8, tamaño - 8);
                            g2d.setColor(Color.RED);
                            g2d.drawString("V", ex + 11, ey + 20);
                        }

                    } else {
                        int var = en.getVariante();
                        Image imgEnemigo = (var >= 1 && var <= imagenes.getSpritesEnemigos().length)
                                ? imagenes.getSpritesEnemigos()[var - 1] : null;

                        if (imgEnemigo != null) {
                            g2d.drawImage(imgEnemigo, ex, ey, tamaño, tamaño, null);
                        } else {
                            g2d.setColor(Color.RED);
                            g2d.fillRect(ex + 4, ey + 4, tamaño - 8, tamaño - 8);
                            g2d.setColor(Color.WHITE);
                            g2d.drawString("V", ex + 11, ey + 20);
                        }
                    }
                }
            }
        }
    }

    public void dibujarJugador(Graphics2D g2d, Personaje jugador, double camX, double camY,
                               int tamaño, int desplazamientoX, int desplazamientoY) {

        if (jugador == null) return;

        int px = (int) ((jugador.getPosXVisual() - camX) * tamaño) + desplazamientoX;
        int py = (int) ((jugador.getPosYVisual() - camY) * tamaño) + desplazamientoY;

        Image sprite = GestorSprites.getInstance().obtenerSpritePersonaje(jugador);

        if (sprite != null) {
            int anchoOriginal = sprite.getWidth(null);
            int altoOriginal = sprite.getHeight(null);
            double margen = 1.3;
            double escala = (double) tamaño / Math.max(anchoOriginal, altoOriginal) * margen;
            int anchoDibujo = (int) (anchoOriginal * escala);
            int altoDibujo = (int) (altoOriginal * escala);
            int dibujoX = px + (tamaño - anchoDibujo) / 2;
            int dibujoY = py + (tamaño - altoDibujo);
            g2d.drawImage(sprite, dibujoX, dibujoY, anchoDibujo, altoDibujo, null);
        } else {
            int margen = tamaño / 10;
            int radio = tamaño / 2 - margen;
            g2d.setColor(new Color(0, 0, 0, 60));
            g2d.fillOval(px + margen + 2, py + margen + 4, radio * 2, radio * 2);
        }
    }
}