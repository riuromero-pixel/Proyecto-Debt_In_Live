package src.modelo.map;
import src.modelo.entities.Personaje;
import java.awt.Image;
public class Bonus extends ObjetoEntorno {

    public int bonusVelocidad = 2; // Aumenta la velocidad del personaje
    public long duracionMs = 5000; // Duración del bonus en milisegundos
    public int nivel = 1;
    public Image imagenBonus; 

    public Bonus(int bonusVelocidad, int duracionMs, int nivel) {
        super(true, true); // llama al constructor de la clase padre (ObjetoEntorno) y envia true osea que se puede pisar y tmb recoger
        this.bonusVelocidad = bonusVelocidad;
        this.duracionMs = duracionMs;
        this.nivel = nivel;
    } 
      public Image getImagenBonus() {
        return imagenBonus;
    }
      @Override
    public void recolectar(Personaje personaje) {
        personaje.activarBonus(this);
    }
    
         @Override
    public TipoObjetoEntorno getTipoObjeto() {
        return TipoObjetoEntorno.BONUS;
    }
}