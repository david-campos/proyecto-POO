/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Comandos;

import Excepciones.ComandoExcepcion;
import Juego.Consola;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public final class ComandoAyuda implements Comando{
    private String tema;
    private Consola consola;
    
    /**
     *
     * @param tema
     * @param consola
     */
    public ComandoAyuda(String tema, Consola consola) {
        this.tema = tema;
        this.consola = consola;
    }

    /**
     *
     * @return
     */
    public Consola getConsola() {
        return consola;
    }

    /**
     *
     * @param consola
     */
    public void setConsola(Consola consola) {
        this.consola = consola;
    }

    /**
     *
     * @return
     */
    public String getTema() {
        return tema;
    }

    /**
     *
     * @param tema
     */
    public void setTema(String tema) {
        this.tema = tema;
    }
    
    /**
     *
     * @throws ComandoExcepcion
     */
    @Override
    public void ejecutar() throws ComandoExcepcion {
       try{
           ayuda();
       }catch(FileNotFoundException e){
           throw new ComandoExcepcion("No se encuentra ese tema de ayuda.");
       }catch(IOException e){
           throw new ComandoExcepcion("Error al cargar la ayuda... Error: " + e.getMessage());
       }
    }
    
    private void ayuda() throws FileNotFoundException, IOException{
        if(tema == null)
            tema = "index";
        String archivo = "ayuda/"+tema.toLowerCase()+".help";
        String cadena;
        FileReader f = new FileReader(archivo);
        BufferedReader buf = new BufferedReader(f);
        
        while((cadena = buf.readLine())!=null) {
            consola.imprimir(cadena);
        }
    }
}
