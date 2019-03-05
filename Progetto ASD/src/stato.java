/**
 * Classe per rappresentare uno stato all'interno di un automa
 * @author Alessandro Magri
 */
public class stato {
    private String nome;

    public stato(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
