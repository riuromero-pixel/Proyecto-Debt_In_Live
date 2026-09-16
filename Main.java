import src.entities.Adulto;
import src.entities.Personaje;
import src.map.Mapa;
import src.vista.VistaJuego;
import src.controlador.ControladorJuego;

public class Main {
    public static void main(String[] args) {
        // 1) MODELO
        Mapa mapa = new Mapa(15, 10);
        mapa.cargarNivel(1);

        Personaje jugador = new Adulto(5, 5, mapa);
        mapa.setPersonaje(jugador, 5, 5);

        // 2) VISTA
        VistaJuego vista = new VistaJuego(mapa, jugador, 1);

        // 3) CONTROLADOR (recibe vista y modelo por constructor)
        ControladorJuego controlador = new ControladorJuego(vista, mapa, jugador);

        // 4) Mostrar la ventana
        vista.mostrar();
    }
}