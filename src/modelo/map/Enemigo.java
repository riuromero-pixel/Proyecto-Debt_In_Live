 package src.modelo.map;

import src.modelo.entities.Personaje;
import src.modelo.movimientos.EstrategiaMovimiento;
import java.util.Random;

// correccion: instanceof no es polimorfismo, porque obliga al codigo externo a conocer las clases concretas de los enemigos. 
// usé polimorfismo en el método hacerDanio() de la clase Enemigo y EnemigoLetal, 
// que es llamado desde Personaje.java y MovimientoErrante.java.
// esto hace que la JVM determine automáticamente qué versión del método ejecutar según el tipo real del objeto.

public class Enemigo extends ObjetoEntorno {
    private int x;
    private int y;
    private int danio;

    private EstrategiaMovimiento estrategia;

    private int variante;

    public Enemigo(int x, int y, int danio, EstrategiaMovimiento estrategia, int nivel) {
        super(true, false); // El enemigo puede ser pisado pero no recogido
        this.x = x;
        this.y = y;
        this.danio = danio;
        this.estrategia = estrategia;


        // Generamos la variante aleatoria según el nivel
        Random rand = new Random();
        if (nivel == 1) {
            this.variante = 1 + rand.nextInt(4); // Sortea del 1 al 4
        } else if (nivel == 2) {
            this.variante = 1 + rand.nextInt(5); // Sortea del 1 al 5
        } else if (nivel == 3) {
            this.variante = 1 + rand.nextInt(4); // Sortea del 1 al 4
        } else {
            this.variante = 1; 
        }
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public int getDanio() { return danio; }
    //public double getPosXVisual() { return posXVisual; }
    //public double getPosYVisual() { return posYVisual; }
    public int getVariante() {return this.variante;}
    
    public TipoVisual getTipoVisual() {
        return TipoVisual.ENEMIGO;
    }
    
    public void setEstrategia(EstrategiaMovimiento estrategia) {
        this.estrategia = estrategia;
    }
    public EstrategiaMovimiento getEstrategia() {
        return this.estrategia;
    }

    public void moverEnemigo(Enemigo enemigo, int mejorX, int mejorY, Mapa mapa) {
            Celda celdaActual = mapa.conseguirCelda(enemigo.x, enemigo.y);
            Celda celdaNueva = mapa.conseguirCelda(mejorX, mejorY);

            if (celdaActual != null && celdaNueva != null) {
                
                // Si el enemigo se va de su celda y había dinero abajo, lo restauramos a visible
                if (celdaActual.getDineroDebajo() != null) {
                    celdaActual.setContenido(celdaActual.getDineroDebajo());
                    celdaActual.setDineroDebajo(null);
                } else {
                    celdaActual.setContenido(null);
                }

                // Si la celda nueva a la que entra tiene dinero, lo resguardamos en 'dineroDebajo'
                if (celdaNueva.getContenido() instanceof Dinero) {
                    celdaNueva.setDineroDebajo((Dinero) celdaNueva.getContenido());
                }

                // Colocamos al enemigo en la celda nueva y actualizamos sus coordenadas
                celdaNueva.setContenido(enemigo);
                enemigo.x = mejorX;
                enemigo.y = mejorY;
            }
        }

    public void actualizarPersecucion(Personaje personaje, Mapa mapa) {
        if (estrategia != null) {
            estrategia.mover(this, personaje, mapa);
        }
    } 

    public void hacerDanio(Personaje jugador) {
        jugador.danioRecibido(this.danio);
    }
} 
