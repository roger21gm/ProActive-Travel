import java.time.LocalDateTime;
import java.util.Set;
import java.util.TimeZone;

/**
 * @class Allotjament
 * @brief És un PuntInteres en el que s'hi pot allotjar.
 * @details Conté informació sobre la categoria de l'allotjament i el preu de l'habitació doble.
 * @author Roger Generoso Masós
 * @version 2017.4.5
 */
public class Allotjament extends PuntInteres{

    private String categoria; ///< És la categoria de l'allotjament.
    private Float preuHabitacioDoble; ///< Preu en euros de l'habitació doble en l'allotjament actual.

    /**
     * @param nom És el nom de l'allotjament
     * @param latitud Latitud on es troba.
     * @param longitud Longitud on es troba.
     * @param zonaHoraria Zona horària on es troba.
     * @param cat És la categoria de l'allotjament.
     * @param preuHabDoble És el preu de l'habitació doble.
     * @param carac Llista de catacterístiques que compleixen les activitats que es duen a terme a l'allotjament.
     */
    Allotjament(String nom, Float latitud, Float longitud, TimeZone zonaHoraria, String cat, Float preuHabDoble, Set<String> carac){
        super(nom, latitud, longitud, zonaHoraria, carac, preuHabDoble);
        categoria = cat;
        preuHabitacioDoble = preuHabDoble;
    }

    /**
     * @pre cert
     * @post Retorna una Estancia a l'Allotjament en dataHoraActual i amb el grau de satsifaccio grSatis-
     * @param dataHoraActual És la hora d'entrada a l'Hotel
     * @param grSatis És el grau de satisfacció de l'estada a l'Hotel.
     * @return Retorna una Estancia a l'Allotjament en dataHoraActual i amb el grau de satsifaccio grSatis-
     */
    public EstadaAllotjament allotjarse(LocalDateTime dataHoraActual, float grSatis) {
        LocalDateTime dataFi = dataHoraActual.plusDays(1).withHour(4).withMinute(0);
        return new EstadaAllotjament(this,dataHoraActual,dataFi,preuHabitacioDoble, grSatis);
    }

    /**
     * @pre cert
     * @post Es retorna la categoria de l'Allotjament actual.
     * @return Es retorna la categoria de l'Allotjament actual.
     */
    public String obtCategoria(){
        return categoria;
    }

    @Override
    public int compareTo(PuntInteres puntInteres) {
        return nom.compareTo(puntInteres.obtNom());
    }
}