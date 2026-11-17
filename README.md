# Proyecto: Debt_In_Life

## 1. Integrantes del Equipo 

- Cabrera, David 
- Del Rio, Axel 
- Romero, Ignacio 
- Vasquez, Joaquin

## 2. Dominio y Alcance del Sistema 

## Descripción del Problema

Buscamos desarrollar un videojuego inspirado en Pac-man. El jugador debe recolectar la cantidad de puntos objetivo (Dinero), evitando los enemigos los cuales restarán puntos al jugador si los toca. La partida tendrá 3 niveles nombrados como Estudiante, Adulto y como nivel final Jubilado.

## Objetivo del Sistema

Será un juego con controles sencillos y conocidos con mecánicas básicas de un juego al estilo Pac-man. Con la particularidad de agregar variabilidad en el gameplay y una temática casual. Siguiendo las bases que identifican al paradigma de programación oriendado a objetos.

### Funcionalidades principales

## Sistema de Enemigos

- Los enemigos serán customizados como entidades que pueden restar dinero, como por ejemplo, una consola de videojuegos o impuestos.

- Los enemigos aparecen en el mapa al comenzar el nivel.

- A medida que se aumentan el nivel aparecerán más enemigos.

- Los enemigos se ubican de forma aleatoria a lo largo del mapa. Al crearse, cada uno recibe una estrategia de movimiento que mantiene durante todo el nivel: algunos **persiguen al jugador** (siempre se acercan a la celda vecina más cercana a él) y otros son **errantes** (se mueven al azar entre las celdas libres vecinas). La mezcla de ambos tipos dificulta la partida.

- En el nivel final aparece además un **Enemigo Letal** que elimina al jugador con solo tocarlo y tiene una habilidad la cual lo teletransporta cada cierto tiempo.

## Mecanicas de Juego

- El jugador comenzará la partida con una cantidad ($) preestablecida en el nivel 1 (Estudiante) y deberá recolectar los puntos (Dinero) que estarán repartidos en el mapa.

- El jugador perderá la partida en cuanto sus puntos caigan por debajo de 0 y deberá elegir entre reintentar, volver al menú o salir.

- Habrán mejoras (Bonus) que afectan la velocidad del personaje según el nivel, y que aparecen repartidas por el mapa. Tienen duración limitada.

- Al recolectar $3000 el jugador avanza al siguiente nivel (o gana el juego si estaba en el último).

- Dependiendo del nivel, el jugador puede **romper paredes** (tecla R) o **crear paredes** (tecla C): el Adulto puede llenar una fila/columna entera, el Jubilado solo puede colocar una pared.

- El personaje "Estudiante" tiene una habilidad especial con (tecla F) esta le permite dar un salto de una casilla permitiendole atravezar paredes, enemigos , etc (esta habilidad solo la tiene el Estudiante y tiene un coldown de 7 segundos)

## Interfaz Grafica

- Visualización de cada nivel con un mapa, enemigos, aspecto del jugador.
El jugador es una persona  que debe evitar sumar deudas a lo largo de su vida en una perspectiva top-down, en la cual en los niveles habrán objetos o entidades que harán que el jugador vaya acumulando deudas y por lo tanto se le reste dinero. El jugador deberá recoger dinero esparcido por el mapa donde en una determinada cantidad de dinero avanzará de nivel. Además, dependiendo del nivel, el jugador podrá construir o destruir paredes a voluntad. Como también, contará con potenciadores temporales donde dependiendo del nivel, afectará de manera diferente al jugador, como por ejemplo en velocidad al moverse o inmunidad. El juego tendrá 3 niveles: Estudiante (Con un mapa ambientado en una escuela), Adulto (ambientando el mapa en una oficina) y Jubilado (ambientando el mapa en un lugar calmo y natural), a medida que se vaya avanzando en los niveles, sera mas difícil sumar dinero. Los escenarios e incluso el mismo personaje que usará el jugador serán variados dependiendo el nivel y los enemigos serán distintos, como por ejemplo un enemigo podría ser Los Impuestos, habrá enemigos que siguen al jugador y otros que avanzarán de manera aleatoria en el mapa.

- El juego muestra un **HUD inferior** con el dinero del jugador, la cantidad de monedas que quedan en el mapa, la fuerza, la velocidad y el nivel actual.

