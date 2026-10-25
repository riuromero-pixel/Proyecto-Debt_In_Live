package src.modelo.entities;

import java.time.LocalDate;

public class Record {
    private String nombreJugador;
    private long tiempoMilisegundos; // tiempo tardado
    private int nivelAlcanzado;
    private String fecha;

    public Record(String nombreJugador, long tiempoMilisegundos, int nivelAlcanzado) {
        this.nombreJugador = nombreJugador;
        this.tiempoMilisegundos = tiempoMilisegundos;
        this.nivelAlcanzado = nivelAlcanzado;
        this.fecha = LocalDate.now().toString();
    }
    //getters
    public String getNombreJugador() { return nombreJugador; }
    public long getTiempoMilisegundos() { return tiempoMilisegundos; }
    public int getNivelAlcanzado() { return nivelAlcanzado; }
    public String getFecha() { return fecha; }
}
