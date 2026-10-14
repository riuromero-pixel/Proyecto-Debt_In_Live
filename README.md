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

-Los enemigos serán customizados como entidades que pueden restar dinero a como por ejemplo, una consola de videojuegos o impuestos.

- Los enemigos aparecen en el mapa al comenzar el nivel.

- A medida que se aumentan el nivel aparecerán más enemigos.

- Los enemigos estarán a lo largo de todo el mapa de forma aleatoria y tendrán un recorrido preestablecido para dificultar la partida al jugador.

## Mecanicas de Juego

- El jugador comenzará la partida con una cantidad ($) preestablecida en el nivel 1 (Estudiante) y deberá recolectar los puntos (Dinero) que estarán repartidos en el mapa.

- El jugador perderá la partida en cuanto sus puntos caigan por debajo de 0 y empezará desde el nivel 1.

- Habrán mejoras donde cada una será diferente dependiendo el nivel actual y que aparecerán cada cierta cantidad de dinero recolectado.

## Interfaz Grafica

- Visualización de cada nivel con un mapa, enemigos, aspecto del jugador.
El jugador es una persona  que debe evitar sumar deudas a lo largo de su vida en una perspectiva top-down, en la cual en los niveles habrán objetos o entidades que harán que el jugador vaya acumulando deudas y por lo tanto se le reste dinero. El jugador deberá recoger dinero esparcido por el mapa donde en una determinada cantidad de dinero avanzará de nivel. Además, dependiendo del nivel, el jugador podrá construir o destruir paredes a voluntad. Como también, contará con potenciadores temporales donde dependiendo del nivel, afectará de manera diferente al jugador, como por ejemplo en velocidad al moverse o inmunidad. El juego tendrá 3 niveles: Estudiante (Con un mapa ambientado en una escuela), Adulto (ambientando el mapa en una oficina) y Jubilado (ambientando el mapa en un lugar calmo y natural), a medida que se vaya avanzando en los niveles, sera mas difícil sumar dinero. Los escenarios e incluso el mismo personaje que usará el jugador serán variados dependiendo el nivel y los enemigos serán distintos, como por ejemplo un enemigo podría ser Los Impuestos, habrá enemigos que siguen al jugador y otros que avanzarán de manera aleatoria en el mapa.

Algunos conceptos de los mapas: 
![Mapa Nivel 1](images/AI%20slop%20concepto%20mapa%20LV1%20Debt_in_life.png)
![Mapa Nivel 2](images/AI%20slop%20concepto%20mapa%20LV2.png)

Algunos conceptos de los enemigos: 
![Enemigos](images/valio%20la%20pena%20secar%20un%20lago%20concepto%20enemigos%20DEbt_in_LIfe.png)

Concepto personaje estudiante: 
![Personaje Jugable](images/AI%20slop%20concepto%20personaje%20jugable%20LV1.png)

## 3. Arquitectura y Diseño

#### Patron de Diseño Adicional: Factory Method y Strategy

- **Nombre del Patrón:** **Factory Method**.

- **Justificación:** Se utilizó este patrón para centralizar la creación de los objetos `Personaje` (Estudiante, Adulto, Jubilado). Se creó la clase `PersonajeFactory` con un método estático `crearPersonaje(int nivel, int x, int y, Mapa mapa)` que decide qué subclase instanciar según el nivel recibido. Esto hará que el controlador solo pida "un personaje para el nivel N" y la factory se encarga de instanciar el correcto. Así, agregar un nuevo tipo de personaje no requerirá modificar el código que gestiona los niveles.
 
- **Nombre del Patrón:** **Strategy**.

- **Justificación:** Se utilizó este patrón para encapsular los distintos comportamientos de movimiento de los enemigos. El que persigue al jugador y el errante que se mueve aleatoriamente. Se definió la interfaz `EstrategiaMovimiento` con las implementaciones `MovimientoPerseguidor` y `MovimientoErrante`. La clase `Enemigo` contiene una estrategia y le **delega** el movimiento, permitiendo intercambiarlo en runtime con `setEstrategia()`. Logrando asi poder agregar un nuevo comportamiento en un futuro solo haciendo una clase nueva, sin modificar `Enemigo` ni `Mapa`.

- **Nombre del Patrón:** ** Singleton**.

- **Justificación:** Se empleo debido a que con la música no necesitabamos tener más de una instancia, para simplificar sus llamados, para evitar solapamientos y tener un mayor encapsulamiento, esto mediante la clase 'Reproductor_audio' el cual posee un builder privado para restringir el acceso externo a la creación de objetos, impidiendo que cualquier otra clase pueda instanciar la clase mediante el operador new y un metodo getInstancia() que revisa si tiene o no una instancia, en el caso de que tenga, la devuelve, y si no tiene, la crea.

#### Diagrama de Clases UML (Conceptual)**
https://excalidraw.com/#room=06e19fa734b3c8b1a63a,D3CVevWOrU4yFgqbOUNB2Q



![Diagrama UML](images/Untitled-2026-09-28-2334.png)

## 4. Stack Tecnológico 

- **Lenguaje:** Java 17
- **IDE:** Visual Studio Code
- **Base de Datos:** MySQL 8.0 (para persistencia de High Scores)
- **Framework de IGU:** Java Swing
- **Control de Versiones:** Git y GitHub Classroom
