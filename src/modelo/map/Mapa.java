package src.modelo.map;

import src.modelo.entities.Personaje;

public class Mapa {

    private Celda[][] celdas;
    private final int ancho;
    private final int alto;

    private final GeneradorNivelMapa generador = new GeneradorNivelMapa();
    private final GestorEnemigosMapa gestorEnemigos = new GestorEnemigosMapa();
    private final GestorObjetosMapa gestorObjetos = new GestorObjetosMapa();

    public Mapa(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        this.celdas = new Celda[alto][ancho];

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                celdas[y][x] = new Celda(x, y);
            }
        }
    }

    public void cargarNivel(int nivel) { generador.cargarNivel(this, nivel); }
    public void cargarMapaPlantilla(int nivel) { generador.cargarMapaPlantilla(this, nivel); }
    public void generarDinero() { generador.generarDinero(this); }
    public void generarBonus(int cantidad, int nivel) { generador.generarBonus(this, cantidad, nivel); }

    public void moverEnemigos(Personaje jugador) { gestorEnemigos.moverEnemigos(this, jugador); }
    public boolean verificarColisionConEnemigo(Personaje jugador) { return gestorEnemigos.verificarColisionConEnemigo(this, jugador); }

    public int contarDineroRestante() { return gestorObjetos.contarDineroRestante(this); }
    public boolean noQuedaDinero() { return gestorObjetos.noQuedaDinero(this); }
    public void recolectarDeCelda(Personaje jugador, int x, int y) { gestorObjetos.recolectarDeCelda(this, jugador, x, y); }

    public Celda conseguirCelda(int x, int y) {
        if (x < 0 || x >= ancho || y < 0 || y >= alto) return null;
        return celdas[y][x];
    }

    public boolean estaDentro(int x, int y) {
        return x >= 0 && x < ancho && y >= 0 && y < alto;
    }

    public ContenidoCelda getContenido(int x, int y) {
        if (!estaDentro(x, y)) return null;
        return celdas[y][x].getContenido();
    }

    public void setContenido(int x, int y, ContenidoCelda contenido) {
        if (!estaDentro(x, y)) return;
        celdas[y][x].setContenido(contenido);
    }

    public boolean estaLibre(int x, int y) {
        if (!estaDentro(x, y)) return false;
        return celdas[y][x].getContenido() == null;
    }

    public boolean esPared(int x, int y) {
        if (!estaDentro(x, y)) return false;
        ContenidoCelda contenido = celdas[y][x].getContenido();
        return contenido != null && contenido.esPared();
    }


    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }
    Celda[][] getCeldas() { return celdas; }

    public void setPersonaje(Personaje personaje, int x, int y) {
        Celda celda = conseguirCelda(x, y);
        if (celda != null) {
            ContenidoCelda contenido = celda.getContenido();
            if (contenido != null && contenido.bloqueaPaso()) return;
            celda.setContenido(personaje);
        }
    }

    public Personaje getPersonaje(int x, int y) {
        if (!estaDentro(x, y)) return null;
        ContenidoCelda contenido = celdas[y][x].getContenido();
        return contenido != null ? contenido.asPersonaje() : null;
    }
}