import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @class Comanda
 * @brief És la Comanda que fan els clients de l'agència de viatges.
 * @details La Comanda se li passarà al calculador de rutes perquè ens calculi el viatge que més s'adapti a la nostra Comanda.
 * @author Adrià Orellana Cruañas
 * @version 2017.05.24
 */
public class Comanda {
	private Map<String, Client> a_llistaClients; ///< Llista de clients de la Comanda.
	private LocalDate a_dataInici; ///< Data d'inici del viatge.
	private LocalTime a_horaInici; ///< Hora d'inici del viatge.
	private int a_nDies; ///< Número de dies que dura el viatge.
	private float a_preuMaxim; ///< Preu màxim que tindrà el viatge.
	private String a_categoriaAllotjaments; ///< Categoria que han de tenir els apartaments del viatge.
	private Lloc a_inici; ///< Lloc on començarà el viatge.
	private Lloc a_fi; ///< Lloc on acabarà el viatge.
	private Set<PuntInteres> a_puntsIntermitjos; ///< Punts d'Interés que es volen visitar des del lloc inici al final.
	private boolean rutaCurta; ///< Variable utilitzada per a la interfície gràfica.
	private boolean rutaBarata; ///< Variable utilitzada per a la interfície gràfica.
	private boolean rutaSaisfactoria; ///< Variable utilitzada per a la interfície gràfica.
	
	/**
	 * @pre paràmetres inicialitzats.
	 * @post S'ha creat una Comanda amb les dades passades per paràmetres.
	 * @param llistaClients És la llista de clients que faran el viatge.
	 * @param dataInici És la data en la que comença el viatge.
	 * @param horaInici És l'hora en la que comença el viatge.
	 * @param nDies És el número de dies que durarà el viatge.
	 * @param preuMaxim És el preu màxim que trindrà el viatge.
	 * @param categoriaAllotjaments És la categoria màxima que tindran els apartaments.
	 * @param inici Lloc on comença el viatge.
	 * @param fi Lloc on acaba el viatge.
	 * @param puntsIntermitjos Llocs per on passa la ruta entremig d'inici i de fi.
	 */
	public Comanda(Map<String, Client> llistaClients, LocalDate dataInici, LocalTime horaInici, int nDies, Float preuMaxim, String categoriaAllotjaments, Lloc inici, Lloc fi, Set<PuntInteres> puntsIntermitjos) {
		this.a_llistaClients = llistaClients;
		this.a_dataInici = dataInici;
		this.a_horaInici = horaInici;
		this.a_nDies = nDies;
		this.a_preuMaxim = preuMaxim;
		this.a_categoriaAllotjaments = categoriaAllotjaments;
		this.a_inici = inici;
		this.a_fi = fi;
		this.a_puntsIntermitjos = puntsIntermitjos;
	}
	
	/**
	 * @pre paràmetre inicialitzat.
	 * @post S'ha afegit un client a la llista de clients de la Comanda.
	 * @param a És el Client que es vol afegir a la Comanda.
	 */
    public void afegirClient(Client a){
		a_llistaClients.put(a.obtNom(),a);
    }
	
	/**
	 * @pre paràmetres inicialitzats.
	 * @post Afegeix un lloc ja visitat a la llista de llocs visitats d'un client.
	 * @param nomClient És el nomd el client que es busca.
	 * @param visita És la visita que es vol afegir a la llista de llocs visitats del client.
	 */
    public void actualitzaClient(String nomClient, Pair<String,LocalDate> visita) {
		if(a_llistaClients.containsKey(nomClient)){
			Client aux = a_llistaClients.get(nomClient);
			aux.afegirVisita(visita);
			a_llistaClients.put(nomClient,aux);
		}
		else{
			System.out.println("No existeix el client que es busca.");
		}
    }

	/**
	 * @pre a_dataInici i a_horaInici inicialitzades.
	 * @post Retorna data i hora d'inici del viatge.
	 * @return Retorna data i hora d'inici del viatge.
	 */
	public LocalDateTime obtDataHoraInici() {
    	return LocalDateTime.of(a_dataInici,a_horaInici);
	}
	
	/**
	 * @pre a_inici inicialitzada.
	 * @post Retorna Lloc on comença el viatge.
	 * @return Retorna Lloc on comença el viatge.
	 */
	public Lloc obtInici(){
    	return a_inici;
	}
	
