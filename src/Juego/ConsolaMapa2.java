/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Juego;

import Mapa.Mapa;
import Mapa.Punto;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.layout.Border;
/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class ConsolaMapa2 extends JFrame implements Consola{
    private final int dim;
    
    private final ArrayList<JLabel> paneles;
    private final HashMap<String, ImageIcon> imagenes;
    private final JTextArea areaConsola;
    
    public ConsolaMapa2(Punto tamMapa){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Punto dim_total = new Punto((int)Math.round(screenSize.width * 0.7), (int) Math.round(screenSize.height * 0.7)); //Tamaño del mapa máximo
        dim = (int) Math.floor(Math.min(dim_total.x/(double)tamMapa.x, dim_total.y/(double)tamMapa.y));
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        /*  LAYOUT DE LA VENTANA  */
        getContentPane().setLayout(new BorderLayout());
        
        /*  GENERAR LOS DOS PANELES GENERALES*/
        JPanel panelMapa = new JPanel(new GridLayout(tamMapa.y, tamMapa.x,0,0));
        panelMapa.setSize(new Dimension(dim*tamMapa.x,dim*tamMapa.y));
        panelMapa.setMinimumSize(panelMapa.getSize());
        JPanel panelConsola = new JPanel();
        panelConsola.setLayout(new BoxLayout(panelConsola, 1));
        
        // <editor-fold defaultstate="collapsed" desc="GENERAR CELDITAS DEL MAPA"> 
        paneles = new ArrayList(tamMapa.x * tamMapa.y);
        imagenes = new HashMap();
        for(int i = 0; i < tamMapa.x * tamMapa.y; i++){
            JLabel jPanel1 = new JLabel();
            jPanel1.setBackground(Color.red);
            
            ImageIcon icon = new ImageIcon("img/celda.png");
            imagenes.put("celda", icon);
            jPanel1.setIcon(icon);
            
            jPanel1.setMinimumSize(new Dimension(dim, dim));
            jPanel1.setPreferredSize(new Dimension(dim, dim));
            jPanel1.setSize(new Dimension(dim, dim));
            
            panelMapa.add(jPanel1);
            paneles.add(jPanel1);
        }
        //</editor-fold>
        
        //<editor-fold  defaultstate="collapsed" desc="GENERAR LA CONSOLA">
        areaConsola = new JTextArea(1, 50);
        areaConsola.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        areaConsola.setBackground(new Color(50,0,0));
        areaConsola.setForeground(new Color(200,0,0));
        areaConsola.setMargin(new Insets(5,10,10,10));
        panelConsola.add(areaConsola);
        //</editor-fold>
        
        //VENTANA
        getContentPane().add(panelMapa, BorderLayout.NORTH);
        getContentPane().add(panelConsola, BorderLayout.SOUTH);
        setResizable(false);
        setTitle("Consola...");
        pack();
        this.setLocationRelativeTo(null);
        setVisible(true);
        toFront();
    }

    @Override
    public final void setVisible(boolean b) {
        super.setVisible(b);
        areaConsola.requestFocusInWindow();
    }
    
    

    @Override
    public void imprimirMapa(Mapa map) {
        //if(mensaje.split(" ").length != paneles.size())
            return;
        
        /*for(String img: mensaje.split(" ")){
            ImageIcon ico;
            if(imagenes.containsKey(img))
                ico = imagenes.get(img);
            else{
                ico = new ImageIcon(new ImageIcon("img/"+img+".png").getImage().getScaledInstance(dim, dim, Image.SCALE_SMOOTH));
                imagenes.put(img, ico);
            }
            paneles.get(i).setIcon(ico);
            i++;
        }*/
    }
    @Override
    public void imprimir(String mensaje) {
        if(mensaje != null)
            System.out.println(mensaje);    
    }
    // <editor-fold defaultstate="collapsed" desc="No deberían ser necesarios para una consola de mapa">   
    @Override
    public void imprimirSinSalto(String mensaje) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String leer(String descripcion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String leer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    //</editor-fold>
    
    @Override
    public void cerrar(){
        setVisible(false);
        dispose();
    }
}
