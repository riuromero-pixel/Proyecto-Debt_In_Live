package src.modelo.map;

import src.modelo.entities.Personaje;
import src.modelo.movimientos.MovimientoPerseguidor;

public class EnemigoLetal extends Enemigo {

    public EnemigoLetal (int x, int y, int danio) { //Enemigo que elimina al jugador con solo tocarlo
    super(x ,y ,danio, new MovimientoPerseguidor(), 3);
    }

    @Override
    public boolean aplicarDanio(Personaje personaje) {
        personaje.morir();
        return true;
    }

    @Override
    public String getTipoVisual() {
        return "ENEMIGO_LETAL";
    }

    @Override
    public boolean esLetal() {
        return true;
    }
}