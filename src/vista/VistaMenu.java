package src.vista;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class VistaMenu extends JFrame {

    // BORRADO: La variable "silenciado" se quitó 

    // NUEVO: Variable para guardar la conexión con el Controlador para el botón de silenciar
    private ActionListener accionSilenciar;

    private JButton btnJugar;
    private JButton btnRecords; 
    private JButton btnComoJugar; 
    private JButton btnOpciones; 

    
    private JPanel panelFondo;

    
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
        btnOpciones = crearBoton("Opciones"); 
        btnComoJugar = crearBoton("Como Jugar"); 
        JButton btnSalir = crearBoton("Salir");
        btnRecords = crearBoton("Récords"); 


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
        JButton boton = new JButton(texto) {
            private Image imagen = new ImageIcon("images/sprites/plata.png").getImage();
            private boolean hover = false;
            private int anchoImagen = 360;
            private int altoImagen = 130;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                });
                setContentAreaFilled(false);
                setBorderPainted(false);
                setFocusPainted(false);
                setOpaque(false);
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                int imgX = (getWidth() - anchoImagen) / 2;
                int imgY = (getHeight() - altoImagen) / 2;
                g2.drawImage(imagen, imgX, imgY, anchoImagen, altoImagen, this);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                if (hover) {
                    g2.setFont(new Font("Arial", Font.BOLD, 22));
                    g2.setColor(Color.YELLOW);
                } else {
                    g2.setFont(new Font("Arial", Font.BOLD, 18));
                    g2.setColor(Color.BLACK);
                }
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(200, 60));
        boton.setPreferredSize(new Dimension(200, 60));
        boton.setFont(new Font("Arial", Font.BOLD, 18));
        boton.setFocusPainted(false);
        return boton;
    }

    public void setAccionJugar(ActionListener listener) {
        btnJugar.addActionListener(listener);
    }
      public void setAccionRecords(ActionListener listener) {
        btnRecords.addActionListener(listener);
    }
    public void setAccionOpciones(ActionListener listener) {
        btnOpciones.addActionListener(listener);
    }

    public void setAccionComoJugar(ActionListener listener) {
        btnComoJugar.addActionListener(listener);
    }
// NUEVO: Listener para el botón silenciar, que delega la acción al controlador
    public void setAccionSilenciar(ActionListener listener) {
        this.accionSilenciar = listener;
    }
    // MODIFICADO: Se usa un JDialog con BoxLayout para que se vea ordenado y prolijo
    

    

    public void mostrar() {
        GraphicsDevice gd = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice();
            gd.setFullScreenWindow(this);
            setVisible(true);
        
    }
}