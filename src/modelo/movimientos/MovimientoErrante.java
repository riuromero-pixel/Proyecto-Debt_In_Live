package src.modelo.movimientos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import src.modelo.entities.Personaje;
import src.modelo.map.Celda;
import src.modelo.map.Dinero;
import src.modelo.map.Enemigo;
import src.modelo.map.EnemigoLetal;
import src.modelo.map.Mapa;
import src.modelo.map.Pared;

public class MovimientoErrante implements EstrategiaMovimiento {
     private final Random random = new Random();
  // Implementación del movimiento errante
    @Override
    public void mover(Enemigo enemigo, Personaje jugador, Mapa mapa) {

        int[][] direcciones = {
            {0, -1}, {0, 1}, {-1, 0}, {1, 0}
        };

        List<int[]> opciones = new ArrayList<>();

        for (int[] dir : direcciones) {
            int nuevaX = enemigo.x + dir[0];
            int nuevaY = enemigo.y + dir[1];

            Celda celdaDestino = mapa.conseguirCelda(nuevaX, nuevaY);
            if (celdaDestino == null) continue;

            // Si pisa al jugador, le hace daño
            if (celdaDestino.contenido instanceof Personaje) {
                if (enemigo instanceof EnemigoLetal){
                    jugador.danioRecibido(jugador.getDinero()+1);
                } else{
                jugador.danioRecibido(enemigo.danio);
                }
                return;
            }

            if (!(celdaDestino.contenido instanceof Pared)
                && !(celdaDestino.contenido instanceof Enemigo)) {
                opciones.add(new int[]{nuevaX, nuevaY});
            }
        }

        if (opciones.isEmpty()) return;

        int[] elegida = opciones.get(random.nextInt(opciones.size()));
        moverEnemigo(enemigo, elegida[0], elegida[1], mapa);
    }

    private void moverEnemigo(Enemigo enemigo, int mejorX, int mejorY, Mapa mapa) {
        Celda celdaActual = mapa.conseguirCelda(enemigo.x, enemigo.y);
        Celda celdaNueva = mapa.conseguirCelda(mejorX, mejorY);
        if (celdaActual == null || celdaNueva == null) return;

        if (celdaActual.dineroDebajo != null) {
            celdaActual.contenido = celdaActual.dineroDebajo;
            celdaActual.dineroDebajo = null;
        } else {
            celdaActual.contenido = null;
        }

        if (celdaNueva.contenido instanceof Dinero) {
            celdaNueva.dineroDebajo = (Dinero) celdaNueva.contenido;
        }

        celdaNueva.contenido = enemigo;
        enemigo.x = mejorX;
        enemigo.y = mejorY;
    }
}

       
    
    

