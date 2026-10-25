package src.vista;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class VistaMenu extends JFrame {

    // MODIFICADO: Reemplazamos la variable de volumen por una booleana de silencio
    private boolean silenciado = false;
    private JButton btnJugar;
    private JButton btnRecords; //boton para ver records


    // NUEVO: panel con la imagen de fondo a pantalla completa
    private JPanel panelFondo;

    // NUEVO: ruta de la imagen de fondo del menú (actualizada a .jpeg)
    private static final String RUTA_IMAGEN_FONDO = "images/menu.jpeg";

    public VistaMenu() {
        setTitle("Debt In Life - Menú");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // NUEVO: ventana sin decoración para que la imagen llegue a los bordes
        setUndecorated(true);

        // NUEVO: pantalla completa real (ocupa toda la pantalla, sin barra de tareas)
        GraphicsDevice gd = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();

        // NUEVO: fondo oscuro por si la imagen no carga
        getContentPane().setBackground(new Color(30, 30, 40));

        // NUEVO: panel con imagen de fondo que cubre toda la ventana
        panelFondo = new JPanel() {
            private Image imagenFondo;

            // NUEVO: bloque de inicialización para cargar la imagen una sola vez
            {
                try {
                    ImageIcon icono = new ImageIcon(RUTA_IMAGEN_FONDO);
                    if (icono.getIconWidth() > 0) {
                        imagenFondo = icono.getImage();
                    } else {
                        System.out.println("ADVERTENCIA: No se encontró la imagen 'images/menu.jpeg'");
                    }
                } catch (Exception ex) {
                    imagenFondo = null;
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // NUEVO: si la imagen cargó, se escala para cubrir toda la pantalla
                if (imagenFondo != null) {
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        // NUEVO: layout para poder ubicar el panel de botones en una esquina
        panelFondo.setLayout(new GridBagLayout());
        panelFondo.setOpaque(true);
        panelFondo.setBackground(new Color(30, 30, 40));
        setContentPane(panelFondo);

        // panel de botones uno abajo del otro, con fondo negro semitransparente
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(true);
        panelBotones.setBackground(new Color(0, 0, 0, 160));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        btnJugar = crearBoton("Jugar");
        JButton btnOpciones = crearBoton("Opciones");
        JButton btnComoJugar = crearBoton("Cómo Jugar");
        JButton btnSalir = crearBoton("Salir");
        btnRecords = crearBoton("Récords"); 


        btnOpciones.addActionListener(e -> mostrarOpciones());
        btnComoJugar.addActionListener(e -> mostrarComoJugar());
        btnSalir.addActionListener(e -> System.exit(0));

        panelBotones.add(btnJugar);
        panelBotones.add(Box.createVerticalStrut(15));
        panelBotones.add(btnOpciones);
        panelBotones.add(Box.createVerticalStrut(15));
        panelBotones.add(btnComoJugar);
        panelBotones.add(Box.createVerticalStrut(15));
        panelBotones.add(btnRecords); 
        panelBotones.add(Box.createVerticalStrut(15));
        panelBotones.add(btnSalir);

        // NUEVO: Configuramos los constraints para que el panel quede en la esquina inferior izquierda
        // Así no tapa al doctor, al empleado ni al cartel de la imagen.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTHWEST; // Esquina inferior izquierda
        gbc.insets = new Insets(0, 50, 50, 0); // 50px de margen desde el borde izquierdo y abajo
        
        panelFondo.add(panelBotones, gbc);
    }

    // creo los botones con el mismo estilo
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(180, 50));
        boton.setPreferredSize(new Dimension(180, 50));
        boton.setFont(new Font("Arial", Font.BOLD, 18));
        boton.setFocusPainted(false);
        boton.setBackground(new Color(255, 220, 50));
        boton.setForeground(Color.BLACK);
        boton.setOpaque(true);
        return boton;
    }

    public void setAccionJugar(ActionListener listener) {
        btnJugar.addActionListener(listener);
    }
      public void setAccionRecords(ActionListener listener) {
        btnRecords.addActionListener(listener);
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
                "- Pierdes si tu dinero baja de 0." +
                "- Ganas o aumentas de nivel si llegas a recolectar $3000.";
        JOptionPane.showMessageDialog(this, mensaje, "Cómo Jugar", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrar() {
        GraphicsDevice gd = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice();
            gd.setFullScreenWindow(this);
            setVisible(true);
        
    }
}