package src.modelo.entities;


import src.modelo.map.Mapa;
import src.modelo.map.Celda;
import src.modelo.map.Pared;

public class Jubilado extends Personaje implements CreadorParedes{

public Jubilado(int x, int y, Mapa mapa) {
super(x, y, 1, 100, false, mapa);
}

@Override
protected int getBonusExtra() {
    return 3;
}

@Override
public void crearParedes() {
    //el jubilado solo puede poner 1 pared
    if (getDireccionActual() == null) {
        return;
    }
    
    char dir = getDireccionActual().charAt(0);
    int nuevaX = getX();
    int nuevaY = getY();
    
    switch (dir) {
        case 'W': nuevaY--; break;
        case 'S': nuevaY++; break;
        case 'A': nuevaX--; break;
        case 'D': nuevaX++; break;
        default:
            return;
    }
    
    // verificar que no sea borde
    if (nuevaX < 0 || nuevaX >= getMapa().getAncho() || nuevaY < 0 || nuevaY >= getMapa().getAlto()) {
        return;
    }
    
    Celda celda = getMapa().conseguirCelda(nuevaX, nuevaY);
    if (celda == null) {
        return;
    }
    
    if (celda.getContenido() != null) {
        return;
    }
    
    celda.setContenido(new Pared());
}
}