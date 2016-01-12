package Editor;

import Excepciones.CeldaObjetivoNoValida;
import Mapa.Celda;
import Utilidades.CeldaGrafica;
import Mapa.ConstantesMapa;
import Mapa.NoTransitable;
import Mapa.Transitable;
import Personajes.Enemigo;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JComponent;

/**
 * <p> Adaptador (listener) general para las celdas del editor.</p>
 * <p> Responde a los eventos de ratón del usuario.</p>
 *
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class CeldasML extends MouseAdapter {

    private int botonPulsado;
    private CeldaGrafica inicial;
    private int celdasRecorridas; //Usada para lo de click derecho
    private CeldaGrafica objetivo;
    private final Editor ed;

    //Obtiene la celda gráfica del editor que se corresponde con este evento
    private CeldaGrafica celdaGrafica(MouseEvent e) {
        for (CeldaGrafica cg : ed.getCeldas()) {
            if (cg.getComponente().equals(e.getComponent())) {
                return cg;
            }
        }
        return null;
    }

    /**
     * Crea una nueva instancia, que funcionará para el editor indicado
     * @param ed editor sobre el que funcionará este adaptador
     */
    public CeldasML(Editor ed) {
        botonPulsado = MouseEvent.NOBUTTON;
        inicial = null;
        celdasRecorridas = 0;
        objetivo = null;
        this.ed = ed;
    }

    /*
     * Cuando se suelta el ratón, según el botón soltado se pueden realizar varias acciones.
     *  - Si el botón soltado es el derecho, y no se ha arrastrado por varias celdas,
     * se despliega el menú contextual de la celda.
     *  - Si el botón soltado es el izquierdo, en caso de estar arrastrándose enemigos
     * de una celda a otra, han de difinitivamente moverse a la nueva celda.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        CeldaGrafica cg = celdaGrafica(e);
        if (ed.getHerramienta() != Editor.Herramienta.NORMAL)
            return; //Sólo debería funcionar con la herramienta NORMAL
        
        switch (botonPulsado) {
            //Si es el derecho y no se ha arrastrado por ahí, desplegamos el menú de celda
            case MouseEvent.BUTTON3:
                if (celdasRecorridas == 0) {
                    new MenuCelda(cg, ed).show(e.getComponent(), e.getX(), e.getY());
                }
                break;
            case MouseEvent.BUTTON1:
                //Si la celda inicial no era nula, movemos los enemigos
                moverEnemigos();
                break;
        }
        celdasRecorridas = 0; //No hemos recorrido ninguna celda ya
        inicial = null; //No hay inicial ya
        botonPulsado = MouseEvent.NOBUTTON; //El botón pulsado, vuelve a ser ninguno
    }

    //Cuando la rueda del ratón es girada sobre una celda, esta debe cambiar de tipo,
    //por desgracia el scroll panel se come este evento jaja
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //Solo responder si la herramienta es la NORMAL
        if (ed.getHerramienta() != Editor.Herramienta.NORMAL) {
            return;
        }
        //inicial != null -> indica que se arrastran enemigos o intercambios de celdas, no realizar esto
        if (inicial != null) return;
        
        CeldaGrafica cg = celdaGrafica(e);
        Celda c = ed.getMapa().getCelda(cg.getId());
        if (c == null) return; //no se encontró la celda correspondiente (sería un error)

        if (c instanceof Transitable) {
            c.tipo = (c.tipo + e.getWheelRotation()) % ConstantesMapa.CE_REPR_TRANS.length;
        } else {
            c.tipo = (c.tipo + e.getWheelRotation()) % ConstantesMapa.CE_REPR_NOTRANS.length;
        }
        ed.repintarCelda(cg); //Repintamos la celda para hacer visible al usuario el cambio de tipo
    }

    //Cuando se presiona un botón, lo registramos, y ajustamos los parámetros
    //necesarios por si es el principio de un arrastre.
    @Override
    public void mousePressed(MouseEvent e) {
        //Sólo con la herramienta NORMAL
        if (ed.getHerramienta() != Editor.Herramienta.NORMAL)
            return;
        if(botonPulsado != MouseEvent.NOBUTTON)
            return;

        CeldaGrafica cg = celdaGrafica(e);
        botonPulsado = e.getButton(); //El botón pulsado
        celdasRecorridas = 0; //0 celdas recorridas
        switch (botonPulsado) {
            case MouseEvent.BUTTON1:
                ed.toggleSeleccionada(cg);

                //Si tiene enemigos... podría ser inicial para arrastrar
                if (ed.getMapa().getCelda(cg.getId()) instanceof Transitable) {
                    Transitable transitable = (Transitable) ed.getMapa().getCelda(cg.getId());
                    if (transitable.getNumEnemigos() > 0) {
                        inicial = cg; //Esta es la celda donde comenzó todo
                        celdasRecorridas = 0;
                        objetivo = null; //Todavía no hay celda objetivo
                    }
                }
                break;
            case MouseEvent.BUTTON3:
                inicial = cg;
                objetivo = null;
                break;
        }
    }

    //Ocurren muchas cosas cada vez que el ratón entra en una celda:
    // 1º las coordenadas indicadas en el editor se actualizan.
    // 2º Se incrementa el contador de celdas recorridas
    // 3º si la herramienta es NORMAL...
    //    - Se cambia el borde de la celda
    //    - Si se está arrastrando enemigos se "simula" el movimiento a la nueva celda
    //    - Si se están intercambiando celdas Transitable<->NoTransitable se intercambian
    // 4º si la herramienta es MOVER_ENEMIGO o MOVER_OBJETO, cambiamos cursor (si el movimiento es posible)
    @Override
    public void mouseEntered(MouseEvent e) {
        CeldaGrafica cg = celdaGrafica(e);
        ed.infoCoordenadas(cg.getId());
        if(botonPulsado != MouseEvent.NOBUTTON)
            celdasRecorridas++; //Recorrimos una celda más
        
        switch (ed.getHerramienta()) {
            case NORMAL:
                //Cambiamos el borde de la celda
                if (ed.getSeleccionada() == null || !e.getComponent().equals(ed.getSeleccionada().getComponente()))
                    ((JComponent) e.getComponent()).setBorder(Editor.BORDE_HOVER);
                
                if (botonPulsado == MouseEvent.BUTTON1 && inicial != null) {
                    //Si el pulsado es el botón izquierdo, simulamos movimiento aquí
                    cg.getComponente().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    cg.setDelante(ed.obtenerImagen("ed_mover_enemigo"));
                    objetivo = cg;
                }else if (botonPulsado == MouseEvent.BUTTON3 && inicial != null) {
                    //Si está pulsado el botón derecho se cambia la celda de transitable a no transitable o viceversa
                    //según la celda inicial
                    objetivo = cg;
                    //Si es la primera, intercambiamos la inicial
                    if (celdasRecorridas == 1) ed.toggleTransitable(inicial);
                    //Obtenemos la celda del mapa
                    Celda c = ed.getMapa().getCelda(cg.getId());
                    if (c == null) return;
                    //Si ya es transitable o intransitable acordemente a la incial (ya cambiada)
                    //no hay que hacer nada.
                    if (ed.getMapa().getCelda(inicial.getId()) instanceof Transitable) {
                        if (c instanceof Transitable) return;
                    }else{
                        if (c instanceof NoTransitable) return;
                    }
                    //Por último, intercambiamos la celda
                    ed.toggleTransitable(cg);
                }
                break;
            case MOVER_ENEMIGO:
                if (ed.getMapa().getPosicionInicial().equals(cg.getId())){
                    ((JComponent) e.getComponent()).setBorder(Editor.BORDE_NOMOVER);
                    break; //No se puede mover enemigos a la posición inicial
                }
            case MOVER_OBJETO:
                if (ed.getMapa().getCelda(cg.getId()) instanceof Transitable) {
                    cg.getComponente().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    ((JComponent) e.getComponent()).setBorder(Editor.BORDE_MOVER);
                } else
                    ((JComponent) e.getComponent()).setBorder(Editor.BORDE_NOMOVER);
                break;
        }
    }

    // Cuando el ratón abandona la celda, la repintamos, y ponemos infoCoordenadas al estado inicial
    @Override
    public void mouseExited(MouseEvent e) {
        CeldaGrafica cg = celdaGrafica(e);
        ed.repintarCelda(cg); //Siempre repintamos la celda al salir de ella.
        if (ed.getSeleccionada() == null || !e.getComponent().equals(ed.getSeleccionada().getComponente()))
            ((JComponent) e.getComponent()).setBorder(Editor.BORDE_DEF);
        ed.infoCoordenadas(null);
    }

    // Doble click sobre una celda abre las propiedades,
    // un click simple con la herramienta MOVER mueve lo que corresponda
    @Override
    public void mouseClicked(MouseEvent e) {
        switch (ed.getHerramienta()) {
            case NORMAL:
                if (e.getClickCount() > 1)
                    ed.editarCelda(celdaGrafica(e));
                break;
            case MOVER_ENEMIGO:
                ed.moverEnemigo(celdaGrafica(e));
                break;
            case MOVER_OBJETO:
                ed.moverObjeto(celdaGrafica(e));
                break;
        }
    }

    private void moverEnemigos() {
        //La posición inicial y objetivo deben ser distintas y no nulas
        if (inicial != null && objetivo != null && !inicial.equals(objetivo)) {
            //La celda objetivo no puede ser la posición inicial del jugador
            if (!objetivo.getId().equals(ed.getMapa().getPosicionInicial())) {
                Transitable c = (Transitable) ed.getMapa().getCelda(inicial.getId()); //La inicial sabemos que es transitable
                for (Enemigo en : c.getEnemigos()){
                    try {
                        en.setPos(objetivo.getId());
                    } catch (CeldaObjetivoNoValida ex) {
                        ed.info("Celda objetivo no válida.");
                    }
                }
                ed.setSeleccionada(objetivo); //La nueva celda seleccionada es la objetivo
                ed.repintarCelda(inicial); //Repintamos la celda inicial
            } else {
                ed.info("La celda objetivo es la posición inicial del jugador!");
            }
            ed.repintarCelda(objetivo); //La celda objetivo debe ser repintada
        }
    }
}
