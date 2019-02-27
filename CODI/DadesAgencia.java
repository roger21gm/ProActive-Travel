import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @class DadesAgencia
 * @brief Conté les dades dels llocs on l'agència ofereix viatges. A més de les dades dels Clients.
 * @details Objecte contenidor de Destinacions i puntInteres amb totes les seves característiques i infomracions pertinents.
 *          També coneix la llista de Clients de l'agència i la comanda (encàrrec) a realitzar.
 * @author Roger Generoso Masós
 * @version 2017.4.5
 */
public class DadesAgencia{

    private Scanner fitxerDades; ///< És el fitxer contenidor de les dades.
    private Map<String, Destinacio> destinacions; ///< Mapa de destinacions que disposa l'agència.
    private Map<String, Client> clients; ///< Mapa de clients de l'agència
    private Map<String, PuntInteres> puntsInteres; ///< Mapa de PuntInteres que té l'agència.
    private Comanda comanda;

    /**
     * @pre cert
     * @post S'han creat unes DadesAgencia buides.
     */
    DadesAgencia(){
        destinacions = new HashMap<>();
        puntsInteres = new HashMap<>();
        clients = new HashMap<>();
    }

    /**
     * @pre Cert
     * @post S'han actualitzat les DadesAgencia amb les dades del fitxer amb nomFitxerDades.
     * @param FfitxerDades És el fitxer amb les dades.
     */
    public void llegirDadesFitxer(File FfitxerDades) throws FileNotFoundException {
        fitxerDades = new Scanner(FfitxerDades);
        String linAct = fitxerDades.nextLine(); //Ens saltem el comentari.
        Locale.setDefault(Locale.US);
        while(fitxerDades.hasNextLine()){
            try{
                linAct = fitxerDades.nextLine();
                if(linAct.equals("client")) crearClient();
                else if(linAct.equals("lloc")) crearLloc();
                else if(linAct.equals("allotjament")) crearAllotjament();
                else if(linAct.equals("lloc visitable")) crearLlocVisitable();
                else if(linAct.equals("visita")) crearVisita();
                else if(linAct.equals("associar lloc")) associarALloc();
                else if(linAct.equals("associar transport")) associarTransportPublic();
                else if(linAct.equals("transport directe")) altaTransDirecte();
                else if(linAct.equals("transport indirecte")) altaTransIndirecte();
                else if(linAct.equals("viatge")) crearComanda();
            }
            catch(Excepcions.jaExistiaLloc a){
                System.err.println("El lloc " + a.getMessage() + " ja existia a l'agència.");
            }catch (Excepcions.jaExistiaClient a) {
                System.err.println("El client " + a.getMessage() + " ja existia a l'agència.");
            } catch (Excepcions.jaExistiaPuntInteres a) {
                System.err.println("El PuntInteres " + a.getMessage() + " ja existia a l'agència.");
            } catch (Excepcions.noExisteixClient a) {
                System.err.println("No existeix el client " + a.getMessage() + " a les dades de l'agència.");
            } catch (Excepcions.noExisteixDestinacio a) {
                System.err.println("No existeix la destinació " + a.getMessage() + " a les dades de l'agència.");
            } catch (Excepcions.noExisteixPuntInteres a) {
                System.err.println("No existeix el Punt d'Interès " + a.getMessage() + " a les dades de l'agència.");
            }
        }
        assignarLlocsDePas();
    }

    /**
     * @pre cert
     * @post s'ha creat un client i s'ha guardat a les DadesAgencia actuals.
     * @throws Excepcions.jaExistiaClient Si ja existia client llança excepció.
     */
    private void crearClient() throws Excepcions.jaExistiaClient {
        List<String> preferencies = new ArrayList<>();
        String nom = fitxerDades.nextLine();
        String prefAct = fitxerDades.nextLine();
        while(!esSeparador(prefAct) && fitxerDades.hasNextLine()){
            preferencies.add(prefAct);
            prefAct =  fitxerDades.nextLine();
        }
        if(!clients.containsKey(nom)){
            Client nou = new Client(nom, preferencies);
            clients.put(nom,nou);
        }else{
            throw new Excepcions.jaExistiaClient(nom);
        }
    }

    /**
     * @pre cert
     * @post s'ha creat un Lloc i s'ha guardat a les DadesAgencia actuals.
     * @throws Excepcions.jaExistiaLloc Si el client ja existia a les Dades.
     */
    private void crearLloc() throws Excepcions.jaExistiaLloc {
        String nom = fitxerDades.nextLine();
        String strCoord = fitxerDades.nextLine(); String[] coordVec = strCoord.split(",");
        Float latitud = Float.parseFloat(coordVec[0]);
        Float longitud = Float.parseFloat(coordVec[1]);
        String zonaHoraria = fitxerDades.nextLine();
        TimeZone zH = TimeZone.getTimeZone(zonaHoraria);
        fitxerDades.nextLine(); //Saltem separador.
        if(!destinacions.containsKey(nom)){
            Destinacio nou = new Destinacio(nom, latitud, longitud, zH);
            destinacions.put(nom, nou);
        }else{
            throw new Excepcions.jaExistiaLloc(nom);
        }
    }

