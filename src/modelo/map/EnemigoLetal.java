package src.modelo.map;

import src.modelo.movimientos.MovimientoPerseguidor;

public class EnemigoLetal extends Enemigo{

    public EnemigoLetal (int x, int y, int danio) { //Enemigo que elimina al jugador con solo tocarlo
    super(x ,y ,danio, new MovimientoPerseguidor());
    }
}
