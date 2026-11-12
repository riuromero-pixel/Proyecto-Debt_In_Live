package src.controlador;

import src.modelo.map.Reproductor_audio;
import src.vista.VistaDialogoEstilo;

public class GestorReglasJuego {

    public void verificarPerdida(ControladorJuego controlador) {
        if (controlador.getJugador().getDinero() < 0) {

            controlador.guardarRecordActual(controlador.getNivelActual());
            Reproductor_audio.getInstancia().detenerSonido();
            controlador.getVista().mostrarGameOver();
            controlador.detener();

            String mensaje;
            switch (controlador.getNivelActual()) {
                case 1: mensaje = "¡PERDISTE! Te quedaste sin plata. Chau play 5:(."; break;
                case 2: mensaje = "¡PERDISTE! Te quedaste sin plata. No llegaste a fin de mes. Pinchó mal."; break;
                case 3: mensaje = "¡PERDISTE! Te quedaste sin plata. Perdiste toda la plata de la jubilación. Cagamo."; break;
                default: mensaje = "¡PERDISTE! Te quedaste sin plata."; break;
            }

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
    }

    public void ganarJuego(ControladorJuego controlador, String mensaje) {
        controlador.setJuegoActivo(false);
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

        int opcion = VistaDialogoEstilo.mostrarOpciones(
            controlador.getVista(),
            "Juego pausado",
            "Tomate un respiro",
            "¿Querés continuar jugando?",
            new String[]{"Continuar", "Volver al Menú", "Salir"}
        );

        if (opcion == 0) {
            controlador.setJuegoActivo(true);
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