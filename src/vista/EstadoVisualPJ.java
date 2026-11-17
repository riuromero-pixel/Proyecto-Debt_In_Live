package src.vista;

public class EstadoVisualPJ {
    private double posXVisual;
    private double posYVisual;
    private static final double FACTOR_INTERPOLACION = 0.2;
    private static final double UMBRAL_SNAP = 0.01;
    private int frameAnimacion = 0;
    private long ultimoFrameAnimacion = 0;
    private boolean enMovimiento = false;
    private String direccionVisual = "S";
    private static final long MS_POR_FRAME = 100;
    private static final int TOTAL_FRAMES_CAMINATA = 22;

    public EstadoVisualPJ(int xLogica, int yLogica) {
        this.posXVisual = xLogica;
        this.posYVisual = yLogica;
    }

    public void actualizarPosicionVisual(int xLogica, int yLogica) {
        posXVisual += (xLogica - posXVisual) * FACTOR_INTERPOLACION;
        posYVisual += (yLogica - posYVisual) * FACTOR_INTERPOLACION;

        if (Math.abs(posXVisual - xLogica) < UMBRAL_SNAP) {
            posXVisual = xLogica;
        }
        if (Math.abs(posYVisual - yLogica) < UMBRAL_SNAP) {
            posYVisual = yLogica;
        }
    }

    public void actualizarAnimacion(boolean enMovimientoActual) {
        this.enMovimiento = enMovimientoActual;

        if (!enMovimiento) {
            // En idle volvemos siempre al primer frame.
            this.frameAnimacion = 0;
            return;
        }

        long ahora = System.currentTimeMillis();
        if (ahora - ultimoFrameAnimacion > MS_POR_FRAME) {
            frameAnimacion++;
            if (frameAnimacion >= TOTAL_FRAMES_CAMINATA) {
                frameAnimacion = 0;
            }
            ultimoFrameAnimacion = ahora;
        }
    }

    public void actualizarDireccionDesdeTecla(char direccionLogica) {
        switch (Character.toUpperCase(direccionLogica)) {
            case 'W': this.direccionVisual = "N"; break;
            case 'S': this.direccionVisual = "S"; break;
            case 'A': this.direccionVisual = "O"; break;
            case 'D': this.direccionVisual = "E"; break;
            default: /* no cambia */ break;
        }
    }

    public void setDireccionVisual(String direccionVisual) {
        if (direccionVisual != null) {
            this.direccionVisual = direccionVisual;
        }
    }

    public void resetearAnimacion() {
        this.frameAnimacion = 0;
        this.enMovimiento = false;
        this.ultimoFrameAnimacion = 0;
    }

    public void reiniciarPosicionVisual(int xLogica, int yLogica) {
        this.posXVisual = xLogica;
        this.posYVisual = yLogica;
    }

    // GETTERS
    public double getPosXVisual() { return posXVisual; }
    public double getPosYVisual() { return posYVisual; }
    public int getFrameAnimacion() { return frameAnimacion; }
    public String getDireccionVisual() { return direccionVisual; }
    public boolean isEnMovimiento() { return enMovimiento; }
}

