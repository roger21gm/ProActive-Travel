import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


/**
 * @class ItemRuta
 * @brief És un ítem de la Ruta.
 * @details Un ItemRuta pot ser: un desplaçament, una estada en un hotel o una visita.
 * @author Adria Orellana Cruañas
 * @version 2017.05.24
 */
public class ItemRuta 
{
	PuntInteres a_lloc; ///< Punt d'interés on es realitza l'activitat.
	LocalDateTime a_dataHoraInici; ///< Data i hora d'inici de l'activitat.
	LocalDateTime a_dataHoraFi; ///< Data i hora de fi de l'activitat.
	float a_preu; ///< Preu de l'activitat.
	float a_grauSatisfaccio; ///< Grau de satisfacció de l'activitat.
	int a_durada; ///< Durada de l'activitat.

	/**
     * @pre cert
     * @post S'ha creat un Item Ruta amb les dades dels paràmetres.
     * @param lloc És el punt d'interés on fem l'activitat.
	 * @param inici És data d'inici de l'activitat.
	 * @param fi És la de fi de l'activitat.
	 * @param preu És el preu de l'activitat.
	 * @param satisfaccio És el grau de satisfacció de l'activitat.
     */
	ItemRuta(PuntInteres lloc, LocalDateTime inici, LocalDateTime fi, float preu, float satisfaccio){
		a_lloc=lloc;
		a_dataHoraInici=inici;
		a_dataHoraFi=fi;
		a_preu=preu;
		a_grauSatisfaccio=satisfaccio;
		a_durada = (int)ChronoUnit.MINUTES.between(inici, fi);
	}

	/**
     * @pre cert
     * @post Retorna el punt d'interés de l'item Ruta.
     * @return Retorna el punt d'interés de l'item Ruta.
     */
	public PuntInteres obtenirPuntInteres() {
		return a_lloc;
	}
	
	/**
     * @pre cert
     * @post Retorna el preu de l'item Ruta.
     * @return Retorna el preu de l'item Ruta.
     */
	public float obtPreu(){
		return a_preu;
	}
	
	/**
     * @pre cert
     * @post Retorna el grau de satisfacció de l'item Ruta.
     * @return Retorna el grau de satisfacció de l'item Ruta.
     */
	public float obtGrauSatis(){
		return a_grauSatisfaccio;
	}
	
	/**
     * @pre cert
     * @post Retorna la data d'inici de l'item Ruta.
     * @return Retorna la data d'inici de l'item Ruta.
     */
	public LocalDateTime obtDataInici(){
		return a_dataHoraInici;
	}
	
	/**
     * @pre cert
     * @post Retorna la data de fi de l'item Ruta.
     * @return Retorna la data de fi de l'item Ruta.
     */
    public LocalDateTime obtDataFi() { 
		return a_dataHoraFi; 
	}
	
	/**
     * @pre cert
     * @post Retorna la durada de l'item Ruta.
     * @return Retorna la durada de l'item Ruta.
     */
    public int obtDurada() {
		return a_durada;
	}
	
	/**
     * @pre cert
     * @post Retorna un string amb el nom del punt d'interés, de l'item Ruta.
     * @return Retorna un string amb el nom del punt d'interés, de l'item Ruta.
     */
	@Override
	public String toString() {
		return a_lloc.toString();
	}
}