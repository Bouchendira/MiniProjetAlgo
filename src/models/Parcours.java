package models;

import java.util.*;

public class Parcours {


       public ListeSommets findShortestPath(ListeSommets[][] voisins,  Sommet start, Sommet end) {
        Map<Integer, Integer> distances = new HashMap<>();
        Map<Integer, Sommet> predecessors = new HashMap<>();
        PriorityQueue<Sommet> pq = new PriorityQueue<>(Comparator.comparingInt(s -> distances.getOrDefault(s.hashCode(), Integer.MAX_VALUE)));

        distances.put(start.hashCode(), 0);
        pq.add(start);

        while (!pq.isEmpty()) {
            Sommet current = pq.poll();
            int ci = current.getI(), cj = current.getJ();

            if (current.hashCode() == end.hashCode()) break;

            ListeSommets neighbors = voisins[ci][cj];
            while (neighbors != null) {
                Sommet neighbor = neighbors.getVal();
                int newDist = distances.get(current.hashCode()) + 1;

                if (!distances.containsKey(neighbor.hashCode()) || newDist < distances.get(neighbor.hashCode())) {
                    distances.put(neighbor.hashCode(), newDist);
                    predecessors.put(neighbor.hashCode(), current);
                    pq.add(neighbor);
                }
                neighbors = neighbors.getSuivant();
            }
        }

        return reconstructPath(predecessors, start, end);
    }


    private ListeSommets reconstructPath(Map<Integer, Sommet> predecessors, Sommet start, Sommet end) {
        if (!predecessors.containsKey(end.hashCode())) return null;

        ListeSommets path = new ListeSommets(end);
        Sommet current = end;

        while (current.hashCode() != start.hashCode()) {
            current = predecessors.get(current.hashCode());
            ListeSommets newNode = new ListeSommets(current);
            newNode.setSuivant(path);
            path = newNode;
        }
        System.out.println(path);

        return path;
    }

}
