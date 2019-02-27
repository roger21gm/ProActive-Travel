import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * @class EstadaAllotjament
 * @brief És una estada a un Allotjament determinat.
 * @details És una estada a un Allotjament determinat amb una data d'Inici i fi, un preu i un grau de satisfacció segons els clients que fan l'estada.
 * @author Roger Generoso Masós
 * @version 2017.4.5
 */

public class EstadaAllotjament extends ItemRuta
{
    /**
     * @pre cert
     * @post Crea una estada a un Allotjament amb dades de paràmetres.
     * @param lloc Ha de ser un Allotjament.
     * @param inici És la data i hora d'entrada a l'Allotjament.
     * @param fi És la data i hora de sortida de l'Allotjament.
     * @param preu És el preu de l'estada.
     * @param grSat És el grau de satisfacció de l'estada.
     */
    EstadaAllotjament(PuntInteres lloc, LocalDateTime inici, LocalDateTime fi, float preu, float grSat) {
        super(lloc, inici, fi, preu, grSat);
    }

    @Override
    public String toString() {
        PuntInteres puntInteres = this.obtenirPuntInteres();
        ZoneId zonaHoraria = puntInteres.obtZonHoraria().toZoneId();
        LocalTime horaIni = this.obtDataInici().atZone(zonaHoraria).toLocalTime();
        LocalTime horaFi = this.obtDataFi().atZone(zonaHoraria).toLocalTime();
        return (horaIni+"-"+horaFi+" "+puntInteres);
    }
}