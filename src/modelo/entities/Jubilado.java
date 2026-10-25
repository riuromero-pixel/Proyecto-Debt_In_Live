package src.modelo.entities;


import src.modelo.map.Mapa;
import src.modelo.map.Celda;
import src.modelo.map.Pared;
import src.modelo.map.Bonus;

public class Jubilado extends Personaje implements CreadorParedes{

private int velocidadOriginal;
private int fuerzaOriginal;

public Jubilado(int x, int y, Mapa mapa) {
super(x, y, 1, 3, 100, false, mapa);
   this.velocidad = 1; // velocidad inicial mas baja por ser un jubilado
   this.fuerza = 1; // fuerza inicial mas baja por ser un jubilado
}
    public void activarBonus(Bonus bonus) {
        if (!isBonusActivado()) {
            velocidadOriginal = velocidad; // Guardar la velocidad original
            fuerzaOriginal = fuerza; // Guardar la fuerza original
        }
        super.activarBonus(bonus);
        this.velocidad += bonus.bonusVelocidad + 3;
        this.fuerza +=4; 
    }
    public void desactivarBonus() {
        super.desactivarBonus();
        this.velocidad = velocidadOriginal; 
        this.fuerza = fuerzaOriginal;
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
}