package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Laby {
    private int hauteur, largeur;
    private Sommet entree, sortie;
    private ListeSommets[][] voisins;
    private List<String> dictionaryWords; // List to store dictionary words
    protected char[][] c;
    Random random = new Random();

    // Getters and setters
    public int getHauteur() {
        return hauteur;
    }

    public void setHauteur(int hauteur) {
        if (hauteur > 0) {
            this.hauteur = hauteur;
        } else {
            throw new IllegalArgumentException("Hauteur must be a positive integer");
        }
    }

    public int getLargeur() {
        return largeur;
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

    public void setEntree(Sommet entree) {
        if (entree != null) {
            this.entree = entree;
        } else {
            throw new IllegalArgumentException("Entree cannot be null");
        }
    }

    public Sommet getSortie() {
        return sortie;
    }

    public void setSortie(Sommet sortie) {
        if (sortie != null) {
            this.sortie = sortie;
        } else {
            throw new IllegalArgumentException("Sortie cannot be null");
        }
    }

    public ListeSommets[][] getVoisins() {
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

    // Load dictionary words
    private void loadDictionaryWords(String filePath) {
        dictionaryWords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                dictionaryWords.add(line.trim().toLowerCase());
            }
            if (dictionaryWords.isEmpty()) {
                throw new IllegalStateException("The dictionary file is empty.");
            }
            System.out.println("Loaded words: " + dictionaryWords); // Debugging
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to load dictionary file: " + filePath, e);
        }
    }

    // Constructor for dictionary-based maze generation
    public Laby(int hauteur, int largeur, String dictionaryFilePath) {
        this.hauteur = hauteur;
        this.largeur = largeur;
        this.voisins = new ListeSommets[hauteur][largeur];
        loadDictionaryWords(dictionaryFilePath); // Load dictionary words
        generateRandomChar(hauteur, largeur); 
        this.entree = new Sommet(0, 0, c[0][0]);
        this.sortie = new Sommet(hauteur - 1, largeur - 1, c[hauteur - 1][largeur - 1]);
        generationLaby();
        addWordsToMaze();
    }


 

   

    // Generate maze with random characters (backward compatibility)
    public void generateRandomChar(int hauteur, int largeur) {
        c = new char[hauteur][largeur];
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                c[i][j] = (char) (random.nextInt(26) + 'a');
            }
        }
    }

    private void generationLaby() {
        // Initialize all cells with default Sommets
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                voisins[i][j] = new ListeSommets(new Sommet(i, j, c[i][j]), null);
            }
        }
        boolean[][] visited = new boolean[hauteur][largeur];
        generationLabyRecursive(entree.getI(), entree.getJ(), visited);
    }

    public Sommet getSommet(int i, int j) {
        if (isValid(i, j)) {
            ListeSommets voisinsList = voisins[i][j];
            while (voisinsList != null) {
                Sommet sommet = voisinsList.getVal();
                if (sommet.getI() == i && sommet.getJ() == j) {
                    return sommet;
                }
                voisinsList = voisinsList.getSuivant();
            }
        }
        return new Sommet(i, j, ' ');
    }

    // Generate maze using DFS
    private void generationLabyRecursive(int i, int j, boolean[][] visited) {
        visited[i][j] = true;
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        List<int[]> randomDirections = new ArrayList<>(Arrays.asList(directions));
        Collections.shuffle(randomDirections, random);

        for (int[] dir : randomDirections) {
            int newI = i + dir[0];
            int newJ = j + dir[1];
            if (isValid(newI, newJ) && !visited[newI][newJ]) {
                Sommet current = new Sommet(i, j, c[i][j]);
                Sommet next = new Sommet(newI, newJ, c[newI][newJ]);
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

    // Print the maze
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

    public void addWordsToMaze() {
        List<String> shuffledWords = new ArrayList<>(dictionaryWords);
        Collections.shuffle(shuffledWords, random);
        boolean[][] usedPositions = new boolean[hauteur][largeur];
        
        // Keep track of potential starting positions (ends of placed words)
        Queue<int[]> chainStarts = new LinkedList<>();
        
        // Place first word normally
        String firstWord = shuffledWords.get(0);
        boolean firstPlaced = false;
        
        for (int i = 0; !firstPlaced && i < hauteur; i++) {
            for (int j = 0; !firstPlaced && j < largeur; j++) {
                List<Sommet> path = findPathForWord(i, j, firstWord.length(), usedPositions);
                if (path != null) {
                    // Place the word
                    for (int k = 0; k < firstWord.length(); k++) {
                        Sommet sommet = path.get(k);
                        sommet.setC(firstWord.charAt(k));
                        usedPositions[sommet.getI()][sommet.getJ()] = true;
                    }
                    // Add end position to chain starts
                    Sommet lastSommet = path.get(path.size() - 1);
                    chainStarts.offer(new int[]{lastSommet.getI(), lastSommet.getJ()});
                    firstPlaced = true;
                }
            }
        }
        
        // Try to place remaining words
        for (int wordIndex = 1; wordIndex < shuffledWords.size(); wordIndex++) {
            String word = shuffledWords.get(wordIndex);
            if (word.length() > hauteur * largeur) continue;
            
            boolean placed = false;
            
            // First try to start from chain positions
            List<int[]> currentChainStarts = new ArrayList<>();
            while (!chainStarts.isEmpty()) {
                currentChainStarts.add(chainStarts.poll());
            }
            
            // Try each chain start position
            for (int[] start : currentChainStarts) {
                if (!placed) {
                    List<Sommet> path = findPathForWord(start[0], start[1], word.length(), usedPositions);
                    if (path != null) {
                        // Place the word
                        for (int k = 0; k < word.length(); k++) {
                            Sommet sommet = path.get(k);
                            sommet.setC(word.charAt(k));
                            usedPositions[sommet.getI()][sommet.getJ()] = true;
                        }
                        // Add end position to chain starts
                        Sommet lastSommet = path.get(path.size() - 1);
                        chainStarts.offer(new int[]{lastSommet.getI(), lastSommet.getJ()});
                        placed = true;
                    }
                }
            }
            
            // If couldn't chain, try placing normally
            if (!placed) {
                for (int i = 0; !placed && i < hauteur; i++) {
                    for (int j = 0; !placed && j < largeur; j++) {
                        if (!usedPositions[i][j]) {
                            List<Sommet> path = findPathForWord(i, j, word.length(), usedPositions);
                            if (path != null) {
                                // Place the word
                                for (int k = 0; k < word.length(); k++) {
                                    Sommet sommet = path.get(k);
                                    sommet.setC(word.charAt(k));
                                    usedPositions[sommet.getI()][sommet.getJ()] = true;
                                }
                                // Add end position to chain starts
                                Sommet lastSommet = path.get(path.size() - 1);
                                chainStarts.offer(new int[]{lastSommet.getI(), lastSommet.getJ()});
                                placed = true;
                            }
                        }
                    }
                }
            }
        }
        
        // Fill remaining positions with random characters
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                if (!usedPositions[i][j]) {
                    Sommet sommet = getSommet(i, j);
                    sommet.setC((char) (random.nextInt(26) + 'a'));
                }
            }
        }
    }

private List<Sommet> findPathForWord(int startI, int startJ, int length, boolean[][] usedPositions) {
    List<Sommet> path = new ArrayList<>();
    boolean[][] visited = new boolean[hauteur][largeur];
    
    if (dfs(startI, startJ, length, visited, usedPositions, path)) {
        return path;
    }
    return null;
}

private boolean dfs(int i, int j, int remainingLength, boolean[][] visited, 
                   boolean[][] usedPositions, List<Sommet> path) {
    if (remainingLength == 0) {
        return true;
    }
    
    if (!isValid(i, j) || visited[i][j] || usedPositions[i][j]) {
        return false;
    }
    
    visited[i][j] = true;
    path.add(getSommet(i, j));
    
    // Get neighbors and shuffle them
    ListeSommets neighbors = voisins[i][j];
    List<Sommet> neighborsList = new ArrayList<>();
    while (neighbors != null) {
        neighborsList.add(neighbors.getVal());
        neighbors = neighbors.getSuivant();
    }
    Collections.shuffle(neighborsList, random);
    
    for (Sommet neighbor : neighborsList) {
        if (dfs(neighbor.getI(), neighbor.getJ(), remainingLength - 1, 
                visited, usedPositions, path)) {
            return true;
        }
    }
    
    visited[i][j] = false;
    path.remove(path.size() - 1);
    return false;
}


}
