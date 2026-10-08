 package src.modelo.map;

import java.util.Random;
import src.modelo.entities.Personaje;

public class Enemigo extends ObjetoEntorno {
    public int x;
    public int y;
    public int danio;
    private Random random;

    // Coordenadas VISUALES para interpolación suave
    public double posXVisual;
    public double posYVisual;
    private static final double FACTOR_INTERPOLACION = 0.15;

    public Enemigo(int x, int y, int danio) {
        super(true);
        this.x = x;
        this.y = y;
        this.danio = danio;
        this.random = new Random();

        this.posXVisual = x;
        this.posYVisual = y;
    }

    public void actualizarPersecucion(Personaje personaje, Mapa mapa) {
        if (personaje == null) return;

        int[][] direcciones = {
            {0, -1}, // Arriba
            {0, 1},  // Abajo
            {-1, 0}, // Izquierda
            {1, 0}   // Derecha
        };

        int mejorX = this.x;
        int mejorY = this.y;
        int menorDistancia = Integer.MAX_VALUE;

        for (int[] dir : direcciones) {
            int nuevaX = this.x + dir[0];
            int nuevaY = this.y + dir[1];

            Celda celdaDestino = mapa.conseguirCelda(nuevaX, nuevaY);

            if (celdaDestino != null) {
                // SI LA CELDA TIENE AL PERSONAJE: Colisión directa, le hace daño y termina
                if (celdaDestino.contenido instanceof Personaje) {
                    personaje.danioRecibido(this.danio);
                    System.out.println("¡Te tocó un enemigo! Daño: " + this.danio);
                    return; 
                }

                // Si la celda está libre (no es pared ni otro enemigo)
                if (!(celdaDestino.contenido instanceof Pared) && !(celdaDestino.contenido instanceof Enemigo)) {
                    int distancia = Math.abs(nuevaX - personaje.getX()) + Math.abs(nuevaY - personaje.getY());

                    if (distancia < menorDistancia) {
                        menorDistancia = distancia;
                        mejorX = nuevaX;
                        mejorY = nuevaY;
                    }
                }
            }
        } 

        
        if (mejorX != this.x || mejorY != this.y) {
            Celda celdaActual = mapa.conseguirCelda(this.x, this.y);
            Celda celdaNueva = mapa.conseguirCelda(mejorX, mejorY);

            if (celdaActual != null && celdaNueva != null) {
                
                // Si el enemigo se va de su celda y había dinero abajo, lo restauramos a visible
                if (celdaActual.dineroDebajo != null) {
                    celdaActual.contenido = celdaActual.dineroDebajo;
                    celdaActual.dineroDebajo = null;
                } else {
                    celdaActual.contenido = null;
                }

                // Si la celda nueva a la que entra tiene dinero, lo resguardamos en 'dineroDebajo'
                if (celdaNueva.contenido instanceof Dinero) {
                    celdaNueva.dineroDebajo = (Dinero) celdaNueva.contenido;
                }

                // Colocamos al enemigo en la celda nueva y actualizamos sus coordenadas
                celdaNueva.contenido = this;
                this.x = mejorX;
                this.y = mejorY;
            }
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
