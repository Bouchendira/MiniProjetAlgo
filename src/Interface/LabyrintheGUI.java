package Interface;

import models.Laby;
import models.Sommet;
import models.ListeSommets;
import javax.swing.*;
import java.awt.*;

public class LabyrintheGUI extends JFrame {
    private Laby laby;
    private LabyrinthePanel gridPanel;

    public LabyrintheGUI(Laby laby) {
        this.laby = laby;
        init();
    }

    private void init() {
        setTitle("Labyrinthe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        gridPanel = new LabyrinthePanel(laby);
        add(gridPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // Créer le labyrinthe
        Laby laby = new Laby(20, 30);
        Sommet sommet = laby.getSommet(0, 0);
        System.out.println("TEST");
        System.out.println(sommet);
        System.out.println("Labyrinthe");
        System.out.println("Entry : " + laby.getEntree());
        System.out.println("Exit : " + laby.getSortie());
        laby.printMaze();

        // Lancer l'application
        SwingUtilities.invokeLater(() -> {
            LabyrintheGUI gui = new LabyrintheGUI(laby);
            gui.setVisible(true);
        });
    }

    private static class LabyrinthePanel extends JPanel {
        private Laby laby;

        public LabyrinthePanel(Laby laby) {
            this.laby = laby;
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
