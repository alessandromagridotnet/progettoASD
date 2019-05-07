package ReteAutomi;

import java.util.ArrayList;
import org.jdom.Element;

/**
 *
 * @author alessandro
 */
public class Coppia {
    private Evento evento;
    private String link;

    public Coppia() {
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
    public String toXML(){
        String xml = "";
        xml += "<Coppia>";
            xml += "<IdEvento>" + this.getEvento().getNome() + "</IdEvento>";
            xml += "<IdLink>" + this.getLink() + "</IdLink>";
        xml += "</Coppia>";
        
        return xml;
    }
    
    /**
     * Funzione per caricare una coppia partendo da un elemento XML
     * @param xml un elemento xml che contiene un LINK
     */
    public void fromXML(Element xml){
        // evento
        Evento evt = new Evento();
        evt.setNome(xml.getChildText("IdEvento"));
        this.setEvento(evt);
        // link
        this.setLink(xml.getChildText("IdLink"));
    }
}
