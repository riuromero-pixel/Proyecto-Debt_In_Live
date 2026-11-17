package src.modelo.map;
import java.io.File;
import javax.sound.sampled.*;

public class Reproductor_audio {
    private static Reproductor_audio instanciaUnica;
    private Clip clipActual;
    private boolean silenciado = false;//atributo para controlar el silencio

private Reproductor_audio() {
        // Constructor privado para evitar instanciación externa
    }

    public static synchronized Reproductor_audio getInstancia() {
        if (instanciaUnica == null) {
            instanciaUnica = new Reproductor_audio();
        }
        return instanciaUnica;
    }
    
    public Clip cargarSonido(int nivel) {
        if (silenciado) return null;

        String nombreArchivo = switch (nivel) {
            case 1 -> "images/Movimiento-Naranja-NES-FC.wav";
            case 2 -> "images/Homero_Chiptune_Master.wav";
            case 3 -> "images/Tango-in-8-bits-por-una-cabeza-NES-famitracker-EliKomponistaSaturn.wav";
            default -> null;
        };
        if (nombreArchivo == null) return null;

        try {
            File archivoAudio = new File(nombreArchivo);
            if (!archivoAudio.exists()) {
                System.out.println("Archivo de audio no encontrado: " + archivoAudio.getAbsolutePath());
                return null;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivoAudio);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void reproducirClip(Clip clip) {
        if (silenciado || clip == null) return;
        detenerSonido();
        clipActual = clip;
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public boolean isSilenciado() {
    return silenciado;
}

    // método para alternar el silencio
    public void alternarSilencio(int nivelReanudado) {
    this.silenciado = !this.silenciado;
    if (this.silenciado) {
        detenerSonido(); 
    } else {
        reproducirSonido(nivelReanudado);   
    }
}
    public void reproducirSonido(int nivel) {
    reproducirClip(cargarSonido(nivel));
}

    // metodo para frenar el clip actual de forma segura
    public void detenerSonido() {
        if (clipActual != null && clipActual.isRunning()) {
            clipActual.stop();
            clipActual.close();
        }
    }
}
 