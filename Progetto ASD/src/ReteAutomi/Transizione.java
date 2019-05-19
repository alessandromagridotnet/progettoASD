package ReteAutomi;

/**
 * Interfaccia per implementare lo Stato
 * @author Alessandro Magri
 */
public interface Transizione {
    public String getNome();
    public void setNome(String nome);
    public Stato getIniziale();
    public void setIniziale(Stato iniziale);
    public Stato getFinale();
    public void setFinale(Stato finale);
    public String toXML();
}
