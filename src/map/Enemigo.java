package src.map;

import java.util.Random;
import src.entities.Personaje;

public class Enemigo extends ObjetoEntorno {
    public int x;
    public int y;
    public int danio;
    private Random random;

    public Enemigo(int x, int y, int danio) {
        //un Enemigo permite pisar su celda para iniciar la interacción de combate/daño
        super(true);
        this.x = x;
        this.y = y;
        this.danio = danio;
        this.random = new Random();
    }

    /**
     * Mueve al Enemigo a una direccion aleatoria (arriba, abajo, izquierda, derecha)
     * dentro de los limites del mapa, respetando las colisiones con las paredes.
     */
    public void mover(Mapa mapa) {
        //direcciones posibles: 0 = Arriba, 1 = Abajo, 2 = Izquierda, 3 = Derecha
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

            // 2. Verificar si hay un Personaje en la celda destino
            if (celdaDestino.contenido instanceof Personaje) {
                Personaje jugador = (Personaje) celdaDestino.contenido;
                jugador.danioRecibido(danio);
                return; // No se mueve, solo hace danio
            }

            // 3. Verificar si hay OTRO ENEMIGO en la celda destino (NO puede pisarlo)
            if (celdaDestino.contenido instanceof Enemigo) {
                return; // No se mueve, ya hay otro enemigo
            }

            // 4. Verificar si hay PARED en la celda destino (NO puede pisarla)
            if (celdaDestino.contenido instanceof Pared) {
                return; // No se mueve, hay pared
            }

            // 5. Verificar si hay DINERO en la celda destino (NO puede pisarlo)
            // El enemigo NO puede pisar dinero, se queda donde esta
            if (celdaDestino.contenido instanceof Dinero) {
                return; // No se mueve, hay dinero
            }

            // 6. Si llegamos aqui, podemos mover
            // Liberar la celda actual
            mapa.celdas[this.y][this.x].contenido = null;

            // Actualizar las coordenadas
            this.x = nuevoX;
            this.y = nuevoY;

            // Ocupar la nueva celda
            mapa.celdas[this.y][this.x].contenido = this;
        }
    }

    /**
     * Retorna la cantidad de danio que inflige el Enemigo al colisionar/atacar.
     */
    public int hacerDanio() {
        return this.danio;
    }
}