
/**
 * @class Estacio
 * @brief Una estació situada a un lloc des d'on surten diferents tranports cap a altres lloc.
 * @details Centre des d'on surten una sèrie de transports cap a altres llocs. 
 * Es triga un temps per arribar-hi i un altre per anar-se'n.
 * @author Amat Martínez Vilà
 * @version 2017.4.5
 */

import java.time.LocalDate;
import java.util.*;

public class Estacio {

	///< Estructura que conté, indexat per lloc de destí i per data, tots els transports que surten de l'estació actual.
	private TreeMap<LocalDate, HashSet<TransportIndirecte>> transports;
	///< Temps que es triga per a arribar fins a l'estació actual.
    private int tempsFinsAEstacio;


    public Estacio(){
        transports = new TreeMap<>();
        tempsFinsAEstacio = 0;
    }

    public Estacio(int tempsDes){
        this.tempsFinsAEstacio = tempsDes;
        transports = new TreeMap<>();
    }

    /**
     * @param dia és una data vàlida.
     * @post afegeix el transport al destí indicat a la llista el dia corresponent.
     * @param transport és un transport Indirecte no buit.
     * @return (void)
     */
    public void afegirTransport(LocalDate dia, TransportIndirecte t){
        HashSet<TransportIndirecte> trans_dia = transports.get(dia);
        if(trans_dia == null) afegirNouDia(dia);
        transports.get(dia).add(t);
    }

    /**
     * @pre dia és una data vàlida.
     * @post afegeix el transport al destí indicat a la llista el dia corresponent.
     * @param dia és el dia en que surt el transport.
     * @return (void)
     */
    private void afegirNouDia(LocalDate dia){
        transports.put(dia,new HashSet<>());
    }

    /**
     * @pre dia ha de ser un dia que contingui connexions. 
     * @return retorna un iterador de les connexions de la estació actual del dia indicat. En el cas que no n'hi hagi, retorna null;
     */
    public Iterator<TransportIndirecte> retornaConnexions(LocalDate dia){
        HashSet<TransportIndirecte> connexions = transports.get(dia);
        if(connexions!=null) return connexions.iterator();
        else return null;
	}

    /**
     * @pre cert
     * @post Retorna el temps que es triga per arribar fins a l'estació actual.
     * @return Retorna el temps que es triga per arribar fins a l'estació actual.
     */
    public int obtTempsFinsEstacio() {
        return tempsFinsAEstacio;
    }
}