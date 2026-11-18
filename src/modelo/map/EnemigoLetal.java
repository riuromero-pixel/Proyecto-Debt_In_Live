package src.modelo.map;

import src.modelo.movimientos.EstrategiaMovimiento;
import src.modelo.entities.Personaje;

public class EnemigoLetal extends Enemigo{

    public EnemigoLetal(int x, int y, int danio, EstrategiaMovimiento estrategia) {
        super(x, y, danio, estrategia, 3);
    }
    @Override 
    public void hacerDanio(Personaje jugador) {
        jugador.danioRecibido(jugador.getDinero() + 1);
    }
    @Override 
    public void actualizarPersecucion(Personaje jugador, Mapa mapa) { // Persecucion con un 10% de probabilidad de teletransportarse a una celda cerca del jugador
        if (Math.random() < 0.1){
            int nuevaX = jugador.getX() + (Math.random() > 0.5 ? 3 : -3);
            int nuevaY = jugador.getY() + (Math.random() > 0.5 ? 3 : -3);

            Celda celda = mapa.conseguirCelda(nuevaX, nuevaY);
            if (celda != null && celda.getContenido() == null) {
                mapa.conseguirCelda(this.getX(), this.getY()).setContenido(null);
                celda.setContenido(this);
                this.setX(nuevaX);
                this.setY(nuevaY);
                return;
            }
        }
        super.actualizarPersecucion(jugador, mapa);
    }
    public TipoVisual getTipoVisual() {
        return TipoVisual.ENEMIGOLETAL;
    }
}
