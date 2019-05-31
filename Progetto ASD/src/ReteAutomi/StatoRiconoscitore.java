package ReteAutomi;

import org.jdom.Element;

/**
 *
 * @author alessandro
 */
public class StatoRiconoscitore implements Stato, Cloneable{
    private String id;
    private Boolean iniziale, finale;

    public StatoRiconoscitore() {
        this.iniziale = false;
        this.finale = false;
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

    public Boolean getFinale() {
        return finale;
    }

    public void setFinale(Boolean finale) {
        this.finale = finale;
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
            xml += "<Finale>" + this.getFinale().toString() + "</Finale>" + System.lineSeparator();
        xml += "</Stato>" + System.lineSeparator();
        
        return xml;
    }
    
    public void fromXML(Element xml){
        this.setId(xml.getChildText("ID"));
        if(xml.getChildText("Iniziale").compareTo("true")==0){
           this.setIniziale(true);
        }else{
            this.setIniziale(false);
        }
        if(xml.getChildText("Finale").compareTo("true")==0){
           this.setFinale(true);
        }else{
            this.setFinale(false);
        }
    }
    
    /**
     * Funzione per colonare l'oggetto e non fare solo una copia del puntatore
     * @return StatoSemplice
     */
    @Override
    public StatoRiconoscitore clone() {
        try {
            return (StatoRiconoscitore) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Verifica che l'oggetto passato sia effettivamente uguale all'istanza
     * @param StatoSemplice
     * @return boolean
     */
    @Override
    public boolean equals(Object o){
        if(o!=null){
            if(this.getClass().isInstance(o)){
                StatoRiconoscitore ss = (StatoRiconoscitore) o;
                if(this.getId().equals(ss.getId()) 
                        && this.getIniziale().equals(ss.getIniziale())
                        && this.getFinale().equals(ss.getFinale())){
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean equalsNotId(Object o){
        if(o!=null){
            if(this.getClass().isInstance(o)){
                StatoRiconoscitore ss = (StatoRiconoscitore) o;
                if(this.getIniziale().equals(ss.getIniziale()) 
                        && this.getFinale().equals(ss.getFinale())){
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean equalsOnlyId(Object o){
        if(o!=null){
            if(this.getClass().isInstance(o)){
                StatoRiconoscitore ss = (StatoRiconoscitore) o;
                if(this.getId().equals(ss.getId())){
                    return true;
                }
            }
        }
        return false;
    }
    
}
