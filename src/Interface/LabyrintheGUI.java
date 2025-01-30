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

public class LabyrintheGUI extends JFrame {
    private Laby laby;
    private LabyrinthePanel gridPanel;
    private Sommet playerPosition; // Position actuelle du joueur
    private Victory victoryPanel;
    private JLabel scoreLabel;
    public ListeSommets solution;

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
        
        // Barre supérieure avec le score et le bouton
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS)); 
        
        
        scoreLabel = new JLabel("Score: 0");
        JButton newMazeButton = new JButton("Nouveau Labyrinthe");
        newMazeButton.addActionListener(e -> resetGame());
        ////////////////////////

        ImageIcon originalIcon = new ImageIcon("C:\\Users\\MSI\\eclipse-workspace\\MiniProjetAlgo\\src\\Interface\\2345321.png");
        Image img = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); // Ajuste la taille ici
        ImageIcon resizedIcon = new ImageIcon(img);
        
        JButton shortPathButton = new JButton();
        shortPathButton.addActionListener(e -> findSolution());
        shortPathButton.setIcon(resizedIcon);
        JPanel shortPathButtonPanel = new JPanel(new BorderLayout());
        shortPathButtonPanel.setBorder(new EmptyBorder(10, 10, 10, 0)); // Top, Left, Bottom, Right
        shortPathButtonPanel.add(shortPathButton, BorderLayout.EAST);
        
        
        ////////////////////
        // Wrap the label in a JPanel and add margins
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Top, Left, Bottom, Right
        labelPanel.add(scoreLabel, BorderLayout.WEST);

        // Wrap the button in a JPanel and add margins
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 10)); // Top, Left, Bottom, Right
        buttonPanel.add(newMazeButton, BorderLayout.EAST);
        
        // Style du bouton
        newMazeButton.setBackground(new Color(70, 130, 180)); // Couleur de fond
        newMazeButton.setForeground(Color.WHITE); // Couleur du texte
        newMazeButton.setFont(new Font("Arial", Font.BOLD, 14)); // Police en gras
        newMazeButton.setFocusPainted(false); // Désactiver la bordure de focus

        // Bordure arrondie
        newMazeButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0), 2), // Bordure extérieure
            BorderFactory.createEmptyBorder(10, 20, 10, 20) // Marge intérieure
        ));

        // Add the wrapped components to the top panel
        topPanel.add(labelPanel); // Add label to the left
        topPanel.add(buttonPanel); // Add button to the right
        topPanel.add(shortPathButtonPanel);

       
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
    
    
    private void resetGame() {
    	// Ferme toutes les fenêtres ouvertes
        for (Window window : Window.getWindows()) {
            if (window instanceof JFrame) {
                window.dispose();
            }
        }
        
        // Réinitialisation du jeu
        Laby laby = new Laby(15,14);

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
        Laby laby = new Laby(15,14);
       

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
