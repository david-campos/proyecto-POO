/*
 * 
 */
package Mapa;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public final class Punto {
    
    /**
     *
     * @param B
     * @param A
     * @return
     */
    public static Punto resta(Punto B, Punto A){
        return new Punto(B.x - A.x, B.y - A.y);
    }
    
    //Los atributos se definen como públicos,
    //pues cualquier valor para ellos será válido,
    //y sería incómodo tener que andar usando setters y getters para esto

    /**
     *
     */
        public int x;

    /**
     *
     */
    public int y;
    
    /**
     *
     * @param x
     * @param y
     */
    public Punto(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     *
     */
    public Punto(){
        this.x = 0;
        this.y = 0;
    }
    
    /**
     *
     * @param pt
     * @return
     */
    public double dist(Punto pt){
        return new Punto(pt.x-this.x, pt.y-this.y).norma();
    }

    /**
     *
     * @param ancho
     * @param alto
     * @return
     */
    public boolean en(int ancho, int alto){
        return (x>=0 && x<ancho && y >= 0 && y < alto);
    }

    /**
     *
     * @return
     */
    public double norma(){
        return Math.sqrt(x*x+y*y);
    }

    /**
     *
     * @return
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
    
}
