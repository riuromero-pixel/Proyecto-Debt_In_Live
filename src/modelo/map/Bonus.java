package src.modelo.map;

import src.modelo.entities.Personaje;

public class Bonus extends ObjetoEntorno {

    private int bonusVelocidad = 2;
    private long duracionMs = 5000;

    public Bonus(int bonusVelocidad, int duracionMs) {
        super(true);
        this.bonusVelocidad = bonusVelocidad;
        this.duracionMs = duracionMs;
    }

    public int getBonusVelocidad() { return bonusVelocidad; }
    public long getDuracionMs() { return duracionMs; }

    @Override 
    public boolean bloqueaPaso() { return false; }

    @Override 
    public boolean interactuar(Personaje p) {
        p.activarBonus(this);
        return true;
    }

}