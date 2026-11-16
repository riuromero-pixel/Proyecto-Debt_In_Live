package src.modelo.entities;

import src.modelo.map.Mapa;
import src.modelo.map.Celda;
import src.modelo.map.Pared;
import src.modelo.map.Bonus;

public class Jubilado extends Personaje implements CreadorParedes, RompeParedes { 
public Jubilado(int x, int y, Mapa mapa) {
super(x, y, 1, 100, false, mapa);
   this.velocidad = 1; // velocidad inicial mas baja por ser un jubilado
}
@Override 
    public void activarBonus(Bonus bonus) {
        super.activarBonus(bonus);
        this.velocidad += bonus.bonusVelocidad + 3;
    }
@Override
public void crearParedes() {
    //el jubilado solo puede poner 1 pared
    if (direccionActual == null) {
        return;
    }
    char dir = direccionActual.charAt(0);
    int nuevaX = this.x;
    int nuevaY = this.y;
    
    switch (dir) {
        case 'W': nuevaY--; break;
        case 'S': nuevaY++; break;
        case 'A': nuevaX--; break;
        case 'D': nuevaX++; break;
        default:
            return;
    }
    // verificar que no sea borde
    if (nuevaX < 0 || nuevaX >= mapa.getAncho() || nuevaY < 0 || nuevaY >= mapa.getAlto()) {
        return;
    }
    Celda celda = mapa.conseguirCelda(nuevaX, nuevaY);
    if (celda == null) {
        return;
    }
    
    if (celda.contenido != null) {
        return;
    }
    celda.contenido = new Pared();
}
public void romperParedes() {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - ultimoGolpe < 400) {
            return;
        }
        // Direcciones: ARRIBA, ABAJO, DERECHA, IZQUIERDA
        int[][] direcciones = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        boolean encontroPared = false;      
        for (int[] dir : direcciones) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            
            //verificar si hay una pared en la posición (nx, ny)
            boolean hayPared = verificarPared(nx, ny);
            
            if (hayPared) {
                //si es una nueva pared o cambiamos de objetivo, reiniciar golpes
                if (paredObjetivo == null || !paredObjetivo.equals(nx + "," + ny)) {
                    paredObjetivo = nx + "," + ny;
                    golpesRestantes = 2;
                }
                golpesRestantes--;
                ultimoGolpe = tiempoActual;
                
                // si los golpes llegan a 0, se rompe la pared
                if (golpesRestantes <= 0) {
                    // Eliminar la pared de la celda
                    Celda celda = conseguirCelda(nx, ny);
                    if (celda != null) {
                        celda.contenido = null;
                    }
                    // Reiniciar contadores
                    golpesRestantes = 2;
                    paredObjetivo = null;
                }      
                encontroPared = true;
                break;
            }
        }    
}
}