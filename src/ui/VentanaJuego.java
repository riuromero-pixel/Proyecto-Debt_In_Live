package src.ui;

import src.entities.Personaje;
import src.entities.Adulto;
import src.entities.Estudiante;
import src.map.Mapa;
import src.map.Pared;
import src.map.Dinero;
import src.map.Bonus;
import src.map.Enemigo;
import javax.swing.*;
import java.awt.*;   //swing y awt nos sirven para el diseño, unicamente como boceto y ver como va ser el juego.
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaJuego extends JFrame{
    private Mapa mapa;
    private int tamaño = 30; //tamaño indica cuantos pixeles ocupa cada celda, en este caso por ahora 30x30
    private Personaje jugador; //variable para almacenar el jugador
    private Timer timerMovimientoEnemigos; //timer para mover Enemigos automaticamente
    private Timer timerJuego; //timer principal del juego para movimiento del jugador
    private int nivelActual = 1; //nivel actual del juego
    private JLabel infoLabel; //etiqueta para mostrar informacion del jugador
    private JLabel dineroLabel; //etiqueta para mostrar el dinero del jugador
    private boolean juegoActivo = true; //controla si el juego sigue activo
    
    //control de movimiento del jugador con velocidad
    private long ultimoMovimientoJugador = 0;
    private char teclaPulsada = ' '; // Espacio = ninguna tecla
    
    //buffer para evitar parpadeo
    private Image bufferImagen;
    private Graphics bufferGraphics;

    public VentanaJuego() {
    mapa = new Mapa(15, 10);
    mapa.cargarNivel(1);
    
    jugador = new Adulto(5, 5, mapa);
    mapa.setPersonaje(jugador, 5, 5);

    setTitle("Juego");
    setSize(15 * tamaño + 45, 10 * tamaño + 120);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);
    
    
    setFocusable(true);
    setFocusTraversalKeysEnabled(false);
    addKeyListener(new TecladoListener());
    
    timerMovimientoEnemigos = new Timer(800, e -> {
        if (juegoActivo) {
            mapa.moverEnemigos();
            actualizarInfo();
            verificarPerdida();
            repaint();
        }
    });
    timerMovimientoEnemigos.start();
    
    timerJuego = new Timer(50, e -> {
        if (juegoActivo && teclaPulsada != ' ') {
            moverJugadorConVelocidad();
        }
    });
    timerJuego.start();
    
    actualizarInfo();
    setVisible(true);
}
    
    //metodo para mover el jugador respetando su velocidad
    private void moverJugadorConVelocidad() {
        long tiempoActual = System.currentTimeMillis();
        int intervaloMovimiento = 500 / Math.max(1, jugador.getVelocidad()); //cuanto mayor velocidad, menor intervalo
        
        if (tiempoActual - ultimoMovimientoJugador >= intervaloMovimiento) {
            int anchoMapa = mapa.getAncho();
            int altoMapa = mapa.getAlto();
            
            jugador.moverse(String.valueOf(teclaPulsada), anchoMapa, altoMapa);
            ultimoMovimientoJugador = tiempoActual;
            
            //verificar si perdio (dinero negativo)
            verificarPerdida();
            
            // Verificar si ya no queda dinero y terminar el juego (GANAR)
            if (juegoActivo && mapa.noQuedaDinero()) {
                ganarJuego();
            }
            
            actualizarInfo();
            repaint();
        }
    }
    
    //verificar si el jugador perdio (dinero negativo)
    private void verificarPerdida() {
        if (jugador.getDinero() < 0) {
            juegoActivo = false;
            System.out.println("¡PERDISTE! Te quedaste sin dinero.");
            JOptionPane.showMessageDialog(this, "¡PERDISTE! Te quedaste sin dinero.");
            System.exit(0);
        }
    }
    
    //terminar juego ganando (NO avanza de nivel)
    private void ganarJuego() {
        juegoActivo = false;
        System.out.println("¡GANASTE! Recolectaste todo el dinero.");
        JOptionPane.showMessageDialog(this, "¡GANASTE! Recolectaste todo el dinero.");
        System.exit(0);
    }

    //metodo para actualizar solo la informacion
    private void actualizarInfo() {
        if (jugador != null && infoLabel != null && dineroLabel != null) {
            String tipoPersonaje = "";
            if (jugador instanceof Adulto) {
                tipoPersonaje = "Adulto";
            } else if (jugador instanceof Estudiante) {
                tipoPersonaje = "Estudiante";
            } else {
                tipoPersonaje = "Jubilado";
            }
            
            //actualizar etiqueta de dinero (destacada)
            dineroLabel.setText("💰 Dinero: $" + jugador.getDinero());
            
            //actualizar informacion general
            infoLabel.setText(String.format(
                "  Personaje: %s |  Fuerza: %d |  Velocidad: %d |  Nivel: %d",
                tipoPersonaje, jugador.getFuerza(), 
                jugador.getVelocidad(), nivelActual
            ));
        }
    }

    public void paint(Graphics g) {
    // Usar doble buffer para evitar parpadeo
    if (bufferImagen == null) {
        bufferImagen = createImage(getWidth(), getHeight());
        bufferGraphics = bufferImagen.getGraphics();
    }
    
    Graphics2D g2d = (Graphics2D) bufferGraphics;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    //limpiar fondo
    g2d.setColor(getBackground());
    g2d.fillRect(0, 0, getWidth(), getHeight());
    
    int desplazamientoX = 15;
    int desplazamientoY = 35;
    
    //dibujar el mapa (ocupa la parte superior)
    for (int y = 0; y < mapa.alto; y++) {
        for (int x = 0; x < mapa.ancho; x++) {
            int px = x * tamaño + desplazamientoX;
            int py = y * tamaño + desplazamientoY;
            
            g2d.setColor(Color.WHITE);
            g2d.fillRect(px, py, tamaño, tamaño);
            
            if (mapa.celdas[y][x].contenido != null) {
                Object obj = mapa.celdas[y][x].contenido;
                if (obj instanceof Pared) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(px, py, tamaño, tamaño);
                } else if (obj instanceof Dinero) {
                    g2d.setColor(Color.YELLOW);
                    g2d.fillOval(px + 5, py + 5, tamaño - 10, tamaño - 10);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString("$", px + 10, py + 20);
                } else if (obj instanceof Enemigo) {
                    g2d.setColor(Color.RED);
                    g2d.fillRect(px + 4, py + 4, tamaño - 8, tamaño - 8);
                    g2d.setColor(Color.WHITE);
                    g2d.drawString("V", px + 11, py + 20);
                }
            }
            
            //dibujar dinero debajo (mismo estilo)
            if (mapa.celdas[y][x].dineroDebajo != null) {
                g2d.setColor(Color.YELLOW);
                g2d.fillOval(px + 5, py + 5, tamaño - 10, tamaño - 10);
                g2d.setColor(Color.BLACK);
                g2d.drawString("$", px + 10, py + 20);
            }
            
            g2d.setColor(Color.GRAY);
            g2d.drawRect(px, py, tamaño, tamaño);
        }
    }
    
    //dibujar el personaje
    if (jugador != null) {
        int px = jugador.getX() * tamaño + desplazamientoX;
        int py = jugador.getY() * tamaño + desplazamientoY;
        dibujarPersonaje(g2d, px, py);
    }
    
    // === DIBUJAR PANEL DE INFORMACIÓN ABAJO ===
    int panelY = 10 * tamaño + 45; //justo debajo del mapa
    int panelHeight = 60;
    
    //fondo del panel
    g2d.setColor(new Color(50, 50, 60));
    g2d.fillRect(0, panelY, getWidth(), panelHeight);
    
    //texto de dinero (grande y amarillo)
    g2d.setColor(Color.YELLOW);
    g2d.setFont(new Font("Arial", Font.BOLD, 20));
    String dineroTexto = "💰 Dinero: $" + (jugador != null ? jugador.getDinero() : 0);
    g2d.drawString(dineroTexto, 20, panelY + 35);
    
    //texto de información (blanco)
    g2d.setColor(Color.WHITE);
    g2d.setFont(new Font("Arial", Font.BOLD, 14));
    String tipo = "";
    if (jugador instanceof Adulto) tipo = "Adulto";
    else if (jugador instanceof Estudiante) tipo = "Estudiante";
    else if (jugador != null) tipo = "Jubilado";
    
    String infoTexto = String.format("Personaje: %s | Fuerza: %d | Velocidad: %d | Nivel: %d",
            tipo,
            jugador != null ? jugador.getFuerza() : 0,
            jugador != null ? jugador.getVelocidad() : 0,
            nivelActual);
    g2d.drawString(infoTexto, 20, panelY + 15);
    
    //copiar el buffer a la pantalla
    g.drawImage(bufferImagen, 0, 0, this);
}

