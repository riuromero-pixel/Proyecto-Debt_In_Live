package src.modelo.entities;

import src.modelo.map.Celda;
import src.modelo.map.Mapa;
import src.modelo.map.Pared;

public class Adulto extends Personaje {

    public Adulto(int x, int y, Mapa mapa) {
        super(x, y, 2, 100, false, mapa);
    }

    @Override
    protected int getBonusExtra() {
        return 2;
    }

    @Override
    public String getNombreTipo() {
        return "Adulto";
    }

    @Override
    public int getTipoSprite() {
        return 1;
    }

    @Override
    public boolean puedeCrearParedes() {
        return true;
    }

    @Override
    public void crearParedes() {
        String dirStr = getDireccionActual();
        if (dirStr == null) return;

        char dir = dirStr.charAt(0);
        int dx = 0, dy = 0;

        switch (dir) {
            case 'W': dy = -1; break;
            case 'S': dy = 1;  break;
            case 'A': dx = -1; break;
            case 'D': dx = 1;  break;
            default: return;
        }

        int nx = getX() + dx;
        int ny = getY() + dy;

        while (nx >= 0 && nx < getMapa().getAncho()
            && ny >= 0 && ny < getMapa().getAlto()) {
            Celda celda = getMapa().conseguirCelda(nx, ny);
            if (celda != null && celda.getContenido() == null) {     
                celda.setContenido(new Pared());                
            }
            nx += dx;
            ny += dy;
        }
    }
}