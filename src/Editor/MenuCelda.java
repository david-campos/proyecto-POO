/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import Mapa.Celda;
import Utilidades.CeldaGrafica;
import Mapa.Transitable;
import Objetos.Objeto;
import Personajes.Enemigo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class MenuCelda extends javax.swing.JPopupMenu{
    private static final String MENU_EDITAR = "Editar";
    private static final String MENU_MOVER = "Mover";
    private static final String MENU_ELIMINAR = "Eliminar";
    
    private final CeldaGrafica celdaAsociada;
    private final Editor editor;
    
    private final HashMap<JMenu, Enemigo> mapaEnemigos;
    private final HashMap<JMenu, Objeto> mapaObjetos;
    
    JCheckBoxMenuItem mitToggleTransitable;
    
    public MenuCelda(CeldaGrafica cg, Editor ed) {
        celdaAsociada = cg;
        editor = ed;
        mapaEnemigos = new HashMap();
        mapaObjetos = new HashMap();
        iniciarMenu();
    }
    
    private Celda celda(){
        return editor.getMapa().getCelda(celdaAsociada.getId());
    }
    
    private Transitable trans(){
        if(celda() instanceof Transitable)
            return (Transitable) celda();
        else
            return null;
    }
    
    private void iniciarMenu(){
        JMenuItem mitPropiedades = new JMenuItem("Propiedades");
        mitPropiedades.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.editarCelda(celdaAsociada);
            }
        });
        add(mitPropiedades);
        
        mitToggleTransitable = new JCheckBoxMenuItem("Transitable");
        mitToggleTransitable.setSelected(celda() instanceof Transitable);
        mitToggleTransitable.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                mitToggleTransitable_actionPerformed(e);
            }
        });
        add(mitToggleTransitable);
        
        if(trans() != null){
            if(trans().getObjetos().size() > 0)
                generarSeccionObjetos(trans());
            
            if(trans().getNumEnemigos() > 0)
                generarSeccionEnemigos(trans());
            else{
                add(new JSeparator());
                add("Hacer posicion inicial").addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        CeldaGrafica anterior = editor.grafica(editor.getMapa().getPosicionInicial());
                        editor.getMapa().setPosicionInicial(celdaAsociada.getId());
                        editor.repintarCelda(celdaAsociada);
                        editor.repintarCelda(anterior);
                    }
                });
            }
        }
    }
    private void generarSeccionEnemigos(Transitable t){
        add(new JSeparator());
        JMenu seccionEnemigos = new JMenu("Enemigos");
        ActionListener menuEnemigo = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                menuEnemigo_actionPerformed(e);
            }
        };
        for(Enemigo e: t.getEnemigos()){
            JMenu otroEnemigo = new JMenu(e.getNombre());
            otroEnemigo.add(MENU_EDITAR).addActionListener(menuEnemigo);
            otroEnemigo.add(MENU_MOVER).addActionListener(menuEnemigo);
            otroEnemigo.add(MENU_ELIMINAR).addActionListener(menuEnemigo);
            mapaEnemigos.put(otroEnemigo, e);
            seccionEnemigos.add(otroEnemigo);
        }
        add(seccionEnemigos);
    }
    private void generarSeccionObjetos(Transitable t){
        add(new JSeparator());
        JMenu seccionObjetos = new JMenu("Objetos");
        ActionListener menuObjeto = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                menuObjeto_actionPerformed(e);
            }
        };
        for(Objeto o: t.getObjetos()){
            JMenu otroObjeto = new JMenu(o.getNombre());
            otroObjeto.add(MENU_EDITAR).addActionListener(menuObjeto);
            otroObjeto.add(MENU_MOVER).addActionListener(menuObjeto);
            otroObjeto.add(MENU_ELIMINAR).addActionListener(menuObjeto);
            mapaObjetos.put(otroObjeto, o);
            seccionObjetos.add(otroObjeto);
        }
        add(seccionObjetos);
    }
    private void mitToggleTransitable_actionPerformed(ActionEvent e){
        editor.toggleTransitable(celdaAsociada);
    }
    private void menuEnemigo_actionPerformed(ActionEvent e){
        JMenuItem src = (JMenuItem)e.getSource();
        Enemigo enemigo = mapaEnemigos.get((JMenu)((JPopupMenu)src.getParent()).getInvoker());
        switch(src.getText()){
            case MENU_EDITAR:
                new PropiedadesEnemigo(editor, enemigo).setVisible(true);
                break;
            case MENU_MOVER:
                editor.setEnemigoMovido(enemigo);
                editor.setCeldaOrigen(trans());
                editor.setHerramienta(Editor.Herramienta.MOVER_ENEMIGO);
                break;
            case MENU_ELIMINAR:
                editor.eliminarEnemigo(enemigo);
                break;
        }
    }
    private void menuObjeto_actionPerformed(ActionEvent e){
        JMenuItem src = (JMenuItem)e.getSource();
        Objeto objeto = mapaObjetos.get((JMenu)((JPopupMenu)src.getParent()).getInvoker());
        switch(src.getText()){
            case MENU_EDITAR:
                new PropiedadesObjeto(editor, objeto).setVisible(true);
                break;
            case MENU_MOVER:
                editor.setObjetoMovido(objeto);
                editor.setCeldaOrigen(trans());
                editor.setHerramienta(Editor.Herramienta.MOVER_OBJETO);
                break;
            case MENU_ELIMINAR:
                editor.eliminarObjeto(objeto);
                break;
        }
    }
}
