package src.controlador;

import javax.sound.sampled.Clip;
import src.modelo.entities.*;
import src.modelo.map.Reproductor_audio;
import src.vista.CargadorImagenesNivel;
import src.vista.GestorSprites;

public class PreparadorNivel {

    public record NivelListo(NivelCreado nivel, CargadorImagenesNivel imagenes, Clip musica) {}

    private final Reproductor_audio audio;

    public PreparadorNivel(Reproductor_audio audio) {
        this.audio = audio;
    }

    public NivelListo preparar(int numero) {
        CreadorNivel creador = RegistroNiveles.paraNivel(numero);
        NivelCreado nivel = creador.crearNivel(creador.getSpawnX(), creador.getSpawnY());

        CargadorImagenesNivel imagenes = new CargadorImagenesNivel();
        imagenes.cargar(numero);
        GestorSprites.getInstance().precargarPersonajePara(nivel.jugador().getTipoSprite());
        if (RegistroNiveles.esUltimoNivel(numero)) {
            GestorSprites.getInstance().precargarEnemigoLetal();
        }
        Clip musica = audio.cargarSonido(numero);

        return new NivelListo(nivel, imagenes, musica);
    }
}