package src.ui;

import src.map.Mapa;
import src.map.Pared;
import src.map.Dinero;
import src.map.Bonus;
import javax.swing.*;
import java.awt.*;   //swing y awt nos sirven para el diseño, unicamente como boceto y ver como va ser el juego.

public class VentanaJuego extends JFrame {      //creamos la clase VentanaJuego que se hereda de JFrame que es una ventana de java
    private Mapa mapa;
    private int tamaño = 30; //tamaño indica cuantos pixeles ocupa cada celda, en este caso por ahora 30x30
    
    public VentanaJuego() {
        mapa = new Mapa(15, 10); // Es el constructor de la ventanita, crea un mapa de 15x30 celdas
        mapa.cargarNivel(1);
        
        setTitle("Juego"); //es el titulo que aparece en la ventanita
        setSize(15 * tamaño + 25, 10 * tamaño + 45); //establece el tamaño de la ventana adaptada a el tamaño del mapa
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //indica que al cerrar la ventana se deja de ejecutar el juego
        setLocationRelativeTo(null); //coloca la ventana en medio de la pantalla
    }
    
    public void paint(Graphics g) { //funcion que se encarga de dibujar todo lo que tenemos por ahora (dinero, paredes, celdas, etc)
    super.paint(g);             //Graphics g es una herramienta que java nos da para dibujar 
    
    // desplazamos todo el dibujo 35 pixeles hacia abajo y 15 hacia la derecha
    // asi la fila de arriba y la columna de la izquierda no quedan escondidas
    int desplazamientoX = 15;
    int desplazamientoY = 35;
    
    for (int y = 0; y < mapa.alto; y++) {
        for (int x = 0; x < mapa.ancho; x++) {      //recorre todas las celdas del mapa
            int px = x * tamaño + desplazamientoX;
            int py = y * tamaño + desplazamientoY;        //calcula donde se va dibujar cada celda en la pantalla
            
            
            g.setColor(Color.WHITE); //pinta el fondo de cada celda de blanco
            g.fillRect(px, py, tamaño, tamaño); //fillRect dibuja un rectangulo relleno
            
            
            if (mapa.celdas[y][x].contenido != null) { //comprueba si la celda tiene algun objeto
                Object obj = mapa.celdas[y][x].contenido; //si el contenido es distinto de null guardamos ese objeto en obj
                
                if (obj instanceof Pared) { //instanceof comprueba el tipo de objeto que es
                    g.setColor(Color.BLACK); //si es pared dibuja un rectangulo negro
                    g.fillRect(px, py, tamaño, tamaño);
                } else if (obj instanceof Dinero) {
                    g.setColor(Color.YELLOW); //si es dinero dibuja un circulo amarillo
                    g.fillOval(px + 5, py + 5, tamaño - 10, tamaño - 10);
                    g.setColor(Color.BLACK);
                    g.drawString("$", px + 10, py + 20); //se le agrega el simbolo de dinero en medio
                }
            }
            
            
            g.setColor(Color.GRAY);
            g.drawRect(px, py, tamaño, tamaño); //aca nos dibuja los bordes de cada celda, asi podemos distingir cuando termina y empieza una nueva
        }
    }
}
    
}