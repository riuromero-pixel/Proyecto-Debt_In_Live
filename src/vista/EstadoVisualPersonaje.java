package src.vista;

public class EstadoVisualPersonaje {

    private double posXVisual;
    private double posYVisual;
    private static final double FACTOR_INTERPOLACION = 0.2;

    private String direccionVisual = "S";
    private int frameAnimacion = 0;
    private long ultimoFrameAnimacion = 0;
    private boolean enMovimiento = false;

    public EstadoVisualPersonaje(int xInicial, int yInicial) {
        this.posXVisual = xInicial;
        this.posYVisual = yInicial;
    }

    public void actualizarPosicionVisual(int x, int y) {
        posXVisual += (x - posXVisual) * FACTOR_INTERPOLACION;
        posYVisual += (y - posYVisual) * FACTOR_INTERPOLACION;

        if (Math.abs(posXVisual - x) < 0.01) posXVisual = x;
        if (Math.abs(posYVisual - y) < 0.01) posYVisual = y;
    }

    public void actualizarAnimacion(boolean moviendo) {
        this.enMovimiento = moviendo;
        if (moviendo) {
            long ahora = System.currentTimeMillis();
            if (ahora - this.ultimoFrameAnimacion > 100) {
                this.frameAnimacion++;
                if (this.frameAnimacion >= 22) this.frameAnimacion = 0;
                this.ultimoFrameAnimacion = ahora;
            }
        } else {
            this.frameAnimacion = 0;
        }
    }

    public double getPosXVisual() { return posXVisual; }
    public double getPosYVisual() { return posYVisual; }
    public String getDireccionVisual() { return direccionVisual; }
    public void setDireccionVisual(String direccionVisual) { this.direccionVisual = direccionVisual; }
    public int getFrameAnimacion() { return frameAnimacion; }
    public boolean isEnMovimiento() { return enMovimiento; }
}