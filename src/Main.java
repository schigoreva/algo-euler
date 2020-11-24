import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import view.GraphView;
import view.ControlView;

public class Main extends Application {
                                        //запуск приложения
    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox root = new HBox();
        GraphView graphView = new GraphView(Preferences.APP_WIDTH / 2, Preferences.APP_HEIGHT);
        ControlView controlView = new ControlView(graphView, primaryStage);
        graphView.setControlView(controlView);

        root.getChildren().add(graphView);
        root.getChildren().add(controlView);

        primaryStage.setResizable(false);
        primaryStage.setTitle("Эйлеров граф");
        primaryStage.setScene(new Scene(root, Preferences.APP_WIDTH, Preferences.APP_HEIGHT));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}