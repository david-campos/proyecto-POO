package Excepciones;

/**
 * Excepción lanzada cuando el objeto no es equipable
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class ObjetoNoEquipableException extends ObjetoException{

    public ObjetoNoEquipableException() {
        super();
    }

    public ObjetoNoEquipableException(String message) {
        super(message);
    }
    
}
