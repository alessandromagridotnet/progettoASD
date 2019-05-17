package ReteAutomi;


import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;

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
        this.uscita = new ArrayList<Coppia>();
        this.iniziale = null;
        this.finale = null;
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

    public ArrayList<Coppia> getUscita() {return uscita;}

    public void setIngresso(Coppia ingresso) {
        this.ingresso = ingresso;
    }

    public Stato getFinale() {
        return finale;
    }

    public void setFinale(Stato finale) {
        this.finale = finale;
    }
    
    public void pushUscita(Coppia u){
        this.uscita.add(u);
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
    
    /**
     * Funzione che ritorna una stringa contenente la rappresentazione XML
     * @return String
     */
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
            str += this.ingresso.toXML();
            str += "<CoppieUscita>" + System.lineSeparator();
                for(int i =0; i<this.getUscita().size(); i++){
                    Coppia cp = this.getUscita().get(i);
                    str += cp.toXML();
                }
            str += "</CoppieUscita>" + System.lineSeparator();
        str += "</Transizione>" + System.lineSeparator();
        return str;
    }
    
    /**
     * Funzione per caricare un link partendo da un elemento XML e da una lista di automi disponibili
     * @param xml un elemento xml che contiene un LINK
     * @param automi la lista degli automi caricati tra cui cercare partenza e arrivo dei vari link
     */
    public void fromXML(Element xml, ArrayList<Stato> stati){
        this.setNome(xml.getChildText("Nome"));
        // trova lo stato iniziale dalla lista degli stati disponibili
        for(Stato s : stati){
            if(s.getId().equals(xml.getChildText("IdStatoIniziale"))){
                Stato s_tmp = s.clone();
                s_tmp.setIniziale(true);
                this.setIniziale(s_tmp);
                break;
            }
        }
        // trova lo stato finale dalla lista degli stati disponibili
        for(Stato s : stati){
            if(s.getId().equals(xml.getChildText("IdStatoFinale"))){
                Stato s_tmp = s.clone();
                s_tmp.setIniziale(false);
                this.setFinale(s_tmp);
                break;
            }
        }
        // ingresso
        Coppia ingresso = new Coppia();
        ingresso.fromXML((Element)xml.getChild("Coppia"));
        this.setIngresso(ingresso);
        // uscite
        List listUscite = xml.getChild("CoppieUscita").getChildren("Coppia");
        for(int i=0; i < listUscite.size(); i++){
            Coppia coppia = new Coppia();
            coppia.fromXML((Element)listUscite.get(i));
            this.pushUscita(coppia);
        }
    }
}
