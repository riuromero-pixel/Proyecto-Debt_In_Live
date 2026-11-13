package src.modelo.map;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import src.modelo.entities.Personaje;

public class Bonus extends ObjetoEntorno {

    private int bonusVelocidad = 2;
    private long duracionMs = 5000;
    private  Image imagenBonus;

    public Bonus(int bonusVelocidad, int duracionMs, int nivel) {
        super(true);
        this.bonusVelocidad = bonusVelocidad;
        this.duracionMs = duracionMs;
         try {
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

    public int getBonusVelocidad() { return bonusVelocidad; }
    public long getDuracionMs() { return duracionMs; }
    public Image getImagenBonus() { return imagenBonus; }

    @Override
    public boolean recolectar(Personaje personaje) {
        personaje.activarBonus(this);
        return true;
    }

    @Override
    public String getTipoVisual() {
        return "BONUS";
    }
}