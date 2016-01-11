package Excepciones;

/**
 * Excepción lanzada cuando la celda objetivo no es válida, por ejemplo
 * cuando se intenta mover un {@code Personaje} a una celda {@code NoTransitable}
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class CeldaObjetivoNoValida extends MapaExcepcion{

    /**
     * Crea una nueva excepción de celda objetivo no válida
     */
    public CeldaObjetivoNoValida() {
        super();
    }

    /**
     * Crea una nueva excepción de celda objetivo no válida
     * @param message mensaje de la excepción
     */
    public CeldaObjetivoNoValida(String message) {
        super(message);
    }
    
}
