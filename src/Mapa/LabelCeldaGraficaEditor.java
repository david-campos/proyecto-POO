/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mapa;

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class LabelCeldaGraficaEditor extends JLabel implements CeldaGrafica{

    @Override
    public void setImagen(Image imagen) {
        setIcon(new ImageIcon(imagen));
    }

    @Override
    public Component getComponente() {
        return this;
    }
    
}
