package src.map;

public class ObjetoEntorno {
    public boolean pasoLibre;
    
    public ObjetoEntorno(boolean pasoLibre) { //con esta funcion verificamos si el personaje puede atravezar la celda ocupada por ese objeto
        this.pasoLibre = pasoLibre;
    }
}