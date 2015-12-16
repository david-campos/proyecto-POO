/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Juego;

import Excepciones.MapaExcepcion;
import Mapa.Mapa;
import Personajes.Francotirador;
import Personajes.Jugador;
import Personajes.Marine;
import Personajes.Zapador;

/**
 *
 * @author crist
 */
public final class CargarJuegoPorDefecto implements CargadorJuego{
    private String tipoJugador;
    private String nombre;
    
    /**
     *
     * @param nombre
     * @param tipoJugador
     */
    public CargarJuegoPorDefecto(String nombre, String tipoJugador) {
        this.nombre = nombre;
        this.tipoJugador = tipoJugador;
    }

    /**
     *
     * @return
     */
    public String getTipoJugador() {
        return tipoJugador;
    }

    /**
     *
     * @param tipoJugador
     */
    public void setTipoJugador(String tipoJugador) {
        this.tipoJugador = tipoJugador;
    }

    /**
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     *
     * @return
     */
    @Override
    public Juego cargarJuego(){
        boolean[] tipos = Mapa.getPlantillaAleatoria(40, 30);
        Mapa map = new Mapa("Mapa1", "Es un bonito mapa de prueba.", 40, 30, tipos, null);
        
        Juego juego = new Juego(map);
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
        map.setJugador(jug);
        try{
            map.setEnemigosAleatorio();
        }catch(MapaExcepcion e){
            juego.log(e.getMessage());
        }
        map.setObjetosAleatorio();
        juego.setJugador(jug);
        return juego;
    }  
}
