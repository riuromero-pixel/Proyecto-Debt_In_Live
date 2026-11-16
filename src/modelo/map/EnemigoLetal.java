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
    @Override 
    public void actualizarPersecucion(Personaje jugador, Mapa mapa) { // Persecucion con un 20% de probabilidad de teletransportarse a una celda cerca del jugador
        if (Math.random() < 0.2){
            int nuevaX = jugador.getX() + (Math.random() > 0.5 ? 3 : -3);
            int nuevaY = jugador.getY() + (Math.random() > 0.5 ? 3 : -3);

            Celda celda = mapa.conseguirCelda(nuevaX, nuevaY);
            if (celda != null && celda.contenido == null) {
                mapa.conseguirCelda(this.getX(), this.getY()).contenido = null;
                celda.contenido = this;
                this.setX(nuevaX);
                this.setY(nuevaY);
                return;

            }
        }
        super.actualizarPersecucion(jugador, mapa);
    }
}
