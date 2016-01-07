/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import Excepciones.CeldaObjetivoNoValida;
import Mapa.Celda;
import Mapa.ConstantesMapa;
import Utilidades.Punto;
import Mapa.Transitable;
import Objetos.*;
import Personajes.Enemigo;
import Personajes.Sectoid;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class PropiedadesCelda extends javax.swing.JDialog {
    private Celda celda;
    private final Editor editor;
    /**
     * Creates new form PropiedadesCelda
     */
    public PropiedadesCelda(Celda celda, Editor contexto) {
        this.celda = celda;
        editor = contexto;
        if(celda != null)
            initComponents();
        setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pmnNuevoObjeto = new javax.swing.JPopupMenu();
        mitArma = new javax.swing.JMenuItem();
        mitArmadura = new javax.swing.JMenuItem();
        mitBinoculares = new javax.swing.JMenuItem();
        mitBotiquin = new javax.swing.JMenuItem();
        mitExplosivo = new javax.swing.JMenuItem();
        mitToritoRojo = new javax.swing.JMenuItem();
        panPestanhas = new javax.swing.JTabbedPane();
        panGeneral = new javax.swing.JPanel();
        tbtTransitable = new javax.swing.JToggleButton();
        lblCoordenadas = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        spnTipo = new javax.swing.JSpinner();
        lblIconoCelda = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstEnemigos = new javax.swing.JList();
        btnEngadir = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        panObjetos = new javax.swing.JPanel();
        btnEngadirObjeto = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstObjetos = new javax.swing.JList();
        btnEliminarObj = new javax.swing.JButton();
        panAceptarCancelar = new javax.swing.JPanel();
        btnAceptar = new javax.swing.JButton();

        mitArma.setText("Arma");
        mitArma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoObjeto(evt);
            }
        });
        pmnNuevoObjeto.add(mitArma);

        mitArmadura.setText("Armadura");
        mitArmadura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoObjeto(evt);
            }
        });
        pmnNuevoObjeto.add(mitArmadura);

        mitBinoculares.setText("Binoculares");
        mitBinoculares.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoObjeto(evt);
            }
        });
        pmnNuevoObjeto.add(mitBinoculares);

        mitBotiquin.setText("Botiquín");
        mitBotiquin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoObjeto(evt);
            }
        });
        pmnNuevoObjeto.add(mitBotiquin);

        mitExplosivo.setText("Explosivo");
        mitExplosivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoObjeto(evt);
            }
        });
        pmnNuevoObjeto.add(mitExplosivo);

        mitToritoRojo.setText("ToritoRojo");
        mitToritoRojo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoObjeto(evt);
            }
        });
        pmnNuevoObjeto.add(mitToritoRojo);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Propiedades de la celda");
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/Menus/ico_map.png")).getImage());
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);

        tbtTransitable.setSelected(celda instanceof Transitable);
        tbtTransitable.setText(celda instanceof Transitable?"Transitable":"No transitable");
        tbtTransitable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbtTransitableActionPerformed(evt);
            }
        });

        lblCoordenadas.setText(String.format("Celda en (%d, %d)", celda.getMapa().getPosDe(celda).x, celda.getMapa().getPosDe(celda).y)
        );

        jLabel1.setText("Tipo:");

        spnTipo.setModel(new javax.swing.SpinnerNumberModel(celda.tipo, 0, celda instanceof Transitable?ConstantesMapa.CE_REPG_TRANS.length-1:ConstantesMapa.CE_REPG_NOTRANS.length-1, 1));
        spnTipo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnTipoStateChanged(evt);
            }
        });

        lblIconoCelda.setBackground(new java.awt.Color(0, 0, 0));
        lblIconoCelda.setIcon(new ImageIcon(new ImageIcon("img/"+celda.representacionGrafica()+".png").getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
        lblIconoCelda.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.white, java.awt.Color.darkGray, java.awt.Color.black));

        lstEnemigos.setBackground(celda instanceof Transitable?Color.white:Color.gray);
        lstEnemigos.setModel(new ModeloListaEnemigos(celda));
        lstEnemigos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstEnemigos.setToolTipText("Enemigos");
        lstEnemigos.setCellRenderer(new RenderizadorListaEnemigos());
        lstEnemigos.setEnabled(celda instanceof Transitable);
        lstEnemigos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstEnemigosMouseClicked(evt);
            }
        });
        lstEnemigos.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstEnemigosValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lstEnemigos);

        btnEngadir.setText("+");
        btnEngadir.setEnabled(celda instanceof Transitable);
        btnEngadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEngadirActionPerformed(evt);
            }
        });

        btnEliminar.setText("-");
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panGeneralLayout = new javax.swing.GroupLayout(panGeneral);
        panGeneral.setLayout(panGeneralLayout);
        panGeneralLayout.setHorizontalGroup(
            panGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panGeneralLayout.createSequentialGroup()
                .addGroup(panGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panGeneralLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnEngadir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminar))
                    .addGroup(panGeneralLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(panGeneralLayout.createSequentialGroup()
                                .addComponent(tbtTransitable, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblCoordenadas)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(spnTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblIconoCelda, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        panGeneralLayout.setVerticalGroup(
            panGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tbtTransitable)
                        .addComponent(lblCoordenadas))
                    .addComponent(lblIconoCelda, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(spnTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)))
                .addGap(39, 39, 39)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEliminar)
                    .addComponent(btnEngadir))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panPestanhas.addTab("General", panGeneral);

        panObjetos.setLayout(new java.awt.BorderLayout());

        btnEngadirObjeto.setText("+");
        btnEngadirObjeto.setEnabled(celda instanceof Transitable);
        btnEngadirObjeto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEngadirObjetoActionPerformed(evt);
            }
        });
        panObjetos.add(btnEngadirObjeto, java.awt.BorderLayout.PAGE_START);

        lstObjetos.setBackground(celda instanceof Transitable?Color.white:Color.gray);
        lstObjetos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstObjetos.setCellRenderer(new ObjetosRenderer());
        lstObjetos.setEnabled(celda instanceof Transitable);
        if( celda instanceof Transitable)
        lstObjetos.setModel(new ObjetosListModel((Transitable)celda));
        lstObjetos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstObjetosMouseClicked(evt);
            }
        });
        lstObjetos.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstObjetosValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(lstObjetos);

        panObjetos.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        btnEliminarObj.setText("-");
        btnEliminarObj.setEnabled(false);
        btnEliminarObj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarObjActionPerformed(evt);
            }
        });
        panObjetos.add(btnEliminarObj, java.awt.BorderLayout.PAGE_END);

        panPestanhas.addTab("Objetos", panObjetos);

        getContentPane().add(panPestanhas, java.awt.BorderLayout.CENTER);

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panAceptarCancelarLayout = new javax.swing.GroupLayout(panAceptarCancelar);
        panAceptarCancelar.setLayout(panAceptarCancelarLayout);
        panAceptarCancelarLayout.setHorizontalGroup(
            panAceptarCancelarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panAceptarCancelarLayout.createSequentialGroup()
                .addGap(0, 460, Short.MAX_VALUE)
                .addComponent(btnAceptar))
        );
        panAceptarCancelarLayout.setVerticalGroup(
            panAceptarCancelarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panAceptarCancelarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnAceptar))
        );

        getContentPane().add(panAceptarCancelar, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        dispose();
        this.setVisible(false);
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void tbtTransitableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbtTransitableActionPerformed
        JToggleButton btn = (JToggleButton) evt.getSource();
        btn.setText(btn.isSelected()?"Transitable":"No transitable");
        celda = editor.toggleTransitable(celda);
        spnTipo.setModel(new javax.swing.SpinnerNumberModel(0, 0, btn.isSelected()?ConstantesMapa.CE_REPG_TRANS.length-1:ConstantesMapa.CE_REPG_NOTRANS.length-1, 1));
        spnTipoStateChanged(new ChangeEvent(spnTipo));
        lstEnemigos.setEnabled(btn.isSelected());
        lstEnemigos.setBackground(btn.isSelected()?Color.white:Color.gray);
        btnEngadir.setEnabled(btn.isSelected());
    }//GEN-LAST:event_tbtTransitableActionPerformed

    private void spnTipoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnTipoStateChanged
        int i = ((SpinnerNumberModel)spnTipo.getModel()).getNumber().intValue();
        if(i<0) return;
        if(tbtTransitable.isSelected()){
            if(i >= ConstantesMapa.CE_REPG_TRANS.length) return;
            celda.tipo = i;
        }else{
            if(i >= ConstantesMapa.CE_REPG_NOTRANS.length) return;
            celda.tipo = i;
        }
        lblIconoCelda.setIcon(new ImageIcon(new ImageIcon("img/"+celda.representacionGrafica()+".png").getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
    }//GEN-LAST:event_spnTipoStateChanged

    private void btnEngadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEngadirActionPerformed
        Punto pos = editor.getMapa().getPosDe(celda);
        
        try {
            editor.getMapa().addEnemigo(new Sectoid(editor.obtenerNombreEnemigo("sectoid"),pos.toArray(),null));
        } catch (CeldaObjetivoNoValida ex) {
            JOptionPane.showMessageDialog(null,
                        "No se pudo generar el enemigo en esta celda.", "Errorcillo",
                        JOptionPane.ERROR_MESSAGE);
        }
        
        ((ModeloListaEnemigos)lstEnemigos.getModel()).actualizar();
    }//GEN-LAST:event_btnEngadirActionPerformed

    private void lstEnemigosValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstEnemigosValueChanged
        btnEliminar.setEnabled(!lstEnemigos.isSelectionEmpty());
        
    }//GEN-LAST:event_lstEnemigosValueChanged

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        System.out.println(((Enemigo)lstEnemigos.getSelectedValue()).toString());
        editor.eliminarEnemigo((Enemigo)lstEnemigos.getSelectedValue());
        ((ModeloListaEnemigos)lstEnemigos.getModel()).actualizar();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void lstEnemigosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstEnemigosMouseClicked
        if(evt.getClickCount() > 1 && !lstEnemigos.isSelectionEmpty())
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new PropiedadesEnemigo(editor, (Enemigo)lstEnemigos.getSelectedValue()).setVisible(true);
                    ((ModeloListaEnemigos)lstEnemigos.getModel()).actualizar();
                }
            });
    }//GEN-LAST:event_lstEnemigosMouseClicked

    private void btnEngadirObjetoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEngadirObjetoActionPerformed
        pmnNuevoObjeto.show((Component)evt.getSource(), ((JButton)evt.getSource()).getWidth()/2, ((JButton)evt.getSource()).getHeight()/2);
    }//GEN-LAST:event_btnEngadirObjetoActionPerformed

    private void lstObjetosValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstObjetosValueChanged
        btnEliminarObj.setEnabled(!lstObjetos.isSelectionEmpty());
    }//GEN-LAST:event_lstObjetosValueChanged

    private void lstObjetosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstObjetosMouseClicked
        if(evt.getClickCount() > 1 && !lstObjetos.isSelectionEmpty()){
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new PropiedadesObjeto(editor, (Objeto)lstObjetos.getSelectedValue()).setVisible(true);
                    ((ObjetosListModel)lstObjetos.getModel()).actualizar();
                }
            });
        }
    }//GEN-LAST:event_lstObjetosMouseClicked

    private void nuevoObjeto(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoObjeto
        if(celda instanceof Transitable){
            Objeto ob;
            String nombre = editor.obtenerNombreObjeto("objeto");
            
            switch(((JMenuItem)evt.getSource()).getLabel()){
                case "Arma":
                    ob = new Arma(10, nombre, "Es un arma", 10, 10, Arma.ARMA_UNA_MANO);
                    break;
                case "Armadura":
                    ob = new Armadura(nombre, "Es una armadura", 10, 10, 0, 0);
                    break;
                case "Binoculares":
                    ob = new Binoculares(nombre, 10, 2);
                    break;
                case "Botiquín":
                    ob = new Botiquin(nombre, 10, 2);
                    break;
                case "Explosivo":
                    ob = new Explosivo(10, nombre);
                    break;
                default:
                    ob = new ToritoRojo(nombre, 10, 10);
                    break;
            }
            ((Transitable)celda).addObjeto(ob);
            ((ObjetosListModel)lstObjetos.getModel()).actualizar();
        }
    }//GEN-LAST:event_nuevoObjeto

    private void btnEliminarObjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarObjActionPerformed
        if(!lstObjetos.isSelectionEmpty()){
            editor.eliminarObjeto((Objeto)lstObjetos.getSelectedValue());
        }
    }//GEN-LAST:event_btnEliminarObjActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEliminarObj;
    private javax.swing.JButton btnEngadir;
    private javax.swing.JButton btnEngadirObjeto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblCoordenadas;
    private javax.swing.JLabel lblIconoCelda;
    private javax.swing.JList lstEnemigos;
    private javax.swing.JList lstObjetos;
    private javax.swing.JMenuItem mitArma;
    private javax.swing.JMenuItem mitArmadura;
    private javax.swing.JMenuItem mitBinoculares;
    private javax.swing.JMenuItem mitBotiquin;
    private javax.swing.JMenuItem mitExplosivo;
    private javax.swing.JMenuItem mitToritoRojo;
    private javax.swing.JPanel panAceptarCancelar;
    private javax.swing.JPanel panGeneral;
    private javax.swing.JPanel panObjetos;
    private javax.swing.JTabbedPane panPestanhas;
    private javax.swing.JPopupMenu pmnNuevoObjeto;
    private javax.swing.JSpinner spnTipo;
    private javax.swing.JToggleButton tbtTransitable;
    // End of variables declaration//GEN-END:variables
}
class ModeloListaEnemigos extends AbstractListModel<Enemigo>{
    Transitable celda;
    public ModeloListaEnemigos(Celda c) {
        if(c instanceof Transitable)
            celda = (Transitable)c;
        else
            celda = null;
    }
    
