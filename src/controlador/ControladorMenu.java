package src.controlador;

import src.controlador.ControladorMenu;
import src.modelo.entities.Personaje;
import src.modelo.entities.PersonajeFactory;
import src.modelo.map.Mapa;
import src.modelo.map.Reproductor_audio;
import src.vista.VistaJuego;
import src.vista.VistaMenu;
import src.vista.VistaIntro;

public class ControladorMenu {
    private final VistaMenu vistaMenu;

    public ControladorMenu(VistaMenu vistaMenu) {
        this.vistaMenu = vistaMenu;
        this.vistaMenu.setAccionJugar(e -> iniciarJuego());
    }

    public void Iniciar(){
        new VistaIntro(()->{
            vistaMenu.mostrar();
        });
    }


    private void iniciarJuego() {

        // 1) MODELO inicial (Nivel 1 siempre es el Estudiante/Adolescente)
        Mapa mapa = new Mapa(40 ,40);
        mapa.cargarNivel(1);
        mapa.generarDinero();
        Reproductor_audio.getInstancia().reproducirSonido(1); // Arranca con la música del Nivel 1
        
        Personaje jugador = PersonajeFactory.crearPersonaje(1, 5, 5, mapa);

        // 2) VISTA
        VistaJuego vista = new VistaJuego(mapa, jugador, 1);

        // 3) CONTROLADOR (El controlador se encarga de la transición de niveles)
        ControladorJuego controlador = new ControladorJuego(vista, mapa, jugador);

        controlador.setAccionVolverMenu(() -> {
            //se vuelve a crear el menu en el caso de querer volver
            VistaMenu nuevoMenu = new VistaMenu();
            new ControladorMenu(nuevoMenu);
            nuevoMenu.mostrar();
        });

        // 4) Mostrar
        vistaMenu.dispose(); 
        vista.mostrar();
    }
}
