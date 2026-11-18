package src.modelo.map;

import java.util.Random;
import src.modelo.movimientos.EstrategiaMovimiento;
import src.modelo.movimientos.MovimientoErrante;
import src.modelo.movimientos.MovimientoPerseguidor;

public class CreaEnemigo extends CreadorEnemigo {
    
    private final Random random = new Random();

    @Override
    public Enemigo crearEnemigo(int x, int y, int nivel) {
        EstrategiaMovimiento estrategiaElegida; //se elige la estrategia al azar
        if (random.nextBoolean()) {
            estrategiaElegida = new MovimientoPerseguidor();
        } else {
            estrategiaElegida = new MovimientoErrante();
        }
        int danioBase = 10 + (nivel * 3);
        return new Enemigo(x, y, danioBase, estrategiaElegida, nivel);
    }
}