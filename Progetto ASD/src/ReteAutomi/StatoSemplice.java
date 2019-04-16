package ReteAutomi;

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
    
}
