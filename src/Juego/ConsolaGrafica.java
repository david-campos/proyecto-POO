package Juego;

import Comandos.Comando;
import Comandos.ComandoAtacar;
import Comandos.ComandoCoger;
import Comandos.ComandoDesequipar;
import Comandos.ComandoEquipar;
import Comandos.ComandoTirar;
import Excepciones.ComandoExcepcion;
import Mapa.Celda;
import Utilidades.CeldaGrafica;
import Utilidades.ImagenCelda;
import Mapa.Mapa;
import Utilidades.PanelCeldaGrafica;
import Utilidades.Punto;
import Mapa.Transitable;
import Menus.MenuGrafico;
import Objetos.Objeto;
import Personajes.Jugador;
import Personajes.Personaje;
import Utilidades.BarraDeCargaUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;

/**
 * <p>Implementación de la Consola de juego mediante una ventana gráfica.</p>
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class ConsolaGrafica extends JFrame implements Consola{
    //Variables necesarias
    private final Mapa mapa;
    private int dim;
    private final Cursor cursorAtacar;
    
    //Elementos que formarán la consola
    private final ArrayList<CeldaGrafica> paneles;
    private final HashMap<String, Image> imagenes;
    private final JTextField areaConsola;
    private final JTextArea areaConsolaDisplay;
    private final JLabel areaEstado;
    private final JScrollPane scrollP;
    private final JPanel panEstado;
    private final JPanel panelMapa;
    private final JPanel panelDerecho;
    private final JPanel panelMochila;
    private final JPanel panelObjetos;
    private final JPanel panCeldaNotif;
    private final JPanel panCelda;
    private final JPanel panScrCelda;
    private final JPanel panLblCelda;
    private final JLayeredPane panGenCelda;
    private final JScrollPane scrCelda;
    private final JProgressBar pbrCargaObjetos;
    private final JProgressBar pbrCargaPeso;
    private final ArrayBlockingQueue<String> comandos;
    private JLabel lblArmaDer;
    private JLabel lblArmaIzq;
    private JLabel lblArmadura;
    private JLabel lblBinoculares;
    private final Clip musica;

    /**
     * Crea una nueva 'consola' gráfica de juego
     * @param mapa mapa sobre el que se desarrolla el juego
     */
    public ConsolaGrafica(Mapa mapa) {
        super();
        this.mapa = mapa;
        //Iniciación de las componentes
        paneles = new ArrayList(mapa.getAlto()*mapa.getAncho());
        panelMapa = new JPanel(new GridLayout(mapa.getAlto(), mapa.getAncho(),0,0));
        imagenes = new HashMap();
        areaEstado = new JLabel("Estado del personaje");
        areaConsola = new JTextField(50);
        areaConsolaDisplay = new JTextArea(2, 50);
        panEstado = new JPanel();
        panelDerecho = new JPanel();
        panelMochila = new JPanel();
        panelObjetos = new JPanel();
        panCeldaNotif = new JPanel();
        panCelda = new JPanel();
        scrCelda = new JScrollPane();
        panScrCelda = new JPanel();
        panLblCelda = new JPanel();
        panGenCelda = new JLayeredPane();
        pbrCargaObjetos = new JProgressBar();
        pbrCargaPeso = new JProgressBar();
        scrollP = new JScrollPane();
        comandos = new ArrayBlockingQueue(10);
        //Fijación de parametros para obtener la ventana adecuada
        cursorAtacar = Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().createImage("img/cursor_atacar.png"), new Point(16,16), "Atacar");
        initComponents();
        //Se hace visible al instanciar
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ConsolaGrafica.this.setVisible(true);
            } 
        });
        musica = Utilidades.Sonido.play("SunWalker_MisteryOfTheNappolli_partIII");
        musica.loop(Clip.LOOP_CONTINUOUSLY);
        FloatControl control = (FloatControl)musica.getControl(FloatControl.Type.MASTER_GAIN);
        control.setValue(-35.0f);
    }
    
    //La función que inicia la ventana (pff tío, vaya rollo)
    private void initComponents(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Punto dim_total = new Punto((int)Math.round(screenSize.width * 0.85), (int) Math.round(screenSize.height * 0.85)); //Tamaño del mapa máximo
        dim = (int) Math.floor(Math.min(dim_total.x/(double)mapa.getAncho(), dim_total.y/(double)mapa.getAlto()));
        dim_total = new Punto(dim*mapa.getAncho(), dim*mapa.getAlto());
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(Color.black);
        
        /*  LAYOUT DE LA VENTANA  */
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        /* DISEÑO */
        /*****************                                           ****************
         * panEstado     *                                           * panelMochila *    **************
         *****************     **********************************    ****************    * panGenCelda* => panScrCelda/panLblCelda
         * panelMapaDisp *  => *   panGenMap     * panelDerecho * => * panCeldaNotif* => **************
         *****************     **********************************    ****************    * scrollP    *
         * panelConsola  *                                           * panIconos    *    **************
         *****************                                           ****************/
        
        /*  GENERAR LOS DOS PANELES GENERALES Y LA BARRA DE ESTADO*/
        panEstado.setBackground(new Color(0,0,50));
        panEstado.setLayout(new BorderLayout());
        areaEstado.setBackground(panEstado.getBackground());
        areaEstado.setForeground(new Color(94,127,203));
        areaEstado.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        areaEstado.setHorizontalAlignment(SwingConstants.CENTER);
        areaEstado.setOpaque(true);
        panEstado.add(areaEstado, BorderLayout.CENTER);
        
        JPanel panelConsola = new JPanel();
        panelConsola.setLayout(new BorderLayout());
        
        JPanel panelMapaDisp = new JPanel();
        panelMapaDisp.setLayout(new BoxLayout(panelMapaDisp, BoxLayout.X_AXIS));
        panelMapaDisp.setBackground(Color.black);
        //panelMapa.setBackground(Color.black);
        JPanel panGenMap = new JPanel();
        panGenMap.setLayout(new OverlayLayout(panGenMap));
        panGenMap.setBorder(new LineBorder(Color.black, 10));
        panelMapa.setOpaque(false);
        
        for(int i = 0; i < mapa.getAncho() * mapa.getAlto(); i++){
            CeldaGrafica celda = new PanelCeldaGrafica(new Punto(i%mapa.getAncho(), i/mapa.getAncho()));
            Image icon = new ImageIcon("img/celda.png").getImage().getScaledInstance(dim, dim, Image.SCALE_FAST);
            imagenes.put("celda", icon);
            celda.setImagen(new ImagenCelda(icon));
            celda.getComponente().setMinimumSize(new Dimension(dim, dim));
            celda.getComponente().setPreferredSize(new Dimension(dim, dim));
            celda.getComponente().addMouseListener(new CoordenadaAlClick(areaConsola, mapa.getJugador(), celda.getId(), MouseEvent.BUTTON1));
            celda.getComponente().addMouseListener(new ComandoAlClick(areaConsola,
                        new ComandoAtacar(mapa.getJugador(), null, celda.getId().x, celda.getId().y),
                    this, MouseEvent.BUTTON3));
            panelMapa.add(celda.getComponente());
            paneles.add(celda);
        }
        JPanel fondo = new JPanel();
        fondo.setLayout(new BoxLayout(fondo, BoxLayout.LINE_AXIS));
        JLabel lbl = new JLabel();
        lbl.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("img/fondo.png").getScaledInstance(dim_total.x, dim_total.y, Image.SCALE_SMOOTH)));
        fondo.add(lbl);
        
        panGenMap.add(panelMapa);
        panGenMap.add(fondo);
        panGenMap.setPreferredSize(new Dimension(dim_total.x, dim_total.y));
        panGenMap.setSize(panGenMap.getPreferredSize());
        panelMapaDisp.add(panGenMap);
        
        // PANEL DERECHO
        panelDerecho.setLayout(new BorderLayout());
        panelDerecho.setBorder( BorderFactory.createMatteBorder(10, 0, 10, 10, Color.black));
        panelDerecho.setBackground(areaEstado.getBackground());
        panelDerecho.setPreferredSize(new Dimension(500, dim_total.y));
        
        // PANEL MOCHILA
        panelMochila.setLayout(new BorderLayout());
        TitledBorder bordeMochila = BorderFactory.createTitledBorder(
                       BorderFactory.createLineBorder(areaEstado.getForeground()), "Mochila");
        bordeMochila.setTitleJustification(TitledBorder.CENTER);
        bordeMochila.setTitleColor(Color.white);
        bordeMochila.setTitleFont(new Font("Verdana",Font.BOLD,16));
        panelMochila.setBorder(bordeMochila);
        panelMochila.setBackground(panelDerecho.getBackground());
        
        pbrCargaObjetos.setUI(new BarraDeCargaUI());
        pbrCargaObjetos.setOpaque(false);
        pbrCargaObjetos.setBorder(new LineBorder(Color.white));
        pbrCargaObjetos.setMaximum(mapa.getJugador().getMochila().getMaxObjetos());
        panelMochila.add(pbrCargaObjetos, BorderLayout.NORTH);
        
        panelObjetos.setLayout(new FlowLayout());
        panelObjetos.setBackground(panelMochila.getBackground());
        panelObjetos.setMinimumSize(new Dimension(0, 60));
        actualizarIconosMochila();
        panelMochila.add(panelObjetos,BorderLayout.CENTER);
        
        pbrCargaPeso.setUI(new BarraDeCargaUI());
        pbrCargaPeso.setOpaque(false);
        pbrCargaPeso.setBorder(new LineBorder(Color.white));
        pbrCargaPeso.setMaximum((int)Math.round(mapa.getJugador().getMochila().getMaxPeso() * 1000) );
        panelMochila.add(pbrCargaPeso,BorderLayout.SOUTH);
        
        panelDerecho.add(panelMochila, BorderLayout.NORTH);
        
        // PANEL CELDA-NOTIFICACIONES
        panCeldaNotif.setLayout(new BorderLayout());
        panCeldaNotif.setBackground(panelDerecho.getBackground());
        
        // PANEL CELDA
        panGenCelda.setLayout(new OverlayLayout(panGenCelda));
        panScrCelda.setLayout(new BorderLayout());
        panCelda.setLayout(new FlowLayout());
        panCelda.setBackground(panelDerecho.getBackground());
        scrCelda.setViewportView(panCelda);
        scrCelda.setBackground(panelDerecho.getBackground());
        scrCelda.setBorder(null);
        scrCelda.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrCelda.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        panScrCelda.add(scrCelda, BorderLayout.CENTER);
        
        panLblCelda.setLayout(new BorderLayout());
        panLblCelda.setOpaque(false);
        JLabel lblIzq = new JLabel(new ImageIcon("img/desp_izq.png"));
        lblIzq.setForeground(areaEstado.getForeground());
        Dimension dimen = new Dimension(20,60);
        lblIzq.setPreferredSize(dimen);
        lblIzq.addMouseListener(new DesplazarAlHover(scrCelda, panGenCelda, panLblCelda, "izq"));
        JLabel lblDer = new JLabel(new ImageIcon("img/desp_der.png"));
        lblDer.setForeground(areaEstado.getForeground());
        lblDer.setPreferredSize(dimen);
        lblDer.addMouseListener(new DesplazarAlHover(scrCelda, panGenCelda, panLblCelda, "der"));
        panLblCelda.add(lblIzq, BorderLayout.BEFORE_LINE_BEGINS);
        JPanel relleno = new JPanel(); relleno.setOpaque(false);
        relleno.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panLblCelda.add(relleno, BorderLayout.CENTER);
        panLblCelda.add(lblDer, BorderLayout.AFTER_LINE_ENDS);
        
        TitledBorder bordeCelda = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(areaEstado.getForeground()), "Celda");
        bordeCelda.setTitleJustification(TitledBorder.CENTER);
        bordeCelda.setTitleColor(Color.white);
        bordeCelda.setTitleFont(new Font("Verdana",Font.BOLD,15));
        panGenCelda.setBackground(panelDerecho.getBackground());
        panGenCelda.setBorder(bordeCelda);
        panGenCelda.add(panLblCelda,0);
        panGenCelda.add(panScrCelda,1);
        
        panCeldaNotif.add(panGenCelda, BorderLayout.NORTH);
        
        // GENERAR CONSOLA DISPLAY
        areaConsolaDisplay.setFont(new Font("Verdana", Font.BOLD, 17));
        areaConsolaDisplay.setBackground(panelDerecho.getBackground());
        areaConsolaDisplay.setForeground(areaEstado.getForeground());
        areaConsolaDisplay.setDisabledTextColor(areaConsolaDisplay.getForeground());
        areaConsolaDisplay.setBorder(null);
        areaConsolaDisplay.setLineWrap(true);
        areaConsolaDisplay.setEnabled(false);
        
        scrollP.setBackground(panelDerecho.getBackground());
        scrollP.setViewportView(areaConsolaDisplay);
        TitledBorder bord = BorderFactory.createTitledBorder(
                       BorderFactory.createLineBorder(areaEstado.getForeground()), "Display");
        bord.setTitleJustification(TitledBorder.CENTER);
        bord.setTitleColor(Color.white);
        bord.setTitleFont(new Font("Verdana",Font.BOLD,15));
        scrollP.setBorder(bord);
        scrollP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        panCeldaNotif.add(scrollP, BorderLayout.CENTER);
        
        panelDerecho.add(panCeldaNotif, BorderLayout.CENTER);
        panelMapaDisp.add(panelDerecho);
        
        // GENERAR ICONOS EQUIPADO
        JPanel panIconos = new JPanel(new FlowLayout());
        lblArmaDer = new JLabel(); lblArmaDer.setIcon(new ImageIcon("img/arma_der_dis.png"));
        lblArmaIzq = new JLabel(); lblArmaIzq.setIcon(new ImageIcon("img/arma_izq_dis.png"));
        lblArmadura = new JLabel(); lblArmadura.setIcon(new ImageIcon("img/armadura_dis.png"));
        lblBinoculares = new JLabel(); lblBinoculares.setIcon(new ImageIcon("img/binoculares_dis.png"));
        lblArmaDer.setToolTipText("Arma derecha");
        lblArmaDer.addMouseListener(new ComandoAlClick(areaConsola, new ComandoDesequipar(mapa.getJugador(), "arma"), this, MouseEvent.BUTTON3));
        lblArmaDer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblArmaIzq.setToolTipText("Arma izquierda");
        lblArmaIzq.addMouseListener(new ComandoAlClick(areaConsola, new ComandoDesequipar(mapa.getJugador(), "arma_izq"), this, MouseEvent.BUTTON3));
        lblArmaIzq.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblArmadura.setToolTipText("Armadura equipada");
        lblArmadura.addMouseListener(new ComandoAlClick(areaConsola, new ComandoDesequipar(mapa.getJugador(), "armadura"), this, MouseEvent.BUTTON3));
        lblArmadura.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblBinoculares.setToolTipText("Binoculares equipados");
        lblBinoculares.addMouseListener(new ComandoAlClick(areaConsola, new ComandoDesequipar(mapa.getJugador(), "binoculares"), this, MouseEvent.BUTTON3));
        lblBinoculares.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        LineBorder borde = new LineBorder(Color.white);
        lblArmaDer.setBorder(borde); panIconos.add(lblArmaDer);
        lblArmaIzq.setBorder(borde); panIconos.add(lblArmaIzq);
        lblArmadura.setBorder(borde); panIconos.add(lblArmadura);
        lblBinoculares.setBorder(borde); panIconos.add(lblBinoculares);
        panIconos.setBackground(Color.black);
        panelDerecho.add(panIconos, BorderLayout.SOUTH);
        
        // GENERAR CONSOLA
        areaConsola.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        areaConsola.setBackground(areaEstado.getBackground());
        areaConsola.setForeground(areaEstado.getForeground());
        areaConsola.setBorder(BorderFactory.createMatteBorder(0, 10, 0, 10, Color.black));
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
        panelConsola.add(areaConsola, BorderLayout.CENTER);
        
        
        //VENTANA
        getContentPane().add(panEstado);
        getContentPane().add(panelMapaDisp);
        getContentPane().add(panelConsola);
        setResizable(false);
        setTitle(mapa.getNombre());
        toFront();
        pack();
        this.setLocationRelativeTo(null);
    }
    
    /**
     * Obtiene la imagen representante de la celda indicada. Nótese que la
     * imagen representante aquí y en el editor no son exactamente la misma
     * debido a las mecánicas de juego.
     * @param c celda de la que obtener representación
     * @return una imagen compuesta con los valores adecuados de representación
     * @see Editor.Editor#imagenRepresentante
     */
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
                CeldaGrafica panel = paneles.get(y*mapa.getAncho()+x);
                panel.setImagen(imagenRepresentante(c));
                if(mapa.getJugador().enRango(new Punto(x,y)) && c instanceof Transitable && ((Transitable)c).getNumEnemigos() > 0)
                    panel.getComponente().setCursor(cursorAtacar);
                else
                    panel.getComponente().setCursor(Cursor.getDefaultCursor());
            }
        
        actualizarIconosMochila();
        actualizarIconosEquipacion();
        actualizarPanelCelda();
    }

    /**
     * Maneja la caché de imágenes.
     * @param representacion representación de la que se desea obtener imagen
     * @return La imagen correspondiente a esa representación
     * @see Editor.Editor#obtenerImagen(java.lang.String)
     */
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
        if(areaConsolaDisplay.getLineCount() > 50) //Tratamos de que nunca tenga más de 50 líneas
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
     * @param descripcion texto a mostrar previo a que el usuario escriba
     * @return Entrada del usuario
     */
    @Override
    public String leer(String descripcion) {
        imprimir(descripcion);
        return leer();
    }
    @Override
    public String leer() {
        String ret=null;
        areaConsola.setEnabled(true);
        while((ret = comandos.poll())==null) //Esperamos a que el usuario escriba algo y esto se añada a la cola
            Thread.yield(); //La razón por la que el juego debe correr en un hilo aparte
        areaConsola.setEnabled(false);
        return ret;
    }
    
    @Override
    public void cerrar(){
        dispose(); //Ceraramos la ventana
        musica.stop();
        new MenuGrafico().lanzar(); //Lanzamos el menú inicial
    }
    
    /**
     * La consola muestra el mensaje de que te has muerto.
     * Luego carga el menú de inicio.
     */
    public void hasMuerto() {
        for(Component c : getContentPane().getComponents())
            getContentPane().remove(c);
        
        JLabel imgMuerte = new JLabel(new ImageIcon("img\\hasMuerto.png"));
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(imgMuerte, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        musica.stop();
        Clip c = Utilidades.Sonido.play("muerte");
        try {
            Thread.sleep(c.getMicrosecondLength()/1500);
        } catch (InterruptedException ex) {
            Logger.getLogger(MenuGrafico.class.getName()).log(Level.SEVERE, null, ex);
        }
        dispose();
        new MenuGrafico().lanzar();
    }
    
    /**
     * La consola se cierra. Podría usarse para mostrar un mensaje de victoria
     */
    public void hasGanado(){
        cerrar();
    }

    private void actualizarIconosMochila() {
        if(mapa == null || mapa.getJugador() == null || mapa.getJugador().getMochila() == null)
            return;
        
        panelObjetos.removeAll();
        
        for(Objeto o: mapa.getJugador().getMochila().getObjetos()){
            IconoObjetoMochila ico = new IconoObjetoMochila(o);
            Comando comando = new ComandoEquipar(mapa.getJugador(), o.getNombre());
            ico.addMouseListener(new ComandoAlClick(areaConsola, comando, this, MouseEvent.BUTTON1));
            comando = new ComandoTirar(mapa.getJugador(), o.getNombre());
            ico.addMouseListener(new ComandoAlClick(areaConsola, comando, this, MouseEvent.BUTTON3));
            ico.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            panelObjetos.add(ico);
        }
        
        pbrCargaObjetos.setValue(mapa.getJugador().getMochila().getNumObj());
        pbrCargaObjetos.setToolTipText("Objetos: " + mapa.getJugador().getMochila().getNumObj() + "/" + mapa.getJugador().getMochila().getMaxObjetos());
        pbrCargaPeso.setValue((int)Math.round(mapa.getJugador().getMochila().pesoActual() * 1000) );
        pbrCargaPeso.setToolTipText("Peso: " + mapa.getJugador().getMochila().pesoActual() + "/" + mapa.getJugador().getMochila().getMaxPeso());
        
        Rectangle r = panelMochila.getBounds();
        panelMochila.repaint(r);
        panelObjetos.revalidate();
    }
    private void actualizarIconosEquipacion() {
        if(mapa != null && mapa.getJugador() != null){
            Jugador j = mapa.getJugador();
            lblArmaDer.setIcon(new ImageIcon("img/arma_der"+(j.getArma() != null?"":"_dis")+".png"));
            lblArmaDer.setToolTipText(j.getArma()!=null?j.getArma().toString():"Arma equipada.");
            lblArmaIzq.setIcon(new ImageIcon("img/arma_izq"+(j.getArma_izq()!= null?"":"_dis")+".png"));
            lblArmaIzq.setToolTipText(j.getArma_izq()!=null?j.getArma_izq().toString():"Arma equipada izquierda.");
            lblArmadura.setIcon(new ImageIcon("img/armadura"+(j.getArmadura() != null?"":"_dis")+".png"));
            lblArmadura.setToolTipText(j.getArmadura()!=null?j.getArmadura().toString():"Armadura equipada.");
            lblBinoculares.setIcon(new ImageIcon("img/binoculares"+(j.tieneBinoculares()?"":"_dis")+".png"));
            lblBinoculares.setToolTipText(j.tieneBinoculares()?j.getBinoculares().toString():"Binoculares equipados.");
        }
    }
    private void actualizarPanelCelda(){
        panCelda.removeAll();
        
        for(Objeto o: ((Transitable)mapa.getCelda(mapa.getJugador().getPos())).getObjetos()){
            IconoObjetoMochila ico = new IconoObjetoMochila(o);
            Comando comando = new ComandoCoger(mapa.getJugador(), o.getNombre());
            ico.addMouseListener(new ComandoAlClick(areaConsola, comando, this, MouseEvent.BUTTON1));
            ico.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            panCelda.add(ico);
        }
        panCelda.repaint();
        panGenCelda.moveToFront(panLblCelda);
    }
    //Adapter creado para escribir unas coordenadas relativas al jugador cuando
    //se hace click en un elemento
    private static class CoordenadaAlClick extends MouseAdapter {
        private Punto pt;
        private JTextField area;
        private Personaje personaje;
        private int boton;
        
        public CoordenadaAlClick(JTextField donde, Personaje relativo, Punto pt, int boton) {
            this.pt = pt;
            this.personaje = relativo;
            this.area = donde;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == boton && area.isEnabled()){
                String pre = area.getText().substring(0, area.getSelectionStart());
                String pos = area.getText().substring(area.getSelectionEnd());
                String texto = String.format("%s%s%d %d%s%s",
                        pre,
                        pre.endsWith(" ")?"":" ",
                        pt.x - personaje.getPos().x, pt.y - personaje.getPos().y,
                        pos.startsWith(" ")?"":" ",
                        pos
                        );
                area.setText(texto);
            }
        }
    } 
    //Adapter creado para escribir un texto cuando se hace click en un elemento
    private static class TextoAlClick extends MouseAdapter {
        private String texto;
        private JTextField area;
        public TextoAlClick(JTextField donde, String texto) {
            this.texto = texto;
            this.area = donde;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(area.isEnabled()){
                String pre = area.getText().substring(0, area.getSelectionStart());
                String pos = area.getText().substring(area.getSelectionEnd());
                String text = String.format("%s%s%s%s%s",
                        pre,
                        pre.endsWith(" ")?"":" ",
                        texto,
                        pos.startsWith(" ")?"":" ",
                        pos
                        );
                area.setText(text);
            }
        }
    }
    //Adapter creado para ejecutar un comando cuando se hace click en un elemento
    private static class ComandoAlClick extends MouseAdapter {
        private Comando comando;
        private JTextField area;
        private ConsolaGrafica cg;
        private int boton;
        public ComandoAlClick(JTextField donde, Comando comando, ConsolaGrafica cg, int boton) {
            this.comando = comando;
            this.area = donde;
            this.cg = cg;
            this.boton = boton;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == boton && area.isEnabled()){
                try {
                    comando.ejecutar();
                } catch (ComandoExcepcion ex) {
                    cg.limpiar();
                    cg.imprimir(ex.getMessage());
                }
                cg.imprimirMapa();
            }
        }
    }    
    //Adapter creado para desplazar un scroll-pane cuando se coloca el ratón encima
    private static class DesplazarAlHover extends MouseAdapter{
        private JScrollPane scroll;
        private String dir;
        private Thread movimiento;
        private JLayeredPane panel;
        private JPanel panelBotones;
        
        public DesplazarAlHover(JScrollPane scroll, JLayeredPane panel, JPanel panelBotones, String dir) {
            this.scroll = scroll;
            this.dir = dir;
            this.panel = panel;
            this.panelBotones = panelBotones;
            movimiento = null;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(movimiento != null){
                movimiento.interrupt();
                panel.moveToFront(panelBotones);
            }
            movimiento = null;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            movimiento = new Thread(){
                @Override
                public void run() {
                    while(true){
                        Rectangle r = scroll.getViewport().getViewRect();
                        switch(dir){
                            case "der":
                                r.x += 1;
                                break;
                            case "izq":
                                r.x -= 1;
                                break;
                        }
                        r.x = Math.max(0, r.x);
                        scroll.getViewport().setViewPosition(r.getLocation());
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException ex) {
                            break;
                        }
                    }
                }
            };
            movimiento.start();
        }
        
    }
}