- Hay una **pantalla de introducción** con un GIF animado antes de llegar al menú principal.

- El movimiento del personaje y de los enemigos se dibuja con **interpolación suave** (se deslizan entre celdas en vez de saltar de golpe), y la **cámara sigue al jugador** de forma fluida dentro de un viewport de 15x10 celdas.


## Sistema de Récords y Sonido

- La partida pide el nombre del jugador al iniciar. Los récords se guardan en una **base de datos SQLite** al perder o al ganar, y se pueden consultar desde el menú con el botón "Récords". Se muestran los 10 mejores ordenados por tiempo jugado.

- La música cambia según el nivel, con un tema diferente para el menú. Existe una opción en el menú para **activar/desactivar el sonido**.


Algunos conceptos de los mapas: 
![Mapa Nivel 1](images/AI%20slop%20concepto%20mapa%20LV1%20Debt_in_life.png)
![Mapa Nivel 2](images/AI%20slop%20concepto%20mapa%20LV2.png)

Algunos conceptos de los enemigos: 
![Enemigos](images/valio%20la%20pena%20secar%20un%20lago%20concepto%20enemigos%20DEbt_in_LIfe.png)

Concepto personaje estudiante: 
![Personaje Jugable](images/sprite4peladofrontal.png)

## 3. Arquitectura y Diseño

#### Patron de Diseño Adicional: Factory Method y Strategy

- **Nombre del Patrón:** **Factory Method**.
  
`CreadorNivel` es la **única fuente de verdad** de cada nivel: no solo crea
el mapa y el personaje, sino que también define:

- `crearPersonaje(x, y, mapa)` → qué Personaje (Estudiante/Adulto/Jubilado)
- `cargarPlantilla(mapa)` → la plantilla del mapa
- `getMensajeInicio()` → diálogo al entrar
- `getMensajeDerrota()` → mensaje al perder
- `getMensajeVictoria()` → mensaje al ganar
- `getSpawnX/Y()` → dónde aparece el jugador

Para agregar un nivel nuevo:

1. Crear `CreadorNivel4 extends CreadorNivel` con esos 5 métodos.
2. Agregar `CREADORES.put(4, new CreadorNivel4());` en `RegistroNiveles`.

**Nada más.** No hay que tocar el Controlador, ni el GestorReglas, ni el
GestorNiveles: todos le preguntan al `CreadorNivel` en lugar de hacer
`if (nivel == X)`.
-----------------------------------------------------
- **Nombre del Patrón:** **Strategy**.

- **Problema:** La clase Enemigo contenía toda la lógica de movimiento adentro de un solo método. Todos los enemigos se comportaban igual y no había forma de tener variación sin meter un `if` gigante.

- **Justificación:** Se utilizó este patrón para encapsular los distintos comportamientos de movimiento de los enemigos. Se definió la interfaz `EstrategiaMovimiento` con dos implementaciones: `MovimientoPerseguidor` (elige la celda vecina libre más cercana al jugador) y `MovimientoErrante` (elige al azar entre las celdas vecinas libres). La clase `Enemigo` guarda una estrategia y le **delega** el movimiento con `estrategia.mover(...)`, sin saber cuál es. Cada enemigo recibe su estrategia al ser creado (la elige `GeneradorNivelMapa`) y la conserva durante el nivel; la variación está **entre enemigos**, no dentro de uno. Agregar un comportamiento nuevo es crear una clase que implemente la interfaz, sin modificar `Enemigo` ni `Mapa`.

- **Similitud entre estrategias:** Ambas recorren las cuatro celdas vecinas y atacan si encuentran al jugador, porque ese es el esqueleto de moverse en una grilla. Lo que las diferencia, y lo que justifica que sean dos clases, es el **criterio de elección** de la celda destino: azar en `MovimientoErrante`, menor distancia Manhattan al jugador en `MovimientoPerseguidor`. Una posible mejora es subir el recorrido de vecinos a una clase base común.

-EstrategiaMovimiento.java
 ```java
public interface EstrategiaMovimiento {
    void mover(Enemigo enemigo, Personaje personaje, Mapa mapa);
}
 ```
