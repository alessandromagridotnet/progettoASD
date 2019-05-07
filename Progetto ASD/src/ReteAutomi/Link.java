package ReteAutomi;

/**
 *
 * @author alessandro
 */
public class Link {
    private String nome;
    private Automa partenza, arrivo;

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
    
    public String toXML(){
        String xml = "";
        xml += "<Link>";
            xml += "<Nome>" + this.getNome() + "</Nome>";
            xml += "<IdAutomaPartenza>" + this.getPartenza().getNome() + "</IdAutomaPartenza>";
            xml += "<IdAutomaArrivo>" + this.getArrivo().getNome() + "</IdAutomaArrivo>";
        xml += "</Link>";
        
        return xml;
    }
}
