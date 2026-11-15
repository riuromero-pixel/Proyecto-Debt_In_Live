package src.modelo.map;

import src.modelo.entities.Personaje;
import src.modelo.movimientos.EstrategiaMovimiento;

public class EnemigoLetal extends Enemigo {

    public EnemigoLetal(int x, int y, EstrategiaMovimiento estrategia) {
        super(x, y, 0, estrategia, 3);
    }
    @Override //instakill en vez de asignarle un daño altisimo
    public void hacerDanio(Personaje jugador) {
        jugador.danioRecibido(jugador.getDinero() + 1);
    }
}
