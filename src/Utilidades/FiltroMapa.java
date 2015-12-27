/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilidades;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class FiltroMapa extends FileFilter {
    @Override
    public boolean accept(File f) {
        if(f.isFile()){
            String nombre = f.getName();
            return (nombre.lastIndexOf(".") != -1 && nombre.substring(nombre.lastIndexOf(".")+1).equals(ConstantesGenerales.EXTENSION_MAPA));
        }
        return true;
    }

    @Override
    public String getDescription() {
        return "Mapas";
    }
}