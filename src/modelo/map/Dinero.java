package src.modelo.map;

import src.modelo.entities.Personaje;

public class Dinero extends ObjetoEntorno {
    private int cantidad;
    private boolean recolectado = false;
    
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
    this.recolectado = true;
    return true;
}

    public boolean estaRecolectado() {
        return recolectado;
    }
}