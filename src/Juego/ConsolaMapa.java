/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Juego;

import java.awt.*;
import javax.swing.*;
/**
 *
 * @author crist
 */
public class ConsolaMapa extends JFrame implements Consola{
    JTextArea area;

    /**
     *
     */
    public ConsolaMapa() {
        area = new JTextArea(400,400);
        area.setBounds(0,0,400,400);
        area.setEditable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(area);
        
        setTitle("El mapa!");
        setSize(400,400);
        setLayout(null);
        setVisible(true);        
    }
    
    /**
     *
     * @param s
     */
    @Override
    public void imprimir(String s) {
        area.setText(s);
    }
    @Override
    public void imprimirMapa(String mensaje) {
        area.setText(mensaje);  
    }

    /**
     *
     * @param mensaje
     */
    @Override
    public void imprimirSinSalto(String mensaje) {
        area.setText(mensaje);
    }

    @Override
    public String leer(String descripcion) {
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public String leer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @return
     */
    @Override
    public boolean esGrafica() {return false;}
}
