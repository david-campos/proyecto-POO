/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mapa;

import Excepciones.CeldaObjetivoNoValida;
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
                            inicial = null; //Usada para saber donde se comenzó a arrastrar enemigos
                            this.ed = ed;
                        }
                        
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            super.mouseReleased(e);
                            CeldaGrafica cg = celdaGrafica(e);
                            switch(botonPulsado){
                                //Si es el derecho, desplegamos menu de celda
                                case MouseEvent.BUTTON3:
                                    new MenuCelda(cg, ed).show(e.getComponent(), e.getX(), e.getY());
                                    break;
                            }
                            botonPulsado = MouseEvent.NOBUTTON;
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
                            inicial = null;
                        }

                        @Override
                        public void mouseWheelMoved(MouseWheelEvent e) {
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
                            botonPulsado = e.getButton();
                            CeldaGrafica cg = celdaGrafica(e);
                            switch(botonPulsado)
                            {
                                case MouseEvent.BUTTON1:
                                    ed.toggleSeleccionada(cg);

                                    //Si tiene enemigos... podría ser inicial para arrastrar
                                    if(ed.getMapa().getCelda(cg.getId()) instanceof Transitable){
                                            Transitable transitable = (Transitable) ed.getMapa().getCelda(cg.getId());
                                            if(transitable.getNumEnemigos() > 0){
                                                objetivo = null;
                                                inicial = cg;
                                            }
                                        }
                                    break;
                            }
                        }
                        
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            CeldaGrafica cg = celdaGrafica(e);
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
                            if(botonPulsado == MouseEvent.BUTTON3){
                                Celda c = ed.getMapa().getCelda(cg.getId());
                                        
                                if(c==null)
                                    return;

                                if(c instanceof Transitable)
                                    ed.getMapa().setCelda(cg.getId(), new NoTransitable());
                                else
                                    ed.getMapa().setCelda(cg.getId(), new Transitable());
                                
                                ed.repintarCelda(cg);
                            }
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            CeldaGrafica cg = celdaGrafica(e);
                            ed.repintarCelda(cg); //Siempre repintamos la celda al salir de ella.
                            if(ed.getSeleccionada() == null || !e.getComponent().equals(ed.getSeleccionada().getComponente()))
                                ((JComponent)e.getComponent()).setBorder(Editor.BORDE_DEF);
                            ed.infoCoordenadas(null);
                        }
    }
