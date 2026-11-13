package src.modelo.map;
import java.util.Random;

public class Pared extends ObjetoEntorno {
    private int variante;
    private int golpesRestantes = 2;

    public Pared() {
        super(false);
        Random rand = new Random();
        this.variante = 1 + rand.nextInt(4);
    }

    public Pared(int varianteElegida) {
        super(false);
        this.variante = varianteElegida;
    }
    public int getVariante() {
        return variante;
    }

    @Override
    public boolean recibirGolpe() {
        golpesRestantes--;
        return golpesRestantes <= 0;
    }

    @Override
    public boolean esGolpeable() {
        return true;
    }

    @Override
    public String getTipoVisual() {
        return "PARED";
    }
}