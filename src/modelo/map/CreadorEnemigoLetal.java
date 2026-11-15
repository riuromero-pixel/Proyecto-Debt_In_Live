package src.modelo.map;

import src.modelo.movimientos.EstrategiaMovimiento;
import src.modelo.movimientos.MovimientoPerseguidor;


public class CreadorEnemigoLetal extends CreadorEnemigo {
    EstrategiaMovimiento estrategiaElegida;
    
    // Acá nos encargamos del daño, la estrategia y el sprite fijo
    @Override 
    public Enemigo crearEnemigo(int x, int y, int nivel) {
        estrategiaElegida = new MovimientoPerseguidor();
        return new EnemigoLetal(x, y, estrategiaElegida);
    } //NO DEVUELVE DAÑO YA Q ESTE SE ASIGNA CON EL SUPER DE ENEMIGO EN O EN EL BUILDER PROPIO!!!!!!
}