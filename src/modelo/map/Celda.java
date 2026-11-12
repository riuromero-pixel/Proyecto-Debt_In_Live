package src.modelo.map;

public class Celda {
    private int x;
    private int y;
    private Object contenido;
    private Dinero dineroDebajo; //para guardar dinero que queda debajo de otros objetos
    private Bonus bonusDebajo; //para guardar bonus que queda debajo de otros objetos
    
    public Celda(int x, int y) {
        this.x = x;
        this.y = y;
        this.contenido = null;
        this.dineroDebajo = null;
        this.bonusDebajo = null;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public Object getContenido() { return contenido; }
    public void setContenido(Object contenido) { this.contenido = contenido; }

    public Dinero getDineroDebajo() { return dineroDebajo; }
    public void setDineroDebajo(Dinero d) { this.dineroDebajo = d; }

    public Bonus getBonusDebajo() { return bonusDebajo; }
    public void setBonusDebajo(Bonus b) { this.bonusDebajo = b; }

}