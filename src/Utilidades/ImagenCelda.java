/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilidades;

import java.awt.Image;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.fondo);
        hash = 13 * hash + Objects.hashCode(this.delante);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImagenCelda other = (ImagenCelda) obj;
        if (!Objects.equals(this.fondo, other.fondo)) {
            return false;
        }
        return Objects.equals(this.delante, other.delante);
    }

}
