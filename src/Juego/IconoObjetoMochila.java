package Juego;

import Objetos.Objeto;
import java.awt.Color;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

/**
 * Clase creada para el elemento que representará un objeto cuando se guarde en
 * la mochila en la consola gráfica.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class IconoObjetoMochila extends JLabel{

    /**
     * Crea un nuevo icono de representación de objeto
     * @param ob objeto a representar
     */
    public IconoObjetoMochila(Objeto ob){
        super();
        this.setIcon(icono(ob));
        this.setBorder(new LineBorder(Color.white));
        this.setToolTipText("<html>"+ob.toString().replace("\n","<br>")+"</html>");
    }

    private Icon icono(Objeto ob) {
        String n = ob.getClass().getSimpleName().toLowerCase();
        if(new File("img/objetos/"+n+".png").exists()){
            return new ImageIcon("img/objetos/"+n+".png");
        }else
            return new ImageIcon("img/objetos/desconocido.png");
    }
}
