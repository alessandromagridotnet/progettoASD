package ReteAutomi;
import org.jdom.*;
import org.jdom.input.*;

import java.util.Iterator;
import java.util.List;
import java.io.File;
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
    
    public void loadFromFile(String file) {
        try {

            System.out.println("il path è: " + file);
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(new File(file));
            Element root = document.getRootElement();
            ReteAutomi rete =new ReteAutomi();
            rete.setNome(root.getChild("Nome").getText());

            Element automi =root.getChild("Automi");
            List listAutomi = automi.getChildren("Automa");

            for (int i = 0; i < listAutomi.size(); i++)
            {
                Automa automa = new Automa();
                Element nodoAutoma = (Element) listAutomi.get(i);
                automa.setNome(nodoAutoma.getChildText("Nome"));
                Element stati =nodoAutoma.getChild("Stati");
                List listStati = stati.getChildren("Stato");
                for (int j = 0; j < listStati.size(); j++) {
                    StatoSemplice stato = new StatoSemplice();
                    Element nodoStato = (Element) listStati.get(i);
                    stato.setId(nodoStato.getChildText("ID"));
                    if (nodoStato.getChildText("StatoIniziale").compareTo("true")==0) {
                       stato.setIniziale(true);
                    }
                    else{
                        stato.setIniziale(false);
                    }





                }







            }



        } catch (Exception e) {
            System.err.println("Errore durante la lettura dal file");
            e.printStackTrace();
        }
    }


    
    public boolean storeIntoFile(String fileName){
        
        
        
        return true;
    }
}
