package ReteAutomi;


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
        this.stati = new ArrayList<>();
        this.transizioni = new ArrayList<>();
    }

    public String getNome() {
        return this.nome;
    }

    public ArrayList<Stato> getStati() {
        return this.stati;
    }
    
    public void pushStato(Stato s){
        this.stati.add(s);
    }

    public ArrayList<Transizione> getTransizioni() {
        return this.transizioni;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void pushTransizioni(Transizione t){
        this.transizioni.add(t);
    }
    
    public String toXML(){
        String xml = "";
        
        xml += "<Automa>" + System.lineSeparator();
            xml += "<Nome>" + this.getNome() + "</Nome>" + System.lineSeparator();
            xml += "<Stati>" + System.lineSeparator();
                for(Stato s : this.stati){
                    xml += s.toXML();
                }
            xml += "</Stati>" + System.lineSeparator();
            xml += "<Transizioni>" + System.lineSeparator();
                for(Transizione t : this.transizioni){
                    xml += t.toXML();
                }
            xml += "</Transizioni>" + System.lineSeparator();
        xml += "</Automa>" + System.lineSeparator();
        
        return xml;
    }
    
}
