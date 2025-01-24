package models;

import java.util.*;

public class Laby {
    private int hauteur, largeur;
    private Sommet entree, sortie;
    private ListeSommets[][] voisins;

    //getters and setters
    public int getHauteur() {
        return hauteur;
    }

    public int getLargeur() {
        return largeur;
    }

    public void setHauteur(int hauteur) {
        if (hauteur > 0) {
            this.hauteur = hauteur;
        } else {
            throw new IllegalArgumentException("Hauteur must be a positive integer");
        }
    }

    public void setLargeur(int largeur) {
        if (largeur > 0) {
            this.largeur = largeur;
        } else {
            throw new IllegalArgumentException("Largeur must be a positive integer");
        }
    }

    public Sommet getEntree() {
        return entree;
    }

    public Sommet getSortie() {
        return sortie;
    }

    public void setEntree(Sommet entree) {
        if (entree != null) {
            this.entree = entree;
        } else {
            throw new IllegalArgumentException("Entree cannot be null");
        }
    }

    public void setSortie(Sommet sortie) {
        if (sortie != null) {
            this.sortie = sortie;
        } else {
            throw new IllegalArgumentException("Sortie cannot be null");
        }
    }

    public ListeSommets[][] getVoisins() {
        // Create a deep copy to prevent direct manipulation
        ListeSommets[][] copieVoisins = new ListeSommets[voisins.length][];
        for (int i = 0; i < voisins.length; i++) {
            copieVoisins[i] = voisins[i].clone();
        }
        return copieVoisins;
    }

    public void setVoisins(ListeSommets[][] voisins) {
        if (voisins != null && voisins.length > 0) {
            this.voisins = new ListeSommets[voisins.length][];
            for (int i = 0; i < voisins.length; i++) {
                this.voisins[i] = voisins[i].clone();
            }
        } else {
            throw new IllegalArgumentException("Voisins must be a non-empty 2D array");
        }
    }





    Random random = new Random();
    //charctére au hazard
    private char getRandomChar() {
        return (char) (random.nextInt(26) + 'a');
    }

    //Génération Laby
    public Laby(int hauteur, int largeur) {
        this.hauteur = hauteur;
        this.largeur = largeur;

        this.voisins = new ListeSommets[hauteur][largeur];
        Random r = new Random();
        char randomChar = (char) (r.nextInt(26) + 'a');
        //initialistion
        this.entree = new Sommet(0, 0,getRandomChar());
        this.sortie = new Sommet(hauteur - 1, largeur - 1,getRandomChar());
        //génération
        generationLaby();
    }

    private void generationLaby() {
        boolean[][] visited = new boolean[hauteur][largeur];
        generationLabyRecursive(entree.getI(), entree.getJ(), visited);
    }

    //Génération Laby DFS (Profondeur)
    private void generationLabyRecursive(int i, int j, boolean[][] visited) {
        visited[i][j] = true;

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        List<int[]> RandomDirections = new ArrayList<>(Arrays.asList(directions));
        Collections.shuffle(RandomDirections, random);

        for (int[] dir : RandomDirections) {
            int newI = i + dir[0];
            int newJ = j + dir[1];

            if (isValid(newI, newJ) && !visited[newI][newJ]) {

                Sommet current = new Sommet(i, j,getRandomChar());
                Sommet next = new Sommet(newI, newJ,getRandomChar());
                voisins[i][j] = new ListeSommets(next, voisins[i][j]);
                voisins[newI][newJ] = new ListeSommets(current, voisins[newI][newJ]);

                generationLabyRecursive(newI, newJ, visited);
            }
        }
    }

    private boolean isValid(int i, int j) {
        return i >= 0 && i < hauteur && j >= 0 && j < largeur;
    }

    public ListeSommets getVoisins(Sommet s) {
        return voisins[s.getI()][s.getJ()];
    }

    public boolean canMoveTo(Sommet current, Sommet target) {
        ListeSommets neighbors = getVoisins(current);
        while (neighbors != null) {
            if (neighbors.getVal().equals(target)) {
                return true;
            }
            neighbors = neighbors.getSuivant();
        }
        return false;
    }
    //DEBUT TEST
    public void printMaze() {
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                System.out.print("(" + i + "," + j + ") -> ");
                ListeSommets neighbors = voisins[i][j];
                while (neighbors != null) {
                    System.out.print(neighbors.getVal() + " ");
                    neighbors = neighbors.getSuivant();
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {

        Laby maze = new Laby(7, 5);
        System.out.println("Labyrinthe");
        System.out.println("Entry : " + maze.entree);
        System.out.println("Exit : " + maze.sortie);
        maze.printMaze();
    }
    //FIN TEST
}