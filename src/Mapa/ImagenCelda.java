/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mapa;

import java.awt.Image;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class ImagenCelda {
    private Image fondo;
    private Image delante;
    
    public ImagenCelda(Image imagen) {
        fondo = imagen;
        delante = null;
    }

    public Image getFondo() {
        return fondo;
    }

    public void setFondo(Image fondo) {
        this.fondo = fondo;
    }

    public Image getDelante() {
        return delante;
    }

    public void setDelante(Image delante) {
        this.delante = delante;
    }
}
