package src.modelo.map;

public class Bonus extends ObjetoEntorno {

    public int bonusVelocidad = 2; // Aumenta la velocidad del personaje
    public long duracionMs = 5000; // Duración del bonus en milisegundos

    public Bonus(int bonusVelocidad, int duracionMs) {
        super(true); // llama al constructor de la clase padre (ObjetoEntorno) y envia true osea que se puede pisar y tmb recoger
        this.bonusVelocidad = bonusVelocidad;
        this.duracionMs = duracionMs;
    }
}