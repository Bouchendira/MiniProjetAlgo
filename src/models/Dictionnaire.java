package models;

import java.io.*;
import java.util.*;

/**
 * Classe permettant de calculer un score en fonction de la presence de mots
 * de dictionnaire dans un texte donne
 */
public class Dictionnaire {
    /**
     * Calcule un score a base des mots du dictionnaire dans le texte
     */
    public static int calculerScore(String texte, String fichierPath) {
        int score = 0;
        try {
            // Creation de set pour stocker les mots du dictionnaire
            Set<String> motsFichier = new HashSet<>();

            // Lecture du fichier dictionnaire
            BufferedReader br = new BufferedReader(new FileReader(fichierPath));
            String ligne;
            while ((ligne = br.readLine()) != null) {
                motsFichier.add(ligne.trim().toLowerCase());
            }
            br.close();

            // Conversion du texte en minuscules pour comparaison insensible a la casse
            // (on a utiliser des characteres en minsicule mais pour un code plus robuste on fait le check)
            texte = texte.toLowerCase();

            // Parcours de tous les mots du dictionnaire
            for (String mot : motsFichier) {
                // Si le mot est dans le texte ajouter 5 points au score
                if (texte.contains(mot)) {
                    score += 5;
                }
            }
        } catch (IOException e) {
            // Gestion des erreurs d'entree/sortie
            e.printStackTrace();
        }
        return score;
    }

    /**
     * Teste de  calcul de score
     */
    public static void main(String[] args) {
        int s = calculerScore("travail", "src/models/dictionnaire");
        System.out.println("score: " + s);

        /////
        int s = calculerScore("tback", "src/models/dictionnaire");
        System.out.println("score: " + s);



    }
}