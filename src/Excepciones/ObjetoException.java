package Excepciones;

/**
 * Excepción relativa a un objeto
 * @author crist
 */
public abstract class ObjetoException extends Exception {
    
    public ObjetoException() {   
        super();
    }
    public ObjetoException(String string) {
        super(string);
    }
    
}
