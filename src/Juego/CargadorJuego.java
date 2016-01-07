/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Juego;

import Excepciones.CargadorException;
import java.io.FileNotFoundException;

/**
 *
 * @author crist
 */
public interface CargadorJuego {
    public Juego cargarJuego(Consola c) throws CargadorException, FileNotFoundException, IllegalArgumentException ;    
}
