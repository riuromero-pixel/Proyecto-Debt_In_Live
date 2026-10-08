package src.map;

import java.io.File;
import java.util.ArrayList; // permite utilizar numeros randoms un paquete ya de java
import java.util.List;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import src.entities.Personaje;

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
                celdas[y][x].bonusDebajo = null;

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

        // esto hace que las paredes o enemigos no ocupen más del 30% del mapa
        int celdasInteriores = (ancho - 2) * (alto - 2);   // 23*23 = 529
        int maxObjetos = (int)(celdasInteriores * 0.30);   // ~158

        int cantidadParedes = Math.min(nivel * 40, maxObjetos / 2);
        int cantidadEnemigos = Math.min(25 * nivel, maxObjetos - cantidadParedes);

         colocarAleatorio(Pared.class, cantidadParedes, nivel);
         colocarAleatorio(Enemigo.class, cantidadEnemigos, nivel);
         generarBonus(nivel * 4); // siempre 3 bonus por nivel
        
    }
    private void colocarAleatorio(Class<?> tipo, int cantidad, int nivel) { //coloca n objetos en el mapa con un maximo de intentos
    int colocados = 0;
    int intentos = 0;
    int maxIntentos = cantidad * 50;   // si después de 50 intentos por objeto no encuentra, corta

    while (colocados < cantidad && intentos < maxIntentos) {
        intentos++;
        int x = 1 + random.nextInt(ancho - 2);
        int y = 1 + random.nextInt(alto - 2);

        if (celdas[y][x].contenido != null || celdas[y][x].dineroDebajo != null) continue;

        if (tipo == Pared.class) {
            celdas[y][x].contenido = new Pared();
        } else if (tipo == Enemigo.class) {
            celdas[y][x].contenido = new Enemigo(x, y, 10 + (nivel * 2));
        }
        colocados++;
    }
}
    //generar bonus
    public void generarBonus(int cantidad) {
    for (int i = 0; i < cantidad; i++) {
        int x, y;
        do {
            x = 1 + random.nextInt(ancho - 2);
            y = 1 + random.nextInt(alto - 2);
        } while (celdas[y][x].contenido != null || celdas[y][x].dineroDebajo != null);

        celdas[y][x].contenido = new Bonus(2, 5000);
    }
}

    //generar dinero
     public void generarDinero() {
        int cantidadDinero = 5 + 3 * 3;
        for (int i = 0; i < cantidadDinero; i++) {
            int x, y;
            do {
                x = 1 + random.nextInt(ancho - 2);
                y = 1 + random.nextInt(alto - 2);
            } while (celdas[y][x].contenido != null || celdas[y][x].dineroDebajo != null);

            celdas[y][x].contenido = new Dinero(1 + random.nextInt(5));
        }
    }

    // NUEVO METODO: Cuenta cuantas monedas quedan en el mapa (tanto visibles como debajo de otros objetos)
    public int contarDineroRestante() {
        int contador = 0;
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                if (celdas[y][x].contenido instanceof Dinero) {
                    contador++;
                }
                if (celdas[y][x].dineroDebajo != null) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public void moverEnemigos(Personaje jugador){
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
           enemigo.actualizarPersecucion(jugador, this);
        }
    }

    // === NUEVO: actualiza las posiciones VISUALES de todos los enemigos ===
    // Lo llama el timer de animación del controlador (~60 FPS) para interpolar
    // el movimiento del enemigo entre la celda anterior y la nueva.
    // Cuando agreguemos sprites, este método sigue siendo el encargado de actualizar
    // la posición que usará la vista para dibujar el sprite del enemigo.
    public void actualizarPosicionesVisualesEnemigos() {
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                if (celdas[y][x].contenido instanceof Enemigo) {
                    ((Enemigo) celdas[y][x].contenido).actualizarPosicionVisual();
                }
            }
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
    public void recolectarDeCelda(Personaje jugador, int x, int y) {
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

        //si hay bonus como contenido
        if (celda.contenido instanceof Bonus) {
            Bonus bonus = (Bonus) celda.contenido;
            jugador.activarBonus(bonus);
            celda.contenido = null;
        }
        //si hay bonus debajo
         if (celda.bonusDebajo != null) {
            Bonus bonus = celda.bonusDebajo;
            jugador.activarBonus(bonus);
            celda.bonusDebajo = null;
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
     // REPRODUCTOR DE MUSICA (recibe el nombre exacto del archivo a reproducir(con extension.wav y todo))
   public static void reproducirSonido(String nombrearchivo) {
    try {
       File archivo = new File(nombrearchivo); 
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivo);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
       clip.loop(Clip.LOOP_CONTINUOUSLY);
    } catch (Exception e) {
        System.out.println("Error al reproducir el audio: " + e.getMessage());
    }
}
}