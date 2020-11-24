package view;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import static view.ViewPreferences.RADIUS;

public class VertexView extends StackPane {

    double x;
    double y;

    public VertexView(double x, double y, int num) {
        this.x = x;
        this.y = y;
        setTranslateX(x - RADIUS);
        setTranslateY(y - RADIUS);
        Circle circle = new Circle(RADIUS, Color.BLACK);
        Text number = new Text(Integer.toString(num));
        number.setFill(Color.WHITE);
        getChildren().add(circle);
        getChildren().add(number);

    }
}
