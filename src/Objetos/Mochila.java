package Objetos;

import Excepciones.MaximoObjetosException;
import Excepciones.MaximoPesoException;
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

    /**
     * Crea una mochila
     * @param maxObjetos máximo número de objetos de la mochila
     * @param maxPeso máximo peso de la mochila
     * @param contenido array con el contenido inicial de la mochila
     */
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
        }
    }
    /**
     * Crea una mochila vacía
     * @param maxObjetos máximo número de objetos de la mochila
     * @param maxPeso máximo peso soportado por la mochila
     */
    public Mochila(int maxObjetos, int maxPeso){
        this(maxObjetos, maxPeso, new ArrayList(maxObjetos));
    }
    /**
     * Crea una mochila vacía con un máximo de 10 objetos y 5Kg de peso.
     */
    public Mochila(){
        this(10,5);
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
     * Cambia el número máximo de objetos de la mochila, debe ser mayor
     * que el número actual de objetos.
     * @param maxObjetos número máximo de objetos deseado
     */
    public void setMaxObjetos(int maxObjetos) {
        if(maxObjetos >= getNumObj())
            this.maxObjetos = maxObjetos;
        else
            this.maxObjetos = getNumObj();
    }
    /**
     * Cambia el peso máximo de la mochila
     * @param maxPeso peso máximo deseado
     */
    public void setMaxPeso(double maxPeso) {
        if(maxPeso > pesoActual())
            this.maxPeso = maxPeso;
        else
            this.maxPeso = pesoActual();
    }
    
    /**
     * Añade un objeto a la mochila.
     * @param obj el objeto a añadir
     * @return siempre {@code true}
     * @throws MaximoObjetosException si se supera el máximo de objetos
     * @throws MaximoPesoException si se supera el máximo de peso
     */
    public boolean addObjeto(Objeto obj) throws MaximoObjetosException, MaximoPesoException {
        if(obj == null) return false;
        if(getNumObj() >= maxObjetos)
            throw new MaximoObjetosException("No caben más objetos en la mochila...");
        if(pesoActual() + obj.getPeso() > maxPeso) 
            throw new MaximoPesoException("El objeto pesa demasiado, romperá la mochila.");
        
        contenido.add(obj);
        peso+=obj.getPeso();
        return true;
    }
    /**
     * Elimina un objeto de la mochila.
     * @param obj el objeto a eliminar.
     */
    public void remObjeto(Objeto obj) {
        if(obj!=null && contenido.remove(obj)){
            peso-=obj.getPeso();
        }
    }
    /**
     * Devuelve el objeto i-ésimo guardado en la mochila.
     * @param i índice del objeto que se quiere obtener, comenzando en 0.
     * @return El objeto solicitado, o {@code null} si no hay tal objeto.
     */
    public Objeto getObjeto(int i){
        if(i >= 0 && i < contenido.size())
            return contenido.get(i);
        else
            return null;
    }
    /**
     * Busca en la mochila el objeto con el nombre dado.
     * @param nombre nombre del objeto a buscar
     * @return El objeto cuyo nombre se corresponde con el dado, o {@code null}
     *          si el objeto no está en la celda.
     */
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
