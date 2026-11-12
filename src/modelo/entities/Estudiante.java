package src.modelo.entities;

import src.modelo.map.Bonus;
import src.modelo.map.Mapa;

public class Estudiante extends Personaje{

    private int velocidadOriginal;

    public Estudiante(int x, int y, Mapa mapa) {
        super(x, y, 3, 100, false, mapa);
    }
    
    @Override
    public void activarBonus(Bonus bonus) {
         if (!isBonusActivado()) {
        velocidadOriginal = getVelocidad(); // Guardar la velocidad original
        }
        super.activarBonus(bonus);
        setVelocidad(getVelocidad() + bonus.getBonusVelocidad() + 3);
    }
      
    @Override
    public void desactivarBonus() {
        super.desactivarBonus();
        setVelocidad(velocidadOriginal);
    }
}