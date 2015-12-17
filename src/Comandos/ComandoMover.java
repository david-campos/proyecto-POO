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
import Excepciones.PersonajeException;
import Personajes.Jugador;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public final class ComandoMover implements Comando{
    private String dir;
    private Jugador jug;
    public ComandoMover(Jugador jug, String direccion){
        dir = direccion.toLowerCase();
        this.jug = jug;
    }
    
    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir.toLowerCase();
    }

    public Jugador getJug() {
        return jug;
    }

    public void setJug(Jugador jug) {
        this.jug = jug;
    }
    
    @Override
    public void ejecutar() throws ComandoExcepcion{
        if(jug!=null)
            try{
                jug.mover(dir);
                jug.getJuego().impMapa();
            } catch (CeldaObjetivoNoValida | DireccionMoverIncorrecta | EnergiaInsuficienteException ex) {
                throw new ComandoExcepcion(ex.getMessage());
            }
    }
    
}
