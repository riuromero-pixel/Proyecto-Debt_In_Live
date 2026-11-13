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

    public int getCantidad() { return cantidad; }

    @Override
    public boolean recolectar(Personaje personaje) {
        personaje.recolectarDinero(this.cantidad);
        return true;
    }

    @Override
    public String getTipoVisual() {
        return "DINERO";
    }

    @Override
    public boolean esDinero() {
        return true;
    }
}