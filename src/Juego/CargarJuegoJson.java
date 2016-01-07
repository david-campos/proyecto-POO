/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Juego;

import Excepciones.CargadorException;
import Excepciones.CeldaObjetivoNoValida;
import Mapa.Celda;
import Mapa.Mapa;
import Objetos.Objeto;
import Personajes.Enemigo;
import Personajes.Francotirador;
import Personajes.Jugador;
import Personajes.Marine;
import Personajes.Zapador;
import Utilidades.Adaptador;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class CargarJuegoJson implements CargadorJuego {
    private String archivo;
    private String tipoJugador;
    private String nombre;

    public CargarJuegoJson(String archivo, String tipoJugador, String nombre) {
        this.archivo = archivo;
        this.tipoJugador = tipoJugador;
        this.nombre = nombre;
    }

    public String getTipoJugador() {
        return tipoJugador;
    }

    public void setTipoJugador(String tipoJugador) {
        this.tipoJugador = tipoJugador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    @Override
    public Juego cargarJuego(Consola con) throws CargadorException, FileNotFoundException, IllegalArgumentException {
        Mapa mapa;
        if(nombre != null && tipoJugador != null){
            if(archivo != null && archivo.lastIndexOf(".") != -1
                    && archivo.substring(archivo.lastIndexOf(".")+1).equals(Utilidades.ConstantesGenerales.EXTENSION_MAPA)){
                BufferedReader lector = new BufferedReader(
                        new FileReader(new File(archivo))
                );
                mapa = new GsonBuilder()
                        .registerTypeAdapter(Celda.class, new Adaptador<Celda>())
                        .registerTypeAdapter(Objeto.class, new Adaptador<Objeto>())
                        .registerTypeAdapter(Enemigo.class, new Adaptador<Enemigo>())
                        .create().fromJson(lector, Mapa.class);

                //Asignamos a las celdas y enemigos el mapa
                for(Celda c: mapa.getCeldas())
                    c.setMapa(mapa);
                
                Juego juego = new Juego(mapa, null, con);

                Jugador jug;
                switch (tipoJugador.toLowerCase()) {
                    default:
                        jug = new Marine(nombre, mapa, juego);
                        break;
                    case "zapador":
                        jug = new Zapador(nombre, mapa, juego);
                        break;
                    case "francotirador":
                        jug = new Francotirador(nombre, mapa, juego);
                        break;
                }
                mapa.setJuego(juego);
                try {
                    mapa.setJugador(jug);
                } catch (CeldaObjetivoNoValida ex) {
                    //No debería ocurrir.
                    Logger.getLogger(CargarJuegoJson.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                for(Enemigo e: mapa.getEnemigos()){
                    try {
                        e.setMapa(mapa);
                        e.setJuego(juego);
                    } catch (CeldaObjetivoNoValida ex) {
                       //No debería ser no válida, fue guardado ahí.
                    }
                }
                juego.setJugador(jug);
                
                return juego;
            }else
                throw new IllegalArgumentException("Archivo a cargar no válido. (no tiene extensión .map)");
        }else
            return null;
    }
    
}