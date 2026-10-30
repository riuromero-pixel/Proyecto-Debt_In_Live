package src.vista;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// Ventana con estilo propio para explicar como se juega.
// Mantiene la misma estetica oscura/dorada que la vista de records.
public class VistaComoJugar extends JDialog {

    // paleta, igual que en VistaRecords
    private static final Color AMARILLO = new Color(255, 215, 0);
    private static final Color CYAN = new Color(0, 220, 220);
    private static final Color TEXTO = new Color(230, 230, 240);
    private static final Color TEXTO_SEC = new Color(170, 170, 190);

    // spritesheets que vamos a animar
    private SpriteSheet sheetEstudiante;
    private SpriteSheet sheetEnemigo;

    // animacion de los sprites
    private Timer timerAnimacion;
    private int frameAnimacion = 0;

    // control de slides
    private int slideActual = 0;
    private JLabel lblTituloSlide;
    private JLabel lblTextoSlide;
    private JPanel panelSprite;
    private JPanel panelDots;
    private JLabel lblContador;

    // cada slide tiene titulo, texto y si muestra al enemigo (true) o al jugador (false)
    private static class Slide {
        String titulo;
        String texto;
        boolean esEnemigo;
        Slide(String t, String tx, boolean e) { this.titulo = t; this.texto = tx; this.esEnemigo = e; }
    }

    // contenido de cada slide
    private static final Slide[] SLIDES = {
        new Slide("Moverse",
                "Usá las teclas <b>W, A, S y D</b> para desplazarte por el mapa. " +
                "Tu personaje se mueve de a una casilla por vez, así que pensá bien cada paso.",
                false),
        new Slide("Recolectar Dinero",
                "Juntá las monedas (<b>$</b>) que aparecen en el mapa. Cada una suma a tu contador. " +
                "¡Llegá a <b>$3000</b> para poder avanzar de nivel!",
                false),
        new Slide("Romper Paredes",
                "Presioná <b>R</b> para golpear las paredes que tengas al lado. " +
                "Con unos cuantos golpes se rompen y te abren nuevos caminos por el mapa.",
                false),
        new Slide("Crear Paredes",
                "Algunos personajes pueden crear paredes con la tecla <b>C</b>. " +
                "Son útiles para bloquear el paso de los enemigos o armar tu propio camino.",
                false),
        new Slide("Enemigos",
                "Los enemigos (<b>V</b>) rondan por el mapa y te quitan dinero si te tocan. " +
                "Algunos te persiguen y otros se mueven al azar. Y ojo con el enemigo rojo... " +
                "ese te mata de un toque. ¡Esquivá a todos!",
                true),
        new Slide("Objetivo",
                "Reuní <b>$3000</b> para avanzar de nivel. Vas a pasar por tres etapas: " +
                "Estudiante, Adulto y Jubilado. Cada nivel es más difícil que el anterior. " +
                "¡Llegá al final sin quedarte sin plata!",
                false)
    };

    public VistaComoJugar(Window padre) {
        super(padre, "Cómo Jugar", Dialog.ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setSize(880, 720);
        setLocationRelativeTo(padre);

        // panel principal con fondo degradado y borde dorado (igual que en records)
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
        principal.add(crearPanelInferior(), BorderLayout.SOUTH);

        setContentPane(principal);

        // cargamos sprites y arrancamos la animacion
        cargarSprites();
        iniciarTimerAnimacion();

        // mostramos el primer slide
        mostrarSlide(0);

        // con ESC se cierra
        getRootPane().registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        // con las flechas del teclado tambien se navega
        getRootPane().registerKeyboardAction(
                e -> anteriorSlide(),
                KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(
                e -> siguienteSlide(),
                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        // frenamos el timer al cerrar para no dejar hilos colgados
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { dispose(); }
        });
    }

    @Override
    public void dispose() {
        if (timerAnimacion != null) timerAnimacion.stop();
        super.dispose();
    }

    // ---------- TITULO ----------
    private JPanel crearTitulo() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("   CÓMO JUGAR   ");
        titulo.setFont(new Font("Arial", Font.BOLD, 30));
        titulo.setForeground(AMARILLO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Guía rápida para sobrevivir en Debt In Life");
        sub.setFont(new Font("Arial", Font.ITALIC, 13));
        sub.setForeground(TEXTO_SEC);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(4));
        panel.add(sub);
        return panel;
    }

