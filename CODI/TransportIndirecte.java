
/**
 * @class TransportIndirecte
 * @brief Mitjà de transport que serveix per moure's entre estacions.
 * @details Cada ciutat té una estació de transports que es mouen entre ciutats. 
 * 			Les estacions contindràn Transports Indirectes per moure's entre elles.
 * @author Amat Martínez Vilà
 * @version 2017.4.5
 */

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TransportIndirecte extends MitjaTransport {

	private LocalDateTime horaSortida; ///< Hora de sortida del transport actual.
    private int tempsDesDEstacio; ///< Temps que es triga per a marxar des de l'estació actual.

    public TransportIndirecte(String nomMitja, Lloc desti, int durada, float preu, LocalDateTime horaSortida, int temps){
    	super(nomMitja,desti,durada,preu);
    	this.horaSortida = horaSortida; 
    	this.tempsDesDEstacio = temps;
    }

    /**
     * @pre cert
     * @post Retorna el temps que es triga des de l'estació on esta el transport actual.
     * @return Retorna el temps que es triga des de l'estació on esta el transport actual.
     */
    public int obtTempsDesDestacio(){
    	return tempsDesDEstacio;
	}

	/**
     * @pre cert
     * @post Redefineix el compareTo. 
     * @return Retorna un nombre positiu si és major; un 0 si és igual; i un nombre negatiu si és menor.
     */
	@Override
	public int compareTo(MitjaTransport o) {
		if(o instanceof TransportIndirecte){
			return ((TransportIndirecte) o).horaSortida.compareTo(horaSortida);
		}else{
			return -1;
		}
	}
	/**
     * @pre cert
     * @post Retorna la hora de sortida del transport actual.
     * @return Retorna la hora de sortida del transport actual.
     */
	public LocalDateTime obtHoraSortida() {
    	return horaSortida;
	}
}