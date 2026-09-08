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

@Override
public void crearParedes() {
    //el jubilado solo puede poner 1 pared
    if (direccionActual == null) {
        System.out.println("No tienes direccion definida");
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
            System.out.println("Direccion invalida");
            return;
    }
    
    // verificar que no sea borde
    if (nuevaX < 0 || nuevaX >= mapa.getAncho() || nuevaY < 0 || nuevaY >= mapa.getAlto()) {
        System.out.println("No puedes poner paredes en los bordes del mapa");
        return;
    }
    
    Celda celda = mapa.conseguirCelda(nuevaX, nuevaY);
    if (celda == null) {
        System.out.println("Celda fuera del mapa");
        return;
    }
    
    if (celda.contenido != null) {
        System.out.println("La celda ya esta ocupada");
        return;
    }
    
    celda.contenido = new Pared();
    System.out.println("Jubilado coloco 1 pared!");
}
}