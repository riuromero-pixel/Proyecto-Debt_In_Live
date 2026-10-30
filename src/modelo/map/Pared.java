package src.modelo.map;
import java.util.Random;

public class Pared extends ObjetoEntorno {
    private int variante;

    public Pared() {
        super(false); // llama al constructor de la clase padre (ObjetoEntorno) y envia false osea que no se puede pisar en esa celda
        Random rand = new Random();
        if (rand.nextInt(100) < 50) {
            this.variante = 0;
        } else {
            this.variante = 1 + rand.nextInt(4);
        }
    }

    public Pared(int varianteElegida) {
        super(false);
        this.variante = varianteElegida;
    }
    public int getVariante() {
        return variante;
    }
}