import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

/**
 * @class LlocVisitable
 * @brief És un PuntInteres amb diverses activitats.
 * @details Podem visitar-lo i coneixem les activitats que s'hi duen a terme. També sabem seu temps de visita estimat i el preu de la visita.
 * @author Roger Generoso Masós
 * @version 2017.4.5
 */

public class LlocVisitable extends PuntInteres{
    private int duradaVisita; ///< És la durada de la visita en minuts.
    private float preuVisita; ///< És el preu de realitzar la visita.
    private Horari horariObertura; ///<Horari d'obertura del LlocVisitable (s'hi poden fer visites).

    /**
     * @pre Cert
     * @post Crea un nou lloc de pas, només s'hi pot passar però no es pot visitar.
     * @param orig És el lloc amb la informació bàsica del lloc de pas.
     */
    LlocVisitable(Lloc orig){
        super(orig.nom, orig.latitud, orig.longitud, orig.zonaHoraria);
        duradaVisita = Integer.MAX_VALUE;
        preuVisita = Integer.MAX_VALUE;
        horariObertura = new Horari(); //No te horaris de visita. Horari buit.
    }

    /**
     * @pre cert
     * @post Crea un LlocVisitable amb un preu, característiques i horari determinat.
     * @param nom Nom del lloc
     * @param latitud Latitud del lloc
     * @param longitud Longitud del lloc
     * @param zonaHoraria Zona horària del lloc
     * @param tempsVisitaRecomanat Temps de visita
     * @param preu Preu de visita.
     * @param carac Característiques del lloc.
     * @param nouHorari Horari d'obertura.
     */
    LlocVisitable(String nom, Float latitud, Float longitud, TimeZone zonaHoraria, int tempsVisitaRecomanat, Float preu, Set<String> carac, Horari nouHorari) {
        super(nom, latitud, longitud, zonaHoraria, carac, preu);
        duradaVisita = tempsVisitaRecomanat;
        preuVisita = preu;
        horariObertura = nouHorari;
        horariObertura.assignarDuradaVisita(duradaVisita);
    }

    /**
     * @pre cert
     * @post Programa una visita en la dataHora indicada si és possible. Altrament la programa el més aviat possible després de la dataHora.
     * @param dataHora és la dataHora en que es vol programar la visita.
     * @param grauSatisfaccio és el grau de satisfacció de la visita segons qui la visita.
     */
    public Visita programarVisita(LocalDateTime dataHora, float grauSatisfaccio){
        if(horariObertura.esVisitable(dataHora)){
            return new Visita(this,dataHora,dataHora.plusMinutes(duradaVisita),preuVisita, grauSatisfaccio);
        }else{
            LocalDateTime primeraHoraDisponible = horariObertura.primeraHoraDisponibleDespresDe(dataHora);
            if(primeraHoraDisponible != null)
                return new Visita(this,primeraHoraDisponible,primeraHoraDisponible.plusMinutes(duradaVisita),preuVisita, grauSatisfaccio);
        }
        return null;

    }

    @Override
    public int compareTo(PuntInteres puntInteres) {
        return nom.compareTo(puntInteres.obtNom());
    }
}

