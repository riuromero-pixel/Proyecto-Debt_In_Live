package src.modelo.map;

import src.modelo.movimientos.MovimientoPerseguidor;
import src.modelo.entities.Personaje;

public class EnemigoLetal extends Enemigo{

    public EnemigoLetal (int x, int y, int danio) { //Enemigo que elimina al jugador con solo tocarlo
    super(x ,y ,danio, new MovimientoPerseguidor(), 3);
    }
    @Override 
    public void hacerDanio(Personaje jugador) {
        jugador.danioRecibido(jugador.getDinero() + 1);
    }
}
