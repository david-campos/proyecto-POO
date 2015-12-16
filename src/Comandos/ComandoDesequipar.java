/*
 * 
 */
package Comandos;

import Excepciones.ComandoExcepcion;
import Personajes.Jugador;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public final class ComandoDesequipar implements Comando{
    private Jugador jug;
    private String nombre;

    /**
     *
     * @param jug
     * @param nombre
     */
    public ComandoDesequipar(Jugador jug, String nombre){
        this.jug = jug;
        this.nombre = nombre;
    }

    /**
     *
     * @return
     */
    public Jugador getJugador() {
        return jug;
    }

    /**
     *
     * @param jug
     */
    public void setJugador(Jugador jug) {
        this.jug = jug;
    }

    /**
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     *
     * @throws ComandoExcepcion
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
                            throw new Exception("¿Desequipar qué?");
                    }
                }catch(Exception e){
                    throw new ComandoExcepcion(e.getMessage());
                }
            }else
                throw new ComandoExcepcion("No se ha indicado qué desequipar...");
    }
    
}
