package src.modelo.map;

import src.modelo.entities.Personaje;

public class Dinero extends ObjetoEntorno {
    private int cantidad;
    
    public Dinero(int cantidad) {
        super(true);
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        this.cantidad = cantidad;
    }

    @Override 
    public boolean bloqueaPaso() { return false; }

    @Override
    public boolean interactuar(Personaje p) {
    p.recolectarDinero(cantidad);
    return true;
}

    public int contarComoDinero() {
        return 1;
    }
}