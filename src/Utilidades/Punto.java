package Utilidades;

/**
 * Tupla ordenada en R2.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public final class Punto {
    
    /**
     * Resta el punto A al punto B.
     * @param B punto B
     * @param A punto A
     * @return (x,y)=(B.x-A.x, B.y-A.y)
     */
    public static Punto resta(Punto B, Punto A){
        return new Punto(B.x - A.x, B.y - A.y);
    }
    
    //Los atributos se definen como públicos,
    //pues cualquier valor para ellos será válido,
    //y sería incómodo tener que andar usando setters y getters para esto

    /**
     * Componente x del punto
     */
    public int x;
    /**
     * Componente y del punto
     */
    public int y;
    
    /**
     * Crea un nuevo punto
     * @param x coordenada x
     * @param y coordenada y
     */
    public Punto(int x, int y){
        this.x = x;
        this.y = y;
    }
    /**
     * Crea un punto en el origen (0,0)
     */
    public Punto(){
        this.x = 0;
        this.y = 0;
    }
    
    /**
     * Devuelve la distancia del punto a otro punto
     * @param pt punto al que medir la distancia
     * @return Distancia del punto a pt
     */
    public double dist(Punto pt){
        return new Punto(pt.x-this.x, pt.y-this.y).norma();
    }
    /**
     * Comprueba si el punto se encuentra en un cuadrado con esquinas (0,0)
     * y (alto, ancho).
     * @param ancho ancho del rectángulo
     * @param alto alto del rectángulo
     * @return {@code true} si el punto se encuentra dentro del rectángulo
     */
    public boolean en(int ancho, int alto){
        return (x>=0 && x<ancho && y >= 0 && y < alto);
    }
    /**
     * Devuelve la distancia del punto al origen
     * @return Distancia del punto al origen (Pitágoras)
     */
    public double norma(){
        return Math.sqrt(x*x+y*y);
    }
    /**
     * Convierte el punto (x,y) al sistema que usábamos en las primeras versiones
     * para los puntos, como un array [i, j], tipo matricial. Debe evitarse
     * el uso de este tipo de puntos ya.
     * @return El array que representaba este punto por el método antiguo.
     */
    public int[] toArray(){
        return new int[]{y, x};
    }
    
    @Override
    public Punto clone(){
        return new Punto(this.x, this.y);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.x;
        hash = 71 * hash + this.y;
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
        final Punto other = (Punto) obj;
        if (this.x != other.x) {
            return false;
        }
        return this.y == other.y;
    }

    @Override
    public String toString() {
        return "Punto(" + x + ", " + y + ')';
    }
    
}
