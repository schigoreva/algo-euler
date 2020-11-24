package view;

import javafx.scene.shape.Line;

import static view.ViewPreferences.RADIUS;

public class EdgeView extends Line {
    public EdgeView(double startX, double startY, double endX, double endY) {
        super(startX + RADIUS, startY + RADIUS, endX + RADIUS, endY + RADIUS);
    }
}
