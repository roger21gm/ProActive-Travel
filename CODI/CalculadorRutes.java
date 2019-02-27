    import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


/**
 * @class CalculadorRutes
 * @brief És un calculador de rutes òptimes.
 * @details A partir d'una comanda amb determinats requerimets de Clients, calcula la ruta òptima segons un criteri concret.
 *          Els criteris poden ser:
 *              -> Ruta barata: Calcularà la ruta més barata possible seguint els requisits dels clients.
 *              -> Ruta satisfactoria: Calcularà la ruta amb un grau de satisfacció major seguint els requisits dels clients.
 *              -> Ruta curta: Calcularà la ruta més curta seguint els requeriments dels clients.
 * @author Roger Generoso Masós (Backtracking) i Amat Martínez (Voraç)
 * @version 2017.4.5
 */
public class CalculadorRutes {
    private static final int MAX_HORES_VISITA = 6; ///< Màxim hores de visita diàries establert per l'agència.
    private Map<PuntInteres,Integer> jaHemPassatPer; ///< Conjunt de PuntInteres pels que ja hem passat a la rutaAct i el nombre de vegades que hi hem passat.
    private Comanda comanda; ///< Conté la comanda amb els requeriments dels clients sobre la ruta.
    private Ruta rutaAct; ///< Conté la ruta provisional (pot no ser completa).
    private Ruta resultat; ///< Emmagatzema la ruta òptima calculada.
    private PuntInteres origen; ///< És l'orígen de la Ruta
    private PuntInteres desti; ///< És el destí de la Ruta.
    private boolean trobat;

    /**
     * @pre cert
     * @post S'ha creat un CalculadorBacktracking amb les dades de la Comanda del grup de clients.
     * @param grup És la comanda que conté els requeriments de la Ruta dels clients.
     */
    CalculadorRutes(Comanda grup){
        jaHemPassatPer = new HashMap<>();
        comanda = grup;
        rutaAct = new Ruta();
    }

    /**
     * @pre cert
     * @post Retorna la ruta òptima de a fins a b passant per tots els PuntInteres de c òptima segons el criteri indicat.
     * @param Criteri És el patró de ruta òptima a calcular. 'b' -> barata   's' -> satisfactòria   'c' -> curta
     * @return Retorna la ruta òptima de a fins a b passant per tots els PuntInteres de c òptima segons el criteri indicat. Si no existeix, retorna null.
     */
    public Ruta camiMinim(char criteri) {
        rutaAct.assignarHoraIni(comanda.obtDataHoraInici());
        Set<PuntInteres> c = new HashSet<>(comanda.obtLlocsIntermitjos());
        c.add((PuntInteres)comanda.obtFi());
        origen = (PuntInteres)comanda.obtInici();
        desti = (PuntInteres)comanda.obtFi();
        Ruta ruta = iCamiMinim(origen,desti,c,criteri);
        rutaAct = new Ruta(); resultat = null; jaHemPassatPer = new HashMap<>();
        return ruta;
    }

    /**
     * @pre cert
     * @post Retorna una ruta –no òptima– que passa per tots els llocs d'interès dels clients que indica la comanda.
     * @return Retorna la ruta calculada.
     */
    public Ruta Vorac() {
        rutaAct = new Ruta();
        resultat = new Ruta();
        Comanda comandaOriginal = comanda;
        Iterator<PuntInteres> itLlocs = ordenarLlocsIntermitjos();
        comanda = comanda.segmentarComanda((comanda.obtLlocsIntermitjos().size()+1));
        PuntInteres pI1 = (PuntInteres) comanda.obtInici();
        PuntInteres pI2 = itLlocs.next();
        resultat = new Ruta(trobarUnCami(pI1,pI2));
        while(!pI2.equals(comanda.obtFi())){
            pI1 = pI2;
            if(itLlocs.hasNext())
                pI2 = itLlocs.next();
            else
                pI2 = (PuntInteres) comanda.obtFi();
            resultat.concatenaRuta(trobarUnCami(pI1,pI2));
        }
        System.out.println("RUTA");
        resultat.mostraPantalla();
        comanda = comandaOriginal;
        return resultat;
    }

    /**
     * @pre a, b  i tots els PuntInteres de c són PuntInteres que coneix l'agència.
     * @post Fa la crida a l'algorisme de backtracking i en retorna el resultat.
     * @param a És l'orígen de la ruta.
     * @param b És el destí de la ruta.
     * @return Retorna la ruta calculada per l'algorisme.
     */
    private Ruta trobarUnCami(PuntInteres a, PuntInteres b) {
        rutaAct = new Ruta();
        jaHemPassatPer = new HashMap<>();
        if (resultat.buida())
            rutaAct.assignarHoraIni(comanda.obtDataHoraInici());
        else
            rutaAct.assignarHoraIni(resultat.obtUltimItem().obtDataFi());
        iUnCami(a,b);
        trobat = false;
        return rutaAct;
    }

