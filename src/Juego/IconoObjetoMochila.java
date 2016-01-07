/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Juego;

import Objetos.Objeto;
import java.awt.Color;
import java.awt.Cursor;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class IconoObjetoMochila extends JLabel{
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
