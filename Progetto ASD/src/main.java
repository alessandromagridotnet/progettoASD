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
            int i =0;
            
            ArrayList tmpArray = new ArrayList<String>();
            
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
            // controlla se sono presenti dei file nella cartella "input"
            if (directoryListing != null) {
                System.out.println("Segliere la rete automi che si vuole caricare: ");
                // stampa il menu
                for (File child : directoryListing) {
                    tmpArray.add(i, child.getPath());
                    i++;
                    System.out.println(i + ") per caricare " + child.getName());
                }
                System.out.println((i+1) + ") terminare il programma ");
                
                // attesa dell'input per la scelta del file da caricare
                while(!quit){
                    int scelta=in.nextInt();
                    in.nextLine();
                    
                    if(scelta >0 && scelta <= (directoryListing.length)){
                        ReteAutomi RA = new ReteAutomi();
                        RA.loadFromFile(tmpArray.get(scelta-1).toString());
                    }else if(scelta == (directoryListing.length +1)){
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






}
