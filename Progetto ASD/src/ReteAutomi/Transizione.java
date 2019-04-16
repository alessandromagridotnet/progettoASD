package ReteAutomi;


import java.util.ArrayList;

/**
 *
 * @author alessandro
 */
public class Transizione {
    private String nome;
    private Stato partenza, arrivo;
    private Coppia ingresso;
    private ArrayList<Coppia> uscita;
    private EtichettaOsservabilita eti_oss;
    private EtichettaRilevanza eti_rile;

    public Transizione() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
