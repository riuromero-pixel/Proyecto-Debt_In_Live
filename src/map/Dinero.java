package src.map;

public class Dinero extends ObjetoEntorno {
    public int cantidad;
    
    public Dinero(int cantidad) {
        super(true); // deja pasar
        this.cantidad = cantidad;
    }
}