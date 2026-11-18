package src.modelo.entities;

import src.modelo.map.Mapa;
import src.modelo.map.Pared;

public abstract class CreadorNivel {

    public NivelCreado crearNivel(int x, int y) {
        Mapa mapa = new Mapa(40, 40);
        mapa.prepararMapa();
        cargarPlantilla(mapa);
        mapa.generarContenidoAleatorio(getNivel());
        mapa.generarDinero();

        Personaje jugador = crearPersonaje(x, y, mapa);
        mapa.setContenido(x, y, null);
        mapa.setPersonaje(jugador, x, y);

        return new NivelCreado(mapa, jugador, getNivel());
    }

    public int getSpawnX() { return 2; }
    public int getSpawnY() { return 2; }

    public String[] getMensajeInicio() { return null; }

    public abstract String getMensajeDerrota();

    public String getMensajeVictoria() { return "¡Muy Bien!"; }

    public abstract int getNivel();

    protected abstract Personaje crearPersonaje(int x, int y, Mapa mapa);

    protected abstract String[] getPlantilla();

    protected void cargarPlantilla(Mapa mapa) {
        String[] plantilla = getPlantilla();
        for (int y = 0; y < plantilla.length && y < mapa.getAlto(); y++) {
            for (int x = 0; x < plantilla[y].length() && x < mapa.getAncho(); x++) {
                char caracter = plantilla[y].charAt(x);
                if (caracter == '#') {
                    mapa.setContenido(x, y, new Pared(0));
                } else if (caracter == '+') {
                    mapa.setContenido(x, y, new Pared());
                }
            }
        }
    }
}