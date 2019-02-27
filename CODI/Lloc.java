import java.util.TimeZone;

/**
 * @class Lloc
 * @brief És un lloc amb la seva informació bàsica.
 * @details Sabem el nom del lloc, les coordenades i la zonaHorària.
 * @author Roger Generoso Masós
 * @version 2017.4.5
 */

public class Lloc{
    String nom; ///< És el nom del lloc.
    Float latitud; ///< És la latitud del lloc.
    Float longitud; ///< És la longitud del lloc.
    TimeZone zonaHoraria; ///< És la zona horària del lloc.

    /**
     * @pre cert
     * @post Crea un lloc.
     * @param nomA És el nom del lloc.
     * @param lat És la latitud del lloc.
     * @param longit És la longitud del lloc.
     * @param zonHoraria És la zona horària del lloc.
     */
    Lloc(String nomA, Float lat, Float longit, TimeZone zonHoraria){
        nom = nomA;
        latitud = lat;
        longitud = longit;
        zonaHoraria = zonHoraria;
    }

    @Override
    public String toString() {
        return nom;
    }

    /**
     * @pre cert
     * @return retorna el nom.
     */
    public String obtNom() {
        return nom;
    }

    /**
     * @pre cert
     * @return retorna la longitud del lloc.
     */
    public float getLon() {
        return longitud;
    }

    /**
     * @pre cert
     * @return retorna la zona horària del lloc.
     */
    public float getLat() {
        return latitud;
    }

    /**
     * @pre Cert
     * @return Obtenim la zonaHoraria del lloc.
     */
    public TimeZone obtZonHoraria() {
        return zonaHoraria;
    }
}