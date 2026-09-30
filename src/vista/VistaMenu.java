package src.vista;

import src.entities.Adulto;
import src.entities.Estudiante;
import src.entities.Jubilado;
import src.entities.Personaje;
import src.map.Mapa;
import src.controlador.ControladorJuego;

import javax.swing.*;
import java.awt.*;

public class VistaMenu extends JFrame {

    private int volumen = 50; // 0 a 100
    private String tipoPersonaje = "Adulto"; // default

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
        JButton btnCambiarPersonaje = new JButton("Cambiar Personaje");
        JButton btnSalir = new JButton("Salir");

        btnJugar.addActionListener(e -> {
            dispose();
            iniciarJuego();
        });

        btnOpciones.addActionListener(e -> mostrarOpciones());
        btnComoJugar.addActionListener(e -> mostrarComoJugar());
        btnCambiarPersonaje.addActionListener(e -> cambiarPersonaje());
        btnSalir.addActionListener(e -> System.exit(0));

        add(titulo);
        add(btnJugar);
        add(btnOpciones);
        add(btnComoJugar);
        add(btnCambiarPersonaje);
        add(btnSalir);
    }

    private void mostrarOpciones() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Volumen actual: " + volumen + "%");
        JButton btnBajar = new JButton("Bajar Volumen (-10)");

        btnBajar.addActionListener(e -> {
            if (volumen >= 10) {
                volumen -= 10;
            } else {
                volumen = 0;
            }
            label.setText("Volumen actual: " + volumen + "%");
        });

        panel.add(label);
        panel.add(btnBajar);

        JOptionPane.showMessageDialog(this, panel, "Opciones", JOptionPane.PLAIN_MESSAGE);
    }

    private void mostrarComoJugar() {
        String mensaje = "Instrucciones:\n" +
                "- Usa W, A, S, D para moverte.\n" +
                "- Recolecta dinero ($) para aumentar tu puntaje.\n" +
                "- Evita a los enemigos (V), te restarán dinero.\n" +
                "- Presiona R para romper paredes cercanas.\n" +
                "- Presiona C para crear paredes (según personaje).\n" +
                "- Gana recolectando todo el dinero.\n" +
                "- Perdes si tu dinero baja de 0.";
        JOptionPane.showMessageDialog(this, mensaje, "Cómo Jugar", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cambiarPersonaje() {
        String[] opciones = {"Estudiante", "Adulto", "Jubilado"};
        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona tu personaje:",
                "Cambiar Personaje",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                tipoPersonaje
        );

        if (seleccion != null) {
            tipoPersonaje = seleccion;
            JOptionPane.showMessageDialog(this, "Personaje cambiado a: " + tipoPersonaje);
        }
    }

    private void iniciarJuego() {
        // 1) MODELO
        Mapa mapa = new Mapa(15, 10);
        mapa.cargarNivel(1);

        // Crear el personaje según la selección
        Personaje jugador;
        switch (tipoPersonaje) {
            case "Estudiante":
                jugador = new Estudiante(5, 5, mapa);
                break;
            case "Jubilado":
                jugador = new Jubilado(5, 5, mapa);
                break;
            default:
                jugador = new Adulto(5, 5, mapa);
                break;
        }
        mapa.setPersonaje(jugador, 5, 5);

        // 2) VISTA
        VistaJuego vista = new VistaJuego(mapa, jugador, 1);

        // 3) CONTROLADOR
        ControladorJuego controlador = new ControladorJuego(vista, mapa, jugador);

        // 4) Mostrar
        vista.mostrar();
    }

    public void mostrar() {
        setVisible(true);
    }
}