	/**
	 * @pre a_fi inicialitzada.
	 * @post Retorna Lloc on acaba el viatge.
	 * @return Retorna Lloc on acaba el viatge.
	 */
	public Lloc obtFi(){
		return a_fi;
	}
	
	/**
	 * @pre cert
	 * @post Retorna cert si la Comanda demana ruta barata, satisfactoria o curta, respectivament, amb el caràcter passat per paràmetres. Altrement retorna fals.
	 * @param a Caràcter que es fa servir per fer referència 
	 * @return Retorna cert si la Comanda demana ruta barata, satisfactoria o curta, respectivament, amb el caràcter passat per paràmetres. Altrement retorna fals. 
	 */
	public boolean esVolRuta(char a){
		if(rutaBarata && a == 'b') return true;
		else if (rutaSaisfactoria && a == 's') return true;
		else if (rutaCurta && a == 'c') return true;
		return false;
	}
	
	/**
	 * @pre variables per paràmetres inicialitzades.
	 * @post Retorna el grau de satisfacció calculat a partir d'un Punt d'Interes i una Data, respecte la Comanda.
	 * @param nou És el punt d'interés per el qual es vol mirar el grau de satisfacció.
	 * @param actual És és la data amb la qual es vol mirar el grau de satisfacció.
	 * @return Retorna el grau de satisfacció calculat a partir d'un Punt d'Interes i una Data, respecte la Comanda.
	 */
	public float calculaSatisfaccio(PuntInteres nou, LocalDate actual) {
		float grSat = 0;
		Set<String> caracsLloc = nou.obtenirCaracteristiques();
		Iterator<Client> itClients = a_llistaClients.values().iterator();
		while(itClients.hasNext()){
			Client act = itClients.next();
			for(String i : caracsLloc){
				if(act.obtenirPreferencies().contains(i)){
					grSat++;
				}
			}
			Pair<Boolean,LocalDate> jaVisitat = act.jaVisitat(nou.obtNom());
			if(jaVisitat.primer){
				int anysPassats = (actual.getYear()-jaVisitat.segon.getYear());
				float disminSat = (float)-1/anysPassats;
				grSat += disminSat;
			}
		}
		return grSat;
    }
	
	/**
	 * @pre cert
	 * @post Retorna el preu màxim del viatge de la Comanda.
	 * @return Retorna el preu màxim del viatge de la Comanda.
	 */
	public float obtPreuMax() {
		return a_preuMaxim;
	}
	
	/**
	 * @pre cert
	 * @post Retorna la categoria desitjada dels allotjaments de la Comanda.
	 * @return Retorna la categoria desitjada dels allotjaments de la Comanda.
	 */
	public String obtCategoriaAllotjamentsDestijada(){
		return a_categoriaAllotjaments;
	}
	
	/**
	 * @pre cert
	 * @post Retorna la durada màxima en minuts, del viatge, de la Comanda.
	 * @return Retorna la durada màxima en minuts, del viatge, de la Comanda.
	 */
	public int obtDuradaMax() {
		return a_nDies*24*60;
	}
	
	/**
	 * @pre cert
	 * @post Retorna cert si el caràcter passat és una 'b', 's' o 'c'. Altrament fals.
	 * @return Retorna cert si el caràcter passat és una 'b', 's' o 'c'. Altrament fals.
	 */
	public void afegirRuta(char a) {
		if(a == 'b') rutaBarata = true;
		else if (a == 's') rutaSaisfactoria = true;
		else if (a == 'c') rutaCurta = true;
	}

	/**
	 * @pre cert
	 * @post Retorna un conjunt amb els punts intermitjos indicats per l'agència.
	 * @return Retorna un conjunt amb els punts intermitjos indicats per l'agència.
	 */
	public Set<PuntInteres> obtLlocsIntermitjos(){
		return a_puntsIntermitjos;
	}

	/**
	 * @pre n != 0
	 * @param n és es el nombre en què vols dividir la comanda, en el cas que s'hagi de calcular una ruta a trams.
	 * @post Divideix les restriccions de dies màxims i preu màxim entre el nombre que ens han passat per paràmetre.
	 * @return Retorna una nova comanda amb les restriccions modificades.
	 */
	protected Comanda segmentarComanda(int n){
	    return new Comanda(a_llistaClients , a_dataInici, a_horaInici, a_nDies/n, a_preuMaxim/n, a_categoriaAllotjaments, a_inici, a_fi, a_puntsIntermitjos);
    }
}