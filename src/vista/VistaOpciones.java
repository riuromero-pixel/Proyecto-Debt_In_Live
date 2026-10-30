package src.vista;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import src.modelo.map.Reproductor_audio;

// Ventana de opciones del juego. Mantiene la misma estetica oscura/dorada
// que las vistas de records y "como jugar".
public class VistaOpciones extends JDialog {

    // paleta igual a las otras vistas
    private static final Color AMARILLO = new Color(255, 215, 0);
    private static final Color TEXTO = new Color(230, 230, 240);
    private static final Color TEXTO_SEC = new Color(170, 170, 190);
    private static final Color VERDE = new Color(80, 200, 100);
    private static final Color ROJO = new Color(220, 80, 80);

    private final ActionListener accionSilenciar;

    // componentes que se actualizan segun el estado
    private JPanel panelIcono;
    private JLabel lblEstado;
    private JButton btnToggle;

    // animacion de pulso del icono
    private Timer timerPulso;
    private float fasePulso = 0f;

    public VistaOpciones(Window padre, ActionListener accionSilenciar) {
        super(padre, "Opciones", Dialog.ModalityType.APPLICATION_MODAL);
        this.accionSilenciar = accionSilenciar;
        setUndecorated(true);
        setSize(620, 620);
        setLocationRelativeTo(padre);

        // panel principal con fondo degradado y borde dorado (igual que las otras vistas)
        JPanel principal = new JPanel(new BorderLayout(0, 12)) {
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
        principal.add(crearContenido(), BorderLayout.CENTER);
        principal.add(crearBotonCerrar(), BorderLayout.SOUTH);

        setContentPane(principal);

        // arrancamos la animacion del pulso
        iniciarPulso();

        // seteamos el estado inicial de los componentes
        actualizarEstado();

        // con ESC tambien se cierra
        getRootPane().registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    @Override
    public void dispose() {
        // frenamos el timer al cerrar para no dejar hilos colgados
        if (timerPulso != null) timerPulso.stop();
        super.dispose();
    }

    // ---------- TITULO ----------
    private JPanel crearTitulo() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("   OPCIONES   ");
        titulo.setFont(new Font("Arial", Font.BOLD, 30));
        titulo.setForeground(AMARILLO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Ajustes del juego");
        sub.setFont(new Font("Arial", Font.ITALIC, 13));
        sub.setForeground(TEXTO_SEC);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(4));
        panel.add(sub);
        return panel;
    }

    // ---------- CONTENIDO ----------
    private JPanel crearContenido() {
        JPanel cont = new JPanel();
        cont.setOpaque(false);
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
        cont.setBorder(new EmptyBorder(25, 20, 20, 20));

        panelIcono = crearPanelIcono();
        panelIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblEtiqueta = new JLabel("Música de fondo");
        lblEtiqueta.setFont(new Font("Arial", Font.BOLD, 20));
        lblEtiqueta.setForeground(TEXTO);
        lblEtiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblEstado = new JLabel(" ");
        lblEstado.setFont(new Font("Arial", Font.BOLD, 15));
        lblEstado.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnToggle = crearBotonToggle();
        btnToggle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAyuda = new JLabel("<html><div style='text-align: center; width: 400px;'>"
                + "La música se escucha de fondo mientras jugás. Si la silenciás, "
                + "no escucharás el audio del juego."
                + "</div></html>");
        lblAyuda.setFont(new Font("Arial", Font.PLAIN, 12));
        lblAyuda.setForeground(TEXTO_SEC);
        lblAyuda.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAyuda.setHorizontalAlignment(SwingConstants.CENTER);

        cont.add(panelIcono);
        cont.add(Box.createVerticalStrut(15));
        cont.add(lblEtiqueta);
        cont.add(Box.createVerticalStrut(8));
        cont.add(lblEstado);
        cont.add(Box.createVerticalStrut(25));
        cont.add(btnToggle);
        cont.add(Box.createVerticalStrut(25));
        cont.add(lblAyuda);

        return cont;
    }

    // circulo con el icono de altavoz, con pulso animado
    private JPanel crearPanelIcono() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                int radioBase = Math.min(getWidth(), getHeight()) / 2 - 18;

                boolean silenciado = estaSilenciado();
                Color colorPrincipal = silenciado ? ROJO : VERDE;

                // halo exterior que pulsa
                int radioHalo = radioBase + (int) (Math.sin(fasePulso) * 6);
                g2.setColor(new Color(colorPrincipal.getRed(), colorPrincipal.getGreen(), colorPrincipal.getBlue(), 55));
                g2.fillOval(cx - radioHalo, cy - radioHalo, radioHalo * 2, radioHalo * 2);

                // circulo principal con gradiente
                GradientPaint grad = new GradientPaint(
                        cx - radioBase, cy - radioBase, colorPrincipal.brighter(),
                        cx + radioBase, cy + radioBase, colorPrincipal.darker());
                g2.setPaint(grad);
                g2.fillOval(cx - radioBase, cy - radioBase, radioBase * 2, radioBase * 2);

                // borde
                g2.setColor(colorPrincipal.brighter());
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(cx - radioBase, cy - radioBase, radioBase * 2, radioBase * 2);

                // emoji del altavoz
                String simbolo = silenciado ? "🔇" : "🔊";
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 62));
                FontMetrics fm = g2.getFontMetrics();
                int tx = cx - fm.stringWidth(simbolo) / 2;
                int ty = cy + fm.getAscent() / 2 - 8;
                g2.setColor(Color.WHITE);
                g2.drawString(simbolo, tx, ty);

                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(180, 180));
        panel.setMaximumSize(new Dimension(180, 180));
        panel.setMinimumSize(new Dimension(180, 180));
        return panel;
    }

    // boton pill que alterna el sonido
    private JButton crearBotonToggle() {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean silenciado = estaSilenciado();
                Color base = silenciado ? new Color(80, 160, 90) : new Color(180, 70, 70);
                Color hover = silenciado ? new Color(100, 185, 110) : new Color(210, 90, 90);

                Color c = getModel().isRollover() ? hover : base;
                GradientPaint grad = new GradientPaint(0, 0, c.brighter(), 0, getHeight(), c.darker());
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());

                g2.setColor(c.brighter());
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                String texto = silenciado ? "    Activar Sonido" : "    Silenciar";
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(texto)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(texto, x, y);

                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(250, 55));
        btn.setMaximumSize(new Dimension(250, 55));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            // le delegamos la accion al controlador
            if (accionSilenciar != null) {
                accionSilenciar.actionPerformed(e);
            }
            // despues actualizamos los componentes visuales
            actualizarEstado();
            if (panelIcono != null) panelIcono.repaint();
            btn.repaint();
        });

        return btn;
    }

    // boton rojo para cerrar, igual que en las otras vistas
    private JPanel crearBotonCerrar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);

        JButton btnCerrar = new JButton("Cerrar") {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setBorderPainted(false);
                setFocusPainted(false);
                setOpaque(false);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c1 = hover ? new Color(220, 90, 90) : new Color(200, 70, 70);
                Color c2 = hover ? new Color(170, 50, 50) : new Color(150, 40, 40);
                GradientPaint grad = new GradientPaint(0, 0, c1, 0, getHeight(), c2);
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(255, 150, 150));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btnCerrar.setPreferredSize(new Dimension(150, 45));
        btnCerrar.addActionListener(e -> dispose());
        panel.add(btnCerrar);
        return panel;
    }

    // ---------- ESTADO Y ANIMACION ----------
    // lee el estado actual directamente del reproductor
    private boolean estaSilenciado() {
        return Reproductor_audio.getInstancia().isSilenciado();
    }

    // actualiza el texto y color del estado segun corresponda
    private void actualizarEstado() {
        boolean silenciado = estaSilenciado();
        if (lblEstado != null) {
            if (silenciado) {
                lblEstado.setText("●  Silenciada");
                lblEstado.setForeground(ROJO);
            } else {
                lblEstado.setText("●  Activada");
                lblEstado.setForeground(VERDE);
            }
        }
    }

    // animacion de pulso suave para el halo del icono
    private void iniciarPulso() {
        timerPulso = new Timer(50, e -> {
            fasePulso += 0.15f;
            if (panelIcono != null) panelIcono.repaint();
        });
        timerPulso.start();
    }
}