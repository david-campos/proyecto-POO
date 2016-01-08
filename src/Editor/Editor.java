/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RowFilter.Entry;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class Editor extends javax.swing.JFrame {
    public static final Border BORDE_DEF = new LineBorder(Color.white);
    public static final Border BORDE_HOVER = new LineBorder(Color.blue);
    public static final Border BORDE_MOVER = new LineBorder(Color.green);
    public static final Border BORDE_SELEC = new LineBorder(Color.black);
    public static final Border BORDE_PROP = new LineBorder(Color.orange);
    public static final Border BORDE_NOMOVER = new LineBorder(Color.red);

    public String obtenerNombreObjeto(String objeto) {
        int i = 0;
        boolean valido;
        
        objeto = objeto.replace(" ", "_");
        
        do{
            valido = true;
            for(Celda c: mapa.getCeldas()){
                String intento = objeto + (i>0?"_"+i:"");
                if(c instanceof Transitable)
                    if(((Transitable)c).getObjeto(intento) != null){
                        i++;
                        valido = false;
                        break;
                    }
            }
        }while(!valido);
        
        do{
            valido = true;
            for(Enemigo e: mapa.getEnemigos()){
                String intento = objeto + (i>0?"_"+i:"");
                if( e.getMochila().getObjeto(intento) != null
                    || (e.getArma() != null
                        && e.getArma().getNombre().equals(intento))
                    || (e.getArma_izq() != null
                        && e.getArma_izq().getNombre().equals(intento))
                    || (e.getArmadura() != null
                        && e.getArmadura().getNombre().equals(intento))
                    ){
                        i++;
                        valido = false;
                        break;
                    }
            }
        }while(!valido);
        
        return objeto + (i>0?"_"+i:"");
    }

    public String obtenerNombreEnemigo(String nombreBase) {
        String nombre;
        int veces = 0;
        nombre = nombreBase.replace(" ", "_");
        boolean exito;
        do{
            exito = true;
            for(Enemigo e: mapa.getEnemigos()){
                if(e.getNombre().equals(nombre)){
                    nombre = String.format("%s_%d", nombreBase, ++veces);
                    exito = false;
                    break;
                }
            }
        }while(!exito);
        
        return nombre;
    }

    public static enum Herramienta{
        NORMAL,
        MOVER_ENEMIGO,
        MOVER_OBJETO
    }
    
    private final MouseListener mouseListenerCeldas = new CeldasML(this);

    public Mapa getMapa() {
        return mapa;
    }

    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
    }

    public CeldaGrafica getSeleccionada() {
        return seleccionada;
    }

    public void toggleSeleccionada(CeldaGrafica cg){
        if(cg != null && cg.equals(seleccionada))
            setSeleccionada(null);
        else
            setSeleccionada(cg);
    }
    public void setSeleccionada(CeldaGrafica seleccionada) {
        if(this.seleccionada != null)
            this.seleccionada.getComponente().setBorder(BORDE_DEF);
        
        this.seleccionada = seleccionada;
        
        if(seleccionada != null)
            seleccionada.getComponente().setBorder(BORDE_SELEC);
    }

    public ArrayList<CeldaGrafica> getCeldas() {
        return celdas;
    }
    
    private Mapa mapa;
    private File archivoMapa;
    private JPanel panMapaEnEdicion;
    private CeldaGrafica seleccionada;
    private final ArrayList<CeldaGrafica> celdas;
    private final HashMap<String, Image> imagenes;
    
    private int tam_celda;
    private Herramienta herramienta;
    private Objeto objetoMovido; //Objeto movido (herramienta mover objeto)
    private Transitable celdaOrigen; //Origen del objeto (herramienta mover objeto)
    private Enemigo enemigoMovido; //Enemigo movido (herramienta mover enemigo)
    /**
     * Creates new form Editor
     */
    public Editor() {
        mapa = null                     ;   archivoMapa = null;
        seleccionada = null             ;   tam_celda = 40;
        objetoMovido = null             ;   enemigoMovido = null;
        imagenes = new HashMap()        ;   celdas = new ArrayList();
        herramienta = Herramienta.NORMAL;
        
        initComponents();
    }

    public Transitable getCeldaOrigen() {
        return celdaOrigen;
    }

    public void setCeldaOrigen(Transitable celdaOrigen) {
        this.celdaOrigen = celdaOrigen;
    }

    public Objeto getObjetoMovido() {
        return objetoMovido;
    }

    public void setObjetoMovido(Objeto objetoMovido) {
        this.objetoMovido = objetoMovido;
    }

    public Enemigo getEnemigoMovido() {
        return enemigoMovido;
    }

    public void setEnemigoMovido(Enemigo enemigoMovido) {
        this.enemigoMovido = enemigoMovido;
    }

    public Herramienta getHerramienta() {
        return herramienta;
    }

    public void setHerramienta(Herramienta herramienta) {
        this.herramienta = herramienta;
    }

    public File getArchivoMapa() {
        return archivoMapa;
    }

    public void setArchivoMapa(File carpetaMapa) {
        this.archivoMapa = carpetaMapa;
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
        mitEditarMapa = new javax.swing.JMenuItem();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Editor de mapas");
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/Menus/ico_map.png")).getImage());
        setMinimumSize(new java.awt.Dimension(440, 417));
        setName("editor"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
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

        mitEditarMapa.setText("Mapa...");
        mitEditarMapa.setToolTipText("");
        jMenu2.add(mitEditarMapa);

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

    private void mitNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNuevoActionPerformed
        nuevoMapa();
    }//GEN-LAST:event_mitNuevoActionPerformed

    private void mitAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAbrirActionPerformed
        abrirMapa();
    }//GEN-LAST:event_mitAbrirActionPerformed

    private void mitGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitGuardarActionPerformed
        guardarMapa();
    }//GEN-LAST:event_mitGuardarActionPerformed

    //Diálogo de creación de mapa
    private void dlgNuevoMapa_btnAceptarClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlgNuevoMapa_btnAceptarClick
        //Nombres y descripción válidos
        if(!txtNombreMapa.getText().isEmpty()){
            if(!txtDescripcionMapa.getText().isEmpty()){
                dlgNuevoMapa.setVisible(false);
                cerrarMapa();
                archivoMapa = null;
                if(cbxAleatorio.isSelected())
                {
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
                generarPanelMapa();
                info("Creado mapa '"+mapa.getNombre()+"'");
            }else
                txtDescripcionMapa.setBackground(new Color(255, 200, 200));
        }else
            txtNombreMapa.setBackground(new Color(255, 200, 200));
            
    }//GEN-LAST:event_dlgNuevoMapa_btnAceptarClick
    private void dlgNuevoMapa_btnCancelarClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlgNuevoMapa_btnCancelarClick
        dlgNuevoMapa.setVisible(false);
        info("Cancelado nuevo mapa.");
    }//GEN-LAST:event_dlgNuevoMapa_btnCancelarClick

    private void fijarFondoBlanco(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fijarFondoBlanco
        ((Component)evt.getSource()).setBackground(Color.white);
    }//GEN-LAST:event_fijarFondoBlanco

    private void mitGuardarComoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitGuardarComoActionPerformed
        guardarMapaComo();
    }//GEN-LAST:event_mitGuardarComoActionPerformed

    private void mitEditarCeldaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditarCeldaActionPerformed
        editarCelda(seleccionada);
    }//GEN-LAST:event_mitEditarCeldaActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        cerrarMapa();
        System.exit(0);
    }//GEN-LAST:event_formWindowClosing

    private void sldZoomStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldZoomStateChanged
       tam_celda = sldZoom.getValue();
       flushImagenes();
       regenerarPanelMapa();
    }//GEN-LAST:event_sldZoomStateChanged

    private void mitCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCerrarActionPerformed
        cerrar();
    }//GEN-LAST:event_mitCerrarActionPerformed

    private void mirCerrarMapaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mirCerrarMapaActionPerformed
        cerrarMapa();
    }//GEN-LAST:event_mirCerrarMapaActionPerformed
    private void cerrar(){
        dispose();
        new MenuGrafico().lanzar();
    }
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
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JLabel lblCoordenadas;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JMenuBar mbrSuperior;
    private javax.swing.JMenuItem mirCerrarMapa;
    private javax.swing.JMenuItem mitAbrir;
    private javax.swing.JMenuItem mitCerrar;
    private javax.swing.JMenuItem mitEditarCelda;
    private javax.swing.JMenuItem mitEditarMapa;
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
    public void nuevoMapa() {
        fchMapa.setCurrentDirectory(null);
        txtNombreMapa.setText("Mapa");
        txtDescripcionMapa.setText("Descripcion...");
        dlgNuevoMapa.setVisible(true);
    }

    private void cerrarMapa(){
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
        //Debe dejar panMapaEnEdicion a null y el array de celdas vacío, poner mapa a null..., ficheroMapa a null..., seleccionada a null
    }
    
    private void abrirMapa() {
        fchMapa.setDialogTitle("Abrir mapa...");
        int returnVal = fchMapa.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String n = fchMapa.getSelectedFile().getName();
            if(n.lastIndexOf(".") != -1 && n.substring(n.lastIndexOf(".")+1).equals(Utilidades.ConstantesGenerales.EXTENSION_MAPA)){
                cerrarMapa();
                try {
                    BufferedReader lector = new BufferedReader(
                            new FileReader(fchMapa.getSelectedFile())
                    );
                    mapa = new GsonBuilder()
                            .registerTypeAdapter(Celda.class, new Adaptador<Celda>())
                            .registerTypeAdapter(Objeto.class, new Adaptador<Objeto>())
                            .registerTypeAdapter(Enemigo.class, new Adaptador<Enemigo>())
                            .create().fromJson(lector, Mapa.class);
                    for(Celda c: mapa.getCeldas())
                        c.setMapa(mapa);
                    for(Enemigo e: mapa.getEnemigos()){
                        try {
                            e.setMapa(mapa);
                        } catch (CeldaObjetivoNoValida ex) {
                           //No debería ser no válida, fue guardado ahí.
                        }

                    }
                    archivoMapa = fchMapa.getSelectedFile();
                    generarPanelMapa();
                    info("Mapa abierto: " + fchMapa.getSelectedFile().getAbsolutePath());
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }else
                JOptionPane.showMessageDialog(null, "Archivo no válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void guardarMapa(){
        if(mapa!=null){
            if(archivoMapa != null)
            {
                File copiaSeguridad = new File(archivoMapa.getName() + ".saving");
                if(archivoMapa.exists())
                    archivoMapa.renameTo(copiaSeguridad);
                try {
                    BufferedWriter escritor = new BufferedWriter(new FileWriter(archivoMapa));
                    new GsonBuilder()
                        .addSerializationExclusionStrategy(new EstrategiaGuardado())
                        .registerTypeAdapter(Celda.class, new Adaptador<Celda>())
                        .registerTypeAdapter(Objeto.class, new Adaptador<Objeto>())
                        .registerTypeAdapter(Enemigo.class, new Adaptador<Enemigo>())
                        .create().toJson(mapa, escritor);
                    escritor.close();

                    if(copiaSeguridad.exists())
                        copiaSeguridad.delete();

                    info("Mapa guardado: " + fchMapa.getSelectedFile().getAbsolutePath());
                } catch (IOException ex) {
                    System.out.println(ex);
                    copiaSeguridad.renameTo(archivoMapa);
                }
            }else
                guardarMapaComo();
        }else
            info("Ningún mapa abierto para guardar.");
    }
    
    private void guardarMapaComo() {
        if(mapa != null){
            fchMapa.setDialogTitle("Guardar como...");
            int returnVal = fchMapa.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String n = fchMapa.getSelectedFile().getName();
                File posibleArchivoMapa;
                if(n.lastIndexOf(".") != -1 && n.substring(n.lastIndexOf(".")+1).equals(Utilidades.ConstantesGenerales.EXTENSION_MAPA)){
                    posibleArchivoMapa = fchMapa.getSelectedFile();
                }else{
                    posibleArchivoMapa = new File(fchMapa.getSelectedFile().getAbsolutePath() + "." + Utilidades.ConstantesGenerales.EXTENSION_MAPA);
                }
                if(posibleArchivoMapa.exists())
                    if(JOptionPane.showConfirmDialog(null, "¿Sobreescribir mapa?", "Mapa ya existente", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
                        return;

                archivoMapa = posibleArchivoMapa;
                guardarMapa();
            }
        }else
            info("Ningún mapa abierto que guardar.");
    }
    
    public void info(String texto){
        lblInfo.setText(texto.replace("\n", " - "));
    }

    public void infoCoordenadas(Punto pt){
        if(pt != null)
            lblCoordenadas.setText(String.format("Coordenadas de la celda: (%d, %d)", pt.x, pt.y));
        else
            lblCoordenadas.setText("Coordenadas de la celda");
    }
    
    private void regenerarPanelMapa(){
        panMapa.remove(panMapaEnEdicion);
        panMapaEnEdicion = null;
        seleccionada = null;
        generarPanelMapa();
        panMapa.repaint();
    }
    private void generarPanelMapa() {
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
    public Celda toggleTransitable(Celda c){
        if(c==null)
            return null;
        
        Punto pt = mapa.getPosDe(c);
        if(c instanceof Transitable){
            for(Enemigo e: ((Transitable)c).getEnemigos())
                mapa.remEnemigo(e);
            mapa.setCelda(pt, (c = new NoTransitable()));
        }else
            mapa.setCelda(pt, (c = new Transitable()));
        return c;
    }
    public void toggleTransitable(CeldaGrafica cg){
        Celda c = mapa.getCelda(cg.getId());
        toggleTransitable(c);
        repintarCelda(cg);
    }
    
    public void repintarCeldas(){
        for(CeldaGrafica cg: celdas)
            repintarCelda(cg);
    }
    
    public void repintarCelda(CeldaGrafica c){
        ImagenCelda img = imagenRepresentante(c);
        c.setImagen(img);
        c.getComponente().setCursor(null);
        if(mapa.getCelda(c.getId()) instanceof Transitable){
            Transitable transitable = (Transitable) mapa.getCelda(c.getId());
                if(transitable.getEnemigos().size() > 0)
                    c.getComponente().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }
        c.getComponente().repaint();
    }
        
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

    public void flushImagenes(){
        imagenes.clear();
    }
    public Image obtenerImagen(String representacion) {
        if(imagenes.get(representacion) == null){
            Image img;
            img = new ImageIcon("img/"+representacion+".png").getImage().getScaledInstance(tam_celda, tam_celda, Image.SCALE_SMOOTH);
            imagenes.put(representacion, img);
            return img;
        }else
            return imagenes.get(representacion);
    }

    public void eliminarEnemigo(Enemigo e) {
        CeldaGrafica cg = grafica(e.getPos());
        mapa.remEnemigo(e);
        if(cg!=null)
            repintarCelda(cg);
    }
    public void eliminarObjeto(Objeto o){
        for(Celda c: mapa.getCeldas())
            if(c instanceof Transitable){
                Transitable transitable = (Transitable) c;
                if(transitable.getObjeto(o.getNombre()) != null)
                    transitable.remObjeto(o);
            }
    }
    void moverEnemigo(CeldaGrafica celdaGrafica) {
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
    void moverObjeto(CeldaGrafica celdaGrafica) {
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
    
    public CeldaGrafica grafica(Punto pos) {
        for(CeldaGrafica cg : celdas)
            if(cg.getId().equals(pos))
                return cg;
        return null;
    }

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
