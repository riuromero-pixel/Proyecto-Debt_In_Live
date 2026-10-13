package src.modelo.movimientos;

import src.modelo.entities.Personaje;
import src.modelo.map.Enemigo;
import src.modelo.map.Mapa;

//Patron strategy para el movimiento de los enemigos, permite cambiar la estrategia de movimiento en tiempo de ejecución
public interface EstrategiaMovimiento {
    void mover(Enemigo enemigo, Personaje personaje, Mapa mapa);
}
