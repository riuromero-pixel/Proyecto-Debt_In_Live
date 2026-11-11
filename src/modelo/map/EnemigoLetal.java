package src.modelo.map;

import src.modelo.movimientos.EstrategiaMovimiento;

public class EnemigoLetal extends Enemigo {

    public EnemigoLetal(int x, int y, int danio, EstrategiaMovimiento estrategia) {
        super(x, y, danio, estrategia, 3);
    }
}
