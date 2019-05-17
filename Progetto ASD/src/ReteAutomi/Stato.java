package ReteAutomi;

/**
 * Interfaccia per implementare lo Stato
 * @author Alessandro Magri
 */
public interface Stato {
    public String getId();
    public void setId(String id);
    public Boolean getIniziale();
    public void setIniziale(Boolean iniziale);
    public String toXML();
    public Stato clone();
    @Override
    public boolean equals(Object o);
    public boolean equalsNotId(Object o);
    public boolean equalsOnlyId(Object o);
}
