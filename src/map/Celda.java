package src.map;

public class Celda {
    public int x;
    public int y;
    public ObjetoEntorno contenido;
    
    public Celda(int x, int y) {
        this.x = x;
        this.y = y;
        this.contenido = null;
    }
}