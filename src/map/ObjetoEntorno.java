package src.map;
//este es un ejemplo de la aplicacion de polimorfismo ya q En laejecución, el programa reconoce la forma específica de cada objeto 
// (mediante 'instanceof' o respondiendo a sus propios métodos/atributos heredados
// como 'pasoLibre'), evitando tener que crear arreglos separados para cada elemento del juego.

public class ObjetoEntorno {
    public boolean pasoLibre;
    
    public ObjetoEntorno(boolean pasoLibre) { //con esta funcion verificamos si el 
    // personaje puede atravezar la celda ocupada por ese objeto
        this.pasoLibre = pasoLibre;
    }
}