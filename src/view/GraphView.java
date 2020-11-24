package view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import model.GraphModel;

import java.util.ArrayList;
import java.util.List;

import static view.ViewPreferences.RADIUS;

public class GraphView extends Group {

    private static final int MARGIN = 30;

    private Rectangle back;
    private Group vertexContainer = new Group();
    private Group edgeContainer = new Group();
    private GraphModel model = new GraphModel();
    private List<VertexView> vertices = new ArrayList<>();
    private List<UndoAction> undoActions = new ArrayList<>();
    private ControlView controlView;

    public GraphView(int width, int height) {
        getChildren().add(new Rectangle(width, height, Color.WHITE));
        back = new Rectangle(MARGIN, MARGIN, width - MARGIN * 2, height - MARGIN * 2);
        back.setFill(Color.TRANSPARENT);
        back.setStrokeType(StrokeType.INSIDE);
        back.setStroke(Color.BLACK);
        back.setOnMouseClicked(mouseEvent -> {
            addVertex(mouseEvent.getX(), mouseEvent.getY());
        });
        getChildren().add(back);
        getChildren().add(edgeContainer);
        getChildren().add(vertexContainer);
    }

    public void addEdge(int startVertex, int endVertex) throws IllegalArgumentException {
        model.addEdge(startVertex, endVertex);
        EdgeView edge = new EdgeView(vertices.get(startVertex - 1).getTranslateX(), vertices.get(startVertex - 1).getTranslateY(),
                vertices.get(endVertex - 1).getTranslateX(), vertices.get(endVertex - 1).getTranslateY());
        edgeContainer.getChildren().add(edge);
        undoActions.add(new AddEdgeAction(startVertex, endVertex));
    }

    public void addVertex(double x, double y) {
        if (vertices.stream().anyMatch(i -> ((i.x - x) * (i.x - x) + (i.y - y) * (i.y - y)) <= 4 * RADIUS * RADIUS)) {
            controlView.setError("Слишком близко к другой вершине");
            return;
        }
        controlView.setSuccess("Вершина добавлена успешно");
        VertexView vertexView = new VertexView(x, y, model.getVertexNumber() + 1);
        vertices.add(vertexView);
        vertexContainer.getChildren().add(vertexView);
        model.addVertex();
        undoActions.add(new AddVertexAction());
    }

    public void clear() {
        model = new GraphModel();
        edgeContainer.getChildren().clear();
        vertexContainer.getChildren().clear();
        vertices.clear();
    }

    public void undo() throws RuntimeException {
        if (undoActions.isEmpty()) {
            throw new RuntimeException("Нет действий, которые можно отменить");
        }
        UndoAction action = undoActions.get(undoActions.size() - 1);
        action.undo();
        undoActions.remove(action);
    }

    public void setControlView(ControlView controlView) {
        this.controlView = controlView;
    }

    public List<Integer> getEulerPath() throws UnsupportedOperationException {
        return model.getEulerPath();
    }

    private interface UndoAction {
        void undo();
    }

    private class AddVertexAction implements UndoAction {
        public void undo() {
            model.undoAddVertex();
            vertexContainer.getChildren().remove(vertexContainer.getChildren().size() - 1);
            vertices.remove(vertices.size() - 1);
        }
    }

    private class AddEdgeAction implements UndoAction {
        int startVertex, endVertex;

        public AddEdgeAction(int startVertex, int endVertex) {
            this.startVertex = startVertex;
            this.endVertex = endVertex;
        }

        public void undo() {
            model.undoAddEdge(startVertex, endVertex);
            edgeContainer.getChildren().remove(edgeContainer.getChildren().size() - 1);
        }
    }
}
