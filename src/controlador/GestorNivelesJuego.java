package src.controlador;

import javax.swing.*;
import src.modelo.entities.*;
import src.vista.VistaCargando;
import src.vista.VistaDialogoEstilo;

public class GestorNivelesJuego {

    public void iniciarNivel(ControladorJuego controlador, int nivel) {

        if (nivel == 2) {
            VistaDialogoEstilo.mostrarMensaje(controlador.getVista(), "¡NIVEL 2!", "Ahora sos un Adulto", "A agarrar la pala.");
        } else if (nivel == 3) {
            VistaDialogoEstilo.mostrarMensaje(controlador.getVista(), "¡NIVEL 3!", "Ahora sos un Jubilado", "Ahora cagaste.");
        }

        VistaCargando cargando = new VistaCargando(controlador.getVista(), "Generando nivel " + nivel + "...");

        SwingWorker<NivelCreado, Void> worker = new SwingWorker<NivelCreado, Void>() {
            @Override
            protected NivelCreado doInBackground() throws Exception {
                return RegistroNiveles.paraNivel(nivel).crearNivel(5, 5);
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