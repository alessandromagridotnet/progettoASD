package ReteAutomi;

/**
 *
 * @author alessandro
 */
public class EtichettaOsservabilita implements Etichetta{
    private String nome;

    public EtichettaOsservabilita(String nome) {
        this.nome = nome;
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
