package ReteAutomi;


import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;

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
    
    /**
     * Funzione che ritorna una stringa contenente la rappresentazione XML
     * @return String
     */
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
    
    /**
     * Funzione per caricare un link partendo da un elemento XML e da una lista di automi disponibili
     * @param xml un elemento xml che contiene un LINK
     */
    public void fromXML(Element xml){
        this.setNome(xml.getChildText("Nome"));
        // array degli stati
        List listStati = xml.getChild("Stati").getChildren("Stato");
        for (int j = 0; j < listStati.size(); j++) {
            StatoSemplice stato = new StatoSemplice();
            Element nodoStato = (Element) listStati.get(j);
            stato.fromXML(nodoStato);
            this.pushStato(stato);
        }
        // array delle transizioni
        List listTransizioni = xml.getChild("Transizioni").getChildren("Transizione");
        for (int j = 0; j < listTransizioni.size(); j++) {
            TransizioneStati transizione = new TransizioneStati();
            Element nodoTransizione = (Element) listTransizioni.get(j);
            transizione.fromXML(nodoTransizione, this.getStati());
            this.pushTransizioni(transizione);
        }
    }
    
    /**
     * Funzione per la potatura, richiamabile solo su un automa dello comportamentale
     * @return 
     */
    public boolean potatura(){
        boolean chk = false;
        boolean chk2 = false;
        for(Stato s : this.getStati()){
            StatoComportamentale sc = (StatoComportamentale) s;
            if(sc.getIniziale()){
                sc.setConfermato(2);
            }
        }
        for(Stato s : this.getStati()){
            StatoComportamentale sc = (StatoComportamentale) s;
            if(sc.getFinale()){
                sc.setConfermato(2);
                chk2 = potatura_ricorsiva(sc);
                chk = chk || chk2;
            }
        }
        int size = 0;
        while(size < this.getStati().size()){
            StatoComportamentale sc = (StatoComportamentale) this.getStati().get(size);
            if(!(sc.isConfermato() || sc.isVisitato())){
                this.getStati().remove(sc);
            }else{
                ++size;
            }
        }
        return chk;
    }
    
    private boolean potatura_ricorsiva(StatoComportamentale sc){
        ArrayList<StatoComportamentale> prec = precedenti(sc);
        boolean chk = false;
        boolean chk2 = false;
        // se non è uno stato già confermato lo imposto a stato visitato
        if(!sc.isConfermato()){
            sc.setConfermato(1);
        }
        // genero tutti i predecessori
        for(StatoComportamentale sc_prec : prec){
            // se il predecessore in esame è confermato confermo anche lo stato attuale
            // altrimenti richiamo ricorsivamente la funzione sol predecessore non visitato
            if(sc_prec.isConfermato()){
                sc.setConfermato(2);
                chk= true;
            }else if(!sc_prec.isVisitato()){
                // fatto su due righe per obbligare leseguzione della funzione
                chk2 = potatura_ricorsiva(sc_prec);
                chk = chk || chk2;
            }
        }
        return chk;
    }
    
    private ArrayList<StatoComportamentale> successivi(StatoComportamentale sc){
        ArrayList<StatoComportamentale> ret = new ArrayList<>();
        for(Transizione tt : this.getTransizioni()){
            TransizioneComportamentale t = (TransizioneComportamentale) tt;
            if(t.getIniziale().equals(sc)){
                StatoComportamentale temporaneo = (StatoComportamentale) this.getStati().get(this.getStati().indexOf(t.getFinale()));
                ret.add(temporaneo);
            }
        }
        return ret;
    }
    
    private ArrayList<StatoComportamentale> precedenti(StatoComportamentale sc){
//        System.out.println(sc.getId()+" - ");
        ArrayList<StatoComportamentale> ret = new ArrayList<>();
        for(Transizione tt : this.getTransizioni()){
            TransizioneComportamentale t = (TransizioneComportamentale) tt;
            if(t.getFinale().equals(sc)){
                StatoComportamentale temporaneo = (StatoComportamentale) this.getStati().get(this.getStati().indexOf(t.getIniziale()));
//                System.out.println(t.getNome()+" "+temporaneo.getId()+",");
                ret.add(temporaneo);
            }
        }
        return ret;
    }
}