    /**
     * @pre cert
     * @post s'ha creat un Allotjament i s'ha guardat a les DadesAgencia actuals.
     * @throws Excepcions.jaExistiaPuntInteres Si el PuntInteres ja exisita.
     */
    private void crearAllotjament() throws Excepcions.jaExistiaPuntInteres {
        String nom = fitxerDades.nextLine();
        String strCoord = fitxerDades.nextLine(); String[] coordVec = strCoord.split(",");
        Float latitud = Float.parseFloat(coordVec[0]);
        Float longitud = Float.parseFloat(coordVec[1]);
        String zonaHoraria = fitxerDades.nextLine();
        TimeZone zH = TimeZone.getTimeZone(zonaHoraria);
        String categoria = fitxerDades.nextLine();
        Float preuHabDoble = Float.parseFloat(fitxerDades.nextLine());
        Set<String> caracteristiques = new HashSet<>();
        String caracAct = fitxerDades.nextLine();
        while(!esSeparador(caracAct) && fitxerDades.hasNextLine()){
            caracteristiques.add(caracAct);
            caracAct = fitxerDades.nextLine();
        }
        if(!puntsInteres.containsKey(nom)){
            PuntInteres nou = new Allotjament(nom, latitud, longitud, zH, categoria, preuHabDoble, caracteristiques);
            puntsInteres.put(nom, nou);
        }else{
            throw new Excepcions.jaExistiaPuntInteres(nom);
        }

    }

    /**
     * @pre cert
     * @post s'ha creat un LlocVisitable i s'ha guardat a les DadesAgencia actuals.
     * @throws Excepcions.jaExistiaPuntInteres Si ja existia el PuntInteres.
     */
    private void crearLlocVisitable() throws Excepcions.jaExistiaPuntInteres {
        String nom = fitxerDades.nextLine();
        String strCoord = fitxerDades.nextLine(); String[] coordVec = strCoord.split(",");
        Float latitud = Float.parseFloat(coordVec[0]);
        Float longitud = Float.parseFloat(coordVec[1]);
        String zonaHoraria = fitxerDades.nextLine();
        TimeZone zH = TimeZone.getTimeZone(zonaHoraria);
        LocalTime tempsVisitaRecomanat = LocalTime.parse(fitxerDades.nextLine());
        Float preu = Float.parseFloat(fitxerDades.nextLine());
        Set<String> caracteristiques = new HashSet<>();
        String caracAct = fitxerDades.nextLine();
        while(!esSeparador(caracAct) && fitxerDades.hasNextLine()){
            caracteristiques.add(caracAct);
            caracAct = fitxerDades.nextLine();
        }
        Locale.setDefault(Locale.US);
        DateTimeFormatter formatData = DateTimeFormatter.ofPattern("MMMM d");
        Horari nouHorari = new Horari();
        String linAct[] = fitxerDades.nextLine().split(": ");
        while(!esSeparador(linAct[0]) && linAct.length == 2 && linAct[0].contains(" - ")){
            String[] periodeAct = linAct[0].split(" - ");
            String[] horariAct = linAct[1].split("-");
            LocalTime horaObertura = LocalTime.parse(horariAct[0]);
            LocalTime horaTancament = LocalTime.parse(horariAct[1]);
            MonthDay dataIni = MonthDay.parse(periodeAct[0], formatData);
            MonthDay dataFi = MonthDay.parse(periodeAct[1], formatData);
            linAct = fitxerDades.nextLine().split(": ");
            nouHorari.afegirAHorariObertura(dataIni,dataFi,horaObertura,horaTancament);
        }
        while(!esSeparador(linAct[0])){
            MonthDay diaExepcio = MonthDay.parse(linAct[0],formatData);
            String[] horariAct = linAct[1].split("-");
            LocalTime horaObertura = LocalTime.parse(horariAct[0]);
            LocalTime horaTancament = LocalTime.parse(horariAct[1]);
            linAct = fitxerDades.nextLine().split(": ");
            nouHorari.afegirExcepio(diaExepcio,horaObertura,horaTancament);
        }
        if(!puntsInteres.containsKey(nom)){
            PuntInteres nou = new LlocVisitable(nom, latitud, longitud, zH, transformaAMinuts(tempsVisitaRecomanat), preu, caracteristiques, nouHorari);
            puntsInteres.put(nom, nou);
        }else{
            throw new Excepcions.jaExistiaPuntInteres(nom);
        }
    }

