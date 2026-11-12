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
    public void actualizarPosicionesVisualesEnemigos() { gestorEnemigos.actualizarPosicionesVisualesEnemigos(this); }
    public boolean verificarColisionConEnemigo(Personaje jugador) { return gestorEnemigos.verificarColisionConEnemigo(this, jugador); }

    public int contarDineroRestante() { return gestorObjetos.contarDineroRestante(this); }
    public boolean noQuedaDinero() { return gestorObjetos.noQuedaDinero(this); }
    public void recolectarDeCelda(Personaje jugador, int x, int y) { gestorObjetos.recolectarDeCelda(this, jugador, x, y); }

    public Celda conseguirCelda(int x, int y) {
        if (x < 0 || x >= ancho || y < 0 || y >= alto) return null;
        return celdas[y][x];
    }

    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }
    public Celda[][] getCeldas() { return celdas; }

    public void setPersonaje(Personaje personaje, int x, int y) {
        Celda celda = conseguirCelda(x, y);
        if (celda != null) {
            if (celda.getContenido() instanceof Enemigo) return;
            celda.setContenido(personaje);
        }
    }

    public Personaje getPersonaje(int x, int y) {
        if (x < 0 || x >= ancho || y < 0 || y >= alto) return null;
        Object contenido = celdas[y][x].getContenido();
        if (contenido instanceof Personaje) return (Personaje) contenido;
        return null;
    }
}


    
 

        
    
