package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class GraphModel {

    private int vertexNumber;
    private int edgeNumber;
    private List<List<Integer>> graph;

    public GraphModel() {
        vertexNumber = 0;
        edgeNumber = 0;
        graph = new ArrayList<>();
        graph.add(List.of()); //заглушка для нулевой вершины
    }

    public int getVertexNumber() {
        return vertexNumber;
    }   // возвращение номера вершины

    public void addVertex() {       // создание новой вершины
        vertexNumber++;
        graph.add(new ArrayList<>());
    }

    public void undoAddVertex() {   // отмена вершины, созданной предыдущем шагом
        graph.remove(vertexNumber);
        vertexNumber--;
    }

    public void addEdge(int a, int b) throws IllegalArgumentException {     // создание ребра
        if (a <= 0 || a > vertexNumber) {
            throw new IllegalArgumentException("Первая вершина не существует");
        }
        if (b <= 0 || b > vertexNumber) {
            throw new IllegalArgumentException("Вторая вершина не существует");
        }
        if (a == b) {
            throw new IllegalArgumentException("Нельзя добавлять петлю");
        }
        if (graph.get(a).contains(b)) {
            throw new IllegalArgumentException("Нельзя добавлять кратные ребра");
        }
        edgeNumber++;
        graph.get(a).add(b);
        graph.get(b).add(a);
    }

    private void dfs(int vertex, List<Boolean> used) {  // проверка на компоненты связности, с помощью поиска в глубину
        used.set(vertex, true);
        for (int i : graph.get(vertex)) {
            if (!used.get(i)) {
                dfs(i, used);
            }
        }
    }

    private void findEulerPath(int vertex, List<List<Edge>> graph, List<Integer> path) {    // нахождение эйлерова пути
        while (!graph.get(vertex).isEmpty()) {
            Edge e = graph.get(vertex).get(0);
            graph.get(vertex).remove(e);            // удаляем ребро, по которому собираемся пойти
            e.backEdge.deleted = true;
            if (!e.deleted) {
                findEulerPath(e.to, graph, path);
            }
        }
        path.add(vertex);
    }

    public List<Integer> getEulerPath() throws UnsupportedOperationException {      // получение эйлерова пути
        if (graph.stream().anyMatch(i -> !i.isEmpty())) {
            int root = IntStream.range(1, graph.size()).filter(x -> !graph.get(x).isEmpty()).findAny().getAsInt();
            List<Boolean> used = new ArrayList<>();
            IntStream.range(0, graph.size()).forEach(x -> used.add(false));
            dfs(root, used);
            if (IntStream.range(1, graph.size()).anyMatch(x -> !graph.get(x).isEmpty() && !used.get(x))) {
                throw new UnsupportedOperationException("Существует два ребра, которые находятся\n в разных компонентах связности");
            }
            long cnt = graph.stream().filter(i -> i.size() % 2 == 1).count();
            if (cnt <= 2) {
                if (cnt == 2) {     // берем любую вершину с нечетной степенью и считаем за начало пути
                    root = IntStream.range(1, graph.size()).filter(x -> graph.get(x).size() % 2 == 1).findAny().getAsInt();
                }
                List<List<Edge>> graphCopy = new ArrayList<>();
                IntStream.range(0, graph.size()).forEach(x -> graphCopy.add(new ArrayList<>()));
                for (int i = 1; i < graph.size(); i++) {
                    for (int j : graph.get(i)) {
                        if (i < j) {
                            Edge directEdge = new Edge(j);
                            Edge backEdge = new Edge(i);
                            directEdge.setBackEdge(backEdge);
                            backEdge.setBackEdge(directEdge);
                            graphCopy.get(i).add(directEdge);
                            graphCopy.get(j).add(backEdge);
                        }
                    }
                }
                List<Integer> path = new ArrayList<>();
                findEulerPath(root, graphCopy, path);
                return path;
            } else {
                throw new UnsupportedOperationException("В графе должно быть не больше\n двух вершин нечетной степени");
            }
        } else {
            return List.of();
        }
    }

    public void undoAddEdge(int a, int b) throws IllegalArgumentException {     // отмена ребра, созданного предыдущим шагом
        graph.get(a).remove(Integer.valueOf(b));
        graph.get(b).remove(Integer.valueOf(a));
        edgeNumber--;
    }

    public int getEdgeNumber() {
        return edgeNumber;
    }

    static class Edge {
        int to;
        Edge backEdge;
        boolean deleted;

        Edge(int to) {
            this.to = to;
            this.deleted = false;
        }

        public int getTo() {
            return to;
        }

        public void setTo(int to) {
            this.to = to;
        }

        public Edge getBackEdge() {
            return backEdge;
        }

        public void setBackEdge(Edge backEdge) {
            this.backEdge = backEdge;
        }

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }
    }
}