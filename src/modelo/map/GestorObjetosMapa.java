package src.modelo.map;

import src.modelo.entities.Personaje;

public class GestorObjetosMapa {

    public int contarDineroRestante(Mapa mapa) {
        int contador = 0;
        for (int y = 0; y < mapa.getAlto(); y++) {
            for (int x = 0; x < mapa.getAncho(); x++) {
                contador += mapa.getCeldas()[y][x].contarComoDinero();
            }
        }
        return contador;
    }

    public boolean noQuedaDinero(Mapa mapa) {
        return contarDineroRestante(mapa) == 0;
    }

    public void recolectarDeCelda(Mapa mapa, Personaje jugador, int x, int y) {
        Celda celda = mapa.conseguirCelda(x, y);
        if (celda == null) return;
        celda.recolectar(jugador);
    }
}