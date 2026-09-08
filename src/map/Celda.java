package src.map;

public class Celda {
    public int x;
    public int y;
    public Object contenido;
    public Dinero dineroDebajo; //para guardar dinero que queda debajo de otros objetos
    
    public Celda(int x, int y) {
        this.x = x;
        this.y = y;
        this.contenido = null;
        this.dineroDebajo = null;
    }
}