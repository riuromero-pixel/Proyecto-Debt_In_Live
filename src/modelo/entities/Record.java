package src.modelo.entities;

import java.time.LocalDate;

public class Record {
    private final String nombreJugador;
    private final long tiempoMilisegundos;
    private final int nivelAlcanzado;
    private final String fecha;

    public Record(String nombreJugador, long tiempoMilisegundos, int nivelAlcanzado) {
        if (nombreJugador == null || nombreJugador.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede estar vacío");
        }
        if (tiempoMilisegundos < 0) {
            throw new IllegalArgumentException("El tiempo no puede ser negativo");
        }
        if (nivelAlcanzado < 1 || nivelAlcanzado > 3) {
            throw new IllegalArgumentException("El nivel debe estar entre 1 y 3");
        }
        this.nombreJugador = nombreJugador;
        this.tiempoMilisegundos = tiempoMilisegundos;
        this.nivelAlcanzado = nivelAlcanzado;
        this.fecha = LocalDate.now().toString();
    }

    public String getNombreJugador() { return nombreJugador; }
    public long getTiempoMilisegundos() { return tiempoMilisegundos; }
    public int getNivelAlcanzado() { return nivelAlcanzado; }
    public String getFecha() { return fecha; }
}
