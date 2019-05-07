package ReteAutomi;


import java.util.ArrayList;

/**
 *
 * @author alessandro
 */
public class Transizione {
    private String nome;
    private Stato iniziale, finale;
    private Coppia ingresso;
    private ArrayList<Coppia> uscita;
    private EtichettaOsservabilita eti_oss;
    private EtichettaRilevanza eti_rile;

    public Transizione() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Stato getIniziale() {
        return iniziale;
    }

    public void setIniziale(Stato iniziale) {
        this.iniziale = iniziale;
    }

    public Coppia getIngresso() {
        return ingresso;
    }

    public void setIngresso(Coppia ingresso) {
        this.ingresso = ingresso;
    }

    public Stato getFinale() {
        return finale;
    }

    public void setFinale(Stato finale) {
        this.finale = finale;
    }

    public EtichettaOsservabilita getEti_oss() {
        return eti_oss;
    }

    public void setEti_oss(EtichettaOsservabilita eti_oss) {
        this.eti_oss = eti_oss;
    }

    public EtichettaRilevanza getEti_rile() {
        return eti_rile;
    }

    public void setEti_rile(EtichettaRilevanza eti_rile) {
        this.eti_rile = eti_rile;
    }
    
    public String toXML(){
        String str = "";
        
        str += "<Transizione>";
            str += "<Nome>" + this.getNome() + "</Nome>";
            str += "<IdStatoIniziale>" + this.getIniziale().getId() + "</IdStatoIniziale>";
            str += "<IdStatoFinale>" + this.getFinale().getId() + "</IdStatoFinale>";
        str += "</Transizione>";
        return str;
    }
}
