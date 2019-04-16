package ReteAutomi;

/**
 *
 * @author alessandro
 */
public class EtichettaRilevanza implements Etichetta{
    private String nome;

    public EtichettaRilevanza() {
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }
}
