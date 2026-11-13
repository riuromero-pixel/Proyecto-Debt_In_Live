package src.modelo.map;

import src.modelo.entities.Personaje;

public class GestorObjetosMapa {

    public int contarDineroRestante(Mapa mapa) {
        int contador = 0;
        for (int y = 0; y < mapa.getAlto(); y++) {
            for (int x = 0; x < mapa.getAncho(); x++) {
                ContenidoCelda contenido = mapa.getCeldas()[y][x].getContenido();
                if (contenido != null && contenido.esDinero()) {
                    contador++;
                }
                if (mapa.getCeldas()[y][x].getDineroDebajo() != null) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public boolean noQuedaDinero(Mapa mapa) {
        for (int y = 0; y < mapa.getAlto(); y++) {
            for (int x = 0; x < mapa.getAncho(); x++) {
                ContenidoCelda contenido = mapa.getCeldas()[y][x].getContenido();
                if (contenido != null && contenido.esDinero()
                        || mapa.getCeldas()[y][x].getDineroDebajo() != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void recolectarDeCelda(Mapa mapa, Personaje jugador, int x, int y) {
        Celda celda = mapa.conseguirCelda(x, y);
        if (celda == null) return;

        if (celda.getContenido() != null) {
            if (celda.getContenido().recolectar(jugador)) {
                celda.setContenido(null);
            }
        }

        if (celda.getDineroDebajo() != null) {
            if (celda.getDineroDebajo().recolectar(jugador)) {
                celda.setDineroDebajo(null);
            }
        }

        if (celda.getBonusDebajo() != null) {
            if (celda.getBonusDebajo().recolectar(jugador)) {
                celda.setBonusDebajo(null);
            }
        }
    }
}