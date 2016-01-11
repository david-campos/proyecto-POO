package Editor;

import Excepciones.CeldaObjetivoNoValida;
import Utilidades.Adaptador;
import Mapa.Celda;
import Utilidades.CeldaGrafica;
import Utilidades.ImagenCelda;
import Mapa.Mapa;
import Mapa.NoTransitable;
import Utilidades.PanelCeldaGrafica;
import Utilidades.Punto;
import Mapa.Transitable;
import Menus.MenuGrafico;
import Objetos.Objeto;
import Personajes.Enemigo;
import com.google.gson.GsonBuilder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * Ventana del editor de mapas, desde ella se inician todas las acciones fundamentales
 * de la edición de mapa.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class Editor extends javax.swing.JFrame {

    /**
     * Borde por defecto de las celdas del editor.
     */
    public static final Border BORDE_DEF = new LineBorder(Color.white);
    /**
     * Borde de las celdas del editor cuando pasa por encima el ratón.
     */
    public static final Border BORDE_HOVER = new LineBorder(Color.blue);
    /**
     * Borde de las celdas del editor cuando la herramienta es MOVER_OBJETO
     * o MOVER_ENEMIGO y el movimiento se puede realizar.
     */
    public static final Border BORDE_MOVER = new LineBorder(Color.green);
    /**
     * Borde de las celdas del editor cuando están seleccionadas.
     */
    public static final Border BORDE_SELEC = new LineBorder(Color.black);
    /**
     * Borde de las celdas del editor cuando se editan sus propiedades
     */
    public static final Border BORDE_PROP = new LineBorder(Color.orange);
    /**
     * Borde de las celdas del editor cuando la herramienta es MOVER_OBJETO
     * o MOVER_ENEMIGO y el movimiento <b>no</b> se puede realizar.
     */
    public static final Border BORDE_NOMOVER = new LineBorder(Color.red);

    /**
     * Obtiene un nombre válido para el objeto en el mapa editado. <br>
     * El método simplemente delega en {@link Mapa#obtenerNombreObjeto}
     * @param objeto nombre de objeto base sobre el que obtener un nombre válido (no debería acabar en número)
     * @param excluido objeto a obviar a la hora de buscar si el nombre es
     *          adecuado (normalmente el propio objeto que se va a nombrar,
     *          o null).
     * @return Un nombre de objeto válido. Un nombre de objeto válido es aquel que no se repite en todo el mapa.
     */
    public String obtenerNombreObjeto(String objeto, Objeto excluido) {
        return mapa.obtenerNombreObjeto(objeto, excluido);
    }

    /**
     * Obtiene un nombre válido para el enemigo en el mapa editado. <br>
     * El método simplemente delega en {@code Mapa.obtenerNombreEnemigo}
     * @param nombreBase nombre de enemigo base sobre el que obtener un nombre válido (no debería acabar en número)
     * @param excluido enemigo excluido a la hora de buscar el nombre válido
     *          (normalmente el propio enemigo al que se va a renombrar, o null).
     * @return Un nombre de enemigo válido. Un nombre de enemigo válido es aquel que no se repite en todo el mapa.
     */
    public String obtenerNombreEnemigo(String nombreBase, Enemigo excluido) {
        return mapa.obtenerNombreEnemigo(nombreBase, excluido);
    }
    
    /**
     * <p>Enumeración de las posibles herramientas del editor</p>
     * <ul>
     * <li> NORMAL: La herramienta normal, permite cambiar los tipos de las celdas
     * al girar la rueda del ratón, abrir el menú desplegable de celda con el click
     * derecho, y cambiar las propiedades de las celdas con un doble click. También
     * permite el movimiento de todos los enemigos de una celda a otra.
     * <li> MOVER_OBJETO: Se activa cuando en el menú desplegable de la celda se
     * va a un objeto y se selecciona "mover". Permite únicamente elegir la posición
     * a la que se moverá el objeto y moverlo haciendo click izquierdo.
     * <li> MOVER_ENEMIGO: Igual que MOVER_OBJETO, pero para los enemigos. No confundir
     * con el movimiento de enemigos de la herramienta NORMAL.
     * </ul>
     * @see #moverEnemigo
     * @see #moverObjeto
     * @see MenuCelda
     * @see CeldasML
     * @see #getHerramienta
     * @see #setHerramienta
     */
    public static enum Herramienta{
        /**
         * @see Herramienta
         */
        NORMAL,
        /**
         * @see Herramienta
         */
        MOVER_ENEMIGO,
        /**
         * @see Herramienta
         */
        MOVER_OBJETO
    }
    
    /* PROPIEDADES DEL EDITOR */
    private final MouseListener mouseListenerCeldas = new CeldasML(this); //mouseListener para las celdas (uno para todas)
    private Mapa mapa; //mapa en edición
    private File archivoMapa; //archivo del mapa en edición
    private JPanel panMapaEnEdicion; //panel del mapa en edición
    private CeldaGrafica seleccionada; //celda seleccionada
    private final ArrayList<CeldaGrafica> celdas; //celdas mostradas en el editor
    private final HashMap<String, Image> imagenes; //base de imágenes ya escaladas
    
    private int tam_celda; //tamaño de las celdas (alto = ancho)
    private Herramienta herramienta; //herramienta actual
    private Objeto objetoMovido; //Objeto movido (herramienta mover objeto)
    private Transitable celdaOrigen; //Origen del objeto (herramienta mover objeto)
    private Enemigo enemigoMovido; //Enemigo movido (herramienta mover enemigo)
    
    /**
     * Crea un nuevo formulario de editor
     */
    public Editor() {
        mapa = null                     ;   archivoMapa = null;
        seleccionada = null             ;   tam_celda = 40;
        objetoMovido = null             ;   enemigoMovido = null;
        imagenes = new HashMap()        ;   celdas = new ArrayList();
        herramienta = Herramienta.NORMAL;
        
        initComponents();
    }

    /**
     * Obtiene el mapa en edición del editor.
     * @return El mapa en edición, o {@code null} si no hay ningún mapa en edición.
     */
    public Mapa getMapa() {
        return mapa;
    }

    /**
     * Fija el mapa en edición del editor. <br>
     * Esto no provoca la actualización de ninguna otra propiedad. Si se desea
     * usar recordar:
     * <ul>
     * <li> Llamar a {@link #cerrarMapa} primero.
     * <li> Llamar a este método.
     * <li> Llamar a continuación a {@link #generarPanelMapa} para generar el panel.
     * </ul>
     * @param mapa el mapa a editar
     */
    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
    }

    /**
     * Devuelve la celda seleccionada actualmente en el editor
     * @return La celda que está seleccionada, o null si no lo está ninguna.
     */
    public CeldaGrafica getSeleccionada() {
        return seleccionada;
    }

    /**
     * Si la celda está seleccionada, la deselecciona, y si no lo está, la selecciona
     * @param cg celda a seleccionar/deseleccionar
     */
    public void toggleSeleccionada(CeldaGrafica cg){
        if(cg != null && cg.equals(seleccionada))
            setSeleccionada(null);
        else
            setSeleccionada(cg);
    }

    /**
     * Selecciona la celda, se ocupa también de corregir los bordes.
     * @param seleccionada celda a seleccionar
     */
    public void setSeleccionada(CeldaGrafica seleccionada) {
        if(this.seleccionada != null)
            this.seleccionada.getComponente().setBorder(BORDE_DEF);
        
        this.seleccionada = seleccionada;
        
        if(seleccionada != null)
            seleccionada.getComponente().setBorder(BORDE_SELEC);
    }

    /**
     * Obtiene las celdas mostradas en este editor
     * @return El array original de celdas mostradas en este editor.
     */
    public ArrayList<CeldaGrafica> getCeldas() {
        return celdas;
    }
    
    /**
     * Obtiene la celdaDeOrigen, este parámetro se usa al realizar movimientos
     * con las herramientas {@code MOVER_OBJETO} o {@code MOVER_ENEMIGO}.
     * @return El valor actual de celdaDeOrigen
     * @see Herramienta
     * @see MenuCelda#menuEnemigo_actionPerformed
     * @see MenuCelda#menuObjeto_actionPerformed
     */
    public Transitable getCeldaOrigen() {
        return celdaOrigen;
    }

    /**
     * Cambia la celdaDeOrigen, este parámetro se usa al realizar movimientos
     * con las herramientas {@code MOVER_OBJETO} o {@code MOVER_ENEMIGO}.
     * @param celdaOrigen el nuevo valor de celdaDeOrigen
     * @see Herramienta
     * @see MenuCelda#menuEnemigo_actionPerformed
     * @see MenuCelda#menuObjeto_actionPerformed
     */
    public void setCeldaOrigen(Transitable celdaOrigen) {
        this.celdaOrigen = celdaOrigen;
    }

    /**
     * Obtiene el objeto movido (si es que se está moviendo alguno).
     * @return El objeto que se está moviendo.
     * @see Herramienta
     */
    public Objeto getObjetoMovido() {
        return objetoMovido;
    }

    /**
     * Fija el objeto movido (sólo útil con la herramienta {@code MOVER_OBJETO})
     * @param objetoMovido el objeto que se desea mover de celda
     * @see Herramienta
     */
    public void setObjetoMovido(Objeto objetoMovido) {
        this.objetoMovido = objetoMovido;
    }

    /**
     * Obtiene el enemigo movido (cuando se usa la herramienta {@code MOVER_ENEMIGO})
     * @return El enemigo que se está moviendo de celda
     * @see Herramienta
     */
    public Enemigo getEnemigoMovido() {
        return enemigoMovido;
    }

    /**
     * Cambia el enemigo movido (solo tiene sentido cuando se usa la herramienta {@code MOVER_ENEMIGO})
     * @param enemigoMovido enemigo que se desea mover de celda
     * @see Herramienta
     */
    public void setEnemigoMovido(Enemigo enemigoMovido) {
        this.enemigoMovido = enemigoMovido;
    }

    /**
     * Obtiene la herramienta activa en el editor
     * @return La herramienta que está funcionando en el momento de la llamada
     * @see Herramienta
     */
    public Herramienta getHerramienta() {
        return herramienta;
    }

    /**
     * Cambia la herramienta activa en el editor.<br>
     * Recordar que {@code MOVER_OBJETO} y {@code MOVER_ENEMIGO} requieren
     * fijar los elementos correspondientes con {@link #setObjetoMovido} y
     * {@link #setEnemigoMovido}.
     * @param herramienta la herramienta activa que se desea usar
     * @see Herramienta
     */
    public void setHerramienta(Herramienta herramienta) {
        this.herramienta = herramienta;
    }

    /**
     * Obtiene el archivo donde se ha de guardar el mapa (null si aun no ha sido
     * guardado nunca).
     * @return El archivo donde se ha de guardar el mapa en edición.
     */
    public File getArchivoMapa() {
        return archivoMapa;
    }

    /**
     * Cambia el archivo donde se ha de guardar el mapa en edición.
     * @param archivoMapa el archivo donde se desee realizar el próximo guardado del mapa
     */
    public void setArchivoMapa(File archivoMapa) {
        this.archivoMapa = archivoMapa;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fchMapa = new javax.swing.JFileChooser(System.getProperty("user.dir"));
        dlgNuevoMapa = new javax.swing.JDialog();
        panGeneral = new javax.swing.JPanel();
        panNombreMapa = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNombreMapa = new javax.swing.JTextField();
        panDescripcion = new javax.swing.JPanel();
        lblDescripcion = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescripcionMapa = new javax.swing.JTextArea();
        panDimensiones = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        spnAncho = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        spnAlto = new javax.swing.JSpinner();
        panAleatorio = new javax.swing.JPanel();
        cbxAleatorio = new javax.swing.JCheckBox();
        panAcceptCancel = new javax.swing.JPanel();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        panMapa = new javax.swing.JPanel();
        tbrInferior = new javax.swing.JToolBar();
        lblInfo = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        lblCoordenadas = new javax.swing.JLabel();
        panZoom = new javax.swing.JPanel();
        sldZoom = new javax.swing.JSlider();
        mbrSuperior = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mitNuevo = new javax.swing.JMenuItem();
        mitAbrir = new javax.swing.JMenuItem();
        mitGuardar = new javax.swing.JMenuItem();
        mitGuardarComo = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mirCerrarMapa = new javax.swing.JMenuItem();
        mitCerrar = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mitEditarNombreMapa = new javax.swing.JMenuItem();
        mitDescripcionMapa = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        mitEditarCelda = new javax.swing.JMenuItem();

        fchMapa.setAcceptAllFileFilterUsed(false);
        fchMapa.setDialogTitle("Abrir mapa");
        fchMapa.setFileFilter(new Utilidades.FiltroMapa());
        fchMapa.setFileView(new Utilidades.FileViewMapa());
        fchMapa.setToolTipText("");

        dlgNuevoMapa.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        dlgNuevoMapa.setTitle("Nuevo mapa");
        dlgNuevoMapa.setAlwaysOnTop(true);
        dlgNuevoMapa.setMinimumSize(new java.awt.Dimension(446, 280));
        dlgNuevoMapa.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dlgNuevoMapa.setResizable(false);
        dlgNuevoMapa.setSize(new java.awt.Dimension(446, 280));
        dlgNuevoMapa.setLocationRelativeTo(null);

        panGeneral.setLayout(new javax.swing.BoxLayout(panGeneral, javax.swing.BoxLayout.Y_AXIS));

        panNombreMapa.setLayout(new java.awt.BorderLayout(5, 0));

        jLabel1.setText("Nombre del mapa:");
        panNombreMapa.add(jLabel1, java.awt.BorderLayout.LINE_START);

        txtNombreMapa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fijarFondoBlanco(evt);
            }
        });
        panNombreMapa.add(txtNombreMapa, java.awt.BorderLayout.CENTER);

        panGeneral.add(panNombreMapa);

        panDescripcion.setLayout(new java.awt.BorderLayout(0, 5));

        lblDescripcion.setText("Descripción:");
        panDescripcion.add(lblDescripcion, java.awt.BorderLayout.PAGE_START);

        txtDescripcionMapa.setColumns(20);
        txtDescripcionMapa.setRows(2);
        txtDescripcionMapa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fijarFondoBlanco(evt);
            }
        });
        jScrollPane1.setViewportView(txtDescripcionMapa);

        panDescripcion.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        panGeneral.add(panDescripcion);

        jLabel3.setText("Ancho:");
        panDimensiones.add(jLabel3);

        spnAncho.setModel(new javax.swing.SpinnerNumberModel(30, 5, 50, 1));
        spnAncho.setEditor(new javax.swing.JSpinner.NumberEditor(spnAncho, ""));
        panDimensiones.add(spnAncho);

        jLabel4.setText("Alto:");
        panDimensiones.add(jLabel4);

        spnAlto.setModel(new javax.swing.SpinnerNumberModel(30, 5, 50, 1));
        spnAlto.setEditor(new javax.swing.JSpinner.NumberEditor(spnAlto, ""));
        spnAlto.setValue(30);
        panDimensiones.add(spnAlto);

        panGeneral.add(panDimensiones);

        cbxAleatorio.setSelected(true);
        cbxAleatorio.setText("Generar aleatorio");
        cbxAleatorio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panAleatorioLayout = new javax.swing.GroupLayout(panAleatorio);
        panAleatorio.setLayout(panAleatorioLayout);
        panAleatorioLayout.setHorizontalGroup(
            panAleatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbxAleatorio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panAleatorioLayout.setVerticalGroup(
            panAleatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbxAleatorio)
        );

        panGeneral.add(panAleatorio);

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dlgNuevoMapa_btnAceptarClick(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dlgNuevoMapa_btnCancelarClick(evt);
            }
        });

        javax.swing.GroupLayout panAcceptCancelLayout = new javax.swing.GroupLayout(panAcceptCancel);
        panAcceptCancel.setLayout(panAcceptCancelLayout);
        panAcceptCancelLayout.setHorizontalGroup(
            panAcceptCancelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panAcceptCancelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAceptar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar))
        );
        panAcceptCancelLayout.setVerticalGroup(
            panAcceptCancelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panAcceptCancelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(panAcceptCancelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAceptar)
                    .addComponent(btnCancelar)))
        );

        panGeneral.add(panAcceptCancel);

        javax.swing.GroupLayout dlgNuevoMapaLayout = new javax.swing.GroupLayout(dlgNuevoMapa.getContentPane());
        dlgNuevoMapa.getContentPane().setLayout(dlgNuevoMapaLayout);
        dlgNuevoMapaLayout.setHorizontalGroup(
            dlgNuevoMapaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgNuevoMapaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panGeneral, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                .addContainerGap())
        );
        dlgNuevoMapaLayout.setVerticalGroup(
            dlgNuevoMapaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgNuevoMapaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panGeneral, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editor de mapas");
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/Menus/ico_map.png")).getImage());
        setMinimumSize(new java.awt.Dimension(440, 417));
        setName("editor"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        panMapa.setBackground(new java.awt.Color(51, 51, 51));
        panMapa.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        panMapa.setLayout(null);
        jScrollPane2.setViewportView(panMapa);

        getContentPane().add(jScrollPane2, java.awt.BorderLayout.CENTER);

        tbrInferior.setFloatable(false);
        tbrInferior.setMaximumSize(new java.awt.Dimension(13, 25));
        tbrInferior.setMinimumSize(new java.awt.Dimension(13, 25));

        lblInfo.setText("Ningún mapa seleccionado");
        tbrInferior.add(lblInfo);
        tbrInferior.add(jSeparator3);

        lblCoordenadas.setText("Coordenadas de la celda");
        tbrInferior.add(lblCoordenadas);

        panZoom.setLayout(new java.awt.BorderLayout());

        sldZoom.setMaximum(160);
        sldZoom.setMinimum(30);
        sldZoom.setMinorTickSpacing(5);
        sldZoom.setToolTipText("Zoom");
        sldZoom.setValue(80);
        sldZoom.setEnabled(false);
        sldZoom.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldZoomStateChanged(evt);
            }
        });
        panZoom.add(sldZoom, java.awt.BorderLayout.LINE_END);

        tbrInferior.add(panZoom);

        getContentPane().add(tbrInferior, java.awt.BorderLayout.SOUTH);

        jMenu1.setText("Archivo");

        mitNuevo.setText("Nuevo mapa");
        mitNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNuevoActionPerformed(evt);
            }
        });
        jMenu1.add(mitNuevo);

        mitAbrir.setText("Abrir mapa");
        mitAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAbrirActionPerformed(evt);
            }
        });
        jMenu1.add(mitAbrir);

        mitGuardar.setText("Guardar mapa");
        mitGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitGuardarActionPerformed(evt);
            }
        });
        jMenu1.add(mitGuardar);

        mitGuardarComo.setText("Guardar como...");
        mitGuardarComo.setToolTipText("");
        mitGuardarComo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitGuardarComoActionPerformed(evt);
            }
        });
        jMenu1.add(mitGuardarComo);
        jMenu1.add(jSeparator1);

        mirCerrarMapa.setText("Cerrar mapa");
        mirCerrarMapa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mirCerrarMapaActionPerformed(evt);
            }
        });
        jMenu1.add(mirCerrarMapa);

        mitCerrar.setText("Cerrar editor");
        mitCerrar.setToolTipText("");
        mitCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCerrarActionPerformed(evt);
            }
        });
        jMenu1.add(mitCerrar);

        mbrSuperior.add(jMenu1);

        jMenu2.setText("Editar");

        mitEditarNombreMapa.setText("Nombre del mapa");
        mitEditarNombreMapa.setToolTipText("");
        mitEditarNombreMapa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditarNombreMapaActionPerformed(evt);
            }
        });
        jMenu2.add(mitEditarNombreMapa);

        mitDescripcionMapa.setText("Descripción del mapa");
        mitDescripcionMapa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDescripcionMapaActionPerformed(evt);
            }
        });
        jMenu2.add(mitDescripcionMapa);
        jMenu2.add(jSeparator2);

        mitEditarCelda.setText("Celda...");
        mitEditarCelda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditarCeldaActionPerformed(evt);
            }
        });
        jMenu2.add(mitEditarCelda);

        mbrSuperior.add(jMenu2);

        setJMenuBar(mbrSuperior);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Click en elemento del menú Archivo/Nuevo mapa
    private void mitNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNuevoActionPerformed
        nuevoMapa();
    }//GEN-LAST:event_mitNuevoActionPerformed
    // Click en elemento del menú Archivo/Abrir mapa
    private void mitAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAbrirActionPerformed
        abrirMapa();
    }//GEN-LAST:event_mitAbrirActionPerformed
    // Click en elemento del menú Archivo/Guardar mapa
    private void mitGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitGuardarActionPerformed
        guardarMapa();
    }//GEN-LAST:event_mitGuardarActionPerformed

    //Diálogo de creación de mapa, al hacer click en aceptar
    private void dlgNuevoMapa_btnAceptarClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlgNuevoMapa_btnAceptarClick
        //Nombres y descripción válidos
        if(!txtNombreMapa.getText().isEmpty()){
            if(!txtDescripcionMapa.getText().isEmpty()){
                dlgNuevoMapa.setVisible(false); //oculta el cuadro de dialogo
                cerrarMapa(); //importante cerrar el mapa
                if(cbxAleatorio.isSelected()){ //Si se ha marcado la casilla de generar aleatorio
                    mapa = new Mapa(
                            txtNombreMapa.getText(),
                            txtDescripcionMapa.getText(),
                            (int)spnAncho.getValue(),
                            (int)spnAlto.getValue(),
                            Mapa.getPlantillaAleatoria((int)spnAncho.getValue(), (int)spnAlto.getValue()),
                            null);
                    mapa.setEnemigosAleatorio();
                    mapa.setObjetosAleatorio();
                }else
                    mapa = new Mapa(txtNombreMapa.getText(),txtDescripcionMapa.getText(),(int)spnAncho.getValue(),(int)spnAlto.getValue(),null);
                generarPanelMapa(); //Generamos el panel
                info("Creado mapa '"+mapa.getNombre()+"'"); //Notificamos la creación
            }else
                txtDescripcionMapa.setBackground(new Color(255, 200, 200)); //Si falta la descripción, se pone en rojo
        }else
            txtNombreMapa.setBackground(new Color(255, 200, 200)); //Si falta el nombre del mapa, se pone en rojo
            
    }//GEN-LAST:event_dlgNuevoMapa_btnAceptarClick
    //Diálogo de creación de mapa, al hacer click en cancelar
    private void dlgNuevoMapa_btnCancelarClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlgNuevoMapa_btnCancelarClick
        dlgNuevoMapa.setVisible(false);
        info("Cancelado nuevo mapa.");
    }//GEN-LAST:event_dlgNuevoMapa_btnCancelarClick
    //Usado para el evento onKeyPressed de los cuadros de texto del diálogo creación de mapa
    private void fijarFondoBlanco(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fijarFondoBlanco
        ((Component)evt.getSource()).setBackground(Color.white);
    }//GEN-LAST:event_fijarFondoBlanco

    //Click en el elemento del menú Archivo/Guardar como...
    private void mitGuardarComoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitGuardarComoActionPerformed
        guardarMapaComo();
    }//GEN-LAST:event_mitGuardarComoActionPerformed
    //Click en el elemento del menú Editar/Celda
    private void mitEditarCeldaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditarCeldaActionPerformed
        editarCelda(seleccionada);
    }//GEN-LAST:event_mitEditarCeldaActionPerformed
    //Evento lanzado cuando el formulario se está cerrando
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        cerrarMapa();
    }//GEN-LAST:event_formWindowClosing
    //Evento de cambio del zoom
    private void sldZoomStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldZoomStateChanged
       tam_celda = sldZoom.getValue(); //Siguiente valor de zoom
       flushImagenes(); //Vaciamos las imágenes (para forzar a reescalarlas)
       regenerarPanelMapa(); //Regeneramos el panel completo
    }//GEN-LAST:event_sldZoomStateChanged
    //Click en el elemento del menú Archivo/Cerrar editor
    private void mitCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCerrarActionPerformed
        dispose();
    }//GEN-LAST:event_mitCerrarActionPerformed
    //Click en el elemento del menú Archivo/Cerrar mapa
    private void mirCerrarMapaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mirCerrarMapaActionPerformed
        cerrarMapa();
    }//GEN-LAST:event_mirCerrarMapaActionPerformed
    //Evento lanzado cuando el formulario se cerró.
    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        new MenuGrafico().lanzar();
    }//GEN-LAST:event_formWindowClosed
    //Click en el elemento del menú Editar/Nombre del mapa
    private void mitEditarNombreMapaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditarNombreMapaActionPerformed
        String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre:", mapa.getNombre());
        if(nuevoNombre != null && !nuevoNombre.isEmpty())
            mapa.setNombre(nuevoNombre);
    }//GEN-LAST:event_mitEditarNombreMapaActionPerformed
    //Click en el elemento del menú Editar/Descripción del mapa
    private void mitDescripcionMapaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDescripcionMapaActionPerformed
        String nuevaDescripcion = JOptionPane.showInputDialog("Nueva descripción:", mapa.getDescripcion());
        if(nuevaDescripcion != null && !nuevaDescripcion.isEmpty())
            mapa.setDescripcion(nuevaDescripcion);
    }//GEN-LAST:event_mitDescripcionMapaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Editor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JCheckBox cbxAleatorio;
    private javax.swing.JDialog dlgNuevoMapa;
    private javax.swing.JFileChooser fchMapa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JLabel lblCoordenadas;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JMenuBar mbrSuperior;
    private javax.swing.JMenuItem mirCerrarMapa;
    private javax.swing.JMenuItem mitAbrir;
    private javax.swing.JMenuItem mitCerrar;
    private javax.swing.JMenuItem mitDescripcionMapa;
    private javax.swing.JMenuItem mitEditarCelda;
    private javax.swing.JMenuItem mitEditarNombreMapa;
    private javax.swing.JMenuItem mitGuardar;
    private javax.swing.JMenuItem mitGuardarComo;
    private javax.swing.JMenuItem mitNuevo;
    private javax.swing.JPanel panAcceptCancel;
    private javax.swing.JPanel panAleatorio;
    private javax.swing.JPanel panDescripcion;
    private javax.swing.JPanel panDimensiones;
    private javax.swing.JPanel panGeneral;
    private javax.swing.JPanel panMapa;
    private javax.swing.JPanel panNombreMapa;
    private javax.swing.JPanel panZoom;
    private javax.swing.JSlider sldZoom;
    private javax.swing.JSpinner spnAlto;
    private javax.swing.JSpinner spnAncho;
    private javax.swing.JToolBar tbrInferior;
    private javax.swing.JTextArea txtDescripcionMapa;
    private javax.swing.JTextField txtNombreMapa;
    // End of variables declaration//GEN-END:variables

    
    //Métodos de funcionamiento

    /**
     * Lanza el cuadro de diálogo de nuevoMapa para iniciar un nuevo mapa.
     */
    public void nuevoMapa() {
        fchMapa.setCurrentDirectory(null);
        txtNombreMapa.setText("Mapa");
        txtDescripcionMapa.setText("Descripcion...");
        dlgNuevoMapa.setVisible(true);
    }

    /**
     * Pregunta al usuario si desea guardar el mapa y a continuación
     * cierra el mapa, dejando todo listo para una apertura de otro
     * mapa distinto.
     */
    public void cerrarMapa(){
        if(mapa != null){
            if(JOptionPane.showConfirmDialog(null,
                    "¿Desea guardar el mapa primero?",
                    "Guardar mapa...",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                guardarMapa();
            
            celdas.clear();
            mapa = null;
            archivoMapa = null;
            seleccionada = null;
            panMapa.remove(panMapaEnEdicion);
            panMapaEnEdicion = null;
            panMapa.repaint();
            sldZoom.setEnabled(false);
        }
    }
    
    /**
     * Solicita al jugador un nuevo mapa a abrir y lo abre. Llama a {@code cerrarMapa}.
     */
    public void abrirMapa() {
        fchMapa.setDialogTitle("Abrir mapa...");
        int returnVal = fchMapa.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String n = fchMapa.getSelectedFile().getName();
            if(n.lastIndexOf(".") != -1 && n.substring(n.lastIndexOf(".")+1).equals(Utilidades.ConstantesGenerales.EXTENSION_MAPA)){
                cerrarMapa(); //Cerramos el mapa
                try {
                    BufferedReader lector = new BufferedReader(
                            new FileReader(fchMapa.getSelectedFile())
                    ); //Creamos el lector
                    mapa = new GsonBuilder()
                            .registerTypeAdapter(Celda.class, new Adaptador<Celda>())
                            .registerTypeAdapter(Objeto.class, new Adaptador<Objeto>())
                            .registerTypeAdapter(Enemigo.class, new Adaptador<Enemigo>())
                            .create().fromJson(lector, Mapa.class); //Gson lo hace por nosotros :D
                    //Es necesario linkear Celdas y Enemigos al mapa,
                    //no descubrí como hacerlo directamente en el Adaptador
                    for(Celda c: mapa.getCeldas())
                        c.setMapa(mapa); //Linkeamos las celdas al mapa (no se guarda esto en el archivo)
                    for(Enemigo e: mapa.getEnemigos()){
                        try {
                            e.setMapa(mapa); //Linkeamos los enemigos al mapa (no se guarda esto en el archivo)
                        } catch (CeldaObjetivoNoValida ex) {
                           //No debería ser no válida, fue guardado ahí.
                        }
                    }
                    archivoMapa = fchMapa.getSelectedFile();
                    generarPanelMapa(); //Generamos el panel
                    info("Mapa abierto: " + fchMapa.getSelectedFile().getAbsolutePath());
                } catch (IOException ex) {
                    System.out.println(ex); //Lo único que podemos hacer es imprimirlo a la consola la verdad
                    info("Error leyendo archivo del mapa.");
                }
            }else
                JOptionPane.showMessageDialog(null, "Archivo no válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Guarda el mapa. Si no hay archivo de guardado definido llama a {@link #guardarMapaComo}.
     */
    public void guardarMapa(){
        if(mapa!=null){
            if(archivoMapa != null)
            {
                File copiaSeguridad = new File(archivoMapa.getName() + ".saving");
                if(archivoMapa.exists())
                    archivoMapa.renameTo(copiaSeguridad); //Por si falla el guardado :s
                try {
                    BufferedWriter escritor = new BufferedWriter(new FileWriter(archivoMapa));
                    new GsonBuilder()
                        .addSerializationExclusionStrategy(new EstrategiaGuardado())
                        .registerTypeAdapter(Celda.class, new Adaptador<Celda>())
                        .registerTypeAdapter(Objeto.class, new Adaptador<Objeto>())
                        .registerTypeAdapter(Enemigo.class, new Adaptador<Enemigo>())
                        .create().toJson(mapa, escritor); //Bendito Gson
                    escritor.close();

                    if(copiaSeguridad.exists())
                        copiaSeguridad.delete(); //La copia de seguridad ya no es necesaria

                    info("Mapa guardado: " + fchMapa.getSelectedFile().getAbsolutePath());
                } catch (IOException ex) {
                    System.out.println(ex);
                    info("Error al guardar el mapa.");
                    copiaSeguridad.renameTo(archivoMapa); //Si falla recuperamos el anterior
                }
            }else
                guardarMapaComo();
        }else
            info("Ningún mapa abierto para guardar.");
    }
    
    /**
     * Solicita al usuario un archivo de guardado del mapa y lo guarda.
     */
    public void guardarMapaComo() {
        if(mapa != null){
            fchMapa.setDialogTitle("Guardar como...");
            int returnVal = fchMapa.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String n = fchMapa.getSelectedFile().getName();
                File posibleArchivoMapa;
                //Agregamos extension si no la tiene
                if(n.lastIndexOf(".") != -1 && n.substring(n.lastIndexOf(".")+1).equals(Utilidades.ConstantesGenerales.EXTENSION_MAPA))
                    posibleArchivoMapa = fchMapa.getSelectedFile();
                else
                    posibleArchivoMapa = new File(fchMapa.getSelectedFile().getAbsolutePath()
                            + "." + Utilidades.ConstantesGenerales.EXTENSION_MAPA);
                
                //Si existe, preguntamos si sobreescribirlo
                if(posibleArchivoMapa.exists())
                    if(JOptionPane.showConfirmDialog(null, "¿Sobreescribir mapa?", "Mapa ya existente", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
                        return;
                
                //Setteamos el archivoMapa y llamamos a guardarMapa()
                archivoMapa = posibleArchivoMapa;
                guardarMapa();
            }
        }else
            info("Ningún mapa abierto que guardar.");
    }
    
    /**
     * Cambia el texto de la barra inferior del editor
     * @param texto nuevo texto de la barra inferior del editor
     */
    public void info(String texto){
        lblInfo.setText(texto.replace("\n", " - "));
    }

    /**
     * Cambia el texto de la sección de coordenadas de la barra inferior del editor
     * @param pt punto de coordenadas a escribir, null para el texto por defecto.
     */
    public void infoCoordenadas(Punto pt){
        if(pt != null)
            lblCoordenadas.setText(String.format("Coordenadas de la celda: (%d, %d)", pt.x, pt.y));
        else
            lblCoordenadas.setText("Coordenadas de la celda");
    }
    
    /**
     * Elimina el panel del mapa en edición y lo genera por completo de nuevo.<br>
     * El array de celdas gráficas no se regenera pero sí se actualizan sus imágenes.
     */
    public void regenerarPanelMapa(){
        panMapa.remove(panMapaEnEdicion);
        panMapaEnEdicion = null;
        seleccionada = null;
        generarPanelMapa();
        panMapa.repaint();
    }

    /**
     * Genera el panel del mapa en edición (debe no existir previamente, llamar a cerrarMapa())
     */
    public void generarPanelMapa() {
        boolean crearCeldas = celdas.isEmpty();
        
        if(panMapaEnEdicion != null){
            //Que panMapaEnEdicion no sea null indica algún fallo de programación, pues debería estar cerrado el mapa siempre que se llame aquí
            JOptionPane.showMessageDialog(null, "Hay algún fallo de programación, el panel no es null o el array celdas no está vacío y debería serlo. Contacte con los autores, gracias.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }else{
            panMapaEnEdicion = new JPanel(new GridLayout(mapa.getAlto(), mapa.getAncho()));
            panMapaEnEdicion.setBackground(Color.white);
            panMapaEnEdicion.setSize(new Dimension(mapa.getAncho()*tam_celda, mapa.getAlto()*tam_celda));
            for(int i=0; i <mapa.getAlto();i++)
                for(int j=0; j<mapa.getAncho(); j++)
                {
                    
                    CeldaGrafica celda;
                    if(crearCeldas){
                        celda = new PanelCeldaGrafica(new Punto(j,i));
                        celda.getComponente().setBorder(BORDE_DEF);
                        celda.getComponente().addMouseListener(mouseListenerCeldas);
                    }else
                        celda = celdas.get(i*mapa.getAncho()+j);
                    
                    repintarCelda(celda);
                    if(crearCeldas)
                        celdas.add(celda);
                    panMapaEnEdicion.add(celda.getComponente());
                }
            panMapa.add(panMapaEnEdicion);
            panMapa.setPreferredSize(panMapaEnEdicion.getSize());
            sldZoom.setValue(tam_celda);
            sldZoom.setEnabled(true);
            panMapa.revalidate();
        }
    }
   
    /**
     * Cambia una celda transitable por una no transitable o viceversa. <br>
     * Los enemigos en ella se eliminan.
     * @param c celda a cambiar por su opuesta
     * @param pt punto en que se encuentra la celda
     * @return La nueva celda
     */
    public Celda toggleTransitable(Celda c, Punto pt){
        if(c == null) return null;
        if(c instanceof Transitable){
            for(Enemigo e: ((Transitable)c).getEnemigos())
                mapa.remEnemigo(e); //Evitar problemas de referencias
            mapa.setCelda(pt, (c = new NoTransitable()));
        }else
            mapa.setCelda(pt, (c = new Transitable()));
        return c;
    }

    /**
     * Cambia una celda transitable por una no transitable o viceversa. <br>
     * Los enemigos en ella se eliminan.
     * @param c celda a cambiar por su opuesta
     * @return La nueva celda
     */
    public Celda toggleTransitable(Celda c){
        if(c==null)
            return null;
        
        Punto pt = mapa.getPosDe(c);
        return toggleTransitable(c, pt);
    }

    /**
     * Cambia una celda transitable por una no transitable o viceversa. <br>
     * Los enemigos en ella se eliminan.
     * @param cg celda gráfica a la que está asociada la celda que se desea cambiar
     * @return la celda gráfica pasada, que ahora hace referencia a la nueva celda
     */
    public CeldaGrafica toggleTransitable(CeldaGrafica cg){
        Celda c = mapa.getCelda(cg.getId());
        Celda d = toggleTransitable(c);
        repintarCelda(cg);
        return cg;
    }
    
    /**
     * Lleva a la UI a repintar todos las celdas visibles.
     */
    public void repintarCeldas(){
        for(CeldaGrafica cg: celdas)
            repintarCelda(cg);
    }
    
    /**
     * Repinta la celda especificada.
     * @param c celda grafica que se desea repintar
     */
    public void repintarCelda(CeldaGrafica c){
        ImagenCelda img = imagenRepresentante(c);
        c.setImagen(img); //Fijamos la imagen
        c.getComponente().setCursor(null); //Fijamos el cursor
        if(mapa.getCelda(c.getId()) instanceof Transitable){
            Transitable transitable = (Transitable) mapa.getCelda(c.getId());
                if(transitable.getEnemigos().size() > 0)
                    c.getComponente().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR)); //Si tiene enemigos, pueden moverse
        }
        c.getComponente().repaint(); //Redibujamos
    }
        
    /**
     * Obtiene el nombre de imagen adecuado para la celda grafica indicada. <br>
     * Aquí se definen que imágenes se mostrarán en el editor en cada casilla
     * según la celda.
     * @param cg celda gráfica de la que se desea obtener representación
     * @return Nombre que indica la imagen que representa la celda. Aunque
     * podría cargarse directamente la imagen (se encuentra en img/ y tiene
     * extensión .png) es recomendable hacerlo mediante {@link #obtenerImagen},
     * pues esta función maneja la caché de imágenes para evitar duplicación
     * y demás.
     */
    public ImagenCelda imagenRepresentante(CeldaGrafica cg){
        Celda c = mapa.getCelda(cg.getId());
        String representacionF, representacionD = null;
        
        if(c == null)
            representacionF = "null";
        else
            representacionF = c.representacionGrafica(); //Obtiene la imagen
        if(c != null)
            if(mapa.getPosicionInicial().equals(mapa.getPosDe(c)))
                representacionD = "jugador";
            else if(c instanceof Transitable){
                Transitable transitable = (Transitable) c;
                if(transitable.getEnemigos().size() > 0)
                    representacionD = "enemigo";
            }  
        ImagenCelda ret =  new ImagenCelda(obtenerImagen(representacionF));
        if(representacionD != null)
            ret.setDelante(obtenerImagen(representacionD));
        return ret;
    }

    /**
     * Vacía la caché de imágenes que suple las imágenes de todas las celdas
     * representadas.
     */
    public void flushImagenes(){
        imagenes.clear();
    }

    /**
     * Maneja el caché de imágenes. Cargando una nueva imagen si la imagen
     * solicitada no se encuentra en la caché, o devolviendo la imagen de
     * caché si esta sí se encuentra.
     * @param representacion representación de la que obtener la imagen,
     * se corresponde con la ruta relativa al archivo de imagen desde 'img/'
     * y sin la extensión (que debe ser siempre .png).
     * @return La imagen solicitada.
     */
    public Image obtenerImagen(String representacion) {
        if(imagenes.get(representacion) == null){
            Image img;
            img = new ImageIcon("img/"+representacion+".png").getImage().getScaledInstance(tam_celda, tam_celda, Image.SCALE_SMOOTH);
            imagenes.put(representacion, img);
            return img;
        }else
            return imagenes.get(representacion);
    }

    /**
     * Elimina del mapa en edición al enemigo indicado. <br>
     * Repinta la casilla correspondiente por si era el último enemigo
     * de esa celda.
     * @param e enemigo a eliminar
     */
    public void eliminarEnemigo(Enemigo e) {
        if(mapa!=null&&e!=null){
            mapa.remEnemigo(e);
            CeldaGrafica cg = grafica(e.getPos());
            if(cg!=null)
                repintarCelda(cg);
        }
    }

    /**
     * Elimina el objeto del mapa en edición
     * @param o objeto que se desea eliminar
     */
    public void eliminarObjeto(Objeto o){
        if(o != null && mapa != null){
            for(Celda c: mapa.getCeldas())
                if(c instanceof Transitable){
                    Transitable transitable = (Transitable) c;
                    if(transitable.getObjeto(o.getNombre()) != null)
                        transitable.remObjeto(o);
                }
        }
    }
    
    /**
     * Mueve el enemigo guardado en {@code enemigoMovido} a la celda asociada
     * a la celda gráfica indicada. Después del uso, la herramienta se fija
     * de nuevo a {@code NORMAL}.<br>
     * Sólo tiene sentido con la herramienta {@code MOVER_ENEMIGO}.
     * @param celdaGrafica celda grafica asociada a la celda a la que se
     *        desea mover el enemigo.
     * @see Herramienta
     * @see #setEnemigoMovido
     * @see #getEnemigoMovido
     */
    public void moverEnemigo(CeldaGrafica celdaGrafica) {
        if(herramienta == Herramienta.MOVER_ENEMIGO && enemigoMovido != null){
            try{
                enemigoMovido.setPos(celdaGrafica.getId());
                repintarCelda(grafica(mapa.getPosDe(celdaOrigen)));
                repintarCelda(celdaGrafica);
            } catch (CeldaObjetivoNoValida ex) {
                JOptionPane.showMessageDialog(null, "Celda a mover el enemigo no válida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        celdaGrafica.getComponente().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        celdaGrafica.getComponente().setBorder(BORDE_DEF);
        enemigoMovido = null;
        celdaOrigen = null;
        herramienta = Herramienta.NORMAL;
    }

    /**
     * Mueve el objeto guardado en {@code objetoMovido} a la celda asociada
     * a la celda gráfica indicada. Después del uso, la herramienta se fija
     * de nuevo a {@code NORMAL}.<br>
     * Sólo tiene sentido con la herramienta {@code MOVER_OBJETO}.
     * @param celdaGrafica celda gráfica asociada a la celda a la que se desea
     *        mover el objeto.
     * @see Herramienta
     * @see #setObjetoMovido
     * @see #getObjetoMovido
     */
    public void moverObjeto(CeldaGrafica celdaGrafica) {
        if(herramienta == Herramienta.MOVER_OBJETO && objetoMovido != null){
            Celda c = mapa.getCelda(celdaGrafica.getId());
            if(c instanceof Transitable){
                Transitable transitable = (Transitable) c;
                transitable.addObjeto(objetoMovido);
                celdaOrigen.remObjeto(objetoMovido);
            }else{
                JOptionPane.showMessageDialog(null, "Celda a mover el objeto no válida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        celdaGrafica.getComponente().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        celdaGrafica.getComponente().setBorder(BORDE_DEF);
        objetoMovido = null;
        celdaOrigen = null;
        herramienta = Herramienta.NORMAL;
    }
    
    /**
     * Obtiene la celda gráfica asociada al punto indicado, es decir,
     * aquella cuyo id es el punto indicado. Si no existe tal celda en
     * este editor, devuelve null.
     * @param pos punto cuya celda gráfica asociada se desea buscar
     * @return La celda asociada al punto, o null si no existe tal celda
     */
    public CeldaGrafica grafica(Punto pos) {
        for(CeldaGrafica cg : celdas)
            if(cg.getId().equals(pos))
                return cg;
        return null;
    }

    /**
     * Abre el cuadro de edición de propiedades de la celda pasada como argumento
     * @param cg celda que se desea editar
     */
    public void editarCelda(CeldaGrafica cg) {
        if(mapa != null && cg != null){
            cg.getComponente().setBorder(BORDE_PROP);
            new PropiedadesCelda(mapa.getCelda(cg.getId()), this).setVisible(true);
            cg.getComponente().setBorder(BORDE_DEF);
            repintarCelda(cg);
        }else
            JOptionPane.showMessageDialog(null, "Ninguna celda seleccionada", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
