package src.modelo.entities;


import src.modelo.map.Bonus;
import src.modelo.map.Mapa;
import src.vista.SpriteSheet;
import java.awt.Image;

public class Estudiante extends Personaje{

    private int velocidadOriginal;

// Spritesheets de CAMINATA (cada uno: 11 columnas x 2 filas = 22 frames)
    private SpriteSheet caminataFrente;   // Sur
    private SpriteSheet caminataEspalda;  // Norte
    private SpriteSheet caminataDerecha;  // Este
    private SpriteSheet caminataIzquierda;// Oeste

    // Imágenes ESTÁTICAS (idle, un solo frame)
    private SpriteSheet idleFrente;
    private SpriteSheet idleEspalda;
    private SpriteSheet idleDerecha;
    private SpriteSheet idleIzquierda;

    public Estudiante(int x, int y, Mapa mapa) {
        super(x, y, 2, 2, 100, false, mapa);
        this.velocidad = 3; // velocidad inicial mas alta por ser un estudiante
        this.fuerza = 2;

        // Cargamos las 4 hojas de caminata (11 columnas x 2 filas)
        caminataFrente    = new SpriteSheet("images/sprites/estudiante/abajoestudiante.png", 11, 2);
        caminataEspalda   = new SpriteSheet("images/sprites/estudiante/arribaestudiante.png", 11, 2);
        caminataDerecha   = new SpriteSheet("images/sprites/estudiante/derechaestudiante.png", 11, 2);
        caminataIzquierda = new SpriteSheet("images/sprites/estudiante/izquierdaestudiante.png", 11, 2);

        // Cargamos las 4 imágenes estáticas (un solo frame)
        idleFrente    = new SpriteSheet("images/sprites/estudiante/estaticoabajoestudiante.png");
        idleEspalda   = new SpriteSheet("images/sprites/estudiante/estaticoarribaestudiante.png");
        idleDerecha   = new SpriteSheet("images/sprites/estudiante/estaticoderechaestudiante.png");
        idleIzquierda = new SpriteSheet("images/sprites/estudiante/estaticoizquierdaestudiante.png");
    }
     public Image getSpriteActual() {
        boolean caminando = enMovimiento; // viene de Personaje

        if (caminando) {
            SpriteSheet hoja = obtenerHojaCaminata();
            if (hoja == null) return null;
            // frameAnimacion va de 0 a 10 (11 columnas). 
            // filaCaminata elige entre fila 0 o 1 (las dos filas del spritesheet).
            int fila = (frameAnimacion / 11) % 2; // cambia de fila cada 11 frames
            int columna = frameAnimacion % 11;
            return hoja.obtenerFrame(fila, columna);
        } else {
            SpriteSheet idle = obtenerIdle();
            return idle != null ? idle.obtenerImagenCompleta() : null;
        }
    }

    private SpriteSheet obtenerHojaCaminata() {
        switch (direccionVisual) {
            case "N": return caminataEspalda;
            case "E": return caminataDerecha;
            case "O": return caminataIzquierda;
            case "S": default: return caminataFrente;
        }
    }

    private SpriteSheet obtenerIdle() {
        switch (direccionVisual) {
            case "N": return idleEspalda;
            case "E": return idleDerecha;
            case "O": return idleIzquierda;
            case "S": default: return idleFrente;
        }
    }
    public void activarBonus(Bonus bonus) {
         if (!isBonusActivado()) {
        velocidadOriginal = velocidad; // Guardar la velocidad original
        }
        super.activarBonus(bonus);
        this.velocidad += bonus.bonusVelocidad + 3;
    }
        
    public void desactivarBonus() {
        super.desactivarBonus();
        this.velocidad = velocidadOriginal; 
    }
}