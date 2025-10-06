package de.khudhurayaz.cropCircle;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;

class View {

    private final Stage primaryStage;
    private Image originalImage;
    private final ImageView imageView;
    private final Circle clip;
    private final Slider radiusSlider;
    private double previewWidth;
    private double previewHeight;
    private final Label infoLabel;
    private final Label positionLabel;
    private final Label sizeLabel;
    private final Label bildSizeLabel;
    private Label hintLabel;
    private Button loadBtn;
    private Button saveBtn;
    private Button resetBtn;
    private Button openFolderBtn;
    private File lastSavedFile;
    private File loadFile;
    private StackPane previewPane;
    private double dragOffsetX;
    private double dragOffsetY;
    private VBox controls;

    public View(Stage primaryStage) {
        this.primaryStage = primaryStage;
        imageView = new ImageView();
        clip = new Circle();
        radiusSlider = new Slider();
        previewWidth = 400;
        previewHeight = 400;
        positionLabel = Utils.createLabel("Zentrum: (-, -)");
        sizeLabel = Utils.createLabel("Radius: - px");
        bildSizeLabel = Utils.createLabel("Original: -");
        infoLabel = Utils.createLabel("Info: Kein Bild geladen");

        setup();
        events();
    }

    private void setup() {
        imageSetup();
        clipSetup();
        overlaySetup();
        previewSetup();
        buttonSetup();
        styleLabel();

        controls = new VBox(8,
                Utils.createLabel(
                        "Controls",
                        "-fx-text-fill: #fff; -fx-font-size: 28px;"),
                Utils.createSeparator(),
                Utils.createLabel("Radius", "-fx-text-fill: #fff;"),
                radiusSlider,
                positionLabel,
                sizeLabel,
                bildSizeLabel,
                infoLabel,
                Utils.createHBox(loadBtn, saveBtn),
                Utils.createHBox(resetBtn, openFolderBtn)
        );
        controls.setAlignment(Pos.TOP_LEFT);
    }

    private void imageSetup() {
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
    }

    private void clipSetup() {
        clip.setCenterX(0);
        clip.setCenterY(0);
        clip.setRadius(0);
        imageView.setClip(clip);
    }

    private void overlaySetup() {
        hintLabel = Utils.createLabel("Kreis mit der Maus verschieben");
        hintLabel.getStyleClass().add("hintLabel");
        hintLabel.setVisible(false);
    }

    private void previewSetup() {
        previewPane = new StackPane(imageView, hintLabel);
        previewPane.setPrefSize(previewWidth, previewHeight);
        previewPane.setMaxSize(previewWidth, previewHeight);
        previewPane.setOpacity(.5);
        previewPane.getStyleClass().add("previewPane");
        previewPane.setAlignment(Pos.CENTER);
    }