private void dibujarPersonaje(Graphics2D g2d, int px, int py) {
    int margen = tamaño / 10;           // 3px si tamaño=30
    int radio = tamaño / 2 - margen;    // 12px si tamaño=30
    
    // === SOMBRA ===
    g2d.setColor(new Color(0, 0, 0, 60));
    g2d.fillOval(px + margen + 2, py + margen + 4, radio * 2, radio * 2);
    
    // === CARA ===
    g2d.setColor(new Color(255, 220, 50));
    g2d.fillOval(px + margen, py + margen, radio * 2, radio * 2);
    
    // === BORDE ===
    g2d.setColor(new Color(200, 170, 0));
    g2d.setStroke(new BasicStroke(2));
    g2d.drawOval(px + margen, py + margen, radio * 2, radio * 2);
    
    // === OJOS ===
    int ojoTamaño = tamaño / 6;          // 5px si tamaño=30
    int ojoY = py + tamaño / 3;          // 10px desde arriba
    int ojoX1 = px + tamaño / 4;         // 7.5px desde la izquierda
    int ojoX2 = px + tamaño * 3 / 4 - ojoTamaño;  // 22.5px desde la izquierda
    
    g2d.setColor(Color.BLACK);
    g2d.fillOval(ojoX1, ojoY, ojoTamaño, ojoTamaño);
    g2d.fillOval(ojoX2, ojoY, ojoTamaño, ojoTamaño);
    
    // === REFLEJO EN OJOS ===
    int reflejo = ojoTamaño / 3;
    g2d.setColor(Color.WHITE);
    g2d.fillOval(ojoX1 + 2, ojoY + 1, reflejo, reflejo);
    g2d.fillOval(ojoX2 + 2, ojoY + 1, reflejo, reflejo);
    
    // === SONRISA ===
    int sonrisaX = px + tamaño / 5;       // 6px
    int sonrisaY = py + tamaño / 2 + 2;    // 17px desde arriba
    int sonrisaAncho = tamaño * 3 / 5;     // 18px
    int sonrisaAlto = tamaño / 4;          // 7.5px
    
    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke(2));
    g2d.drawArc(sonrisaX, sonrisaY, sonrisaAncho, sonrisaAlto, 0, -180);
    
    // === MEJILLAS ===
    int mejillaTamaño = tamaño / 6;
    g2d.setColor(new Color(255, 150, 150, 100));
    g2d.fillOval(px + margen + 2, py + tamaño / 2 + 2, mejillaTamaño, mejillaTamaño / 2);
    g2d.fillOval(px + tamaño - margen - mejillaTamaño - 2, py + tamaño / 2 + 2, mejillaTamaño, mejillaTamaño / 2);
}


 private class TecladoListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            char tecla = e.getKeyChar();
            
            switch (tecla) {
                case 'w': case 'W': 
                    teclaPulsada = 'W';
                    break;
                case 's': case 'S': 
                    teclaPulsada = 'S';
                    break;
                case 'a': case 'A': 
                    teclaPulsada = 'A';
                    break;
                case 'd': case 'D': 
                    teclaPulsada = 'D';
                    break;
                case 'r': case 'R': 
                    jugador.romperParedes();
                    repaint();
                    break;
                case 'c': case 'C': 
                    jugador.crearParedes();
                    repaint();
                    break;
            }
        }
        
        @Override
        public void keyReleased(KeyEvent e) {
            char tecla = e.getKeyChar();
            
            //si la tecla soltada es la que esta pulsada, detener movimiento
            if (Character.toUpperCase(tecla) == teclaPulsada) {
                teclaPulsada = ' ';
            }
        }
    }
    
    @Override
    public void dispose() {
        juegoActivo = false;
        if (timerMovimientoEnemigos != null) {
            timerMovimientoEnemigos.stop();
        }
        if (timerJuego != null) {
            timerJuego.stop();
        }
        super.dispose();
    }
    
}