-Enemigo.java 
 ```java
public class Enemigo extends ObjetoEntorno {
    private int x;
    private int y;
    private int danio;
    private EstrategiaMovimiento estrategia;

    public Enemigo(int x, int y, int danio, EstrategiaMovimiento estrategia, int nivel) {
        super(false);
        this.x = x;
        this.y = y;
        this.danio = danio;
        this.estrategia = estrategia;
        // ...
    }

    public void actualizarMovimiento(Mapa mapa, Personaje jugador) {
        if (estrategia != null) {
            estrategia.mover(this, jugador, mapa);
        }
    }
}
 ```
-Asignación en GeneradorNivelMapa
 ```java
EstrategiaMovimiento estrategia = random.nextBoolean()
        ? new MovimientoPerseguidor()
        : new MovimientoErrante();
Enemigo enemigo = new Enemigo(x, y, 10 + (nivel * 2), estrategia, nivel);
 ```
-----------------------------------------------------   
Nombre del Patrón: Singleton.

Justificación:

Problema: Antes teníamos la lógica del sonido dentro de la clase Mapa con un método estático que abría un clip nuevo cada vez que lo llamábamos. ¿Qué pasaba? Si cambiabas de nivel o se ejecutaba mal, se solapaban los audios, se consumía memoria innecesariamente y cualquier clase podía instanciar la reproducción sin control.

Código Anterior (estaba en la clase Mapa):
<img width="713" height="208" alt="image" src="https://github.com/user-attachments/assets/d2e5e78c-89ef-4336-b8c5-2b2dec0d82bd" />





Solución: Aplicamos el patrón Singleton con la clase Reproductor_audio. Le pusimos un constructor privado para que nadie pueda crear objetos con new en otras clases, y un método getInstancia() que controla si ya hay una instancia creada; si existe la devuelve, y si no, la crea de cero. Con esto nos aseguramos de que haya un único reproductor en todo el programa, simplificamos los llamados y evitamos que los temas se encimen (agregando un detenerSonido() antes de arrancar otro).

Código Nuevo / Aplicado (Clase nueva Reproductor_audio):
<img width="980" height="642" alt="image" src="https://github.com/user-attachments/assets/02766d17-54ce-4e81-abed-bf83e2cfd743" />
<img width="848" height="588" alt="image" src="https://github.com/user-attachments/assets/61bbc140-1cbd-4168-849b-57f9a2f22dd7" />

- **Interfaz adicional:** CreadorParedes

- **Problema:** Algunos personajes (Adulto y Jubilado) pueden crear paredes, pero cada uno lo hace de forma distinta. Si el controlador o la vista tuvieran que chequear el tipo concreto de personaje para saber si puede crear paredes, se complicaba el código y se rompía el polimorfismo.

- **Justificación:** Se creó la interfaz CreadorParedes con el método crearParedes(). Las clases Adulto y Jubilado la implementan, cada una con su propia lógica (el Adulto llena una fila/columna entera en la dirección actual, el Jubilado coloca una sola pared). De esta forma, el controlador solo pregunta if (jugador instanceof CreadorParedes) y llama a crearParedes() sin importarle qué personaje concreto es. El Estudiante, que no implementa la interfaz, simplemente no puede usar la tecla C.


-CreadorParedes.java // Interfaz Nueva
 ```java
public interface CreadorParedes {
   void crearParedes();
}
```

-Uso desde el ControladorJuego (tecla C):
 ```java
case 'c': case 'C':
   if (jugador instanceof CreadorParedes){
       CreadorParedes creador = (CreadorParedes) jugador;
       creador.crearParedes();
       vista.repaint();
   }
   break;
```


Diagrama de Clases UML (Conceptual)
https://excalidraw.com/#room=06e19fa734b3c8b1a63a,D3CVevWOrU4yFgqbOUNB2Q

<img width="4696" height="3078" alt="diagrama uml debt in live" src="https://github.com/user-attachments/assets/856e080b-062b-4a25-81eb-a4e17f708bd6" />

4. Stack Tecnológico
Lenguaje: Java 17

IDE: Visual Studio Code

Base de Datos: MySQL 8.0 (para persistencia de High Scores)

Framework de IGU: Java Swing

Control de Versiones: Git y GitHub Classro
