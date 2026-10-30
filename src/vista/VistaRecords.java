package src.vista;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import src.modelo.entities.Record;

// Ventana con estilo propio para mostrar los records de los jugadores.
// Se ve mucho mas "de juego" que un JOptionPane comun.
public class VistaRecords extends JDialog {

    // paleta de colores, siguiendo el estilo oscuro del juego
    private static final Color AMARILLO = new Color(255, 215, 0);
    private static final Color CYAN = new Color(0, 220, 220);
    private static final Color TEXTO = new Color(230, 230, 240);
    private static final Color FILA_PAR = new Color(55, 55, 75);
    private static final Color FILA_IMPAR = new Color(45, 45, 62);

    // colores de las medallas para el podio
    private static final Color ORO = new Color(255, 215, 0);
    private static final Color PLATA = new Color(200, 200, 210);
    private static final Color BRONCE = new Color(205, 127, 50);

    public VistaRecords(Window padre, List<Record> records) {
        super(padre, "Récords", Dialog.ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setSize(750, 650);
        setLocationRelativeTo(padre);

        // panel principal con fondo degradado y borde dorado
        JPanel principal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // fondo con gradiente sutil
                GradientPaint grad = new GradientPaint(0, 0, new Color(42, 42, 58),
                        0, getHeight(), new Color(18, 18, 28));
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                // borde dorado fino
                g2.setColor(new Color(140, 115, 40));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
                g2.dispose();
            }
        };
        principal.setOpaque(false);
        principal.setBorder(new EmptyBorder(22, 28, 22, 28));

        principal.add(crearTitulo(), BorderLayout.NORTH);
        principal.add(crearLista(records), BorderLayout.CENTER);
        principal.add(crearBotonCerrar(), BorderLayout.SOUTH);

        setContentPane(principal);

        // con ESC tambien se cierra, es mas comodo
        getRootPane().registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    // arma la parte de arriba con el titulo y subtitulo
    private JPanel crearTitulo() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(0, 0, 18, 0));

        JLabel titulo = new JLabel("   RECORDS   ");
        titulo.setFont(new Font("Arial", Font.BOLD, 30));
        titulo.setForeground(AMARILLO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Los mejores jugadores de Debt In Life");
        sub.setFont(new Font("Arial", Font.ITALIC, 13));
        sub.setForeground(new Color(170, 170, 190));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(4));
        panel.add(sub);

        return panel;
    }

    // arma el panel scrolleable con la lista de records
    private JScrollPane crearLista(List<Record> records) {
        JPanel contenedor = new JPanel();
        contenedor.setOpaque(false);
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));

        contenedor.add(crearCabecera());
        contenedor.add(Box.createVerticalStrut(8));

        if (records.isEmpty()) {
            JLabel vacio = new JLabel("Todavía no hay récords guardados.");
            vacio.setFont(new Font("Arial", Font.ITALIC, 16));
            vacio.setForeground(new Color(180, 180, 200));
            vacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            contenedor.add(Box.createVerticalStrut(40));
            contenedor.add(vacio);

            JLabel vacio2 = new JLabel("¡Sé el primero en jugar!");
            vacio2.setFont(new Font("Arial", Font.ITALIC, 14));
            vacio2.setForeground(new Color(140, 140, 160));
            vacio2.setAlignmentX(Component.CENTER_ALIGNMENT);
            contenedor.add(Box.createVerticalStrut(6));
            contenedor.add(vacio2);
        } else {
            int pos = 1;
            for (Record r : records) {
                contenedor.add(crearFila(r, pos));
                contenedor.add(Box.createVerticalStrut(5));
                pos++;
            }
        }

        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        return scroll;
    }

    // fila con los titulos de las columnas
    private JPanel crearCabecera() {
        JPanel cab = new JPanel(new GridLayout(1, 4)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(60, 60, 90));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
            }
        };
        cab.setOpaque(false);
        cab.setBorder(new EmptyBorder(12, 15, 12, 15));
        cab.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        String[] headers = {"#", "JUGADOR", "TIEMPO", "NIVEL"};
        for (String h : headers) {
            JLabel l = new JLabel(h, SwingConstants.CENTER);
            l.setFont(new Font("Arial", Font.BOLD, 14));
            l.setForeground(CYAN);
            cab.add(l);
        }
        return cab;
    }

    // fila individual con los datos de un record
    private JPanel crearFila(Record r, int posicion) {
        final Color fondo = (posicion % 2 == 0) ? FILA_PAR : FILA_IMPAR;

        JPanel fila = new JPanel(new GridLayout(1, 4)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(fondo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
            }
        };
        fila.setOpaque(false);
        fila.setBorder(new EmptyBorder(10, 15, 10, 15));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // en los primeros 3 puestos usamos color de medalla y emoji
        Color colorFila = TEXTO;
        String textoPos = String.valueOf(posicion);
        if (posicion == 1) { colorFila = ORO; textoPos = "1"; }
        else if (posicion == 2) { colorFila = PLATA; textoPos = "2"; }
        else if (posicion == 3) { colorFila = BRONCE; textoPos = "3"; }

        // formateo del tiempo a mm:ss
        long segsTot = r.getTiempoMilisegundos() / 1000;
        long mins = segsTot / 60;
        long segs = segsTot % 60;
        String tiempo = String.format("%02d:%02d", mins, segs);

        JLabel lblPos = new JLabel(textoPos, SwingConstants.CENTER);
        lblPos.setFont(new Font("Arial", Font.BOLD, 16));
        lblPos.setForeground(colorFila);

        JLabel lblNombre = new JLabel(r.getNombreJugador(), SwingConstants.CENTER);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 15));
        lblNombre.setForeground(colorFila);

        JLabel lblTiempo = new JLabel(tiempo, SwingConstants.CENTER);
        lblTiempo.setFont(new Font("Arial", Font.BOLD, 15));
        lblTiempo.setForeground(colorFila);

        JLabel lblNivel = new JLabel("Nivel " + r.getNivelAlcanzado(), SwingConstants.CENTER);
        lblNivel.setFont(new Font("Arial", Font.BOLD, 15));
        lblNivel.setForeground(colorFila);

        fila.add(lblPos);
        fila.add(lblNombre);
        fila.add(lblTiempo);
        fila.add(lblNivel);

        return fila;
    }

    // boton rojo para cerrar la ventana, con efecto hover
    private JPanel crearBotonCerrar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(18, 0, 0, 0));

        JButton btnCerrar = new JButton("Cerrar") {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setBorderPainted(false);
                setFocusPainted(false);
                setOpaque(false);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) { hover = true; repaint(); }
                    public void mouseExited(java.awt.event.MouseEvent e) { hover = false; repaint(); }
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
}