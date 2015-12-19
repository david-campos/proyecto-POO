/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Menus;

import Juego.CargadorJuego;
import Juego.CargarJuegoDeFicheros;
import Juego.CargarJuegoPorDefecto;
import Juego.Consola;
import Juego.ConsolaNormal;
import Juego.Juego;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class MenuConsola implements Menu{

    @Override
    public void lanzar() {
        Consola c = new ConsolaNormal(null);
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
        } catch (Exception ex) {
            c.imprimir("Hubo un errorcillo cargando el juego ^^' : " + ex.getMessage());
        }
    }
    
}
