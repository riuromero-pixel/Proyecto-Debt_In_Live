package src.modelo.map;

import java.util.Random;
import src.modelo.movimientos.EstrategiaMovimiento;
import src.modelo.movimientos.MovimientoErrante;
import src.modelo.movimientos.MovimientoPerseguidor;

public class GeneradorNivelMapa {

    private final Random random = new Random();

    public void prepararMapa(Mapa mapa) {
        for (int y = 0; y < mapa.getAlto(); y++) {
            for (int x = 0; x < mapa.getAncho(); x++) {
                mapa.getCeldas()[y][x].setContenido(null);
                mapa.getCeldas()[y][x].setDineroDebajo(null);
                mapa.getCeldas()[y][x].setBonusDebajo(null);
            }
        }

        for (int x = 0; x < mapa.getAncho(); x++) {
            mapa.getCeldas()[0][x].setContenido(new Pared(0));
            mapa.getCeldas()[mapa.getAlto() - 1][x].setContenido(new Pared(0));
        }
        for (int y = 0; y < mapa.getAlto(); y++) {
            mapa.getCeldas()[y][0].setContenido(new Pared(0));
            mapa.getCeldas()[y][mapa.getAncho() - 1].setContenido(new Pared(0));
        }
    }

    public void generarContenidoAleatorio(Mapa mapa, int nivel) {
        int celdasInteriores = (mapa.getAncho() - 2) * (mapa.getAlto() - 2);
        int maxObjetos = (int) (celdasInteriores * 0.30);

        int cantidadParedes = Math.min(nivel * 40, maxObjetos / 2);
        int cantidadEnemigos = Math.min(10 * nivel, maxObjetos - cantidadParedes);

        colocarAleatorio(mapa, cantidadEnemigos, nivel);
        generarBonus(mapa, nivel * 4);
        colocarEnemigoLetal(mapa, nivel);
    }

    private void colocarAleatorio(Mapa mapa, int cantidad, int nivel) {
        int colocados = 0;
        int intentos = 0;
        int maxIntentos = cantidad * 50;

        while (colocados < cantidad && intentos < maxIntentos) {
            intentos++;
            int x = 1 + random.nextInt(mapa.getAncho() - 2);
            int y = 1 + random.nextInt(mapa.getAlto() - 2);

            if (mapa.getCeldas()[y][x].getContenido() != null
                    || mapa.getCeldas()[y][x].getDineroDebajo() != null) continue;

            EstrategiaMovimiento estrategia = random.nextBoolean()
                    ? new MovimientoPerseguidor()
                    : new MovimientoErrante();

            mapa.getCeldas()[y][x].setContenido(new Enemigo(x, y, 10 + (nivel * 2), estrategia, nivel));
            colocados++;
        }
    }

    private void colocarEnemigoLetal(Mapa mapa, int nivel) {
        if (nivel != 3) return;

        int x, y, intentos = 0;
        do {
            x = 1 + random.nextInt(mapa.getAncho() - 2);
            y = 1 + random.nextInt(mapa.getAlto() - 2);
            intentos++;
        } while ((mapa.getCeldas()[y][x].getContenido() != null
                || Math.abs(x - 5) + Math.abs(y - 5) < 8)
                && intentos < 100);

        if (intentos < 100) {
            mapa.getCeldas()[y][x].setContenido(new EnemigoLetal(x, y, 9999));
        }
    }

    private void generarBonus(Mapa mapa, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            int x, y;
            do {
                x = 1 + random.nextInt(mapa.getAncho() - 2);
                y = 1 + random.nextInt(mapa.getAlto() - 2);
            } while (mapa.getCeldas()[y][x].getContenido() != null
                    || mapa.getCeldas()[y][x].getDineroDebajo() != null);

            mapa.getCeldas()[y][x].setContenido(new Bonus(2, 5000));
        }
    }

    public void generarDinero(Mapa mapa) {
        int cantidadDinero = 5 + 3 * 3;
        for (int i = 0; i < cantidadDinero; i++) {
            int x, y;
            do {
                x = 1 + random.nextInt(mapa.getAncho() - 2);
                y = 1 + random.nextInt(mapa.getAlto() - 2);
            } while (mapa.getCeldas()[y][x].getContenido() != null
                    || mapa.getCeldas()[y][x].getDineroDebajo() != null);
            mapa.getCeldas()[y][x].setContenido(new Dinero(100 + random.nextInt(400)));
        }
    }
}