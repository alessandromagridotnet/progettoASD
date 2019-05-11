/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import ReteAutomi.Automa;
import ReteAutomi.Link;
import ReteAutomi.ReteAutomi;

import java.io.*;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
/**
 *
 * @author alessandro
 */
public class main {

    /**
     * @param args the command line arguments
     */
        public static void main (String args[]) throws IOException,FileNotFoundException{
            
            menu_caricamento();
            
            
        }
        
        private static void menu_caricamento(){
            int i =0;
            ArrayList tmpArray = new ArrayList<>();
            
            Scanner in=new Scanner(System.in);
            boolean quit = false;
            
            File dir = null;
            
            // bugfix temporaneo per i path di linux e windows
            if(System.getProperty("os.name").compareTo("Linux") == 0){
                dir = new File("src/input/");
            }else{
                dir = new File("Progetto ASD\\src\\input\\");
            }
            File[] directoryListing = dir.listFiles();
            
            System.out.println("*******************************************************************************");
            System.out.println("******************************* MENU PRINCIPALE *******************************");
            System.out.println("*******************************************************************************");
            
            // controlla se sono presenti dei file nella cartella "input"
            if (directoryListing != null) {
                System.out.println("Segliere la rete automi che si vuole caricare: ");
                // stampa il menu
                for (File child : directoryListing) {
                    tmpArray.add(i, child.getPath());
                    i++;
                    System.out.println(i + ") per caricare " + child.getName());
                }
                System.out.println("0) terminare il programma ");
                
                // attesa dell'input per la scelta del file da caricare
                while(!quit){
                    int scelta=in.nextInt();
                    in.nextLine();
                    
                    if(scelta >0 && scelta <= (directoryListing.length)){
                        ReteAutomi RA = new ReteAutomi();
                        if(RA.loadFromFile(tmpArray.get(scelta-1).toString())){
                            // caricamento riuscito
                            menu_azioni(RA);
                        }else{
                            // caricamento non riuscito
                            System.out.println("Errore durante il caricamento.");
                            System.out.println("Programma terminato. Buona giornata.");
                            break;
                        }
                    }else if(scelta == 0){
                        // uscita dal programma
                        System.out.println("Programma terminato. Buona giornata.");
                        quit=true;
                    }else{
                        System.out.println("Opzione non disponibile.");
                    }
                }
            } else {
                System.out.println("Non sono presenti file nella cartella input!!");
            }
            System.out.println("*******************************************************************************");
        }

        private static void menu_azioni(ReteAutomi RA){
            Scanner in=new Scanner(System.in);
            boolean quit = false;
            
            System.out.println("*******************************************************************************");
            System.out.println("***************************** MENU SECONDO LIVELLO ****************************");
            System.out.println("*******************************************************************************");
            System.out.println("");
            System.out.println("1) per esportare la rete caricata ");
            System.out.println("0) per tornare al menu principale ");
            
            while(!quit){
                int scelta=in.nextInt();
                in.nextLine();
                
                switch(scelta){
                    case 1:
                        RA.storeIntoFile("src/output/output.xml");
                        break;
                    case 2:
                        for(Automa a : RA.getAutomi()){
                            System.out.println(a.toXML());
                        }
                        break;
                }  
            }
        }

}
