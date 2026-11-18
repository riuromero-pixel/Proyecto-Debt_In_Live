package src.modelo.map;

import src.modelo.entities.Personaje;
import src.modelo.movimientos.EstrategiaMovimiento;
import java.util.Random;

public class Enemigo implements ContenidoCelda {
    private int x;
    private int y;
    private int danio;

    private EstrategiaMovimiento estrategia;

    private int variante;

    public Enemigo(int x, int y, int danio, EstrategiaMovimiento estrategia, int nivel) {
        this.x = x;
        this.y = y;
        this.danio = danio;
        this.estrategia = estrategia;

        // Generamos la variante aleatoria según el nivel
        Random rand = new Random();
        if (nivel == 1) {
            this.variante = 1 + rand.nextInt(4); // Sortea del 1 al 4
        } else if (nivel == 2) {
            this.variante = 1 + rand.nextInt(5); // Sortea del 1 al 5
        } else if (nivel == 3) {
            this.variante = 1 + rand.nextInt(4); // Sortea del 1 al 4
        } else {
            this.variante = 1; 
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public int getVariante() {
        return this.variante;
    }

    @Override
    public boolean bloqueaPaso() {
        return true;
    }
    
    @Override 
    public boolean interactuar(Personaje p) {
        aplicarDanio(p);
        return false;
    }

    public void aplicarDanio(Personaje p) {
        p.danioRecibido(danio);
    }

    public void actualizarMovimiento(Mapa mapa, Personaje jugador) {
        if (estrategia != null) {
            estrategia.mover(this, jugador, mapa);
        }
    }

    public void moverEnemigo(int mejorX, int mejorY, Mapa mapa) {
        Celda celdaActual = mapa.conseguirCelda(this.x, this.y);
        Celda celdaNueva = mapa.conseguirCelda(mejorX, mejorY);
        if (celdaActual == null || celdaNueva == null) return;

        if (celdaActual.getContenidoDebajo() != null) {
            celdaActual.setContenido(celdaActual.getContenidoDebajo());
            celdaActual.setContenidoDebajo(null);
        } else {
            celdaActual.setContenido(null);
        }

        if (celdaNueva.getContenido() != null && !celdaNueva.getContenido().bloqueaPaso()) {
            celdaNueva.setContenidoDebajo(celdaNueva.getContenido());
        }

        celdaNueva.setContenido(this);
        this.x = mejorX;
        this.y = mejorY;
    }
}