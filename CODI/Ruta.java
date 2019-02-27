import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @class Ruta
 * @brief És una Ruta d'un viatge de l'agència de viatges.
 * @details Una Ruta conté tots els detalls del nostre viatge i pot ser: Barata, Satisfactòria o Curta.
 * @author Adrià Orellana Cruañas
 * @version 2017.05.24
 */
public class Ruta{
	private List<ItemRuta> a_planificacio; ///< Llista d'ItemRuta ordenats que ens diuen el que haurem de fer en tot moment.
	private float a_preuTotal; ///< Preu total de la Ruta.
	private float a_grauSatisTotal; ///< Grau de satisfacció total de la Ruta.
	private LocalDateTime a_dataHoraActual; ///< Data i hora actuals.
	private LocalDateTime a_dataHoraInici; ///< Data i hora d'inici del viatge.
	private int a_minutsDuradaRuta; ///< Minuts totals que dura la Ruta.

	/**
	 * @pre cert
	 * @post S'ha creat una Ruta.
	 */
	Ruta(){
		a_planificacio= new ArrayList<ItemRuta>();
		a_preuTotal=0;
		a_grauSatisTotal=0;
		a_dataHoraActual=LocalDateTime.now();
		a_minutsDuradaRuta = 0;
	}

	/**
	 * @pre cert
	 * @post S'ha creat una Ruta amb les dades de rutaAct.
	 * @param rutaAct És la ruta que volem copiar.
	 */
	public Ruta(Ruta rutaAct) {
		a_planificacio = new ArrayList<>(rutaAct.a_planificacio);
		a_preuTotal = rutaAct.obtPreu();
		a_grauSatisTotal = rutaAct.a_grauSatisTotal;
		a_dataHoraActual = LocalDateTime.from(rutaAct.getDataHoraActual());
		a_dataHoraInici = LocalDateTime.from(rutaAct.getDataHoraIni());
		a_minutsDuradaRuta = rutaAct.a_minutsDuradaRuta;
	}

	/**
	 * @pre cert
	 * @post S'ha concatenat la Ruta actual amb la Ruta 'a'.
	 * @param a És la ruta que volem concatenar amb la Ruta actual.
	 * @return Retorna la ruta actual que ja estarà concatenada amb la Ruta 'a' passada per paràmetres.
	 */
	public void concatenaRuta(Ruta a){
		a_planificacio.addAll(a.a_planificacio);
		a_preuTotal=a_preuTotal+a.a_preuTotal;
		a_grauSatisTotal=a_grauSatisTotal+a.a_grauSatisTotal;
		a_dataHoraActual = obtUltimItem().obtDataFi();
	}

	/**
	 * @pre cert
	 * @post S'ha afegit un ItemRuta a la Ruta.
	 * @param a És l'ItemRuta que es vol afegir a la Ruta.
	 */
	public void afegirItemRuta(ItemRuta a){
		if(a_planificacio.isEmpty()){
			a_dataHoraActual=a.obtDataInici();
		}
		a_planificacio.add(a);
		a_preuTotal=a_preuTotal+a.obtPreu();
		a_dataHoraActual = a.obtDataFi();
		a_grauSatisTotal=a_grauSatisTotal+a.obtGrauSatis();
		a_minutsDuradaRuta += a.a_durada;
	}

	/**
	 * @pre cert
	 * @post Retorna la Data i Hores Actuals.
	 * @return Retorna la Data i Hores Actuals.
	 */
	public LocalDateTime getDataHoraActual(){
		return a_dataHoraActual;
	}

	/**
	 * @pre cert
	 * @post Actualitza la Data i Hora d'inici amb la Data i Hora actuals.
	 */
	public void assignarHoraIni(LocalDateTime horaAct) {
		a_dataHoraInici = LocalDateTime.from(horaAct);
		a_dataHoraActual = horaAct;
	}

	/**
	 * @pre cert
	 * @post Retorna l'últim lloc de la Ruta. Si la Ruta és buida retorna null.
	 * @return Retorna l'últim lloc de la Ruta. Si la Ruta és buida retorna null.
	 */
	public PuntInteres obtenirLlocAct() {
		if(a_planificacio.isEmpty()) return null;
		return a_planificacio.get(a_planificacio.size()-1).obtenirPuntInteres();
	}

