package Editor;

import Objetos.Objeto;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Renderer que genera los elementos visuales de la lista de objetos de
 * {@link PropiedadesCelda} y {@link PropiedadesEnemigo}.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
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