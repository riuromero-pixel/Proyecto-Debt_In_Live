package src.map;

public class Dinero extends ObjetoEntorno {
    public int cantidad;
    
    public Dinero(int cantidad) {
        super(true); // llama al constructor de la clase padre (ObjetoEntorno) y envia true osea que se puede pisar y tmb recoger
        this.cantidad = cantidad; //el dinero que asigna al recoger
    }
}