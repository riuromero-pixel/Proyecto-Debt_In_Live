package src.vista;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import src.modelo.entities.Personaje;
import src.modelo.entities.Adulto;
import src.modelo.entities.Jubilado;

public class GestorSprites {
    
    private static GestorSprites instancia;
    
    // se usan arreglos 2D [Tipo][Direccion].
    // Indices de Tipo: 0=Estudiante, 1=Adulto, 2=Jubilado
    // Indices de Direccion: 0=Abajo, 1=Arriba, 2=Derecha, 3=Izquierda
    private BufferedImage[][] cacheEstaticos;
    private SpriteSheet[][] cacheAnimados;

    private GestorSprites() {
        cacheEstaticos = new BufferedImage[3][4];
        cacheAnimados = new SpriteSheet[3][4];
    }

    public static GestorSprites getInstance() {
        if (instancia == null) {
            instancia = new GestorSprites();
        }
        return instancia;
    }

    // MODIFICADO: Método centralizado limpio y sin código duplicado
    public Image obtenerSpritePersonaje(Personaje jugador) {
        // 1. Identificar el tipo de personaje
        int tipoIndex = 0;
        String tipoStr = "estudiante";
        if (jugador instanceof Adulto) { 
            tipoIndex = 1; tipoStr = "adulto"; 
        } else if (jugador instanceof Jubilado) { 
            tipoIndex = 2; tipoStr = "jubilado"; 
        }

        // 2. Identificar la dirección visual
        int dirIndex = 0;
        String dirStr = "abajo";
        if (jugador.direccionVisual != null) {
            switch(jugador.direccionVisual) {
                case "N": dirIndex = 1; dirStr = "arriba"; break;
                case "E": dirIndex = 2; dirStr = "derecha"; break;
                case "O": dirIndex = 3; dirStr = "izquierda"; break;
                case "S": default: dirIndex = 0; dirStr = "abajo"; break;
            }
        }

        // 3. Retornar el frame animado o el estático según el estado
        if (jugador.isEnMovimiento()) {
            // MODIFICADO: Se llama con 4 argumentos para usar la configuración por sprite irregular
            SpriteSheet sheet = getSpriteAnimado(tipoIndex, tipoStr, dirIndex, dirStr);
            if (sheet == null) return null;
            
            // NUEVO: Obtenemos las columnas y filas reales directamente del SpriteSheet cargado
            int cols = sheet.getColumnas();
            int fils = sheet.getFilas();
            
            int fila = (jugador.frameAnimacion / cols) % fils; 
            int columna = jugador.frameAnimacion % cols;
            return sheet.obtenerFrame(fila, columna);
        } else {
            return getSpriteEstatico(tipoIndex, tipoStr, dirIndex, dirStr);
        }
    }

    // método privado que carga y guarda en la matriz (caché) el sprite estático
    private BufferedImage getSpriteEstatico(int tipoIndex, String tipoStr, int dirIndex, String dirStr) {
        if (cacheEstaticos[tipoIndex][dirIndex] == null) {
            String ruta = "images/sprites/" + tipoStr + "/estatico" + dirStr + tipoStr + ".png";
            try {
                cacheEstaticos[tipoIndex][dirIndex] = ImageIO.read(new File(ruta));
            } catch (IOException e) {
                System.err.println("Error cargando sprite estático: " + ruta);
            }
        }
        return cacheEstaticos[tipoIndex][dirIndex];
    }

    // método privado que define las columnas y filas exactas para cada spritesheet irregular
    private SpriteSheet getSpriteAnimado(int tipoIndex, String tipoStr, int dirIndex, String dirStr) {
        if (cacheAnimados[tipoIndex][dirIndex] == null) {
            String ruta = "images/sprites/" + tipoStr + "/" + dirStr + tipoStr + ".png";
            
            // Valores por defecto por si acaso
            int columnas = 11; 
            int filas = 2;     

            // CONFIGURACIÓN INDIVIDUAL: Cambia estos números según las dimensiones reales de cada archivo .png
            if (tipoStr.equals("estudiante")) {
                if (dirStr.equals("abajo"))        { columnas = 11; filas = 2; }
                else if (dirStr.equals("arriba"))   { columnas = 11; filas = 2; }
                else if (dirStr.equals("derecha"))  { columnas = 11; filas = 2; }
                else if (dirStr.equals("izquierda")) { columnas = 11; filas = 2; }
            } 
            else if (tipoStr.equals("adulto")) {
                if (dirStr.equals("abajo"))        { columnas = 5;  filas = 5; }
                else if (dirStr.equals("arriba"))   { columnas = 11;  filas = 2; }
                else if (dirStr.equals("derecha"))  { columnas = 12;  filas = 2; }
                else if (dirStr.equals("izquierda")) { columnas = 12;  filas = 2; }
            } 
            else if (tipoStr.equals("jubilado")) {
                if (dirStr.equals("abajo"))        { columnas = 11;  filas = 2; }
                else if (dirStr.equals("arriba"))   { columnas = 8;  filas = 2; }
                else if (dirStr.equals("derecha"))  { columnas = 13;  filas = 2; }
                else if (dirStr.equals("izquierda")) { columnas = 13;  filas = 2; }
            }

            // Creamos el SpriteSheet con sus dimensiones particulares
            cacheAnimados[tipoIndex][dirIndex] = new SpriteSheet(ruta, columnas, filas);
        }
        return cacheAnimados[tipoIndex][dirIndex];
    }
}