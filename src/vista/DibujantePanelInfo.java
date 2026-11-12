package src.vista;

import java.awt.*;
import src.modelo.entities.*;
import src.modelo.map.Mapa;

public class DibujantePanelInfo {

    private static final int PANEL_ALTO = 60;

    public void dibujar(Graphics2D g2d, int anchoPantalla, int altoPantalla,
                        Personaje jugador, Mapa mapa, int nivelActual,
                        long tiempoInicioPartida, boolean gameOver) {

        int panelY = altoPantalla - PANEL_ALTO;

        g2d.setColor(new Color(50, 50, 60));
        g2d.fillRect(0, panelY, anchoPantalla, PANEL_ALTO);

        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        String dineroTexto = "💰 Dinero: $" + (jugador != null ? jugador.getDinero() : 0);
        g2d.drawString(dineroTexto, 20, panelY + 35);

        g2d.setColor(Color.CYAN);
        String monedasRestantes = "Fajos de plata restantes: " + (mapa != null ? mapa.contarDineroRestante() : 0);
        g2d.drawString(monedasRestantes, 250, panelY + 35);

        if (tiempoInicioPartida > 0) {
            long tiempoActual = System.currentTimeMillis();
            long tiempoTranscurrido = tiempoActual - tiempoInicioPartida;
            long segundosTotales = tiempoTranscurrido / 1000;
            long minutos = segundosTotales / 60;
            long segundos = segundosTotales % 60;

            String textoTiempo = String.format("Tiempo: %02d:%02d", minutos, segundos);
            g2d.setColor(Color.GREEN);
            g2d.drawString(textoTiempo, 800, panelY + 35);
        }

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));

        String tipo = "";
        if (jugador instanceof Adulto) tipo = "Adulto";
        else if (jugador instanceof Estudiante) tipo = "Estudiante";
        else if (jugador != null) tipo = "Jubilado";

        String infoTexto = String.format("Personaje: %s | Velocidad: %d | Nivel: %d",
                tipo,
                jugador != null ? jugador.getVelocidad() : 0,
                nivelActual);
        g2d.drawString(infoTexto, 20, panelY + 15);

        if (gameOver) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 80));
            String texto = "PINCHÓ";
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(texto, (anchoPantalla - fm.stringWidth(texto)) / 2,
                    (altoPantalla - PANEL_ALTO) / 2 - 50);
        }
    }
}