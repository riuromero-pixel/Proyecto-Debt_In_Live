package src.modelo.entities;


import src.modelo.map.Mapa;

public class Jubilado extends Personaje {

    public Jubilado(int x, int y, Mapa mapa) {
        super(x, y, 1, 100, false, mapa);
    }

    @Override
    protected int getBonusExtra() {
        return 3;
    }

    @Override
    public String getNombreTipo() {
        return "Jubilado";
    }

    @Override
    public int getTipoSprite() {
        return 2;
    }

    @Override
    public void usarHabilidadEspecial() {
        ponerParedes(1);
    }
}