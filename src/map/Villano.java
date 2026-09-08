package src.map;

import java.util.Random;

public class Villano extends ObjetoEntorno {
    public int x;
    public int y;
    public int danio;
    private Random random;

    public Villano(int x, int y, int danio) {
        // Un villano permite pisar su celda para iniciar la interacción de combate/daño
        super(true);
        this.x = x;
        this.y = y;
        this.danio = danio;
        this.random = new Random();
    }

    /**
     * Mueve al villano a una direccion aleatoria (arriba, abajo, izquierda, derecha)
     * dentro de los limites del mapa, respetando las colisiones con las paredes.
     */
    public void mover(Mapa mapa) {
        // Direcciones posibles: 0 = Arriba, 1 = Abajo, 2 = Izquierda, 3 = Derecha
        int direccion = random.nextInt(4);
        int nuevoX = this.x;
        int nuevoY = this.y;

        switch (direccion) {
            case 0: nuevoY--; break; // Arriba
            case 1: nuevoY++; break; // Abajo
            case 2: nuevoX--; break; // Izquierda
            case 3: nuevoX++; break; // Derecha
        }

        // 1. Verificar colision
        if (nuevoX >= 0 && nuevoX < mapa.ancho && nuevoY >= 0 && nuevoY < mapa.alto) {
            Celda celdaDestino = mapa.celdas[nuevoY][nuevoX];

            // 2. Verificar colision con paredes u objetos infranqueables
            boolean hayPared = celdaDestino.contenido != null && !celdaDestino.contenido.pasoLibre;

            if (!hayPared) {
                // Liberar la celda actual
                mapa.celdas[this.y][this.x].contenido = null;

                // Actualizar las coordenadas
                this.x = nuevoX;
                this.y = nuevoY;

                // Ocupar la nueva celda
                mapa.celdas[this.y][this.x].contenido = this;
            }
        }
    }

    /**
     * Retorna la cantidad de danio que inflige el villano al colisionar/atacar.
     */
    public int hacerDanio() {
        return this.danio;
    }
}