package ReteAutomi;
import java.io.ByteArrayInputStream;
import org.jdom.*;
import org.jdom.input.*;

import java.util.List;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import static jdk.nashorn.internal.objects.NativeArray.map;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alessandro
 */
public class ReteAutomi {
    private String nome;
    private ArrayList<Link> links;
    private ArrayList<Automa> automi;

    public ReteAutomi() {
        this.links = new ArrayList<Link>();
        this.automi = new ArrayList<Automa>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public ArrayList<Automa> getAutomi() {
        return automi;
    }
    
    public void pushLink(Link l){
        this.links.add(l);
    }
    
    public void pushAutoma(Automa a){
        this.automi.add(a);
    }
    
    
    public boolean loadFromFile(String file) {
        try {
            SAXBuilder builder = new SAXBuilder();
            Element root = builder.build(new File(file)).getRootElement();
            
            this.setNome(root.getChild("Nome").getText());

            List listAutomi =root.getChild("Automi").getChildren("Automa");
            // Automi
            for (int i = 0; i < listAutomi.size(); i++){
                Automa automa = new Automa();
                Element nodoAutoma = (Element) listAutomi.get(i);
                
                automa.fromXML(nodoAutoma);
                
                this.pushAutoma(automa);
            }

            // Links
            Element links =root.getChild("Links");
            List listLink = links.getChildren("Link");
            for (int i = 0; i < listLink.size(); i++){
                Link lnk = new Link();
                lnk.fromXML((Element)listLink.get(i), this.getAutomi());
                this.pushLink(lnk);
            }

            System.out.println("Caricamento completato");
            
            return true;

        } catch (Exception e) {
            System.err.println("Errore durante la lettura dal file");
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Funzione che salva l'XML della rete di automi caricata in un file 
     * @param fileName path e nome del file in cui si vuole salvare l'xml generato
     * @return boolean
     */
    public boolean storeIntoFile(String fileName){
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.print(this.toPrettyString(this.toXML()));
            System.out.println("Salvataggio eseguito correttamente");
            return true;
        }catch(Exception e){
            System.out.println("Errore durante il salvataggio del file");
            return false;
        }
    }
    
    /**
     * Funzione che ritorna una stringa contenente la rappresentazione XML
     * @return String
     */
    public String toXML(){
        String xml = "";
        System.out.println("Composta da " + this.getAutomi().size() + " automi e da " + this.getLinks().size() + " links");
        xml += "<ReteAutomi>" + System.lineSeparator();
            xml += "<Nome>" + this.getNome() + "</Nome>" + System.lineSeparator();
            xml += "<Descrizione>Composta da " + this.getAutomi().size() + " automi e da " + this.getLinks().size() + " links</Descrizione>" + System.lineSeparator();
            xml += "<Automi>" + System.lineSeparator();
                for(Automa a : this.automi){
                    xml += a.toXML();
                }
            xml += "</Automi>" + System.lineSeparator();
            
            xml += "<Links>" + System.lineSeparator();
                for(Link l : this.links){
                    xml += l.toXML();
                }
            xml += "</Links>" + System.lineSeparator();
        xml += "</ReteAutomi>" + System.lineSeparator();
        
        return xml;
    }
    
    /**
     * Funzione che calcola il primo Stato all'interno dell'automa chiamato StatoComportamentale
     * @param A_out l'automa dello StatoComportamentale che si vuole venga compilato
     * @return boolean
     */
    public boolean calcolaStatoComportamentale(Automa A_out){
        StatoComportamentale sc = new StatoComportamentale();
        int[] conteggio = new int[1];
        conteggio[0]=0;
        sc.setId(conteggio[0]+"");
        this.getAutomi().forEach((a) -> {
            a.getStati().forEach((s) -> {
                if(s.getIniziale()){
                    sc.pushStato(s);
                }
            });
        });
        Evento eventoNull = new Evento();
        eventoNull.setNome("NULL");
        this.getLinks().forEach((l) -> {
            Coppia cp = new Coppia();
            cp.setLink(l.getNome());
            cp.setEvento(eventoNull);
            sc.pushCoppia(cp);
        });
        sc.setFinale(true);
        sc.setIniziale(true);
        
        A_out.pushStato(sc);
        
        conteggio[0]++;
        statoComportamentaleRicorsivo(A_out, sc, conteggio);
        
        return true;
    }
    
    /**
     * Funzione ricorsiva che calcola tutti gli stati discendenti dal primo all'interno dell'automa StatoComportamentale
     * @param A_out l'automa dello StatoComportamentale che si vuole venga compilato
     * @param sc_pre lo stato da cui parte la ricerca (al primo giro quello generato da "calcolaStatoComportamentale()")
     * @param conteggio un intero che indica il numero di stati generati dal sistema
     * @return boolean
     */
    private boolean statoComportamentaleRicorsivo(Automa A_out, StatoComportamentale sc_pre, int[] conteggio){
        Evento eventoNull = new Evento();
        eventoNull.setNome("NULL");
        // cicliamo su automi e transizioni per poter scansionare tutte le possibili transizioni
        for (Automa a : this.getAutomi()) {
            for(Transizione tt : a.getTransizioni()){
                // casto a TransizioneStati perchè so che può essere solo quello
                TransizioneStati t = (TransizioneStati) tt;
                // controllo se lo stato iniziale della transizione considerata è tra gli stati attualmente attivi
                if(sc_pre.getStati().contains(t.getIniziale())){
                    // Controlliamo che siano verificati i prerequisiti degli eventi in ingresso
                    if(t.getIngresso().getEvento().getNome().equals("NULL") || sc_pre.getCoppie().contains(t.getIngresso())){
                        boolean tmp = true;
                        for(Coppia u : t.getUscita()){
                            for (Coppia u2 : sc_pre.getCoppie()) {
                                //Controlliamo che i link associati agli eventi in uscita siano liberi
                                if (u.getLink().equals(u2.getLink()) && !(u2.getEvento().equals(eventoNull))){
                                    tmp = false;
                                    break;
                                }
                            }
                            if (!tmp){
                                break;
                            }
                        }
                        // entra nel seguente if solo se sono state verificate tutte le precondizioni
                        if(tmp){
                            // scaturisce l'evento e lancia una nuova istanza ricorsiva
                            StatoComportamentale sc = new StatoComportamentale();
                            sc.clone(sc_pre);
                            sc.setIniziale(false);
                            sc.setFinale(false);
                            sc.setId(conteggio[0]+"");
                            // rimuovo lo stato iniziale della transizione dalla lista degli stati nel nuovo spazio comportamentale
                            if(sc.getStati().contains(t.getIniziale())){
                                sc.getStati().remove(t.getIniziale());
                            }
                            // aggiungo alla lista degli stati del nuovo spazio comportamentale lo stato finale della transizione
                            Stato st =t.getFinale().clone();
                            st.setIniziale(true);
                            sc.pushStato(st);
                            
                            // loop sulle coppie evento link in uscita alla transizione abilitata
                            if (!t.getIngresso().getEvento().getNome().equals("NULL")){
                               for (Coppia cp_s : sc.getCoppie()){
                                   if (cp_s.getLink().equals(t.getIngresso().getLink())){
                                       cp_s.setEvento(eventoNull);
                                   }
                               }
                            }
                            for(Coppia cp_u : t.getUscita()){
                                for (Coppia cp_s : sc.getCoppie()){
                                    if (cp_u.getLink().equals(cp_s.getLink())){
                                        cp_s.setEvento(cp_u.getEvento());
                                    }
                                }
                            }
                            // controlli per determinare se lo stato è finale
                            Integer cnt_void_link = 0;
                            for(Coppia cp : sc.getCoppie()){
                                if(cp.getEvento().getNome().equals("NULL")){
                                    cnt_void_link++;
                                }
                            }
                            // controllo se tutti i link sono vuoti
                            if(cnt_void_link == sc.getCoppie().size()){
                                sc.setFinale(true);
                            }else{
                                sc.setFinale(false);
                            }
                            
                            TransizioneComportamentale t_comp = new TransizioneComportamentale();
                            t_comp.setNome(t.getNome());
                            t_comp.setIniziale(sc_pre);
                            // controllo che il nuovo stato generato non sia già presente nell'automa dello stato comportamentale
                            int chk_count = 0;
                            for(Stato s_chk : A_out.getStati()){
                                if(s_chk.equalsNotId(sc)){
                                    chk_count++;
                                    t_comp.setFinale(s_chk);
                                    A_out.pushTransizioni(t_comp);
                                    break;
                                }
                            }
                            
                            // se il ciclo sopra ha trovato uno stato uguale nell'albero già generato allora non entro nell'if sotto
                            if( chk_count==0){
                                t_comp.setFinale(sc);
                                // aggiunta dello stato all'Automa "statocomportamentale"
                                A_out.pushStato(sc);
                                A_out.pushTransizioni(t_comp);
                                // chiamata ricorsiva
                                conteggio[0]++;
                                statoComportamentaleRicorsivo(A_out, sc, conteggio);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * Funzione che calcola il primo Stato all'interno dell'automa chiamato StatoComportamentale decorato
     * @param A_out l'automa dello StatoComportamentale che si vuole venga compilato
     * @return boolean
     */
    public boolean calcolaStatoComportamentaleDecorato(Automa A_out){
        StatoComportamentale sc = new StatoComportamentale();
        int[] conteggio = new int[1];
        conteggio[0]=0;
        sc.setId("a"+conteggio[0]+"");
        this.getAutomi().forEach((a) -> {
            a.getStati().forEach((s) -> {
                if(s.getIniziale()){
                    sc.pushStato(s);
                }
            });
        });
        Evento eventoNull = new Evento();
        eventoNull.setNome("NULL");
        this.getLinks().forEach((l) -> {
            Coppia cp = new Coppia();
            cp.setLink(l.getNome());
            cp.setEvento(eventoNull);
            sc.pushCoppia(cp);
        });
        sc.setFinale(true);
        sc.setIniziale(true);
        
        A_out.pushStato(sc);
        
        conteggio[0]++;
        statoComportamentaleDecoratoRicorsivo(A_out, sc, conteggio);
        
        return true;
    }
    
    /**
     * Funzione ricorsiva che calcola tutti gli stati discendenti dal primo all'interno dell'automa StatoComportamentale decorato
     * @param A_out l'automa dello StatoComportamentale che si vuole venga compilato
     * @param sc_pre lo stato da cui parte la ricerca (al primo giro quello generato da "calcolaStatoComportamentale()")
     * @param conteggio un intero che indica il numero di stati generati dal sistema
     * @return boolean
     */
    private boolean statoComportamentaleDecoratoRicorsivo(Automa A_out, StatoComportamentale sc_pre, int[] conteggio){
        Evento eventoNull = new Evento();
        eventoNull.setNome("NULL");
        // cicliamo su automi e transizioni per poter scansionare tutte le possibili transizioni
        for (Automa a : this.getAutomi()) {
            for(Transizione tt : a.getTransizioni()){
                // casto a TransizioneStati perchè so che può essere solo quello
                TransizioneStati t = (TransizioneStati) tt;
                // controllo se lo stato iniziale della transizione considerata è tra gli stati attualmente attivi
                if(sc_pre.getStati().contains(t.getIniziale())){
                    // Controlliamo che siano verificati i prerequisiti degli eventi in ingresso
                    if(t.getIngresso().getEvento().getNome().equals("NULL") || sc_pre.getCoppie().contains(t.getIngresso())){
                        boolean tmp = true;
                        for(Coppia u : t.getUscita()){
                            for (Coppia u2 : sc_pre.getCoppie()) {
                                //Controlliamo che i link associati agli eventi in uscita siano liberi
                                if (u.getLink().equals(u2.getLink()) && !(u2.getEvento().equals(eventoNull))){
                                    tmp = false;
                                    break;
                                }
                            }
                            if (!tmp){
                                break;
                            }
                        }
                        // entra nel seguente if solo se sono state verificate tutte le precondizioni
                        if(tmp){
                            // scaturisce l'evento e lancia una nuova istanza ricorsiva
                            StatoComportamentale sc = new StatoComportamentale();
                            sc.clone(sc_pre);
                            sc.setIniziale(false);
                            sc.setFinale(false);
                            sc.setId("a"+conteggio[0]+"");
                            
                            if(!(t.getRilevanza().equals("NULL")) && !(sc.getRilevanza().contains(t.getRilevanza()))){
                                sc.pushRilevanza(t.getRilevanza());
                            }
                            
                            // rimuovo lo stato iniziale della transizione dalla lista degli stati nel nuovo spazio comportamentale
                            if(sc.getStati().contains(t.getIniziale())){
                                sc.getStati().remove(t.getIniziale());
                            }
                            // aggiungo alla lista degli stati del nuovo spazio comportamentale lo stato finale della transizione
                            Stato st =t.getFinale().clone();
                            st.setIniziale(true);
                            sc.pushStato(st);
                            
                            // loop sulle coppie evento link in uscita alla transizione abilitata
                            if (!t.getIngresso().getEvento().getNome().equals("NULL")){
                               for (Coppia cp_s : sc.getCoppie()){
                                   if (cp_s.getLink().equals(t.getIngresso().getLink())){
                                       cp_s.setEvento(eventoNull);
                                   }
                               }
                            }
                            for(Coppia cp_u : t.getUscita()){
                                for (Coppia cp_s : sc.getCoppie()){
                                    if (cp_u.getLink().equals(cp_s.getLink())){
                                        cp_s.setEvento(cp_u.getEvento());
                                    }
                                }
                            }
                            // controlli per determinare se lo stato è finale
                            Integer cnt_void_link = 0;
                            for(Coppia cp : sc.getCoppie()){
                                if(cp.getEvento().getNome().equals("NULL")){
                                    cnt_void_link++;
                                }
                            }
                            // controllo se tutti i link sono vuoti
                            if(cnt_void_link == sc.getCoppie().size()){
                                sc.setFinale(true);
                            }else{
                                sc.setFinale(false);
                            }
                            
                            TransizioneComportamentale t_comp = new TransizioneComportamentale();
                            t_comp.setNome(t.getNome());
                            t_comp.setIniziale(sc_pre);
                            t_comp.setOsservabilita(t.getOsservabilita());
                            // controllo che il nuovo stato generato non sia già presente nell'automa dello stato comportamentale
                            int chk_count = 0;
                            for(Stato s_chk : A_out.getStati()){
                                if(s_chk.equalsNotId(sc)){
                                    chk_count++;
                                    t_comp.setFinale(s_chk);
                                    A_out.pushTransizioni(t_comp);
                                    break;
                                }
                            }
                            
                            // se il ciclo sopra ha trovato uno stato uguale nell'albero già generato allora non entro nell'if sotto
                            if( chk_count==0){
                                t_comp.setFinale(sc);
                                // aggiunta dello stato all'Automa "statocomportamentale"
                                A_out.pushStato(sc);
                                A_out.pushTransizioni(t_comp);
                                // chiamata ricorsiva
                                conteggio[0]++;
                                statoComportamentaleDecoratoRicorsivo(A_out, sc, conteggio);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * Funzione per aggiungere automaticamente l'indentatura alle stringhe contenenti xml, parametro di indentazione automatico a 4
     * @param xml la stringa xml
     * @return 
     */
    public static String toPrettyString(String xml){
        return toPrettyString(xml, 4);
    }
    
    /**
     * Funzione per aggiungere automaticamente l'indentatura alle stringhe contenenti xml
     * @param xml la stringa xml
     * @param indent la dimensione dell'indentazione in numero di spazi
     * @return 
     */
    public static String toPrettyString(String xml, int indent) {
        try {
            // Turn xml string into a document
            org.w3c.dom.Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

            // Remove whitespaces outside tags
            document.normalize();
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']",
                                                          document,
                                                          XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);
                node.getParentNode().removeChild(node);
            }

            // Setup pretty print options
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // Return pretty print xml string
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
    /**
     * Funzione che calcola il primo Stato all'interno dell'automa chiamato StatoComportamentale decorato
     * @param A_out l'automa dello StatoComportamentale che si vuole venga compilato
     * @return boolean
     */
    public boolean calcolaStatoComportamentaleDecoratoOsservazione(Automa A_out, Automa osservatore){
        
        StatoComportamentale sc = new StatoComportamentale();
        // devo cercare lo stato iniziale dell'automa osservatore
        String nome_stato_osservatore = "";
        for(Stato s : osservatore.getStati()){
            StatoRiconoscitore sr = (StatoRiconoscitore) s;
            if(sr.getIniziale()==true){
                nome_stato_osservatore = sr.getId();
                sc.setFinale(sr.getFinale());
            }
        }
        
        int[] conteggio = new int[1];
        conteggio[0]=0;
        sc.setId("a"+conteggio[0]+"");
        // ciclo sugli automi per trovare gli stati iniziali di ognuno
        this.getAutomi().forEach((a) -> {
            a.getStati().forEach((s) -> {
                if(s.getIniziale()){
                    sc.pushStato(s);
                }
            });
        });
        Evento eventoNull = new Evento();
        eventoNull.setNome("NULL");
        this.getLinks().forEach((l) -> {
            Coppia cp = new Coppia();
            cp.setLink(l.getNome());
            cp.setEvento(eventoNull);
            sc.pushCoppia(cp);
        });
        sc.setIniziale(true);
        sc.setStatoRiconoscitore(nome_stato_osservatore);
        A_out.pushStato(sc);
        
        conteggio[0]++;
        statoComportamentaleDecoratoRicorsivoOsservazione(A_out, sc, conteggio, osservatore);
        
        return true;
    }
    
    /**
     * Funzione ricorsiva che calcola tutti gli stati discendenti dal primo all'interno dell'automa StatoComportamentale decorato
     * @param A_out l'automa dello StatoComportamentale che si vuole venga compilato
     * @param sc_pre lo stato da cui parte la ricerca (al primo giro quello generato da "calcolaStatoComportamentale()")
     * @param conteggio un intero che indica il numero di stati generati dal sistema
     * @return boolean
     */
    private boolean statoComportamentaleDecoratoRicorsivoOsservazione(Automa A_out, StatoComportamentale sc_pre, int[] conteggio, Automa osservatore){
        Evento eventoNull = new Evento();
        eventoNull.setNome("NULL");
        boolean controllo_riconoscitore=false;
        // estraggo dall'osservatore le prossime etichette di osservabilità ammesse
        Map<Integer,String> etichette_ammesse = new HashMap<>();
        Map<Integer,String> id_stati_finali = new HashMap<>();
        int i = 0;
        for(Transizione t_o : osservatore.getTransizioni()){
            if(t_o.getIniziale().getId().equals(sc_pre.getStatoRiconoscitore())){
                etichette_ammesse.put(i, t_o.getOsservabilita());
                id_stati_finali.put(i, t_o.getFinale().getId());
            }
        }
        // cicliamo su automi e transizioni per poter scansionare tutte le possibili transizioni
        for (Automa a : this.getAutomi()) {
            for(Transizione tt : a.getTransizioni()){
                // casto a TransizioneStati perchè so che può essere solo quello
                TransizioneStati t = (TransizioneStati) tt;
                if(t.getOsservabilita().equals("NULL") || etichette_ammesse.containsValue(t.getOsservabilita())){
                    // controllo se lo stato iniziale della transizione considerata è tra gli stati attualmente attivi
                    if(sc_pre.getStati().contains(t.getIniziale())){
                        // Controlliamo che siano verificati i prerequisiti degli eventi in ingresso
                        if(t.getIngresso().getEvento().getNome().equals("NULL") || sc_pre.getCoppie().contains(t.getIngresso())){
                            boolean tmp = true;
                            for(Coppia u : t.getUscita()){
                                for (Coppia u2 : sc_pre.getCoppie()) {
                                    //Controlliamo che i link associati agli eventi in uscita siano liberi
                                    if (u.getLink().equals(u2.getLink()) && !(u2.getEvento().equals(eventoNull))){
                                        tmp = false;
                                        break;
                                    }
                                }
                                if (!tmp){
                                    break;
                                }
                            }
                            // entra nel seguente if solo se sono state verificate tutte le precondizioni
                            if(tmp){
                                // scaturisce l'evento e lancia una nuova istanza ricorsiva
                                StatoComportamentale sc = new StatoComportamentale();
                                sc.clone(sc_pre);
                                sc.setIniziale(false);
                                sc.setFinale(false);
                                sc.setId("a"+conteggio[0]+"");
                                // setta lo stato riconoscitore al quello puntato
                                if(!t.getOsservabilita().equals("NULL")){
                                    for (Map.Entry<Integer, String> entry : etichette_ammesse.entrySet()) {
                                        if(entry.getValue().equals(t.getOsservabilita())){
                                            sc.setStatoRiconoscitore(id_stati_finali.get(entry.getKey()));
                                        }
                                    }
                                }
                                

                                if(!(t.getRilevanza().equals("NULL")) && !(sc.getRilevanza().contains(t.getRilevanza()))){
                                    sc.pushRilevanza(t.getRilevanza());
                                }

                                // rimuovo lo stato iniziale della transizione dalla lista degli stati nel nuovo spazio comportamentale
                                if(sc.getStati().contains(t.getIniziale())){
                                    sc.getStati().remove(t.getIniziale());
                                }
                                // aggiungo alla lista degli stati del nuovo spazio comportamentale lo stato finale della transizione
                                Stato st =t.getFinale().clone();
                                st.setIniziale(true);
                                sc.pushStato(st);

                                // loop sulle coppie evento link in uscita alla transizione abilitata
                                if (!t.getIngresso().getEvento().getNome().equals("NULL")){
                                   for (Coppia cp_s : sc.getCoppie()){
                                       if (cp_s.getLink().equals(t.getIngresso().getLink())){
                                           cp_s.setEvento(eventoNull);
                                       }
                                   }
                                }
                                for(Coppia cp_u : t.getUscita()){
                                    for (Coppia cp_s : sc.getCoppie()){
                                        if (cp_u.getLink().equals(cp_s.getLink())){
                                            cp_s.setEvento(cp_u.getEvento());
                                        }
                                    }
                                }
                                // controlli per determinare se lo stato è finale
                                Integer cnt_void_link = 0;
                                for(Coppia cp : sc.getCoppie()){
                                    if(cp.getEvento().getNome().equals("NULL")){
                                        cnt_void_link++;
                                    }
                                }
                                for(Stato s_r : osservatore.getStati()){
                                    StatoRiconoscitore sr = (StatoRiconoscitore) s_r;
                                    if(sc.getStatoRiconoscitore().equals(sr.getId())){
                                        controllo_riconoscitore=sr.getFinale();
                                    }
                                }
                                // controllo se tutti i link sono vuoti
                                if(cnt_void_link == sc.getCoppie().size() && controllo_riconoscitore){
                                    sc.setFinale(true);
                                }else{
                                    sc.setFinale(false);
                                }

                                TransizioneComportamentale t_comp = new TransizioneComportamentale();
                                t_comp.setNome(t.getNome());
                                t_comp.setIniziale(sc_pre);
                                t_comp.setOsservabilita(t.getOsservabilita());
                                // controllo che il nuovo stato generato non sia già presente nell'automa dello stato comportamentale
                                int chk_count = 0;
                                for(Stato s_chk : A_out.getStati()){
                                    if(s_chk.equalsNotId(sc)){
                                        chk_count++;
                                        t_comp.setFinale(s_chk);
                                        A_out.pushTransizioni(t_comp);
                                        break;
                                    }
                                }

                                // se il ciclo sopra ha trovato uno stato uguale nell'albero già generato allora non entro nell'if sotto
                                if( chk_count==0){
                                    t_comp.setFinale(sc);
                                    // aggiunta dello stato all'Automa "statocomportamentale"
                                    A_out.pushStato(sc);
                                    A_out.pushTransizioni(t_comp);
                                    // chiamata ricorsiva
                                    conteggio[0]++;
                                    statoComportamentaleDecoratoRicorsivoOsservazione(A_out, sc, conteggio, osservatore);
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    
    
    /**
     * Funzione che calcola il primo Stato all'interno dell'automa chiamato StatoComportamentale decorato
     * @param A_out l'automa dello StatoComportamentale che si vuole venga compilato
     * @return boolean
     */
    public boolean calcolaStatoComportamentaleDecoratoVincolato(Automa A_out, Automa scenario){
        String stato_scenario = "";
        for(Stato s : scenario.getStati()){
            if(s.getIniziale()==true){
                stato_scenario = s.getId();
            }
        }
        
        StatoComportamentale sc = new StatoComportamentale();
        int[] conteggio = new int[1];
        conteggio[0]=0;
        sc.setId("a"+conteggio[0]+"");
        this.getAutomi().forEach((a) -> {
            a.getStati().forEach((s) -> {
                if(s.getIniziale()){
                    sc.pushStato(s);
                }
            });
        });
        Evento eventoNull = new Evento();
        eventoNull.setNome("NULL");
        this.getLinks().forEach((l) -> {
            Coppia cp = new Coppia();
            cp.setLink(l.getNome());
            cp.setEvento(eventoNull);
            sc.pushCoppia(cp);
        });
        sc.setFinale(true);
        sc.setIniziale(true);
        sc.setStatoScenario(stato_scenario);
        A_out.pushStato(sc);
        
        conteggio[0]++;
        
        calcolaStatoComportamentaleDecoratoVincolatoRicorsivo(A_out, sc, conteggio, scenario, stato_scenario);
        
        return true;
    }
    
    /**
     * Funzione ricorsiva che calcola tutti gli stati discendenti dal primo all'interno dell'automa StatoComportamentale decorato
     * @param A_out l'automa dello StatoComportamentale che si vuole venga compilato
     * @param sc_pre lo stato da cui parte la ricerca (al primo giro quello generato da "calcolaStatoComportamentale()")
     * @param conteggio un intero che indica il numero di stati generati dal sistema
     * @return boolean
     */
    private boolean calcolaStatoComportamentaleDecoratoVincolatoRicorsivo(Automa A_out, StatoComportamentale sc_pre, int[] conteggio, Automa scenario, String stato_scenario){
        ArrayList<TransizioneScenario> transizioni_scenario_abilitate = transizioni_abilitate_scenario(scenario, stato_scenario);

        Evento eventoNull = new Evento();
        eventoNull.setNome("NULL");
        // cicliamo su automi e transizioni per poter scansionare tutte le possibili transizioni
        for (Automa a : this.getAutomi()) {
            for(Transizione tt : a.getTransizioni()){
                // casto a TransizioneStati perchè so che può essere solo quello
                TransizioneStati t = (TransizioneStati) tt;
                StatoComportamentale stato_finale_scenario = new StatoComportamentale();
                if(controllo_transizioni_scenario(t, transizioni_scenario_abilitate, stato_finale_scenario)){
                    // controllo se lo stato iniziale della transizione considerata è tra gli stati attualmente attivi
                    if(sc_pre.getStati().contains(t.getIniziale())){
                        // Controlliamo che siano verificati i prerequisiti degli eventi in ingresso
                        if(t.getIngresso().getEvento().getNome().equals("NULL") || sc_pre.getCoppie().contains(t.getIngresso())){
                            boolean tmp = true;
                            for(Coppia u : t.getUscita()){
                                for (Coppia u2 : sc_pre.getCoppie()) {
                                    //Controlliamo che i link associati agli eventi in uscita siano liberi
                                    if (u.getLink().equals(u2.getLink()) && !(u2.getEvento().equals(eventoNull))){
                                        tmp = false;
                                        break;
                                    }
                                }
                                if (!tmp){
                                    break;
                                }
                            }
                            // entra nel seguente if solo se sono state verificate tutte le precondizioni
                            if(tmp){
                                // scaturisce l'evento e lancia una nuova istanza ricorsiva
                                StatoComportamentale sc = new StatoComportamentale();
                                sc.clone(sc_pre);
                                sc.setIniziale(false);
                                sc.setFinale(false);
                                sc.setId("a"+conteggio[0]+"");
                                sc.setStatoScenario(stato_finale_scenario.getId());
                                
                                if(!(t.getRilevanza().equals("NULL")) && !(sc.getRilevanza().contains(t.getRilevanza()))){
                                    sc.pushRilevanza(t.getRilevanza());
                                }

                                // rimuovo lo stato iniziale della transizione dalla lista degli stati nel nuovo spazio comportamentale
                                if(sc.getStati().contains(t.getIniziale())){
                                    sc.getStati().remove(t.getIniziale());
                                }
                                // aggiungo alla lista degli stati del nuovo spazio comportamentale lo stato finale della transizione
                                Stato st =t.getFinale().clone();
                                st.setIniziale(true);
                                sc.pushStato(st);

                                // loop sulle coppie evento link in uscita alla transizione abilitata
                                if (!t.getIngresso().getEvento().getNome().equals("NULL")){
                                   for (Coppia cp_s : sc.getCoppie()){
                                       if (cp_s.getLink().equals(t.getIngresso().getLink())){
                                           cp_s.setEvento(eventoNull);
                                       }
                                   }
                                }
                                for(Coppia cp_u : t.getUscita()){
                                    for (Coppia cp_s : sc.getCoppie()){
                                        if (cp_u.getLink().equals(cp_s.getLink())){
                                            cp_s.setEvento(cp_u.getEvento());
                                        }
                                    }
                                }
                                // controlli per determinare se lo stato è finale
                                Integer cnt_void_link = 0;
                                for(Coppia cp : sc.getCoppie()){
                                    if(cp.getEvento().getNome().equals("NULL")){
                                        cnt_void_link++;
                                    }
                                }
                                // controllo se tutti i link sono vuoti e lo stato attuale dello scenario è finale
                                if(cnt_void_link == sc.getCoppie().size() && stato_finale_scenario.getFinale()){
                                    sc.setFinale(true);
                                }else{
                                    sc.setFinale(false);
                                }

                                TransizioneComportamentale t_comp = new TransizioneComportamentale();
                                t_comp.setNome(t.getNome());
                                t_comp.setIniziale(sc_pre);
                                t_comp.setOsservabilita(t.getOsservabilita());
                                // controllo che il nuovo stato generato non sia già presente nell'automa dello stato comportamentale
                                int chk_count = 0;
                                for(Stato s_chk : A_out.getStati()){
                                    if(s_chk.equalsNotId(sc)){
                                        chk_count++;
                                        t_comp.setFinale(s_chk);
                                        A_out.pushTransizioni(t_comp);
                                        break;
                                    }
                                }

                                // se il ciclo sopra ha trovato uno stato uguale nell'albero già generato allora non entro nell'if sotto
                                if( chk_count==0){
                                    t_comp.setFinale(sc);
                                    // aggiunta dello stato all'Automa "statocomportamentale"
                                    A_out.pushStato(sc);
                                    A_out.pushTransizioni(t_comp);
                                    // chiamata ricorsiva
                                    conteggio[0]++;
                                    calcolaStatoComportamentaleDecoratoVincolatoRicorsivo(A_out, sc, conteggio, scenario, stato_finale_scenario.getId());
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    private boolean controllo_transizioni_scenario(TransizioneStati t, ArrayList<TransizioneScenario>transizioni_scenario_abilitate, StatoComportamentale stato_finale_scenario){
        for(TransizioneScenario ts : transizioni_scenario_abilitate){
            if(t.getNome().equals(ts.getNome())){
                stato_finale_scenario = (StatoComportamentale) ts.getFinale();
                return true;
            }
        }
        return false;
    }
    
    private ArrayList<TransizioneScenario> transizioni_abilitate_scenario(Automa scenario, String stato_scenario){
        ArrayList<TransizioneScenario> transizioni_scenario_abilitate = new ArrayList<>();
        for(Transizione tt : scenario.getTransizioni()){
            if(tt.getIniziale().getId().equals(stato_scenario)){
                transizioni_scenario_abilitate.add((TransizioneScenario)tt);
            }
        }
        return transizioni_scenario_abilitate;
    }
}
