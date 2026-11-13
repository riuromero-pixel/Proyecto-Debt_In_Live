package src.modelo.map;

import src.modelo.entities.Personaje;
import src.modelo.movimientos.EstrategiaMovimiento;
import java.util.Random;

public class Enemigo extends ObjetoEntorno {
    private int x;
    private int y;
    private int danio;

    private EstrategiaMovimiento estrategia;

    private int variante;

    public Enemigo(int x, int y, int danio, EstrategiaMovimiento estrategia, int nivel) {
        super(false);
        this.x = x;
        this.y = y;
        this.danio = danio;
        this.estrategia = estrategia;

        // Generamos la variante aleatoria según el nivel
        Random rand = new Random();
        if (nivel == 1) {
            this.variante = 1 + rand.nextInt(4); // Sortea del 1 al 4
        } else if (nivel == 2) {
            this.variante = 1 + rand.nextInt(5); // Sortea del 1 al 5
        } else if (nivel == 3) {
            this.variante = 1 + rand.nextInt(4); // Sortea del 1 al 4
        } else {
            this.variante = 1; 
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getDanio() { return danio; }

    public int getVariante() {
        return this.variante;
    }
    
    public void setEstrategia(EstrategiaMovimiento estrategia) {
        this.estrategia = estrategia;
    }
    public EstrategiaMovimiento getEstrategia() {
        return this.estrategia;
    }

    @Override
    public Enemigo asEnemigo() {
        return this;
    }

    @Override
    public void aplicarDanio(Personaje personaje) {
        personaje.danioRecibido(this.danio);
    }

    @Override
    public String getTipoVisual() {
        return "ENEMIGO";
    }

    @Override
    public boolean esEnemigo() {
        return true;
    }

    public boolean esLetal() {
        return false;
    }

    public void moverEnemigo(int mejorX, int mejorY, Mapa mapa) {
            Celda celdaActual = mapa.conseguirCelda(this.x, this.y);
            Celda celdaNueva = mapa.conseguirCelda(mejorX, mejorY);

            if (celdaActual != null && celdaNueva != null) {
                
                // Si el enemigo se va de su celda y había dinero abajo, lo restauramos a visible
                if (celdaActual.getDineroDebajo() != null) {
                    celdaActual.setContenido(celdaActual.getDineroDebajo());
                    celdaActual.setDineroDebajo(null);
                } else {
                    celdaActual.setContenido(null);
                }

                // Si la celda nueva a la que entra tiene dinero, lo resguardamos en 'dineroDebajo'
                if (celdaNueva.getContenido() != null && celdaNueva.getContenido().esDinero()) {
                    celdaNueva.setDineroDebajo(celdaNueva.getContenido().asDinero());
                }

                // Colocamos al enemigo en la celda nueva y actualizamos sus coordenadas
                celdaNueva.setContenido(this);
                this.x = mejorX;
                this.y = mejorY;
            }
        }

    public void actualizarPersecucion(Personaje personaje, Mapa mapa) {

        if (estrategia != null) {
            estrategia.mover(this, personaje, mapa);
        }
    } 
}