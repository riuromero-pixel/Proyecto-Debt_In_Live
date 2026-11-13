package src.modelo.map;

import src.modelo.entities.Personaje;

public class Celda {
    private int x;
    private int y;
    private ContenidoCelda contenido;
    private ContenidoCelda contenidoDebajo; //para guardar contenido que queda debajo de otros objetos
    
    public Celda(int x, int y) {
        this.x = x;
        this.y = y;
        this.contenido = null;
        this.contenidoDebajo = null;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public ContenidoCelda getContenido() { return contenido; }
    public void setContenido(ContenidoCelda contenido) { this.contenido = contenido; }

    public ContenidoCelda getContenidoDebajo() { return contenidoDebajo; }
    public void setContenidoDebajo(ContenidoCelda contenidoDebajo) { this.contenidoDebajo = contenidoDebajo; }

    public void recolectar(Personaje jugador) {
        if (contenido != null && contenido.recolectar(jugador)) {
            contenido = null;
        }
        if (contenidoDebajo != null && contenidoDebajo.recolectar(jugador)) {
            contenidoDebajo = null;
        }
    }

    public int contarComoDinero() {
        int total = 0;
        if (contenido != null) total += contenido.contarComoDinero();
        if (contenidoDebajo != null) total += contenidoDebajo.contarComoDinero();
        return total;
    }
}