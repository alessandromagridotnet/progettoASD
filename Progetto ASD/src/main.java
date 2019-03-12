/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
/**
 *
 * @author alessandro
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
    }
    
    private void menu(){
        String scelta = new String();
        
        System.out.println("");
                
        switch(scelta){
            case "1":
                fileLoader("");
            break;
            case "100":
                System.out.println("Bye");
                System.exit(0);
            break;
        }
    }
    
    private ReteAutomi fileLoader(String path){
        ReteAutomi rete = new ReteAutomi();
        
        
        
        return rete;
    }
}
