package src.modelo.movimientos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import src.modelo.entities.Personaje;
import src.modelo.map.Celda;
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
            int nuevaX = enemigo.getX() + dir[0];
            int nuevaY = enemigo.getY() + dir[1];

            Celda celdaDestino = mapa.conseguirCelda(nuevaX, nuevaY);
            if (celdaDestino == null) continue;

            // Si pisa al jugador, le hace daño
            if (celdaDestino.getContenido() instanceof Personaje) {
                if (enemigo instanceof EnemigoLetal){
                    jugador.danioRecibido(jugador.getDinero()+1);
                } else{
                jugador.danioRecibido(enemigo.getDanio());
                }
                return;
            }

            if (!(celdaDestino.getContenido() instanceof Pared)
                && !(celdaDestino.getContenido() instanceof Enemigo)) {
                opciones.add(new int[]{nuevaX, nuevaY});
            }
        }

        if (opciones.isEmpty()) return;

        int[] elegida = opciones.get(random.nextInt(opciones.size()));
        enemigo.moverEnemigo(enemigo, elegida[0], elegida[1], mapa);
    }
}

       
    
    

