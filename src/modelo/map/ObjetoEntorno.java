package src.modelo.map;
// el polimorfismo se aplica en el metodo bloqueaPaso(), que cada subclase puede sobrescribir
// para indicar si el objeto bloquea el paso, asi el personaje puede preguntar a cualquier
// ContenidoCelda si bloquea el paso sin conocer su tipo concreto.

public class ObjetoEntorno implements ContenidoCelda {
    private boolean pasoLibre;
    
    public ObjetoEntorno(boolean pasoLibre) { //con esta funcion verificamos si el 
    // personaje puede atravezar la celda ocupada por ese objeto
        this.pasoLibre = pasoLibre;
    }

    @Override
    public boolean bloqueaPaso() {
        return !this.pasoLibre;
    }

    public boolean isPasoLibre() { return pasoLibre; }

}