package models;

import java.io.*;
import java.util.*;

public class Dictionnaire {
    public static int calculerScore(String texte, String fichierPath) {
        int score = 0;
        try {
            Set<String> motsFichier = new HashSet<>();
            BufferedReader br = new BufferedReader(new FileReader(fichierPath));
            String ligne;
            while ((ligne = br.readLine()) != null) {
                motsFichier.add(ligne.trim().toLowerCase());
            }
            br.close();

            texte = texte.toLowerCase();
            for (String mot : motsFichier) {
                if (texte.contains(mot)) {
                    score += 5;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return score; 
    }



    public static void main(String[] args) {
        int s = calculerScore("travail","src/models/dictionnaire");
        System.out.println("score: "+s);


    }
}
