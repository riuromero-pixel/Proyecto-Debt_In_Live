package src.vista;

import src.entities.Adulto;
import src.entities.Personaje;
import src.map.Mapa;
import src.controlador.ControladorJuego;

import javax.swing.*;
import java.awt.*;

public class VistaMenu extends JFrame {

    public VistaMenu() {
        setTitle("Debt In Live - Menú");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setAlwaysOnTop(true);

        // FlowLayout centrado
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JLabel titulo = new JLabel("DEBT IN LIVE");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));

        JButton btnJugar = new JButton("Jugar");
        JButton btnSalir = new JButton("Salir");

        btnJugar.addActionListener(e -> {
            dispose();       // cierra el menú
            iniciarJuego();  // arranca el juego
        });

        btnSalir.addActionListener(e -> System.exit(0));

        add(titulo);
        add(btnJugar);
        add(btnSalir);
    }

    private void iniciarJuego() {
        // 1) MODELO
        Mapa mapa = new Mapa(15, 10);
        mapa.cargarNivel(1);

        Personaje jugador = new Adulto(5, 5, mapa);
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

