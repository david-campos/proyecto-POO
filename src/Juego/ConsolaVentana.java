/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Juego;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
/**
 *
 * @author crist
 */
public class ConsolaVentana extends JFrame implements Consola{
    private final JTextArea area;
    private JTextField textInput;
    private String textoActual = "";
    private boolean hayEntrada = false;

    /**
     *
     */
    public ConsolaVentana() {
        area = new JTextArea(400,300);
        area.setBounds(0,0,400,300);
        area.setEditable(false);
        area.setBackground(Color.gray);
        
        
        KeyListener keyList = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    textoActual = textInput.getText();
                    textInput.setText("");
                    hayEntrada = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {                
            }
        };
 
        textInput = new JTextField();
        textInput.setBounds(0,300,400,100);
        textInput.addKeyListener(keyList);
        //textInput.setLineWrap(true);
        //textInput.setWrapStyleWord(true);
        
        add(area);
        add(textInput);
        
        
        setTitle("La Ventana");
        setSize(400,400);
        setLayout(new BorderLayout());
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
        imprimir(mensaje);    
    }

    /**
     *
     * @param mensaje
     */
    @Override
    public void imprimirSinSalto(String mensaje) {
        area.setText(mensaje);
    }
    
    /**
     *
     * @return
     */
    @Override
    public String leer() {
        while(!hayEntrada) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConsolaVentana.class.getName()).log(Level.SEVERE, null, ex);
            }
        } hayEntrada = false;
        return textoActual;
    }

    @Override
    public String leer(String descripcion) {
        area.append(descripcion + '\n');
        return leer();
    }
    
    /**
     *
     * @return
     */
    @Override
    public boolean esGrafica() {return false;}
}
        
