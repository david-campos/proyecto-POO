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
        if(f.isDirectory())
        {
            int cuenta = 0;
            if(f.list() != null){
                for(String otroF : f.list())
                    if(otroF.equals("objetos.csv") ||
                       otroF.equals("npcs.csv")    ||
                       otroF.equals("mapa.csv")
                      )
                        cuenta++;
            if(cuenta == 3)
                return new ImageIcon("img/ico_map.png");
            }
        }
        return super.getIcon(f);
    }
}