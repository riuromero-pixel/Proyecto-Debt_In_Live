package src.map;

import src.entities.Personaje;
import java.util.Random; // permite utilizar numeros randoms un paquete ya de java

public class Mapa {             //crea la clase mapa, tiene una matriz Celda que se llama celdas y representa el mapa
    public Celda[][] celdas;    // ancho y alto indican sus dimensiones y random genera las posiciones aleatorias
    public int ancho;
    public int alto;
    private Random random;
    
    public Mapa(int ancho, int alto) {      //es el constructor de mapa, recibe sus dimensiones
        this.ancho = ancho;                 
        this.alto = alto;
        this.celdas = new Celda[alto][ancho]; //crea conforme a eso las celdas necesarias
        this.random = new Random();             // random es un objeto, y se crea para despues colocar objetos aleatorios 
        
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                celdas[y][x] = new Celda(x, y);     //estos dos for recorren todas las posiciones del mapa. 
                                                    // para cada posicion se crea una Celda con sus coordenadas x e y
            }
        }
    }
    
    public void cargarNivel(int nivel) { //cargar nivel recibe el numero de nivel actual
        // estos dos primeros for elimina todo objeto que haya quedado anteriormente
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                celdas[y][x].contenido = null;
            }
        }
        
       
        // ponemos paredes en los bordes del mapa
        for (int x = 0; x < ancho; x++) {
         celdas[0][x].contenido = new Pared();           // borde de arriba
         celdas[alto-1][x].contenido = new Pared();      // borde de abajo
            }
        for (int y = 0; y < alto; y++) {
         celdas[y][0].contenido = new Pared();           // borde de la izquierda
         celdas[y][ancho-1].contenido = new Pared();     // borde de la derecha 
            }
        
        //calcula cuanto dinero va a haber apenas empezamos el nivel
        int cantidadDinero = 5 + nivel * 3; //default ponemos 5 y sumamos dependiendo el nivel que sea, por tres (no esta definido esto aun, es un boceto)
        for (int i = 0; i < cantidadDinero; i++) { //repite el proceso "cantidadDinero" de veces
            int x, y;
            do {
                x = 1 + random.nextInt(ancho - 2);
                y = 1 + random.nextInt(alto - 2);
            } while (celdas[y][x].contenido != null);   //genera posiciones aleatorias por dentro de las paredes de los bordes
                                                        //mientras la posicion elegida este ocupada, busca otra posicion

            celdas[y][x].contenido = new Dinero(1 + random.nextInt(5)); //y cuando encuentra una celda libre coloca el dinero
        }
        
        
        int cantidadParedes = nivel * 3;  //a medida que el nivel sea mayor, se van a colocar mas paredes
        for (int i = 0; i < cantidadParedes; i++) {
            int x, y;
            do {
                x = 1 + random.nextInt(ancho - 2);
                y = 1 + random.nextInt(alto - 2);           //esto hace lo mismo que con el dinero, nomas que con paredes
            } while (celdas[y][x].contenido != null);
            
            celdas[y][x].contenido = new Pared();
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
            celda.contenido = personaje;
        }
    }
     public Personaje getPersonaje(int x, int y) {
    if (x < 0 || x >= ancho || y < 0 || y >= alto) {
        return null; // Fuera del mapa
    }
    Object contenido = celdas[y][x].contenido;
    if (contenido instanceof Personaje) {
        return (Personaje) contenido;
    }
    return null;
}

}