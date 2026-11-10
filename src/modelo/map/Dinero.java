package src.modelo.map;
import src.modelo.entities.Personaje;

public class Dinero extends ObjetoEntorno {
    public int cantidad;
    
    public Dinero(int cantidad) {
        super(true, true); // llama al constructor de la clase padre (ObjetoEntorno) y envia true osea que se puede pisar y tmb recoger
        this.cantidad = cantidad; //el dinero que asigna al recoger
    }
    public void recolectar(Personaje personaje) {
        personaje.recolectarDinero(this.cantidad);
    }
}