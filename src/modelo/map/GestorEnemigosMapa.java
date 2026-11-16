package src.modelo.map;

import src.modelo.entities.Personaje;

public class GestorEnemigosMapa {

    public void moverEnemigos(Mapa mapa, Personaje jugador) {
        for (Enemigo enemigo : mapa.getEnemigos()) {
            enemigo.actualizarMovimiento(mapa, jugador);
        }
    }

    public boolean verificarColisionConEnemigo(Mapa mapa, Personaje jugador) {
        for (Enemigo enemigo : mapa.getEnemigos()) {
            if (enemigo.getX() == jugador.getX() && enemigo.getY() == jugador.getY()) {
                enemigo.aplicarDanio(jugador);
                return true;
            }
        }
        return false;
    }
}