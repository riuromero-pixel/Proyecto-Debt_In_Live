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
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public ContenidoCelda getContenido() { return contenido; }
    void setContenido(ContenidoCelda contenido) { this.contenido = contenido; }

    public ContenidoCelda getContenidoDebajo() { return contenidoDebajo; }
    void setContenidoDebajo(ContenidoCelda contenidoDebajo) { this.contenidoDebajo = contenidoDebajo; }

    public void recolectar(Personaje jugador) {
        if (contenido != null && contenido.interactuar(jugador)) {
            contenido = null;
        }
        if (contenidoDebajo != null && contenidoDebajo.interactuar(jugador)) {
            contenidoDebajo = null;
        }
    }

}