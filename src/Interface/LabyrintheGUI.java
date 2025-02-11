package Interface;

import models.Laby;
import models.Sommet;
import models.ListeSommets;
import models.Parcours;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.plaf.basic.BasicComboBoxUI;
public class LabyrintheGUI extends JFrame {
    private Laby laby;
    private LabyrinthePanel gridPanel;
    private Sommet playerPosition; // Position actuelle du joueur
    private Victory victoryPanel;
    private JLabel scoreLabel;
    public ListeSommets solution;
    private String currentDifficulty = "Facile";

    public LabyrintheGUI(Laby laby) {
        this.laby = laby;
        this.playerPosition = laby.getEntree(); // Le joueur commence à l'entrée
        this.solution = null ;
        init();
    }

    private void init() {
        setTitle("Labyrinthe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());// Un seul layout manager
        
        // Barre supérieure avec le score
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS)); 
        scoreLabel = new JLabel("Score: 0");
        
        // Create difficulty selector
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

     // Ensure renderer is a JLabel before setting alignment
        difficultySelector.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (component instanceof JLabel label) {
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    if (isSelected) {
                        label.setBackground(new Color(255, 215, 0)); // Highlight selected item
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
                button.setBackground(new Color(255, 215, 0)); // Gold button
                button.setFocusPainted(false); // Remove focus border
                return button;
            }
        });
        // Add action listener for difficulty changes
        difficultySelector.addActionListener(e -> {
            String newDifficulty = (String) difficultySelector.getSelectedItem();
            if (!newDifficulty.equals(currentDifficulty)) {
                currentDifficulty = newDifficulty;
                createNewMaze();
            }
        });

        ////////////////////////
      
        ImageIcon originalIcon = new ImageIcon("C:\\Users\\MSI\\eclipse-workspace\\MiniProjetAlgo\\src\\Interface\\2345321.png");
        Image img = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); // Ajuste la taille ici
        ImageIcon resizedIcon = new ImageIcon(img);
        
        JButton shortPathButton = new JButton();
        shortPathButton.addActionListener(e -> findSolution());
        shortPathButton.setIcon(resizedIcon);

        shortPathButton.setPreferredSize(new Dimension(40, 40)); // Taille plus petite
        shortPathButton.setBackground(new Color(60, 63, 65)); // Même couleur sombre que le JComboBox
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
        ImageIcon originalIcon1 = new ImageIcon("C:\\Users\\MSI\\eclipse-workspace\\MiniProjetAlgo\\src\\Interface\\1248862.png");
        Image img1 = originalIcon1.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); // Ajuste la taille ici
        ImageIcon resizedIcon1 = new ImageIcon(img1);
        
        JButton NouveauButton = new JButton();
        NouveauButton.addActionListener(e -> resetGame());
        NouveauButton.setIcon(resizedIcon1);

        NouveauButton.setPreferredSize(new Dimension(40, 40)); // Taille plus petite
        NouveauButton.setBackground(new Color(60, 63, 65)); // Même couleur sombre que le JComboBox
        NouveauButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0), 2), // Bordure dorée
            BorderFactory.createEmptyBorder(5, 5, 5, 5) // Espace intérieur
        ));
        NouveauButton.setFocusPainted(false); // Désactiver l'effet de focus
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
        // Wrap the label in a JPanel and add margins
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Top, Left, Bottom, Right
        labelPanel.add(scoreLabel, BorderLayout.WEST);
        
        // Create a panel for the difficulty selector with margins
        JPanel selectorPanel = new JPanel(new BorderLayout());
        selectorPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        selectorPanel.add(difficultySelector, BorderLayout.CENTER);

        // Add the wrapped components to the top panel

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
        gamePanel.setLayout(new OverlayLayout(gamePanel));  // Utiliser OverlayLayout seulement ici
        gamePanel.add(victoryPanel);
        gamePanel.add(gridPanel);
           
        add(topPanel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER); // Ajoutez gamePanel avec OverlayLayout ici
        // Gestion des événements souris
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e);
                if (victoryPanel.isVisible() && victoryPanel.isPointInButton(e.getPoint())) {
                    System.out.println("heeeyyyyy");
                    resetGame();
                }
            }
        });
        // Gestion du clavier
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                System.out.println("Touche pressée : " + key);
                if(!victoryPanel.isVisible()) {
                	handlePlayerMovement(key);
                }
                
            }
        });
        
        // Important pour la gestion du clavier
        setFocusable(true);
        requestFocusInWindow();
    }

    
    
    

    private void handlePlayerMovement(int key) {
        int i = playerPosition.getI();
        int j = playerPosition.getJ();
        Sommet newPosition = null;

        // Vérifier les touches et définir la nouvelle position potentielle
        if (key == 38) {
            newPosition = new Sommet(i - 1, j, ' ');
        } else if (key == 40) {
            newPosition = new Sommet(i + 1, j, ' ');
        } else if (key == 37) {
            newPosition = new Sommet(i, j - 1, ' ');
        } else if (key == 39) {
            newPosition = new Sommet(i, j + 1, ' ');
        }

        // Vérifier si le mouvement est valide (pas de mur)
        if (newPosition != null && isNeighbor(playerPosition, newPosition)) {
            playerPosition = newPosition; // Déplacer le joueur
    
            gridPanel.setPlayerPosition(playerPosition); // Mettre à jour l'affichage
            if (playerPosition.getI()==laby.getSortie().getI()&&playerPosition.getJ()==laby.getSortie().getJ()) {
            	victoryPanel.setVisible(true);
                victoryPanel.startAnimation();
            }
        }
    }
    
    private void findSolution() {
    	Parcours p = new Parcours();
        ListeSommets l =p.findShortestPath(laby.getVoisins(),laby.getEntree(),laby.getSortie());
        
    	gridPanel.setSolution(l);
    	 
    	
    }
    
    // New method to create maze based on current difficulty
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
        
        // Create new maze with selected size
        Laby newLaby = new Laby(size, size, "src/models/dictionnaire");
        
        // Update the current window instead of creating a new one
        this.laby = newLaby;
        this.playerPosition = newLaby.getEntree();
        this.solution = null;
        
        // Update the display
        gridPanel.setLaby(newLaby);
        gridPanel.setPlayerPosition(playerPosition);
        gridPanel.setSolution(null);
        gridPanel.repaint();
        
        // Reset victory panel
        victoryPanel.setVisible(false);
        
        // Ensure focus for keyboard controls
        requestFocusInWindow();
    }
    
    
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
        ListeSommets neighbors = laby.getVoisins(current);
        while (neighbors != null) {
        	
            if (neighbors.getVal().getI()==target.getI()&& neighbors.getVal().getJ()==target.getJ()) {
                return true;
            }
            neighbors = neighbors.getSuivant();
        }
        return false;
    }


    public static void main(String[] args) {
        // Créer le labyrinthe
    	Laby laby = new Laby(10, 10, "src/models/dictionnaire");
       

        // Lancer l'application
        SwingUtilities.invokeLater(() -> {
            LabyrintheGUI gui = new LabyrintheGUI(laby);
            gui.setVisible(true);
           
        });
        
       
    }

    private static class LabyrinthePanel extends JPanel {
        private Laby laby;
        private Sommet playerPosition;
        private ListeSommets solution;

        public LabyrinthePanel(Laby laby, Sommet playerPosition , ListeSommets solution) {
            this.laby = laby;
            this.playerPosition = playerPosition;
            this.solution = solution ;
        }
        
        public void setLaby(Laby laby) {
            this.laby = laby;
        }
        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 500);  // Réduit la hauteur du labyrinthe
        }

        public void setPlayerPosition(Sommet playerPosition) {
            this.playerPosition = playerPosition;
            repaint(); // Redessiner le labyrinthe avec la nouvelle position du joueur
        }
        
        public void setSolution(ListeSommets l) {
            this.solution = l;
            repaint(); // Redessiner le labyrinthe avec la nouvelle position du joueur
        }
        

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int rows = laby.getHauteur();
            int cols = laby.getLargeur();
            int cellWidth = getWidth() / cols;
            int cellHeight = getHeight() / rows;
            
            


            // Dessiner les cellules et les murs
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    // Coordonnées de la cellule
                    int x = j * cellWidth;
                    int y = i * cellHeight;

                    
                    // Dessiner la cellule (entrée, sortie ou vide)
                    if (laby.getEntree().getI() == i && laby.getEntree().getJ() == j) {
                        g.setColor(new Color(0xfc9410));
                        g.fillRect(x, y, cellWidth, cellHeight);
                    } else if (laby.getSortie().getI() == i && laby.getSortie().getJ() == j) {
                        g.setColor(new Color(0xfc9410));
                        g.fillRect(x, y, cellWidth, cellHeight);
                    } else {
                        g.setColor(Color.WHITE);
                        g.fillRect(x, y, cellWidth, cellHeight);
                    }
                    
                    //plus court chemain

                    ListeSommets l = solution;
                    while (l != null) {
                    	
                    	int a = l.getVal().getJ();
                        int b = l.getVal().getI();
                        if(a==j && b==i) {
                        	g.setColor(new Color(0xfd9410));
                            g.fillRect(x, y, cellWidth, cellHeight);                           
                        }
                     	l=l.getSuivant();   
                     	

                     }
                    

                    // Dessiner la lettre du sommet
                    Sommet sommet = laby.getSommet(i, j);
                    if (sommet != null) {
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Arial", Font.BOLD, 14));
                        String letter = String.valueOf(sommet.getC());
                        FontMetrics fm = g.getFontMetrics();
                        int textWidth = fm.stringWidth(letter);
                        int textHeight = fm.getAscent();
                        g.drawString(letter, x + (cellWidth - textWidth) / 2, y + (cellHeight + textHeight) / 2);
                    }

                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, cellWidth, cellHeight);
                    
                   
                    // Dessiner les murs autour de la cellule
                    drawWalls(g, i, j, x, y, cellWidth, cellHeight);
                }
            }

            // Dessiner le joueur
            int playerX = playerPosition.getJ() * cellWidth;
            int playerY = playerPosition.getI() * cellHeight;
            g.setColor(new Color(70, 130, 180));
            g.fillOval(playerX + cellWidth / 8, playerY + cellHeight /8, cellWidth / 4, cellHeight / 4);
            

        }



		private void drawWalls(Graphics g, int i, int j, int x, int y, int cellWidth, int cellHeight) {
            ListeSommets neighbors = laby.getVoisins(new Sommet(i, j, ' '));

            // Initialiser les murs (tous activés par défaut)
            boolean topWall = true;
            boolean bottomWall = true;
            boolean leftWall = true;
            boolean rightWall = true;

            // Déterminer quels murs doivent être supprimés
            while (neighbors != null) {
                Sommet neighbor = neighbors.getVal();
                if (neighbor.getI() == i - 1 && neighbor.getJ() == j) {
                    topWall = false;
                } else if (neighbor.getI() == i + 1 && neighbor.getJ() == j) {
                    bottomWall = false;
                } else if (neighbor.getI() == i && neighbor.getJ() == j - 1) {
                    leftWall = false;
                } else if (neighbor.getI() == i && neighbor.getJ() == j + 1) {
                    rightWall = false;
                }
                neighbors = neighbors.getSuivant();
            }

            // Dessiner les murs restants
            g.setColor(Color.BLACK);
            if (topWall) {
                g.fillRect(x, y, cellWidth, 2); // Mur du haut
            }
            if (bottomWall) {
                g.fillRect(x, y + cellHeight - 2, cellWidth, 2); // Mur du bas
            }
            if (leftWall) {
                g.fillRect(x, y, 2, cellHeight); // Mur de gauche
            }
            if (rightWall) {
                g.fillRect(x + cellWidth - 2, y, 2, cellHeight); // Mur de droite
            }
        }
    }
}
