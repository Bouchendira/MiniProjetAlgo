package Interface;

import models.Laby;
import models.Sommet;
import models.ListeSommets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

<<<<<<< Updated upstream
=======
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

>>>>>>> Stashed changes
public class LabyrintheGUI extends JFrame {
    private Laby laby;
    private LabyrinthePanel gridPanel;
    private Sommet playerPosition; // Position actuelle du joueur

    public LabyrintheGUI(Laby laby) {
        this.laby = laby;
        this.playerPosition = laby.getEntree(); // Le joueur commence à l'entrée
        init();
    }

    private void init() {
        setTitle("Labyrinthe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
<<<<<<< Updated upstream
        setLayout(new BorderLayout());

        gridPanel = new LabyrinthePanel(laby, playerPosition);
        add(gridPanel, BorderLayout.CENTER);

        // Ajouter un écouteur pour les touches du clavier
=======
        setLayout(new BorderLayout());// Un seul layout manager

        // Barre supérieure avec le score et le bouton
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS)); 
        
        
        scoreLabel = new JLabel("Score: 0");
        JButton newMazeButton = new JButton("Nouveau Labyrinthe");
        newMazeButton.addActionListener(e -> resetGame());
        ////////////////////////

        ImageIcon originalIcon = new ImageIcon("C:\\Users\\MSI\\eclipse-workspace\\MiniProjetAlgo\\src\\Interface\\2345321.png");
        Image img = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(img);
        
        JButton shortPathButton = new JButton();
        shortPathButton.addActionListener(e -> findSolution());
        shortPathButton.setIcon(resizedIcon);
        JPanel shortPathButtonPanel = new JPanel(new BorderLayout());
        shortPathButtonPanel.setBorder(new EmptyBorder(10, 10, 10, 0));
        shortPathButtonPanel.add(shortPathButton, BorderLayout.EAST);
        
        
        ////////////////////
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        labelPanel.add(scoreLabel, BorderLayout.WEST);


        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 10));
        buttonPanel.add(newMazeButton, BorderLayout.EAST);

        // Style du bouton
        newMazeButton.setBackground(new Color(70, 130, 180));
        newMazeButton.setForeground(Color.WHITE);
        newMazeButton.setFont(new Font("Arial", Font.BOLD, 14));
        newMazeButton.setFocusPainted(false);

        // Bordure arrondie
        newMazeButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        topPanel.add(labelPanel); // Add label to the left
        topPanel.add(buttonPanel); // Add button to the right
        topPanel.add(shortPathButtonPanel);

        gridPanel = new LabyrinthePanel(laby, playerPosition,solution);
        gridPanel.setBounds(0, 150, 800, 500);
        victoryPanel = new Victory();
        victoryPanel.setVisible(false);

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new OverlayLayout(gamePanel));
        gamePanel.add(victoryPanel);
        gamePanel.add(gridPanel);
        

        
        add(topPanel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);

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

>>>>>>> Stashed changes
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                handlePlayerMovement(key);
            }
        });
    }



    private Stack<Sommet> sommetStack = new Stack<>();

    private void mot(Sommet sommet) {
        if (sommetStack.contains(sommet)) {
            while (!sommetStack.isEmpty() && !sommetStack.peek().equals(sommet)) {
                sommetStack.pop();
            }
        } else {
            sommetStack.push(sommet);
        }
    }

    private void handlePlayerMovement(int key) {
        int i = playerPosition.getI();
        int j = playerPosition.getJ();
        Sommet newPosition = null;

<<<<<<< Updated upstream
        // Vérifier les touches et définir la nouvelle position potentielle
        if (key == KeyEvent.VK_UP) {
            newPosition = new Sommet(i - 1, j, ' ');
        } else if (key == KeyEvent.VK_DOWN) {
            newPosition = new Sommet(i + 1, j, ' ');
        } else if (key == KeyEvent.VK_LEFT) {
            newPosition = new Sommet(i, j - 1, ' ');
        } else if (key == KeyEvent.VK_RIGHT) {
            newPosition = new Sommet(i, j + 1, ' ');
=======

        if (key == 38) {
            newPosition = laby.getSommet(i - 1, j);;
        } else if (key == 40) {
            newPosition = laby.getSommet(i +1, j);;
        } else if (key == 37) {
            newPosition = laby.getSommet(i , j-1);;
        } else if (key == 39) {
            newPosition = laby.getSommet(i, j+1);;
>>>>>>> Stashed changes
        }

        if (newPosition != null && isNeighbor(playerPosition, newPosition)) {
<<<<<<< Updated upstream
            playerPosition = newPosition; // Déplacer le joueur
            System.out.println(playerPosition);
            gridPanel.setPlayerPosition(playerPosition); // Mettre à jour l'affichage
        }
    }
=======
            mot(playerPosition);// Mise à jour de la pile
            playerPosition = newPosition;

            gridPanel.setPlayerPosition(playerPosition);
            System.out.println("Stack: " + stackToString());

            if (playerPosition.getI() == laby.getSortie().getI() && playerPosition.getJ() == laby.getSortie().getJ()) {
                victoryPanel.setVisible(true);
                victoryPanel.startAnimation();
                System.out.println("SommetStack content:");
                for (Sommet s : sommetStack) {
                    System.out.println("Sommet: (" + s.getI() + ", " + s.getJ() + ") -> " + s.getC());
                }
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
        Laby laby = new Laby(25,14);

        // Crée une nouvelle interface
        SwingUtilities.invokeLater(() -> {
            LabyrintheGUI gui = new LabyrintheGUI(laby);
            gui.setVisible(true);
        });
    }

>>>>>>> Stashed changes

    // Méthode pour vérifier si deux sommets sont voisins
    private boolean isNeighbor(Sommet current, Sommet target) {
        ListeSommets neighbors = laby.getVoisins(current);
        while (neighbors != null) {
            if (neighbors.getVal().equals(target)) {
                return true;
            }
            neighbors = neighbors.getSuivant();
        }
        return false;
    }
    public String stackToString() {
        StringBuilder sb = new StringBuilder();
        for (Sommet s : sommetStack) {
            sb.append(s.getC());
        }
        return sb.toString().trim();
    }

    public static void main(String[] args) {
        // Créer le labyrinthe
<<<<<<< Updated upstream
        Laby laby = new Laby(20, 30);
=======
        Laby laby = new Laby(10,5);
        laby.printMaze();
       
>>>>>>> Stashed changes

        // Lancer l'application
        SwingUtilities.invokeLater(() -> {
            LabyrintheGUI gui = new LabyrintheGUI(laby);
            gui.setVisible(true);
        });
<<<<<<< Updated upstream
=======





>>>>>>> Stashed changes
    }

    private static class LabyrinthePanel extends JPanel {
        private Laby laby;
        private Sommet playerPosition;

        public LabyrinthePanel(Laby laby, Sommet playerPosition) {
            this.laby = laby;
            this.playerPosition = playerPosition;
        }

        public void setPlayerPosition(Sommet playerPosition) {
            this.playerPosition = playerPosition;
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
                        g.setColor(Color.GREEN);
                        g.fillRect(x, y, cellWidth, cellHeight);
                    } else if (laby.getSortie().getI() == i && laby.getSortie().getJ() == j) {
                        g.setColor(Color.RED);
                        g.fillRect(x, y, cellWidth, cellHeight);
                    } else {
                        g.setColor(Color.WHITE);
                        g.fillRect(x, y, cellWidth, cellHeight);
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
            g.setColor(Color.BLUE);
            g.fillOval(playerX + cellWidth / 4, playerY + cellHeight / 4, cellWidth / 2, cellHeight / 2);
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
