package ReteAutomi;

import org.jdom.Element;

/**
 *
 * @author alessandro
 */
public class StatoSemplice implements Stato{
    private String id;
    private Boolean iniziale;

    public StatoSemplice() {
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Boolean getIniziale() {
        return iniziale;
    }
    
    @Override
    public void setIniziale(Boolean iniziale) {
        this.iniziale = iniziale;
    }
    
    /**
     * Funzione che ritorna una stringa contenente la rappresentazione XML
     * @return String
     */
    @Override
    public String toXML(){
        String xml = "";
        xml += "<Stato>" + System.lineSeparator();
            xml += "<ID>" + this.getId() + "</ID>" + System.lineSeparator();
            xml += "<Iniziale>" + this.getIniziale().toString() + "</Iniziale>" + System.lineSeparator();
        xml += "</Stato>" + System.lineSeparator();
        
        return xml;
    }
    
    public void fromXML(Element xml){
        this.setId(xml.getChildText("ID"));
        if(xml.getChildText("StatoIniziale").compareTo("true")==0){
           this.setIniziale(true);
        }else{
            this.setIniziale(false);
        }
    }
    
    public boolean equals(Stato s){
        boolean result = false;
        
        if(s != null && s instanceof StatoSemplice){
            if (this.getId().equals(s.getId())) {
                if(this.getIniziale() == s.getIniziale()){
                    result = true;
                }
            }
        }
        
        return result;
    }
}
