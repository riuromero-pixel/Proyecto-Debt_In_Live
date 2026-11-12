package src.vista;

import src.modelo.entities.Personaje;
import src.modelo.map.Mapa;

public class CamaraJuego {

    public static final int VIEW_COLS = 15;
    public static final int VIEW_ROWS = 10;

    private double camXSuave = -1;
    private double camYSuave = -1;
    private static final double SUAVIDAD_CAMARA = 0.15;

    public void actualizar(Personaje jugador, Mapa mapa) {
        if (jugador == null || mapa == null) return;

        double camXObjetivo = jugador.getPosXVisual() - VIEW_COLS / 2.0;
        double camYObjetivo = jugador.getPosYVisual() - VIEW_ROWS / 2.0;

        camXObjetivo = Math.max(0, Math.min(camXObjetivo, mapa.getAncho() - VIEW_COLS));
        camYObjetivo = Math.max(0, Math.min(camYObjetivo, mapa.getAlto() - VIEW_ROWS));

        if (camXSuave < 0 || camYSuave < 0) {
            camXSuave = camXObjetivo;
            camYSuave = camYObjetivo;
            return;
        }

        camXSuave += (camXObjetivo - camXSuave) * SUAVIDAD_CAMARA;
        camYSuave += (camYObjetivo - camYSuave) * SUAVIDAD_CAMARA;
    }

    public void reiniciar() {
        camXSuave = -1;
        camYSuave = -1;
    }

    public double getCamX(Personaje jugador, Mapa mapa) {
        if (camXSuave >= 0) return camXSuave;
        double camXCalc = jugador != null ? jugador.getPosXVisual() - VIEW_COLS / 2.0 : 0;
        return Math.max(0, Math.min(camXCalc, mapa.getAncho() - VIEW_COLS));
    }

    public double getCamY(Personaje jugador, Mapa mapa) {
        if (camYSuave >= 0) return camYSuave;
        double camYCalc = jugador != null ? jugador.getPosYVisual() - VIEW_ROWS / 2.0 : 0;
        return Math.max(0, Math.min(camYCalc, mapa.getAlto() - VIEW_ROWS));
    }
}