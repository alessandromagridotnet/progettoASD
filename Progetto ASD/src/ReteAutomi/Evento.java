package ReteAutomi;

import java.util.ArrayList;
import org.jdom.Element;

/**
 *
 * @author alessandro
 */
public class Evento {
    private String nome;

    public Evento()
    {
        this.setNome("");
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * Funzione che ritorna una stringa contenente la rappresentazione XML
     * @return String
     */
    public String toXML(){
        String xml = "";
        if(this == null){
            xml += "<Evento>" + System.lineSeparator();
                xml += "<Nome>NULL</Nome>" + System.lineSeparator();
            xml += "</Evento>";
        }else{
            xml += "<Evento>" + System.lineSeparator();
                xml += "<Nome>" + this.getNome() + "</Nome>" + System.lineSeparator();
            xml += "</Evento>";
        }
        
        return xml;
    }
    
    /**
     * Funzione per caricare un link partendo da un elemento XML
     * @param xml un elemento xml che contiene un EVENTO
     */
    public void fromXML(Element xml){
        if(xml.getChildText("Nome") == "NULL"){
            this.setNome(null);
        }else{
            this.setNome(xml.getChildText("Nome"));
        }
    }
    
    @Override
    public boolean equals(Object o){
        if(o!=null){
            if(this.getClass().isInstance(o)){
                Evento ee = (Evento) o;
                if(this.getNome().equals(ee.getNome())){
                    return true;
                }
            }
        }else{
            if(this.getNome().equals("NULL")){
                return true;
            }
        }
        return false;
    }
}
