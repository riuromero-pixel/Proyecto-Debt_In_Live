package src.controlador;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import src.modelo.entities.Record;
import src.vista.VistaRecords;
import src.vista.VistaCargando;
import src.vista.VistaComoJugar;
import src.vista.VistaDialogoEstilo;
import src.vista.VistaOpciones;

import src.modelo.entities.NivelCreado;
import src.modelo.entities.Personaje;
import src.modelo.entities.RecordDAO;
import src.modelo.entities.RegistroNiveles;
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
        // Usamos nuestra ventana con estilo en vez del JOptionPane
        String nombreJugador = VistaDialogoEstilo.pedirTexto(
                vistaMenu, 
                "Nuevo Record", 
                "Ingresá tu nombre", 
                "¿Cómo te llamás?"
        );
        if (nombreJugador == null) {
            return;
        }
        if (nombreJugador.trim().isEmpty()) {
            nombreJugador = "Anonimo";
        }
        final String nombreJugadorFinal = nombreJugador; 

        this.tiempoInicioPartida = System.currentTimeMillis();
    // diálogo de carga
    final VistaCargando cargando = new VistaCargando(vistaMenu, "Generando nivel 1...");

    SwingWorker<NivelCreado, Void> worker = new SwingWorker<NivelCreado, Void>() {

        @Override
        protected NivelCreado doInBackground() throws Exception {
            return RegistroNiveles.paraNivel(1).crearNivel(4, 4);
        }

        @Override
        protected void done() {
            try {
                NivelCreado resultado = get();
                Mapa mapa = resultado.mapa();
                Personaje jugador = resultado.jugador();

                Reproductor_audio.getInstancia().reproducirSonido(1);

                VistaJuego vista = new VistaJuego(mapa, jugador, 1);
                vista.setTiempoInicioPartida(tiempoInicioPartida);
                ControladorJuego controlador = new ControladorJuego(
                    vista, mapa, jugador, tiempoInicioPartida, recordDAO, nombreJugadorFinal
                );

                controlador.setAccionVolverMenu(() -> {
                    VistaMenu nuevoMenu = new VistaMenu();
                    new ControladorMenu(nuevoMenu);
                    nuevoMenu.mostrar();
                    Reproductor_audio.getInstancia().reproducirSonido(3);
                });

                // orden anti-parpadeo: 
                cargando.dispose(); //cerrar carga
                vista.mostrar();//mostrar juego
                vistaMenu.dispose(); //cerrar menú

            } catch (Exception ex) {
                cargando.dispose();
                JOptionPane.showMessageDialog(vistaMenu,
                    "Error al cargar el nivel: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    };
    worker.execute();
    cargando.setVisible(true); 
}


    private void mostrarRecords() {
    if (this.recordDAO == null) {
        JOptionPane.showMessageDialog(vistaMenu,
                "Error: Base de datos no conectada.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    }

    new SwingWorker<java.util.List<Record>, Void>() {
        @Override
        protected java.util.List<Record> doInBackground() {
            // HILO DE FONDO: consulta a SQLite.
            return recordDAO.obtenerMejores();
        }

        @Override
        protected void done() {
            // EDT: crear y mostrar la ventana.
            try {
                java.util.List<Record> records = get();
                VistaRecords ventana = new VistaRecords(vistaMenu, records);
                ventana.setVisible(true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (java.util.concurrent.ExecutionException e) {
                JOptionPane.showMessageDialog(vistaMenu,
                        "Error al leer los récords.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }.execute();
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
