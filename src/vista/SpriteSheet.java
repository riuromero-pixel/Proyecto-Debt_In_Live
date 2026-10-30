package src.vista;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteSheet {
    private BufferedImage imagen;
    private int anchoFrame;
    private int altoFrame;

    // guardamos las columnas y filas con las que se inicializó este spritesheet específico
    private int columnas;
    private int filas;

    // Para spritesheets con grilla (caminata: 11 columnas x 2 filas)
    public SpriteSheet(String ruta, int columnas, int filas) {
     
    this.columnas = columnas; 
    this.filas = filas;        
        try {
            imagen = ImageIO.read(new File(ruta));
            if (imagen != null) {
                this.anchoFrame = imagen.getWidth() / columnas;
                this.altoFrame = imagen.getHeight() / filas;
            }
        } catch (IOException e) {
            System.out.println("Error al cargar sprite: " + ruta);
            e.printStackTrace();
        }
    }

// getters para que GestorSprites o el Controlador sepan la estructura de este sprite
    public int getColumnas() { return columnas; }
    public int getFilas() { return filas; }
    public int getTotalFrames() { return columnas * filas; }

    // Para imágenes estáticas (1 sola imagen)
    public SpriteSheet(String ruta) {
        this(ruta, 1, 1);
    }

    public Image obtenerFrame(int fila, int columna) {
        if (imagen == null) return null;
        return imagen.getSubimage(
            columna * anchoFrame,
            fila * altoFrame,
            anchoFrame,
            altoFrame
        );
    }

    // Para imágenes estáticas: devuelve la imagen completa
    public Image obtenerImagenCompleta() {
        return imagen;
    }
}
