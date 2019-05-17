package ReteAutomi;
import org.jdom.*;
import org.jdom.input.*;

import java.util.List;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

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
            ReteAutomi rete =new ReteAutomi();
            rete.setNome(root.getChild("Nome").getText());

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
            out.print(this.toXML());
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
        xml += "<ReteAutomi>" + System.lineSeparator();
            xml += "<Nome>" + this.getNome() + "</Nome>" + System.lineSeparator();
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
        Integer conteggio = 0;
        sc.setId(conteggio.toString());
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
        
        A_out.pushStato(sc);
        
        statoComportamentaleRicorsivo(A_out, sc, ++conteggio);
        
        return true;
    }
    
    /**
     * Funzione ricorsiva che calcola tutti gli stati discendenti dal primo all'interno dell'automa StatoComportamentale
     * @param A_out l'automa dello StatoComportamentale che si vuole venga compilato
     * @param sc_pre lo stato da cui parte la ricerca (al primo giro quello generato da "calcolaStatoComportamentale()")
     * @param conteggio un intero che indica il numero di stati generati dal sistema
     * @return boolean
     */
    private boolean statoComportamentaleRicorsivo(Automa A_out, StatoComportamentale sc_pre, Integer conteggio){
        System.out.println("ENTRATO IN FUNZIONE RICORSIVA " + conteggio);
        
        Evento eventoNull = new Evento();
        eventoNull.setNome("NULL");
        for (Automa a : this.getAutomi()) {
            System.out.println(a.getNome());
            for(Transizione t : a.getTransizioni()){
                System.out.println(t.getNome()+"Transizione------------------------------------------------");
//                if(conteggio==3 && a.getNome().equals("C3")){
//                    System.out.println(t.getIniziale().toXML());
//                    System.out.println("___");
//                    for(Stato sssss : sc_pre.getStati()){
//                        System.out.println(sssss.toXML());
//                        System.out.println(t.getIniziale().equals(sssss));
//                    }
//                    
//                }
                
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

                            System.out.println("Entrato in "+t.getNome());
                            // scaturisce l'evento e lancia una nuova istanza ricorsiva
                            StatoComportamentale sc = sc_pre.clone();
                            // setta il nuovo nome (quello per la ridenominazione)
                            sc.setId(conteggio.toString());
                            // rimuovo lo stato iniziale della transizione dalla lista degli stati nel nuovo spazio comportamentale
                            if(sc.getStati().contains(t.getIniziale())){
                                sc.getStati().remove(t.getIniziale());
                            }
                            // aggiungo alla lista degli stati del nuovo spazio comportamentale lo stato finale della transizione
                            Stato st =t.getFinale().clone();
                            st.setIniziale(true);
                            sc.pushStato(st);
                            
                            // loop sulle coppie evento link in uscita alla transizione abilitata
                            if (t.getIngresso().getEvento().getNome()!="NULL"){
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
                            // aggiunta dello stato all'Automa "statocomportamentale"
                            A_out.pushStato(sc);
                            // chiamata ricorsiva
                            
//                            System.out.println(sc.toXML());
                            
                            statoComportamentaleRicorsivo(A_out, sc, ++conteggio);
                            System.out.println("ritorno: " + conteggio);
                        }
                    }
                }
            }
        }
        return true;
    }
}
