package Interface;

import models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;
import javax.swing.plaf.basic.BasicComboBoxUI;
public class LabyrintheGUI extends JFrame {
    private Laby laby;
    private LabyrinthePanel gridPanel;
    private Sommet playerPosition; // Position actuelle du joueur
    private Victory victoryPanel;
    private JLabel scoreLabel;
    public ListeSommets solution;
    private String currentDifficulty = "Facile";
    private Stack<Sommet> sommetStack = new Stack<>();

    public LabyrintheGUI(Laby laby) {
        this.laby = laby;
        this.playerPosition = laby.getEntree(); // Le joueur commence à l'entrée
        this.solution = null ;
        init();
    }
    //Interface graphique

    private void init() {
        setTitle("Labyrinthe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());
        
        // Barre supérieure
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS)); 

        
        //creation Selecteur difficulter
        String[] difficulties = {"Facile", "Moyenne", "Difficile"};
        JComboBox<String> difficultySelector = new JComboBox<>(difficulties);
        difficultySelector.setFont(new Font("Arial", Font.BOLD, 12));
        difficultySelector.setPreferredSize(new Dimension(100, 40));
        difficultySelector.setBackground(new Color(60, 63, 65)); // Dark theme
        difficultySelector.setForeground(Color.BLACK);
        difficultySelector.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0), 2), // Gold border
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

     // assurer que Renderer et JLabel avant l'aligner
        difficultySelector.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (component instanceof JLabel label) {
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    if (isSelected) {
                        label.setBackground(new Color(255, 215, 0)); // Highlight item selecter
                        label.setForeground(Color.BLACK);
                    }
                }
                return component;
            }
        });
        
        difficultySelector.setUI(new BasicComboBoxUI() {
            protected JButton createArrowButton() {
                JButton button = new JButton("▼"); // Custom arrow symbol
                button.setFont(new Font("Arial", Font.BOLD, 10));
                button.setPreferredSize(new Dimension(5, 5));
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setBackground(new Color(255, 215, 0)); //  boutton doré
                button.setFocusPainted(false); // deactiver focus border
                return button;
            }
        });
        // ajout action listener pour changement de difficulté
        difficultySelector.addActionListener(e -> {
            String newDifficulty = (String) difficultySelector.getSelectedItem();
            if (!newDifficulty.equals(currentDifficulty)) {
                currentDifficulty = newDifficulty;
                createNewMaze();
            }
        });

        ////////////////////////
      
        ImageIcon originalIcon = new ImageIcon("src/Interface/2345321.png");
        Image img = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); //  taille icon
        ImageIcon resizedIcon = new ImageIcon(img);
        
        JButton shortPathButton = new JButton();
        shortPathButton.addActionListener(e -> findSolution());
        shortPathButton.setIcon(resizedIcon);

        shortPathButton.setPreferredSize(new Dimension(40, 40)); // taille plus petite
        shortPathButton.setBackground(new Color(60, 63, 65)); // méme couleur sombre que le JComboBox
        shortPathButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0), 2), // Bordure dorée
            BorderFactory.createEmptyBorder(5, 5, 5, 5) // Espace intérieur
        ));
        shortPathButton.setFocusPainted(false); // Désactiver l'effet de focus
        shortPathButton.setContentAreaFilled(true); // Remplir le fond
        shortPathButton.setOpaque(true);
        
     // Effet au survol (hover effect)
        shortPathButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                shortPathButton.setBackground(new Color(255, 215, 0)); // Devient doré au survol
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                shortPathButton.setBackground(new Color(60, 63, 65)); // Reprend la couleur sombre
            }
        });
        
        JPanel shortPathButtonPanel = new JPanel(new BorderLayout());
        shortPathButtonPanel.setBorder(new EmptyBorder(10, 10, 10, 0)); // Top, Left, Bottom, Right
        shortPathButtonPanel.add(shortPathButton, BorderLayout.EAST);
        
        ////////////////////
        ImageIcon originalIcon1 = new ImageIcon("src/Interface/1248862.png");
        Image img1 = originalIcon1.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); // Ajuste la taille ici
        ImageIcon resizedIcon1 = new ImageIcon(img1);
        
        JButton NouveauButton = new JButton();
        NouveauButton.addActionListener(e -> resetGame());
        NouveauButton.setIcon(resizedIcon1);

        NouveauButton.setPreferredSize(new Dimension(40, 40)); // taille plus petite
        NouveauButton.setBackground(new Color(60, 63, 65)); //méme couleur sombre que le JComboBox
        NouveauButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0), 2), // Bordure dorée
            BorderFactory.createEmptyBorder(5, 5, 5, 5) // Espace interieur
        ));
        NouveauButton.setFocusPainted(false); // Desactiver l'effet de focus
        NouveauButton.setContentAreaFilled(true); // Remplir le fond
        NouveauButton.setOpaque(true);
        
     // Effet au survol (hover effect)
        shortPathButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                shortPathButton.setBackground(new Color(255, 215, 0)); // Devient doré au survol
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                shortPathButton.setBackground(new Color(60, 63, 65)); // Reprend la couleur sombre
            }
        });
        
        JPanel NouveauButtonPanel = new JPanel(new BorderLayout());
        NouveauButtonPanel.setBorder(new EmptyBorder(10, 10, 10, 0)); // Top, Left, Bottom, Right
        NouveauButtonPanel.add(NouveauButton, BorderLayout.EAST);
        
        ///////////////////
        // Wrap  label in a JPanel
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Top, Left, Bottom, Right

        
        // Create a panel for the difficulty selector with margins
        JPanel selectorPanel = new JPanel(new BorderLayout());
        selectorPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        selectorPanel.add(difficultySelector, BorderLayout.CENTER);

        // Add  wrapped components

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)); // Espacement réduit (5px)
        leftPanel.setOpaque(false); // Fond transparent
        leftPanel.add(NouveauButtonPanel);
        leftPanel.add(shortPathButtonPanel);
        leftPanel.add(selectorPanel);
        topPanel.add(labelPanel); 
        topPanel.add(leftPanel);

       
        // Création et ajout des panneaux dans le bon ordre
        // Le dernier ajouté sera au-dessus
        gridPanel = new LabyrinthePanel(laby, playerPosition,solution);
        gridPanel.setBounds(0, 150, 800, 500); // Position le labyrinthe plus bas
        victoryPanel = new Victory();
        victoryPanel.setVisible(false);
        
        // Créer un seul panneau pour contenir la grille et la victoire
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new OverlayLayout(gamePanel));
        gamePanel.add(victoryPanel);
        gamePanel.add(gridPanel);
           
        add(topPanel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);
        // Gestion des événements souris
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (victoryPanel.isVisible() && victoryPanel.isPointInButton(e.getPoint())) {
                    resetGame();
                }
            }
        });
        // Gestion du clavier
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if(!victoryPanel.isVisible()) {
                	handlePlayerMovement(key);
                }
                
            }
        });
        
        // Important pour la gestion du clavier
        setFocusable(true);
        requestFocusInWindow();
    }

    
    
    
    //gestion de mouvement de joueur
    private void handlePlayerMovement(int key) {
        int i = playerPosition.getI();
        int j = playerPosition.getJ();
        Sommet newPosition = null;

        // Vérifier les touches et définir la nouvelle position potentielle
        if (key == 38) {
            newPosition = laby.getSommet(i-1,j);
        } else if (key == 40) {
            newPosition = laby.getSommet(i + 1, j);
        } else if (key == 37) {
            newPosition = laby.getSommet(i, j - 1);
        } else if (key == 39) {
            newPosition = laby.getSommet(i, j + 1);
        }

        // Vérifier si le mouvement est valide (pas de mur)
        if (newPosition != null && isNeighbor(playerPosition, newPosition)) {
            mot(playerPosition);// Mise à jour de la pile
            System.out.println("Stack: " + stackToString());
            playerPosition = newPosition; // Déplacer le joueur
            mot(playerPosition);
            System.out.println("Stack: " + stackToString());
            gridPanel.setPlayerPosition(playerPosition); // Mettre à jour l'affichage
            if (playerPosition.getI()==laby.getSortie().getI()&&playerPosition.getJ()==laby.getSortie().getJ()) {
                //victoir et calcul de score
                int score = calculatePlayerScore();
                victoryPanel.setScore(score);
                victoryPanel.setVisible(true);
            	victoryPanel.setVisible(true);
                System.out.println("Score " + calculatePlayerScore() );
                victoryPanel.startAnimation();

            }
        }
    }

    //donner le solution en cas de blockage
    private void findSolution() {
    	Parcours p = new Parcours();
        ListeSommets l =p.findShortestPath(laby.getVoisins(),laby.getEntree(),laby.getSortie());
        
    	gridPanel.setSolution(l);
    	if (l == null) {
    	    System.out.println("aucun chemin trouvé");
    	} else {
    	    System.out.println("chemin trouvé : ");
    	    ListeSommets current = l; // Commencer par le début de la liste
    	    while (current != null) {
    	        Sommet sommet = current.getVal(); // Récupérer le sommet courant
    	        current = current.getSuivant(); // Passer au sommet suivant
    	    }
    	}
    	
    }

    //traitement de chaine
    private void mot(Sommet sommet) {
    	if (sommetStack.contains(sommet)) {
            //cas de retour en arière
            while (!sommetStack.isEmpty() && !sommetStack.peek().equals(sommet)) {
                sommetStack.pop();//depiler les letters dans la pile jusqu'a la position courante
            }
        } else {
            sommetStack.push(sommet);//empiler si non
        }
    }
    //transformer la pile en chaine de caractéres
    public String stackToString() {
        StringBuilder sb = new StringBuilder();
        for (Sommet s : sommetStack) {
            sb.append(s.getC());
        }
        return sb.toString().trim();
    }
    
    // creation de labyrinthe a base de difficulté
    private void createNewMaze() {
        int size;
        switch (currentDifficulty) {
            case "Difficile":
                size = 15;
                break;
            case "Moyenne":
                size = 12;
                break;
            default:
                size = 10;
                break;
        }
        
        // nouvelle laby avec size
        Laby newLaby = new Laby(size, size, "src/models/dictionnaire");
        
        //update la fenétre courante
        this.laby = newLaby;
        this.playerPosition = newLaby.getEntree();
        this.solution = null;
        this.sommetStack = new Stack<>();
        
        // Update  display
        gridPanel.setLaby(newLaby);
        gridPanel.setPlayerPosition(playerPosition);
        gridPanel.setSolution(null);
        gridPanel.repaint();
        
        // Reset victory panel
        victoryPanel.setVisible(false);
        
        //  focus for keyboard controls
        //c'est important pour que fenétre détecte le mouvement (disabled en cas de victoir ou requéte de solution)
        requestFocusInWindow();
    }
    
    //creation de nouvelle jeux
    private void resetGame() {
    	
    	createNewMaze();
    	// Ferme toutes les fenêtres ouvertes
        for (Window window : Window.getWindows()) {
            if (window instanceof JFrame) {
                window.dispose();
            }
        }
       
        // Crée une nouvelle interface
        SwingUtilities.invokeLater(() -> {
            LabyrintheGUI gui = new LabyrintheGUI(laby);
            gui.setVisible(true);
        });
    }


    // Méthode pour vérifier si deux sommets sont voisins
    private boolean isNeighbor(Sommet current, Sommet target) {
        ListeSommets neighbors = laby.getVoisins(current);//extract les voisin de sommet courante
        while (neighbors != null) {
            //parcour et vérification de voisins
            if (neighbors.getVal().getI()==target.getI()&& neighbors.getVal().getJ()==target.getJ()) {
                return true;
            }
            neighbors = neighbors.getSuivant();
        }
        return false;
    }

    /////////////////////////
    /////////////////////////



    //calcule de score
    public int calculatePlayerScore() {
        // Produit de dimention
        //on a utilisé largeur et hauteur méme si le labyrinthe est carré pour assure l'evoluvité
        int baseScore = laby.getLargeur() * laby.getHauteur();
        System.out.println("Score baseScore : "+baseScore);

        // Score de mots de dictionnaire
        String playerPathText = stackToString();
        int dictScore = Dictionnaire.calculerScore(playerPathText, "src/models/dictionnaire");
        System.out.println("Score dcionnaire: "+dictScore);

        int bonus = 0;
        String playerCoords = getPlayerCoords();
        String solutionCoords = getSolutionCoords();
        System.out.println("Player Coords: " + playerCoords);
        System.out.println("Shortest Coords: " + solutionCoords);
        if (!solutionCoords.isEmpty() && playerCoords.equals(solutionCoords)) {
            bonus = 10;
        }
        System.out.println("Bonus Score: " + bonus);

        return baseScore + dictScore + bonus;
    }
    //Extract les coordonnées de chemin de joueur
    private String getPlayerCoords() {
        StringBuilder sb = new StringBuilder();
        for (Sommet s : sommetStack) {
            sb.append("(").append(s.getI()).append(",").append(s.getJ()).append(")");
        }
        return sb.toString();
    }

    //Extract les coordonnées de plus cour chemin
    private String getSolutionCoords() {
        // trouver la plus cours chemin
        if (solution == null) {
            Parcours p = new Parcours();
            solution = p.findShortestPath(laby.getVoisins(), laby.getEntree(), laby.getSortie());
        }
        if (solution == null) {
            return "";
        }
        // TO STRING
        StringBuilder sb = new StringBuilder();
        ListeSommets current = solution;
        while (current != null) {
            Sommet s = current.getVal();
            sb.append("(").append(s.getI()).append(",").append(s.getJ()).append(")");
            current = current.getSuivant();
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        // Créer le labyrinthe
    	Laby laby = new Laby(10, 10, "src/models/dictionnaire");
        laby.printMaze();
        System.out.println("start: "+laby.getEntree().getC() +laby.getEntree().getI() +laby.getEntree().getJ() );



        // Lancer l'application
        SwingUtilities.invokeLater(() -> {
            LabyrintheGUI gui = new LabyrintheGUI(laby);
            gui.setVisible(true);
           
        });
        
       
    }


}
