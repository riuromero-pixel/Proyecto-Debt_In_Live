package src.entities;

import src.map.Celda;
import src.map.Mapa;
import src.map.Pared;
import src.map.Bonus;
public class Adulto extends Personaje {

    private int velocidadOriginal;

    public Adulto(int x, int y, Mapa mapa) {
        super(x, y, 2, 2, 40, false, mapa);
        this.velocidad = 2; // velocidad inicial por ser un adulto
        this.fuerza = 3; // fuerza inicial por ser un adulto
    }
    public void activarBonus(Bonus bonus) {
        super.activarBonus(bonus);
        velocidadOriginal = velocidad; // Guardar la velocidad original
        this.velocidad += bonus.bonusVelocidad + 3;
        this.fuerza +=4; 
    }
    public void desactivarBonus() {
        super.desactivarBonus();
        this.velocidad = velocidadOriginal; 
    }


@Override
public void crearParedes() {
    // El adulto puede llenar su fila entera de paredes a la que apunta
    if (direccionActual == null) {
        System.out.println("No tienes direccion definida");
        return;
    }
    
    char dir = direccionActual.charAt(0);
    int dx = 0, dy = 0;
    
    switch (dir) {
        case 'W': dy = -1; break;
        case 'S': dy = 1; break;
        case 'A': dx = -1; break;
        case 'D': dx = 1; break;
        default:
            System.out.println("Direccion invalida");
            return;
    }
    
    //colocar paredes en toda la fila/columna desde la posicion actual hasta el borde
    int paredesColocadas = 0;
    int nx = this.x + dx;
    int ny = this.y + dy;
    
    while (nx >= 0 && nx < mapa.getAncho() && ny >= 0 && ny < mapa.getAlto()) {
        Celda celda = mapa.conseguirCelda(nx, ny);
        if (celda != null && celda.contenido == null) {
            celda.contenido = new Pared();
            paredesColocadas++;
        }
        nx += dx;
        ny += dy;
    }
    
    if (paredesColocadas > 0) {
        System.out.println("Adulto coloco " + paredesColocadas + " paredes!");
    } else {
        System.out.println("No se pudo colocar paredes");
    }
}
}