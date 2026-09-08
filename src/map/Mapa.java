package src.map;

import src.entities.Personaje;
import java.util.Random; // permite utilizar numeros randoms un paquete ya de java
import java.util.ArrayList;
import java.util.List;

public class Mapa {
    public Celda[][] celdas;
    public int ancho;
    public int alto;
    private Random random;

    public Mapa(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        this.celdas = new Celda[alto][ancho];
        this.random = new Random();

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                celdas[y][x] = new Celda(x, y);
            }
        }
    }

    public void cargarNivel(int nivel) {
        //limpiar mapa
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                celdas[y][x].contenido = null;
                celdas[y][x].dineroDebajo = null;
            }
        }

        //paredes bordes
        for (int x = 0; x < ancho; x++) {
            celdas[0][x].contenido = new Pared();
            celdas[alto - 1][x].contenido = new Pared();
        }
        for (int y = 0; y < alto; y++) {
            celdas[y][0].contenido = new Pared();
            celdas[y][ancho - 1].contenido = new Pared();
        }

        //generar dinero
        int cantidadDinero = 5 + nivel * 3;
        for (int i = 0; i < cantidadDinero; i++) {
            int x, y;
            do {
                x = 1 + random.nextInt(ancho - 2);
                y = 1 + random.nextInt(alto - 2);
            } while (celdas[y][x].contenido != null || celdas[y][x].dineroDebajo != null);

            celdas[y][x].contenido = new Dinero(1 + random.nextInt(5));
        }

        //generar paredes internas
        int cantidadParedes = nivel * 3;
        for (int i = 0; i < cantidadParedes; i++) {
            int x, y;
            do {
                x = 1 + random.nextInt(ancho - 2);
                y = 1 + random.nextInt(alto - 2);
            } while (celdas[y][x].contenido != null || celdas[y][x].dineroDebajo != null);

            celdas[y][x].contenido = new Pared();
        }

        //generar Enemigos
        int cantidadEnemigos = 4 * nivel;
        for (int i = 0; i < cantidadEnemigos; i++) {
            int x, y;
            do {
                x = 1 + random.nextInt(ancho - 2);
                y = 1 + random.nextInt(alto - 2);
            } while (celdas[y][x].contenido != null || celdas[y][x].dineroDebajo != null);

            celdas[y][x].contenido = new Enemigo(x, y, 10 + (nivel * 2));
        }
    }

    public void moverEnemigos(){
        //lista para evitar modificar mientras iteramos
        List<Enemigo> enemigos = new ArrayList<>();
        
        //recolectar todos los enemigos
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                if (celdas[y][x].contenido instanceof Enemigo) {
                    enemigos.add((Enemigo) celdas[y][x].contenido);
                }
            }
        }
        
        //mover cada enemigo
        for (Enemigo enemigo : enemigos) {
            enemigo.mover(this);
        }
    }
    
    //verificar si el jugador piso un Enemigo
    public boolean verificarColisionConEnemigo(Personaje jugador) {
        Celda celdaJugador = conseguirCelda(jugador.getX(), jugador.getY());
        if (celdaJugador != null && celdaJugador.contenido instanceof Enemigo) {
            Enemigo enemigo = (Enemigo) celdaJugador.contenido;
            jugador.danioRecibido(enemigo.danio);
            return true;
        }
        return false;
    }
    
    //verificar si no queda dinero en el mapa (visible o debajo)
    public boolean noQuedaDinero() {
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                if (celdas[y][x].contenido instanceof Dinero || celdas[y][x].dineroDebajo != null) {
                    return false;
                }
            }
        }
        return true;
    }
    
    //recolectar dinero si hay en contenido o en dineroDebajo
    public void recolectarDineroDeCelda(Personaje jugador, int x, int y) {
        Celda celda = conseguirCelda(x, y);
        if (celda == null) return;
        
        //si hay dinero como contenido
        if (celda.contenido instanceof Dinero) {
            Dinero dinero = (Dinero) celda.contenido;
            jugador.recolectarDinero(dinero.cantidad);
            celda.contenido = null;
        }
        
        //si hay dinero debajo
        if (celda.dineroDebajo != null) {
            Dinero dinero = celda.dineroDebajo;
            jugador.recolectarDinero(dinero.cantidad);
            celda.dineroDebajo = null;
        }
    }
    
    public Celda conseguirCelda(int x, int y) {
        if (x < 0 || x >= ancho || y < 0 || y >= alto) {
            return null;
        }
        return celdas[y][x];
    }
    public int getAncho() {
        return ancho;
    }
    public int getAlto() {
        return alto;
    }
    
    public void setPersonaje(Personaje personaje, int x, int y) {
        Celda celda = conseguirCelda(x, y);
        if (celda != null) {
            //si hay enemigo, el personaje NO se mueve a esa celda
            if (celda.contenido instanceof Enemigo) {
                return; // No poner personaje encima del enemigo
            }
            //si hay dinero, lo dejamos en contenido (el personaje se pone encima y lo recolecta)
            //el dinero se mantiene visible hasta que el personaje lo recolecte
            celda.contenido = personaje;
        }
    }
    
    public Personaje getPersonaje(int x, int y) {
        if (x < 0 || x >= ancho || y < 0 || y >= alto) {
            return null;
        }
        Object contenido = celdas[y][x].contenido;
        if (contenido instanceof Personaje) {
            return (Personaje) contenido;
        }
        return null;
    }
}