import javax.swing.SwingUtilities;

import src.controlador.ControladorMenu;
import src.vista.VistaMenu;

public class Main {
    public static void main(String[] args) { // Punto de entrada del programa, donde se inicia la aplicación y se muestra el menú principal
        System.setProperty("sun.java2d.uiScale.enabled", "true");
        System.setProperty("sun.java2d.uiScale", "1.0");
        SwingUtilities.invokeLater(() -> {
        VistaMenu vistaMenu = new VistaMenu();
        ControladorMenu controladorMenu = new ControladorMenu(vistaMenu);
        controladorMenu.Iniciar();
        });
    }
}