    /**
     * @pre cert
     * @post s'ha actualitzat les dades d'un client amb una visita prèvia a algun Lloc.
     * @throws Excepcions.noExisteixClient Si no existia el client.
     */
    private void crearVisita() throws Excepcions.noExisteixClient {
        String nom = fitxerDades.nextLine();
        String llocVisitat = fitxerDades.nextLine();
        String dataStr = fitxerDades.nextLine();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate data = LocalDate.parse(dataStr,format);
        if(clients.containsKey(nom)){
            clients.get(nom).afegirVisita(new Pair<String,LocalDate>(llocVisitat,data));
            fitxerDades.nextLine();
        }else{
            throw new Excepcions.noExisteixClient(nom);
        }
    }


    /**
     * @pre cert
     * @post S'ha associat un PuntInteres a una Destinacio determinada.
     * @throws Excepcions.noExisteixPuntInteres Si no existeix PuntInteres
     * @throws Excepcions.noExisteixDestinacio Si no existeix Destinacio.
     */
    private void associarALloc() throws Excepcions.noExisteixPuntInteres, Excepcions.noExisteixDestinacio {
        String nomLlocVisitable = fitxerDades.nextLine();
        String nomLloc = fitxerDades.nextLine();
        fitxerDades.nextLine();
        Destinacio llocActual = destinacions.get(nomLloc);
        PuntInteres pIActual = puntsInteres.get(nomLlocVisitable);
        if(pIActual != null){
            if(llocActual != null){
                llocActual.afegirPuntInteres(nomLlocVisitable, pIActual);
                pIActual.associarADestinacio(llocActual);
            }else{
                throw new Excepcions.noExisteixDestinacio(nomLloc);
            }
        }else{
            throw new Excepcions.noExisteixPuntInteres(nomLlocVisitable);
        }
    }

    /**
     * @pre cert
     * @post S'ha associat un TransportPublic a una Destinacio.
     * @throws Excepcions.noExisteixDestinacio Si no existeix la Destinacio.
     */
    private void associarTransportPublic() throws Excepcions.noExisteixDestinacio {
        String nomLloc = fitxerDades.nextLine();
        String nomMitjaTransp = fitxerDades.nextLine();
        LocalTime duradaTrajecte = LocalTime.parse(fitxerDades.nextLine());
        Float preu = Float.parseFloat(fitxerDades.nextLine());
        Destinacio act = destinacions.get(nomLloc);
        if(act != null){
            TransportPublic nou = new TransportPublic(nomMitjaTransp, transformaAMinuts(duradaTrajecte), preu);
            act.assignarTransportPublic(nou);
            fitxerDades.nextLine();
        }else{
            fitxerDades.nextLine();
            throw new Excepcions.noExisteixDestinacio(nomLloc);
        }

    }

    /**
     * @pre cert
     * @post S'ha donat d'alta un TransportDirecte entre dos PuntInteres.
     * @throws Excepcions.noExisteixPuntInteres Si no existeix el PuntInteres.
     */
    private void altaTransDirecte() throws Excepcions.noExisteixPuntInteres {
        String origen = fitxerDades.nextLine();
        String desti = fitxerDades.nextLine();
        String nomTransport = fitxerDades.nextLine();
        LocalTime durada = LocalTime.parse(fitxerDades.nextLine());
        Float preu = Float.parseFloat(fitxerDades.nextLine());
        PuntInteres PIDesti = puntsInteres.get(desti);
        if(PIDesti!=null){
            TransportDirecte nou = new TransportDirecte(nomTransport, PIDesti, transformaAMinuts(durada), preu);
            PuntInteres orig = puntsInteres.get(origen);
            if(orig != null){
                orig.afegirConnexioDirecta(puntsInteres.get(desti), nou);
            }else{
                throw new Excepcions.noExisteixPuntInteres(origen);
            }
        }else{
            throw new Excepcions.noExisteixPuntInteres(desti);
        }
    }

