package Excepciones;

/**
 * Excepción relativa al mapa
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public abstract class MapaExcepcion extends Exception{
    public MapaExcepcion() {
        super();
    }

    public MapaExcepcion(String message) {
        super(message);
    }
}
