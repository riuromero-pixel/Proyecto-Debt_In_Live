import javax.swing.SwingUtilities;
import src.controlador.ControladorMenu;
import src.vista.VistaMenu;

public class Main {
    public static void main(String[] args) { // Punto de entrada del programa, donde se inicia la aplicación y se muestra el menú principal
        SwingUtilities.invokeLater(() -> {
        VistaMenu vistaMenu = new VistaMenu();
        ControladorMenu controladorMenu = new ControladorMenu(vistaMenu);
        controladorMenu.Iniciar();
        });
    }
}