
import java.util.ArrayList;

/**
 *
 * @author alessandro
 */
public class Automa {
    private String nome;
    private ArrayList<Stato> stati;
    private ArrayList<Transizione> transizioni;

    public Automa() {
        this.stati = new ArrayList<Stato>();
        this.transizioni = new ArrayList<Transizione>();
    }

    public String getNome() {
        return this.nome;
    }

    public ArrayList<Stato> getStati() {
        return this.stati;
    }

    public ArrayList<Transizione> getTransizioni() {
        return this.transizioni;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void pushStati(Stato s){
        this.stati.add(s);
    }
    
    public void pushTransizioni(Transizione t){
        this.transizioni.add(t);
    }
    
}
