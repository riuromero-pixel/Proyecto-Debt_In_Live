package src.modelo.map;

import java.util.ArrayList;
import java.util.List;
import src.modelo.entities.Personaje;

public class GestorEnemigosMapa {

    public void moverEnemigos(Mapa mapa, Personaje jugador) {
        List<Enemigo> enemigos = new ArrayList<>();

        for (int y = 0; y < mapa.getAlto(); y++) {
            for (int x = 0; x < mapa.getAncho(); x++) {
                if (mapa.getCeldas()[y][x].getContenido() instanceof Enemigo) {
                    enemigos.add((Enemigo) mapa.getCeldas()[y][x].getContenido());
                }
            }
        }

        for (Enemigo enemigo : enemigos) {
            enemigo.actualizarPersecucion(jugador, mapa);
        }
    }

    public void actualizarPosicionesVisualesEnemigos(Mapa mapa) {
        for (int y = 0; y < mapa.getAlto(); y++) {
            for (int x = 0; x < mapa.getAncho(); x++) {
                if (mapa.getCeldas()[y][x].getContenido() instanceof Enemigo) {
                    ((Enemigo) mapa.getCeldas()[y][x].getContenido()).actualizarPosicionVisual();
                }
            }
        }
    }

    public boolean verificarColisionConEnemigo(Mapa mapa, Personaje jugador) {
        Celda celdaJugador = mapa.conseguirCelda(jugador.getX(), jugador.getY());
        if (celdaJugador != null && celdaJugador.getContenido() instanceof Enemigo) {
            Enemigo enemigo = (Enemigo) celdaJugador.getContenido();
            jugador.danioRecibido(enemigo.getDanio());
            return true;
        }
        return false;
    }
}