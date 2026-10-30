package src.vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class VistaCargando extends JDialog {

    private static final Color AMARILLO = new Color(255, 215, 0);
    private static final Color TEXTO_SEC = new Color(170, 170, 190);

    private JLabel lblMensaje;
    private JProgressBar barra;

    public VistaCargando(Window padre, String mensaje) {
        super(padre, "Cargando", Dialog.ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setSize(620, 360);
        setLocationRelativeTo(padre);

        // panel principal
        JPanel principal = new JPanel(new BorderLayout(0, 12)) { //mismo estilo que las otras vistas
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint grad = new GradientPaint(0, 0, new Color(42, 42, 58),
                        0, getHeight(), new Color(18, 18, 28));
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(140, 115, 40));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
                g2.dispose();
            }
        };
        principal.setOpaque(false);
        principal.setBorder(new EmptyBorder(22, 28, 22, 28));

        principal.add(crearTitulo(), BorderLayout.NORTH);
        principal.add(crearContenido(mensaje), BorderLayout.CENTER);

        setContentPane(principal);
    }
    private JPanel crearTitulo() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("   CARGANDO   ");
        titulo.setFont(new Font("Arial", Font.BOLD, 30));
        titulo.setForeground(AMARILLO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Cargando siguiente nivel...");
        sub.setFont(new Font("Arial", Font.ITALIC, 13));
        sub.setForeground(TEXTO_SEC);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(4));
        panel.add(sub);
        return panel;
    }
    private JPanel crearContenido(String mensaje) {
        JPanel cont = new JPanel();
        cont.setOpaque(false);
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
        cont.setBorder(new EmptyBorder(30, 20, 20, 20));

        lblMensaje = new JLabel(mensaje, SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 15));
        lblMensaje.setForeground(new Color(230, 230, 240));
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        barra = new JProgressBar();
        barra.setIndeterminate(true);
        barra.setBorderPainted(false);
        barra.setBackground(new Color(30, 30, 40));
        barra.setForeground(AMARILLO);
        barra.setPreferredSize(new Dimension(420, 14));
        barra.setMaximumSize(new Dimension(420, 14));
        barra.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel ayuda = new JLabel(
            "<html><div style='text-align:center; width:400px;'>"
            + "Esto puede tardar unos segundos."
            + "</div></html>");
        ayuda.setFont(new Font("Arial", Font.PLAIN, 12));
        ayuda.setForeground(TEXTO_SEC);
        ayuda.setAlignmentX(Component.CENTER_ALIGNMENT);

        cont.add(Box.createVerticalGlue());
        cont.add(lblMensaje);
        cont.add(Box.createVerticalStrut(20));
        cont.add(barra);
        cont.add(Box.createVerticalStrut(18));
        cont.add(ayuda);
        cont.add(Box.createVerticalGlue());

        return cont;
    }
    //permite actualizar el texto mientras "carga"
    public void setMensaje(String mensaje) {
        if (lblMensaje != null) lblMensaje.setText(mensaje);
    }
}