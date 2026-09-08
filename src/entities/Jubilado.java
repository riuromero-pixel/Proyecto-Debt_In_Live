package src.entities;


import src.map.Mapa;
import src.map.Celda;
import src.map.Pared;
public class Jubilado extends Personaje {

public Jubilado(int x, int y, Mapa mapa) {
super(x, y, 1, 3, 50, false, mapa);
   this.velocidad = 1; // velocidad inicial mas baja por ser un jubilado
   this.fuerza = 1; // fuerza inicial mas baja por ser un jubilado
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