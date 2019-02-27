
/**
 * @class TransportPublic
 * @brief És un transport públic, el qual es mou entre la mateixa ciutat, entre punts de interès.
 * @details En coneixem el nom del mitjà, la durada del seu trajecte (independentment del lloc de destí), i el seu preu.
 * @author Amat Martínez Vila
 * @version 2017.4.5
 */


public class TransportPublic {

    String nomMitja; ///<El nom del mitjà de transport.
    int duradaTrajecte; ///< La durada del trajecte, independentment del destí.
    Float preuUnic; ///< El preu de qualsevol trajecte que es faci amb ell.


    TransportPublic(String nomMitjaTransp, int duradaTrajecte, Float preu) {
        nomMitja = nomMitjaTransp;
        this.duradaTrajecte = duradaTrajecte;
        preuUnic = preu;
    }

    /**
     * @pre cert
     * @post Retorna la durada del trajecte en minuts.
     * @return Retorna la durada del trajecte en minuts.
     */
    public int obtDurada() {
        return duradaTrajecte;
    }

    /**
     * @pre cert
     * @post Retorna el preu del trajecte.
     * @return Retorna el preu del trajecte.
     */
    public float obtPreu() {
        return preuUnic;
    }

    /**
     * @pre cert
     * @post Retorna el nom del mitjà de transport.
     * @return Retorna el nom del mitjà de transport.
     */
    public String obtNomMitja() {
        return nomMitja;
    }
}
