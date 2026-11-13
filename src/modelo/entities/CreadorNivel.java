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

    public abstract int getNivel();

    protected abstract Personaje crearPersonaje(int x, int y, Mapa mapa);

    protected abstract void cargarPlantilla(Mapa mapa);
}