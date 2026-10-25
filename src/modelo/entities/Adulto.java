package src.modelo.entities;

import src.modelo.map.Celda;
import src.modelo.map.Mapa;
import src.modelo.map.Pared;
import src.modelo.map.Bonus;
public class Adulto extends Personaje implements CreadorParedes{

    private int velocidadOriginal;
    private int fuerzaOriginal;

    public Adulto(int x, int y, Mapa mapa) {
        super(x, y, 2, 2, 100, false, mapa);
        this.velocidad = 2; // velocidad inicial por ser un adulto
        this.fuerza = 3; // fuerza inicial por ser un adulto
    }
    public void activarBonus(Bonus bonus) {
        if (!isBonusActivado()) {
            velocidadOriginal = velocidad; // Guardar la velocidad original
            fuerzaOriginal = fuerza; // Guardar la fuerza original
        }
        super.activarBonus(bonus);
        this.velocidad += bonus.bonusVelocidad + 2;
        this.fuerza +=6; 
    }
    public void desactivarBonus() {
        super.desactivarBonus();
        this.velocidad = velocidadOriginal; 
        this.fuerza = fuerzaOriginal;
    }


@Override
public void crearParedes() {
    // El adulto puede llenar su fila entera de paredes a la que apunta
    if (direccionActual == null) {
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
            return;
    }
    
    //colocar paredes en toda la fila/columna desde la posicion actual hasta el borde
    int nx = this.x + dx;
    int ny = this.y + dy;
    
    while (nx >= 0 && nx < mapa.getAncho() && ny >= 0 && ny < mapa.getAlto()) {
        Celda celda = mapa.conseguirCelda(nx, ny);
        if (celda != null && celda.contenido == null) {
            celda.contenido = new Pared();
        }
        nx += dx;
        ny += dy;
    }
    
}
}