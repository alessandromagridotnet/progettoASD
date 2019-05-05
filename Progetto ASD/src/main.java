/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import ReteAutomi.Automa;
import ReteAutomi.Link;
import ReteAutomi.ReteAutomi;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
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

            Scanner in=new Scanner(System.in);
            boolean quit = false;


            while(!quit){
                System.out.println("\n1) Prenotazione");
                System.out.println("2) Gestione (riservato ai docenti");
                System.out.println("3) Esci");

                int scelta=in.nextInt();
                in.nextLine();

                switch(scelta){

                    case 1:
                    System.out.println("scelta1");
                        break;
                    case 2:
                    System.out.println("scelta2");
                        break;


                    case 3: break;

                    default:
                        System.out.println("Scelta non supportata;");
                }
                if(scelta==3) quit=true;
            }
            System.out.println("Uscita dal sistema eseguita");
        }






}
