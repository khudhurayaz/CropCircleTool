package de.khudhurayaz.cropCircle;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * <h1>Crop Class</h1>
 *
 * <section id=open>
 *     <h1>Methode: Open</h1>
 * </section>
 * @author Khudhur, Ayaz
 * @version 18.09.2025 - 1.0.0.0
 */

class Crop {

    /**
     * Öffnet ein Dateiauswahlfenster (FileChooser) und gibt die vom Benutzer ausgewählte Datei zurück.
     *
     * @param primaryStage        Das Hauptfenster der Anwendung, in dem der Dialog angezeigt wird.
     * @param fileChooserTitle    Der Titel, der im Dateiauswahlfenster angezeigt wird.
     * @param extensionFilter     Eine Liste von Filteroptionen, um die Auswahl auf bestimmte Dateitypen zu beschränken.
     *                            Beispiel: Nur .png oder .jpg Dateien anzeigen.
     * @return Die vom Benutzer ausgewählte Datei oder {@code null}, wenn keine Auswahl getroffen wurde.
     */
    public static File open(
            Stage primaryStage,
            String fileChooserTitle,
            FileChooser.ExtensionFilter...extensionFilter) {
        FileChooser fc = new FileChooser();
        fc.setTitle(fileChooserTitle);
        fc.getExtensionFilters().addAll(extensionFilter);
        File bilderOrdner = new File(System.getProperty("user.home"), "Pictures");
        if (bilderOrdner.exists()) {
            fc.setInitialDirectory(bilderOrdner);
        }
        return fc.showOpenDialog(primaryStage);
    }

    /**
     * Öffnet ein Speichern-Dialogfenster und speichert ein kreisförmig zugeschnittenes Bild als PNG-Datei.
     *
     * @param primaryStage    Das Hauptfenster der Anwendung, in dem der Speichern-Dialog angezeigt wird.
     * @param originalImage   Das Originalbild, das gespeichert werden soll. Wenn {@code null}, wird ein Hinweis angezeigt.
     * @param clip            Ein Kreisobjekt, das als Maske für den Zuschnitt des Bildes dient.
     * @param previewWidth    Die Breite der Vorschau bzw. des zugeschnittenen Bildes.
     * @param previewHeight   Die Höhe der Vorschau bzw. des zugeschnittenen Bildes.
     * @param info            Ein Label zur Anzeige von Status- oder Fehlermeldungen.
     * @return Die gespeicherte Datei oder {@code null}, wenn keine Datei ausgewählt oder ein Fehler aufgetreten ist.
     */
    public static File save(
            Stage primaryStage,
            Image originalImage,
            Circle clip,
            double previewWidth,
            double previewHeight,
            Label info
            ) {
        if (originalImage == null) {
            info.setText("[Crop][save] Image ist leer oder wurde nicht initialisiert!");
            return null;
        }
        FileChooser fc = new FileChooser();
        fc.setTitle("Speichern als PNG");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Datei", "*.png"));
        // in Ordner Bilder die FileChooser öffnen
        File bilderOrdner = new File(System.getProperty("user.home"), "Pictures");
        if (bilderOrdner.exists()) {
            fc.setInitialDirectory(bilderOrdner);
        }
        File out = fc.showSaveDialog(primaryStage);
        if (out != null) {
            try {
                exportCircularPng(out, clip, originalImage, previewWidth, previewHeight);
                info.setText("Gespeichert: " + out.getAbsoluteFile());
            } catch (Exception ex) {
                info.setText("Fehler: " + ex.getMessage());
            }
        }
        return out;
    }

    /**
     * Das Bild wird in runden Form gezeichnet und dann im Datei gespeichert!
     * @param outFile Dateipfad zum Speichern das Bild.
     * @param clip Kreis wie das Bild gezeichnet werden soll.
     * @param originalImage Originale Bild
     * @param previewWidth Die vorschaubreite
     * @param previewHeight Die vorschauhöhe
     * @throws Exception Fehler, sobald das Bild gespeichert werden soll, könnte auftreten.
     */
    private static void exportCircularPng(
            File outFile,
            Circle clip,
            Image originalImage,
            double previewWidth,
            double previewHeight) throws Exception {
        //BufferedImage objekt erstellen
        BufferedImage origBuf = SwingFXUtils.fromFXImage(originalImage, null);

        // x, y scale
        double scaleX = originalImage.getWidth() / previewWidth;
        double scaleY = originalImage.getHeight() / previewHeight;

        // Circle Attributen
        int r = (int) Math.round(clip.getRadius() * scaleX);
        int cx = (int) Math.round(clip.getCenterX() * scaleX);
        int cy = (int) Math.round(clip.getCenterY() * scaleY);

        //durchmesser
        int durchmesser = 2 * r;

        // rundes bufferedImage erstellen
        BufferedImage circleImg = new BufferedImage(durchmesser, durchmesser, BufferedImage.TYPE_INT_ARGB);
        //Graphics2D
        Graphics2D g2 = circleImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //Clip setzen
        g2.setClip(new Ellipse2D.Double(0, 0, durchmesser, durchmesser));

        // x, y position
        int srcX = cx - r;
        int srcY = cy - r;

        // clip zeichnen
        g2.drawImage(origBuf,
                0, 0, durchmesser, durchmesser,
                srcX, srcY, srcX + durchmesser, srcY + durchmesser,
                null);

        // graphics2d schliessen
        g2.dispose();
        // mit ImageIO in den angegebenen File schreiben, im format png
        ImageIO.write(circleImg, "png", outFile);

        System.out.println("[Crop][exportCircularPng] Erfolgreich exportiert!");
    }

    /**
     * Stellt sicher, dass ein Kreis innerhalb der Grenzen einer Vorschaufläche bleibt.
     * <br>
     * Diese Methode überprüft die aktuelle Position und den Radius des übergebenen Kreises (`clip`)
     * und passt die Position gegebenenfalls so an, dass der gesamte Kreis innerhalb der
     * angegebenen Vorschaufläche (`previewWidth` x `previewHeight`) liegt.
     *
     * @param clip          Der Kreis, dessen Position überprüft und ggf. angepasst wird.
     * @param previewWidth  Die Breite der Vorschaufläche, innerhalb der sich der Kreis befinden soll.
     * @param previewHeight Die Höhe der Vorschaufläche, innerhalb der sich der Kreis befinden soll.
     */
    public static void keepCircleInBounds(Circle clip, double previewWidth, double previewHeight) {
        double r = clip.getRadius();
        double cx = clip.getCenterX();
        double cy = clip.getCenterY();

        if (cx - r < 0) cx = r;
        if (cy - r < 0) cy = r;
        if (cx + r > previewWidth) cx = previewWidth - r;
        if (cy + r > previewHeight) cy = previewHeight - r;

        clip.setCenterX(cx);
        clip.setCenterY(cy);
    }

}
