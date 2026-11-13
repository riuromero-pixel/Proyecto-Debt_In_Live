package src.modelo.entities;

import src.modelo.map.Mapa;

public record NivelCreado(Mapa mapa, Personaje jugador, int nivel) {
}