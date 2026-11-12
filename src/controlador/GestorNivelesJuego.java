package src.controlador;

import javax.swing.*;
import src.modelo.entities.*;
import src.modelo.map.Mapa;
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

        SwingWorker<Object[], Void> worker = new SwingWorker<Object[], Void>() {
            @Override
            protected Object[] doInBackground() throws Exception {
                Mapa nuevoMapa = new Mapa(40, 40);
                nuevoMapa.cargarNivel(nivel);
                nuevoMapa.generarDinero();

                Personaje nuevoJugador = PersonajeFactory.crearPersonaje(nivel, 5, 5, nuevoMapa);
                nuevoMapa.getCeldas()[5][5].setContenido(null);
                nuevoMapa.setPersonaje(nuevoJugador, 5, 5);

                return new Object[]{nuevoMapa, nuevoJugador};
            }

            @Override
            protected void done() {
                try {
                    Object[] modelo = get();
                    Mapa nuevoMapa = (Mapa) modelo[0];
                    Personaje nuevoJugador = (Personaje) modelo[1];

                    controlador.cambiarNivel(nuevoMapa, nuevoJugador, nivel);

                    cargando.dispose();
                } catch (Exception ex) {
                    cargando.dispose();
                    JOptionPane.showMessageDialog(controlador.getVista(),
                        "Error al cargar el nivel: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
        cargando.setVisible(true);
    }
}