    /**
     * @pre a, b  i tots els PuntInteres de c són PuntInteres que coneix l'agència.
     * @post Retorna una ruta –no òptima– entre a i b.
     * @param a És l'orígen de la ruta.
     * @param b És el destí de la ruta.
     * @return Retorna la ruta calculada per l'algorisme.
     */
    private void iUnCami(PuntInteres a, PuntInteres b){
        Iterator<ItemRuta> candidatsPossibles = obtenirCandidats(a);
        while(candidatsPossibles.hasNext() && !trobat){
            ItemRuta act = candidatsPossibles.next();
            if(esPotAfegirARuta(act)){
                rutaAct.afegirItemRuta(act);
                afegirLlocQueHemPassat(act.obtenirPuntInteres());
                if(!b.equals(rutaAct.obtUltimItem().obtenirPuntInteres())){
                    iUnCami(rutaAct.obtenirLlocAct(),b);
                    if (!trobat){
                        rutaAct.eliminarDarrerItem();
                        eliminarLlocQueHemPassat(act.obtenirPuntInteres());
                    }
                }else{
                    trobat = true;
                }
            }
        }
    }

     /**
     * @pre cert
     * @post Ordena els llocs intermitjos segons proximitat geogràfica.
     * @return Retorna un iterador al conjunt ordenat prèviament.
     */
    private Iterator<PuntInteres> ordenarLlocsIntermitjos(){
        ArrayList<PuntInteres> llista = new ArrayList<>();
        PuntInteres inici = (PuntInteres) comanda.obtInici();
        PuntInteres actual;
        llista.addAll(comanda.obtLlocsIntermitjos());
        for (int i = 1; i < comanda.obtLlocsIntermitjos().size(); i++){
            int j = i;
            actual = llista.get(i);
            while(j > 0 && inici.distanciaA(actual) < inici.distanciaA(llista.get(i-1))){
                llista.set(j, llista.get(j-1));
                j--;
            }
            llista.set(j,actual);
        }
        return llista.iterator();
    }

    /**
     * @pre a, b  i tots els PuntInteres de c són PuntInteres que coneix l'agència.
     * @post Retorna la ruta òptima de a fins a b passant per tots els PuntInteres de c òptima segons el criteri indicat.
     * @param a És l'orígen de la ruta.
     * @param b És el destí de la ruta.
     * @param c És un conjunt de PuntInteres intermitjos per on es requereix que passi la ruta.
     * @param criteri És el patró de ruta òptima a calcular. 'b' -> barata   's' -> satisfactòria   'c' -> curta
     * @return Retorna la ruta òptima de a fins a b passant per tots els PuntInteres de c òptima segons el criteri indicat. Si no exisiteix, retorna null.
     */
    private Ruta iCamiMinim(PuntInteres a, PuntInteres b, Set<PuntInteres> c, char criteri){
        Iterator<ItemRuta> candidatsPossibles = obtenirCandidats(a);
        while(candidatsPossibles.hasNext()){
            ItemRuta act = candidatsPossibles.next();
            if(esPotAfegirARuta(act) && esPotMillorarRuta(act, criteri)){
                rutaAct.afegirItemRuta(act);
                afegirLlocQueHemPassat(act.obtenirPuntInteres());
                Set<PuntInteres> c2 = new HashSet<>(c);
                if(act instanceof Visita && c2.contains(act.obtenirPuntInteres()))
                    c2.remove(act.obtenirPuntInteres());
                if(!esRutaCompleta(b, c)){ //Ruta completa
                    iCamiMinim(rutaAct.obtenirLlocAct(),b,c2, criteri);
                }else{
                    if(esRutaOptima(criteri)){
                        resultat = new Ruta(rutaAct);
                    }
                }
                rutaAct.eliminarDarrerItem();
                eliminarLlocQueHemPassat(act.obtenirPuntInteres());
            }
        }
        return resultat;
    }

