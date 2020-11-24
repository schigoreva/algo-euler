package view;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.GraphModel;

import java.util.stream.Collectors;

public class ControlView extends VBox {

    private Text textError;

    public ControlView(GraphView graphView, Stage root) {
        super(10);

        textError = new Text();
        setTranslateX(30);
        setTranslateY(90);
        VBox vertex1 = new VBox(5);
        VBox vertex2 = new VBox(5);
        HBox enterEdge = new HBox(10);
        HBox deleteButtons = new HBox(10);
        Button createEdge = new Button("Создать ребро");
        TextField vertexText1 = new TextField();
        TextField vertexText2 = new TextField();
        Text answerText = new Text();
        Button resetButton = new Button("Сброс");
        Button undoButton = new Button("Отмена");
        Button infoButton = new Button("Информация");
        Button findRoute = new Button("Найти маршрут");

        createEdge.setOnAction(actionEvent -> {
            try {
                graphView.addEdge(Integer.parseInt(vertexText1.getText()), Integer.parseInt(vertexText2.getText()));
                setSuccess("Ребро добавлено успешно");
            } catch (NumberFormatException e) {
                setError("Неверный формат вершин");
            } catch (IllegalArgumentException e) {
                setError(e.getMessage());
            }
        });

        resetButton.setOnAction(actionEvent -> {
            graphView.clear();
        });

        undoButton.setOnAction(actionEvent -> {
            try {
                graphView.undo();
                setSuccess("Действие отменено успешно");
            } catch (RuntimeException e) {
                setError(e.getMessage());
            }
        });

        infoButton.setOnAction(actionEvent -> {
            new InfoView(root);
        });

        findRoute.setOnAction(actionEvent -> {
            try {
                setSuccess("Эйлеров путь:\n" +
                        graphView.getEulerPath().stream().map(Object::toString).collect(Collectors.joining(" - ")));
            } catch (UnsupportedOperationException e) {
                setError(e.getMessage());
            }
        });



        vertex1.getChildren().add(new Text("Вершина 1"));
        vertex1.getChildren().add(vertexText1);

        vertex2.getChildren().add(new Text("Вершина 2"));
        vertex2.getChildren().add(vertexText2);

        enterEdge.getChildren().add(vertex1);
        enterEdge.getChildren().add(vertex2);

        deleteButtons.getChildren().add(resetButton);
        deleteButtons.getChildren().add(undoButton);

        getChildren().add(infoButton);
        getChildren().add(enterEdge);
        getChildren().add(createEdge);
        getChildren().add(deleteButtons);
        getChildren().add(findRoute);
        getChildren().add(answerText);
        textError.setFont(Font.font(null, FontWeight.BOLD, 20));
        getChildren().add(textError);
    }

    public void setError(String text) {
        textError.setText(text);
        textError.setFill(Color.RED);
    }

    public void setSuccess(String text) {
        textError.setText(text);
        textError.setFill(Color.GREEN);
    }
}
