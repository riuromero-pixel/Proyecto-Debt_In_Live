package src.modelo.entities;

import src.modelo.map.Bonus;
import src.modelo.map.Mapa;

public class Estudiante extends Personaje{



    public Estudiante(int x, int y, Mapa mapa) {
        super(x, y, 2, 100, false, mapa);
        this.velocidad = 3; // velocidad inicial mas alta por ser un estudiante
    }
    @Override 

    public void activarBonus(Bonus bonus) {
        super.activarBonus(bonus);
        this.velocidad += bonus.bonusVelocidad + 3;
    }
}