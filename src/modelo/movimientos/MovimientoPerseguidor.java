package src.modelo.movimientos;

import src.modelo.entities.Personaje;
import src.modelo.map.Celda;
import src.modelo.map.ContenidoCelda;
import src.modelo.map.Enemigo;
import src.modelo.map.Mapa;

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
                ContenidoCelda contenido = celdaDestino.getContenido();

                // SI LA CELDA TIENE AL PERSONAJE: Colisión directa, le hace daño y termina
                if (contenido instanceof Personaje) {
                    enemigo.aplicarDanio(personaje);
                    return; 
                }

                // Si la celda está libre (no bloquea el paso)
                if (contenido == null || !contenido.bloqueaPaso()) {
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
