package src.modelo.entities;

import src.modelo.map.Bonus;
import src.modelo.map.Celda;
import src.modelo.map.Mapa;
import src.modelo.map.ContenidoCelda;
import src.modelo.map.Golpeable;

public abstract class Personaje implements ContenidoCelda {
    private int x;
    private int y;
    private int velocidad;
    private int velocidadOriginal;
    private int dinero;
    private boolean bonusActivado = false;
    private long ultimoGolpe;
    private final Mapa mapa;
    private String direccionActual;
    private long bonusFin = 0;

    public Personaje(int x, int y, int velocidad, int dinero, boolean bonusActivado, Mapa mapa){
        this.x = x;
        this.y = y;
        this.velocidad = velocidad;
        this.velocidadOriginal = velocidad;
        this.dinero = dinero; 
        this.bonusActivado = bonusActivado;
        this.ultimoGolpe = 0;
        this.mapa = mapa;
        this.direccionActual = null;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getDinero() { return dinero; }
    public int getVelocidad() { return velocidad; }
    public boolean isBonusActivado() { return bonusActivado; }
    public Mapa getMapa() { return mapa; }
    protected  void setVelocidad(int velocidad) { this.velocidad = velocidad; }
    public String getDireccionActual() { return direccionActual; }

    protected int getBonusExtra() {
        return 0;
    }

    @Override
    public boolean bloqueaPaso() {
        return false;
    }

    public abstract String getNombreTipo();

    public int getTipoSprite() {
        return 0;
    }

    @Override
    public boolean interactuar(Personaje p) {
        return false;
    }

    public void saltar() {
    }

    protected void setPosicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void crearParedes() {
    }

    public void morir() {
        this.dinero = -9999;
    }

    public void moverse(String tecla, int anchoMapa, int altoMapa) {
        char direccion = tecla.toUpperCase().charAt(0);
        int nuevaX = this.x;
        int nuevaY = this.y;

        this.direccionActual = String.valueOf(direccion);

        switch (direccion) {
            case 'W': nuevaY = Math.min(altoMapa - 1, this.y - 1); break;
            case 'S': nuevaY = Math.max(0, this.y + 1); break;
            case 'A': nuevaX = Math.max(0, this.x - 1); break;
            case 'D': nuevaX = Math.min(anchoMapa - 1, this.x + 1); break;
        }

        Celda celdaDestino = mapa.conseguirCelda(nuevaX, nuevaY);
        if (celdaDestino == null) return;

        // Si la celda bloquea, el personaje "choca" (le pega al enemigo/pared) y no entra.
        ContenidoCelda contenidoDestino = celdaDestino.getContenido();
        if (contenidoDestino != null && contenidoDestino.bloqueaPaso()) {
            contenidoDestino.interactuar(this);   // despacho dinámico
            return;
        }

        // Si no bloquea, el personaje entra y la celda resuelve lo que haya.
        if (nuevaX != this.x || nuevaY != this.y) {
            mapa.recolectarDeCelda(this, nuevaX, nuevaY); // interactúa con contenido y contenidoDebajo
            mapa.limpiarCeldaDe(this);
            this.x = nuevaX;
            this.y = nuevaY;
            mapa.setPersonaje(this, nuevaX, nuevaY);
        }
    }

    public void recolectarDinero(int cantidad) {
        this.dinero += cantidad;
    }

    public boolean danioRecibido(int danio) {
        this.dinero -= danio;
        return this.dinero < 0;
    }

    private Celda conseguirCelda(int x, int y) {
        if (mapa == null) return null;
        return mapa.conseguirCelda(x, y); 
    }    
    
    public void romperParedes() {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - ultimoGolpe < 400) return;

        int[][] direcciones = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        for (int[] dir : direcciones) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            Celda celda = conseguirCelda(nx, ny);
            if (celda == null || celda.getContenido() == null) continue;

            ContenidoCelda contenido = celda.getContenido();
            if (contenido instanceof Golpeable golpeable) {
                if (golpeable.recibirGolpe()) {
                    mapa.limpiarCelda(nx, ny);
                }
                ultimoGolpe = tiempoActual;
                break;
            }
        }
    }

    public void activarBonus(Bonus bonus) {
        if (!this.bonusActivado) {
            this.velocidadOriginal = this.velocidad;
        }
        this.bonusActivado = true;
        this.bonusFin = System.currentTimeMillis() + bonus.getDuracionMs();
        this.velocidad += bonus.getBonusVelocidad() + this.getBonusExtra();
    }
    public void actualizarBonus() {
        if (bonusActivado && System.currentTimeMillis() >= bonusFin) {
            desactivarBonus();
        }
    }
    public void desactivarBonus() {
        if (!this.bonusActivado) {
            return;
        }
        this.bonusActivado = false;
        this.velocidad = this.velocidadOriginal;
    }
}