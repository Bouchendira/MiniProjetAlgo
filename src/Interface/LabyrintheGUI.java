package Interface;

import models.Laby;
import models.Sommet;
import models.ListeSommets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
        setLayout(new BorderLayout());

        gridPanel = new LabyrinthePanel(laby, playerPosition);
        add(gridPanel, BorderLayout.CENTER);

        // Ajouter un écouteur pour les touches du clavier
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                handlePlayerMovement(key);
            }
        });
    }

    private void handlePlayerMovement(int key) {
        int i = playerPosition.getI();
        int j = playerPosition.getJ();
        Sommet newPosition = null;

        // Vérifier les touches et définir la nouvelle position potentielle
        if (key == KeyEvent.VK_UP) {
            newPosition = new Sommet(i - 1, j, ' ');
        } else if (key == KeyEvent.VK_DOWN) {
            newPosition = new Sommet(i + 1, j, ' ');
        } else if (key == KeyEvent.VK_LEFT) {
            newPosition = new Sommet(i, j - 1, ' ');
        } else if (key == KeyEvent.VK_RIGHT) {
            newPosition = new Sommet(i, j + 1, ' ');
        }

        // Vérifier si le mouvement est valide (pas de mur)
        if (newPosition != null && isNeighbor(playerPosition, newPosition)) {
            playerPosition = newPosition; // Déplacer le joueur
            System.out.println(playerPosition);
            gridPanel.setPlayerPosition(playerPosition); // Mettre à jour l'affichage
        }
    }

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


    public static void main(String[] args) {
        // Créer le labyrinthe
        Laby laby = new Laby(20, 30);

        // Lancer l'application
        SwingUtilities.invokeLater(() -> {
            LabyrintheGUI gui = new LabyrintheGUI(laby);
            gui.setVisible(true);
        });
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
