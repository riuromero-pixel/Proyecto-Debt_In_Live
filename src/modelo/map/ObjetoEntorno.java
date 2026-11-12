package src.modelo.map;
// este es un ejemplo de la aplicacion de polimorfismo ya q En la ejecución, el programa reconoce la forma específica de cada objeto 
// (mediante 'instanceof' o respondiendo a sus propios métodos/atributos heredados
// como 'pasoLibre'), evitando tener que crear arreglos separados para cada elemento del juego.

public class ObjetoEntorno {
    private boolean pasoLibre;
    
    public ObjetoEntorno(boolean pasoLibre) { //con esta funcion verificamos si el 
    // personaje puede atravezar la celda ocupada por ese objeto
        this.pasoLibre = pasoLibre;
    }

    public boolean isPasoLibre() { return pasoLibre; }

}