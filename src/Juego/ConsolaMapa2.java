/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Juego;

import Mapa.Celda;
import Mapa.Mapa;
import Mapa.Punto;
import Mapa.Transitable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class ConsolaMapa2 extends JFrame implements Consola{
    private final int dim;
    private final ArrayList<JLabel> paneles;
    private final HashMap<String, ImageIcon> imagenes;
    private final JTextArea areaConsola;
    private final JTextArea areaConsolaDisplay;
    private final JTextArea areaEstado;
    private final JScrollPane scrollP;
    private final JPanel panelMapa;
    private final ArrayBlockingQueue<String> comandos;
    
    public ConsolaMapa2(Punto tamMapa){
        comandos = new ArrayBlockingQueue(10);
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        /*  LAYOUT DE LA VENTANA  */
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        /*  GENERAR LOS DOS PANELES GENERALES Y LA BARRA DE ESTADO*/
        areaEstado = new JTextArea(1,50);
        areaEstado.setBackground(new Color(0,0,50));
        areaEstado.setForeground(new Color(100,100,200));
        areaEstado.setDisabledTextColor(areaEstado.getForeground());
        areaEstado.setFont(new Font(Font.MONOSPACED, Font.BOLD + Font.ITALIC, 20));
        areaEstado.setEnabled(false);
        JPanel panelConsola = new JPanel();
        panelConsola.setLayout(new BoxLayout(panelConsola, BoxLayout.Y_AXIS));
        
        imagenes = new HashMap();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Punto dim_total = new Punto((int)Math.round(screenSize.width * 0.7), (int) Math.round(screenSize.height * 0.7)); //Tamaño del mapa máximo
        dim = (int) Math.floor(Math.min(dim_total.x/(double)tamMapa.x, dim_total.y/(double)tamMapa.y));
        
        JPanel panelMapaDisp = new JPanel();
        panelMapaDisp.setLayout(new BoxLayout(panelMapaDisp, BoxLayout.X_AXIS));
        paneles = new ArrayList(tamMapa.x * tamMapa.y);
        panelMapa = new JPanel(new GridLayout(tamMapa.y, tamMapa.x,0,0));
        panelMapa.setSize(new Dimension(dim*tamMapa.x,dim*tamMapa.y));
        panelMapa.setMinimumSize(panelMapa.getSize());
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
        panelMapaDisp.add(panelMapa);
        
        //<editor-fold  defaultstate="collapsed" desc="GENERAR LA CONSOLA">
        areaConsola = new JTextArea(1, 50);
        areaConsola.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        areaConsola.setBackground(areaEstado.getBackground());
        areaConsola.setForeground(areaEstado.getForeground());
        areaConsola.setMargin(new Insets(5,10,10,10));
        areaConsola.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try{
                        if(comandos.add(areaConsola.getText()))
                            areaConsola.setText("");
                    }catch(IllegalStateException ise){
                        //Ignore
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    areaConsola.setText(areaConsola.getText().replace("\n", ""));
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });
        areaConsola.setEnabled(false);
        
        areaConsolaDisplay = new JTextArea(2, 50);
        areaConsolaDisplay.setFont(areaConsola.getFont());
        areaConsolaDisplay.setBackground(new Color(50,0,0));
        areaConsolaDisplay.setForeground(new Color(200,0,0));
        areaConsolaDisplay.setDisabledTextColor(areaConsolaDisplay.getForeground());
        areaConsolaDisplay.setBorder(null);
        areaConsolaDisplay.setLineWrap(true);
        areaConsolaDisplay.setEnabled(false);
        
        scrollP = new JScrollPane();
        scrollP.setViewportView(areaConsolaDisplay);
        scrollP.setBorder(BorderFactory.createLineBorder(areaConsolaDisplay.getBackground(), 10));
        scrollP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panelMapaDisp.add(scrollP);
        panelConsola.add(areaConsola);
        //</editor-fold>
        
        //VENTANA
        getContentPane().add(areaEstado);
        getContentPane().add(panelMapaDisp);
        getContentPane().add(panelConsola);
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
        if(map == null || map.getCelda(0,0) == null || paneles == null)
            return;
        
        for(int y=0;y<map.getAlto();y++)
            for(int x=0; x<map.getAncho();x++){
                ImageIcon ico;
                Celda c = map.getCelda(x,y);
                String img;
                if(c == null)
                    img = "null";
                else if(map.getJugador().getPos().equals(map.getPosDe(c)))
                    img = "jugador";
                else if(!map.getJugador().enRango(map.getPosDe(c)))
                    img = "no";
                else if(c instanceof Transitable){
                    Transitable transitable = (Transitable) c;
                    if(transitable.getEnemigos().size() > 0)
                        img = "enemigo";
                    else
                        img = c.representacionGrafica(); //Obtiene la imagen
                }else
                    img = c.representacionGrafica(); //Obtiene la imagen
                
                if(imagenes.containsKey(img))
                    ico = imagenes.get(img);
                else{
                    ico = new ImageIcon(new ImageIcon("img/"+img+".png").getImage().getScaledInstance(dim, dim, Image.SCALE_SMOOTH));
                    imagenes.put(img, ico);
                }
                paneles.get(y*map.getAncho()+x).setIcon(ico);
            }
    }
    @Override
    public void imprimir(String mensaje) {
        areaConsolaDisplay.append("\n"+mensaje);
        if(areaConsolaDisplay.getLineCount() > 50)
            try {
                areaConsolaDisplay.setText(areaConsolaDisplay.getText().substring(areaConsolaDisplay.getLineStartOffset(areaConsolaDisplay.getLineCount() - 50)));
            } catch (BadLocationException ex) {
                areaConsolaDisplay.setText("ERROR AL ACOTAR TEXTITO...");
            }
        areaConsolaDisplay.setCaretPosition(areaConsolaDisplay.getDocument().getLength());
    }
    @Override
    public void imprimirEstado(String mensaje){
        areaEstado.setText(mensaje);
    }
    @Override
    public void imprimirSinSalto(String mensaje) {
        imprimir(mensaje);
    }

    @Override
    public void limpiar() {
        areaConsolaDisplay.setText("");
    }
    
    
    
    /**
     * Pide datos al usario en la consola por defecto
     * @param descripcion Texto a mostrar previo a que el usuario escriba
     * @return Entrada del usuario
     */
    @Override
    public String leer(String descripcion) {
        imprimir(descripcion);
        return leer();
    }
        /**
     * Pide datos al usario en la consola por defecto
     * @return Entrada del usuario
     */
    @Override
    public String leer() {
        String ret=null;
        areaConsola.setEnabled(true);
        while((ret = comandos.poll())==null)
            Thread.yield();
        areaConsola.setEnabled(false);
        return ret;
    }
    
    @Override
    public void cerrar(){
        setVisible(false);
        dispose();
    }
}
