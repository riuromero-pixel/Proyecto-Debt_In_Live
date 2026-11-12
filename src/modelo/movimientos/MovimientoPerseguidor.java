package src.modelo.movimientos;

import src.modelo.entities.Personaje;
import src.modelo.map.Celda;
import src.modelo.map.Enemigo;
import src.modelo.map.Mapa;
import src.modelo.map.Pared;

public class MovimientoPerseguidor implements EstrategiaMovimiento {

    @Override
    public void mover(Enemigo enemigo, Personaje personaje, Mapa mapa) {
        // Implementación del movimiento perseguidor

        if (personaje == null) return;

        int[][] direcciones = {
            {0, -1}, // Arriba
            {0, 1},  // Abajo
            {-1, 0}, // Izquierda
            {1, 0}   // Derecha
        };

        int mejorX = enemigo.getX();
        int mejorY = enemigo.getY();
        int menorDistancia = Integer.MAX_VALUE;

        for (int[] dir : direcciones) {
            int nuevaX = enemigo.getX() + dir[0];
            int nuevaY = enemigo.getY() + dir[1];

            Celda celdaDestino = mapa.conseguirCelda(nuevaX, nuevaY);

            if (celdaDestino != null) {
                // SI LA CELDA TIENE AL PERSONAJE: Colisión directa, le hace daño y termina
                if (celdaDestino.getContenido() instanceof Personaje) {
                    personaje.danioRecibido(enemigo.getDanio());
                    return; 
                }

                // Si la celda está libre (no es pared ni otro enemigo)
                if (!(celdaDestino.getContenido() instanceof Pared) && !(celdaDestino.getContenido() instanceof Enemigo)) {
                    int distancia = Math.abs(nuevaX - personaje.getX()) + Math.abs(nuevaY - personaje.getY());

                    if (distancia < menorDistancia) {
                        menorDistancia = distancia;
                        mejorX = nuevaX;
                        mejorY = nuevaY;
                    }
                }
            }
        } 
        enemigo.moverEnemigo(enemigo, mejorX, mejorY, mapa);
    }     
}
