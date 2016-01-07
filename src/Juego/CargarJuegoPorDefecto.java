/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Juego;

import Excepciones.CeldaObjetivoNoValida;
import Mapa.Mapa;
import Personajes.Francotirador;
import Personajes.Jugador;
import Personajes.Marine;
import Personajes.Zapador;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author crist
 */
public final class CargarJuegoPorDefecto implements CargadorJuego{
    private String tipoJugador;
    private String nombre;
    private double modDificultad;
    
    public CargarJuegoPorDefecto(String nombre, String tipoJugador, double dificultad) {
        this.nombre = nombre;
        this.tipoJugador = tipoJugador;
        modDificultad = dificultad;
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
    
    @Override
    public Juego cargarJuego(Consola c){
        boolean[] tipos = Mapa.getPlantillaAleatoria(30, 30);
        Mapa map = new Mapa("Mapa1", "Es un bonito mapa de prueba.", 30, 30, tipos, null);
        
        Juego juego = new Juego(map, null, c);
        juego.setModDificultad(modDificultad);
        Jugador jug;
        switch (tipoJugador.toLowerCase()) {
            default:
                jug = new Marine(nombre, map, juego);
                break;
            case "zapador":
                jug = new Zapador(nombre, map, juego);
                break;
            case "francotirador":
                jug = new Francotirador(nombre, map, juego);
                break;
        }
        map.setJuego(juego);
        try {
            map.setJugador(jug);
        } catch (CeldaObjetivoNoValida ex) {
                map.hacerTransitable(map.getPosicionInicial(), false);
                try {
                    map.setJugador(jug);
                } catch (CeldaObjetivoNoValida ex1) {
                    Logger.getLogger(CargarJuegoJson.class.getName()).log(Level.SEVERE, null, ex1);
                }
        }
        
        map.setEnemigosAleatorio();
        map.setObjetosAleatorio();
        juego.setJugador(jug);
        return juego;
    }  
}
