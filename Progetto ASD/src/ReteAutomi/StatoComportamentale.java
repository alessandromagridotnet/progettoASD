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
public class StatoComportamentale implements Stato{
    private String id;
    private Boolean iniziale;
    private Boolean finale;
    private ArrayList<Stato> stati;
    private ArrayList<Coppia> coppie;

    public StatoComportamentale() {
        this.stati = new ArrayList<Stato>();
        this.coppie = new ArrayList<Coppia>();
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
    
    public String toXML(){
        String xml = "";
        
        
        
        return xml;
    }
}
