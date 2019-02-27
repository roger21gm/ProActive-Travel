import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.time.LocalDate;

/**
 * @class Client
 * @brief És un client de l'agència de viatges.
 * @details Un client proporciona a l'agència de viatges: 
 *				- El seu nom.
 *				- Una llista de llocs on li agradaria anar.
 *				- Una llista de llocs on ja ha anat. 
 * @author Adrià Orellana Cruañas
 * @version 2017.05.24
 */
public class Client{
	private String a_nom; ///< Nom del client.
	private List<String> a_preferencies; ///< Llista amb les preferències del client.
	private List<Pair<String,LocalDate>> a_llocsVisitats; ///< Llista amb els llocs que ja ha visitat el client.
	
	/**
	 * @pre cert
	 * @post S'ha creat un Client amb les dades buides.
	 */
	Client(){
		a_nom="";
		a_preferencies = new ArrayList<String>();
		a_llocsVisitats =  new ArrayList<Pair<String,LocalDate>>();
	}
	
	/**
	 * @pre nom i prefs inicialitzats.
	 * @post S'ha creat un Client amb les dades proporcionades per paràmetres.
	 * @param nom És el nom del client
	 * @param prefs És la llista de llocs amb prefrència que vol visitar el client.
	 */
	Client(String nom, List<String> prefs){
		a_nom=nom;
		a_preferencies=prefs;
		a_llocsVisitats =  new ArrayList<Pair<String,LocalDate>>();
	}
	
	/**
	 * @pre a_preferencies no null.
	 * @post Retorna una llista amb les preferències del clients.
	 * @return Retorna una llista amb les preferències del clients.
	 */
	public List<String> obtenirPreferencies(){
		return a_preferencies;
	}
	
	/**
	 * @pre nom inicialitzat.
	 * @post Retorna un Pair amb <Cert,Data> si s'ha visitat el lloc que se li passa per paràmetres. 
	 * 		 Retorna <Fals,DataMAX> amb una data per defecte si no s'ha visitat.
	 * @param nom És el nom del lloc que es vol mirar si s'ha visitat o no.
	 * @return Retorna un Pair amb <Cert,Data> si s'ha visitat el lloc que se li passa per paràmetres. 
	 * 		   Retorna <Fals,DataMAX> amb una data per defecte si no s'ha visitat.
	 */
	public Pair<Boolean,LocalDate> jaVisitat(String nom){
		Iterator <Pair<String,LocalDate>> it= a_llocsVisitats.iterator();
		while(it.hasNext()){
			Pair<String, LocalDate> act = it.next();
			if(act.primer.equals(nom)){
				return new Pair<Boolean, LocalDate>(true,act.segon);
			}
		}
		return new Pair<Boolean, LocalDate>(false,LocalDate.MAX);
	}
	
	/**
	 * @pre visita inicialitzada.
	 * @post Afegeix una visita a la llista de llocs visitats del Client.
	 * @param visita És la visita que es vol afegir a la llista del Client com a lloc ja visitat.
	 * @return Retorna un Pair amb <Cert,Data> si s'ha visitat el lloc que se li passa per paràmetres. 
	 * 		   Retorna <Fals,DataMAX> amb una data per defecte si no s'ha visitat.
	 */
	public void afegirVisita (Pair<String, LocalDate> visita){
		a_llocsVisitats.add(visita);
	}
	/**
	 * @pre cert
	 * @post Retorna el nom del Client.
	 * @return Retorna el nom del Client.
	 */
	public String obtNom(){ 
		return a_nom; 
	}




}