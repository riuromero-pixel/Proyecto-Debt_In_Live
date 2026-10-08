package src.controlador;

import src.modelo.entities.Estudiante;
import src.modelo.entities.Personaje;
import src.modelo.map.Mapa;
import src.vista.VistaJuego;
import src.vista.VistaMenu;

public class ControladorMenu {
    private final VistaMenu vistaMenu;

    public ControladorMenu(VistaMenu vistaMenu) {
        this.vistaMenu = vistaMenu;
        this.vistaMenu.setAccionJugar(e -> iniciarJuego());
    }

    private void iniciarJuego() {
        // 1) MODELO inicial (Nivel 1 siempre es el Estudiante/Adolescente)
        Mapa mapa = new Mapa(40 ,40);
        mapa.cargarNivel(1);
        mapa.generarDinero();
        
        Personaje jugador = new Estudiante(5, 5, mapa);
        mapa.celdas[5][5].contenido = null; // Limpiar por si las dudas
        mapa.setPersonaje(jugador, 5, 5);

        // 2) VISTA
        VistaJuego vista = new VistaJuego(mapa, jugador, 1);

        // 3) CONTROLADOR (El controlador se encarga de la transición de niveles)
        ControladorJuego controlador = new ControladorJuego(vista, mapa, jugador);

        // 4) Mostrar
        vistaMenu.dispose(); 
        vista.mostrar();
    }
}
