package src.modelo.entities;

import src.modelo.map.Mapa;

public class Adulto extends Personaje {

    public Adulto(int x, int y, Mapa mapa) {
        super(x, y, 2, 100, false, mapa);
    }

    @Override
    protected int getBonusExtra() {
        return 2;
    }

    @Override public int getTipoSprite() { return 1; }

    @Override
    public String getNombreTipo() {
        return "Adulto";
    }

    @Override
    public void usarHabilidadEspecial() {
        ponerParedes(Integer.MAX_VALUE);
    }
}