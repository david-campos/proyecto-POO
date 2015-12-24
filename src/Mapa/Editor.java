/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mapa;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class Editor extends javax.swing.JFrame {
    private static final Border borde1 = new LineBorder(Color.white);
    private static final Border borde2 = new LineBorder(Color.black);
    
    private class celdasML extends MouseAdapter{
                        private int botonPulsado = 0;
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            super.mouseReleased(e);
                            botonPulsado = MouseEvent.NOBUTTON;
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                            botonPulsado = e.getButton();
                            if(e.getButton() == MouseEvent.BUTTON1){
                                Celda c=null;
                                CeldaGrafica celdaGrafica = null;
                                for(CeldaGrafica cg : celdas)
                                    if(cg.getComponente().equals(e.getComponent())){
                                        c = Editor.this.mapa.getCelda(cg.getId());
                                        celdaGrafica = cg;
                                        break;
                                    }
                                if(c==null)
                                    return;

                                if(c instanceof Transitable)
                                    c.tipo=(c.tipo+1)%ConstantesMapa.CE_REPR_TRANS.length;
                                else
                                    c.tipo=(c.tipo+1)%ConstantesMapa.CE_REPR_NOTRANS.length;
                                Editor.this.repintarCelda(celdaGrafica);
                            }else if(e.getButton() == MouseEvent.BUTTON3){
                                Celda c=null;
                                CeldaGrafica celdaGrafica = null;
                                for(CeldaGrafica cg : celdas)
                                    if(cg.getComponente().equals(e.getComponent())){
                                        c = Editor.this.mapa.getCelda(cg.getId());
                                        celdaGrafica = cg;
                                        break;
                                    }
                                if(c==null)
                                    return;

                                if(c instanceof Transitable)
                                    mapa.setCelda(celdaGrafica.getId(), new NoTransitable());
                                else
                                    mapa.setCelda(celdaGrafica.getId(), new Transitable());
                                
                                Editor.this.repintarCelda(celdaGrafica);
                            }
                        }
                        
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            ((JComponent)e.getComponent()).setBorder(borde2);
                            
                            if(botonPulsado == MouseEvent.BUTTON1){
                                Celda c=null;
                                CeldaGrafica celdaGrafica = null;
                                for(CeldaGrafica cg : celdas)
                                    if(cg.getComponente().equals(e.getComponent())){
                                        c = Editor.this.mapa.getCelda(cg.getId());
                                        celdaGrafica = cg;
                                        break;
                                    }
                                if(c==null)
                                    return;

                                if(c instanceof Transitable)
                                    c.tipo=(c.tipo+1)%ConstantesMapa.CE_REPR_TRANS.length;
                                else
                                    c.tipo=(c.tipo+1)%ConstantesMapa.CE_REPR_NOTRANS.length;
                                Editor.this.repintarCelda(celdaGrafica);
                            }else if(botonPulsado == MouseEvent.BUTTON3){
                                Celda c=null;
                                CeldaGrafica celdaGrafica = null;
                                for(CeldaGrafica cg : celdas)
                                    if(cg.getComponente().equals(e.getComponent())){
                                        c = Editor.this.mapa.getCelda(cg.getId());
                                        celdaGrafica = cg;
                                        break;
                                    }
                                if(c==null)
                                    return;

                                if(c instanceof Transitable)
                                    mapa.setCelda(celdaGrafica.getId(), new NoTransitable());
                                else
                                    mapa.setCelda(celdaGrafica.getId(), new Transitable());
                                
                                Editor.this.repintarCelda(celdaGrafica);
                            }
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            ((JComponent)e.getComponent()).setBorder(borde1);
                        }
    }
    private final MouseListener mouseListenerCeldas = new celdasML();
    
    private Mapa mapa;
    private File carpetaMapa;
    private JPanel panMapaEnEdicion;
    private final ArrayList<CeldaGrafica> celdas;
    private final HashMap<String, Image> imagenes;
    
    private static final int TAM_CELDA = 40;
    
    /**
     * Creates new form Editor
     */
    public Editor() {
        mapa = null;
        carpetaMapa = null;
        imagenes = new HashMap();
        celdas = new ArrayList();
        initComponents();
    }

    public File getCarpetaMapa() {
        return carpetaMapa;
    }

    public void setCarpetaMapa(File carpetaMapa) {
        this.carpetaMapa = carpetaMapa;
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
        panNombreJugador = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNombreJugador = new javax.swing.JTextField();
        panDimensiones = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        spnAncho = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        spnAlto = new javax.swing.JSpinner();
        panAcceptCancel = new javax.swing.JPanel();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        panMapa = new javax.swing.JPanel();
        tbrInferior = new javax.swing.JToolBar();
        lblInfo = new javax.swing.JLabel();
        mbrSuperior = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mitNuevo = new javax.swing.JMenuItem();
        mitAbrir = new javax.swing.JMenuItem();
        mitGuardar = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mitEditarMapa = new javax.swing.JMenuItem();
        mitEditarCelda = new javax.swing.JMenuItem();

        fchMapa.setAcceptAllFileFilterUsed(false);
        fchMapa.setDialogTitle("Abrir mapa");
        fchMapa.setFileFilter(new Utilidades.FiltroMapa());
        fchMapa.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        fchMapa.setFileView(new Utilidades.FileViewMapa());
        fchMapa.setToolTipText("");

        dlgNuevoMapa.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        dlgNuevoMapa.setTitle("Nuevo mapa");
        dlgNuevoMapa.setAlwaysOnTop(true);
        dlgNuevoMapa.setMinimumSize(new java.awt.Dimension(434, 230));
        dlgNuevoMapa.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dlgNuevoMapa.setResizable(false);
        dlgNuevoMapa.setSize(new java.awt.Dimension(434, 250));

        panGeneral.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panNombreMapa.setLayout(new java.awt.BorderLayout(5, 0));

        jLabel1.setText("Nombre del mapa:");
        panNombreMapa.add(jLabel1, java.awt.BorderLayout.LINE_START);

        txtNombreMapa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fijarFondoBlanco(evt);
            }
        });
        panNombreMapa.add(txtNombreMapa, java.awt.BorderLayout.CENTER);

        panGeneral.add(panNombreMapa, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 410, 20));

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

        panDescripcion.add(jScrollPane1, java.awt.BorderLayout.PAGE_END);

        panGeneral.add(panDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 410, 60));

        panNombreJugador.setLayout(new java.awt.BorderLayout(5, 0));

        jLabel2.setText("Nombre del jugador:");
        panNombreJugador.add(jLabel2, java.awt.BorderLayout.LINE_START);

        txtNombreJugador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fijarFondoBlanco(evt);
            }
        });
        panNombreJugador.add(txtNombreJugador, java.awt.BorderLayout.CENTER);

        panGeneral.add(panNombreJugador, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 410, -1));

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

        panGeneral.add(panDimensiones, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 130, 400, -1));

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
                .addContainerGap(243, Short.MAX_VALUE)
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

        panGeneral.add(panAcceptCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, -1, -1));

        javax.swing.GroupLayout dlgNuevoMapaLayout = new javax.swing.GroupLayout(dlgNuevoMapa.getContentPane());
        dlgNuevoMapa.getContentPane().setLayout(dlgNuevoMapaLayout);
        dlgNuevoMapaLayout.setHorizontalGroup(
            dlgNuevoMapaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgNuevoMapaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dlgNuevoMapaLayout.setVerticalGroup(
            dlgNuevoMapaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgNuevoMapaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Editor de mapas");
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/Menus/ico_map.png")).getImage());
        setName("editor"); // NOI18N

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

        getContentPane().add(tbrInferior, java.awt.BorderLayout.SOUTH);

        jMenu1.setText("Archivo");

        mitNuevo.setText("Nuevo mapa...");
        mitNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNuevoActionPerformed(evt);
            }
        });
        jMenu1.add(mitNuevo);

        mitAbrir.setText("Abrir mapa...");
        mitAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAbrirActionPerformed(evt);
            }
        });
        jMenu1.add(mitAbrir);

        mitGuardar.setText("Guardar mapa...");
        mitGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitGuardarActionPerformed(evt);
            }
        });
        jMenu1.add(mitGuardar);

        mbrSuperior.add(jMenu1);

        jMenu2.setText("Editar");

        mitEditarMapa.setText("Mapa...");
        mitEditarMapa.setToolTipText("");
        jMenu2.add(mitEditarMapa);

        mitEditarCelda.setText("Celda...");
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
                if(!txtNombreJugador.getText().isEmpty()){
                    File nuevoMapa = new File(fchMapa.getSelectedFile(), txtNombreMapa.getText());
                    if(!nuevoMapa.exists()){
                        cerrarMapa();
                        carpetaMapa = nuevoMapa;
                        mapa = new Mapa(txtNombreMapa.getText(),txtDescripcionMapa.getText(),(int)spnAncho.getValue(),(int)spnAlto.getValue(),null);
                        generarPanelMapa();
                        JOptionPane.showMessageDialog(null,
                                String.format("Creado mapa en '%s', recuerde pulsar 'Guardar mapa' para guardar.", carpetaMapa.getAbsolutePath()));
                        info("Creado mapa '"+mapa.getNombre()+"'");
                    }else
                        info("El nombre de mapa ya existe!");
                    dlgNuevoMapa.setVisible(false);
                }else
                    txtNombreJugador.setBackground(new Color(255, 200, 200));
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
    private javax.swing.JDialog dlgNuevoMapa;
    private javax.swing.JFileChooser fchMapa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JMenuBar mbrSuperior;
    private javax.swing.JMenuItem mitAbrir;
    private javax.swing.JMenuItem mitEditarCelda;
    private javax.swing.JMenuItem mitEditarMapa;
    private javax.swing.JMenuItem mitGuardar;
    private javax.swing.JMenuItem mitNuevo;
    private javax.swing.JPanel panAcceptCancel;
    private javax.swing.JPanel panDescripcion;
    private javax.swing.JPanel panDimensiones;
    private javax.swing.JPanel panGeneral;
    private javax.swing.JPanel panMapa;
    private javax.swing.JPanel panNombreJugador;
    private javax.swing.JPanel panNombreMapa;
    private javax.swing.JSpinner spnAlto;
    private javax.swing.JSpinner spnAncho;
    private javax.swing.JToolBar tbrInferior;
    private javax.swing.JTextArea txtDescripcionMapa;
    private javax.swing.JTextField txtNombreJugador;
    private javax.swing.JTextField txtNombreMapa;
    // End of variables declaration//GEN-END:variables

    
    //Métodos de funcionamiento
    private void nuevoMapa() {
        fchMapa.setCurrentDirectory(null);
        int returnVal = fchMapa.showDialog(this, "Crear aquí");
        if (returnVal == JFileChooser.APPROVE_OPTION && fchMapa.getSelectedFile().isDirectory()) {
            mapa = null;
            txtNombreJugador.setText("Nombre");
            txtNombreMapa.setText("Mapa");
            txtDescripcionMapa.setText("Descripcion...");
            dlgNuevoMapa.setVisible(true);
        }else
            info("Directorio inválido o creación cancelada.");
    }

    private void cerrarMapa(){
        JOptionPane.showMessageDialog(null, "Ahora debería mostrarse lo de guardar mapa si hay un mapa abierto y tal.");
        //Debe dejar panMapaEnEdicion a null
    }
    
    private void abrirMapa() {
        int returnVal = fchMapa.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            lblInfo.setText("ABRIMOS UN MAPA :D \n" + fchMapa.getSelectedFile().getAbsolutePath());
        }
    }

    private void guardarMapa() {
        int returnVal = fchMapa.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            lblInfo.setText("GUARDAMOS UN MAPA :D \n" + fchMapa.getSelectedFile().getAbsolutePath());
        }
    }
    
    private void info(String texto){
        lblInfo.setText(texto.replace("\n", " - "));
    }

    private void generarPanelMapa() {
        if(panMapaEnEdicion != null || !celdas.isEmpty()){
            //Que panMapaEnEdicion no sea null indica algún fallo de programación, pues debería estar cerrado el mapa siempre que se llame aquí
            JOptionPane.showMessageDialog(null, "Hay algún fallo de programación, el panel no es null o el array celdas no está vacío y debería serlo. Contacte con los autores, gracias.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }else{
            panMapaEnEdicion = new JPanel(new GridLayout(mapa.getAlto(), mapa.getAncho()));
            panMapaEnEdicion.setBackground(Color.white);
            panMapaEnEdicion.setSize(new Dimension(mapa.getAncho()*TAM_CELDA, mapa.getAlto()*TAM_CELDA));
            for(int i=0; i <mapa.getAlto();i++)
                for(int j=0; j<mapa.getAncho(); j++)
                {
                    
                    CeldaGrafica celda = new LabelCeldaGraficaEditor(new Punto(j,i));
                    celda.getComponente().setBorder(borde1);
                    celda.getComponente().addMouseListener(mouseListenerCeldas);
                    Image img = imagenRepresentante(celda);
                    celda.setImagen(img);
                    celdas.add(celda);
                    panMapaEnEdicion.add(celda.getComponente());
                }
            panMapa.add(panMapaEnEdicion);
            panMapa.setPreferredSize(panMapaEnEdicion.getSize());
            panMapa.revalidate();
        }
    }
    
    private void repintarCeldas(){
        for(CeldaGrafica cg: celdas)
            repintarCelda(cg);
    }
    
    private void repintarCelda(CeldaGrafica c){
        Image img = imagenRepresentante(c);
        c.setImagen(img);
        c.getComponente().repaint();
    }
        
    private Image imagenRepresentante(CeldaGrafica cg){
        Celda c = mapa.getCelda(cg.getId());
        String representacion;
        if(c == null)
            representacion = "null";
        else if(mapa.getPosicionInicial().equals(mapa.getPosDe(c)))
            representacion = "jugador";
        else if(c instanceof Transitable){
            Transitable transitable = (Transitable) c;
            if(transitable.getEnemigos().size() > 0)
                representacion = "enemigo";
            else
                representacion = c.representacionGrafica(); //Obtiene la imagen
        }else
            representacion = c.representacionGrafica(); //Obtiene la imagen
        Image img;
        if(imagenes.get(representacion) == null){
            img = new ImageIcon("img/"+representacion+".png").getImage().getScaledInstance(TAM_CELDA, TAM_CELDA, Image.SCALE_FAST);
            imagenes.put(representacion, img);
        }else
            img = imagenes.get(representacion);
        return img;  
    }
}
