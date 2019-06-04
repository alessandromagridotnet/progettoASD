package ReteAutomi;


import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author alessandro
 */
public class Automa {
    private String nome;
    private ArrayList<Stato> stati;
    private ArrayList<Transizione> transizioni;

    public Automa() {
        this.stati = new ArrayList<>();
        this.transizioni = new ArrayList<>();
    }

    public String getNome() {
        return this.nome;
    }

    public ArrayList<Stato> getStati() {
        return this.stati;
    }
    
    public void pushStato(Stato s){
        this.stati.add(s);
    }

    public ArrayList<Transizione> getTransizioni() {
        return this.transizioni;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void pushTransizioni(Transizione t){
        this.transizioni.add(t);
    }
    
    /**
     * Funzione che ritorna una stringa contenente la rappresentazione XML
     * @return String
     */
    public String toXML(){
        String xml = "";
        System.out.println("Questo automa è composto da " + this.getStati().size() + " stati e da " + this.getTransizioni().size() + " transizioni");
        xml += "<Automa>" + System.lineSeparator();
            xml += "<Nome>" + this.getNome() + "</Nome>" + System.lineSeparator();
            xml += "<Descrizione>Questo automa è composto da " + this.getStati().size() + " stati e da " + this.getTransizioni().size() + " transizioni</Descrizione>" + System.lineSeparator();
            xml += "<Stati>" + System.lineSeparator();
                for(Stato s : this.stati){
                    xml += s.toXML();
                }
            xml += "</Stati>" + System.lineSeparator();
            xml += "<Transizioni>" + System.lineSeparator();
                for(Transizione t : this.transizioni){
                    xml += t.toXML();
                }
            xml += "</Transizioni>" + System.lineSeparator();
        xml += "</Automa>" + System.lineSeparator();
        
        return xml;
    }
    
    /**
     * Funzione per caricare un link partendo da un elemento XML e da una lista di automi disponibili
     * @param xml un elemento xml che contiene un LINK
     */
    public void fromXML(Element xml){
        this.setNome(xml.getChildText("Nome"));
        // array degli stati
        List listStati = xml.getChild("Stati").getChildren("Stato");
        for (int j = 0; j < listStati.size(); j++) {
            StatoSemplice stato = new StatoSemplice();
            Element nodoStato = (Element) listStati.get(j);
            stato.fromXML(nodoStato);
            this.pushStato(stato);
        }
        // array delle transizioni
        List listTransizioni = xml.getChild("Transizioni").getChildren("Transizione");
        for (int j = 0; j < listTransizioni.size(); j++) {
            TransizioneStati transizione = new TransizioneStati();
            Element nodoTransizione = (Element) listTransizioni.get(j);
            transizione.fromXML(nodoTransizione, this.getStati());
            this.pushTransizioni(transizione);
        }
    }
    
    /**
     * Funzione per la potatura, richiamabile solo su un automa dello comportamentale
     * @return boolean
     */
    public boolean potatura(){
        boolean chk = false;
        boolean chk2 = false;
        ///////////////////////////////////////////////////////////////////////
        ////////////////////////// POTATURA DEGLI STATI ///////////////////////
        ///////////////////////////////////////////////////////////////////////
        for(Stato s : this.getStati()){
            StatoComportamentale sc = (StatoComportamentale) s;
            if(sc.getIniziale()){
                sc.setConfermato(2);
            }
        }
        for(Stato s : this.getStati()){
            StatoComportamentale sc = (StatoComportamentale) s;
            if(sc.getFinale()){
                sc.setConfermato(2);
                chk2 = potatura_ricorsiva(sc);
                chk = chk || chk2;
            }
        }
        int size = 0;
        while(size < this.getStati().size()){
            StatoComportamentale sc = (StatoComportamentale) this.getStati().get(size);
            if(!(sc.isConfermato() || sc.isVisitato())){
                this.getStati().remove(sc);
            }else{
                ++size;
            }
        }
        ///////////////////////////////////////////////////////////////////////
        ///////////////////// POTATURA DELLE TRANSIZIONI //////////////////////
        ///////////////////////////////////////////////////////////////////////
        
        for(Transizione t : this.getTransizioni()){
            boolean presente = false, presente2=false;
            for(Stato s : this.getStati()){
                if(t.getIniziale().getId().equals(s.getId())){
                    presente = true;
                    break;
                }
            }
            for(Stato s : this.getStati()){
                if(t.getFinale().getId().equals(s.getId())){
                    presente2 = true;
                    break;
                }
            }
            if(!(presente && presente2)){
                t.setNome("DACANCELLARE");
            }
        }
        
        size = 0;
        while(size < this.getTransizioni().size()){
            TransizioneComportamentale tc = (TransizioneComportamentale) this.getTransizioni().get(size);
            if(tc.getNome().equals("DACANCELLARE")){
                this.getTransizioni().remove(size);
            }else{
                ++size;
            }
        }
        
        return chk;
    }
    
    /**
     * Sotto-funzione per la potatura che va ad esplorare l'albero marcando gli stati esplorati come confermati (se finali) o semplicemente come esplorati
     * @param sc lo stato da cui partire
     * @return boolean
     */
    private boolean potatura_ricorsiva(StatoComportamentale sc){
        ArrayList<StatoComportamentale> prec = precedenti(sc);
        boolean chk = false;
        boolean chk2 = false;
        // se non è uno stato già confermato lo imposto a stato visitato
        if(!sc.isConfermato()){
            sc.setConfermato(1);
        }
        // genero tutti i predecessori
        for(StatoComportamentale sc_prec : prec){
            // se il predecessore in esame è confermato confermo anche lo stato attuale
            // altrimenti richiamo ricorsivamente la funzione sol predecessore non visitato
            if(sc_prec.isConfermato()){
                sc.setConfermato(2);
                chk= true;
            }else if(!sc_prec.isVisitato()){
                // fatto su due righe per obbligare leseguzione della funzione
                chk2 = potatura_ricorsiva(sc_prec);
                chk = chk || chk2;
            }
        }
        return chk;
    }
    
    /**
     * Funzione per la determinizzazione di uno SpazioComportamentaleDecorato e la creazione del relativo automa
     * @param A_out Automa all'interno del quale calcolare l'output
     * @return boolean
     */
    public boolean determinizzazione(Automa A_out){
        StatoComportamentale si = new StatoComportamentale();
        // trovo lo stato iniziale
        for(Stato s : this.getStati()){
            StatoComportamentale sc = (StatoComportamentale) s;
            sc.setConfermato(0);
            if(sc.getIniziale()){
                si.clone(sc);
                break;
            }
        }
        ArrayList<StatoComportamentale> arr_sc = new ArrayList<>();
        arr_sc.add(si);
        // chiamata alla funzione ricorsiva
        return determinizzazioneRicorsiva(A_out, arr_sc, 0);
    }
    
    public boolean determinizzazioneRicorsiva(Automa A_out, ArrayList<StatoComportamentale> a_sc, int incrementale){
        StatoComportamentale nuovo = new StatoComportamentale();
        // calcola tutti gli stati successivi a quelli passati che siano collegati da transizioni con osservabilità nulla
        successiviOsservabilitaNull(a_sc).forEach((sc) -> {
            nuovo.getStati().add(sc);
        });
        boolean contains = false;
        for(Stato check_state : A_out.getStati()){
            StatoComportamentale sc = (StatoComportamentale) check_state;
            if(sc.equalsNotId(nuovo)){
                nuovo.clone(sc);
                contains = true;
                break;
            }
        }
        if(!contains){
            // cerco se esiste una transizione pendente senza stato finale e la aggiorno
            for(Transizione trans : A_out.getTransizioni()){
                if(trans.getFinale()==null){
                    trans.setFinale(nuovo);
                }
            }
            // controllo se è uno stato finale
            for(Stato s : nuovo.getStati()){
                StatoComportamentale s_c = (StatoComportamentale)s;
                if(s_c.getFinale()){
                    nuovo.setFinale(true);
                }
                if(s_c.getIniziale()){
                    nuovo.setIniziale(true);
                }
            }
            if(nuovo.getFinale()){
                calcola_diagnosi(nuovo);
            }
            nuovo.setId("b" + incrementale);
            incrementale = incrementale + 1;
            // aggiungi stato e completa il link
            A_out.pushStato(nuovo);
        }else{
            // cerco se esiste una transizione pendente senza stato finale e la aggiorno
            for(Transizione trans : A_out.getTransizioni()){
                if(trans.getFinale()==null){
                    trans.setFinale(nuovo);
                }
            }
            // termina
            return true;
        }
        // trova tutte le possibili etichette di osservabilità in uscita dall'insieme di stati considerati
        ArrayList<String> etichette_osservabilita = new ArrayList<>();
        for(Transizione tt : this.getTransizioni()){
            // controlla che l'etichetta non sia nulla e che lo stato iniziale sia uno di quelli considerati
            if((!tt.getOsservabilita().equals("NULL")) && nuovo.getStati().contains((StatoComportamentale) tt.getIniziale())){
                if(!etichette_osservabilita.contains(tt.getOsservabilita())){
                    etichette_osservabilita.add(tt.getOsservabilita());
                }
            }
        }
        Map<String,ArrayList<StatoComportamentale>> map= new HashMap<>();
        // per ogni etichetta di osservabilità possibile
        for(String e_o : etichette_osservabilita){
            ArrayList<StatoComportamentale> tmp_array = new ArrayList<>();
            // ciclo sulle transizioni
            for(Transizione tt : this.getTransizioni()){
                // cerco quelle che hanno una delle etichette di osservabilità possibili e che partono da uno degli stati considerati
                if(tt.getOsservabilita().equals(e_o) && nuovo.getStati().contains((StatoComportamentale) tt.getIniziale())){
                    // le aggiungo ad un array tempooraneo
                    tmp_array.add((StatoComportamentale) tt.getFinale());
                }
            }
            // metto l'array temporaneo in una map list in modo da raggruppare tutti gli stati in uscita accomunati dalla stessa erichetta
            map.put(e_o, tmp_array);
        }
        
        for (Map.Entry<String, ArrayList<StatoComportamentale>> entry : map.entrySet()) {
            // creo una transizione che parte dallo stato nuovo appena inserito e che ha la giusta etichetta di osservabilità
            // lacsio a null lo stato finale che setterò al prossimo loop
            TransizioneComportamentale tc = new TransizioneComportamentale();
            tc.setIniziale(nuovo);
            tc.setFinale(null);
            tc.setOsservabilita(entry.getKey());
            A_out.pushTransizioni(tc);
            
            determinizzazioneRicorsiva(A_out, entry.getValue(), incrementale);
        }
        return true;
        
    }
    
    /**
     * Funzione che calcola gli stati successivi all'interno dell'automa collegati da una transizione con etichetta di osservabilità nulla
     * @param a_sc Un ArrayList contenente gli stati confine attuali
     * @return Un ArrayList contenente tutti gli stati successivi collegati da transizioni ad osservabilità nulla
     */
    public ArrayList<StatoComportamentale> successiviOsservabilitaNull(ArrayList<StatoComportamentale> a_sc){
        ArrayList<StatoComportamentale> ret = new ArrayList<>();
        // aggiunge gli stati passati
        if(!a_sc.isEmpty()){
            try{
                for(StatoComportamentale sc : a_sc){
                    ret.add(sc);
                }
            }catch(Exception e){
                System.out.println(e);
            }
            // ciclo sulle transizioni
            for(Transizione tt : this.getTransizioni()){
                TransizioneComportamentale t = (TransizioneComportamentale) tt;
                //ArrayList<StatoComportamentale> finale = new ArrayList<>();
                for(StatoComportamentale sc : a_sc){
                    // se la transizione parte dallo stato comportamentale passato e ha etichetta di osservabilità nulla
                    if(t.getIniziale().equals(sc) && t.getOsservabilita().equals("NULL")){
                        // calcolo lo stato comportamentale successivo e se non è già contenuto in finale lo aggiungo
                        if(!ret.contains((StatoComportamentale) this.getStati().get(this.getStati().indexOf(t.getFinale())))){
                            ret.add((StatoComportamentale) this.getStati().get(this.getStati().indexOf(t.getFinale())));
                        }
                    }
                }
                if(ret.size()>a_sc.size()){
                    // chiamata ricorsiva per cercare i figli e aggiunta dei ritorni
                    successiviOsservabilitaNull(ret).forEach((sc) -> {
                        if(!ret.contains(sc)){
                            ret.add(sc);
                        }
                    });
                }
            }
        }
        return ret;
    }
    
    /**
     * Funzione che trova gli stati comportamentali successivi
     * @param sc Lo stato comportamentale rifermento
     * @return  L'ArrayList degli stati comportamentali successivi
     */
    private ArrayList<StatoComportamentale> successivi(StatoComportamentale sc){
        ArrayList<StatoComportamentale> ret = new ArrayList<>();
        for(Transizione tt : this.getTransizioni()){
            TransizioneComportamentale t = (TransizioneComportamentale) tt;
            if(t.getIniziale().equals(sc)){
                StatoComportamentale temporaneo = (StatoComportamentale) this.getStati().get(this.getStati().indexOf(t.getFinale()));
                ret.add(temporaneo);
            }
        }
        return ret;
    }
    
    /**
     * Funzione che trova gli stati comportamentali precedenti
     * @param sc Lo stato comportamentale rifermento
     * @return  L'ArrayList degli stati comportamentali precedenti
     */
    private ArrayList<StatoComportamentale> precedenti(StatoComportamentale sc){
        ArrayList<StatoComportamentale> ret = new ArrayList<>();
        for(Transizione tt : this.getTransizioni()){
            TransizioneComportamentale t = (TransizioneComportamentale) tt;
            // se la transizione ha come finale lo stato di riferimento e lo stato iniziale esiste nell'automa
            if(t.getFinale().equals(sc) && this.getStati().indexOf(t.getIniziale())>=0){
                StatoComportamentale temporaneo = (StatoComportamentale) this.getStati().get(this.getStati().indexOf(t.getIniziale()));
                ret.add(temporaneo);
            }
        }
        return ret;
    }
    
    /**
     * Funzione che verifica la validità di una osservazione lineare
     * @param osservazione_lineare un array di stringhe String[] contenente le varie etichette che indicano il percorso che mi aspetto di trovare
     * @return boolean
     */
    public boolean osservazione_valida(String[] osservazione_lineare){
        int i=0;
        // cerco lo stato iniziale nell'automa
        for(Stato st: this.getStati()){
            StatoComportamentale sc = (StatoComportamentale) st;
            if(sc.getIniziale()){
                // ciclo sulle transizioni per trovare quelle che partono dallo stato iniziale
                i=0;
                while(i < osservazione_lineare.length){
                    boolean trovato = false;
                    for(Transizione tr : this.getTransizioni()){
                        TransizioneComportamentale trc = (TransizioneComportamentale) tr;
                        if(trc.getIniziale().equals(sc)){
                            if(trc.getOsservabilita().equals(osservazione_lineare[i])){
                                i=i+1;
                                trovato=true;
                                sc = (StatoComportamentale) trc.getFinale();
                                break;
                            }
                        }
                    }
                    if(!trovato){
                        System.out.println("Osservazione inserita non valida ");
                        return false;
                    }
                }
                // l'osservazione è valida 
                if(i==osservazione_lineare.length){
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }
    
    /**
     * Funzione per la ricerca di un dizionario di osservazioni sull'automa this
     * @param osservazione_lineare un array di stringhe String[] contenente le varie etichette che indicano il percorso che mi aspetto di trovare
     */
    public String ricerca_dizionario(String[] osservazione_lineare){
        int i=0;
        boolean iniziale = false;
        // cerco lo stato iniziale nell'automa
        for(Stato st: this.getStati()){
            StatoComportamentale sc = (StatoComportamentale) st;
            if(sc.getIniziale()){
                iniziale=true;
                // ciclo sulle transizioni per trovare quelle che partono dallo stato iniziale
                i=0;
                while(i < osservazione_lineare.length){
                    boolean trovato = false;
                    for(Transizione tr : this.getTransizioni()){
                        TransizioneComportamentale trc = (TransizioneComportamentale) tr;
                        if(trc.getIniziale().equals(sc)){
                            if(trc.getOsservabilita().equals(osservazione_lineare[i])){
                                i=i+1;
                                trovato=true;
                                sc = (StatoComportamentale) trc.getFinale();
                                break;
                            }
                        }
                    }
                    if(!trovato){
                        System.out.println("Osservazione inserita non valida ");
                        return "Osservazione inserita non valida";
                    }
                }
                // l'osservazione è valida 
                if(i==osservazione_lineare.length){
                    String diagnosi = "{";
                    for(String dia : sc.getDiagnosi()){
                        diagnosi = diagnosi + "{" + dia + "},";
                    }
                    // rimozione dell'ultima virgola
                    diagnosi = diagnosi.substring(0, diagnosi.length()-1);
                    diagnosi = diagnosi + "}";
                    
                    System.out.println("La diagnosi trovata è: ");
                    System.out.println(diagnosi);
                    return "La diagnosi trovata è: " + diagnosi;
                }
            }
        }
        if(!iniziale){
            System.out.println("Osservazione inserita non valida");
            return "Osservazione inserita non valida";
        }
        return "Non dovrebbe capitare";
    }
    
    /**
     * Funzione che calcola la diagnosi di un macrostato a partire dalle etichette di osservabilità degli stati contenuti
     * La diagnosi viene messa nell'attributo diagnosi del macrostato
     * @param sc Il macrostato in oggetto
     */
    public void calcola_diagnosi(StatoComportamentale sc){
        for(Stato so : sc.getStati()){
            StatoComportamentale st_co_oss = (StatoComportamentale) so;
            // prendo solo gli stati finali
            if(st_co_oss.getFinale()){
                String etichette = "";
                if(st_co_oss.getRilevanza().size()>0){
                    for(String rilevanza : st_co_oss.getRilevanza()){
                        etichette = etichette + rilevanza + ",";
                    }
                    etichette = etichette.substring(0, etichette.length()-1);
                }else{
                    etichette = "";
                }
                if(!sc.getDiagnosi().contains(etichette)){
                    sc.pushDiagnosi(etichette);
                }
            }
        }
    }
    
    public void estrai_osservazione(String[] osservazione_lineare){
        // rappresenta in generale la frontiera da cui parte la ricerca, nello specifico contiene solo lo stato iniziale
        ArrayList<StatoComportamentale> arr_sc = new ArrayList<>();
        // trovo lo stato iniziale
        for(Stato s : this.getStati()){
            StatoComportamentale sc = (StatoComportamentale) s;
            sc.setConfermato(0);
            if(sc.getIniziale()){
                sc.setConfermato(2);
            }
        }
        // chiamata alla funzione ricorsiva
        estrai_osservazione_ricorsiva(osservazione_lineare, 0, false);
        
        int size = 0;
        while(size < this.getStati().size()){
            StatoComportamentale sc = (StatoComportamentale) this.getStati().get(size);
            if(sc.getConfermato()==0){
                this.getStati().remove(sc);
            }else{
                ++size;
            }
        }
        
        // RESET VARIABLES
        for(Stato s : this.getStati()){
            StatoComportamentale sc = (StatoComportamentale) s;
            sc.setConfermato(0);
        }
        this.potatura();
        this.ridenominazione("b");
    }
    
    /**
     * funzione che lascia nell'automa attuale solamente gli stati e le transizioni facenti parte dell'osservazione lineare ricevuta in ingresso
     * @param osservazione_lineare un array contenete le etichette di osservabilità richieste dall'osservazione lineare
     * @param position la posizione dell'etichetta di osservabilità da considerate
     */
    public void estrai_osservazione_ricorsiva(String[] osservazione_lineare, int position, boolean last){
        // calcolo l'array a_sc che contiene tutti gli stati frontiera da cui partirà la ricerca degli stati connessi da transizioni con osservabilità nulla
        ArrayList<StatoComportamentale> a_sc = new ArrayList<>();
        for(Stato s : this.getStati()){
            StatoComportamentale sc = (StatoComportamentale) s;
            if(sc.getConfermato()==2){
                a_sc.add(sc);
            }
        }
        // calcola tutti gli stati successivi a quelli passati che siano collegati da transizioni con osservabilità nulla
        successiviOsservabilitaNull(a_sc).forEach((sc) -> {
            if(a_sc.indexOf(sc)<0){
                ((StatoComportamentale) this.getStati().get(this.getStati().indexOf(sc))).setConfermato(2);
            }
        });
        // bugfix per aggiungere gli stati collegati da transizione null all'ultimo giro
        if(!last){
            String osservazione = "";
        
            // estraggo la transizione da considerare
            osservazione = osservazione_lineare[position];
            // ciclo tutte le transizioni disponibili
            for(Transizione tt : this.getTransizioni()){
                // se la transizione ha la giusta etichetta di osservabilità e parte da uno stato tra quelli da considerare
                if(tt.getOsservabilita().equals(osservazione) && ((StatoComportamentale)tt.getIniziale()).getConfermato()==2){
                    ((StatoComportamentale)tt.getFinale()).setConfermato(1);
                }
            }
            for(Stato s : this.getStati()){
                StatoComportamentale sc = (StatoComportamentale) s;
                if(sc.getConfermato()==2){
                    // visitato, confermato ma non più sulla frontiera
                    sc.setConfermato(3);
                }else if(sc.getConfermato()==1){
                    // è la nuova frontiera
                    sc.setConfermato(2);
                }
            }
            position++;
            
            // finche ci sono osservazioni estraibili dall'osservazione lineare
            if(position < osservazione_lineare.length){
                estrai_osservazione_ricorsiva(osservazione_lineare, position, false);
            }else{
                // bugfix per aggiungere gli stati collegati da transizione null all'ultimo giro
                estrai_osservazione_ricorsiva(osservazione_lineare, position, true);
            }
        }
    }
    
    /**
     * Funzione per la ridenominazione degli stati
     * @param prefisso 
     */
    public void ridenominazione(String prefisso){
        int i=0;
        String old_id = "";
        for(Stato s : this.getStati()){
            old_id = s.getId();
            s.setId(prefisso + i);
            for(Transizione t : this.getTransizioni()){
                if(t.getFinale().getId().equals(old_id)){
                    t.setFinale(s);
                }
            }
            i++;
        }
    }
    
    
    
    public boolean loadFromFile(String file) {
        try {
            SAXBuilder builder = new SAXBuilder();
            Element root = builder.build(new File(file)).getRootElement();
            
            this.fromXMLRiconoscitore(root);

            System.out.println("Caricamento completato");
            
            return true;

        } catch (Exception e) {
            System.err.println("Errore durante la lettura dal file");
            e.printStackTrace();
            return false;
        }
    }
    public void fromXMLRiconoscitore(Element xml){
        this.setNome(xml.getChildText("Nome"));
        // array degli stati
        List listStati = xml.getChild("Stati").getChildren("Stato");
        for (int j = 0; j < listStati.size(); j++) {
            StatoRiconoscitore stato = new StatoRiconoscitore();
            Element nodoStato = (Element) listStati.get(j);
            stato.fromXML(nodoStato);
            this.pushStato(stato);
        }
        // array delle transizioni
        List listTransizioni = xml.getChild("Transizioni").getChildren("Transizione");
        for (int j = 0; j < listTransizioni.size(); j++) {
            TransizioneComportamentale transizione = new TransizioneComportamentale();
            Element nodoTransizione = (Element) listTransizioni.get(j);
            transizione.fromXML(nodoTransizione, this.getStati());
            this.pushTransizioni(transizione);
        }
    }
//    public void fromXMLScenario(Element xml){
//        this.setNome(xml.getChildText("Nome"));
//        // array degli stati
//        List listStati = xml.getChild("Stati").getChildren("Stato");
//        for (int j = 0; j < listStati.size(); j++) {
//            StatoRiconoscitore stato = new StatoRiconoscitore();
//            Element nodoStato = (Element) listStati.get(j);
//            stato.fromXML(nodoStato);
//            this.pushStato(stato);
//        }
//        // array delle transizioni
//        List listTransizioni = xml.getChild("Transizioni").getChildren("Transizione");
//        for (int j = 0; j < listTransizioni.size(); j++) {
//            TransizioneScenario transizione = new TransizioneScenario();
//            Element nodoTransizione = (Element) listTransizioni.get(j);
//            transizione.fromXML(nodoTransizione, this.getStati());
//            this.pushTransizioni(transizione);
//        }
//    }
    
    public void fusione_dizionari(ArrayList<Automa> automi){
        Transizione t;
        StatoComportamentale sc = new StatoComportamentale();
        sc.setId("U0");
        sc.setIniziale(true);
        this.pushStato(sc);
        
        for(Automa a : automi){
            for(Stato s : a.getStati()){
                if(s.getIniziale()){
                    t = new TransizioneComportamentale();
                    t.setIniziale(sc);
                    t.setFinale(s);
                    t.setOsservabilita("NULL");
                    this.pushTransizioni(t);
                    
                    s.setIniziale(false);
                }
                this.pushStato(s);
            }
            for(Transizione tt : a.getTransizioni()){
                this.pushTransizioni(tt);
            }
        }
    }
    
    public void estrai_diagnosi(){
        for(Stato s : this.getStati()){
            StatoComportamentale sc = (StatoComportamentale) s;
            sc.clearDiagnosi();
            if(sc.getFinale()){
                for(Stato ss : sc.getStati()){
                    StatoComportamentale ssc = (StatoComportamentale) ss;
                    if(ssc.getFinale()){
                        for(String diagnosi : ssc.getDiagnosi()){
                            if(!sc.getDiagnosi().contains(diagnosi)){
                                sc.pushDiagnosi(diagnosi);
                            }
                        }
                    }
                }
            }
        }
    }
}
