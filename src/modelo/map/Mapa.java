package src.modelo.map;

import java.util.ArrayList;
import java.util.List;

import src.modelo.entities.Personaje;

public class Mapa {

    private Celda[][] celdas;
    private final int ancho;
    private final int alto;

    private final List<Enemigo> enemigos = new ArrayList<>();
    private final List<Dinero> dineros = new ArrayList<>();

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

    public void prepararMapa() { generador.prepararMapa(this); }
    public void generarContenidoAleatorio(int nivel) { generador.generarContenidoAleatorio(this, nivel); }
    public void generarDinero() { generador.generarDinero(this); }

    public void registrarEnemigo(Enemigo e) { enemigos.add(e); }
    public List<Enemigo> getEnemigos() { return enemigos; }

    public void registrarDinero(Dinero d) { dineros.add(d); }
    public List<Dinero> getDineros() { return dineros; }

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

    public boolean ponerPared(int x, int y) {
        Celda c = conseguirCelda(x, y);
        if (c == null || c.getContenido() != null) return false;
        c.setContenido(new Pared());
        return true;
    }

    public void limpiarCelda(int x, int y) {
        Celda c = conseguirCelda(x, y);
        if (c != null) c.setContenido(null);
    }

    public void limpiarCeldaDe(Personaje personaje) {
        Celda c = conseguirCelda(personaje.getX(), personaje.getY());
        if (c == null) return;
        if (c.getContenido() == personaje) {
            c.setContenido(null);
        }
        if (c.getContenidoDebajo() == personaje) {
            c.setContenidoDebajo(null);
        }
    }

    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }
    Celda[][] getCeldas() { return celdas; }

    public void setPersonaje(Personaje personaje, int x, int y) {
        Celda celda = conseguirCelda(x, y);
        if (celda == null) return;
        ContenidoCelda contenido = celda.getContenido();
        if (contenido != null && contenido.bloqueaPaso()) return;
        celda.setContenido(personaje);
    }
}