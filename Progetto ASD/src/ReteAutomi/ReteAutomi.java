package ReteAutomi;


import java.io.File;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alessandro
 */
public class ReteAutomi {
    private String nome;
    private ArrayList<Link> links;
    private ArrayList<Automa> automi;

    public ReteAutomi() {
        
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public ArrayList<Automa> getAutomi() {
        return automi;
    }
    
    public void pushLink(Link l){
        this.links.add(l);
    }
    
    public void pushAutoma(Automa a){
        this.automi.add(a);
    }
    
    public void choseFileToLoad(){
        int i =0;
        File dir = new File("input/");
        File[] directoryListing = dir.listFiles();
        // controlla se sono presenti dei file nella cartella "input"
        if (directoryListing != null) {
            System.out.println("Segliere la rete automi che si vuole caricare: ");
            // stampa il menu
            for (File child : directoryListing) {
                i++;
                System.out.println(i + ") per caricare " + child.getName());
            }
        } else {
            System.out.println("Non sono presenti file nella cartella input!!");
        }
        System.out.println("*******************************************************************************");
    }
    
    public boolean loadFromFile(){
        
        
        
        return true;
    }
}
