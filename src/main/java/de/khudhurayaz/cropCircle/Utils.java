package de.khudhurayaz.cropCircle;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * Utils – dieser Klasse beinhaltet die standard Methoden, die immer verwendet wird.
 *
 * <p>
 * Zweck: Utils dient zur vereinfachung des Codes und erleichterung für die Programmierung.
 * </p>
 *
 * @author Khudhur, Ayaz
 * @version 05.10.2025 - 1.0.0.0
 */
class Utils {

    /**
     * Label erstellen
     * @param txt Label beschriftung
     * @return Neues erstelltes Label
     */
    public static Label createLabel(String txt) {
        return new Label(txt);
    }

    /**
     * Überladung der createLabel methode.
     * @param text Ein String wird erwartet!
     * @param style Label style
     * @return Neues erstelltes Label
     */
    public static Label createLabel(String text, String style) {
        Label lbl = new Label(text);
        lbl.setStyle(style);
        return lbl;
    }

    /**
     * createButton erstellt ein Button und gibt es zurück.
     * @param name Button beschriftung
     * @return Neues initialisiertes Button.
     */
    public static Button createButton(String name) {
        return new Button(name);
    }

    /**
     * Ein Separator wird erstellt
     * @return Ausgabe ist der erstellte Separator.
     */
    public static Region createSeparator() {
        Region line = new Region();
        line.setPrefHeight(2);
        line.setPrefWidth(240);
        line.getStyleClass().add("separator");
        return line;
    }

    /**
     * Ein HBox erstellen und mit kinder befüllen.
     * @param nodes Kinder die im HBox kommen.
     * @return Neues HBox mit jeweiligen kinder.
     */
    public static HBox createHBox(Node...nodes) {
        HBox hb = new HBox(nodes);
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.setPadding(new Insets(5));
        hb.setSpacing(15);
        return hb;
    }

    public static final String APP_NAME = "CropCircle – Vorschau & Export";

}