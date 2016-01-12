package Personajes;

import Excepciones.CeldaObjetivoNoValida;
import Excepciones.DireccionMoverIncorrecta;
import Excepciones.EnergiaInsuficienteException;
import Excepciones.MaximoObjetosException;
import Excepciones.MaximoPesoException;
import Excepciones.PosicionFueraDeAlcanceException;
import Excepciones.PosicionFueraDeRangoException;
import Juego.ConsolaGrafica;
import Juego.Juego;
import Utilidades.Punto;

/**
 * Tipo de enemigo LightFloater
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public final class LightFloater extends Floater{

    /**
     * Crea un nuevo LightFloater
     * @param nombre nombre del enemigo
     * @param vida vida del enemigo
     * @param energiaPorTurno energía con la que cuenta al principio de cada turno
     * @param posicion posición del enemigo
     * @param juego juego al que se enlaza
     * @throws CeldaObjetivoNoValida si la posición no es válida
     */
    public LightFloater(String nombre, int vida, int energiaPorTurno, int[] posicion, Juego juego) throws CeldaObjetivoNoValida {
        super(nombre, vida, energiaPorTurno, posicion, juego);
    }

    /**
     * Crea un nuevo LightFloater
     * @param nombre nombre del enemigo
     * @param posicion posición del enemigo
     * @param juego juego al que se enlaza
     * @throws CeldaObjetivoNoValida si la posición no es válida
     */
    public LightFloater(String nombre, int[] posicion, Juego juego) throws CeldaObjetivoNoValida {
        super(nombre, posicion, juego);
    }

    /**
     * El LightFloater tiene una ia mejor, porque es más listo.
     */
    @Override
    public void iaTurno() {
        while(getEnergia() > 0){
            boolean hizoAlgo = false;
            if(getEnergia() > 3*ConstantesPersonajes.GE_MOVER){
                try {
                    iaAtacar();
                    hizoAlgo = true;
                    juego.imprimirEstado();
                } catch (PosicionFueraDeRangoException | PosicionFueraDeAlcanceException | EnergiaInsuficienteException ex){/*No hace nada*/}
            }
            int intentos = 0;
            for(;;){
                try {
                    if(!hizoAlgo)
                        iaMover();
                    hizoAlgo = true;
                } catch (EnergiaInsuficienteException ex) {
                    /*No hace nada*/
                } catch (CeldaObjetivoNoValida ex) {
                    if(++intentos < 4) 
                        continue;
                }
                break;
            }
            try {
                hizoAlgo = hizoAlgo || iaRecogerObjetos();
            } catch (MaximoObjetosException | MaximoPesoException | EnergiaInsuficienteException ex) {/*No hace nada*/}           
            
            if(mapa.getJugador().enRango(getPos())){
                if(juego.getConsola() instanceof ConsolaGrafica){
                    ((ConsolaGrafica)juego.getConsola()).imprimirMapa(true);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        //Pues nada...
                    }
                }
            }
            if(!hizoAlgo)
                break;
        }
    }

    @Override
    protected void iaMover() throws EnergiaInsuficienteException, CeldaObjetivoNoValida{
        String dir="N";
        //Se mueve aleatoriamente si no ve al jugador
        if(!enRango(mapa.getJugador().getPos())){
            dir = direccionAleatoria();
        }else if(getEnergia() <= ConstantesPersonajes.GE_MOVER*3)
            dir = iaDirHuir();
        else if(!enAlcance(mapa.getJugador().getPos()))
            dir = iaDirJugador();
        else
            throw new EnergiaInsuficienteException("¡No mover!");
        try {
            try {
                mover(dir);
            } catch (DireccionMoverIncorrecta ex) {
                //Las direcciones de movimiento son correctas
            }
        } catch (CeldaObjetivoNoValida ex) {
            try {
                mover(direccionAleatoria());    //Vuelve a intentarlo
            } catch (DireccionMoverIncorrecta ex1) {
               //Las direcciones de movimiento devueltas son correctas
            }
        }
    }

    @Override
    protected int obtenerConsumoEnergiaMover() {
        return (int)Math.ceil(super.obtenerConsumoEnergiaMover()/2.0);
    }

    
    
    private String iaDirHuir() {
           Punto vector = Punto.resta(getPos(), mapa.getJugador().getPos());
        if(Math.abs(vector.x) > Math.abs(vector.y))
            return vector.x>0?"E":"O";
        else if(Math.abs(vector.x) == Math.abs(vector.y))
            if(r.nextDouble() > 0.5)
                return vector.x>0?"E":"O";
            else
                return vector.y>0?"S":"N";
        else
            return vector.y>0?"S":"N";
    }
}
