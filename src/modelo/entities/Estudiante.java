package src.modelo.entities;


import src.modelo.map.Bonus;
import src.modelo.map.Mapa;

public class Estudiante extends Personaje{

    private int velocidadOriginal;

    public Estudiante(int x, int y, Mapa mapa) {
        super(x, y, 2, 2, 30, false, mapa);
        this.velocidad = 3; // velocidad inicial mas alta por ser un estudiante
        this.fuerza = 2;
    }
    public void activarBonus(Bonus bonus) {
        super.activarBonus(bonus);
        velocidadOriginal = velocidad; // Guardar la velocidad original
        this.velocidad += bonus.bonusVelocidad + 3;
    }
    public void desactivarBonus() {
        super.desactivarBonus();
        this.velocidad = velocidadOriginal; 
    }
}