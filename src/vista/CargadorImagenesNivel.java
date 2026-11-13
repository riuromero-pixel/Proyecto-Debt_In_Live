package src.vista;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class CargadorImagenesNivel {

    private Image imagenPARED;
    private Image imagenPISO;
    private Image imagenSilla1, imagenSilla2, imagenSilla3, imagenSilla4;
    private Image imagenDinero;
    private Image imagenBonus;
    private Image[] spritesEnemigos = new Image[5];

    public void cargar(int nivelActual) {
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

                    spritesEnemigos[0] = ImageIO.read(new File("images/sprites/estudiante/enemigos/alfajor.png"));
                    spritesEnemigos[1] = ImageIO.read(new File("images/sprites/estudiante/enemigos/futbol.png"));
                    spritesEnemigos[2] = ImageIO.read(new File("images/sprites/estudiante/enemigos/play5.png"));
                    spritesEnemigos[3] = ImageIO.read(new File("images/sprites/estudiante/enemigos/steam.png"));

                    imagenBonus = ImageIO.read(new File("images/bonus_estudiante.png"));
                } catch (IOException e) {
                    System.out.println("Error al cargar los sprites del nivel 1");
                }
                break;

            case 2:
                paredtemp = "images/sprites/adulto/paredes/bloqueladrillo.PNG";
                pisotemp = "images/baldosatierra.PNG";
                try {
                    spritesEnemigos[0] = ImageIO.read(new File("images/sprites/adulto/enemigos/agua.png"));
                    spritesEnemigos[1] = ImageIO.read(new File("images/sprites/adulto/enemigos/alquiler.png"));
                    spritesEnemigos[2] = ImageIO.read(new File("images/sprites/adulto/enemigos/asado.png"));
                    spritesEnemigos[3] = ImageIO.read(new File("images/sprites/adulto/enemigos/bebe.png"));
                    spritesEnemigos[4] = ImageIO.read(new File("images/sprites/adulto/enemigos/cerveza.png"));

                    imagenBonus = ImageIO.read(new File("images/bonus_adulto.png"));
                } catch (IOException e) {
                    System.out.println("Error al cargar los sprites del nivel 2");
                }
                break;

            case 3:
                paredtemp = "images/arbusto.PNG";
                pisotemp = "images/baldosapasto.PNG";
                try {
                    spritesEnemigos[0] = ImageIO.read(new File("images/sprites/jubilado/enemigos/casino.png"));
                    spritesEnemigos[1] = ImageIO.read(new File("images/sprites/jubilado/enemigos/compras.png"));
                    spritesEnemigos[2] = ImageIO.read(new File("images/sprites/jubilado/enemigos/luz.png"));
                    spritesEnemigos[3] = ImageIO.read(new File("images/sprites/jubilado/enemigos/pami.png"));

                    imagenBonus = ImageIO.read(new File("images/bonus_anciano.png"));
                } catch (IOException e) {
                    System.out.println("Error al cargar los sprites del nivel 3");
                }
                break;

            default:
                imagenPARED = null;
                imagenPISO = null;
                imagenBonus = null;
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

    public Image getImagenPARED() { return imagenPARED; }
    public Image getImagenPISO() { return imagenPISO; }
    public Image getImagenSilla1() { return imagenSilla1; }
    public Image getImagenSilla2() { return imagenSilla2; }
    public Image getImagenSilla3() { return imagenSilla3; }
    public Image getImagenSilla4() { return imagenSilla4; }
    public Image getImagenDinero() { return imagenDinero; }
    public Image getImagenBonus() { return imagenBonus; }
    public Image[] getSpritesEnemigos() { return spritesEnemigos; }
}