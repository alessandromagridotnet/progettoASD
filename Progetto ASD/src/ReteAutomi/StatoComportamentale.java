/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReteAutomi;

import java.util.ArrayList;

/**
 *
 * @author alessandro
 */
public class StatoComportamentale implements Stato, Cloneable {
    private String id;
    private Boolean iniziale;
    private Boolean finale;
    private ArrayList<Stato> stati;
    private ArrayList<Coppia> coppie;
    private int confermato; // 0 se è da rimuovere | 1 se è stato visitato | 2 se confermato

    public StatoComportamentale() {
        this.iniziale = false;
        this.finale = false;
        this.stati = new ArrayList<>();
        this.coppie = new ArrayList<>();
        this.confermato = 0;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Boolean getIniziale() {
        return iniziale;
    }
    
    @Override
    public void setIniziale(Boolean iniziale) {
        this.iniziale = iniziale;
    }

    public Boolean getFinale() {
        return finale;
    }

    public void setFinale(Boolean finale) {
        this.finale = finale;
    }
    
    public ArrayList<Stato> getStati() {
        return this.stati;
    }
    
    public void pushStato(Stato s){
        this.stati.add(s);
    }
    
    public ArrayList<Coppia> getCoppie() {
        return this.coppie;
    }
    
    public void pushCoppia(Coppia c){
        this.coppie.add(c);
    }

    public int getConfermato() {
        return confermato;
    }

    public void setConfermato(int confermato) {
        this.confermato = confermato;
    }
    
    /**
     * Funzione che ritorna una stringa contenente la rappresentazione XML
     * @return String
     */
    @Override
    public String toXML(){
        String xml = "";
        xml += "<Stato>" + System.lineSeparator();
            xml += "<ID>" + this.getId() + "</ID>" + System.lineSeparator();
            xml += "<Iniziale>" + this.getIniziale().toString() + "</Iniziale>" + System.lineSeparator();
            xml += "<Finale>" + this.getFinale().toString() + "</Finale>" + System.lineSeparator();
            xml += "<Stati>" + System.lineSeparator();
            for(Stato s : this.getStati()){
                xml += s.toXML();
            }
            xml += "</Stati>" + System.lineSeparator();
            xml += "<Coppie>" + System.lineSeparator();
            for(Coppia c : this.getCoppie()){
                xml += c.toXML();
            }
            xml += "</Coppie>" + System.lineSeparator();
        xml += "</Stato>" + System.lineSeparator();
        
        return xml;
    }
    
    @Override
    public StatoComportamentale clone() {
        try {
            return (StatoComportamentale) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void clone(StatoComportamentale sc) {
        this.setId(sc.getId());
        this.setFinale(sc.getFinale());
        this.setIniziale(sc.getIniziale());
        for(Coppia c : sc.getCoppie()){
            Coppia cp = c.clone();
            this.pushCoppia(cp);
        }
        for(Stato s : sc.getStati()){
            Stato ss = s.clone();
            this.pushStato(ss);
        }
    }
    
    /**
     * Verifica che l'oggetto passato sia effettivamente uguale all'istanza
     * @param StatoSemplice
     * @return boolean
     */
    @Override
    public boolean equals(Object o){
        if(o!=null){
            if(this.getClass().isInstance(o)){
                StatoComportamentale sc = (StatoComportamentale) o;
                if(this.getId().equals(sc.getId()) 
                    && this.getIniziale().equals(sc.getIniziale())
                    && this.getFinale().equals(sc.getFinale())
                    ){
                        for(Coppia cp : this.getCoppie()){
                            if(!(sc.getCoppie().contains(cp))){
                                return false;
                            }
                        }
                        for(Stato s :this.getStati()){
                            if(!(sc.getStati().contains(s))){
                                return false;
                            }
                        }
                        return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean equalsNotId(Object o){
        if(o!=null){
            if(this.getClass().isInstance(o)){
                StatoComportamentale sc = (StatoComportamentale) o;
                
                for(Coppia cp : this.getCoppie()){
                    if(!(sc.getCoppie().contains(cp))){
                        return false;
                    }
                }
                for(Stato s :this.getStati()){
                    boolean chk_count = true;
                    for(Stato s_chk : sc.getStati()){
                        if(s_chk.equalsOnlyId(s)){
                            chk_count=false;
                            break;

                        }
                    }
                    if(chk_count){
                        return false;
                    }
                }
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public boolean equalsOnlyId(Object o){
        if(o!=null){
            if(this.getClass().isInstance(o)){
                StatoComportamentale ss = (StatoComportamentale) o;
                if(this.getId().equals(ss.getId())){
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isConfermato(){
        return this.getConfermato()==2;
    }
    
    public boolean isVisitato(){
        return this.getConfermato()==1;
    }
}
