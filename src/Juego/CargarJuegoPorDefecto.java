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
 * Crea un juego por defecto. El juego por defecto es un juego con una dificultad
 * concreta en un mapa generado aleatoriamente.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 * @author crist
 */
public final class CargarJuegoPorDefecto implements CargadorJuego{
    private String tipoJugador;
    private String nombre;
    private double modDificultad;
    
    /**
     * Crea un nuevo cargador de juego por defecto
     * @param nombre nombre del jugador que tendrá el juego
     * @param tipoJugador tipo de jugador que tendrá el juego (ver {@link #setTipoJugador}
     * @param dificultad dificultad con la que se iniciará el juego
     */
    public CargarJuegoPorDefecto(String nombre, String tipoJugador, double dificultad) {
        this.nombre = nombre;
        this.tipoJugador = tipoJugador;
        modDificultad = dificultad;
    }

    /**
     * Obtiene el tipo de jugador
     * @return El tipo de jugador
     * @see #setTipoJugador
     */
    public String getTipoJugador() {
        return tipoJugador;
    }

    /**
     * Fija el tipo de jugador. Para ver los posibles tipos de jugador
     * consultar {@link CargarJuegoJson#setTipoJugador}
     * @param tipoJugador tipo de jugador que se desea fijar
     */
    public void setTipoJugador(String tipoJugador) {
        this.tipoJugador = tipoJugador;
    }

    /**
     * Obtiene el nombre actual para el jugador que se creará
     * @return El nombre que tendrá el jugador cuando se cargue el juego
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la dificultad con la que se creará el juego
     * @return La dificultad con la que se creará el juego
     */
    public double getModDificultad() {
        return modDificultad;
    }

    /**
     * Modifica la dificultad con la que se iniciará el juego
     * @param modDificultad la nueva dificultad deseada
     */
    public void setModDificultad(double modDificultad) {
        this.modDificultad = modDificultad;
    }
    
    /**
     * Modifica el nombre que tomará el jugador cuando se cargue el juego
     * @param nombre nuevo nombre deseado para el jugador creado
     */
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
