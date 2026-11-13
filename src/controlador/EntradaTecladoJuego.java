package src.controlador;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EntradaTecladoJuego extends KeyAdapter {

    private final ControladorJuego controlador;

    public EntradaTecladoJuego(ControladorJuego controlador) {
        this.controlador = controlador;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            controlador.mostrarPausa();
            return;
        }

        char tecla = e.getKeyChar();

        switch (tecla) {
            case 'w': case 'W': controlador.setTeclaPulsada('W'); break;
            case 's': case 'S': controlador.setTeclaPulsada('S'); break;
            case 'a': case 'A': controlador.setTeclaPulsada('A'); break;
            case 'd': case 'D': controlador.setTeclaPulsada('D'); break;

            case 'r': case 'R':
                controlador.getJugador().romperParedes();
                controlador.getVista().repaint();
                break;

            case 'c': case 'C':
                if (controlador.getJugador().puedeCrearParedes()) {
                    controlador.getJugador().crearParedes();
                    controlador.getVista().repaint();
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        char tecla = e.getKeyChar();
        if (Character.toUpperCase(tecla) == controlador.getTeclaPulsada()) {
            controlador.setTeclaPulsada(' ');
        }
    }
}