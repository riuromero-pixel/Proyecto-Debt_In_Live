package src.controlador;

import javax.sound.sampled.Clip;
import javax.swing.*;
import src.modelo.entities.*;
import src.vista.CargadorImagenesNivel;
import src.vista.GestorSprites;
import src.vista.VistaCargando;
import src.vista.VistaDialogoEstilo;

public class GestorNivelesJuego {

    public void iniciarNivel(ControladorJuego controlador, int nivel) {

        CreadorNivel creador = RegistroNiveles.paraNivel(nivel);

        String[] msg = creador.getMensajeInicio();
        if (msg != null) {
            VistaDialogoEstilo.mostrarMensaje(controlador.getVista(), msg[0], msg[1], msg[2]);
        }

        VistaCargando cargando = new VistaCargando(controlador.getVista(), "Generando nivel " + nivel + "...");

        SwingWorker<NivelCreado, Void> worker = new SwingWorker<>() {

            private Clip clipMusica;
            private CargadorImagenesNivel cargadorImagenes;

            @Override
            protected NivelCreado doInBackground() throws Exception {
                NivelCreado resultado = creador.crearNivel(creador.getSpawnX(), creador.getSpawnY());
                cargadorImagenes = new CargadorImagenesNivel();
                cargadorImagenes.cargar(nivel);
                GestorSprites.getInstance().precargarPersonajePara(resultado.jugador().getTipoSprite());
                if (nivel == 3) {
                    GestorSprites.getInstance().precargarEnemigoLetal();
                }
                clipMusica = controlador.getReproductorAudio().cargarSonido(nivel);

                return resultado;
            }

            @Override
            protected void done() {
                try {
                    NivelCreado resultado = get();
                    controlador.cambiarNivel(resultado.mapa(), resultado.jugador(), nivel, cargadorImagenes, clipMusica);
                    cargando.dispose();
                } catch (Exception ex) {
                    cargando.dispose();
                    VistaDialogoEstilo.mostrarMensaje(controlador.getVista(), "Error", "", "Error al cargar el nivel: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
        cargando.setVisible(true);
    }
}