    @Override
    public int getSize() {
        if(celda != null)
            return celda.getNumEnemigos();
        else
            return 0;
    }

    @Override
    public Enemigo getElementAt(int index) {
        if(celda != null)
            return celda.getEnemigo(index);
        else
            return null;
    }
    
    public void actualizar(){
        if(getSize() > 0)
            this.fireContentsChanged(this, 0, getSize()-1);
        else
            this.fireContentsChanged(this, 0, 0);
    }
}

class RenderizadorListaEnemigos extends JLabel implements ListCellRenderer<Enemigo> {
    @Override
    public Component getListCellRendererComponent(
      JList<? extends Enemigo> list,           // the list
      Enemigo enemigo,            // value to display
      int index,               // cell index
      boolean isSelected,      // is the cell selected
      boolean cellHasFocus)    // does the cell have focus
    {
        if(enemigo == null)
            setText("null");
        else{
            setText(
                    enemigo.getNombre() + "[ "
                    +(enemigo.tieneArmas()?"+"+enemigo.getEfectoArmas()+"A , ":"")
                    +(enemigo.getArmadura()!=null?"+"+enemigo.getArmadura().getDefensa()+"\u2666 , ":"")
                    +enemigo.getVida()+"\u2665 , "
                    +enemigo.getEnergia()+"E ] "
            );
        }
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
    }
}
class ObjetosListModel extends AbstractListModel<Objeto> implements ListModel<Objeto> {
    Transitable celda;

    public ObjetosListModel(Transitable transitable) {
        celda = transitable;
    }

    @Override
    public int getSize() {
        return celda.getObjetos().size();
    }

    @Override
    public Objeto getElementAt(int index) {
        return celda.getObjetos().get(index);
    }
    
    public void actualizar(){
        if(getSize() > 0)
            this.fireContentsChanged(this, 0, getSize()-1);
        else
            this.fireContentsChanged(this, 0, 0);
    }
}