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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Carga el juego a partir de un archivo .map con la información del mapa en
 * Json (tal como lo guarda el editor de mapas).
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class CargarJuegoJson implements CargadorJuego {
    private String archivo;
    private String tipoJugador;
    private String nombre;

    /**
     * Crea un nuevo cargador de juego desde json
     * @param archivo archivo del que se cargará el juego
     * @param tipoJugador tipo de jugador que se usará (ver {@link #setTipoJugador}.
     * @param nombre nombre del jugador con el que el juego será iniciado
     */
    public CargarJuegoJson(String archivo, String tipoJugador, String nombre) {
        this.archivo = archivo;
        this.tipoJugador = tipoJugador;
        this.nombre = nombre;
    }

    /**
     * Obtiene el tipo de jugador
     * @return el tipo de jugador
     * @see #setTipoJugador
     */
    public String getTipoJugador() {
        return tipoJugador;
    }

    /**
     * <p>Cambia el tipo de jugador. El tipo de jugador debería ser uno de los
     * siguientes:</p>
     * <ul>
     * <li> Marine
     * <li> Francotirador
     * <li> Zapador
     * </ul>
     * <p>No se distinguen mayúsculas y minúsculas, de no coincidir con ninguno
     * de los anteriores, el tipo fijado cuando se llame a {@code cargarJuego}
     * será "marine".</p>
     * @param tipoJugador tipo de jugador deseado
     */
    public void setTipoJugador(String tipoJugador) {
        this.tipoJugador = tipoJugador;
    }

    /**
     * Obtiene el nombre del jugador que tendrá el juego que se cargue
     * @return El nombre del jugador que tendrá el juego
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Cambia el nombre del jugador que tendrá el juego
     * @param nombre nuevo nombre para el jugador
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el archivo del que se pretende cargar el juego
     * @return El archivo del que se pretende cargar el juego
     */
    public String getArchivo() {
        return archivo;
    }

    /**
     * Cambia el archivo del que se pretende cambiar el juego
     * @param archivo dirección relativa o absoluta al archivo a cargar
     */
    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    @Override
    public Juego cargarJuego(Consola con) throws CargadorException, FileNotFoundException, IllegalArgumentException {
        Mapa mapa;
        if(nombre != null && tipoJugador != null){
            if(archivo != null && archivo.lastIndexOf(".") != -1
                    && archivo.substring(archivo.lastIndexOf(".")+1).equals(Utilidades.ConstantesGenerales.EXTENSION_MAPA)
                    && new File(archivo).exists()){
                BufferedReader lector = new BufferedReader(
                        new FileReader(new File(archivo))
                );
                mapa = new GsonBuilder()
                        .registerTypeAdapter(Celda.class, new Adaptador<Celda>())
                        .registerTypeAdapter(Objeto.class, new Adaptador<Objeto>())
                        .registerTypeAdapter(Enemigo.class, new Adaptador<Enemigo>())
                        .create().fromJson(lector, Mapa.class);

                //Asignamos el juego al mapa
                Juego juego = new Juego(mapa, null, con);
                mapa.setJuego(juego);
                
                //Asignamos a las celdas y enemigos el mapa
                for(Celda c: mapa.getCeldas())
                    c.setMapa(mapa);
                for(Enemigo e: mapa.getEnemigos()){
                    try {
                        e.setMapa(mapa);
                        e.setJuego(juego); //Asignamos de paso el juego a los enemigos
                    } catch (CeldaObjetivoNoValida ex) {
                       //No debería ser no válida, fue guardado ahí.
                    }
                }
                
                //Creamos el jugador
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
                try {
                    mapa.setJugador(jug); //y lo asignamos al mapa
                } catch (CeldaObjetivoNoValida ex) {
                    //No debería ocurrir.
                    Logger.getLogger(CargarJuegoJson.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                juego.setJugador(jug); //asignamos el jugador al juego
                
                return juego; //devolvemos el juego
            }else
                throw new IllegalArgumentException("Archivo a cargar no válido (no existe o no tiene extensión .map)");
        }else
            return null;
    }
    
}