/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import Objetos.Objeto;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class ObjetosRenderer extends JLabel implements ListCellRenderer<Objeto> {
        @Override
        public Component getListCellRendererComponent(
                JList<? extends Objeto> list,
                Objeto value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
            if(value == null)
                setText("null");
            else{
                setText(value.getNombre() + "[" + PropiedadesObjeto.tipoObjeto(value) + ", peso " + value.getPeso() + "Kg]");
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