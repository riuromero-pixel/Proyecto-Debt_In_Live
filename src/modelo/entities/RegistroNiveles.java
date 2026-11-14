package src.modelo.entities;

public class RegistroNiveles {

    public static CreadorNivel paraNivel(int nivel) {
        switch (nivel) {
            case 1: return new CreadorNivel1();
            case 2: return new CreadorNivel2();
            case 3: return new CreadorNivel3();
            default: throw new IllegalArgumentException("Nivel desconocido: " + nivel);
        }
    }
}