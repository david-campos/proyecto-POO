/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Juego;

import Mapa.Celda;
import Mapa.CeldaGrafica;
import Mapa.ImagenCelda;
import Mapa.Mapa;
import Mapa.PanelCeldaGraficaEditor;
import Mapa.Punto;
import Mapa.Transitable;
import Menus.Menu;
import Menus.MenuGrafico;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class ConsolaGrafica extends JFrame implements Consola{
    private final Mapa mapa;
    private int dim;
    private final Juego juego;
    
    private final ArrayList<CeldaGrafica> paneles;
    private final HashMap<String, Image> imagenes;
    private final JTextArea areaConsola;
    private final JTextArea areaConsolaDisplay;
    private final JTextArea areaEstado;
    private final JScrollPane scrollP;
    private final JPanel panelMapa;
    private final ArrayBlockingQueue<String> comandos;

    public ConsolaGrafica(Juego j, Mapa mapa) throws HeadlessException {
        super();
        juego = j;
        this.mapa = mapa;
        paneles = new ArrayList(mapa.getAlto()*mapa.getAncho());
        panelMapa = new JPanel(new GridLayout(mapa.getAlto(), mapa.getAncho(),0,0));
        imagenes = new HashMap();
        areaEstado = new JTextArea(1,50);
        areaConsola = new JTextArea(1, 50);
        areaConsolaDisplay = new JTextArea(2, 50);
        scrollP = new JScrollPane();
        comandos = new ArrayBlockingQueue(10);
        initComponents();
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ConsolaGrafica.this.setVisible(true);
            } 
        });
    }
    
    private void initComponents(){
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        /*  LAYOUT DE LA VENTANA  */
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        /*  GENERAR LOS DOS PANELES GENERALES Y LA BARRA DE ESTADO*/
        areaEstado.setBackground(new Color(0,0,50));
        areaEstado.setForeground(new Color(100,100,200));
        areaEstado.setDisabledTextColor(areaEstado.getForeground());
        areaEstado.setFont(new Font(Font.MONOSPACED, Font.BOLD + Font.ITALIC, 20));
        areaEstado.setEnabled(false);
        JPanel panelConsola = new JPanel();
        panelConsola.setLayout(new BoxLayout(panelConsola, BoxLayout.Y_AXIS));
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Punto dim_total = new Punto((int)Math.round(screenSize.width * 0.85), (int) Math.round(screenSize.height * 0.85)); //Tamaño del mapa máximo
        dim = (int) Math.floor(Math.min(dim_total.x/(double)mapa.getAncho(), dim_total.y/(double)mapa.getAlto()));
        
        JPanel panelMapaDisp = new JPanel();
        panelMapaDisp.setLayout(new BoxLayout(panelMapaDisp, BoxLayout.X_AXIS));
        //panelMapa.setSize(new Dimension(dim*mapa.getAncho(), dim*mapa.getAlto()));
        panelMapa.setMinimumSize(panelMapa.getSize());
        for(int i = 0; i < mapa.getAncho() * mapa.getAlto(); i++){
            CeldaGrafica celda = new PanelCeldaGraficaEditor(new Punto(i/mapa.getAncho(), i%mapa.getAncho()));
            Image icon = new ImageIcon("img/celda.png").getImage();
            imagenes.put("celda", icon);
            celda.setImagen(new ImagenCelda(icon));
            celda.getComponente().setMinimumSize(new Dimension(dim, dim));
            celda.getComponente().setPreferredSize(new Dimension(dim, dim));
            //celda.getComponente().setSize(new Dimension(dim, dim));
            
            panelMapa.add(celda.getComponente());
            paneles.add(celda);
        }
        panelMapaDisp.add(panelMapa);
        
        //<editor-fold  defaultstate="collapsed" desc="GENERAR LA CONSOLA">
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
        
        areaConsolaDisplay.setFont(areaConsola.getFont());
        areaConsolaDisplay.setBackground(new Color(50,0,0));
        areaConsolaDisplay.setForeground(new Color(200,0,0));
        areaConsolaDisplay.setDisabledTextColor(areaConsolaDisplay.getForeground());
        areaConsolaDisplay.setBorder(null);
        areaConsolaDisplay.setLineWrap(true);
        areaConsolaDisplay.setEnabled(false);
        
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
        setTitle(mapa.getNombre());
        toFront();
        pack();
        this.setLocationRelativeTo(null);
    }
    
    public ImagenCelda imagenRepresentante(Celda c){
        String representacionF, representacionD = null;
        
        if(c == null)
            representacionF = "null";
        else if(mapa.getJugador().enRango(mapa.getPosDe(c))){
            representacionF = c.representacionGrafica(); //Obtiene la imagen
        }else if(c.isVisitada()){
            representacionF = c.representacionGrafica();
            representacionD = "sombra";
        }else
            representacionF = "no";
        
        if(c != null && mapa.getJugador().enRango(mapa.getPosDe(c)))
            if(mapa.getJugador().getPos().equals(mapa.getPosDe(c)))
                representacionD = "jugador";
            else if(c instanceof Transitable){
                Transitable transitable = (Transitable) c;
                if(transitable.getEnemigos().size() > 0)
                    if(mapa.getJugador().enAlcance(mapa.getPosDe(c)))
                        representacionD = "enemigo";
                    else
                        representacionD = "ed_mover_enemigo";
            }
        ImagenCelda ret =  new ImagenCelda(obtenerImagen(representacionF));
        if(representacionD != null)
            ret.setDelante(obtenerImagen(representacionD));
        return ret;
    }
    
    @Override
    public void imprimirMapa() {
        if(mapa == null || mapa.getCelda(0,0) == null || paneles == null)
            return;
        
        for(int y=0;y<mapa.getAlto();y++)
            for(int x=0; x<mapa.getAncho();x++){
                Celda c = mapa.getCelda(x,y);
                paneles.get(y*mapa.getAncho()+x).setImagen(imagenRepresentante(c));
            }
    }
    public Image obtenerImagen(String representacion) {
        if(imagenes.get(representacion) == null){
            Image img;
            img = new ImageIcon("img/"+representacion+".png").getImage().getScaledInstance(dim, dim, Image.SCALE_SMOOTH);
            imagenes.put(representacion, img);
            return img;
        }else
            return imagenes.get(representacion);
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
        System.exit(0);
    }
    
    public void hasMuerto() {
        areaConsola.setVisible(false);
        areaConsolaDisplay.setVisible(false);
        areaEstado.setVisible(false);
        scrollP.setVisible(false);
        panelMapa.setVisible(false);   
        JLabel imgMuerte = new JLabel(new ImageIcon("img\\hasMuerto.png"));
        add(imgMuerte, BorderLayout.CENTER, 0); 
        pack();
        this.setLocationRelativeTo(null);
        try {
            Thread.sleep(2000); //2 segundos espera
        } catch (InterruptedException ex) {
            Logger.getLogger(ConsolaGrafica.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Thread() {
            @Override
            public void run() {
                Menu menuInicio = new MenuGrafico();
                menuInicio.lanzar();
            }
        }.start();
        this.dispose();
    }
}
