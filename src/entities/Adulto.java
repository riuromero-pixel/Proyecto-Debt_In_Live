package src.entities;

import src.map.Celda;
import src.map.Mapa;
import src.map.Pared;
public class Adulto extends Personaje {

    public Adulto(int x, int y, Mapa mapa) {
        super(x, y, 2, 2, 40, false, mapa);
        this.velocidad = 2; // velocidad inicial por ser un adulto
        this.fuerza = 3; // fuerza inicial por ser un adulto
    }
    public void activarBonus() {
    if (this.bonusActivado) {
        this.velocidad += 3; 
        this.fuerza += 3;
    }
}
public void crearParedes(int x, int y) {
    // Obtener la celda donde está el personaje
        Celda celda = mapa.conseguirCelda(x,y);
        if (celda == null) {
            return;
        }
        // Verificar que la celda esté vacía
        if (celda.contenido != null) {
            return;
        }
        if (this.fuerza > 2) { // solo puede crear paredes si tiene suficiente fuerza
            Pared pared = new Pared();
            celda.contenido = pared;
        }
}
}