    /**
     * @pre cert
     * @post Retorna cert si afegint l' ItemRuta act a la rutaAct encara és possible obtenir una millor ruta que la que òptima fins al moment segons el criteri.
     * @param act És l' ItemRuta a afegir.
     * @param criteri És el criteri d'elecció. 'b' -> barata   's' -> satisfactòria   'c' -> curta
     * @return Retorna cert si afegint l' ItemRuta act a la rutaAct encara és possible obtenir una millor ruta que la que òptima fins al moment segons el criteri.
     */
    private boolean esPotMillorarRuta(ItemRuta act, char criteri) {
        if(criteri == 'b'){
            rutaAct.afegirItemRuta(act);
            boolean potSerMillor = rutaAct.obtPreu() <= comanda.obtPreuMax();
            if(resultat != null){
                boolean compleixRequisits = potSerMillor && rutaAct.obtPreu() <= resultat.obtPreu();
                rutaAct.eliminarDarrerItem();
                return compleixRequisits;
            }
            rutaAct.eliminarDarrerItem();
            return potSerMillor;
        }else if(criteri == 'c'){
            rutaAct.afegirItemRuta(act);
            boolean potSerMillor = rutaAct.obtDuradaAct() <= comanda.obtDuradaMax();
            if(resultat != null){
                boolean compleixRequisits = potSerMillor && rutaAct.obtDuradaAct() <= resultat.obtDuradaAct();
                rutaAct.eliminarDarrerItem();
                return compleixRequisits;
            }
            rutaAct.eliminarDarrerItem();
            return potSerMillor;
        }else if(criteri == 's'){
            return true; //Sempre pot arribar a ser més satisfactoria.
        }
        return false;
    }
    /**
     * @pre Cert
     * @post Retorna cert si la rutaAct és una ruta completa. El destí de la ruta és a b i passa per tots els PuntInteres de c.
     * @param b És el PuntInteres de destí.
     * @param c És un conjunt de PuntInteres que es requereix visitar.
     * @return Retorna cert si la rutaAct és una ruta completa. El destí de la ruta és a b i passa per tots els PuntInteres de c.
     */
    private boolean esRutaCompleta(PuntInteres b, Set<PuntInteres> c) {
        if(rutaAct.obtenirLlocAct()!=null && rutaAct.obtenirLlocAct().equals(b)){
            for (PuntInteres aC : c) {
                if(!rutaAct.jaShaVisitat(aC)) return false;
            }
            return true;
        }
        return false;
    }
    /**
     * @pre rutaAct ha de ser completa.
     * @post Retorna cert si la rutaAct és més optima que el la òptima fins al moment segons un criteri determinat.
     * @param criteri És el criteri d'elecció. 'b' -> barata   's' -> satisfactòria   'c' -> curta
     * @return Retorna cert si la rutaAct és més optima que el la òptima fins al moment segons un criteri determinat.
     */
    private boolean esRutaOptima(char criteri) {
        if(resultat != null){
            if(criteri == 'b'){
                if(rutaAct.obtPreu() < resultat.obtPreu()) return true;
                else if(rutaAct.obtPreu() == resultat.obtPreu()){
                    if(rutaAct.obtGrauSatisfaccio() > resultat.obtGrauSatisfaccio()) return true;
                    else if(rutaAct.obtGrauSatisfaccio() == resultat.obtGrauSatisfaccio()) return rutaAct.obtDuradaAct() < resultat.obtDuradaAct();
                }
            }else if(criteri == 'c'){
                if(rutaAct.obtDuradaAct() < resultat.obtDuradaAct()) return true;
                else if(rutaAct.obtDuradaAct() == resultat.obtDuradaAct()){
                    if(rutaAct.obtGrauSatisfaccio() > resultat.obtGrauSatisfaccio()) return true;
                    else if(rutaAct.obtGrauSatisfaccio() == resultat.obtGrauSatisfaccio()) return rutaAct.obtPreu() < resultat.obtPreu();
                }
            }else if(criteri == 's'){
                if(rutaAct.obtGrauSatisfaccio() > resultat.obtGrauSatisfaccio()) return true;
                else if(rutaAct.obtGrauSatisfaccio() == resultat.obtGrauSatisfaccio()){
                    if(rutaAct.obtPreu() < resultat.obtPreu()) return true;
                    else if(rutaAct.obtPreu() == resultat.obtPreu()) return rutaAct.obtDuradaAct()<resultat.obtDuradaAct();
                }
            }
            return false;
        }
        return true;
    }
    /**
     * @pre cert
     * @post retorna cert si l'ItemRuta act compleix els requisits per afegir-se a la rutaAct.
     * @param act És l'item que es vol afegir.
     * @return retorna cert si l'ItemRuta act compleix els requisits per afegir-se a la rutaAct.
     */
    private boolean esPotAfegirARuta(ItemRuta act) {
       /*
        PRIMER PACK CONDICIONS: Estat de la ruta abans d'afegir l'Ítem:
            Condicio 1: No hem d'afegir un item que vulneri les condicions de ruta de la comanda.
            Condició 2: No podem afegir un item amb dia i hora anterior a la hora actual de la ruta.
            Condició 3: No tornem a desplaçar-nos fins al lloc que ja ens hem desplaçat a no ser que sigui un allotjament.
            Condició 4: No s'ha d'haver visitat prèviament.
        */
        boolean cond1 = false;
        ItemRuta ultItemRuta = rutaAct.obtUltimItem();
        if (ultItemRuta == null || !(rutaAct.obtPreu() + ultItemRuta.obtPreu() > comanda.obtPreuMax()) || !(rutaAct.obtDuradaAct() + ultItemRuta.obtDurada() > comanda.obtDuradaMax()))
            if (act.obtDataInici().isAfter(rutaAct.getDataHoraActual()) || act.obtDataInici().isEqual(rutaAct.getDataHoraActual()))
                if(!(act instanceof Desplacament && !act.obtenirPuntInteres().equals(desti)) || !jaHemPassatPer.containsKey(act.obtenirPuntInteres()) || act.obtenirPuntInteres() instanceof Allotjament)
                    cond1 = (!(act instanceof Visita) || !rutaAct.jaShaVisitat(act.obtenirPuntInteres()));

        /*
        SEGON PACK CONDICIONS: Estat de la ruta després d'afegir l'Ítem, la ruta ha de quedar en un estat consistent:
            Condició 5: No s'ha de tornar a programar una Visita/Allotjament a un PuntInteres que acabem de Visitar/Allotjar.
            Condició 6: No estem en hora prohibida (hora de dinar), o bé estem en hora prohibida però es tracta d'un Desplaçament.
            Condició 7: No superem el max d'hores de visita diares permeses.
        */
        boolean cond2 = false;
        if (cond1) {
            rutaAct.afegirItemRuta(act);
            if (!(ultItemRuta != null && ultItemRuta.getClass().equals(act.getClass())) || !ultItemRuta.obtenirPuntInteres().equals(act.obtenirPuntInteres()))
                if (!estemEnHoraProhibida() || estemEnHoraProhibida() && act instanceof Desplacament)
                    cond2 = (!(rutaAct.horesVisitaUltimDia() > MAX_HORES_VISITA));

            rutaAct.eliminarDarrerItem();
        }
        return cond2;
    }

