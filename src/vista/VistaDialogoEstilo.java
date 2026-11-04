package src.vista;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// Clase reutilizable para mostrar dialogos con el mismo estilo oscuro/dorado del juego.
public class VistaDialogoEstilo extends JDialog {

    private int resultadoOpcion = -1;
    private String resultadoTexto = null;

    private VistaDialogoEstilo(Window padre, String titulo, String subtitulo) {
        super(padre, titulo, ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setLocationRelativeTo(padre);

        // Panel principal con el mismo fondo degradado y borde dorado
        JPanel principal = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint grad = new GradientPaint(0, 0, new Color(42, 42, 58), 0, getHeight(), new Color(18, 18, 28));
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(140, 115, 40));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
                g2.dispose();
            }
        };
        principal.setOpaque(false);
        principal.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Titulo y Subtitulo
        JPanel panelTitulo = new JPanel();
        panelTitulo.setOpaque(false);
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(255, 215, 0));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTitulo.add(lblTitulo);

        if (subtitulo != null && !subtitulo.isEmpty()) {
            JLabel lblSub = new JLabel(subtitulo);
            lblSub.setFont(new Font("Arial", Font.ITALIC, 13));
            lblSub.setForeground(new Color(170, 170, 190));
            lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelTitulo.add(Box.createVerticalStrut(4));
            panelTitulo.add(lblSub);
        }

        principal.add(panelTitulo, BorderLayout.NORTH);
        setContentPane(principal);

        // ESC para cerrar
        getRootPane().registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    // 1) PARA MOSTRAR SOLO UN MENSAJE (Niveles / Ganar)
    public static void mostrarMensaje(Window padre, String titulo, String subtitulo, String mensaje) {
        VistaDialogoEstilo dialogo = new VistaDialogoEstilo(padre, titulo, subtitulo);
        JPanel cont = dialogo.crearPanelMensaje(mensaje);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botones.setOpaque(false);
        JButton btnAceptar = dialogo.crearBoton("Aceptar", new Color(80, 160, 90), new Color(100, 185, 110));
        btnAceptar.addActionListener(e -> dialogo.dispose());
        botones.add(btnAceptar);

        ((JPanel)dialogo.getContentPane()).add(cont, BorderLayout.CENTER);
        ((JPanel)dialogo.getContentPane()).add(botones, BorderLayout.SOUTH);

        dialogo.pack();
        if (dialogo.getWidth() < 450) dialogo.setSize(450, dialogo.getHeight());
        dialogo.setLocationRelativeTo(padre);
        dialogo.setVisible(true);
    }

    // 2) PARA MOSTRAR OPCIONES (Pausa / Perder)
    // Devuelve el indice del boton clickeado (0, 1, 2...)
    public static int mostrarOpciones(Window padre, String titulo, String subtitulo, String mensaje, String[] opciones) {
        VistaDialogoEstilo dialogo = new VistaDialogoEstilo(padre, titulo, subtitulo);
        JPanel cont = dialogo.crearPanelMensaje(mensaje);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        botones.setOpaque(false);

        for (int i = 0; i < opciones.length; i++) {
            final int index = i;
            // Si la palabra es "Salir" o "Menú", lo pintamos rojo
            boolean esSalir = opciones[i].toLowerCase().contains("salir") || opciones[i].toLowerCase().contains("menú");
            Color cBase = esSalir ? new Color(200, 70, 70) : new Color(80, 140, 200);
            Color cHover = esSalir ? new Color(220, 90, 90) : new Color(100, 160, 220);

            JButton btn = dialogo.crearBoton(opciones[i], cBase, cHover);
            btn.addActionListener(e -> {
                dialogo.resultadoOpcion = index;
                dialogo.dispose();
            });
            botones.add(btn);
        }

        ((JPanel)dialogo.getContentPane()).add(cont, BorderLayout.CENTER);
        ((JPanel)dialogo.getContentPane()).add(botones, BorderLayout.SOUTH);

        dialogo.pack();
        if (dialogo.getWidth() < 550) dialogo.setSize(550, dialogo.getHeight());
        dialogo.setLocationRelativeTo(padre);
        dialogo.setVisible(true);

        return dialogo.resultadoOpcion;
    }

    // 3) PARA PEDIR UN TEXTO (Ingresar Nombre)
    // Devuelve el String ingresado o null si cancela
    public static String pedirTexto(Window padre, String titulo, String subtitulo, String mensaje) {
        VistaDialogoEstilo dialogo = new VistaDialogoEstilo(padre, titulo, subtitulo);

        JPanel cont = new JPanel();
        cont.setOpaque(false);
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));

        JLabel lblMsg = new JLabel(mensaje);
        lblMsg.setFont(new Font("Arial", Font.PLAIN, 15));
        lblMsg.setForeground(new Color(230, 230, 240));
        lblMsg.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField txtInput = new JTextField();
        txtInput.setFont(new Font("Arial", Font.PLAIN, 16));
        txtInput.setMaximumSize(new Dimension(300, 38));
        txtInput.setPreferredSize(new Dimension(300, 38));
        txtInput.setBackground(new Color(60, 60, 80));
        txtInput.setForeground(Color.WHITE);
        txtInput.setCaretColor(Color.WHITE);
        txtInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(140, 115, 40), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtInput.setAlignmentX(Component.CENTER_ALIGNMENT);

        cont.add(lblMsg);
        cont.add(Box.createVerticalStrut(15));
        cont.add(txtInput);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        botones.setOpaque(false);

        JButton btnAceptar = dialogo.crearBoton("Aceptar", new Color(80, 160, 90), new Color(100, 185, 110));
        JButton btnCancel = dialogo.crearBoton("Cancelar", new Color(200, 70, 70), new Color(220, 90, 90));

        btnAceptar.addActionListener(e -> {
            dialogo.resultadoTexto = txtInput.getText();
            dialogo.dispose();
        });
        btnCancel.addActionListener(e -> {
            dialogo.resultadoTexto = null;
            dialogo.dispose();
        });

        botones.add(btnAceptar);
        botones.add(btnCancel);

        ((JPanel)dialogo.getContentPane()).add(cont, BorderLayout.CENTER);
        ((JPanel)dialogo.getContentPane()).add(botones, BorderLayout.SOUTH);

        dialogo.pack();
        if (dialogo.getWidth() < 450) dialogo.setSize(450, dialogo.getHeight());
        dialogo.setLocationRelativeTo(padre);
        dialogo.setVisible(true);

        return dialogo.resultadoTexto;
    }

    // --- HELPERS (Para no repetir codigo) ---

    private JPanel crearPanelMensaje(String mensaje) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 10, 20, 10));
        // Usamos HTML para que el texto haga salto de linea solo si es muy largo
        JLabel lbl = new JLabel("<html><div style='text-align: center; width: 400px;'>" + mensaje.replace("\n", "<br>") + "</div></html>");
        lbl.setFont(new Font("Arial", Font.PLAIN, 15));
        lbl.setForeground(new Color(230, 230, 240));
        panel.add(lbl);
        return panel;
    }

    private JButton crearBoton(String texto, Color colorBase, Color colorHover) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isRollover() ? colorHover : colorBase;
                GradientPaint grad = new GradientPaint(0, 0, c.brighter(), 0, getHeight(), c.darker());
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(c.brighter());
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 15));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(130, 42));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}