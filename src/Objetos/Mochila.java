package Objetos;

import Excepciones.ObjetoException;
import Juego.Consola;
import Juego.ConsolaNormal;
import java.util.ArrayList;
/**
 * Una bonita mochila, probablemente de color rosa, que portan enemigos y jugador para llevar sus cuernos de unicornio y demás cosas cuquis.
 * @author David Campos y Cristofer Canosa
 */
public class Mochila {
    private int maxObjetos;
    private double maxPeso;
    private double peso;
    private ArrayList<Objeto> contenido;
    private Consola consola;

    public Mochila(int maxObjetos, int maxPeso, ArrayList<Objeto> contenido){
        if(maxObjetos > 0)
            this.maxObjetos = maxObjetos;
        if(maxPeso > 0)
            this.maxPeso = maxPeso;
        if(contenido != null && contenido.size() <= maxObjetos)
        {
            peso=0;
            for(Objeto obj: contenido)
                peso+=obj.getPeso();
            if(peso <= maxPeso)
                this.contenido = (ArrayList) contenido.clone();
            else{
                peso = 0;
                this.contenido = new ArrayList();
            }
            consola = new ConsolaNormal();
        }
    }
    public Mochila(int maxObjetos, int maxPeso){
        this(maxObjetos, maxPeso, new ArrayList(maxObjetos));
    }
    public Mochila(){
        this(10,50);
    }
    
    /**
     * Obtiene el número máximo de objetos de la mochila.
     * @return Máximo de objetos de la mochila
     */
    public int getMaxObjetos() {
        return maxObjetos;
    }
    /**
     * Obtiene el máximo de peso de la mochila
     * @return Máximo peso de la mochila
     */
    public double getMaxPeso() {
        return maxPeso;
    }
    
    /**
     * Añade un objeto a la mochila.
     * @param obj El objeto a añadir
     * @return True si el objeto se pudo añadir, false si no se pudo.
     */
    public boolean addObjeto(Objeto obj) {
        if(obj == null) return false;
        if(getNumObj() >= maxObjetos) {
            consola.imprimir("No caben más objetos en la mochila...");
            return false;
        }
        if(pesoActual() + obj.getPeso() > maxPeso) {
            consola.imprimir("El objeto pesa demasiado, romperá la mochila.");
            return false;
        }
        contenido.add(obj);
        peso+=obj.getPeso();
        return true;
    }
    /**
     * Elimina un objeto de la mochila.
     * @param obj El objeto a eliminar.
     */
    public void remObjeto(Objeto obj) {
        if(obj!=null && contenido.remove(obj)){
            peso-=obj.getPeso();
        }
    }
    /**
     * Devuelve el objeto i-ésimo guardado en la mochila.
     * @param i índice del objeto que se quiere obtener, comenzando en 0.
     * @return el objeto solicitado, null si no hay tal objeto.
     */
    public Objeto getObjeto(int i){
        if(i >= 0 && i < contenido.size())
            return contenido.get(i);
        else
            return null;
    }
    public Objeto getObjeto(String nombre) {
        if(nombre != null)
            for (Objeto ob : contenido) 
                if(ob.nombre.equals(nombre))
                    return ob;
        return null;
    }
    /**
     * Devuelve una lista de todos los objetos en la mochila.
     * @return Lista de objetos en la mochila.
     */
    public ArrayList<Objeto> getObjetos(){
        return (ArrayList) contenido.clone();
    }
    /**
     * Devuelve el número de objetos en la mochila.
     * @return Número de objetos
     */
    public int getNumObj () {
        return contenido.size();
    }
    /**
     * Devuelve el peso actual de la mochila
     * @return El peso actual
     */
    public double pesoActual () {
        return peso;
    }
    
    @Override
    public String toString() {
        String ret = new String();
        for(int i=0;i<contenido.size();i++)
            ret+="\t" + i + ". " + contenido.get(i).nombre + "\n";
        return ret;
    }
}
