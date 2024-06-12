//Aidar Sarvartdinov

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class NegativeCycle {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Graph<Integer, Integer> graph = new Graph<>();
        int n = scanner.nextInt();
        for (int i = 1; i <= n; i++) {
            graph.addVertex(i);
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                int edge = scanner.nextInt();
                if (edge != 100000) {
                    graph.addEdge(i, j, edge);
                }
            }
        }
        ArrayList<Graph<Integer,Integer>.Vertex> cycle = graph.AidarSarvartdinov_sp();
        if (cycle.size() == 0) {
            System.out.println("NO");
        } else {
            System.out.println("YES");
            System.out.println(cycle.size());
            for (Graph<Integer,Integer>.Vertex vertex: cycle) {
                System.out.print(vertex.value + " ");
            }
        }
    }
}

class Graph<V,E> {
    class Vertex {
        V value;
        List<Vertex> adjacent;
        int indgree;
        int outdgree;

        public Vertex(V value) {
            this.value = value;
            this.adjacent = new LinkedList<>();
            this.indgree = 0;
            this.outdgree = 0;
        }
    }

    class Edge {
        Vertex from;
        Vertex to;
        E label;

        public Edge(Vertex from, Vertex to, E label) {
            this.from = from;
            this.to = to;
            this.label = label;
        }
    }
    List<Vertex> vertices;
    List<Edge> edges;

    public Graph() {
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    Vertex addVertex(V value) {
        Vertex v = new Vertex(value);
        this.vertices.add(v);
        return v;
    }

    public Vertex getVertex(V value) {
        for (Vertex vertex: vertices) {
            if (vertex.value == value) {
                return vertex;
            }
        }
        return null;
    }

    public Edge addEdge(V from, V to, E label) {
        return addEdge(getVertex(from), getVertex(to), label);
    }

    Edge addEdge(Vertex from, Vertex to, E label) {
        Edge edge = new Edge(from, to, label);
        this.edges.add(edge);
        from.adjacent.add(to);
        from.outdgree++;
        to.indgree++;
        return edge;
    }

    boolean adjacent(Vertex u, Vertex v) {
        for (Vertex vertex: u.adjacent) {
            if (vertex.equals(v)) {
                return true;
            }
        }
        return false;
    }

    void removeEdge(Edge edge) {
        edge.from.adjacent.remove(edge.to);
        edge.from.outdgree--;
        edge.to.indgree--;
        this.edges.remove(edge);
    }

    ArrayList<Vertex> AidarSarvartdinov_sp() {
        ArrayList<Vertex> cycle = new ArrayList<>();
        int MAX = 10000000;
        for (Vertex start: vertices) {
            int[] dist = new int[vertices.size()+1];
            for (int i = 0; i < vertices.size()+1; i++) {
                dist[i] = MAX;
            }
            dist[(int)start.value] = 0;
            ArrayList<Vertex> from = new ArrayList<>(vertices.size()+1);
            for (int i = 0; i <= vertices.size(); i++) {
                from.add(null);
            }
            for (int i = 0; i < vertices.size()-1; i++) {
                for (Edge edge: edges) {
                    if (dist[(int)edge.from.value] != MAX && dist[(int)edge.to.value] > dist[(int)edge.from.value] + (int)edge.label) {
                        dist[(int)edge.to.value] = Math.max(dist[(int)edge.from.value]+(int)edge.label, -MAX);//
                        from.set((int)edge.to.value, edge.from);
                    }
                }
            }
            for (Edge edge: edges) {
                Vertex fromV = edge.from;
                Vertex toV = edge.to;
                int label = (int)edge.label;
                if (dist[(int)fromV.value] != MAX && dist[(int)toV.value] > dist[(int)fromV.value] + label) {
                    from.set((int)toV.value, fromV);
                    for (int i = 0; i < vertices.size()-1; i++) {
                        toV = from.get((int)toV.value);
                    }
                    cycle.add(toV);
                    for (Vertex v = from.get((int)toV.value); !v.equals(toV); v = from.get((int)v.value)) {
                        cycle.add(v);
                    }
                    ArrayList<Vertex> result = new ArrayList<>();
                    for (int i = cycle.size()-1; i >= 0; i--) {
                        result.add(cycle.get(i));
                    }
                    return result;
                }
            }
        }
        
        return cycle;
    }
}