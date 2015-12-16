/*
 * 
 */
package Excepciones;

/**
 * Una excepción "Bolita de lana" que debería ser controlada por algún gatito juguetón.
 * @author ElJüsticieroMisteryo
 */
public class BolitaDeLanaException extends Exception{
    private final String color;

    /**
     *
     * @param color
     */
    public BolitaDeLanaException(String color) {
        this.color = color;
    }
   
    @Override
    public String toString() {
        return "Una bolita de lana " + color + " se ha escapado :c";
    }

    @Override
    public String getMessage() {
        return "Una bolita de lana " + color + " se ha escapado.";
    }
    
}
