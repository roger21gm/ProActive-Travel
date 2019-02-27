import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.util.*;

/**
 * @class Horari
 * @brief És un contenidor amb els horaris d'obertura d'un determinat LlocVisitable
 * @details Sabem els dies de l'any que obre i l'horari que obre. A més també coneixem les excepcions.
 * @author Roger Generoso Masós
 * @version 2017.4.5
 */
public class Horari {
    private Map< Pair<MonthDay,MonthDay>, Pair<LocalTime,LocalTime>> periodesObertura; ///< Conté els períodes regulars d'obertura
    private Map< MonthDay, Pair<LocalTime, LocalTime> > excepcions; ///< Conté les excepcions d'obertures durant l'any.
    private int duradaVisitaRegular;
    private Pair<LocalTime, LocalTime> horaProhibidaAgencia;

    /**
     * @pre Cert
     * @post Crea un horari buit. Sempre està tancat.
     */
    Horari(){  
        periodesObertura = new HashMap<>();
        excepcions = new HashMap<>();
        duradaVisitaRegular = Integer.MAX_VALUE;
        horaProhibidaAgencia = new Pair<>(LocalTime.of(12,0), LocalTime.of(14,0));
    }

    /**
     * @pre cert
     * @post S'ha assignat una duradaVisita a l'horari de visites actual.
     * @param duradaVisita És el temps de duracio de la visita de l'horari de visites.
     */
    public void assignarDuradaVisita(int duradaVisita) {
        duradaVisitaRegular = duradaVisita;
    }

    /**
     * @pre ini i fi no ha de ser un període que ja pertanyi a l'horari d'obertura
     * @post S'ha afegit l'horari d'obertura de horaIni a horaFi pel període de ini a fi.
     * @param ini És el mes i el dia d'inici del període.
     * @param fi És el mes i el dia de final del període.
     * @param horaIni És la hora d'obertura durant el període.
     * @param horaFi És la hora de tancament duarnt el període.
     */
    void afegirAHorariObertura(MonthDay ini, MonthDay fi, LocalTime horaIni, LocalTime horaFi){
        Pair<MonthDay,MonthDay> nouPeriode = new Pair<MonthDay,MonthDay>(ini, fi);
        Pair<LocalTime,LocalTime> nouHorariObertura = new Pair<LocalTime,LocalTime>(horaIni, horaFi);
        periodesObertura.put(nouPeriode,nouHorariObertura);
    }

    /**
     * @pre Cert
     * @post Es crea una excepció pel dia amb un horari d'obertura entre horaIni i horaFi.
     * @param dia És el dia de l'excepció
     * @param horaIni Hora d'obertura el dia de l'excepció
     * @param horaFi Hora de tancament el dia de l'excepció.
     */
    void afegirExcepio(MonthDay dia, LocalTime horaIni, LocalTime horaFi){
        Pair<LocalTime,LocalTime> horariExecep = new Pair<LocalTime,LocalTime>(horaIni, horaFi);
        excepcions.put(dia, horariExecep);
    }


    /**
     * @pre Cert
     * @post retorna cert si segons l'horari establert, és visitable en aquest dia i hora.
     * @param dataHora És el dia i hora a comprovar.
     * @return retorna cert si segons l'horari establert, és visitable en aquest dia i hora.
     */
    public boolean esVisitable(LocalDateTime dataHora) {
        MonthDay mesDia = MonthDay.from(dataHora);
        LocalTime horaAct = LocalTime.from(dataHora);
        if(seraEnHoraProhibida(horaAct)) return false;
        else{
            if(esExcepcio(mesDia)){
                return esPotVisitarDiaExcepcio(dataHora);
            }
            Pair<MonthDay, MonthDay> periodeAct = obtPeriodePerData(mesDia);
            if(periodeAct != null){
                Pair<LocalTime, LocalTime> horariAct = periodesObertura.get(periodeAct);
                return esPotVisitarEnAquestaHora(horariAct, horaAct);
            }else
                return false;
        }
    }