	/**
	 * @pre cert
	 * @post Elimina el darrer Item de la llista a_planificacio.
	 */
	public void eliminarDarrerItem() {
		ItemRuta aEliminar = a_planificacio.remove(a_planificacio.size()-1);
		a_preuTotal -= aEliminar.obtPreu();
		if(a_planificacio.isEmpty()) a_dataHoraActual = aEliminar.obtDataInici();
		else a_dataHoraActual = a_planificacio.get(a_planificacio.size()-1).obtDataFi();
		a_minutsDuradaRuta -= aEliminar.a_durada;
		a_grauSatisTotal -= aEliminar.obtGrauSatis();
	}

	/**
	 * @pre cert
	 * @post Retorna les hores de Visita de l'últim dia.
	 * @return Retorna les hores de Visita de l'últim dia.
	 */
	public int horesVisitaUltimDia() {
		int minutsVisita = 0;
		int i = a_planificacio.size()-1;
		LocalDate diaActual = a_dataHoraActual.toLocalDate();
		while(i >= 0 && a_planificacio.get(i).obtDataFi().toLocalDate().equals(diaActual)){
			ItemRuta act = a_planificacio.get(i);
			if(act instanceof Visita){
				minutsVisita += act.obtDurada();
			}
			i--;
		}
		return minutsVisita/60;
	}

	/**
	 * @pre cert
	 * @post Retorna el preu total de la Ruta.
	 * @return Retorna el preu total de la Ruta.
	 */
	public float obtPreu() {
		return a_preuTotal;
	}

	/**
	 * @pre cert
	 * @post Retorna cert si el punt d'interés passat per paràmetres ja s'ha visitat. Altrament retorna fals.
	 * @param a És el punt d'interés que volem mirar si ja s'ha visitat.
	 * @return Retorna cert si el punt d'interés passat per paràmetres ja s'ha visitat. Altrament retorna fals.
	 */
	public boolean jaShaVisitat(PuntInteres a) {
		Iterator<ItemRuta> plan = a_planificacio.iterator();
		boolean trobat = false;
		while(plan.hasNext() && !trobat){
			ItemRuta act = plan.next();
			trobat = act instanceof Visita && act.obtenirPuntInteres().equals(a);
		}
		return trobat;
	}

	/**
	 * @pre cert
	 * @post Retorna l'últim item de la llista d'item Ruta.
	 * @return Retorna l'últim item de la llista d'item Ruta.
	 */
	public ItemRuta obtUltimItem(){
		if(!a_planificacio.isEmpty())
			return a_planificacio.get(a_planificacio.size()-1);
		else return null;
	}

	/**
	 * @pre cert
	 * @post Retorna un String on a més a més hi suma el preu total de la Ruta.
	 * @return Retorna un String on a més a més hi suma el preu total de la Ruta.
	 */
	@Override
	public String toString() {
		return a_planificacio.toString() + a_preuTotal;
	}

	/**
	 * @pre cert
	 * @post Mostra per pantalla tota la planificació.
	 */
	public void mostraPantalla(){
		System.out.println("Preu " + String.format("%.02f", a_preuTotal) + "€");
		System.out.println("Satisfaccio " + String.format("%.02f", a_grauSatisTotal));
		System.out.println(a_dataHoraInici.toLocalDate());
		Iterator<ItemRuta> items = a_planificacio.iterator();
		LocalDateTime actual = LocalDateTime.from(a_dataHoraInici);
		while(items.hasNext()){
			ItemRuta itemAct = items.next();
			if(!(itemAct instanceof EstadaAllotjament)){
				if(itemAct.obtDataInici().equals(actual)){
					System.out.println(itemAct);
				}else{
					System.out.println(actual.toLocalTime()+"-"+itemAct.obtDataInici().toLocalTime()+" Temps lliure");
					System.out.println(itemAct);
				}
			}
			if(!itemAct.obtDataFi().toLocalDate().equals(actual.toLocalDate())){
				System.out.println(itemAct.obtDataFi().toLocalDate());
			}
			actual = itemAct.obtDataFi();
		}
	}

	/**
	 * @pre cert
	 * @post Retorna cert si la planificació es buida.
	 * @return Retorna cert si la planificació es buida.
	 */
	public boolean buida() {
		return a_planificacio.isEmpty();
	}

	/**
	 * @pre cert
	 * @post Retorna el grau de satisfacció total de la Ruta.
	 * @return Retorna el grau de satisfacció total de la Ruta.
	 */
	public float obtGrauSatisfaccio() {
		return a_grauSatisTotal;
	}

	/**
	 * @pre cert
	 * @post Retorna la durada actual de la Ruta en minuts.
	 * @return Retorna la durada actual de la Ruta en minuts.
	 */
	public int obtDuradaAct() {
		return a_minutsDuradaRuta;
	}

