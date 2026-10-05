package src.vista;

import java.awt.*;
import javax.swing.*;
import src.controlador.ControladorJuego;
import src.entities.Estudiante;
import src.entities.Personaje;
import src.map.Mapa;

public class VistaMenu extends JFrame {

    // MODIFICADO: Reemplazamos la variable de volumen por una booleana de silencio
    private boolean silenciado = false;

    public VistaMenu() {
        setTitle("Debt In Live - Menú");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setAlwaysOnTop(true);

        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JLabel titulo = new JLabel("DEBT IN LIVE");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));

        JButton btnJugar = new JButton("Jugar");
        JButton btnOpciones = new JButton("Opciones");
        JButton btnComoJugar = new JButton("Cómo Jugar");
        JButton btnSalir = new JButton("Salir");

        btnJugar.addActionListener(e -> {
            dispose();
            iniciarJuego();
        });

        btnOpciones.addActionListener(e -> mostrarOpciones());
        btnComoJugar.addActionListener(e -> mostrarComoJugar());
        btnSalir.addActionListener(e -> System.exit(0));

        add(titulo);
        add(btnJugar);
        add(btnOpciones);
        add(btnComoJugar);
        add(btnSalir);
    }

    // MODIFICADO: Se usa un JDialog con BoxLayout para que se vea ordenado y prolijo
    private void mostrarOpciones() {
        JDialog dialogo = new JDialog(this, "Opciones", true);
        dialogo.setSize(300, 180);
        dialogo.setLocationRelativeTo(this);
        dialogo.setResizable(false);
        dialogo.setLayout(new BoxLayout(dialogo.getContentPane(), BoxLayout.Y_AXIS));

        // Etiqueta del estado del sonido
        JLabel label = new JLabel("Estado del sonido: " + (silenciado ? "Silenciado" : "Activado"));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        // Boton para alternar el mute
        JButton btnMute = new JButton(silenciado ? "Activar Sonido" : "Silenciar");
        btnMute.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnMute.addActionListener(e -> {
            silenciado = !silenciado;
            label.setText("Estado del sonido: " + (silenciado ? "Silenciado" : "Activado"));
            btnMute.setText(silenciado ? "Activar Sonido" : "Silenciar");
        });

        // Boton Aceptar para cerrar
        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAceptar.addActionListener(e -> dialogo.dispose());

        // Espaciado entre componentes
        dialogo.add(Box.createVerticalStrut(20));
        dialogo.add(label);
        dialogo.add(Box.createVerticalStrut(15));
        dialogo.add(btnMute);
        dialogo.add(Box.createVerticalStrut(15));
        dialogo.add(btnAceptar);
        dialogo.add(Box.createVerticalStrut(10));

        dialogo.setVisible(true);
    }

    private void mostrarComoJugar() {
        String mensaje = "Instrucciones:\n" +
                "- Usa W, A, S, D para moverte.\n" +
                "- Recolecta dinero ($) para aumentar tu puntaje.\n" +
                "- Evita a los enemigos (V), te restarán dinero.\n" +
                "- Presiona R para romper paredes cercanas.\n" +
                "- Presiona C para crear paredes (según personaje).\n" +
                "- Completa los 3 niveles para ganar el juego.\n" +
                "- Pierdes si tu dinero baja de 0.";
        JOptionPane.showMessageDialog(this, mensaje, "Cómo Jugar", JOptionPane.INFORMATION_MESSAGE);
    }

    private void iniciarJuego() {
        // 1) MODELO inicial (Nivel 1 siempre es el Estudiante/Adolescente)
        Mapa mapa = new Mapa(25, 25);
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
        vista.mostrar();
    }

    public void mostrar() {
        setVisible(true);
    }
}