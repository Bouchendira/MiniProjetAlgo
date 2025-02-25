package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Laby {
    private int hauteur, largeur;
    private Sommet entree, sortie;
    private ListeSommets[][] voisins;
    private List<String> dictionaryWords; // liste pour stocker les mots du dictionnaire
    protected char[][] c;
    Random random = new Random();

    // Getters et setters
    public int getHauteur() {
        return hauteur;
    }

    public void setHauteur(int hauteur) {
        if (hauteur > 0) {
            this.hauteur = hauteur;
        } else {
            throw new IllegalArgumentException("Hauteur doit étre positif");
        }
    }

    public int getLargeur() {
        return largeur;
    }

    public void setLargeur(int largeur) {
        if (largeur > 0) {
            this.largeur = largeur;
        } else {
            throw new IllegalArgumentException("Largeur doit étre positif");
        }
    }

    public Sommet getEntree() {
        return entree;
    }

    public void setEntree(Sommet entree) {
        if (entree != null) {
            this.entree = entree;
        } else {
            throw new IllegalArgumentException("Entree Null ");
        }
    }

    public Sommet getSortie() {
        return sortie;
    }

    public void setSortie(Sommet sortie) {
        if (sortie != null) {
            this.sortie = sortie;
        } else {
            throw new IllegalArgumentException("Sortie null");
        }
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


    public ListeSommets[][] getVoisins() {
        ListeSommets[][] copieVoisins = new ListeSommets[voisins.length][];
        for (int i = 0; i < voisins.length; i++) {
            copieVoisins[i] = voisins[i].clone();
        }
        return copieVoisins;
    }


    //////////



    // Chargement des mots du dictionnaire
    private void loadDictionaryWords(String filePath) {
        dictionaryWords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                dictionaryWords.add(line.trim().toLowerCase());
            }
            if (dictionaryWords.isEmpty()) {
                throw new IllegalStateException("Le fichier dictionnaire est vide.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Echec de chargement du fichier dictionnaire: " + filePath, e);
        }
    }

    // Constructeur pour la generation de labyrinthe a partir des mots du dictionnaire
    public Laby(int hauteur, int largeur, String dictionaryFilePath) {
        this.hauteur = hauteur;
        this.largeur = largeur;
        this.voisins = new ListeSommets[hauteur][largeur];
        loadDictionaryWords(dictionaryFilePath); // Chargement du dictionnaire
        generateRandomChar(hauteur, largeur);

        generationLaby();
        addWordsToMaze();
        this.entree = this.getSommet(0,0) ;
        this.sortie = this.getSommet(hauteur - 1,largeur - 1);

    }



    // Generation du labyrinthe avec des caracteres aleatoires
    public void generateRandomChar(int hauteur, int largeur) {
        c = new char[hauteur][largeur];
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                c[i][j] = (char) (random.nextInt(26) + 'a');
            }
        }
    }

    public void generationLaby() {
        // Initialisation avec des Sommets par defaut
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                voisins[i][j] = new ListeSommets(new Sommet(i, j, c[i][j]), null);
            }
        }
        boolean[][] visited = new boolean[hauteur][largeur];
        generationLabyRecursive(0, 0, visited);
        addMultiplePaths();
    }



    // Generation du labyrinthe en  DFS (parcours en profondeur)
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

    // Ajout de chemins multiples
    public void addMultiplePaths() {
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                if (random.nextDouble() < 0.1) { // 10% chance d'ajout
                    List<int[]> possibleMoves = new ArrayList<>();
                    if (isValid(i + 1, j)) possibleMoves.add(new int[]{i + 1, j});
                    if (isValid(i, j + 1)) possibleMoves.add(new int[]{i, j + 1});
                    if (isValid(i - 1, j)) possibleMoves.add(new int[]{i - 1, j});
                    if (isValid(i, j - 1)) possibleMoves.add(new int[]{i, j - 1});

                    if (!possibleMoves.isEmpty()) {
                        int[] move = possibleMoves.get(random.nextInt(possibleMoves.size()));
                        Sommet current = new Sommet(i, j, c[i][j]);
                        Sommet next = new Sommet(move[0], move[1], c[move[0]][move[1]]);
                        voisins[i][j] = new ListeSommets(next, voisins[i][j]);
                        voisins[move[0]][move[1]] = new ListeSommets(current, voisins[move[0]][move[1]]);
                    }
                }
            }
        }
    }

    // Verifie si les coordonnees sont valides dans le labyrinthe
    private boolean isValid(int i, int j) {
        return i >= 0 && i < hauteur && j >= 0 && j < largeur;
    }

    // Recupere les voisins d'un sommet
    public ListeSommets getVoisins(Sommet s) {
        return voisins[s.getI()][s.getJ()];
    }


    // Affichage du labyrinthe
    public void printMaze() {
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                //les caracteres mis sont de place holders a cause des eurreurs la seul
                //caractere a compte et celle de neoud courante
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

    // Ajout de mots du dictionnaire dans le labyrinthe
    public void addWordsToMaze() {
        List<String> shuffledWords = new ArrayList<>(dictionaryWords);
        Collections.shuffle(shuffledWords, random);
        boolean[][] usedPositions = new boolean[hauteur][largeur];

        // Garde trace des positions de depart potentielles (fins des mots places)
        Queue<int[]> chainStarts = new LinkedList<>();

        // Place le premier mot normalement
        String firstWord = shuffledWords.get(0);
        boolean firstPlaced = false;

        for (int i = 0; !firstPlaced && i < hauteur; i++) {
            for (int j = 0; !firstPlaced && j < largeur; j++) {
                List<Sommet> path = findPathForWord(i, j, firstWord.length(), usedPositions);
                if (path != null) {
                    // Placement du mot
                    for (int k = 0; k < firstWord.length(); k++) {
                        Sommet sommet = path.get(k);
                        sommet.setC(firstWord.charAt(k));
                        usedPositions[sommet.getI()][sommet.getJ()] = true;
                    }
                    // Ajoute la position finale aux points de depart de chaines
                    Sommet lastSommet = path.get(path.size() - 1);
                    chainStarts.offer(new int[]{lastSommet.getI(), lastSommet.getJ()});
                    firstPlaced = true;
                }
            }
        }

        // Tente de placer les mots restants
        for (int wordIndex = 1; wordIndex < shuffledWords.size(); wordIndex++) {
            String word = shuffledWords.get(wordIndex);
            if (word.length() > hauteur * largeur) continue;

            boolean placed = false;

            // Essaie d'abord de partir des positions de chaines
            List<int[]> currentChainStarts = new ArrayList<>();
            while (!chainStarts.isEmpty()) {
                currentChainStarts.add(chainStarts.poll());
            }

            // Essaie chaque position de depart de chaine
            for (int[] start : currentChainStarts) {
                if (!placed) {
                    List<Sommet> path = findPathForWord(start[0], start[1], word.length(), usedPositions);
                    if (path != null) {
                        // Placement du mot
                        for (int k = 0; k < word.length(); k++) {
                            Sommet sommet = path.get(k);
                            sommet.setC(word.charAt(k));
                            usedPositions[sommet.getI()][sommet.getJ()] = true;
                        }
                        // Ajoute la position finale aux points de depart de chaines
                        Sommet lastSommet = path.get(path.size() - 1);
                        chainStarts.offer(new int[]{lastSommet.getI(), lastSommet.getJ()});
                        placed = true;
                    }
                }
            }

            // Si impossible de chainer  placer normalement
            if (!placed) {
                for (int i = 0; !placed && i < hauteur; i++) {
                    for (int j = 0; !placed && j < largeur; j++) {
                        if (!usedPositions[i][j]) {
                            List<Sommet> path = findPathForWord(i, j, word.length(), usedPositions);
                            if (path != null) {
                                // Placement du mot
                                for (int k = 0; k < word.length(); k++) {
                                    Sommet sommet = path.get(k);
                                    sommet.setC(word.charAt(k));
                                    usedPositions[sommet.getI()][sommet.getJ()] = true;
                                }
                                // Ajoute la position finale aux points de depart de chaines
                                Sommet lastSommet = path.get(path.size() - 1);
                                chainStarts.offer(new int[]{lastSommet.getI(), lastSommet.getJ()});
                                placed = true;
                            }
                        }
                    }
                }
            }
        }

        // Remplit les positions restantes avec des caracteres aleatoires
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                if (!usedPositions[i][j]) {
                    Sommet sommet = getSommet(i, j);
                    sommet.setC(c[i][j]);
                }
            }
        }
    }

    // Trouve un chemin pour un mot a partir de position donnee
    private List<Sommet> findPathForWord(int startI, int startJ, int length, boolean[][] usedPositions) {
        List<Sommet> path = new ArrayList<>();
        boolean[][] visited = new boolean[hauteur][largeur];

        if (dfs(startI, startJ, length, visited, usedPositions, path)) {
            return path;
        }
        return null;
    }

    // Parcours DFS pour trouver un chemin valide pour un mot
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

        // Recupere les voisins aleatoirement
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