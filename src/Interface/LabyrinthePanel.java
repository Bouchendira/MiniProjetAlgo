package Interface;

import models.Laby;
import models.ListeSommets;
import models.Sommet;

import javax.swing.*;
import java.awt.*;

//cette classe dessine la Panel de labyrinthe a partir de graphe
public  class LabyrinthePanel extends JPanel {
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
        return new Dimension(800, 500);
    }


    public void setPlayerPosition(Sommet playerPosition) {
        this.playerPosition = playerPosition;
        repaint(); // Redessiner le labyrinthe avec la nouvelle position du joueur
    }

    public void setSolution(ListeSommets l) {
        this.solution = l;
        repaint(); // Redessiner le solution
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


    // dessiner les chemins de labyrinthe
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
            if (neighbor.getI() == i - 1 && neighbor.getJ() == j) { // Mur du haut
                topWall = false;
            } else if (neighbor.getI() == i + 1 && neighbor.getJ() == j) { // Mur du bas
                bottomWall = false;
            } else if (neighbor.getI() == i && neighbor.getJ() == j - 1) { // Mur de gauche
                leftWall = false;
            } else if (neighbor.getI() == i && neighbor.getJ() == j + 1) { // Mur de droite
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
