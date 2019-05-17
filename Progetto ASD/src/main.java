/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ReteAutomi.ReteAutomi;
import ReteAutomi.Automa;
import ReteAutomi.StatoSemplice;
import ReteAutomi.Link;
import ReteAutomi.Transizione;
import ReteAutomi.Stato;
import ReteAutomi.Coppia;

import java.io.*;
import java.util.*;
/**
 *
 * @author alessandro
 */
public class main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
        public static void main (String args[]) throws IOException{
            
            menu_caricamento();
            System.out.println("-------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------");
            
            
        }
        
        private static void menu_caricamento() throws IOException{
            int i =0;
            ArrayList tmpArray = new ArrayList<>();
            boolean quit = false;
            File dir = null;
            
            // bugfix temporaneo per i path di linux e windows
            if(System.getProperty("os.name").compareTo("Linux") == 0){
                dir = new File("src/input/");
            }else{
                dir = new File("Progetto ASD\\src\\input\\");
            }
            File[] directoryListing = dir.listFiles();
            
            System.out.println("###############################################################################");
            System.out.println("############################### MENU PRINCIPALE ###############################");
            System.out.println("###############################################################################");
            
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
                    try{
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        int scelta = Integer.parseInt(br.readLine());

                        if(scelta >0 && scelta <= (directoryListing.length)){
                            ReteAutomi RA = new ReteAutomi();
                            if(RA.loadFromFile(tmpArray.get(scelta-1).toString())){
                                // caricamento riuscito
                                Boolean azioni = true;
                                while(azioni){
                                    azioni = menu_azioni(RA);
                                }
                                quit=true;
                                menu_caricamento();
                            }else{
                                // caricamento non riuscito
                                System.err.println("Errore durante il caricamento.");
                                System.out.println("Programma terminato. Buona giornata.");
                                quit=true;
                                break;
                            }
                        }else if(scelta == 0){
                            // uscita dal programma
                            System.out.println("Programma terminato. Buona giornata.");
                            quit=true;
                        }else{
                            System.err.println("Opzione non disponibile.");
                        }
                    }catch(NumberFormatException e){
                        System.err.println("Scelta non consentita. Inserire solo valori numerici consentiti!");
                    }
                }
            } else {
                System.out.println("Non sono presenti file nella cartella input!!");
            }
        }

        private static boolean menu_azioni(ReteAutomi RA) throws IOException{
            boolean quit = false;
            
            System.out.println("");
            System.out.println("*******************************************************************************");
            System.out.println("***************************** MENU SECONDO LIVELLO ****************************");
            System.out.println("*******************************************************************************");
            System.out.println("");
            System.out.println("1) per esportare la rete caricata ");
            System.out.println("2) per calcolare lo stato comportamentale ");
            System.out.println("3) per visualizzare la rete caricata ");
            System.out.println("0) per tornare al menu principale ");
            
            while(!quit){
                try{
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    int scelta = Integer.parseInt(br.readLine());

                    switch(scelta){
                        case 1:
                            return RA.storeIntoFile("src/output/output.xml");
                        case 2:
                            Automa A_out = new Automa();
                            RA.calcolaStatoComportamentale(A_out);
                            
//                            System.out.println(A_out.toXML());
                            
                            return true;
                        case 3:
                            mostraRete(RA);
                            break;
                        case 0:
                            return false;
                        default:
                            System.err.println("Scelta non consentita. Inserire solo valori numerici consentiti ");
                            break;
                    }
                }catch(NumberFormatException e){
                    System.err.println("Scelta non consentita. Inserire solo valori numerici consentiti!");
                }
            }
            return false;
        }
        
        private static boolean mostraRete (ReteAutomi RA){
            System.out.println("La rete"+RA.getNome()+" presenta questi elementi:");
            System.out.println("AUTOMI:");
            for (Automa automa : RA.getAutomi()) {
               System.out.println("Automa "+automa.getNome());
               System.out.println("Questo automa ha i seguenti stati:");
               for (Stato stato : automa.getStati()){
                    System.out.print("Stato "+stato.getId());
                    System.out.println("");
                    if (stato.getIniziale())
                        System.out.print("(Questo è lo stato iniziale)");
                        System.out.println("");
               }
                System.out.println("Questo automa ha le seguenti transizioni:");
               for(Transizione transizione: automa.getTransizioni()){
                   System.out.println("Transizione "+transizione.getNome());
                   System.out.println("La transizione parte dall'automa "+transizione.getIniziale().getId()+" e arriva all'automa "+transizione.getFinale().getId());
                   System.out.println("");
                   if (transizione.getIngresso() != null ) {
                       System.out.println("Questa transizione richiede in ingresso l'evento " + transizione.getIngresso().getEvento().getNome() + " trasportato nel link " + transizione.getIngresso().getLink());
                   }
                   if (transizione.getUscita().size()!=0){
                       for (Coppia coppia : transizione.getUscita()){
                           System.out.println("La transizione produce l'evento "+coppia.getEvento().getNome()+" da depositare sul link "+coppia.getLink());
                       }
                   }
                   System.out.println("");
                   System.out.println("");

               }


           }
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("I link presenti nella rete sono:");
            for (Link link : RA.getLinks()){
                System.out.println("Link "+link.getNome()+" che parte dall'automa "+link.getArrivo().getNome()+" e arriva all'automa "+link.getArrivo().getNome());
            }
            System.out.println("");
            System.out.println("");
            System.out.println("");
            return true;
        }

}
