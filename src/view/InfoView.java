package view;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InfoView extends Stage {

    public InfoView(Stage parent) {
        initOwner(parent);
        initModality(Modality.APPLICATION_MODAL);
        Group group = new Group();
        group.setLayoutX(20);
        group.setLayoutY(20);
        group.getChildren().add(new Rectangle(400, 350, Color.WHITE));
        group.getChildren().add(new Text("Задан неориентированный граф G=(V,E).\n" +
                "Найти маршрут (если он существует), который\n" +
                "проходит по каждому ребру графа один раз\n" +
                "(фактически – обвести граф не отрывая руки)."));
        setScene(new Scene(group));
        show();
    }
}