    /**
     * @pre cert
     * @post S'obtenen tots els ItemRuta que es poden dur a terme des del PuntInteres a.
     * @param a És el PuntInteres des d'on partim.
     * @return Retorna un iterador de tots els ItemRuta que es poden dur a terme des del PuntInteres a.
     */
    private Iterator<ItemRuta> obtenirCandidats(PuntInteres a) {
        List<ItemRuta> cosesAFer = new ArrayList<>();

        if(rutaAct.buida()){
            ItemRuta novaVisita = visitarOAllotjarseA(a);
            if(novaVisita != null) cosesAFer.add(novaVisita);
        }else{
            ItemRuta novaVisita = visitarOAllotjarseA(a);
            if(novaVisita != null) cosesAFer.add(novaVisita);//Podem visitar o allotjar-nos al PuntInteres.

            //Afegirem també tots els transportsDirectes que disposa.
            Iterator<TransportDirecte> transDir = a.obtenirConnexionsDirectes();
            while(transDir.hasNext()){
                TransportDirecte act = transDir.next();
                ItemRuta ultElement = rutaAct.obtUltimItem();
                if(!(ultElement instanceof Desplacament && ((Desplacament) ultElement).obtOrigen().equals(act.obtLlocDesti()))){
                    Desplacament nouDespDirecte = new Desplacament(a,rutaAct.getDataHoraActual(),act, (PuntInteres)act.obtLlocDesti());
                    cosesAFer.add(nouDespDirecte);
                }
            }

            Destinacio ciutat = a.obtDestinacio();
            //Si és una ciutat afegirem totes les connexions amb transportPublic
            if(ciutat != null){
                //Afegim també tots els transportsIndirectes del dia actual.
                Iterator<TransportIndirecte> transIndir = ciutat.sortidesTransportsIndirectes(rutaAct.getDataHoraActual().toLocalDate());
                while(transIndir!=null && transIndir.hasNext()){
                    TransportIndirecte act = transIndir.next();
                    Destinacio desti = (Destinacio)act.obtLlocDesti();
                    Iterator<PuntInteres> pICiutatDesti = desti.obtenirPuntsInteres();
                    while(pICiutatDesti.hasNext()){
                        PuntInteres aux = pICiutatDesti.next();
                        ItemRuta ultElement = rutaAct.obtUltimItem();
                        if(!(ultElement instanceof Desplacament && ((Desplacament) ultElement).obtOrigen().equals(aux))){
                            if(act.obtHoraSortida().isAfter(rutaAct.getDataHoraActual())){
                                Desplacament nouDespIndirec = new Desplacament(a,act.obtHoraSortida(),act,aux);
                                cosesAFer.add(nouDespIndirec);
                            }
                        }
                    }
                }
                if(ciutat.obtTransPub() != null){
                    Iterator<PuntInteres> pICiutat = ciutat.obtenirPuntsInteres();
                    while(pICiutat.hasNext()){
                        PuntInteres act = pICiutat.next();
                        if(!act.equals(a)){
                            ItemRuta ultElement = rutaAct.obtUltimItem();
                            if(!(ultElement instanceof Desplacament && ((Desplacament) ultElement).obtOrigen().equals(act))){
                                Desplacament nouDespPublic = new Desplacament(a,rutaAct.getDataHoraActual(),ciutat.obtTransPub(), act);
                                cosesAFer.add(nouDespPublic);
                            }
                        }
                    }
                }
            }
        }
        return cosesAFer.iterator();
    }
    /**
     * @pre cert
     * @post Retornarà un ItemRuta amb una visita en la primera hora que sigui disponible o un allotjament en cas que compleixi els requisits dels clients.
     * @param a És el PuntInteres on ens volem allotjar o visitar.
     * @return Retornarà un ItemRuta amb una visita en la primera hora que sigui disponible o un allotjament en cas que compleixi els requisits dels clients.
     */
    private ItemRuta visitarOAllotjarseA(PuntInteres a) {
        ItemRuta nouItem;
        if(a instanceof LlocVisitable){
            LlocVisitable nou = (LlocVisitable)a;
            float grauSatisfaccio = calcularGrauSatisfaccio(nou);
            Visita visitaIni = nou.programarVisita(rutaAct.getDataHoraActual(), grauSatisfaccio);
            nouItem = visitaIni;
        }else{
            Allotjament nou = (Allotjament)a;
            if(nou.obtCategoria().equals(comanda.obtCategoriaAllotjamentsDestijada())){
                float grauSatisfaccio = calcularGrauSatisfaccio(nou);
                EstadaAllotjament novaEstada = nou.allotjarse(rutaAct.getDataHoraActual(), grauSatisfaccio);
                nouItem = novaEstada;
            }
            else nouItem = null;
        }
        return nouItem;
    }
    /**
     * @pre cert
     * @return retorna cert si la ruta actual té l'últim ítem en hora prohibida.
     */
    private boolean estemEnHoraProhibida() {
        LocalDateTime act = rutaAct.getDataHoraActual();
        LocalTime horaAct = act.toLocalTime();
        return horaAct.isAfter(LocalTime.of(12,0)) && horaAct.isBefore(LocalTime.of(14,0));
    }
    /**
     * @pre cert
     * @post Retorna el grau de satisfacció de visitar o allotjar-se a nou segons les preferències de la Comanda dels clients.
     * @param nou És el PuntInteres on ens volem allotjar o visitar.
     * @return Retorna el grau de satisfacció de visitar o allotjar-se a nou segons les preferències de la Comanda dels clients..s
     */
    private float calcularGrauSatisfaccio(PuntInteres nou) {
        return comanda.calculaSatisfaccio(nou, rutaAct.getDataHoraActual().toLocalDate());
    }

    /**
     * @pre ja hem passat algun cop per puntInteres a la rutaAct
     * @post S'ha eliminat de la llista una "passada" per aquest lloc. Si només hi haviem passat una vegada s'ha eliminat definitivament.
     * @param puntInteres És el lloc que volem eliminar
     */
    private void eliminarLlocQueHemPassat(PuntInteres puntInteres) {
        Integer vegades = jaHemPassatPer.get(puntInteres) - 1;
        if(vegades == 0) jaHemPassatPer.remove(puntInteres);
        else jaHemPassatPer.replace(puntInteres,vegades);
    }
    /**
     * @pre Cert
     * @post S'ha afegit una "passada" pel puntInteres al conjunt de llocs que ja hem passat a la rutaAct
     * @param puntInteres És el lloc que volem afegir.
     */
    private void afegirLlocQueHemPassat(PuntInteres puntInteres) {
        if(!jaHemPassatPer.containsKey(puntInteres)){
            jaHemPassatPer.put(puntInteres,1);
        }else{
            Integer vegades = jaHemPassatPer.get(puntInteres) + 1;
            jaHemPassatPer.replace(puntInteres,vegades);
        }
    }
}
