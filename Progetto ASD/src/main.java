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
import ReteAutomi.TransizioneStati;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;
/**
 *
 * @author alessandro
 */
public class main {
    
    private static Instant startTime;

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
        System.out.println("1) per visualizzare la rete caricata ");
        System.out.println("2) per calcolare lo spazio comportamentale ");
        System.out.println("3) potatura dello spazio comportamentale ");
        System.out.println("4) per calcolare lo spazio comportamentale decorato");
        System.out.println("5) per calcolare lo spazio comportamentale decorato con metodo alternativo");
//        System.out.println("5) per calcolare la determinizzazione");
        System.out.println("6) per la lettura della diagnosi relativa ad un'osservazione lineare su un dizionario");
        System.out.println("7) per la generazione dello spazio comportamentale relativo ad un'osservazione");
        System.out.println("8) per la generazione dello spazio comportamentale determinizzato relativo ad un'osservazione");
        System.out.println("9) per la lettura della diagnosi relativa ad un'osservazione lineare sullo spazio comportamentale determinizzato relativo ad un'osservazione");
        System.out.println("10) fusione di dizionari parziali");
        System.out.println("11) ricerca osservazione in una fusione di dizionari");
        System.out.println("12) calcolo spazio comportamentale vincolato da scenario");

        System.out.println("0) per tornare al menu principale ");
        
        boolean continua = true;
        String diagnosi;
        String[] osservazione_lineare;
        Automa A_out = new Automa();
        Automa automa_osservazione = new Automa();
        ArrayList<Automa> automa_osservazione_array = new ArrayList<>();
        Automa automa_scenario = new Automa();
        ReteAutomi tmp = new ReteAutomi();
        Automa A_tmp;
        ArrayList<Automa> automi; // automi di appoggio

