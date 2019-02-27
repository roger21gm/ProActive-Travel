
import java.time.LocalDateTime;
import java.util.*;

/**
 * @class PuntInteres
 * @brief És un lloc visitable o un allotjament. Té una sèrie de Connexions Directes que pot visitar usant TransportsDirecte.
 * @details De cada PuntInteres en coneixem les característiques de les activitats que s'hi duen a terme i una sèrie de connexions directes amb altres PuntInteres.
 * @author Roger Generoso Masós
 * @version 2017.4.5
 */

public abstract class PuntInteres extends Lloc implements Comparable<PuntInteres>{
    //ATRIBUTS
    private Set<String> caracteristiques; ///< Son les característiques de les activitats que es duen a terme en el PuntInteres.
    private Map<PuntInteres, TransportDirecte> connexionsDirectes; ///< Llista de PuntInteres amb el que està connectat el PuntInteres actual.
    private Destinacio ciutatAQuePertany; ///< Conté la Destinació a la qual està assignat el PuntInteres. Si no està assignat a cap ciutat, null
    private Float preu; ///< Preu del PuntInteres

    /**
     * @pre cert
     * @post Crea un PuntInteres amb nom, latitud, longitud i zonaHoraria determinades i sense característiques, altrament dit, un Punt Aïllat.
     * @param nom És el nom del PuntInteres
     * @param latitud Latitud en que es troba el PuntInteres
     * @param longitud Longitud en que es troba el PuntInteres
     * @param zonaHoraria ZonaHoraria del PuntInteres.
     */
    public PuntInteres(String nom, Float latitud, Float longitud, TimeZone zonaHoraria) {
        super(nom,latitud,longitud,zonaHoraria);
        connexionsDirectes = new HashMap<>();
        caracteristiques = null;
        preu = Float.MAX_VALUE;
    }


    /**
     * @pre cert
     * @post Crea un PuntInteres amb nom, latitud, longitud ,zonaHoraria, preu i característiques determinades.
     * @param nom És el nom del PuntInteres
     * @param latitud És la latitud del PuntInteres
     * @param longitud És la longitud del PuntInteres
     * @param zonaHoraria És la zona horària del PuntInteres
     * @param carac És una conjunt de característiques que compleix el PuntInteres.
     * @param preu És el preu del PuntInteres.
     */
    public PuntInteres(String nom, Float latitud, Float longitud, TimeZone zonaHoraria, Set<String> carac, Float preu) {
        super(nom,latitud,longitud,zonaHoraria);
        connexionsDirectes = new HashMap<>();
        caracteristiques = carac;
        this.preu = preu;
    }


    //MÈTODES

    /**
     * @pre Cert
     * @post Afegeix una nova Connexió Directa al PuntInteres
     * @param desti És el PuntInteres amb que fas la connexió directa.
     * @param trans És el transport directe amb que estableixes connexió entre els dos punts.
     */
    void afegirConnexioDirecta(PuntInteres desti, TransportDirecte trans){
        connexionsDirectes.put(desti,trans);
    }

    /**
     * @pre cert
     * @post Retorna un iterador de PuntInteres amb les connexions directes del PuntInteres actual.
     * @return Retorna un iterador de PuntInteres amb les connexions directes del PuntInteres actual.
     */
    public Iterator<TransportDirecte> obtenirConnexionsDirectes(){
        return connexionsDirectes.values().iterator();
    }

    /**
     * @pre cert
     * @post Retorna un Set amb les característiques del PuntInteres.
     * @return Retorna un Set amb les característiques del PuntInteres.
     */
    public Set<String> obtenirCaracteristiques(){ return caracteristiques; }


    /**
     * @pre cert
     * @post El PuntInteres actual ara pertany a la Destinacio a
     * @param a És la Destinacio a la que es vol associar el PuntInteres.
     */
    public void associarADestinacio(Destinacio a){
        ciutatAQuePertany = a;
    }

    /**
     * @pre cert
     * @post Retorna la Destinació a la que pertany el PuntInteres. Si no pertany a cap Destinació retorna null.
     * @return Retorna la Destinació a la que pertany el PuntInteres. Si no pertany a cap Destinació retorna null.
     */
    public Destinacio obtDestinacio(){
        return ciutatAQuePertany;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PuntInteres that = (PuntInteres) o;

        if (caracteristiques != null ? !caracteristiques.equals(that.caracteristiques) : that.caracteristiques != null)
            return false;
        if (connexionsDirectes != null ? !connexionsDirectes.equals(that.connexionsDirectes) : that.connexionsDirectes != null)
            return false;
        if (ciutatAQuePertany != null ? !ciutatAQuePertany.equals(that.ciutatAQuePertany) : that.ciutatAQuePertany != null)
            return false;
        return preu != null ? preu.equals(that.preu) : that.preu == null;
    }

    /**
     * @pre b És un punt de interès no nul.
     * @post Calcula la distància entre el punt de interès actual en base a les coordenades geogràfiques.
     * @return Retorna la distància entre els dos punts.
     */
    public float distanciaA(PuntInteres b){
        return (float) Math.sqrt(Math.pow(b.obtCoordenades().segon - obtCoordenades().segon,2)+Math.pow(b.obtCoordenades().primer - obtCoordenades().primer,2));
    }

    /**
     * @pre cert
     * @post Retorna les coordenades geogràfiques del punt de interès actual.
     * @return Retorna les coordenades geogràfiques del punt de interès actual.
     */
    public Pair<Float, Float> obtCoordenades(){
        return new Pair<Float,Float>(super.getLat(),super.getLon());
    }

}
