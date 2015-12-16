/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Juego;

import Mapa.Punto;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class ConsolaMapa2 extends JFrame implements Consola{
    private static final int DIM = 30;
    
    private final ArrayList<JLabel> paneles;
    private final HashMap<String, ImageIcon> imagenes;
    
    /**
     *
     * @param tamMapa
     */
    public ConsolaMapa2(Punto tamMapa){
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        GridLayout g = new GridLayout(tamMapa.y, tamMapa.x,0,0);
        getContentPane().setLayout(g);
        g.setHgap(0);
        g.setVgap(0);
        
        paneles = new ArrayList(tamMapa.x * tamMapa.y);
        imagenes = new HashMap();
        for(int i = 0; i < tamMapa.x * tamMapa.y; i++){
            JLabel jPanel1 = new JLabel();
            jPanel1.setBackground(Color.red);
            
            ImageIcon icon = new ImageIcon("img/celda.png");
            imagenes.put("celda", icon);
            jPanel1.setIcon(icon);
            
            jPanel1.setMinimumSize(new Dimension(DIM, DIM));
            jPanel1.setPreferredSize(new Dimension(DIM, DIM));
            jPanel1.setSize(new Dimension(DIM, DIM));
            
            getContentPane().add(jPanel1);
            paneles.add(jPanel1);
        }
        
        //VENTANA
        setResizable(false);
        setTitle("Mapa");
        pack();
        setVisible(true);
    }

    @Override
    public void imprimirMapa(String mensaje) {
        if(mensaje.split(" ").length != paneles.size())
            return;
        int i=0;
        for(String img: mensaje.split(" ")){
            ImageIcon ico;
            if(imagenes.containsKey(img))
                ico = imagenes.get(img);
            else{
                ico = new ImageIcon("img/"+img+".png");
                imagenes.put(img, ico);
            }
            paneles.get(i).setIcon(ico);
            i++;
        }
    }

    /**
     *
     * @param mensaje
     */
    @Override
    public void imprimir(String mensaje) {
        if(mensaje != null)
            System.out.println(mensaje);    
    }
    // <editor-fold defaultstate="collapsed" desc="No deberían ser necesarios para una consola de mapa">   

    /**
     *
     * @param mensaje
     */
        @Override
    public void imprimirSinSalto(String mensaje) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String leer(String descripcion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @return
     */
    @Override
    public String leer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    //</editor-fold>
    
    /**
     *
     * @return
     */
    @Override
    public boolean esGrafica() {return true;}
}
