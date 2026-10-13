package src.modelo.movimientos;

import src.modelo.entities.Personaje;
import src.modelo.map.Celda;
import src.modelo.map.Dinero;
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

        int mejorX = enemigo.x;
        int mejorY = enemigo.y;
        int menorDistancia = Integer.MAX_VALUE;

        for (int[] dir : direcciones) {
            int nuevaX = enemigo.x + dir[0];
            int nuevaY = enemigo.y + dir[1];

            Celda celdaDestino = mapa.conseguirCelda(nuevaX, nuevaY);

            if (celdaDestino != null) {
                // SI LA CELDA TIENE AL PERSONAJE: Colisión directa, le hace daño y termina
                if (celdaDestino.contenido instanceof Personaje) {
                    personaje.danioRecibido(enemigo.danio);
                    System.out.println("¡Te tocó un enemigo! Daño: " + enemigo.danio);
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
            moverEnemigo(enemigo, mejorX, mejorY, mapa);
        } 
    }   

        
        private void moverEnemigo(Enemigo enemigo, int mejorX, int mejorY, Mapa mapa) {
            Celda celdaActual = mapa.conseguirCelda(enemigo.x, enemigo.y);
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
                celdaNueva.contenido = enemigo;
                enemigo.x = mejorX;
                enemigo.y = mejorY;
            }
        }
    
}
