package Juego;

/**
 * Interfaz para implementar la consola
 * @author crist
 */
public interface Consola {

    /**
     * Imprime el mapa de juego
     */
    public void imprimirMapa();

    /**
     * Limpia la consola, de ser posible
     */
    public void limpiar();
    /**
     * Imprime de la forma que se requiera el mensaje, añade un salto de
     * línea al final.
     * @param mensaje mensaje a mostrar
     */
    public void imprimir (String mensaje);

    /**
     * Imprime el mensaje sin añadir un salto de línea al final.
     * @param mensaje mensaje a mostrar
     */
    public void imprimirSinSalto(String mensaje);

    /**
     * Cierra la consola, si es posible
     */
    public void cerrar();

    /**
     * Imprime el estado del jugador de la forma que considere oportuna
     * @param mensaje mensaje con el estado del jugador
     */
    public void imprimirEstado(String mensaje);
    /**
     * Sirve para conseguir datos del usuario
     * @param descripcion texto a mostrar previo a la introducción de los datos
     * @return Cadena introducida por el usuario.
     */
    public String leer (String descripcion);  
    /**
     * Sirve para conseguir datos del usuario
     * @return Cadena introducida por el usuario
     */
    public String leer();
}
