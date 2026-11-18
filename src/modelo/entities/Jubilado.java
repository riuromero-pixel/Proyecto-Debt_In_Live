package src.modelo.entities;


import src.modelo.map.Mapa;

public class Jubilado extends Personaje {

    public Jubilado(int x, int y, Mapa mapa) {
        super(x, y, 1, 100, false, mapa);
    }

    @Override
    protected int getBonusExtra() {
        return 3;
    }

    @Override
    public String getNombreTipo() {
        return "Jubilado";
    }

    @Override
    public int getTipoSprite() {
        return 2;
    }

    @Override
    public void usarHabilidadEspecial() {
        if (getDireccionActual() == null) return;

        char dir = getDireccionActual().charAt(0);
        int nuevaX = getX();
        int nuevaY = getY();
        switch (dir) {
            case 'W': nuevaY--; break;
            case 'S': nuevaY++; break;
            case 'A': nuevaX--; break;
            case 'D': nuevaX++; break;
            default: return;
        }

        if (nuevaX < 0 || nuevaX >= getMapa().getAncho()
                || nuevaY < 0 || nuevaY >= getMapa().getAlto()) return;
        getMapa().ponerPared(nuevaX, nuevaY);
    }
}