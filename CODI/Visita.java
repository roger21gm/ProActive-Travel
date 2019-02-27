import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * @class Visita
 * @brief És una visita a un LlocVisitable determinat.
 * @details En sabem el LlocVisitable, la data i hora d'inici i fi, el preu i el grau de satisfacció segons els clients que la fan.
 * @author Roger Generoso Masós
 * @version 2017.4.5
 */
public class Visita extends ItemRuta{

    /**
     * @pre cert
     * @post Crea una visita en un lloc, amb una data d'inici i fi, preu i grau satisfacció segons clients.
     * @param lloc És el PuntInteres que es visita.
     * @param inici És la data i l'hora d'inici de la Visita.
     * @param fi És la data i l'hora de fi de la Visita.
     * @param preu És el preu de la visita.
     * @param grSat És el grau de satisfacció de la visita.
     */
    Visita(PuntInteres lloc, LocalDateTime inici, LocalDateTime fi, float preu, float grSat) {
        super(lloc, inici, fi, preu, grSat);
    }

    /**
     * @return retorna String de la visita en format "horaini-horaFi PuntInteresVisitat"
     */
    @Override
    public String toString() {
        PuntInteres puntInteres = this.obtenirPuntInteres();
        ZoneId zonaHoraria = puntInteres.obtZonHoraria().toZoneId();
        LocalTime horaIni = this.obtDataInici().atZone(zonaHoraria).toLocalTime();
        LocalTime horaFi = this.obtDataFi().atZone(zonaHoraria).toLocalTime();
        return (horaIni+"-"+horaFi+" "+puntInteres);
    }
}