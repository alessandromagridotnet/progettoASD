package ReteAutomi;

import java.util.ArrayList;
import org.jdom.Element;

/**
 *
 * @author alessandro
 */
public class TransizioneComportamentale implements Transizione{
    private String nome;
    private Stato iniziale, finale;
    private String osservabilita, rilevanza;

    public TransizioneComportamentale() {
        this.iniziale = null;
        this.finale = null;
        this.rilevanza="NULL";
        this.osservabilita="NULL";
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
            str += "<Osservabilita>" + this.getOsservabilita()+ "</Osservabilita>" + System.lineSeparator();
        str += "</Transizione>" + System.lineSeparator();
        return str;
    }
    
    @Override
    public boolean equals(Object o){
        if(o!=null){
            if(this.getClass().isInstance(o)){
                TransizioneComportamentale tc = (TransizioneComportamentale) o;
                if(this.getNome().equals(tc.getNome()) 
                        && this.getOsservabilita().equals(tc.getOsservabilita())
                        && this.getRilevanza().equals(tc.getRilevanza())
                        && this.getIniziale().equals(tc.getIniziale())
                        && this.getFinale().equals(tc.getFinale())){
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean equalsRilevanzaEsclusa(TransizioneComportamentale tc){
        if(this.getNome().equals(tc.getNome()) 
                && ((StatoComportamentale)this.getIniziale()).equalsNotEtichette((StatoComportamentale)tc.getIniziale())
                && ((StatoComportamentale)this.getFinale()).equalsNotEtichette((StatoComportamentale)tc.getFinale())){
            return true;
        }
        return false;
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
        // etichette
        this.setOsservabilita(xml.getChildText("Osservabilita"));
        this.setRilevanza(xml.getChildText("Rilevanza"));
    }
}
