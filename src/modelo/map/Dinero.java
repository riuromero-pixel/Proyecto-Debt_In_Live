package src.modelo.map;

public class Dinero extends ObjetoEntorno {
    private int cantidad;
    
    public Dinero(int cantidad) {
        super(true);
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        this.cantidad = cantidad;
    }

    public int getCantidad() { return cantidad; }

}