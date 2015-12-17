/*
 * 
 */
package proyecto2;

import Excepciones.CargadorException;
import Juego.*;
import Mapa.Punto;

/**
 * Proyecto de Programación Orientada a Objetos,
 * versión 3
 * @author David Campos y Cristofer Canosa
 */
public class Proyecto2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        Consola c = new ConsolaNormal();
        String tipo = c.leer("Qué tipo de tipo quieres ser, tipo (marine/francotirador/zapador)? ").toLowerCase();
        String cargadorPD = c.leer("Vale... le cargamos el juego por defecto (si/s/no/n)? ").toLowerCase();
        CargadorJuego cargante;
        switch(cargadorPD){
            case "si":
            case "s":
                String nombre = c.leer("Oye... dime tu nombre premoh: ");
                cargante = (CargadorJuego) new CargarJuegoPorDefecto(nombre, tipo);
                break;
            case "no":
            case "n":
                String carpeta = c.leer("Buah loco, dime la carpeta del mapa anda: ");
                if(!carpeta.isEmpty())
                    cargante = new CargarJuegoDeFicheros(carpeta + "/mapa.csv", carpeta+"/objetos.csv", carpeta+"/npcs.csv", tipo);
                else
                    cargante = new CargarJuegoDeFicheros(tipo);
                break;
            default:
                c.imprimir("Chiquillo... ¡las opciones eran si, s, no y n!");
                return;
        }
        try {
            Juego j = cargante.cargarJuego();
            j.iniciar();
        } catch (CargadorException ex) {
            c.imprimir("Hubo un errorcillo cargando el juego ^^' : " + ex.getMessage());
        }
        
    }
    
}
