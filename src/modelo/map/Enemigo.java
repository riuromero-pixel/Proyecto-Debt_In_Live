 package src.modelo.map;

import src.modelo.entities.Personaje;
import src.modelo.movimientos.EstrategiaMovimiento;

public class Enemigo extends ObjetoEntorno {
    public int x;
    public int y;
    public int danio;

    private EstrategiaMovimiento estrategia;

    // Coordenadas VISUALES para interpolación suave
    public double posXVisual;
    public double posYVisual;
    private static final double FACTOR_INTERPOLACION = 0.15;

    public Enemigo(int x, int y, int danio, EstrategiaMovimiento estrategia) {
        super(true);
        this.x = x;
        this.y = y;
        this.danio = danio;
        this.estrategia = estrategia;

        this.posXVisual = x;
        this.posYVisual = y;
    }
    public void setEstrategia(EstrategiaMovimiento estrategia) {
        this.estrategia = estrategia;
    }
    public EstrategiaMovimiento getEstrategia() {
        return this.estrategia;
    }

    public void actualizarPersecucion(Personaje personaje, Mapa mapa) {

        if (estrategia != null) {
            estrategia.mover(this, personaje, mapa);
        }
    } 

    // Interpola suavemente las coordenadas visuales hacia las lógicas
    public void actualizarPosicionVisual() {
        posXVisual += (x - posXVisual) * FACTOR_INTERPOLACION;
        posYVisual += (y - posYVisual) * FACTOR_INTERPOLACION;

        // Umbral para no oscilar indefinidamente
        if (Math.abs(posXVisual - x) < 0.01) posXVisual = x;
        if (Math.abs(posYVisual - y) < 0.01) posYVisual = y;
    }

    public int hacerDanio() {
        return this.danio;
    }
} 
