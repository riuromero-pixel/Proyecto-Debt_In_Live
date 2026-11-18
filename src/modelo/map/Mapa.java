package src.modelo.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import src.modelo.entities.Personaje;
import src.modelo.map.Enemigo;
import src.modelo.movimientos.EstrategiaMovimiento;
import src.modelo.movimientos.MovimientoErrante;
import src.modelo.movimientos.MovimientoPerseguidor;

public class Mapa {
    private Random random;
     private Celda[][] celdas;
    private int ancho;
    private int alto;

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
    //getters
    public Celda[][] getCeldas() { return celdas; }
    public int getAncho() {return ancho;}
    public int getAlto() {return alto;}

    public void cargarMapaPlantilla(int nivel) {
        //limpiar mapa
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
               celdas[y][x].setContenido(null);
                celdas[y][x].setDineroDebajo(null);
                celdas[y][x].setBonusDebajo(null);

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
                     celdas[y][x].setContenido(new Pared(0));// Pared por defecto del nivel
                } else if (caracter == '+') {
                     celdas[y][x].setContenido(new Pared()); }}// Pared con silla (solo para nivel 1)}
                }
    }
    public void cargarNivel(int nivel) { //carga el mapa según el nivel y genera enemigos, bonus y dinero
        //limpiar mapa
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
             celdas[y][x].setContenido(null);
                celdas[y][x].setDineroDebajo(null);
                celdas[y][x].setBonusDebajo(null);
            }
        }
        //paredes bordes
        for (int x = 0; x < ancho; x++) {
            celdas[0][x].setContenido(new Pared(0));
            celdas[alto - 1][x].setContenido(new Pared(0));
        }
        for (int y = 0; y < alto; y++) {
            celdas[y][0].setContenido(new Pared(0));
            celdas[y][ancho - 1].setContenido(new Pared(0));
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

        if (celdas[y][x].getContenido() != null || celdas[y][x].getDineroDebajo() != null) continue;
            if (tipo == Enemigo.class) {
            
            celdas[y][x].setContenido(new HacerEnemigo().crearEnemigo(x, y, nivel));
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
    } while ((celdas[y][x].getContenido() != null 
           || Math.abs(x - 5) + Math.abs(y - 5) < 8) 
           && intentos < 100);

    if (intentos < 100) {
        celdas[y][x].setContenido(new CreadorEnemigoLetal().crearEnemigo(x, y, 3));
    }
    }
    //generar bonus
    public void generarBonus(int cantidad, int nivel) {
    for (int i = 0; i < cantidad; i++) {
        int x, y;
        do {
            x = 1 + random.nextInt(ancho - 2);
            y = 1 + random.nextInt(alto - 2);
        } while (celdas[y][x].getContenido() != null || celdas[y][x].getDineroDebajo() != null);

        celdas[y][x].setContenido(new Bonus(2, 5000, nivel));
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
            } while (celdas[y][x].getContenido() != null || celdas[y][x].getDineroDebajo() != null);
                        celdas[y][x].setContenido(new Dinero(2999));

           // celdas[y][x].setContenido(new Dinero(100 + random.nextInt(400)));
        }
    }

    // cuenta cuantas monedas quedan en el mapa (tanto visibles como debajo de otros objetos)
    public int contarDineroRestante() {
        int contador = 0;
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                if (celdas[y][x].getContenido() instanceof Dinero) {
                    contador++;
                }
                if (celdas[y][x].getDineroDebajo() != null) {
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
                if (celdas[y][x].getContenido() instanceof Enemigo) {
                    enemigos.add((Enemigo) celdas[y][x].getContenido());
                }
            }
        }
        
        //mover cada enemigo
        for (Enemigo enemigo : enemigos) {
           enemigo.actualizarPersecucion(jugador, this);
        }
    }
    //verificar si el jugador piso un Enemigo
   public boolean verificarColisionConEnemigo(Personaje jugador) {
    Celda celdaJugador = conseguirCelda(jugador.getX(), jugador.getY());

    if (celdaJugador != null && celdaJugador.getContenido() instanceof Enemigo) {
        Enemigo enemigo = (Enemigo) celdaJugador.getContenido();

        enemigo.hacerDanio(jugador); // POLIMORFISMO

        return true;
    }

    return false;
}
    
    //verificar si no queda dinero en el mapa (visible o debajo)
    public boolean noQuedaDinero() {
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                if (celdas[y][x].getContenido() instanceof Dinero || celdas[y][x].getDineroDebajo() != null) {
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

        if (celda.getContenido() instanceof ObjetoEntorno) {
            ObjetoEntorno objeto = (ObjetoEntorno) celda.getContenido();
            objeto.recolectar(jugador);
            celda.setContenido(null); // Limpiar la celda después de recolectar
        }
        
        //si hay dinero debajo
        if (celda.getDineroDebajo() != null) {
            celda.getDineroDebajo().recolectar(jugador);
            celda.setDineroDebajo(null);
        }

        //si hay bonus como contenido
        if (celda.getBonusDebajo() != null) {
            celda.getBonusDebajo().recolectar(jugador);
            celda.setBonusDebajo(null);
        }

    }
    
    public Celda conseguirCelda(int x, int y) {
        if (x < 0 || x >= ancho || y < 0 || y >= alto) {
            return null;
        }
        return celdas[y][x];
    }
    
    public void setPersonaje(Personaje personaje, int x, int y) {
        Celda celda = conseguirCelda(x, y);
        if (celda != null) {
            //si hay enemigo, el personaje NO se mueve a esa celda
            if (celda.getContenido() instanceof Enemigo) {
                return; // No poner personaje encima del enemigo
            }
            //si hay dinero, lo dejamos en contenido (el personaje se pone encima y lo recolecta)
            //el dinero se mantiene visible hasta que el personaje lo recolecte
            celda.setContenido(personaje);
        }
    }
    public Celda getCelda(int x, int y) {
    return celdas[y][x];
}
   public List<Enemigo> getEnemigos() {
    List<Enemigo> listaEnemigos = new ArrayList<Enemigo>();

    for (int y = 0; y < alto; y++) {
        for (int x = 0; x < ancho; x++) {
            Object contenido = celdas[y][x].getContenido();

            if (contenido instanceof Enemigo) {
                listaEnemigos.add((Enemigo) contenido);
            }
        }
    }

    return listaEnemigos;
}
    
    public Personaje getPersonaje(int x, int y) {
        if (x < 0 || x >= ancho || y < 0 || y >= alto) {
            return null;
        }
        Object contenido = celdas[y][x].getContenido();
        if (contenido instanceof Personaje) {
            return (Personaje) contenido;
        }
        return null;}

    }
 

        
    
