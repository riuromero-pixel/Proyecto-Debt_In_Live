package src.modelo.entities;

import java.util.LinkedHashMap;
import java.util.Map;
public class RegistroNiveles {

    private static final Map<Integer, CreadorNivel> CREADORES = new LinkedHashMap<>();

        static {
            CREADORES.put(1, new CreadorNivel1());
            CREADORES.put(2, new CreadorNivel2());
            CREADORES.put(3, new CreadorNivel3());
        }

        public static CreadorNivel paraNivel(int nivel) {
            CreadorNivel creador = CREADORES.get(nivel);
            if (creador == null) throw new IllegalArgumentException("Nivel desconocido: " + nivel);
            return creador;
        }

        public static boolean esUltimoNivel(int nivel) {
            int max = CREADORES.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);
            return nivel >= max;
        }

}