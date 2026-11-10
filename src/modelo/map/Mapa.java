package src.modelo.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import src.modelo.entities.Personaje;
import src.modelo.movimientos.EstrategiaMovimiento;
import src.modelo.movimientos.MovimientoErrante;
import src.modelo.movimientos.MovimientoPerseguidor;
import src.modelo.map.ObjetoEntorno;

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
    public void cargarMapaPlantilla(int nivel) {
        //limpiar mapa
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                celdas[y][x].contenido = null;
                celdas[y][x].dineroDebajo = null;
                celdas[y][x].bonusDebajo = null;

            }
        }
        //tiles de mapa según nivel
        String[] plantilla; // Plantilla de mapa con paredes(#), sillas (+) en el caso de Estudiante y espacios vacíos (.) de 40x40
            if (nivel == 1) { // Plantilla de mapa para el nivel de Estudiante
                plantilla = new String[]{
            "########################################",
            "#......................................#",
            "#.......+....................+.........#",
            "#................+.....................#",
            "#......................................#",
            "###############..........###############",
            "#......................................#",
            "#........####................####......#",
            "#......................................#",
            "#........####................####......#",
            "#......................................#",
            "#......................................#",
            "#......+...............................#",
            "#......................................#",
            "#......+...............................#",
            "###############..........###############",
            "#............+.........................#",
            "#........####................####......#",
            "#......................................#",
            "#........####................####......#",
            "#......................+...............#",
            "#........+.............................#",
            "#..####....####..........####....####..#",
            "#......................................#",
            "#..####....####..........####....####..#",
            "#......................................#",
            "#......................+...............#",
            "#........+.............................#",
            "#......................+...............#",
            "#........+.............................#",
            "#.........+++++++......................#",
            "#......................................#",
            "#..####....####..........####....####..#",
            "#......................................#",
            "#..####....####..........####....####..#",
            "#......................................#",
            "#......................................#",
            "#..............++++....................#",
            "#......................................#",
            "########################################"
            };
            } else if (nivel == 2) {
                plantilla = new String[]{
            "########################################",
            "#...........#.............#............#",
            "#....####...#.............#....####....#",
            "#....####...#.............#....####....#",
            "#...........#.............#............#",
            "#####......##.............####.....#####",
            "#..........####........####............#",
            "#..........####........####............#",
            "#......................................#",
            "#......................................#",
            "#....##............................##..#",
            "#....##............................##..#",
            "#................####..................#",
            "#############.............##############",
            "#...........#.............#............#",
            "#....####...#.............#............#",
            "#....####...#.............#............#",
            "#...........#.............#............#",
            "#####......##.............####.....#####",
            "#......................................#",
            "#....##............................##..#",
            "#....##............................##..#",
            "#......................................#",
            "#......................................#",
            "#................####..................#",
            "#############.............##############",
            "#...........#.............#............#",
            "#...........#.............#............#",
            "#...........#.............#............#",
            "#...........#.............#............#",
            "#####......##.............####.....#####",
            "#......................................#",
            "#....##............................##..#",
            "#....##............................##..#",
            "#......................................#",
            "#......................................#",
            "#......................................#",
            "#......................................#",
            "#......................................#",
            "########################################"
                };
  
            } else { // Plantilla de mapa para el nivel de Jubilado
                plantilla = new String[]{
            "########################################",
            "#......................................#",
            "#....####......................####....#",
            "#....####......................####....#",
            "#......................................#",
            "#......................................#",
            "#..........####........####............#",
            "#..........####........####............#",
            "#......................................#",
            "#......................................#",
            "#....##............................##..#",
            "#....##............................##..#",
            "#................####..................#",
            "#................####..................#",
            "#......................................#",
            "#......................................#",
            "#..........####........####............#",
            "#..........####........####............#",
            "#......................................#",
            "#......................................#",
            "#....##............................##..#",
            "#....##............................##..#",
            "#......................................#",
            "#......................................#",
            "#................####..................#",
            "#................####..................#",
            "#......................................#",
            "#......................................#",
            "#..........####........####............#",
            "#..........####........####............#",
            "#......................................#",
            "#......................................#",
            "#....##............................##..#",
            "#....##............................##..#",
            "#......................................#",
            "#......................................#",
            "#......................................#",
            "#......................................#",
            "#......................................#",
            "########################################"
            };
        } 

        for (int y = 0; y < plantilla.length && y < alto; y++) {
            for (int x = 0; x < plantilla[y].length() && x < ancho; x++) {
                char caracter = plantilla[y].charAt(x);

                if (caracter == '#') {
                    celdas[y][x].contenido = new Pared(0); // Pared por defecto del nivel
                } else if (caracter == '+') {
                    celdas[y][x].contenido = new Pared(); // Pared con silla (solo para nivel 1)
                }
            }
        }
    }
    public void cargarNivel(int nivel) { //carga el mapa según el nivel y genera enemigos, bonus y dinero
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
            celdas[0][x].contenido = new Pared(0);
            celdas[alto - 1][x].contenido = new Pared(0);
        }
        for (int y = 0; y < alto; y++) {
            celdas[y][0].contenido = new Pared(0);
            celdas[y][ancho - 1].contenido = new Pared(0);
        }
        cargarMapaPlantilla(nivel);

        // esto hace que las paredes o enemigos no ocupen más del 30% del mapa
        int celdasInteriores = (ancho - 2) * (alto - 2);   // 23*23 = 529
        int maxObjetos = (int)(celdasInteriores * 0.30);   // ~158

        int cantidadParedes = Math.min(nivel * 40, maxObjetos / 2); //cantidad de paredes segun nivel
        int cantidadEnemigos = Math.min(10 * nivel, maxObjetos - cantidadParedes); //cantidad de enemigos segun nivel

         colocarAleatorio(Enemigo.class, cantidadEnemigos, nivel);
         generarBonus(nivel * 4, nivel); // siempre 3 bonus por nivel
         colocarEnemigoLetal(nivel);
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
            if (tipo == Enemigo.class) {
            EstrategiaMovimiento estrategia = random.nextBoolean() 
            ? new MovimientoPerseguidor() 
            : new MovimientoErrante(); //mezcla al azar entre perseguidor y errante
            celdas[y][x].contenido = new Enemigo(x, y, 10 + (nivel * 2), estrategia, nivel);
        }
        colocados++;
    }
}
    //metodo para crear un solo enemigo letal en el nivel 3
    private void colocarEnemigoLetal(int nivel) {
    if (nivel != 3) return;

    int x, y, intentos = 0;
    do {
        x = 1 + random.nextInt(ancho - 2);
        y = 1 + random.nextInt(alto - 2);
        intentos++;
    } while ((celdas[y][x].contenido != null 
           || Math.abs(x - 5) + Math.abs(y - 5) < 8) 
           && intentos < 100);

    if (intentos < 100) {
        celdas[y][x].contenido = new EnemigoLetal(x, y, 9999);
    }
    }
    //generar bonus
    public void generarBonus(int cantidad, int nivel) {
    for (int i = 0; i < cantidad; i++) {
        int x, y;
        do {
            x = 1 + random.nextInt(ancho - 2);
            y = 1 + random.nextInt(alto - 2);
        } while (celdas[y][x].contenido != null || celdas[y][x].dineroDebajo != null);

        celdas[y][x].contenido = new Bonus(2, 5000, nivel);
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
                        celdas[y][x].contenido = new Dinero(2999);

           // celdas[y][x].contenido = new Dinero(100 + random.nextInt(400));
        }
    }

    // cuenta cuantas monedas quedan en el mapa (tanto visibles como debajo de otros objetos)
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

    // actualiza las posiciones VISUALES de todos los enemigos
    // Lo llama el timer de animación del controlador (~60 FPS) para interpolar
    // el movimiento del enemigo entre la celda anterior y la nueva.
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
        if (celda.contenido instanceof ObjetoEntorno) {
            ObjetoEntorno objeto = (ObjetoEntorno) celda.contenido;
            if (objeto.esRecolectable()) {
                objeto.recolectar(jugador);
                celda.contenido = null; // Limpiar la celda después de recolectar
            }
        }
        //si hay dinero debajo
        if (celda.dineroDebajo != null) {
            celda.dineroDebajo.recolectar(jugador);
            celda.dineroDebajo = null;
        }
        //si hay bonus debajo
         if (celda.bonusDebajo != null) {
            celda.bonusDebajo.recolectar(jugador);
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
    }}
 

        
    
