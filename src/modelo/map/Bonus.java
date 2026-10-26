package src.modelo.map;
//imports para la imagen del bonus
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bonus extends ObjetoEntorno {

    public int bonusVelocidad = 2; // Aumenta la velocidad del personaje
    public long duracionMs = 5000; // Duración del bonus en milisegundos
    protected  Image imagenBonus;

    

    public Bonus(int bonusVelocidad, int duracionMs, int nivel) {
        super(true); // llama al constructor de la clase padre (ObjetoEntorno) y envia true osea que se puede pisar y tmb recoger
        this.bonusVelocidad = bonusVelocidad;
        this.duracionMs = duracionMs;
         try {
        // PROBAMOS CON ESTUDIANTE PERO HAY Q HACER EL CASE O NOSE VEREMOS
       switch (nivel) {
            case 1:
                imagenBonus = ImageIO.read(new File("images/bonus_estudiante.png"));
                break;
            case 2:
                imagenBonus = ImageIO.read(new File("images/bonus_adulto.png"));
                break;
            case 3:
                imagenBonus = ImageIO.read(new File("images/bonus_anciano.png"));
                break;
            default:
        imagenBonus = ImageIO.read(new File("images/bonus_adulto.png"));
        }
    } catch (IOException e) {
        e.printStackTrace();
        System.out.println("No se pudo cargar la imagen del bonus");
    }
    }
    public Image getImagenBonus() {
        return imagenBonus;
    }
}