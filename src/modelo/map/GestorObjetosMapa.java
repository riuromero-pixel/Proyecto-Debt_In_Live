package src.modelo.map;

import src.modelo.entities.Personaje;

public class GestorObjetosMapa {

    public int contarDineroRestante(Mapa mapa) {
        int contador = 0;
        for (int y = 0; y < mapa.getAlto(); y++) {
            for (int x = 0; x < mapa.getAncho(); x++) {
                if (mapa.getCeldas()[y][x].getContenido() instanceof Dinero) {
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
                if (mapa.getCeldas()[y][x].getContenido() instanceof Dinero
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

        if (celda.getContenido() instanceof Dinero) {
            Dinero dinero = (Dinero) celda.getContenido();
            jugador.recolectarDinero(dinero.getCantidad());
            celda.setContenido(null);
        }

        if (celda.getDineroDebajo() != null) {
            Dinero dinero = celda.getDineroDebajo();
            jugador.recolectarDinero(dinero.getCantidad());
            celda.setDineroDebajo(null);
        }

        if (celda.getContenido() instanceof Bonus) {
            Bonus bonus = (Bonus) celda.getContenido();
            jugador.activarBonus(bonus);
            celda.setContenido(null);
        }

        if (celda.getBonusDebajo() != null) {
            Bonus bonus = celda.getBonusDebajo();
            jugador.activarBonus(bonus);
            celda.setBonusDebajo(null);
        }
    }
}