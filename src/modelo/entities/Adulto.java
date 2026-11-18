package src.modelo.entities;

import src.modelo.map.Mapa;

public class Adulto extends Personaje {

    public Adulto(int x, int y, Mapa mapa) {
        super(x, y, 2, 100, false, mapa);
    }

    @Override
    protected int getBonusExtra() {
        return 2;
    }

    @Override public int getTipoSprite() { return 1; }

    @Override
    public String getNombreTipo() {
        return "Adulto";
    }

    @Override
    public void usarHabilidadEspecial() {
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
            getMapa().ponerPared(nx, ny);
            nx += dx;
            ny += dy;
        }
    }
}