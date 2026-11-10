package src.modelo.map;
import src.modelo.entities.Personaje;
public class Bonus extends ObjetoEntorno {

    public int bonusVelocidad = 2; // Aumenta la velocidad del personaje
    public long duracionMs = 5000; // Duración del bonus en milisegundos
    public int nivel = 1;

    public Bonus(int bonusVelocidad, int duracionMs, int nivel) {
        super(true, true); // llama al constructor de la clase padre (ObjetoEntorno) y envia true osea que se puede pisar y tmb recoger
        this.bonusVelocidad = bonusVelocidad;
        this.duracionMs = duracionMs;
        this.nivel = nivel;
    }
    public void recolectar(Personaje personaje) {
        personaje.activarBonus(this);
    }
}