    // ---------- CONTENIDO (sprite + texto) ----------
    private JPanel crearContenido() {
        JPanel cont = new JPanel(new BorderLayout(25, 0));
        cont.setOpaque(false);
        cont.setBorder(new EmptyBorder(20, 10, 20, 10));

        panelSprite = crearPanelSprite();
        cont.add(panelSprite, BorderLayout.WEST);
        cont.add(crearPanelTexto(), BorderLayout.CENTER);
        return cont;
    }

    // cuadradito donde se ve el sprite animandose
    private JPanel crearPanelSprite() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();

                // fondo con gradiente sutil
                GradientPaint grad = new GradientPaint(0, 0, new Color(35, 35, 50),
                        0, h, new Color(18, 18, 28));
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, w, h, 20, 20);

                // borde dorado
                g2.setColor(new Color(140, 115, 40));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, w - 2, h - 2, 20, 20);

                // obtenemos el frame actual y lo dibujamos centrado
                Slide s = SLIDES[slideActual];
                Image frame = getFrameActual(s.esEnemigo);
                if (frame != null) {
                    int margen = 30;
                    int ladoMax = Math.min(w, h) - margen * 2;
                    int imgW = frame.getWidth(null);
                    int imgH = frame.getHeight(null);
                    double escala = (double) ladoMax / Math.max(imgW, imgH);
                    int drawW = (int) (imgW * escala);
                    int drawH = (int) (imgH * escala);
                    int dx = (w - drawW) / 2;
                    int dy = (h - drawH) / 2;
                    g2.drawImage(frame, dx, dy, drawW, drawH, null);
                } else {
                    // por si no carga la imagen, mostramos un cuadrado de color
                    g2.setColor(s.esEnemigo ? new Color(200, 50, 50) : new Color(60, 120, 200));
                    int size = Math.min(w, h) - 80;
                    int dx = (w - size) / 2;
                    int dy = (h - size) / 2;
                    g2.fillRoundRect(dx, dy, size, size, 15, 15);
                }
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(300, 300));
        return panel;
    }

    // panel con el titulo y el texto del slide actual
    private JPanel crearPanelTexto() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 0, 0, 0));

        lblTituloSlide = new JLabel(" ");
        lblTituloSlide.setFont(new Font("Arial", Font.BOLD, 26));
        lblTituloSlide.setForeground(AMARILLO);
        lblTituloSlide.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblTextoSlide = new JLabel(" ");
        lblTextoSlide.setFont(new Font("Arial", Font.PLAIN, 15));
        lblTextoSlide.setForeground(TEXTO);
        lblTextoSlide.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblTituloSlide);
        panel.add(Box.createVerticalStrut(18));
        panel.add(lblTextoSlide);
        return panel;
    }

    // ---------- PANEL INFERIOR (flechas, dots, contador, cerrar) ----------
    private JPanel crearPanelInferior() {
        JPanel bottom = new JPanel(new BorderLayout(0, 15));
        bottom.setOpaque(false);

        // navegacion: flecha izq | dots + contador | flecha der
        JPanel nav = new JPanel(new BorderLayout(10, 0));
        nav.setOpaque(false);

        JButton btnPrev = crearBotonFlecha("<-");
        JButton btnNext = crearBotonFlecha("->");
        btnPrev.addActionListener(e -> anteriorSlide());
        btnNext.addActionListener(e -> siguienteSlide());

        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        panelDots = crearPanelDots();
        panelDots.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblContador = new JLabel("1 / " + SLIDES.length);
        lblContador.setFont(new Font("Arial", Font.PLAIN, 12));
        lblContador.setForeground(TEXTO_SEC);
        lblContador.setAlignmentX(Component.CENTER_ALIGNMENT);

        centro.add(panelDots);
        centro.add(Box.createVerticalStrut(4));
        centro.add(lblContador);

        nav.add(btnPrev, BorderLayout.WEST);
        nav.add(centro, BorderLayout.CENTER);
        nav.add(btnNext, BorderLayout.EAST);

        bottom.add(nav, BorderLayout.CENTER);
        bottom.add(crearBotonCerrar(), BorderLayout.SOUTH);
        return bottom;
    }

    // puntitos que indican en que slide estamos
    private JPanel crearPanelDots() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        panel.setOpaque(false);
        for (int i = 0; i < SLIDES.length; i++) {
            final int idx = i;
            JLabel dot = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (idx == slideActual) {
                        g2.setColor(AMARILLO);
                        g2.fillOval(3, 3, 14, 14);
                    } else {
                        g2.setColor(new Color(100, 100, 130));
                        g2.fillOval(5, 5, 10, 10);
                    }
                    g2.dispose();
                }
            };
            dot.setPreferredSize(new Dimension(20, 20));
            dot.setCursor(new Cursor(Cursor.HAND_CURSOR));
            dot.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) { mostrarSlide(idx); }
            });
            panel.add(dot);
        }
        return panel;
    }

    // boton de flecha, con estilo dorado y efecto hover
    private JButton crearBotonFlecha(String texto) {
        JButton btn = new JButton(texto) {
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
                Color bg = hover ? new Color(80, 70, 40) : new Color(50, 45, 30);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(180, 150, 60));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.setColor(AMARILLO);
                g2.setFont(new Font("Arial", Font.BOLD, 22));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(55, 55));
        return btn;
    }

    // boton rojo para cerrar, igual que en records
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

    // ---------- SPRITES ----------
    private void cargarSprites() {
        try {
            // estudiante: 11 columnas x 2 filas (misma config que en GestorSprites)
            sheetEstudiante = new SpriteSheet("images/sprites/estudiante/abajoestudiante.png", 11, 2);
        } catch (Exception e) {
            sheetEstudiante = null;
        }
        try {
            // enemigo letal: si las dimensiones del spritesheet son distintas,
            // cambiar estos numeros aca
            sheetEnemigo = new SpriteSheet("images/sprites/enemigoLetal/abajomuerte.png", 11, 2);
        } catch (Exception e) {
            sheetEnemigo = null;
        }
    }

    // devuelve el frame actual para dibujar (o null si no se pudo cargar)
    private Image getFrameActual(boolean esEnemigo) {
        SpriteSheet sheet = esEnemigo ? sheetEnemigo : sheetEstudiante;
        if (sheet == null) return null;
        int cols = sheet.getColumnas();
        int fils = sheet.getFilas();
        int total = cols * fils;
        if (total <= 0) return null;
        int frame = frameAnimacion % total;
        int fila = frame / cols;
        int col = frame % cols;
        return sheet.obtenerFrame(fila, col);
    }

    private void iniciarTimerAnimacion() {
        timerAnimacion = new Timer(100, e -> {
            frameAnimacion++;
            if (panelSprite != null) panelSprite.repaint();
        });
        timerAnimacion.start();
    }

    // ---------- NAVEGACION DE SLIDES ----------
    private void mostrarSlide(int index) {
        if (index < 0 || index >= SLIDES.length) return;
        slideActual = index;
        Slide s = SLIDES[index];

        lblTituloSlide.setText(s.titulo);
        // usamos HTML para que el texto largo haga wrap automatico
        lblTextoSlide.setText("<html><div style='width: 400px; line-height: 1.5;'>" + s.texto + "</div></html>");

        if (lblContador != null) lblContador.setText((index + 1) + " / " + SLIDES.length);
        if (panelDots != null) panelDots.repaint();
        if (panelSprite != null) panelSprite.repaint();

        // reseteamos la animacion para que cada sprite arranque desde el frame 0
        frameAnimacion = 0;
    }

    private void siguienteSlide() { mostrarSlide((slideActual + 1) % SLIDES.length); }
    private void anteriorSlide()  { mostrarSlide((slideActual - 1 + SLIDES.length) % SLIDES.length); }
}