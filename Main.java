import src.controlador.ControladorMenu;
import src.vista.VistaMenu;

public class Main {
    public static void main(String[] args) {
        VistaMenu vistaMenu = new VistaMenu();
        ControladorMenu controladorMenu = new ControladorMenu(vistaMenu);
        controladorMenu.Iniciar();
    }
}