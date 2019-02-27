
/**
 * @class TransportDirecte
 * @brief Mitja de transport que s'utilitza per moure's entre Llocs.
 * @details Els atributs són els mateixos que el mitjà de transport genèric.
 * @author Amat Martínez Vilà
 * @version 2017.4.5
 */

import java.time.LocalTime;

public class TransportDirecte extends MitjaTransport
{

    public TransportDirecte(String n, Lloc d, int durada, float p) {
        super(n, d, durada, p);
    }

    /**
     * @pre cert
     * @post Redefineix el compareTo. 
     * @return Retorna un nombre positiu si és major; un 0 si és igual; i un nombre negatiu si és menor.
     */
    @Override
    public int compareTo(MitjaTransport o) {
        if(o instanceof TransportIndirecte){
            return o.compareTo(this);
        }else{
            return -1;
        }
    }
}