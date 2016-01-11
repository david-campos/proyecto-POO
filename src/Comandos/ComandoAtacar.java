package Comandos;

import Excepciones.ComandoExcepcion;
import Excepciones.EnergiaInsuficienteException;
import Excepciones.PosicionFueraDeAlcanceException;
import Excepciones.PosicionFueraDeRangoException;
import Mapa.Celda;
import Utilidades.Punto;
import Mapa.Transitable;
import Personajes.Jugador;
import java.util.Random;

/**
 * Comando de ataque para el jugador.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public final class ComandoAtacar implements Comando{
    private Jugador jug;
    private String nombre;
    private int x, y;
    
    /**
     * Crea un comando atacar con el nombre, posición y jugador indicados.
     * @param jug El jugador que ha de atacar
     * @param nombre Nombre del enemigo atacado (null para atacar toda la celda)
     * @param x Posición x de la celda (absoluta)
     * @param y Posición y de la celda (absoluta)
     * @see Utilidades.Punto
     * @see Personajes.Jugador#atacar(Personajes.Personaje) 
     * @see Personajes.Jugador#atacar(Mapa.Transitable) 
     */
    public ComandoAtacar(Jugador jug, String nombre, int x, int y){
        //nombre puede ser null
        this.nombre = nombre;
        setX(x);
        setY(y);
        this.jug = jug;
    }

    /**
     * Obtiene el jugador asociado al comando.
     * @return El jugador asociado
     */
    public Jugador getJug() {
        return jug;
    }

    /**
     * Cambia el jugador asociado al comando.
     * @param jug El jugador asociado
     */
    public void setJug(Jugador jug) {
        this.jug = jug;
    }

    /**
     * Obtiene el nombre del enemigo a atacar
     * @return El nombre del enemigo a atacar
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Fija el nombre del enemigo a atacar
     * @param nombre nombre del enemigo
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la componente x de la celda a atacar
     * @return la componente x (absoluta) de la celda a atacar
     * @see Utilidades.Punto
     */
    public int getX() {
        return x;
    }

    /**
     * Fija/cambia la componente x de la celda a atacar
     * @param x la componente x (absoluta) de la celda a atacar
     * @see Utilidades.Punto
     */
    public final void setX(int x) {
        this.x = x;
    }

    /**
     * Obtiene la componente y de la celda a atacar
     * @return la componente y (absoluta) de la celda a atacar
     * @see Utilidades.Punto
     */
    public int getY() {
        return y;
    }

    /**
     * Fija/cambia la componente y de la celda a atacar
     * @param y La componente y de la celda a atacar
     * @see Utilidades.Punto
     */
    public final void setY(int y) {
        this.y = y;
    }
    
    /**
     * Fija ambas componentes (x e y) de la celda a atacar
     * @param x componente x de la celda a atacar
     * @param y componente y de la celda a atacar
     * @see Utilidades.Punto
     */
    public void setDir(int x, int y){
        setX(x);
        setY(y);
    }
    
    /**
     * Provoca al jugador el ataque a la celda en caso de que {@code nombre}
     * sea null o al enemigo indicado en caso contrario
     * @throws ComandoExcepcion excepción a la que se convierten todas las
     *         excepciones devueltas por {@link Personajes.Jugador#atacar}
     */
    @Override
    public void ejecutar() throws ComandoExcepcion {
        if(jug != null){
            Celda c = jug.getMapa().getCelda(new Punto(x, y));
            if(c instanceof Transitable){
                    try {
                        if(nombre == null || nombre.isEmpty())
                            jug.atacar((Transitable) c);
                        else
                            jug.atacar(((Transitable)c).getEnemigo(nombre));
                    } catch (PosicionFueraDeRangoException | PosicionFueraDeAlcanceException ex) {
                        throw new ComandoExcepcion(ex.getMessage());
                    } catch (EnergiaInsuficienteException ex) {
                        if(new Random().nextFloat() > 0.4) Utilidades.Sonido.play("bostezo");
                        throw new ComandoExcepcion(ex.getMessage());
                    }
            }else
                throw new ComandoExcepcion("La celda a atacar es no transitable...");
        }
    }
}
