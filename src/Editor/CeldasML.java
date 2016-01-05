/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import Editor.Editor;
import Editor.MenuCelda;
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
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class CeldasML extends MouseAdapter{
                        private int botonPulsado;
                        private CeldaGrafica inicial;
                        private int celdasRecorridas; //Usada para lo de click derecho
                        private CeldaGrafica objetivo;
                        private final Editor ed;
                        
                        private CeldaGrafica celdaGrafica(MouseEvent e){
                            for(CeldaGrafica cg: ed.getCeldas())
                                if(cg.getComponente().equals(e.getComponent()))
                                    return cg;
                            return null;
                        }
                        
                        public CeldasML(Editor ed) {
                            botonPulsado = 0;
                            inicial = null;
                            celdasRecorridas = 0;
                            objetivo = null;
                            this.ed = ed;
                        }
                        
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            super.mouseReleased(e);
                            CeldaGrafica cg = celdaGrafica(e);
                            
                            if(ed.getHerramienta() != Editor.Herramienta.NORMAL)
                                return;
                            
                            switch(botonPulsado){
                                //Si es el derecho y no se ha arrastrado por ahí, desplegamos esto.
                                case MouseEvent.BUTTON3:
                                    if(celdasRecorridas == 0){
                                        new MenuCelda(cg, ed).show(e.getComponent(), e.getX(), e.getY());
                                    }
                                    break;
                                case MouseEvent.BUTTON1:
                                    //Si la celda inicial no era nula, movemos los enemigos
                                    if(inicial != null && objetivo != null && !inicial.equals(objetivo)){
                                        if(!objetivo.getId().equals(ed.getMapa().getPosicionInicial())){
                                            Transitable c = (Transitable) ed.getMapa().getCelda(inicial.getId());
                                            for(Enemigo en : c.getEnemigos()){
                                                try {
                                                    en.setPos(objetivo.getId());
                                                } catch (CeldaObjetivoNoValida ex) {
                                                    ed.info("Celda objetivo no válida.");
                                                }
                                            }
                                            ed.setSeleccionada(objetivo);
                                            ed.repintarCelda(inicial);
                                        }else
                                            ed.info("La celda objetivo es la posición inicial del jugador!");
                                        ed.repintarCelda(objetivo);    
                                    }
                            }
                            celdasRecorridas = 0;
                            inicial = null;
                            botonPulsado = MouseEvent.NOBUTTON;
                        }

                        @Override
                        public void mouseWheelMoved(MouseWheelEvent e) {
                            if(ed.getHerramienta() != Editor.Herramienta.NORMAL)
                                return;
                            
                            if(inicial != null) //Cuando se arrastran enemigos no funciona esto
                                return;
                            Celda c=null;
                            CeldaGrafica celdaGrafica = null;
                            for(CeldaGrafica cg : ed.getCeldas())
                                if(cg.getComponente().equals(e.getComponent())){
                                    c = ed.getMapa().getCelda(cg.getId());
                                    celdaGrafica = cg;
                                    break;
                                }
                            if(c==null)
                                return;

                            if(c instanceof Transitable)
                                c.tipo=(c.tipo+e.getWheelRotation())%ConstantesMapa.CE_REPR_TRANS.length;
                            else
                                c.tipo=(c.tipo+e.getWheelRotation())%ConstantesMapa.CE_REPR_NOTRANS.length;
                            ed.repintarCelda(celdaGrafica);
                        }
                        
                        @Override
                        public void mousePressed(MouseEvent e) {
                            if(ed.getHerramienta() != Editor.Herramienta.NORMAL)
                                return;
                            
                            CeldaGrafica cg = celdaGrafica(e);
                            botonPulsado = e.getButton();
                            switch(botonPulsado)
                            {
                                case MouseEvent.BUTTON1:
                                    ed.toggleSeleccionada(cg);

                                    //Si tiene enemigos... podría ser inicial para arrastrar
                                    if(ed.getMapa().getCelda(cg.getId()) instanceof Transitable){
                                            Transitable transitable = (Transitable) ed.getMapa().getCelda(cg.getId());
                                            if(transitable.getNumEnemigos() > 0){
                                                inicial = cg;
                                                celdasRecorridas = 0;
                                                objetivo = null;
                                            }
                                        }
                                    break;
                                case MouseEvent.BUTTON3:
                                    celdasRecorridas = 0;
                                    inicial=cg;
                                    objetivo = null;
                                    break;
                            }
                        }
                        
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            CeldaGrafica cg = celdaGrafica(e);
                            switch(ed.getHerramienta()){
                                case NORMAL:
                                    ed.infoCoordenadas(cg.getId());
                                    if(ed.getSeleccionada() == null || !e.getComponent().equals(ed.getSeleccionada().getComponente()))
                                        ((JComponent)e.getComponent()).setBorder(Editor.BORDE_HOVER);
                                    //Si el pulsado es el botón izquierdo, e inicial no es null, simulamos movimiento aquí
                                    if(botonPulsado == MouseEvent.BUTTON1 && inicial != null){
                                        cg.getComponente().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                                        cg.setDelante(ed.obtenerImagen("ed_mover_enemigo"));
                                        objetivo = cg;
                                    }

                                    //Si está pulsado el botón derecho se cambia la celda de transitable a no transitable o viceversa
                                    //según la celda inicial
                                    if(botonPulsado == MouseEvent.BUTTON3 && inicial != null){
                                        objetivo = cg;
                                        celdasRecorridas++;
                                        Celda c = ed.getMapa().getCelda(cg.getId());

                                        //Si es la primera, intercambiamos la inicial
                                        if(celdasRecorridas == 1){
                                            if(ed.getMapa().getCelda(inicial.getId()) instanceof Transitable){
                                                for(Enemigo enem : ((Transitable)ed.getMapa().getCelda(inicial.getId())).getEnemigos()){
                                                    ed.eliminarEnemigo(enem); //Eliminamos el enemigo.
                                                }
                                                ed.getMapa().setCelda(inicial.getId(), new NoTransitable());
                                            }else
                                                ed.getMapa().setCelda(inicial.getId(), new Transitable());
                                            ed.repintarCelda(inicial);
                                        }

                                        if(c==null)
                                            return;
                                        if(inicial == null)
                                            return;

                                        //Si ya es transitable o intransitable acordemente a la incial (ya cambiada)
                                        //no hay que hacer nada.
                                        if(ed.getMapa().getCelda(inicial.getId()) instanceof Transitable){
                                            if(c instanceof Transitable)
                                                return;
                                        }else{
                                            if(c instanceof NoTransitable)
                                                return;
                                        }

                                        if(c instanceof Transitable){
                                            for(Enemigo enem : ((Transitable)ed.getMapa().getCelda(cg.getId())).getEnemigos()){
                                                    ed.eliminarEnemigo(enem); //Eliminamos el enemigo.
                                            }
                                            ed.getMapa().setCelda(cg.getId(), new NoTransitable());
                                        }else
                                            ed.getMapa().setCelda(cg.getId(), new Transitable());

                                        ed.repintarCelda(cg);
                                    }
                                    break;
                                case MOVER_ENEMIGO:
                                    
                                    if(!ed.getMapa().getPosicionInicial().equals(cg.getId())
                                            && ed.getMapa().getCelda(cg.getId()) instanceof Transitable){
                                        cg.getComponente().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                                        ((JComponent)e.getComponent()).setBorder(Editor.BORDE_MOVER);
                                    }else
                                        ((JComponent)e.getComponent()).setBorder(Editor.BORDE_NOMOVER);
                                    break;
                                case MOVER_OBJETO:
                                    if(ed.getMapa().getCelda(cg.getId()) instanceof Transitable){
                                        cg.getComponente().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                                        ((JComponent)e.getComponent()).setBorder(Editor.BORDE_MOVER);
                                    }else
                                        ((JComponent)e.getComponent()).setBorder(Editor.BORDE_NOMOVER);
                                    break;
                            }
                            super.mouseEntered(e);
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            CeldaGrafica cg = celdaGrafica(e);
                            ed.repintarCelda(cg); //Siempre repintamos la celda al salir de ella.
                            if(ed.getSeleccionada() == null || !e.getComponent().equals(ed.getSeleccionada().getComponente()))
                                ((JComponent)e.getComponent()).setBorder(Editor.BORDE_DEF);
                            ed.infoCoordenadas(null);
                        }
                        
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            switch(ed.getHerramienta()){
                                case NORMAL:
                                    if(e.getClickCount() > 1)
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
    }
