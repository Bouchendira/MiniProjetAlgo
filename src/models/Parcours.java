package models;

import java.util.*;

public class Parcours {

    /**
     * Trouve le chemin le plus court entre deux sommets avec Dijkstra
     */
    public ListeSommets findShortestPath(ListeSommets[][] voisins, Sommet start, Sommet end) {
        // Stockage des distances entre le sommet de depart et chaque sommet visite
        Map<Integer, Integer> distances = new HashMap<>();
        // Stockage des predecesseurs pour reconstruire le chemin
        Map<Integer, Sommet> predecessors = new HashMap<>();
        // File de priorite pour explorer les sommets par ordre de distance croissante
        PriorityQueue<Sommet> pq = new PriorityQueue<>(Comparator.comparingInt(s -> distances.getOrDefault(s.hashCode(), Integer.MAX_VALUE)));

        // Initialisation avec le sommet de depart a distance 0
        distances.put(start.hashCode(), 0);
        pq.add(start);

        while (!pq.isEmpty()) {
            // Recupere le sommet le plus proche non traite
            Sommet current = pq.poll();
            int ci = current.getI(), cj = current.getJ();

            // Si on arrive a la destination, on arrete la recherche

            if (current.hashCode() == end.hashCode()) break;

            // Parcours de tous les voisins du sommet courant
            ListeSommets neighbors = voisins[ci][cj];
            while (neighbors != null) {
                Sommet neighbor = neighbors.getVal();
                // Calcul de la nouvelle distance (chaque passage vaut 1)
                int newDist = distances.get(current.hashCode()) + 1;

                // Si on trouve un chemin plus court vers ce voisin
                if (!distances.containsKey(neighbor.hashCode()) || newDist < distances.get(neighbor.hashCode())) {
                    // Mise a jour de la distance et du predecesseur
                    distances.put(neighbor.hashCode(), newDist);
                    predecessors.put(neighbor.hashCode(), current);
                    // Ajout du voisin a la file pour exploration future
                    pq.add(neighbor);
                }
                neighbors = neighbors.getSuivant();
            }
        }

        // Reconstruction du chemin a partir des predecesseurs
        return reconstructPath(predecessors, start, end);
    }

    /**
     * Reconstruit le chemin a partir de la liste des predecesseurs

     */
    private ListeSommets reconstructPath(Map<Integer, Sommet> predecessors, Sommet start, Sommet end) {
        // Verifie si un chemin existe
        if (!predecessors.containsKey(end.hashCode())) return null;

        // Initialisation du chemin avec le sommet d'arrivee
        ListeSommets path = new ListeSommets(end);
        Sommet current = end;

        // Remonte le chemin en suivant les predecesseurs jusqu'au depart
        while (current.hashCode() != start.hashCode()) {
            current = predecessors.get(current.hashCode());
            // Ajout du sommet au debut du chemin
            ListeSommets newNode = new ListeSommets(current);
            newNode.setSuivant(path);
            path = newNode;
        }
        // debug
        System.out.println(path);

        return path;
    }
}