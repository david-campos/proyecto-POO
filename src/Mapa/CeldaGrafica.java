/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mapa;

import java.awt.Image;
import javax.swing.JComponent;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public interface CeldaGrafica{
    public void setImagen(ImagenCelda imagen);
    public void setDelante(Image imagen);
    public JComponent getComponente();
    public void setId(Punto pt);
    public Punto getId();
}
