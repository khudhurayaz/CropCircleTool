package de.khudhurayaz.cropCircle.test;

import de.khudhurayaz.cropCircle.CropCircle;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * TestApp – Das Tool wird hier ausgeführt.
 *
 * <p>
 * Zweck: TestApp dient zum Testen der Anwendung.
 * </p>
 *
 * @author Khudhur, Ayaz
 * @version 06.10.2025 - 1.0.0.0
 */
public class TestApp extends Application {

    @Override
    public void start(Stage stage) {
        CropCircle cropCircle = new CropCircle(stage);
        cropCircle.createApp();
    }

    public static void main(String[] args) {
        launch(args);
    }
}