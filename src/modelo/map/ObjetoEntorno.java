package src.modelo.map;
// este es un ejemplo de la aplicacion de polimorfismo ya q En la ejecución, el programa reconoce la forma específica de cada objeto 
// (mediante 'instanceof' o respondiendo a sus propios métodos/atributos heredados
// como 'pasoLibre'), evitando tener que crear arreglos separados para cada elemento del juego.
import src.modelo.entities.Personaje;

public class ObjetoEntorno {
    public boolean pasoLibre;
    protected boolean esRecolectable;
    
    public ObjetoEntorno(boolean pasoLibre, boolean esRecolectable) { //con esta funcion verificamos si el 
    // personaje puede atravezar la celda ocupada por ese objeto
        this.pasoLibre = pasoLibre;
        this.esRecolectable = esRecolectable;
    }
     public void recolectar (Personaje personaje){

    } //se va a sobreescribir en las clases hijas
    
    public boolean esRecolectable() {
        return esRecolectable;
    }
}