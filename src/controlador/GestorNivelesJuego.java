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

        SwingWorker<NivelCreado, Void> worker = new SwingWorker<NivelCreado, Void>() {
            @Override
            protected NivelCreado doInBackground() throws Exception {
                return creador.crearNivel(creador.getSpawnX(), creador.getSpawnY());
            }

            @Override
            protected void done() {
                try {
                    NivelCreado resultado = get();
                    controlador.cambiarNivel(resultado.mapa(), resultado.jugador(), nivel);
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