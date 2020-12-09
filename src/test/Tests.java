package test;
import model.GraphModel;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    GraphModel graphModel = new GraphModel();

    @Test
    void checkUndoAddVertex() {     // проверка удаления вершины
        graphModel.addVertex();
        graphModel.undoAddVertex();
        assertEquals(graphModel.getVertexNumber(), 0);
    }

    @Test
    void checkUndoAddEdge() {   // проверка удаления ребра
        graphModel.addVertex();
        graphModel.addVertex();
        graphModel.addEdge(1, 2);
        graphModel.undoAddEdge(1, 2);
        assertEquals(graphModel.getEdgeNumber(), 0);
    }

    @Test
    void checkEulerPath() {        // проверка Эйлерова пути
        Random random = new Random();
        for (int test = 0; test < 10; ) {       // генерируем граф
            graphModel = new GraphModel();
            Set<Edge> edges = new HashSet<>();
            int n = random.nextInt() % 10 + 1;
            for (int i = 1; i <= n; i++) {
                graphModel.addVertex();
            }
            for (int i = 1; i <= n; i++) {
                for (int j = i + 1; j <= n; j++) {
                    if (Math.random() < 0.3) {
                        graphModel.addEdge(i, j);
                        edges.add(new Edge(i, j));
                    }
                }
            }
            try {
                List<Integer> path = graphModel.getEulerPath();
                for (int i = 0; i + 1 < path.size(); i++) {
                    assertTrue(edges.contains(new Edge(path.get(i), path.get(i + 1))));
                    edges.remove(new Edge(path.get(i), path.get(i + 1)));
                }
                assertTrue(edges.isEmpty());
                test++; // переходим к следующему тесту
            } catch (UnsupportedOperationException e) {
                //нет операций, пути не существует
            }
        }


    }

    private static class Edge {     // создаем здесь класс чтобы написать функцию equals для сравнения ребер
        int a, b;

        public Edge(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return (a == edge.a && b == edge.b) || (a == edge.b && b == edge.a);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a + b);
        }
    }
}
