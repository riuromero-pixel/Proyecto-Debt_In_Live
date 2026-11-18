package src.modelo.map;

import src.modelo.movimientos.EstrategiaMovimiento;
import src.modelo.movimientos.MovimientoPerseguidor;

public class CreadorEnemigoLetal extends CreadorEnemigo {
    EstrategiaMovimiento estrategia;
    
    // Acá nos encargamos del daño, la estrategia y el sprite fijo
    @Override 
    public Enemigo crearEnemigo(int x, int y, int nivel) {
        int danioBase = 100000;
        estrategia = new MovimientoPerseguidor();
        return new EnemigoLetal(x, y, danioBase, estrategia);
    }
}