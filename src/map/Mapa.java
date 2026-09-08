package src.map;

import src.entities.Personaje;
import java.util.Random; // permite utilizar numeros randoms un paquete ya de java

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

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                celdas[y][x] = new Celda(x, y);
            }
        }
    }

    public void cargarNivel(int nivel) {
        // Limpiar mapa
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                celdas[y][x].contenido = null;
            }
        }

        // Paredes bordes
        for (int x = 0; x < ancho; x++) {
            celdas[0][x].contenido = new Pared();
            celdas[alto - 1][x].contenido = new Pared();
        }
        for (int y = 0; y < alto; y++) {
            celdas[y][0].contenido = new Pared();
            celdas[y][ancho - 1].contenido = new Pared();
        }

        // Generar dinero
        int cantidadDinero = 5 + nivel * 3;
        for (int i = 0; i < cantidadDinero; i++) {
            int x, y;
            do {
                x = 1 + random.nextInt(ancho - 2);
                y = 1 + random.nextInt(alto - 2);
            } while (celdas[y][x].contenido != null);

            celdas[y][x].contenido = new Dinero(1 + random.nextInt(5));
        }

        // Generar paredes internas
        int cantidadParedes = nivel * 3;
        for (int i = 0; i < cantidadParedes; i++) {
            int x, y;
            do {
                x = 1 + random.nextInt(ancho - 2);
                y = 1 + random.nextInt(alto - 2);
            } while (celdas[y][x].contenido != null);

            celdas[y][x].contenido = new Pared();
        }

        // Generar villanos
        int cantidadVillanos = 4 * nivel;
        for (int i = 0; i < cantidadVillanos; i++) {
            int x, y;
            do {
                x = 1 + random.nextInt(ancho - 2);
                y = 1 + random.nextInt(alto - 2);
            } while (celdas[y][x].contenido != null);

            celdas[y][x].contenido = new Villano(x, y, 10 + (nivel * 2));
        }
    }

    public void moverVillanos(){
        boolean[][] procesado = new boolean[alto][ancho];

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                if (!procesado[y][x] && celdas[y][x].contenido instanceof Villano) {
                    Villano v = (Villano) celdas[y][x].contenido;
                    int origX = v.x;
                    int origY = v.y;

                    v.mover(this);

                    procesado[v.y][v.x] = true;
                    procesado[origY][origX] = true;
                }
            }
        }
    }
       public Celda conseguirCelda(int x, int y) {
        if (x < 0 || x >= ancho || y < 0 || y >= alto) {
            return null;
        }
        return celdas[y][x];
    }
    public int getAncho() {
        return ancho;
    }
    public int getAlto() {
        return alto;
    }
    
    public void setPersonaje(Personaje personaje, int x, int y) {
        Celda celda = conseguirCelda(x, y);
        if (celda != null) {
            celda.contenido = personaje;
        }
    }
     public Personaje getPersonaje(int x, int y) {
    if (x < 0 || x >= ancho || y < 0 || y >= alto) {
        return null; // Fuera del mapa
    }
    Object contenido = celdas[y][x].contenido;
    if (contenido instanceof Personaje) {
        return (Personaje) contenido;
    }
    return null;
}

}