package ReteAutomi;

/**
 *
 * @author alessandro
 */
public class Coppia {
    private Evento evento;
    private Link link;

    public Coppia() {
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }
    
    public String toXML(){
        String xml = "";
        xml += "<Coppia>";
            xml += "<IdEvento>" + this.getEvento().getNome() + "</IdEvento>";
            xml += "<IdLink>" + this.getLink().getNome() + "</IdLink>";
        xml += "</Coppia>";
        
        return xml;
    }
}
