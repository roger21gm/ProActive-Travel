import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * @class Desplacament
 * @brief Un trasllat entre dos llocs existents.
 * @details Utilitza un mitjà de transport determinat, que triga un temps en desplaçar-se i es fa en un dia determinat (YYYY-MM-DD).
 * @author Amat Martínez Vilà
 * @version 2017.4.5
 */
public class Desplacament extends ItemRuta {
	private PuntInteres origen;
	private PuntInteres desti;
	private LocalDateTime horaArribada;
	private LocalDateTime horaSortida;
	private String mitja;

	public Desplacament(PuntInteres origen, LocalDateTime horaSortida, TransportDirecte m, PuntInteres desti){
		super(desti,horaSortida,horaSortida.plusMinutes(m.obtDurada()), m.obtPreu(), 0);
		this.origen = origen;
		this.horaSortida = horaSortida;
		this.mitja = m.obtNom();
		this.horaArribada = horaSortida.plusMinutes(m.obtDurada());
		this.desti = desti;
	}

	public Desplacament(PuntInteres origen, LocalDateTime horaSortida, TransportIndirecte m, PuntInteres desti){
		super(desti,horaSortida,horaSortida.plusMinutes(m.obtDurada()+m.obtTempsDesDestacio()+origen.obtDestinacio().obtTempsFinsEstacio()), m.obtPreu(), 0);
		this.origen = origen;
		this.horaSortida = horaSortida;
		this.mitja = m.obtNom();
		int duradaTotal = m.obtDurada()+m.obtTempsDesDestacio()+origen.obtDestinacio().obtTempsFinsEstacio();
		this.horaArribada = horaSortida.plusMinutes(duradaTotal);
		this.desti = desti;
	}

	public Desplacament(PuntInteres origen, LocalDateTime horaSortida, TransportPublic m, PuntInteres desti){
		super(desti,horaSortida,horaSortida.plusMinutes(m.obtDurada()), m.obtPreu(), 0);
		this.origen = origen;
		this.horaSortida = horaSortida;
		this.mitja = m.obtNomMitja();
		this.horaArribada = horaSortida.plusMinutes(m.obtDurada());
		this.desti = desti;
	}

	/**
     * @pre cert
     * @post Retorna el punt de interès d'origen del desplaçament.
     * @return Retorna el punt de interès d'origen del desplaçament.
     */
	public PuntInteres obtOrigen(){
		return origen;
	}

	/**
     * @pre cert
     * @post Redefineix el toString de Object.
     * @return Retorna un string que conté tota la informació del Desplaçament.
     */
	@Override
	public String toString() {
		ZoneId zonaHorariaIni = origen.obtZonHoraria().toZoneId();
		ZoneId zonaHorariaDest = desti.obtZonHoraria().toZoneId();
		LocalTime horaIni = horaSortida.atZone(zonaHorariaIni).toLocalTime();
		LocalTime horaFi = horaArribada.atZone(zonaHorariaDest).toLocalTime();
		return (horaIni+"-"+horaFi+" "+origen+" -> "+desti+" ("+ mitja+")");
	}
}