        String dir;
        // bugfix temporaneo per i path di linux e windows
        if(System.getProperty("os.name").compareTo("Linux") == 0){
            dir = "src/output/";
        }else{
            dir = "Progetto ASD\\src\\output\\";
        }
        while(!quit){
            try{
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                int scelta = Integer.parseInt(br.readLine());

                switch(scelta){
                    case 1:
                        return mostraRete(RA);
//                            return RA.storeIntoFile(dir + "output.xml");
                    case 2:
                        A_out = new Automa();
                        startTime = Instant.now();
                        RA.calcolaStatoComportamentale(A_out);
                        System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                        tmp = new ReteAutomi();
                        tmp.pushAutoma(A_out);
                        System.out.println("");
                        System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE ------");
                        return tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale.xml");
                    case 3:
                        A_out = new Automa();
                        startTime = Instant.now();
                        RA.calcolaStatoComportamentale(A_out);
                        System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                        tmp = new ReteAutomi();
                        tmp.pushAutoma(A_out);
                        System.out.println("");
                        System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE ------");
                        if(tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale.xml") && proseguire()){
                            startTime = Instant.now();
                            A_out.potatura();
                            System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                            tmp=null;
                            tmp = new ReteAutomi();
                            tmp.pushAutoma(A_out);
                            System.out.println("");
                            System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE POTATO ------");
                            return tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_potato.xml");
                        }else{
                            return true;
                        }
                    case 4:
                        A_out = new Automa();
                        startTime = Instant.now();
                        RA.calcolaStatoComportamentaleDecorato(A_out);
                        System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                        tmp = new ReteAutomi();
                        tmp.pushAutoma(A_out);
                        System.out.println("");
                        System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE DECORATO ------");
                        if(tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_decorato.xml") && proseguire()){
                            startTime = Instant.now();
                            A_out.potatura();
                            System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                            tmp = null;
                            tmp = new ReteAutomi();
                            tmp.pushAutoma(A_out);
                            System.out.println("");
                            System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE DECORATO POTATO ------");
                            return tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_decorato_potato.xml");
                        }else{
                            return true;
                        }
                    case 5:
                        A_out = new Automa();
                        startTime = Instant.now();
                        RA.calcolaStatoComportamentale(A_out);
                        System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                        tmp = new ReteAutomi();
                        tmp.pushAutoma(A_out);
                        System.out.println("");
                        System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE ------");
                        if(tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale.xml") && proseguire()){
                            startTime = Instant.now();
                            A_out.potatura();
                            System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                            tmp=null;
                            tmp = new ReteAutomi();
                            tmp.pushAutoma(A_out);
                            System.out.println("");
                            System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE POTATO ------");
                            if(tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_potato.xml") && proseguire()){
                                A_tmp = new Automa();
                                startTime = Instant.now();
                                A_out.decorazioneStatoComportamentale(A_tmp);
                                System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                                tmp = null;
                                tmp = new ReteAutomi();
                                tmp.pushAutoma(A_tmp);
                                System.out.println("");
                                System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE DECORATO CON METODO ALTERNATIVO ------");
                                return tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_decorato_alternativo.xml");
                            }
                        }else{
                            return true;
                        }
//                    case 5:
//                        A_tmp = new Automa();
//                        A_out = new Automa();
//                        RA.calcolaStatoComportamentaleDecorato(A_tmp);
//                        tmp = new ReteAutomi();
//                        tmp.pushAutoma(A_tmp);
//                        System.out.println("");
//                        System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE DECORATO ------");
//                        if(tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_decorato.xml") && proseguire()){
//                            A_tmp.potatura();
//                            tmp = null;
//                            tmp = new ReteAutomi();
//                            tmp.pushAutoma(A_tmp);
//                            System.out.println("");
//                            System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE DECORATO POTATO ------");
//                            if(tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_decorato_potato.xml") && proseguire()){
//                                A_tmp.determinizzazione(A_out);
//                                tmp = null;
//                                tmp = new ReteAutomi();
//                                tmp.pushAutoma(A_out);
//                                System.out.println("");
//                                System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE DECORATO DETERMINIZZATO ------");
//                                return tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_decorato_determinizzato.xml");
//                            }else{
//                                break;
//                            }
//                        }else{
//                            break;
//                        }
                    case 6:
                        A_tmp = new Automa();
                        A_out = new Automa();
                        startTime = Instant.now();
                        RA.calcolaStatoComportamentaleDecorato(A_tmp);
                        System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                        tmp = new ReteAutomi();
                        tmp.pushAutoma(A_tmp);
                        System.out.println("");
                        System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE DECORATO ------");
                        if(tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_decorato.xml") && proseguire()){
                            startTime = Instant.now();
                            A_tmp.potatura();
                            System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                            tmp = null;
                            tmp = new ReteAutomi();
                            tmp.pushAutoma(A_tmp);
                            System.out.println("");
                            System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE DECORATO POTATO ------");
                            if(tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_decorato_potato.xml") && proseguire()){
                                startTime = Instant.now();
                                A_tmp.determinizzazione(A_out);
                                System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                                tmp = null;
                                tmp = new ReteAutomi();
                                tmp.pushAutoma(A_out);
                                System.out.println("");
                                System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE DECORATO DETERMINIZZATO ------");
                                if(tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_decorato_determinato.xml") && proseguire("Si desidera inserire un'osservazione lineare da cui estrarre una diagnosi?", "Elaborazione interrotta!")){
                                    osservazione_lineare = acquisizione_osservazione_lineare();
                                    startTime = Instant.now();
                                    diagnosi = A_out.ricerca_dizionario(osservazione_lineare);
                                    System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                                    System.out.println("");
                                    System.out.println("----- SALVATAGGIO DELLA DIAGNOSI ------");
                                    return storeIntoFile(dir + RA.getNome() + "-diagnosi.txt", diagnosi);
                                }
                            }else{
                                return true;
                            }
                        }else{
                            return true;
                        }
                    case 7:
                        A_out = new Automa();
                        automa_osservazione = new Automa();
                        if(caricaOsservazione(automa_osservazione) && proseguire()){
                            startTime = Instant.now();
                            RA.calcolaStatoComportamentaleDecoratoOsservazione(A_out, automa_osservazione);
                            System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                            tmp = new ReteAutomi();
                            tmp.pushAutoma(A_out);
                            System.out.println("");
                            System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE RELATIVO AD UNA OSSERVAZIONE ------");
                            if(tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_osservazione.xml") && proseguire()){
                                startTime = Instant.now();
                                A_out.potatura();
                                System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                                tmp = null;
                                tmp = new ReteAutomi();
                                tmp.pushAutoma(A_out);
                                System.out.println("");
                                System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE POTATO RELATIVO AD UNA OSSERVAZIONE ------");
                                return tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_osservazione_potato.xml");
                            }else{
                                return true;
                            }
                        }else{
                            return true;
                        }
                    case 8:
                        A_out = new Automa();
                        A_tmp = new Automa();

                        automa_osservazione = new Automa();
                        if(caricaOsservazione(automa_osservazione) && proseguire()){
                            startTime = Instant.now();
                            RA.calcolaStatoComportamentaleDecoratoOsservazione(A_out, automa_osservazione);
                            System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                            tmp = new ReteAutomi();
                            tmp.pushAutoma(A_out);
                            System.out.println("");
                            System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE RELATIVO AD UNA OSSERVAZIONE ------");
                            if(tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_osservazione.xml") && proseguire()){
                                startTime = Instant.now();
                                A_out.potatura();
                                System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                                tmp = null;
                                tmp = new ReteAutomi();
                                tmp.pushAutoma(A_out);
                                System.out.println("");
                                System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE POTATO RELATIVO AD UNA OSSERVAZIONE ------");
                                if(tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_osservazione_potato.xml") && proseguire()){
                                    startTime = Instant.now();
                                    A_out.determinizzazione(A_tmp);
                                    System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                                    tmp = null;
                                    tmp = new ReteAutomi();
                                    tmp.pushAutoma(A_tmp);
                                    System.out.println("");
                                    System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE POTATO RELATIVO AD UNA OSSERVAZIONE E DETERMINIZZATO ------");
                                    return tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_determinizzazione_osservazione.xml");
                                }
                            }else{
                                return true;
                            }
                        }else{
                            return true;
                        }
                    case 9:
                        A_out = new Automa();
                        A_tmp = new Automa();
                        automa_osservazione = new Automa();
                        if(caricaOsservazione(automa_osservazione) && proseguire()){
                            startTime = Instant.now();
                            RA.calcolaStatoComportamentaleDecoratoOsservazione(A_out, automa_osservazione);
                            System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                            tmp = new ReteAutomi();
                            tmp.pushAutoma(A_out);
                            System.out.println("");
                            System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE RELATIVO AD UNA OSSERVAZIONE ------");
                            if(tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_osservazione.xml") && proseguire()){
                                startTime = Instant.now();
                                A_out.potatura();
                                System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                                tmp = null;
                                tmp = new ReteAutomi();
                                tmp.pushAutoma(A_out);
                                System.out.println("");
                                System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE POTATO RELATIVO AD UNA OSSERVAZIONE ------");
                                if(tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_osservazione_potato.xml") && proseguire()){
                                    startTime = Instant.now();
                                    A_out.determinizzazione(A_tmp);
                                    System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                                    tmp = null;
                                    tmp = new ReteAutomi();
                                    tmp.pushAutoma(A_tmp);
                                    System.out.println("");
                                    System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE POTATO RELATIVO AD UNA OSSERVAZIONE E DETERMINIZZATO ------");
                                    if(tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_determinizzazione_osservazione.xml") && proseguire()){
                                        osservazione_lineare = acquisizione_osservazione_lineare();
                                        startTime = Instant.now();
                                        diagnosi = A_tmp.ricerca_dizionario(osservazione_lineare);
                                        System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                                        System.out.println("");
                                        System.out.println("----- SALVATAGGIO DELLA DIAGNOSI ------");
                                        return storeIntoFile(dir + RA.getNome() + "-diagnosi.txt", diagnosi);
                                    }else{
                                        return true;
                                    }
                                }
                            }else{
                                return true;
                            }
                        }else{
                            return true;
                        }
                    case 10:
                        automa_osservazione_array = new ArrayList<>();
                        automi = new ArrayList<>();
                        continua = true;
                        while(continua){
                            automa_osservazione = null;
                            automa_osservazione = new Automa();
                            if(caricaOsservazione(automa_osservazione)){
                                automa_osservazione_array.add(automa_osservazione);
                                continua = proseguire("Acquisire una ulteriore osservazione?", "");
                            }else{
                                continua = false;
                            }
                        }
                        if(automa_osservazione_array.size()<=1){
                            System.out.println("Hai caricato " + automa_osservazione_array.size() + " osservazioni. Non ha senso proseguire con l'elaborazione.");
                            return true;
                        }else{
                            for(Automa osservazione : automa_osservazione_array){
                                A_out = null; 
                                A_out = new Automa();
                                A_tmp = null;
                                A_tmp = new Automa();
                                startTime = Instant.now();
                                RA.calcolaStatoComportamentaleDecoratoOsservazione(A_out, osservazione);
                                A_out.potatura();
                                A_out.determinizzazione(A_tmp);
                                A_tmp.ridenominazione("x");
                                automi.add(A_tmp);
                                System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                                System.out.println("Aggiunta osservazione n°: " + automi.size());
                                if(!proseguire()){
                                    return true;
                                }
                            }
                            A_out = null;
                            A_out = new Automa();
                            startTime = Instant.now();
                            A_out.fusione_dizionari(automi);
                            System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                            tmp = new ReteAutomi();
                            tmp.pushAutoma(A_out);

                            System.out.println("");
                            System.out.println("----- SALVATAGGIO RELATIVO ALLA FUSIONE DIZIONARI ------");
                            if(tmp.storeIntoFile(dir + RA.getNome() + "-fusione_dizionari.xml") && proseguire()){
                                A_tmp = null;
                                A_tmp = new Automa();
                                startTime = Instant.now();
                                A_out.determinizzazione(A_tmp);
                                System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                                tmp = new ReteAutomi();
                                tmp.pushAutoma(A_tmp);

                                System.out.println("");
                                System.out.println("----- SALVATAGGIO RELATIVO ALLA DETERMINIZZAZIONE DELLA FUSIONE DIZIONARI ------");
                                return tmp.storeIntoFile(dir + RA.getNome() + "-determinizzazione_fusione_dizionari.xml");
                            }else{
                                return true;
                            }
                        }
                    case 11:
                        automa_osservazione_array = new ArrayList<>();
                        automi = new ArrayList<>();
                        continua = true;
                        while(continua){
                            automa_osservazione = null;
                            automa_osservazione = new Automa();
                            if(caricaOsservazione(automa_osservazione)){
                                automa_osservazione_array.add(automa_osservazione);
                                continua = proseguire("Acquisire una ulteriore osservazione?", "");
                            }else{
                                continua = false;
                            }
                        }
                        if(automa_osservazione_array.size()<=1){
                            System.out.println("Hai caricato " + automa_osservazione_array.size() + " osservazioni. Non ha senso proseguire con l'elaborazione.");
                            return true;
                        }else{
                            for(Automa osservazione : automa_osservazione_array){
                                startTime = Instant.now();
                                A_out = null; 
                                A_out = new Automa();
                                A_tmp = null;
                                A_tmp = new Automa();
                                RA.calcolaStatoComportamentaleDecoratoOsservazione(A_out, osservazione);
                                A_out.potatura();
                                A_out.determinizzazione(A_tmp);
                                A_tmp.ridenominazione("x");
                                automi.add(A_tmp);
                                System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                                System.out.println("Aggiunta osservazione n°: " + automi.size());
                                if(!proseguire()){
                                    return true;
                                }
                            }
                            A_out = null;
                            A_out = new Automa();
                            startTime = Instant.now();
                            A_out.fusione_dizionari(automi);
                            System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                            tmp = new ReteAutomi();
                            tmp.pushAutoma(A_out);

                            System.out.println("");
                            System.out.println("----- SALVATAGGIO RELATIVO ALLA FUSIONE DIZIONARI ------");
                            if(tmp.storeIntoFile(dir + RA.getNome() + "-fusione_dizionari.xml") && proseguire()){
                                A_tmp = null;
                                A_tmp = new Automa();
                                startTime = Instant.now();
                                A_out.determinizzazione(A_tmp);
                                System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                                tmp = new ReteAutomi();
                                tmp.pushAutoma(A_tmp);

                                System.out.println("");
                                System.out.println("----- SALVATAGGIO RELATIVO ALLA DETERMINIZZAZIONE DELLA FUSIONE DIZIONARI ------");
                                if(tmp.storeIntoFile(dir + RA.getNome() + "-determinizzazione_fusione_dizionari.xml") && proseguire()){
                                    A_tmp.estrai_diagnosi();
                                    osservazione_lineare = acquisizione_osservazione_lineare();
                                    startTime = Instant.now();
                                    diagnosi = A_tmp.ricerca_dizionario(osservazione_lineare);
                                    System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                                    System.out.println("");
                                    System.out.println("----- SALVATAGGIO DELLA DIAGNOSI ------");
                                    return storeIntoFile(dir + RA.getNome() + "-diagnosi.txt", diagnosi);
                                }else{
                                    return true;
                                }
                            }else{
                                return true;
                            }
                        }
                    case 12:
                        automa_scenario = new Automa();
                        if(caricaScenario(automa_scenario) && proseguire()){
                            A_out = new Automa();
                            A_tmp = new Automa();
                            startTime = Instant.now();
                            RA.calcolaStatoComportamentaleDecoratoVincolato(A_out, automa_scenario);
                            System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                            tmp = new ReteAutomi();
                            tmp.pushAutoma(A_out);
                            System.out.println("");
                            System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE VINCOLATO DA UNO SCENARIO ------");
                            if(tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_vincolato_scenario.xml") && proseguire()){
                                startTime = Instant.now();
                                A_out.potatura();
                                A_out.ridenominazione("");
                                System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                                tmp = null;
                                tmp = new ReteAutomi();
                                tmp.pushAutoma(A_out);
                                System.out.println("");
                                System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE VINCOLATO DA UNO SCENARIO POTATO E RIDENOMINATO ------");
                                if(tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_vincolato_scenario_potato.xml") && proseguire()){
                                    startTime = Instant.now();
                                    A_out.determinizzazione(A_tmp);
                                    System.out.println("L'elaborazione è durata: " + Integer.toString(getUsedTimeMilliSeconds()) + " millisecondi");
                                    tmp = null;
                                    tmp = new ReteAutomi();
                                    tmp.pushAutoma(A_tmp);
                                    System.out.println("");
                                    System.out.println("----- SALVATAGGIO RELATIVO ALLO SPAZIO COMPORTAMENTALE VINCOLATO DA UNO SCENARIO DETERMINIZZATO ------");
                                    return tmp.storeIntoFile(dir + RA.getNome() + "-spazio_comportamentale_vincolato_scenario_determinizzato.xml");
                                }else{
                                    return true;
                                }
                            }else{
                                return true;
                            }
                        }else{
                            return true;
                        }
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

    /**
     * Funzione per l'acquisizione da linea di comando dell'osservazione lineare da cercare
     * @return 
     */
    private static String[] acquisizione_osservazione_lineare(){
        String[] ret_arr;
        //
        Scanner scanner = new Scanner(System.in);
        String osservazione = "";

        while(!(osservazione.startsWith("<") && osservazione.endsWith(">"))){
            System.out.println("Inserire l'osservazione lineare desiderata nel formato <o1,o2,o3,...>");
            osservazione = scanner.nextLine();
        }

        osservazione = osservazione.substring(1, osservazione.length()-1);
        ret_arr = osservazione.split(",");
        return ret_arr;
    }

    private static boolean mostraRete (ReteAutomi RA){
        try{
            System.out.println("La rete "+RA.getNome()+" presenta questi elementi:");
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
               for(Transizione tt: automa.getTransizioni()){
                    // casto a TransizioneStati perchè so che può essere solo quello
                    TransizioneStati transizione = (TransizioneStati) tt;
                   System.out.println("Transizione "+transizione.getNome());
                   System.out.println("La transizione parte dallo stato "+transizione.getIniziale().getId()+" e arriva allo stato "+transizione.getFinale().getId());
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
        } catch(Exception e){
            return false;
        }
    }

    private static boolean caricaOsservazione(Automa auto) throws IOException{

        int i =0;
        ArrayList tmpArray = new ArrayList<>();
        boolean quit = false;
        File dir = null;

        // bugfix temporaneo per i path di linux e windows
        if(System.getProperty("os.name").compareTo("Linux") == 0){
            dir = new File("src/riconoscitori/");
        }else{
            dir = new File("Progetto ASD\\src\\riconoscitori\\");
        }
        File[] directoryListing = dir.listFiles();

        System.out.println("###############################################################################");

        // controlla se sono presenti dei file nella cartella "input"
        if (directoryListing != null) {
            System.out.println("Segliere l'automa riconoscitore che si vuole caricare: ");
            // stampa il menu
            for (File child : directoryListing) {
                tmpArray.add(i, child.getPath());
                i++;
                System.out.println(i + ") per caricare " + child.getName());
            }

            // attesa dell'input per la scelta del file da caricare
            while(!quit){
                try{
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    int scelta = Integer.parseInt(br.readLine());

                    if(scelta >0 && scelta <= (directoryListing.length)){
                        if(auto.loadFromFile(tmpArray.get(scelta-1).toString(), "riconoscitore")){
                            return true;
                        }else{
                            // caricamento non riuscito
                            System.err.println("Errore durante il caricamento.");
                            quit=true;
                            break;
                        }
                    }else{
                        System.err.println("Opzione non disponibile.");
                    }
                }catch(NumberFormatException e){
                    System.err.println("Scelta non consentita. Inserire solo valori numerici consentiti!");
                }
            }
        }else{
            System.out.println("Non sono presenti file nella cartella input!!");
        }
        return false;
    }
        
    /**
     * Funzione che salva la stringa passata in un file
     * @param fileName path e nome del file in cui si vuole salvare la stringa generata
     * @param content La stringa che si vuole salvare nel file
     * @return boolean
     */
    public static boolean storeIntoFile(String fileName, String content){
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.print(content);
            System.out.println("Salvataggio eseguito correttamente");
            return true;
        }catch(Exception e){
            System.out.println("Errore durante il salvataggio del file");
            return false;
        }
    }
    
    private static boolean caricaScenario(Automa auto) throws IOException{

        int i =0;
        ArrayList tmpArray = new ArrayList<>();
        boolean quit = false;
        File dir = null;

        // bugfix temporaneo per i path di linux e windows
        if(System.getProperty("os.name").compareTo("Linux") == 0){
            dir = new File("src/scenari/");
        }else{
            dir = new File("Progetto ASD\\src\\scenari\\");
        }
        File[] directoryListing = dir.listFiles();

        System.out.println("###############################################################################");

        // controlla se sono presenti dei file nella cartella "input"
        if (directoryListing != null) {
            System.out.println("Segliere l'automa scenario che si vuole caricare: ");
            // stampa il menu
            for (File child : directoryListing) {
                tmpArray.add(i, child.getPath());
                i++;
                System.out.println(i + ") per caricare " + child.getName());
            }
            System.out.println("0) tornare al menu precedente ");

            // attesa dell'input per la scelta del file da caricare
            while(!quit){
                try{
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    int scelta = Integer.parseInt(br.readLine());

                    if(scelta >0 && scelta <= (directoryListing.length)){
                        if(auto.loadFromFile(tmpArray.get(scelta-1).toString(), "scenario")){
                            return true;
                        }else{
                            // caricamento non riuscito
                            System.err.println("Errore durante il caricamento.");
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
        }else{
            System.out.println("Non sono presenti file nella cartella input!!");
        }
        return false;
    }
    
    private static boolean proseguire() throws IOException{
        boolean quit = false;
        System.out.println("Si desidera proseguire con l'elaborazione? (s/n)");
        while(!quit){
            try{
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String scelta = br.readLine();
                if(scelta.equalsIgnoreCase("s")){
                    return true;
                }else if(scelta.equalsIgnoreCase("n")){
                    System.out.println("Elaborazione interrotta!");
                    quit=true;
                }else{
                    System.err.println("Scelta non consentita. Inserire solo valori consentiti!");
                }
            }catch(NumberFormatException e){
                System.err.println("Scelta non consentita. Inserire solo valori consentiti!");
            }
        }
        return false;
    }
    
    private static boolean proseguire(String richiesta, String errore) throws IOException{
        boolean quit = false;
        System.out.println(richiesta + " (s/n)");
        while(!quit){
            try{
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String scelta = br.readLine();
                if(scelta.equalsIgnoreCase("s")){
                    return true;
                }else if(scelta.equalsIgnoreCase("n")){
                    System.out.println(errore);
                    quit=true;
                }else{
                    System.err.println("Scelta non consentita. Inserire solo valori consentiti!");
                }
            }catch(NumberFormatException e){
                System.err.println("Scelta non consentita. Inserire solo valori consentiti!");
            }
        }
        return false;
    }
    
    private static int getUsedTimeMilliSeconds(){
        return (int) ((Duration.between(startTime, Instant.now()).getNano())/1000000);
    }
}
