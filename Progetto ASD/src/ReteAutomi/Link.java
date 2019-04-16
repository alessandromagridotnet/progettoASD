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
    
    
}
