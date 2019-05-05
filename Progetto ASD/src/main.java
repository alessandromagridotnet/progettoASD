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
    public static void main(String[] args) {
        System.out.println("Antonio");
        main.reader();
    }


public static void reader ()
    {
        JSONParser parser = new JSONParser();

        try
        {
            Object obj = parser.parse(new FileReader("C:\\Users\\Antonio\\IdeaProjects\\Progetto ASD\\Progetto ASD\\src\\ReteBaseZanella.json"));
            JSONObject jsonObject = (JSONObject) obj;

            ReteAutomi rete =new ReteAutomi();
            ArrayList<Automa> listaAutomi= new ArrayList<Automa>();
            ArrayList<Link> listaLink= new ArrayList<Link>();

            JSONObject ra = (JSONObject) jsonObject.get("ReteAutomi");

            JSONArray lk = (JSONArray) ra.get("Links");
            for (int i=0; i<lk.size(); i++){
                JSONObject l = (JSONObject) lk.get(i);
                Link nuovo = new Link();

                nuovo.setNome(l.get("nome"));
                nuovo.setAutomaI=l.get("automaI");
                nuovo.setAutomaF=l.get("automaF");
                nuovo.setEventoOn=l.get("eventoOn");

                listaLink.add(nuovo);
            }



            System.out.println(lk);






        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }


    }

}
