package src.modelo.map;

import src.modelo.entities.Personaje;
import src.modelo.movimientos.MovimientoErrante;

public class EnemigoLetal extends Enemigo {

    private static final long INTERVALO_TELETRANSPORTE_MS = 6000;
    private long ultimoTeletransporte = System.currentTimeMillis();
    private final java.util.Random random = new java.util.Random();

    public EnemigoLetal (int x, int y, int danio) { //Enemigo que elimina al jugador con solo tocarlo
    super(x ,y ,danio, new MovimientoErrante(), 3);
    }

    @Override
    public boolean aplicarDanio(Personaje personaje) {
        personaje.morir();
        return true;
    }

    @Override
    public String getTipoVisual() {
        return "ENEMIGO_LETAL";
    }

    @Override
    public boolean esLetal() {
        return true;
    }

    @Override
    public void actualizarMovimiento(Mapa mapa, Personaje jugador) {
        long ahora = System.currentTimeMillis();

        if (ahora - ultimoTeletransporte >= INTERVALO_TELETRANSPORTE_MS) {
            if (intentarTeletransporte(mapa, jugador)) {
                ultimoTeletransporte = ahora;
                return;
            }
        }
        super.actualizarMovimiento(mapa, jugador);
    }

private boolean intentarTeletransporte(Mapa mapa, Personaje jugador) {
    for (int intento = 0; intento < 30; intento++) {
        int nx = random.nextInt(mapa.getAncho());
        int ny = random.nextInt(mapa.getAlto());

        ContenidoCelda contenido = mapa.getContenido(nx, ny);

        if (contenido != null && (contenido.bloqueaPaso() || contenido == jugador)) {
            continue;
        }

        moverEnemigo(nx, ny, mapa); // reutiliza el metodo de Enemigo
        return true;
    }
    return false;
}

}