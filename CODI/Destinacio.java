import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

/**
 * @class Destinacio
 * @brief És una Lloc amb una sèrie de PuntInteres.
 * @details Coneixem tots els PuntInteres que té, el seu TransportPublic i l'agenda de sortides de TransportIndirecte.
 * @author Roger Generoso Masós
 * @version 2017.4.5
 */
public class Destinacio extends Lloc {
    private Map<String, PuntInteres> puntsInteres; ///< Conté tots els PuntInteres de la destianció actual.
    private TransportPublic transportPublic; ///< És el transport públic de la destinació.
    private Estacio sortidesTransIndirectes; ///< Conté les sortides de transports indirectes des de la ciutat.

    /**
     * @pre cert
     * @post S'ha creat una destinació sense transport públic i sense cap PuntInteres.
     * @param nomA És el nom de la destinació
     * @param lat Latitut d'on es troba
     * @param longit Longitud d'on es troba
     * @param zonHoraria Zona horaria d'on es troba.
     */
    Destinacio(String nomA, Float lat, Float longit, TimeZone zonHoraria){
        super(nomA,lat,longit,zonHoraria);
        puntsInteres = new HashMap<>();
        sortidesTransIndirectes = new Estacio();
    }

    /**
     * @pre nomPI no existeix com a PuntInteres associat a la Destinacio.
     * @post S'ha afegit pi amb nom nomPI com a no PuntInteres de la Destinacio actual
     * @param nomPI És el nom del PuntInteres
     * @param pi És el PuntInteres.
     */
    public void afegirPuntInteres(String nomPI, PuntInteres pi){
        puntsInteres.put(nomPI, pi);
    }

    /**
     * @pre La Destinacio no té cap TransportPublic assignat
     * @post S'ha assignat a com a TransportPublic de la Destinacio actual.
     * @param a És el transport públic a associar.
     */
    public void assignarTransportPublic(TransportPublic a){
        transportPublic = a;
    }

    /**
     * @pre cert
     * @post retorna el nombre de PuntInteres de la destinació
     * @return retorna el nombre de PuntInteres de la destinació
     */
    public int nPuntsInteres(){ return puntsInteres.size(); }

    /**
     * @pre Cert
     * @post Afegeix una nova sortida de TransportIndirecte a la Destinació actual pel dia data.
     * @param data És el dia que s'afageix la sortida.
     * @param t És el transport que s'afageix.
     * @param tempsOrigenFinsMitja És el temps des de qualsevol PuntInteres associat a la Destinació fins arribar a la sortida.
     */
    public void afegirNovaSortidaIndirecta(LocalDate data, TransportIndirecte t, int tempsOrigenFinsMitja){
        if(sortidesTransIndirectes == null) sortidesTransIndirectes = new Estacio(tempsOrigenFinsMitja);
        sortidesTransIndirectes.afegirTransport(data, t);
    }

    /**
     * @pre cert
     * @post Retorna Iterador de sorides Indirectes per la data x.
     * @param x És el dia que es vol obtenir les sortides.
     * @return Retorna Iterador de sorides Indirectes per la data x.
     */
    public Iterator<TransportIndirecte> sortidesTransportsIndirectes(LocalDate x){
        return sortidesTransIndirectes.retornaConnexions(x);
    }

    /**
     * @pre cert
     * @post Retorna Iterador dels PuntInteres associats a la Destinacio actual.
     * @return Retorna Iterador dels PuntInteres associats a la Destinacio actual.
     */
    public Iterator<PuntInteres> obtenirPuntsInteres() {
        return puntsInteres.values().iterator();
    }

    /**
     * @pre cert
     * @post Retorna el transport públic de la Destinació, en cas que en tingui. Altrament null.
     * @return Retorna el transport públic de la Destinació, en cas que en tingui. Altrament null.
     */
    public TransportPublic obtTransPub() {
        return transportPublic;
    }

    public int obtTempsFinsEstacio() {
        return sortidesTransIndirectes.obtTempsFinsEstacio();
    }
}