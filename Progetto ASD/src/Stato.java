/**
 * Classe per rappresentare uno stato all'interno di un automa
 * @author Alessandro Magri
 */
public class Stato {
    private String nome;

    public Stato(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
