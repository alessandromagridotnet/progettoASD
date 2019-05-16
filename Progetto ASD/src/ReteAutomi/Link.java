package ReteAutomi;

import java.util.ArrayList;
import org.jdom.Element;

/**
 *
 * @author alessandro
 */
public class Link {
    private String nome;
    private Automa partenza, arrivo;
//    private Evento eventoOn;

    public Link() {
        
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Automa getPartenza() {
        return partenza;
    }

    public void setPartenza(Automa partenza) {
        this.partenza = partenza;
    }

    public Automa getArrivo() {
        return arrivo;
    }

    public void setArrivo(Automa arrivo) {
        this.arrivo = arrivo;
    }

//    public Evento getEventoOn() {
//        return eventoOn;
//    }
//
//    public void setEventoOn(Evento eventoOn) {
//        this.eventoOn = eventoOn;
//    }
    
    /**
     * Funzione che ritorna una stringa contenente la rappresentazione XML
     * @return String
     */
    public String toXML(){
        String xml = "";
        xml += "<Link>" + System.lineSeparator();
            xml += "<Nome>" + this.getNome() + "</Nome>" + System.lineSeparator();
            xml += "<IdAutomaPartenza>" + this.getPartenza().getNome() + "</IdAutomaPartenza>" + System.lineSeparator();
            xml += "<IdAutomaArrivo>" + this.getArrivo().getNome() + "</IdAutomaArrivo>" + System.lineSeparator();
//            xml += this.getEventoOn().toXML();
        xml += "</Link>";
        
        return xml;
    }
    
    /**
     * Funzione per caricare un link partendo da un elemento XML e da una lista di automi disponibili
     * @param xml un elemento xml che contiene un LINK
     * @param automi la lista degli automi caricati tra cui cercare partenza e arrivo dei vari link
     */
    public void fromXML(Element xml, ArrayList<Automa> automi){
        this.setNome(xml.getChildText("Nome"));
        // trova l'automa di partenza dalla lista degli automi disponibili
        for(int i=0; i < automi.size(); i++){
            if(automi.get(i).getNome().equals(xml.getChildText("IdAutomaPartenza"))){
                this.setPartenza(automi.get(i));
                break;
            }
        }
        // trova l'automa di arrivo dalla lista degli automi disponibili
        for(int i=0; i < automi.size(); i++){
            if(automi.get(i).getNome().equals(xml.getChildText("IdAutomaArrivo"))){
                this.setArrivo(automi.get(i));
                break;
            }
        }
//        Evento e = new Evento();
//        e.fromXML(xml.getChild("Evento"));
//        this.setEventoOn(e);
    }
}
