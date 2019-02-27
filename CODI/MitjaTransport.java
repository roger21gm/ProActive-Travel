
/**
 * @class MitjaTransport
 * @brief Transport que va a un lloc determinat.
 * @details Parteix des de un lloc determinat i pot anar tant a estacions, com a punts de interès. El desplaçament val un preu i té una durada.
 * @author Amat Martínez Vilà
 * @version 2017.4.5
 */

public abstract class MitjaTransport implements Comparable<MitjaTransport>
{
	protected Lloc desti; ///< Lloc de destí
	protected String nomMitja; ///< Nom del mitjà de transport actual
	private int durada_trajecte; ///< Durada del trajecte amb el mitjà de transport actual
	private float preu; ///< Preu del desplaçament.

	public MitjaTransport(String n, Lloc d, int durada, float p)
	{
		this.nomMitja = n;
		this.desti = d;
		this.durada_trajecte = durada;
		this.preu = p;
	} 	

	/**
     * @pre cert
     * @post Retorna el nom del mitjà de transport actual.
     * @return Retorna el nom del mitjà de transport actual.
     */
	public String obtNom(){
		return nomMitja;
	}

	/**
     * @pre cert
     * @post Retorna el lloc de destí.
     * @return Retorna el lloc de destí.
     */
	public Lloc obtLlocDesti(){
		return desti;
	}

	/**
     * @pre cert
     * @post Retorna la durada del trajecte.
     * @return Retorna la durada del trajecte.
     */
	public int obtDurada(){
		return durada_trajecte;
	}

	/**
     * @pre cert
     * @post Retorna el preu del trajecte.
     * @return Retorna el preu del trajecte.
     */
	public Float obtPreu() { return preu; }

	/**
     * @pre cert
     * @post Redefineix el equals. 
     * @return Retorna un booleà que indica si el transport actual i b són iguals.
     */
	@Override public boolean equals(Object b){
		if (b instanceof MitjaTransport) {
			MitjaTransport m = (MitjaTransport)b;
			return nomMitja.equals(m.nomMitja) && desti.equals(m.desti);
		}
		else return false;
	}

}