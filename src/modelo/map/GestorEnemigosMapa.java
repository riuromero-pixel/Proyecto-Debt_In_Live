package src.modelo.map;

import java.util.ArrayList;
import java.util.List;
import src.modelo.entities.Personaje;

public class GestorEnemigosMapa {

    public void moverEnemigos(Mapa mapa, Personaje jugador) {
        List<ContenidoCelda> movibles = new ArrayList<>();

        for (int y = 0; y < mapa.getAlto(); y++) {
            for (int x = 0; x < mapa.getAncho(); x++) {
                ContenidoCelda contenido = mapa.getCeldas()[y][x].getContenido();
                if (contenido != null && contenido.puedeMoverse()) {
                    movibles.add(contenido);
                }
            }
        }

        for (ContenidoCelda contenido : movibles) {
            contenido.actualizarMovimiento(mapa, jugador);
        }
    }

    public boolean verificarColisionConEnemigo(Mapa mapa, Personaje jugador) {
        Celda celdaJugador = mapa.conseguirCelda(jugador.getX(), jugador.getY());
        if (celdaJugador == null || celdaJugador.getContenido() == null) {
            return false;
        }
        return celdaJugador.getContenido().aplicarDanio(jugador);
    }
}