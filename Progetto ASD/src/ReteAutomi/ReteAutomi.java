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
     * @param fileNamepath path e nome del file in cui si vuole salvare l'xml generato
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
    
    public boolean calcolaStatoComportamentale(ReteAutomi RA_out){
        StatoComportamentale sc = new StatoComportamentale();
        this.getAutomi().forEach((a) -> {
            a.getStati().forEach((s) -> {
                if(s.getIniziale()){
                    sc.pushStato(s);
                }
            });
        });
        this.getLinks().forEach((l) -> {
            Coppia cp = new Coppia();
            cp.setLink(l.getNome());
            sc.pushCoppia(cp);
        });
        sc.setFinale(true);
        
        System.out.println(sc.toXML());
        
        return true;
    }
}