    private void buttonSetup() {
        loadBtn = Utils.createButton("Bild laden...");
        saveBtn = Utils.createButton("Rundes PNG speichern...");
        saveBtn.setDisable(true);
        resetBtn = Utils.createButton("Zurücksetzen");
        resetBtn.setDisable(true);
        resetBtn.setOnAction(e -> {
            reset();
        });

        openFolderBtn = Utils.createButton("Ordner öffnen");
        openFolderBtn.setDisable(true);
        openFolderBtn.setOnAction(e -> {
            if (lastSavedFile != null) {
                try {
                    Desktop.getDesktop().open(lastSavedFile.getParentFile());
                } catch (Exception ex) {
                    infoLabel.setText("Fehler beim Öffnen des Ordners");
                }
            }
        });

        radiusSlider.setMin(10);
        radiusSlider.setDisable(true);
        radiusSlider.valueProperty().addListener((obs, o, n) -> {
            clip.setRadius(n.doubleValue());
            Crop.keepCircleInBounds(clip, previewWidth, previewHeight);
            updateLabels();
            hintLabel.setVisible(true);
            previewPane.setOpacity(1);
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                javafx.application.Platform.runLater(() -> hintLabel.setVisible(false));
            }).start();
        });

        previewPane.setOnMousePressed(e -> {
            dragOffsetX = e.getX() - clip.getCenterX();
            dragOffsetY = e.getY() - clip.getCenterY();
        });

        previewPane.setOnMouseDragged(e -> {
            double newX = e.getX() - dragOffsetX;
            double newY = e.getY() - dragOffsetY;
            clip.setCenterX(newX);
            clip.setCenterY(newY);
            Crop.keepCircleInBounds(clip, previewWidth, previewHeight);
            updateLabels();
        });
    }

    private void reset(){
        clip.setRadius(0);
        clip.setCenterX(0);
        clip.setCenterY(0);

        radiusSlider.setDisable(true);
        saveBtn.setDisable(true);
        openFolderBtn.setDisable(true);
        resetBtn.setDisable(true);

        //slider
        radiusSlider.setValue(1);

        //Labels
        positionLabel.setText("Zentrum: (-, -)");
        sizeLabel.setText("Radius: - px");
        bildSizeLabel.setText("Original: -");
        infoLabel.setText("Info: Kein Bild geladen");
        hintLabel.setVisible(false);

        //Image
        imageView.setImage(null);
    }

    private void styleLabel(){
        Label[] labels = {bildSizeLabel, sizeLabel, positionLabel, infoLabel};

        for (Label lbl : labels) {
            lbl.getStyleClass().add("white");
        }
    }

    private void resetClip() {
        clip.setCenterX(previewWidth / 2);
        clip.setCenterY(previewHeight / 2);
        clip.setRadius(Math.min(previewWidth, previewHeight) / 2);
    }

    private void updateLabels() {
        positionLabel.setText(String.format("Zentrum: (%.0f, %.0f)", clip.getCenterX(), clip.getCenterY()));
        sizeLabel.setText(String.format("Radius: %.0f px", clip.getRadius()));
        infoLabel.setText("Info: Bild '" + loadFile.getName() + "' geladen!");
        bildSizeLabel.setText(String.format("Original: %.0f × %.0f px ", originalImage.getWidth(), originalImage.getHeight()));
    }

    /**
     * Save und Open Button
     */
    private void events() {
        loadBtn.setOnAction(e -> {
            File f = Crop.open(
                    primaryStage,
                    "Bild wählen",
                    new FileChooser.ExtensionFilter("Bilder", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif"),
                    new FileChooser.ExtensionFilter("Alle Dateien", "*.*"));
            if (f != null) {
                loadFile = f;
                Image img = new Image(f.toURI().toString());
                originalImage = img;

                // Begrenzung auf Scene-Größe mit Puffer
                double maxSceneWidth = primaryStage.getScene().getWidth() - 100;
                double maxSceneHeight = primaryStage.getScene().getHeight() - 300;
                // Bild Größe
                double imgWidth = img.getWidth();
                double imgHeight = img.getHeight();
                // Skalierung
                double scale = Math.min(maxSceneWidth / imgWidth, maxSceneHeight / imgHeight);
                // an das Vorschaufenster anpassen
                previewWidth = imgWidth * scale;
                previewHeight = imgHeight * scale;
                //vorschauattributen an das imageview anpassen
                imageView.setFitWidth(previewWidth);
                imageView.setFitHeight(previewHeight);
                imageView.setImage(img);
                //vorschau pane die vorschauattributen setzen
                previewPane.setPrefSize(previewWidth, previewHeight);
                previewPane.setMaxSize(previewWidth, previewHeight);
                // radius für circle und slider
                double initialRadius = Math.min(previewWidth, previewHeight) / 2;
                clip.setRadius(initialRadius);
                clip.setCenterX(previewWidth / 2);
                clip.setCenterY(previewHeight / 2);
                //Slider
                radiusSlider.setMin(10);
                radiusSlider.setMax(initialRadius);
                radiusSlider.setValue(initialRadius);

                //Buttons
                radiusSlider.setDisable(false);
                saveBtn.setDisable(false);
                resetBtn.setDisable(false);

                // reset und update der labels aufrufen
                resetClip();
                updateLabels();
            }
        });
        // button zum Speichern
        saveBtn.setOnAction(e -> {
            File save = Crop.save(
                    primaryStage,
                    originalImage,
                    clip,
                    previewWidth,
                    previewHeight,
                    infoLabel
            );
            if (save != null) {
                lastSavedFile = save;
                openFolderBtn.setDisable(false);
                infoLabel.setText("Gespeichert: " + save.getAbsoluteFile());
            }
        });
    }

    public StackPane getPreviewPane() {
        return previewPane;
    }

    public VBox getControls() {
        return controls;
    }

    public Stage getStage() {
        return primaryStage;
    }
}