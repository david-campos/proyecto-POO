package Comandos;

import Excepciones.ComandoExcepcion;
import Juego.Consola;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Comando de ayuda, imprime un tema de ayuda por la consola.
 * Los temas de ayuda son archivos '.help' situados en la carpeta
 * 'ayuda/'.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public final class ComandoAyuda implements Comando{
    private String tema;
    private Consola consola;
    
    /**
     * Crea un nuevo {@code ComandoAyuda}, sobre un tema concreto y para
     * lanzarse en una consola concreta.
     * @param tema el nombre del archivo '.help' sin la extensión
     * @param consola consola sobre la que imprimir la ayuda
     * 
     * @see Juego.Consola
     */
    public ComandoAyuda(String tema, Consola consola) {
        this.tema = tema;
        this.consola = consola;
    }

    /**
     * Obtiene la consola
     * @return la consola donde se imprimirá la ayuda
     */
    public Consola getConsola() {
        return consola;
    }

    /**
     * Cambia la consola
     * @param consola la consola donde se imprimirá la ayuda
     */
    public void setConsola(Consola consola) {
        this.consola = consola;
    }

    /**
     * Obtiene el tema de ayuda que se imprimirá
     * @return el tema de ayuda (nombre del archivo '.help' situado en 'ayuda/')
     */
    public String getTema() {
        return tema;
    }

    /**
     * Fija el tema de ayuda que se imprimirá
     * @param tema nombre del archivo '.help' situado en 'ayuda/'
     */
    public void setTema(String tema) {
        this.tema = tema;
    }
    
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
            tema = "index"; //Si el tema es null, se imprime la ayuda general
        String archivo = "ayuda/"+tema.toLowerCase()+".help";
        String cadena;
        FileReader f = new FileReader(archivo);
        BufferedReader buf = new BufferedReader(f);
        
        while((cadena = buf.readLine())!=null) {
            consola.imprimir(cadena);
        }
    }
}