    /**
     * @pre dataHora és un dia d'excepció
     * @post Retorna cert si es pot visitar en aquest dia i hora dataHora.
     * @param dataHora És el dia i hora a comprovar.
     * @return Retorna cert si es pot visitar en aquest dia i hora dataHora.
     */
    private boolean esPotVisitarDiaExcepcio(LocalDateTime dataHora) {
        MonthDay mesDia = MonthDay.from(dataHora);
        LocalTime horaAct = LocalTime.from(dataHora);
        Pair<LocalTime, LocalTime> periode = excepcions.get(mesDia);
        return esPotVisitarEnAquestaHora(periode, horaAct);
    }

    /**
     * @pre cert
     * @post Retorna el període de l'any, segons els horaris, corresponent a el dia i mes a. Retorna null si no existeix.
     * @param a És el mes i dia a comprovar-ne el períde.
     * @return Retorna el període de l'any, segons els horaris, corresponent a el dia i mes a. Retorna null si no existeix.
     */
    private Pair<MonthDay,MonthDay> obtPeriodePerData(MonthDay a){
        Iterator<Pair<MonthDay,MonthDay>> it = periodesObertura.keySet().iterator();
        while(it.hasNext()){
            Pair<MonthDay,MonthDay> periode = it.next();
            MonthDay iniPer = periode.primer;
            MonthDay fiPer = periode.segon;
            if(a.isAfter(iniPer) && a.isBefore(fiPer) || a.equals(iniPer) || a.equals(fiPer))
                return periode;
        }
        return null;
    }

    /**
     * @pre cert
     * @post Retorna cert si el mesDia és una excepció a l'horari.
     * @param mesDia És el dia a comprovar.
     * @return Retorna cert si el mesDia és una excepció a l'horari.
     */
    private boolean esExcepcio(MonthDay mesDia) {
        return excepcions.get(mesDia) != null;
    }

    /**
     * @pre cert
     * @post Retorna cert si es pot visitar en hora x en el periode.
     * @param periode És el període a consultar.
     * @param x És la hora a visitar.
     * @return Retorna cert si es pot visitar en hora x en el periode.
     */
    private boolean esPotVisitarEnAquestaHora(Pair<LocalTime,LocalTime> periode, LocalTime x){
        return x.isAfter(periode.primer) && x.isBefore(periode.segon) && x.plusMinutes(duradaVisitaRegular).isBefore(periode.segon) || x.equals(periode.primer);
    }

    /**
     * @pre cert
     * @post Retorna cert si la visita s'efectuaria en hora prohibida per l'agència si es visités en horaAct.
     * @param horaAct És la hora d'inici de la visita.
     * @return Retorna cert si la visita s'efectuaria en hora prohibida per l'agència si es visités en horaAct.
     */
    private boolean seraEnHoraProhibida(LocalTime horaAct) {
        LocalTime horaFi = horaAct.plusMinutes(duradaVisitaRegular);
        boolean c1 = horaFi.equals(horaProhibidaAgencia.primer);
        boolean c2 = horaFi.isAfter(horaProhibidaAgencia.primer);
        boolean c3 = horaFi.isBefore(horaProhibidaAgencia.segon);
        return !c1 && c2 && c3;
    }

    /**
     * @pre Cert
     * @post Retorna la primera hora de visita disponible després de x. En cas que hagi de canviar de dia, retorna null.
     * @param x És la hora de referècnia.
     * @return Retorna la primera hora de visita disponible després de x. En cas que hagi de canviar de dia, retorna null.
     */
    public LocalDateTime primeraHoraDisponibleDespresDe(LocalDateTime x){
        MonthDay mesDia = MonthDay.from(x);
        LocalTime horaAct = LocalTime.from(x);
        Pair<LocalTime, LocalTime> periodeObertura;
        if(!esExcepcio(mesDia)){
            Pair<MonthDay, MonthDay> periode = obtPeriodePerData(mesDia);
            periodeObertura = periodesObertura.get(periode);
        }else{
            periodeObertura = excepcions.get(mesDia);
        }
        if(periodeObertura != null) {
            if (seraEnHoraProhibida(horaAct)) {
                return LocalDateTime.of(mesDia.atYear(x.getYear()), horaProhibidaAgencia.segon);
            } else if (horaAct.isBefore(periodeObertura.primer)) {
                return LocalDateTime.of(mesDia.atYear(x.getYear()), periodeObertura.primer);
            }
        }
        return null;
    }
}
