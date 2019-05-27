package ReteAutomi;

import org.jdom.Element;

/**
 *
 * @author alessandro
 */
public class StatoSemplice implements Stato, Cloneable{
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
    
    /**
     * Funzione per colonare l'oggetto e non fare solo una copia del puntatore
     * @return StatoSemplice
     */
    @Override
    public StatoSemplice clone() {
        try {
            return (StatoSemplice) super.clone();
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
                StatoSemplice ss = (StatoSemplice) o;
                if(this.getId().equals(ss.getId()) && this.getIniziale().equals(ss.getIniziale())){
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
                StatoSemplice ss = (StatoSemplice) o;
                if(this.getIniziale().equals(ss.getIniziale())){
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
                StatoSemplice ss = (StatoSemplice) o;
                if(this.getId().equals(ss.getId())){
                    return true;
                }
            }
        }
        return false;
    }
    
}
