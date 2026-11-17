package src.vista;

 /**
 * Encapsula el estado visual de un enemigo para la capa de Vista.
 * 
 * Responsabilidades:
 *  - Guardar las coordenadas visuales (double) para interpolación suave.
 */
public class EstadoVisualEnemigo {

    private double posXVisual;
    private double posYVisual;

    private static final double FACTOR_INTERPOLACION = 0.15;
    private static final double UMBRAL_SNAP = 0.01;


    public EstadoVisualEnemigo(int xLogica, int yLogica) {
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

  
    public double getPosXVisual() { return posXVisual; }
    public double getPosYVisual() { return posYVisual; }
}