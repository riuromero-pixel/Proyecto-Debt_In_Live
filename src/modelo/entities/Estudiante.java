package src.modelo.entities;

import src.modelo.map.Celda;
import src.modelo.map.ContenidoCelda;
import src.modelo.map.Mapa;

public class Estudiante extends Personaje{

    private static final long COOLDOWN_SALTO_MS = 7000;
    private long ultimoSalto = 0;

    public Estudiante(int x, int y, Mapa mapa) {
        super(x, y, 3, 100, false, mapa);
    }

    @Override
    protected int getBonusExtra() {
        return 3;
    }

    @Override
    public String getNombreTipo() {
        return "Estudiante";
    }

    @Override
    public int getTipoSprite() {
        return 0;
    }


@Override
public void saltar() {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoSalto < COOLDOWN_SALTO_MS) return;

        String dirStr = getDireccionActual();
        if (dirStr == null) return;

        char dir = dirStr.charAt(0);
        int dx = 0, dy = 0;
        switch (dir) {
            case 'W': dy = -2; break;
            case 'S': dy =  2; break;
            case 'A': dx = -2; break;
            case 'D': dx =  2; break;
            default: return;
        }

        Mapa m = getMapa();
        int nx = getX() + dx;
        int ny = getY() + dy;

        if (nx < 0 || nx >= m.getAncho() || ny < 0 || ny >= m.getAlto()) return;

        Celda destino = m.conseguirCelda(nx, ny);
        if (destino != null) {
            ContenidoCelda contenido = destino.getContenido();
            if (contenido != null) return;
        }

        if (destino != null) m.recolectarDeCelda(this, nx, ny);
        m.limpiarCeldaDe(this);
        setPosicion(nx, ny);
        m.setPersonaje(this, nx, ny);

        ultimoSalto = ahora;
    }

}