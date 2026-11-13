package src.modelo.map;

import java.util.ArrayList;
import java.util.List;
import src.modelo.entities.Personaje;

public class GestorEnemigosMapa {

    public void moverEnemigos(Mapa mapa, Personaje jugador) {
        List<Enemigo> enemigos = new ArrayList<>();

        for (int y = 0; y < mapa.getAlto(); y++) {
            for (int x = 0; x < mapa.getAncho(); x++) {
                ContenidoCelda contenido = mapa.getCeldas()[y][x].getContenido();
                if (contenido != null && contenido.esEnemigo()) {
                    enemigos.add(contenido.asEnemigo());
                }
            }
        }

        for (Enemigo enemigo : enemigos) {
            enemigo.actualizarPersecucion(jugador, mapa);
        }
    }

    public boolean verificarColisionConEnemigo(Mapa mapa, Personaje jugador) {
        Celda celdaJugador = mapa.conseguirCelda(jugador.getX(), jugador.getY());
        if (celdaJugador != null && celdaJugador.getContenido() != null && celdaJugador.getContenido().esEnemigo()) {
            Enemigo enemigo = celdaJugador.getContenido().asEnemigo();
            jugador.danioRecibido(enemigo.getDanio());
            return true;
        }
        return false;
    }
}