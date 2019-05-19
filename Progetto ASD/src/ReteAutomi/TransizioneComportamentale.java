/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReteAutomi;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;

/**
 *
 * @author alessandro
 */
public class TransizioneComportamentale implements Transizione{
    private String nome;
    private Stato iniziale, finale;

    public TransizioneComportamentale() {
        this.iniziale = null;
        this.finale = null;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public Stato getIniziale() {
        return iniziale;
    }

    @Override
    public void setIniziale(Stato iniziale) {
        this.iniziale = iniziale;
    }

    @Override
    public Stato getFinale() {
        return finale;
    }

    @Override
    public void setFinale(Stato finale) {
        this.finale = finale;
    }
    
    /**
     * Funzione che ritorna una stringa contenente la rappresentazione XML
     * @return String
     */
    @Override
    public String toXML(){
        String str = "";
        
        str += "<Transizione>" + System.lineSeparator();
            str += "<Nome>" + this.getNome() + "</Nome>" + System.lineSeparator();
            if(this.getIniziale() == null){
                str += "<IdStatoIniziale>NULL</IdStatoIniziale>" + System.lineSeparator();
            }else{
                str += "<IdStatoIniziale>" + this.getIniziale().getId() + "</IdStatoIniziale>" + System.lineSeparator();
            }
            if(this.getFinale() == null){
                str += "<IdStatoFinale>NULL</IdStatoFinale>" + System.lineSeparator();
            }else{
                str += "<IdStatoFinale>" + this.getFinale().getId() + "</IdStatoFinale>" + System.lineSeparator();
            }
        str += "</Transizione>" + System.lineSeparator();
        return str;
    }
}
