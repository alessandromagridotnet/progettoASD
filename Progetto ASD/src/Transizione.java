
import java.util.ArrayList;

/**
 *
 * @author alessandro
 */
public class Transizione {
    private String nome;
    private Stato statoI, statoF;
    private Evento eventoI;
    private ArrayList<Evento> eventiO;
    private String CHERUFIGO;

    public Transizione() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Stato getStatoI() {
        return statoI;
    }

    public void setStatoI(Stato statoI) {
        this.statoI = statoI;
    }

    public Stato getStatoF() {
        return statoF;
    }

    public void setStatoF(Stato statoF) {
        this.statoF = statoF;
    }

    public Evento getEventoI() {
        return eventoI;
    }

    public void setEventoI(Evento eventoI) {
        this.eventoI = eventoI;
    }

    public ArrayList<Evento> getEventiO() {
        return eventiO;
    }

    public void setEventiO(ArrayList<Evento> eventiO) {
        this.eventiO = eventiO;
    }
    
}
