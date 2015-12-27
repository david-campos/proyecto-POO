/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilidades;

import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileView;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class FileViewMapa extends FileView {
    public FileViewMapa() {
        super();
    }

    @Override
    public Icon getIcon(File f) {
        if(!f.isDirectory())
        {
            String nombre = f.getName();
            if(nombre.lastIndexOf(".") != -1 && nombre.substring(nombre.lastIndexOf(".")+1).equals(ConstantesGenerales.EXTENSION_MAPA))
                return new ImageIcon("img/ico_map.png");
        }
        return super.getIcon(f);
    }
}