    /**
     * @pre cert
     * @post S'ha donat d'alta un TransportIndirecte entre dos PuntInteres.
     * @throws Excepcions.noExisteixDestinacio Si no existeix la Destinacio.
     */
    private void altaTransIndirecte() throws Excepcions.noExisteixDestinacio {
        String origen = fitxerDades.nextLine();
        String desti = fitxerDades.nextLine();
        String nomTransport = fitxerDades.nextLine();
        LocalTime tempsOrigenFinsMitja = LocalTime.parse(fitxerDades.nextLine());
        LocalTime tempsDesDeMitjaADesti = LocalTime.parse(fitxerDades.nextLine());
        String linAct = fitxerDades.nextLine();
        Destinacio orig = destinacions.get(origen);
        if(orig != null){
            while(!esSeparador(linAct)){
                LocalDate data = LocalDate.parse(linAct);
                linAct = fitxerDades.nextLine();
                while(!esSeparador(linAct) && linAct.split("-").length != 3){
                    LocalTime hora = LocalTime.parse(linAct);
                    LocalTime durada = LocalTime.parse(fitxerDades.nextLine());
                    Float preu = Float.parseFloat(fitxerDades.nextLine());
                    if(destinacions.containsKey(desti)){
                        TransportIndirecte nouTrans = new TransportIndirecte(nomTransport,destinacions.get(desti),transformaAMinuts(durada),preu,data.atTime(hora),transformaAMinuts(tempsDesDeMitjaADesti));
                        orig.afegirNovaSortidaIndirecta(data, nouTrans, transformaAMinuts(tempsOrigenFinsMitja));
                        linAct = fitxerDades.nextLine();
                    }else{
                        throw new Excepcions.noExisteixDestinacio(desti);
                    }
                }
            }
        }else{

        }
    }


    /**
     * @pre cert
     * @post Crea una comanda amb diversos requeriments.
     */
    private void crearComanda(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dataIni = LocalDate.parse(fitxerDades.nextLine(),format);
        LocalTime horaIni = LocalTime.parse(fitxerDades.nextLine());
        int nDies = Integer.parseInt(fitxerDades.nextLine());
        float preuMax = Float.parseFloat(fitxerDades.nextLine());
        String catAllotj = fitxerDades.nextLine();
        Map<String, Client> clientsParticipants = new HashMap<>();
        String cliAct = fitxerDades.nextLine();
        while(!esSeparador(cliAct)){
            clientsParticipants.put(cliAct, clients.get(cliAct));
            cliAct = fitxerDades.nextLine();
        }
        String llocInici = fitxerDades.nextLine();
        Set<PuntInteres> llocsInt = new HashSet<>();
        String llocIntAct = fitxerDades.nextLine();
        String ultLlocEntrat = llocIntAct;
        while(!esSeparador(llocIntAct)){
            llocsInt.add(puntsInteres.get(llocIntAct));
            ultLlocEntrat = llocIntAct;
            llocIntAct = fitxerDades.nextLine();
        }
        llocsInt.remove(puntsInteres.get(ultLlocEntrat));
        String llocFi = ultLlocEntrat;
        comanda = new Comanda(clientsParticipants,dataIni,horaIni,nDies,preuMax,catAllotj,puntsInteres.get(llocInici),puntsInteres.get(llocFi),llocsInt);
        String rutaACalcularAct = fitxerDades.nextLine();
        while(!esSeparador(rutaACalcularAct)){
            if(rutaACalcularAct.equals("ruta barata")) comanda.afegirRuta('b');
            else if(rutaACalcularAct.equals("ruta satisfactoria")) comanda.afegirRuta('s');
            else if(rutaACalcularAct.equals("ruta curta")) comanda.afegirRuta('c');
            rutaACalcularAct = fitxerDades.nextLine();
        }
    }

    /**
     * @pre Cert
     * @post Es transforma x a minuts (Tractant el LocalTime com període de temps i no com una hora.)
     * @param x És el període de temps en qüestió.
     * @return Retrona els minuts del període.
     */
    private int transformaAMinuts(LocalTime x){
        LocalTime init = LocalTime.MIDNIGHT;
        int minuts = (int) Duration.between(init, x).toMinutes();
        return minuts;
    }

    /**
     * @pre Cert
     * @post Retorna cert si a és "*"
     * @param a Cadena de caràcters a evaluar.
     * @return Retorna cert si a és "*"
     */
    private boolean esSeparador(String a){
        return a.equals("*");
    }

    /**
     * @pre cert
     * @post Per cada Destinació que no te cap PuntInteres associat el reconvertim a Lloc de Pas. (S'hi podrà passar però no visitar-lo)
     */
    private void assignarLlocsDePas(){
        Set<Map.Entry<String,Destinacio>> dest = destinacions.entrySet();
        List<String> aEsborrar = new ArrayList<>();
        for(Map.Entry<String, Destinacio> i : dest){
            Destinacio act = i.getValue();
            if(act.nPuntsInteres() == 0){ //Creem un lloc de pas
                PuntInteres nouLLP = new LlocVisitable((Lloc)act);
                aEsborrar.add(i.getKey());
                puntsInteres.put(i.getKey(), nouLLP);
            }
        }
        Iterator<String> esborrar = aEsborrar.iterator();
        while(esborrar.hasNext()) destinacions.remove(esborrar.next());
    }


    /**
     * @pre cert
     * @post Retorna la comanda de l'agència en cas que existeixi, altrament null.
     * @return Retorna la comanda de l'agència en cas que existeixi, altrament null.
     */
    Comanda obtComanda() {
        return comanda;
    }
}