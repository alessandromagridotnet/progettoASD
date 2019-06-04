package ReteAutomi;


import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;

/**
 *
 * @author alessandro
 */
public class TransizioneStati implements Transizione{
    private String nome;
    private Stato iniziale, finale;
    private Coppia ingresso;
    private ArrayList<Coppia> uscita;
    private String osservabilita, rilevanza;

    public TransizioneStati() {
        this.uscita = new ArrayList<>();
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

    public Coppia getIngresso() {
        return ingresso;
    }

    public ArrayList<Coppia> getUscita() {return uscita;}

    public void setIngresso(Coppia ingresso) {
        this.ingresso = ingresso;
    }

    @Override
    public Stato getFinale() {
        return finale;
    }

    @Override
    public void setFinale(Stato finale) {
        this.finale = finale;
    }
    
    public void pushUscita(Coppia u){
        this.uscita.add(u);
    }

    public String getOsservabilita() {
        return osservabilita;
    }

    public void setOsservabilita(String osservabilita) {
        this.osservabilita = osservabilita;
    }

    public String getRilevanza() {
        return rilevanza;
    }

    public void setRilevanza(String rilevanza) {
        this.rilevanza = rilevanza;
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
            str += this.ingresso.toXML();
            str += "<CoppieUscita>" + System.lineSeparator();
                for(int i =0; i<this.getUscita().size(); i++){
                    Coppia cp = this.getUscita().get(i);
                    str += cp.toXML();
                }
            str += "</CoppieUscita>" + System.lineSeparator();
                str += "<Osservabilita>" + this.getOsservabilita() + "</Osservabilita>" + System.lineSeparator();
                str += "<Rilevanza>" + this.getRilevanza() + "</Rilevanza>" + System.lineSeparator();
        str += "</Transizione>" + System.lineSeparator();
        return str;
    }
    
    /**
     * Funzione per caricare una transizione partendo da un elemento XML e da una lista di stati disponibili
     * @param xml un elemento xml che contiene un LINK
     * @param stati la lista degli stati caricati tra cui cercare iniziale e finale
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
        if(!((Element)xml.getChild("Coppia")==null)){
            ingresso.fromXML((Element)xml.getChild("Coppia"));
        }
        this.setIngresso(ingresso);
        // uscite
        if(!((Element)xml.getChild("CoppieUscita")==null)){
            List listUscite = xml.getChild("CoppieUscita").getChildren("Coppia");
            for(int i=0; i < listUscite.size(); i++){
                Coppia coppia = new Coppia();
                coppia.fromXML((Element)listUscite.get(i));
                this.pushUscita(coppia);
            }
        }
        // etichette
        this.setOsservabilita(xml.getChildText("Osservabilita"));
        this.setRilevanza(xml.getChildText("Rilevanza"));
    }
}
