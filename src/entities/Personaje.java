package src.entities;

import src.map.Bonus;
import src.map.Celda;
import src.map.Pared;
import src.map.Mapa;

public class Personaje{
protected int x;
protected int y;
protected int velocidad;
protected int fuerza;
protected int dinero;
protected boolean bonusActivado;
protected int golpesRestantes;   
protected long ultimoGolpe;
protected Mapa mapa;
protected Object paredObjetivo;  

public Personaje(int x, int y, int velocidad, int fuerza, int dinero, boolean bonusActivado, Mapa mapa){
    this.x = x;
    this.y = y;
    this.velocidad = velocidad;
    this.fuerza = fuerza;
    this.dinero = 0;
    this.bonusActivado = bonusActivado;
    this.ultimoGolpe = 0;
    this.mapa = mapa;
}
public int getX() { return x; }
    public int getY() { return y; }
    public int getDinero() { return dinero; }
    public int getVelocidad() { return velocidad; }
    public int getFuerza() { return fuerza; }
    public boolean isBonusActivado() { return bonusActivado; }
    public Mapa getMapa() { return mapa; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setDinero(int dinero) { this.dinero = dinero; }
    public void setVelocidad(int velocidad) { this.velocidad = velocidad; }
    public void setFuerza(int fuerza) { this.fuerza = fuerza; }
    public void setBonusActivado(boolean bonusActivado) { this.bonusActivado = bonusActivado; }
    public void setMapa(Mapa mapa) { this.mapa = mapa; }

public void moverse(String tecla, int anchoMapa, int altoMapa) {
    char direccion = tecla.toUpperCase().charAt(0);
        int nuevaX = this.x;
        int nuevaY = this.y;
        
        switch (direccion) {
            case 'W':
                nuevaY = Math.min(altoMapa - 1, this.y - 1); 
                break;
            case 'S':
                nuevaY = Math.max(0, this.y + 1);  
                break;
            case 'A':
                nuevaX = Math.max(0, this.x - 1); 
                break;
            case 'D':
                nuevaX = Math.min(anchoMapa - 1, this.x + 1); 
                break;
                
        }
            if (nuevaX != this.x || nuevaY != this.y) {
            this.x = nuevaX;
            this.y = nuevaY;
            }
}




public void recolectarDinero(int cantidad) {
    this.dinero += cantidad;
}
public boolean danioRecibido(int danio) {
    this.dinero -= 10; 
    if (this.dinero < 0) {
        System.out.println("Te quedaste sin plata. Pinchó.");
        return true;
    }
    return false;
}
private Celda conseguirCelda(int x, int y) { // es un metodo para conseguir la celda en la que esta el personaje
        if (mapa == null) return null;
        return mapa.conseguirCelda(x, y); 
    }    
    private boolean verificarPared(int x, int y) { //verifica si hay una pared en la celda que recibe por parametro
        Celda celda = conseguirCelda(x, y);
        if (celda == null) return false;
        return celda.contenido != null && celda.contenido instanceof Pared;
    }
public void romperParedes() {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - ultimoGolpe < 400) {
            return;
        }
        // Direcciones: ARRIBA, ABAJO, DERECHA, IZQUIERDA
        int[][] direcciones = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        boolean encontroPared = false;      
        for (int[] dir : direcciones) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            
            // Verificar si hay una pared en la posición (nx, ny)
            // En tu juego esto sería: Celda celda = mapa.conseguirCelda(nx, ny);
            boolean hayPared = verificarPared(nx, ny);
            
            if (hayPared) {
                // Si es una nueva pared o cambiamos de objetivo, reiniciar golpes
                // En tu juego: if (paredObjetivo == null || paredObjetivo != celda)
                if (paredObjetivo == null || !paredObjetivo.equals(nx + "," + ny)) {
                    paredObjetivo = nx + "," + ny;
                    golpesRestantes = 2;
                }
                golpesRestantes--;
                ultimoGolpe = tiempoActual;
                
                // si los golpes llegan a 0, se rompe la pared
                if (golpesRestantes <= 0) {
                    // Reiniciar contadores
                    golpesRestantes = 2;
                    paredObjetivo = null;
                }      
                encontroPared = true;
                break;
            }
        }    
}







}