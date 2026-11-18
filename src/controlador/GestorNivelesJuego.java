package src.controlador;

import javax.swing.*;
import src.modelo.entities.*;
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

        SwingWorker<PreparadorNivel.NivelListo, Void> worker = new SwingWorker<>() {

            @Override
            protected PreparadorNivel.NivelListo doInBackground() {
                return new PreparadorNivel(controlador.getReproductorAudio()).preparar(nivel);
            }

            @Override
            protected void done() {
                try {
                    PreparadorNivel.NivelListo listo = get();
                    controlador.cambiarNivel(
                        listo.nivel().mapa(),
                        listo.nivel().jugador(),
                        nivel,
                        listo.imagenes(),
                        listo.musica()
                        );
                } catch (Exception ex) {
                    cargando.dispose();
                    VistaDialogoEstilo.mostrarMensaje(controlador.getVista(), "Error", "", "Error al cargar el nivel: " + ex.getMessage());
                    ex.printStackTrace();
                }finally {
                    cargando.dispose();
                }
            }
        };

        worker.execute();
        cargando.setVisible(true);
    }
}