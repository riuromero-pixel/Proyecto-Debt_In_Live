package src.modelo.map;
import src.modelo.entities.Personaje;

public class ObjetoEntorno {
    public boolean pasoLibre;
    protected boolean esRecolectable;
    
    public ObjetoEntorno(boolean pasoLibre, boolean esRecolectable) { //con esta funcion verificamos si el 
    // personaje puede atravezar la celda ocupada por ese objeto
        this.pasoLibre = pasoLibre;
        this.esRecolectable = esRecolectable;
    }
    public void recolectar(Personaje personaje) { //metodo que se sobreescribe en los hijos de esta clase, Dinero y Bonus.
    }
    public boolean esRecolectable() {
        return esRecolectable;
    }
}