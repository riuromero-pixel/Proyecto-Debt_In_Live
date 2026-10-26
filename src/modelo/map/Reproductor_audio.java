package src.modelo.map;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Reproductor_audio {
    private static Reproductor_audio instanciaUnica;
    private Clip clipActual;
    // NUEVO: atributo para controlar el silencio
    private boolean silenciado = false;
    
private Reproductor_audio() {
        // Constructor privado para evitar instanciación externa
    }
    //solo una instancia
public static Reproductor_audio getInstancia() {
        if (instanciaUnica == null) {
            instanciaUnica = new Reproductor_audio();
        }
        return instanciaUnica;
    }
    // NUEVO: Método para alternar el silencio
    public void alternarSilencio() {
    this.silenciado = !this.silenciado;
    
    if (this.silenciado) {
        detenerSonido(); // Llamá al método que ya tenés para frenar la música
    } else {
        reproducirSonido(1); // Llamá al método para volver a darle play
    }
}
    public void reproducirSonido(int nivel) {
        detenerSonido(); // Se asegura de cortar la música anterior antes de arrancar otra

        String nombreArchivo = "";
        switch (nivel) {
            case 1:
                nombreArchivo = "images/Movimiento-Naranja-NES-FC.wav";
                break;
            case 2:
            
                break;
            case 3:
                nombreArchivo = "images/Tango-in-8-bits-por-una-cabeza-NES-famitracker-EliKomponistaSaturn.wav";
                break;
            default:
                return;
        }

        try {
            File archivoAudio = new File(nombreArchivo);
            if (archivoAudio.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivoAudio);
                clipActual = AudioSystem.getClip();
                clipActual.open(audioStream);
                clipActual.start();
                clipActual.loop(Clip.LOOP_CONTINUOUSLY); // Reproducción en bucle
            } else {
                System.out.println("Archivo de audio no encontrado en: " + archivoAudio.getAbsolutePath());
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Método para frenar el clip actual de forma segura
    public void detenerSonido() {
        if (clipActual != null && clipActual.isRunning()) {
            clipActual.stop();
            clipActual.close();
        }
    }
}
 