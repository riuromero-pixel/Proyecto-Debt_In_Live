package src.modelo.entities;

import java.util.List;

public class RegistroNiveles {

    private static final List<CreadorNivel> CREADORES = List.of(
        new CreadorNivel1(),
        new CreadorNivel2(),
        new CreadorNivel3()
    );

    public static CreadorNivel paraNivel(int nivel) {
        for (CreadorNivel creador : CREADORES) {
            if (creador.getNivel() == nivel) {
                return creador;
            }
        }
        throw new IllegalArgumentException("Nivel desconocido: " + nivel);
    }
}