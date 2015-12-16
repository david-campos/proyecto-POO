/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Comandos;

import Excepciones.ComandoExcepcion;
import Excepciones.PersonajeException;
import java.util.ArrayList;

/**
 *
 * @author ElJüsticieroMisteryo
 */
public class ComandoCompuesto implements Comando{
    private final ArrayList<Comando> comandos;

    /**
     *
     */
    public ComandoCompuesto() {
        comandos = new ArrayList();
    }

    /**
     *
     * @param tam
     */
    public ComandoCompuesto(int tam){
        comandos = new ArrayList(tam);
    }

    /**
     *
     * @return
     */
    public int size() {
        return comandos.size();
    }

    /**
     *
     * @param e
     * @return
     */
    public boolean add(Comando e) {
        return comandos.add(e);
    }

    /**
     *
     * @param c
     * @return
     */
    public boolean remove(Comando c) {
        return comandos.remove(c);
    }

    /**
     *
     * @return
     */
    public ArrayList<Comando> getComandos() {
        return (ArrayList) comandos.clone();
    }
    
    /**
     *
     * @throws ComandoExcepcion
     */
    @Override
    public void ejecutar() throws ComandoExcepcion {
        for(Comando c: comandos)
            c.ejecutar();
    }
}
