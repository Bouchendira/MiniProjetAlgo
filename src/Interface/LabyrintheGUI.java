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
        setTitle("Labyrinthe ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        gridPanel = new LabyrinthePanel(laby);
        add(gridPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // Create  labyrinth
        Laby laby = new Laby(20, 30);

        // Launch
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

            // Draw cells and  connections
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    // Draw  cell
                    int x = j * cellWidth;
                    int y = i * cellHeight;

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

                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, cellWidth, cellHeight);

                    // draw route
                    ListeSommets neighbors = laby.getVoisins(new Sommet(i, j, ' '));
                    while (neighbors != null) {
                        Sommet neighbor = neighbors.getVal();
                        drawConnection(g, x, y, neighbor, cellWidth, cellHeight);
                        neighbors = neighbors.getSuivant();
                    }
                }
            }
        }

        private void drawConnection(Graphics g, int x, int y, Sommet neighbor, int cellWidth, int cellHeight) {
            int neighborX = neighbor.getJ() * cellWidth + cellWidth / 2;
            int neighborY = neighbor.getI() * cellHeight + cellHeight / 2;

            int centerX = x + cellWidth / 2;
            int centerY = y + cellHeight / 2;

            g.setColor(Color.BLUE);
            g.drawLine(centerX, centerY, neighborX, neighborY);
        }
    }
}


