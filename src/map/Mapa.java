package src.map;

import java.util.Random;

public class Mapa {
    public Celda[][] celdas;
    public int ancho;
    public int alto;
    private Random random;
    
    public Mapa(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        this.celdas = new Celda[alto][ancho];
        this.random = new Random();
        
        // Crear todas las celdas vacías
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                celdas[y][x] = new Celda(x, y);
            }
        }
    }
    
    public void cargarNivel(int nivel) {
        // Primero limpiamos todo
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                celdas[y][x].contenido = null;
            }
        }
        
        // Poner bordes de pared
        for (int x = 0; x < ancho; x++) {
            celdas[0][x].contenido = new Pared();           // Arriba
            celdas[alto-1][x].contenido = new Pared();      // Abajo
        }
        for (int y = 0; y < alto; y++) {
            celdas[y][0].contenido = new Pared();           // Izquierda
            celdas[y][ancho-1].contenido = new Pared();     // Derecha
        }
        
        // Poner dinero aleatorio (dependiendo del nivel)
        int cantidadDinero = 5 + nivel * 3;
        for (int i = 0; i < cantidadDinero; i++) {
            int x, y;
            do {
                x = 1 + random.nextInt(ancho - 2);
                y = 1 + random.nextInt(alto - 2);
            } while (celdas[y][x].contenido != null);
            
            celdas[y][x].contenido = new Dinero(1 + random.nextInt(5));
        }
        
        // Poner paredes internas (más en niveles altos)
        int cantidadParedes = nivel * 3;
        for (int i = 0; i < cantidadParedes; i++) {
            int x, y;
            do {
                x = 1 + random.nextInt(ancho - 2);
                y = 1 + random.nextInt(alto - 2);
            } while (celdas[y][x].contenido != null);
            
            celdas[y][x].contenido = new Pared();
        }
    }
}