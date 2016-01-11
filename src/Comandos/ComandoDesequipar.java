package Comandos;

import Excepciones.ComandoExcepcion;
import Personajes.Jugador;

/**
 * Comando que lleva al jugador a desequipar alguno de los objetos que lleva
 * equipados. El objeto tratará de ser introducido en la mochila, en caso de no
 * caber, será arrojado a la celda.
 * Los objetos equipados pueden ser:
 * <ul>
 * <li> El arma derecha ('arma')
 * <li> El arma izquierda ('arma_izq')
 * <li> La armadura ('armadura')
 * <li> Los binoculares ('binoculares')
 * </ul>
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 * 
 * @see Jugador#desequipar(Objetos.Objeto) 
 */
public final class ComandoDesequipar implements Comando{
    private Jugador jug;
    private String nombre;

    /**
     * Crea un nuevo ComandoDesequipar para el jugador y nombre indicados
     * @param jug jugador que desequipará el objeto
     * @param nombre identificador del objeto a desequipar (ver {@link ComandoDesequipar#setNombre(java.lang.String)})
     */
    public ComandoDesequipar(Jugador jug, String nombre){
        this.jug = jug;
        setNombre(nombre);
    }

    /**
     * Obtiene el jugador
     * @return el jugador que desequipará el objeto
     */
    public Jugador getJugador() {
        return jug;
    }

    /**
     * Fija el jugador
     * @param jug jugador que desequipará el objeto
     */
    public void setJugador(Jugador jug) {
        this.jug = jug;
    }

    /**
     * Obtiene el identificador del objeto a desequipar, este puede tomar
     * uno de los siguientes valores:
     * <ul>
     * <li> 'arma' - el arma derecha
     * <li> 'arma_izq' - el arma izquierda
     * <li> 'armadura' - la armadura
     * <li> 'binoculares' - los binoculares
     * </ul>
     * @return  El identificador del objeto a desequipar
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Cambia el identificador del objeto a desequipar, este puede tomar
     * uno de los siguientes valores:
     * <ul>
     * <li> 'arma' - el arma derecha
     * <li> 'arma_izq' - el arma izquierda
     * <li> 'armadura' - la armadura
     * <li> 'binoculares' - los binoculares
     * </ul>
     * @param nombre nuevo nombre del objeto a desequipar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * {@inheritDoc }
     * Si el identificador de objeto fijado no se corresponde con ninguno válido
     * se lanzará una excepción
     * @throws ComandoExcepcion excepción a la que se castea cualquier excepción
     *          lanzada por {@link Jugador#desequipar(Objetos.Objeto)}
     */
    @Override
    public void ejecutar() throws ComandoExcepcion {
        if(jug!=null)
            if(nombre != null && !nombre.isEmpty()){
                try{
                    switch(nombre.toLowerCase()){
                        case "arma":
                            jug.desequipar(jug.getArma());
                            break;
                        case "arma_izq":
                            jug.desequipar(jug.getArma_izq());
                            break;
                        case "armadura":
                            jug.desequipar(jug.getArmadura());
                            break;
                        case "binoculares":
                            jug.desequipar(jug.getBinoculares());
                            break;
                        default:
                            throw new ComandoExcepcion("¿Desequipar qué?");
                    }
                }catch(Exception e){
                    throw new ComandoExcepcion(e.getMessage());
                }
            }else
                throw new ComandoExcepcion("No se ha indicado qué desequipar...");
    }
    
}
