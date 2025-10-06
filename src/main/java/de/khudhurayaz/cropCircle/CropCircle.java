package de.khudhurayaz.cropCircle;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.Objects;

/**
 * CropCircle – Man kann durch die CropCircle auf die Anwendung zugreifen und die Anwendung somit starten.
 *
 * <p>
 * Zweck: CropCircle dient zur Anwendung und Benutzung der App.
 * </p>
 *
 * <p>
 * Diese Klasse initialisiert die Hauptoberfläche, verwaltet das Fenster (Stage)
 * und stellt Methoden zum Öffnen und Speichern von Bildern bereit.
 * </p>
 *
 * @author Khudhur, Ayaz
 * @version 05.10.2025 - 1.0.0.0
 */
public class CropCircle {

    private final View view;

    /**
     * Konstruktor der Klasse {@code CropCircle}.
     * <p>
     * Initialisiert die Benutzeroberfläche, lädt das App-Icon und
     * bereitet die Hauptbühne (Stage) für die Anzeige vor.
     * </p>
     *
     * @param stage Die Hauptbühne (Stage) der Anwendung.
     */
    public CropCircle(Stage stage) {
        System.err.println("["+Utils.APP_NAME+"][CropCircle] CropCircle initialisierung....");
        view = new View(stage);
        view.getStage().getIcons().add(new Image(
                Objects.requireNonNull(CropCircle.class.getResourceAsStream("CropCircleIcon.png"))
        ));
        System.err.println("["+Utils.APP_NAME+"][CropCircle] CropCircle initialisierung end....");
    }

    /**
     * Erstellt und zeigt die Anwendung.
     * <p>
     * Diese Methode setzt den Fenstertitel, lädt die Szene,
     * fügt das Layout hinzu und zeigt die App dem Benutzer an.
     * </p>
     */
    public void createApp(){
        view.getStage().setTitle("Rundes Bild - Vorschau & Export");
        Scene scene = new Scene(getApp());
        view.getStage().setResizable(false);
        view.getStage().setScene(scene);
        view.getStage().getIcons().add(new Image(
                Objects.requireNonNull(CropCircle.class.getResourceAsStream("CropCircleIcon.png"))
        ));
        view.getStage().setTitle(Utils.APP_NAME);
        view.getStage().show();
    }

    /**
     * Baut das Hauptlayout der Anwendung auf.
     * <p>
     * Diese Methode erstellt das zentrale {@link BorderPane}-Layout
     * und fügt alle UI-Bereiche (Titel, Vorschau, Steuerung, Fußzeile) hinzu.
     * </p>
     *
     * @return Das vollständige {@link BorderPane}-Layout der Anwendung.
     */
    public BorderPane getApp() {
        System.err.println("["+Utils.APP_NAME+"][CropCircle] App laden");

        BorderPane root = new BorderPane();
        root.setMinSize(800, 600);
        root.getStylesheets().add(Objects.requireNonNull(CropCircle.class.getResource("style.css")).toExternalForm());
        root.getStyleClass().add("root");

        // Vorschau zentriert
        root.setCenter(view.getPreviewPane());

        // Steuerelemente rechts
        VBox controls = view.getControls();
        controls.getStyleClass().add("controls");
        root.setRight(controls);

        // Titel oben
        Label title = new Label("CropCircle – Vorschau & Export");
        title.getStyleClass().add("title");
        root.setTop(new HBox(title));

        // Status unten
        Label footer = new Label("© Ayaz Khudhur - Tools");
        footer.getStyleClass().add("footer");
        root.setBottom(new HBox(footer));

        System.err.println("["+Utils.APP_NAME+"][CropCircle] App initialisiert! Kann aufgerufen und angezeigt werden.");
        return root;
    }

    /**
     * Öffnet den Datei-Dialog zum Laden eines Bildes.
     *
     * @param fileChooserTitle  Der Titel des Datei-Dialogs.
     * @param extensionFilters  Die erlaubten Dateitypen (z. B. PNG, JPG).
     * @return Die ausgewählte Datei oder {@code null}, wenn der Dialog abgebrochen wurde.
     */
    public File open(
            String fileChooserTitle,
            FileChooser.ExtensionFilter...extensionFilters) {
        return Crop.open(view.getStage(), fileChooserTitle, extensionFilters);
    }

    /**
     * Speichert das bearbeitete (rund zugeschnittene) Bild.
     *
     * @param originalImage  Das Originalbild, das gespeichert werden soll.
     * @param clip           Der Kreis-Ausschnitt, der angewendet wurde.
     * @param previewWidth   Die Breite der Vorschau.
     * @param previewHeight  Die Höhe der Vorschau.
     * @param info           Ein Label zur Anzeige von Statusinformationen.
     * @return Die gespeicherte Datei oder {@code null}, wenn der Speichervorgang abgebrochen wurde.
     */
    public File save(
            Image originalImage,
            Circle clip,
            double previewWidth,
            double previewHeight,
            Label info
    ) {
        return Crop.save(view.getStage(), originalImage, clip, previewWidth, previewHeight, info);
    }
}
