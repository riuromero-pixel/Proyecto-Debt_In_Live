package src.modelo.entities;

import src.modelo.map.Celda;
import src.modelo.map.Mapa;
import src.modelo.map.Pared;
import src.modelo.map.Bonus;

public class Adulto extends Personaje implements CreadorParedes{ // Clase que representa a un personaje adulto en el juego, con habilidades específicas para crear paredes y activar/desactivar bonus

    private int velocidadOriginal;

    public Adulto(int x, int y, Mapa mapa) {
        super(x, y, 2, 100, false, mapa);
    }
    @Override
    public void activarBonus(Bonus bonus) {
         if (!isBonusActivado()) {
        velocidadOriginal = getVelocidad(); // Guardar la velocidad original
        }
        super.activarBonus(bonus);
        setVelocidad(getVelocidad() + bonus.getBonusVelocidad() + 2);
    }
      
    @Override
    public void desactivarBonus() {
        super.desactivarBonus();
        setVelocidad(velocidadOriginal);
    }

     @Override
public void crearParedes() {
    String dirStr = getDireccionActual();
    if (dirStr == null) return;

    char dir = dirStr.charAt(0);
    int dx = 0, dy = 0;

    switch (dir) {
        case 'W': dy = -1; break;
        case 'S': dy = 1;  break;
        case 'A': dx = -1; break;
        case 'D': dx = 1;  break;
        default: return;
    }

    int nx = getX() + dx;
    int ny = getY() + dy;

    while (nx >= 0 && nx < getMapa().getAncho()
        && ny >= 0 && ny < getMapa().getAlto()) {
        Celda celda = getMapa().conseguirCelda(nx, ny);
        if (celda != null && celda.getContenido() == null) {     
            celda.setContenido(new Pared());                
        }
        nx += dx;
        ny += dy;
    }
}
}