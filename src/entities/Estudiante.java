package src.entities;


import src.map.Bonus;
import src.map.Mapa;

public class Estudiante extends Personaje{
    public Estudiante(int x, int y, Mapa mapa) {
        super(x, y, 2, 2, 30, false, mapa);
        this.velocidad = 3; // velocidad inicial mas alta por ser un estudiante
        this.fuerza = 2;
    }

    public void activarBonus(Bonus bonus) {
    this.bonusActivado = true;
    this.activarBonus(bonus);
}
public void activarBonus() {
    if (this.bonusActivado) {
        this.velocidad += 4; 
        this.fuerza += 4;
    }
}
}
