package src.modelo.entities;
import src.modelo.map.Bonus;
import src.modelo.map.Celda;
import src.modelo.map.Pared;
import src.modelo.map.Mapa;
import src.modelo.map.ObjetoEntorno;
import src.modelo.map.Enemigo;
import src.modelo.map.EnemigoLetal;

public abstract class Personaje{
protected int x;
protected int y;
protected int velocidad;
protected int velocidadOriginal;
protected int dinero;
protected boolean bonusActivado = false;
protected int golpesRestantes;   
protected long ultimoGolpe;
protected Mapa mapa;

protected Object paredObjetivo;  
protected String direccionActual; //guarda la ultima direccion del personaje
protected long bonusFin = 0; //tiempo en milisegundos cuando el bonus expira

public Personaje(int x, int y, int velocidad, int dinero, boolean bonusActivado, Mapa mapa){ // Constructor que inicializa las propiedades del personaje, incluyendo su posición, velocidad, fuerza, dinero, estado de bonus y referencia al mapa
    this.x = x;
    this.y = y;
    this.velocidad = velocidad;
    this.dinero = dinero; 
    this.bonusActivado = bonusActivado;
    this.ultimoGolpe = 0;
    this.mapa = mapa;
    this.direccionActual = null; 
}
    //getters y setters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getDinero() { return dinero; }
    public int getVelocidad() { return velocidad; }
    public boolean isBonusActivado() { return bonusActivado; }
    public Mapa getMapa() { return mapa; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setDinero(int dinero) { this.dinero = dinero; }
    public void setVelocidad(int velocidad) { this.velocidad = velocidad; }
    public void setBonusActivado(boolean bonusActivado) { this.bonusActivado = bonusActivado; }
    public void setMapa(Mapa mapa) { this.mapa = mapa; }
    public String getDireccionActual() { return direccionActual; }
    public abstract TipoPersonaje getTipo();


public void moverse(String tecla, int anchoMapa, int altoMapa) {
    char direccion = tecla.toUpperCase().charAt(0);
    int nuevaX = this.x;
    int nuevaY = this.y;
    
    //guardar la direccion actual
    this.direccionActual = String.valueOf(direccion);
    
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

    //verificar si la celda destino tiene una pared (NO se puede pisar)
    Celda celdaDestino = mapa.conseguirCelda(nuevaX, nuevaY);
       if (celdaDestino != null && celdaDestino.getContenido() instanceof ObjetoEntorno) {
        ObjetoEntorno objeto = (ObjetoEntorno) celdaDestino.getContenido();
        if (!objeto.pasoLibre) {
            return; // No se puede pisar una pared
        }
    }
    
    //verificar si la celda destino tiene un ENEMIGO o ENEMIGO LETAL (NO se mueve, solo recibe daño)
 if (celdaDestino != null && celdaDestino.getContenido() instanceof Enemigo) {
        Enemigo enemigo = (Enemigo) celdaDestino.getContenido() ;
    enemigo.hacerDanio(this);
    return;
}
    
    if (nuevaX != this.x || nuevaY != this.y) {
        //recolectar dinero de la celda destino ANTES de mover al personaje
        if (celdaDestino != null) {
            mapa.recolectarDeCelda(this, nuevaX, nuevaY);
        }
        
        //limpiar la celda anterior
        Celda celdaAnterior = mapa.conseguirCelda(this.x, this.y);
        if (celdaAnterior != null && celdaAnterior.getContenido()  == this) {
            celdaAnterior.setContenido(null);}

        this.x = nuevaX;
        this.y = nuevaY;
        
        //colocar al personaje en la nueva celda
        mapa.setPersonaje(this, nuevaX, nuevaY);
        Celda celdaActual = mapa.conseguirCelda(this.x, this.y);
        if (celdaActual != null && celdaActual.getContenido() instanceof EnemigoLetal){
            this.dinero = -9999;
            return;
        }
    }
}

public void recolectarDinero(int cantidad) {
    this.dinero += cantidad;
}
public boolean danioRecibido(int danio) {
    this.dinero -= danio; // AHORA USA EL DANIO DEL Enemigo
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
        return celda.getContenido() != null && celda.getContenido() instanceof Pared;
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
            
            //verificar si hay una pared en la posición (nx, ny)
            boolean hayPared = verificarPared(nx, ny);
            
            if (hayPared) {
                //si es una nueva pared o cambiamos de objetivo, reiniciar golpes
                if (paredObjetivo == null || !paredObjetivo.equals(nx + "," + ny)) {
                    paredObjetivo = nx + "," + ny;
                    golpesRestantes = 2;
                }
                golpesRestantes--;
                ultimoGolpe = tiempoActual;
                
                // si los golpes llegan a 0, se rompe la pared
                if (golpesRestantes <= 0) {
                    // Eliminar la pared de la celda
                    Celda celda = conseguirCelda(nx, ny);
                    if (celda != null) {
                       celda.setContenido(null);
                    }
                    // Reiniciar contadores
                    golpesRestantes = 2;
                    paredObjetivo = null;
                }      
                encontroPared = true;
                break;
            }
        }    
}

public void activarBonus(Bonus bonus) {
    velocidadOriginal = velocidad; 
    this.bonusActivado = true;
    this.bonusFin = System.currentTimeMillis() + bonus.duracionMs;
}

public void actualizarBonus() {
    if (bonusActivado && System.currentTimeMillis() >= bonusFin) {
        desactivarBonus();
    }
}
public void desactivarBonus() {
    this.bonusActivado = false;
     this.velocidad = velocidadOriginal;
}
}