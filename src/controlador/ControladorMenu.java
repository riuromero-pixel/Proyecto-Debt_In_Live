package src.controlador;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import src.modelo.entities.Record;
import src.vista.VistaRecords;
import src.vista.VistaComoJugar;
import src.vista.VistaOpciones;

import src.modelo.entities.Personaje;
import src.modelo.entities.PersonajeFactory;
import src.modelo.entities.RecordDAO;
import src.persistencia.RecordDAOSQLite;
import src.persistencia.Conexion;
import src.modelo.map.Mapa;
import src.modelo.map.Reproductor_audio;
import src.vista.VistaJuego;
import src.vista.VistaMenu;
import src.vista.VistaIntro;


public class ControladorMenu {
    private final VistaMenu vistaMenu;
    private RecordDAO recordDAO;
    private long tiempoInicioPartida;
    private java.awt.event.ActionListener accionSilenciarGlobal;



    public ControladorMenu(VistaMenu vistaMenu) {
        this.vistaMenu = vistaMenu;
        this.vistaMenu.setAccionJugar(e -> iniciarJuego());
        this.vistaMenu.setAccionRecords(e -> mostrarRecords());
        this.vistaMenu.setAccionComoJugar(e -> mostrarComoJugar());
        this.vistaMenu.setAccionOpciones(e -> mostrarOpciones());

    // NUEVO: Conectamos la acción del botón "Alternar Sonido" de la vista con el controlador.
        // El controlador es quien realiaz la accion
        this.accionSilenciarGlobal = e -> {
    Reproductor_audio.getInstancia().alternarSilencio(); 
};
this.vistaMenu.setAccionSilenciar(this.accionSilenciarGlobal);

        try {
        Connection conexion = Conexion.getInstancia().get();
        this.recordDAO = new RecordDAOSQLite(conexion);
    } catch (SQLException e) {
        System.out.println("Error al conectar con la base de datos");
    }
    }

    public void Iniciar(){
        new VistaIntro(()->{
            vistaMenu.mostrar();
            Reproductor_audio.getInstancia().reproducirSonido(3);
        });
    }


    private void iniciarJuego() {


        String nombreJugador = (String) JOptionPane.showInputDialog(
        vistaMenu,
        "Ingresa tu nombre:",
        "Nuevo Record",
        JOptionPane.PLAIN_MESSAGE,
        null,
        null, 
        ""    
         );
        if (nombreJugador == null) {
            return; // si cancela, no arranca el juego
        }
        if (nombreJugador.trim().isEmpty()) {
            nombreJugador = "Anónimo";
        }

        this.tiempoInicioPartida = System.currentTimeMillis(); // guarda el tiempo de inicio


        // 1) MODELO inicial (Nivel 1 siempre es el Estudiante/Adolescente)
        Mapa mapa = new Mapa(40 ,40);
        mapa.cargarNivel(1);
        mapa.generarDinero();
        Reproductor_audio.getInstancia().detenerSonido(); //detiene el sonido del menu

        Reproductor_audio.getInstancia().reproducirSonido(1); // Arranca con la música del Nivel 1
        
        Personaje jugador = PersonajeFactory.crearPersonaje(1, 5, 5, mapa);

        // 2) VISTA
        VistaJuego vista = new VistaJuego(mapa, jugador, 1);

        // 3) CONTROLADOR (El controlador se encarga de la transición de niveles)
        ControladorJuego controlador = new ControladorJuego(vista, mapa, jugador, this.tiempoInicioPartida, this.recordDAO, nombreJugador);

        controlador.setAccionVolverMenu(() -> {
            //se vuelve a crear el menu en el caso de querer volver
            VistaMenu nuevoMenu = new VistaMenu();
            new ControladorMenu(nuevoMenu);
            nuevoMenu.mostrar();
            Reproductor_audio.getInstancia().reproducirSonido(3); 

        });

        // 4) Mostrar
        vistaMenu.dispose(); 
        vista.mostrar();
    }

    private void mostrarRecords() {
    if (this.recordDAO == null) {
        JOptionPane.showMessageDialog(vistaMenu, "Error: Base de datos no conectada.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    java.util.List<Record> records = this.recordDAO.obtenerMejores();

    // ahora usamos la ventana propia con estilo, en vez del JOptionPane simple
    VistaRecords ventana = new VistaRecords(vistaMenu, records);
    ventana.setVisible(true);
}

private void mostrarComoJugar() {
    // ventana propia con estilo, en vez del JOptionPane simple
    VistaComoJugar ventana = new VistaComoJugar(vistaMenu);
    ventana.setVisible(true);
}

private void mostrarOpciones() {
    VistaOpciones ventana = new VistaOpciones(vistaMenu, accionSilenciarGlobal);
    ventana.setVisible(true);
}

}
