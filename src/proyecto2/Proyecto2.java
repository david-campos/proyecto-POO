/*
 * 
 */
package proyecto2;

import Menus.Menu;
import Menus.MenuConsola;
import Menus.MenuGrafico;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Proyecto de Programación Orientada a Objetos,
 * versión 3
 * @author David Campos y Cristofer Canosa
 */
public class Proyecto2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        Menu menuInicio;
        if(args.length > 0 && args[0].equalsIgnoreCase("-consola"))
            menuInicio =  new MenuConsola();
        else{
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                Logger.getLogger(MenuGrafico.class.getName()).log(Level.SEVERE, null, ex);
            }
            menuInicio = new MenuGrafico();
        }
        menuInicio.lanzar();
    }
}
