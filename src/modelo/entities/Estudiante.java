package src.modelo.entities;

import src.modelo.map.Mapa;

public class Estudiante extends Personaje{

    public Estudiante(int x, int y, Mapa mapa) {
        super(x, y, 3, 100, false, mapa);
    }

    @Override
    protected int getBonusExtra() {
        return 3;
    }
}