	/**
	 * @pre cert
	 * @post Modifica l'arxiu passat per paràmetres i genera un arxiu KML per visualitzar la Ruta per Google Earth.
	 * @param arxiuDesti És l'arxiu que modificarem amb les dades de la Ruta.
	 */
	public void generarKML(File arxiuDesti) throws IOException {
		ItemRuta lloc;
		float longitud,latitud;
		String coordenades= "",nomZona = "";
		String coordenadesInici="";
		boolean primerCop=true;
		String aux="";
		Lloc zona;
		FileWriter arxiu;
		arxiu = new FileWriter(arxiuDesti);
		arxiu.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n <kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n <Folder id=\"results\">\n \t<name>C-66</name>\n \t<Style id= \"dir_step\">\n \t\t<IconStyle>\n \t\t\t<Icon>\n \t\t\t\t<href>http://maps.gstatic.com/mapfiles/kml/paddle/pause.png</href>\n \t\t\t</Icon>\n \t\t</IconStyle>\n \t\t<BalloonStyle>\n  \t\t\t<text><![CDATA[<div>$[description]</div><div>$[geDirections]</div>]]></text>\n \t\t\t<textColor>ff000000</textColor>\n \t\t\t<displayMode>default</displayMode>\n \t\t</BalloonStyle>\n \t</Style>\n\n \t<Folder id=\"1\">\n \t\t<Style id=\"dir_route\">\n \t\t\t<LineStyle>\n \t\t\t\t<color>7fa00000</color>\n \t\t\t\t<colorMode>normal</colorMode>\n \t\t\t\t<width>6</width>\n \t\t\t</LineStyle>\n \t\t</Style>\n\n \t\t<Placemark id=\"2\">\n \t\t\t<styleUrl>#dir_route</styleUrl>\n \t\t\t<LineString>\n \t\t\t\t<coordinates>\n  ");
		arxiu.flush();
		Iterator<ItemRuta> it = a_planificacio.iterator();
		while(it.hasNext()){
			lloc = it.next();
			zona=lloc.obtenirPuntInteres().obtDestinacio();
			if(zona!=null){
				nomZona=lloc.obtenirPuntInteres().obtDestinacio().obtNom();
				if(!nomZona.equals(aux)){
					longitud=lloc.obtenirPuntInteres().obtDestinacio().getLon();
					latitud=lloc.obtenirPuntInteres().obtDestinacio().getLat();
					coordenades=String.valueOf(longitud).concat(",").concat(String.valueOf(latitud));
					arxiu.write("\t\t\t\t\t"+coordenades+"\n");
					arxiu.flush();
					if(primerCop){
						coordenadesInici=coordenades;
						primerCop=false;
					}
					aux=nomZona;
				}
			}
			else{
				nomZona=lloc.obtenirPuntInteres().obtNom();
				if(!nomZona.equals(aux)){
					longitud=lloc.obtenirPuntInteres().getLon();
					latitud=lloc.obtenirPuntInteres().getLat();
					coordenades = String.valueOf(longitud).concat(",").concat(String.valueOf(latitud));
					arxiu.write("\t\t\t\t\t"+coordenades+"\n");
					arxiu.flush();
					if(primerCop){
						coordenadesInici=coordenades;
						primerCop=false;
					}
					aux=nomZona;
				}
			}
		}
		arxiu.write("\t\t\t\t</coordinates>\n \t\t\t</LineString>\n \t\t</Placemark>\n \t</Folder>\n \t<Placemark id=\"1.2\">\n \t\t<styleUrl>#dir_endpoint</styleUrl>\n \t\t<Point>\n \t\t\t<coordinates>");
		arxiu.flush();
		arxiu.write(coordenades);
		arxiu.write("</coordinates>\n \t\t</Point>\n \t</Placemark>\n \t<Placemark id=\"1.3\">\n \t\t<styleUrl>#dir_startpoint</styleUrl>\n \t\t<Point>\n \t\t\t<coordinates>");
		arxiu.write(coordenadesInici);
		arxiu.write("</coordinates>\n \t\t</Point>\n \t</Placemark>\n  </Folder>\n </kml>\n");
		arxiu.close();
	}

	/**
	 * @pre cert
	 * @post Retorna la Data i Hora d'inici de la Ruta.
	 * @return Retorna la Data i Hora d'inici de la Ruta.
	 */
	public LocalDateTime getDataHoraIni() {
		return a_dataHoraInici;
	}
}



