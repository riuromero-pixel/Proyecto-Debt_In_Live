package src.controlador;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import src.modelo.entities.Record;
import src.vista.VistaRecords;
import src.vista.VistaComoJugar;
import src.vista.VistaDialogoEstilo;
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
import src.vista.VistaCargando;


public class ControladorMenu {
    private final VistaMenu vistaMenu;
    private RecordDAO recordDAO;
    private long tiempoInicioPartida;
    private java.awt.event.ActionListener accionSilenciarGlobal;

    public ControladorMenu(VistaMenu vistaMenu) { // Constructor que recibe la vista del menú y configura las acciones de los botones
        this.vistaMenu = vistaMenu;
        this.vistaMenu.setAccionJugar(e -> iniciarJuego());
        this.vistaMenu.setAccionRecords(e -> mostrarRecords());
        this.vistaMenu.setAccionComoJugar(e -> mostrarComoJugar());
        this.vistaMenu.setAccionOpciones(e -> mostrarOpciones());

    // conecta la acción del botón "Alternar Sonido" de la vista con el controlador.
        // El controlador es quien realiza la accion
        this.accionSilenciarGlobal = e -> {
    Reproductor_audio.getInstancia().alternarSilencio(3); 
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
    VistaIntro[] introRef = new VistaIntro[1];
    //esta parte es para quitar un poco el pequeño flash que hay entre la intro y el menu de inicio
    introRef[0] = new VistaIntro(() -> {
        vistaMenu.mostrar();
        Reproductor_audio.getInstancia().reproducirSonido(3);
        // usamos un invokeLater para esperar a que Swing termine de pintar TODOS los componentes del menú en la pantalla.
        SwingUtilities.invokeLater(() -> {
            if (introRef[0] != null) {
                // una vez que el menú está pintado, se saca la intro
                introRef[0].dispose(); 
            }
        });
    });
    }

    private void iniciarJuego() {
        // MODIFICADO: Usamos nuestra ventana con estilo en vez del JOptionPane
        String nombreJugador = VistaDialogoEstilo.pedirTexto(
                vistaMenu, 
                "Nuevo Record", 
                "Ingresá tu nombre", 
                "¿Cómo te llamás?"
        );

        if (nombreJugador == null) {
            return; // si cancela, no arranca el juego
        }
        if (nombreJugador.trim().isEmpty()) {
            nombreJugador = "Anónimo";
        }

        this.tiempoInicioPartida = System.currentTimeMillis(); 

        // 1) MODELO inicial (Nivel 1 siempre es el Estudiante/Adolescente)
        Mapa mapa = new Mapa(40 ,40);
        mapa.cargarNivel(1);
        mapa.generarDinero();
        Reproductor_audio.getInstancia().detenerSonido(); 

        Reproductor_audio.getInstancia().reproducirSonido(1); 
        
        Personaje jugador = PersonajeFactory.crearPersonaje(1, 5, 5, mapa);

        // 2) VISTA
        VistaJuego vista = new VistaJuego(mapa, jugador, 1);

        // 3) CONTROLADOR 
        ControladorJuego controlador = new ControladorJuego(vista, mapa, jugador, this.tiempoInicioPartida, this.recordDAO, nombreJugador);

        controlador.setAccionVolverMenu(() -> {
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

    // usamos la ventana propia con un estilo
    VistaRecords ventana = new VistaRecords(vistaMenu, records);
    ventana.setVisible(true);
}

private void mostrarComoJugar() {
    // ventana propia con un estilo
    VistaComoJugar ventana = new VistaComoJugar(vistaMenu);
    ventana.setVisible(true);
}

private void mostrarOpciones() {
    VistaOpciones ventana = new VistaOpciones(vistaMenu, accionSilenciarGlobal);
    ventana.setVisible(true);
}

}
