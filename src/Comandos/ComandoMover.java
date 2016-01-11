/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Comandos;

import Excepciones.CeldaObjetivoNoValida;
import Excepciones.ComandoExcepcion;
import Excepciones.DireccionMoverIncorrecta;
import Excepciones.EnergiaInsuficienteException;
import Personajes.Jugador;
import java.util.Random;

/**
 * Comando que permite al jugador moverse por el mapa. El movimiento se realiza
 * indicando simplemente la dirección y por la distancia de una casilla.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 * 
 * @see #setDir(java.lang.String)
 */
public final class ComandoMover implements Comando{
    private String dir;
    private Jugador jug;

    /**
     * Crea una nueva instancia de este comando
     * @param jug jugador a mover
     * @param direccion direccion de movimiento (ver {@code setDir})
     * @see #setDir(java.lang.String) 
     */
    public ComandoMover(Jugador jug, String direccion){
        setDir(direccion);
        this.jug = jug;
    }
    
    /**
     * Obtiene el valor de dirección de movimiento especificado.
     * @return El valor de dirección de movimiento especificado.
     */
    public String getDir() {
        return dir;
    }

    /**
     * Cambia la dirección del movimiento del jugador realizado cuando se
     * llame a {@code ejecutar} este comando. <br>
     * Los valores posibles son:
     * <ul>
     * <li> 'N' para mover hacia el norte (parte superior de la pantalla,
     * componente 'y' de la posición menor)
     * <li> 'S' para mover hacia el sur (parte inferior de la pantalla,
     * componente 'y' de la posición mayor)
     * <li> 'O' para mover hacia el oeste (parte izquierda de la pantalla,
     * componente 'x' de la posición menor)
     * <li> 'E' para mover hacia el este (parte derecha de la pantalla,
     * componente 'x' de la posición mayor)
     * </ul>
     * No se distinguen mayúsculas y minúsculas.
     * @param dir nuevo valor de dirección de movimiento
     */
    public void setDir(String dir) {
        this.dir = dir.toLowerCase();
    }

    /**
     * Obtiene el jugador
     * @return jugador que realizará el movimiento
     */
    public Jugador getJug() {
        return jug;
    }

    /**
     * Cambia el jugador
     * @param jug jugador que realizará el movimiento
     */
    public void setJug(Jugador jug) {
        this.jug = jug;
    }
    
    @Override
    public void ejecutar() throws ComandoExcepcion{
        if(jug!=null)
            try{
                jug.mover(dir);
                if(new Random().nextFloat() > 0.8) Utilidades.Sonido.play("hey");
                else if(new Random().nextFloat() > 0.5) Utilidades.Sonido.play("pasos_escalera");
                jug.getJuego().impMapa();
            } catch (CeldaObjetivoNoValida ex){
                Utilidades.Sonido.play("no");
                throw new ComandoExcepcion(ex.getMessage());
            } catch (DireccionMoverIncorrecta ex){
                Utilidades.Sonido.play("carraspeo");
                throw new ComandoExcepcion(ex.getMessage());
            } catch (EnergiaInsuficienteException ex){
                if(new Random().nextFloat() > 0.4) Utilidades.Sonido.play("bostezo");
                throw new ComandoExcepcion(ex.getMessage());
            }
    }
    
}
