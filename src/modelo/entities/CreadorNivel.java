package src.modelo.entities;

import src.modelo.map.Mapa;

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

    public int getSpawnX() { return 5; }
    public int getSpawnY() { return 5; }

    public String[] getMensajeInicio() { return null; }

    public abstract String getMensajeDerrota();

    public String getMensajeVictoria() { return "¡Ganaste!"; }

    public abstract int getNivel();

    protected abstract Personaje crearPersonaje(int x, int y, Mapa mapa);

    protected abstract void cargarPlantilla(Mapa mapa);
}