import src.controlador.ControladorMenu;
import src.vista.VistaMenu;

public class Main {
    public static void main(String[] args) {
        VistaMenu vistaMenu = new VistaMenu();
        new ControladorMenu(vistaMenu);
        vistaMenu.mostrar();
    }
}