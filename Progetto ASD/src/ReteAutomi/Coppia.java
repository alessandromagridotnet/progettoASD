package ReteAutomi;

import java.util.ArrayList;
import org.jdom.Element;

/**
 *
 * @author alessandro
 */
public class Coppia implements Cloneable{
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
    
    /**
     * Funzione che ritorna una stringa contenente la rappresentazione XML
     * @return String
     */
    public String toXML(){
        String xml = "";
        xml += "<Coppia>" + System.lineSeparator();
            if(this.getEvento()==null){
                xml += "<IdEvento>NULL</IdEvento>" + System.lineSeparator();
            }else{
                xml += "<IdEvento>" + this.getEvento().getNome() + "</IdEvento>" + System.lineSeparator();
            }
            xml += "<IdLink>" + this.getLink() + "</IdLink>" + System.lineSeparator();
        xml += "</Coppia>" + System.lineSeparator();
        
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
    
    @Override
    public boolean equals(Object o){
        if(o!=null){
            if(this.getClass().isInstance(o)){
                Coppia cc = (Coppia) o;
                
//                System.out.println(this.toXML());
//                System.out.println(cc.toXML());
//                System.out.println(" ");
                
                if(this.getLink().equals(cc.getLink()) || (this.getLink()==null && cc.getLink()==null)){
                    if(this.getEvento().equals(cc.getEvento())){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public Coppia clone() {
        try {
            return (Coppia) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
