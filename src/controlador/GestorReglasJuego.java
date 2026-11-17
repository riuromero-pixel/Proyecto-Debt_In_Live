package src.controlador;

import src.modelo.entities.RegistroNiveles;
import src.vista.VistaDialogoEstilo;

public class GestorReglasJuego {

    public void verificarPerdida(ControladorJuego controlador) {
        if (controlador.getJugador().getDinero() >= 0) return;
        if (!controlador.isJuegoActivo()) return;

            controlador.guardarRecordActual(controlador.getNivelActual());
            controlador.detener();
            controlador.getVista().mostrarGameOver();    

            String mensaje = RegistroNiveles.paraNivel(controlador.getNivelActual()).getMensajeDerrota();

            int opcion = VistaDialogoEstilo.mostrarOpciones(
                controlador.getVista(),
                "¡PERDISTE!",
                "¿Que hacemo?",
                mensaje,
                new String[]{"Reintentar", "Volver al Menú", "Salir"}
            );

            if (opcion == 0) {
                controlador.setNivelActual(1);
                controlador.iniciarNivel(1);
            } else if (opcion == 1) {
                controlador.getVista().dispose();
                controlador.volverAlMenu();
            } else {
                System.exit(0);
            }
        }
    

    public void ganarJuego(ControladorJuego controlador, String mensaje) {
        controlador.setJuegoActivo(false);
        controlador.detener(); 
        controlador.guardarRecordActual(3);

        long tiempoTotal = System.currentTimeMillis() - controlador.getTiempoInicioPartida();
        long segundosTotal = tiempoTotal / 1000;
        long minutos = segundosTotal / 60;
        long segundos = segundosTotal % 60;

        VistaDialogoEstilo.mostrarMensaje(
            controlador.getVista(),
            "¡GANASTE!",
            "Sobreviviste a las deudas de la vida",
            mensaje + "\n\n¡Tu tiempo total fue de: " + minutos + " minutos y " + segundos + " segundos!"
        );

        controlador.getVista().dispose();
        controlador.volverAlMenu();
    }

    public void mostrarPausa(ControladorJuego controlador) {
        controlador.setJuegoActivo(false);
        controlador.detener(); 

        int opcion = VistaDialogoEstilo.mostrarOpciones(
            controlador.getVista(),
            "Juego pausado",
            "Tomate un respiro",
            "¿Querés continuar jugando?",
            new String[]{"Continuar", "Volver al Menú", "Salir"}
        );

        if (opcion == 0) {
            controlador.setJuegoActivo(true);
            controlador.iniciarTimers();
            controlador.getVista().requestFocusInWindow();
        } else if (opcion == 1) {
            controlador.detener();
            controlador.getVista().dispose();
            controlador.volverAlMenu();
        } else {
            System.exit(0);
        }
    }
}