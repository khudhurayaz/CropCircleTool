# Projektbeschreibung

CropCircle ist eine Java-Bibliothek mit JavaFX-UI-Komponenten zur Anzeige, Vorschau und zum Export runder Bilder. Das Projekt ist als wiederverwendbares Modul gedacht und wird in ein anderes (Host-)Projekt eingebunden und von dort aus gestartet.

## Integrieren als Abhängigkeit

### Maven
```xml
<dependency>
    <groupId>de.khudhurayaz</groupId>
    <artifactId>CropCircle</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle
```gradle
dependencies {
    implementation 'de.khudhurayaz:CropCircle:1.0.0'
}
```

---

## Voraussetzungen

- **Java**: 23
- **JavaFX**: 21.0.6
- **Build-Tool**: Maven oder Gradle (je nach Host-Projekt)

---

## Was Entwickler einbinden müssen

Entwickler, die CropCircle in ihr Projekt einbinden, müssen sowohl CropCircle als Dependency als auch die JavaFX-Module (inkl. javafx-swing) in ihrem Host-Projekt verfügbar machen. Das bedeutet: die JavaFX-Abhängigkeiten müssen im Host-Projekt (Maven oder Gradle) deklariert sein.

Wichtig: **javafx-swing muss enthalten sein**.

### Beispiel: Maven - Host-Projekt

1. CropCircle als Dependency (angenommene Koordinaten):

```xml
<dependency>
    <groupId>de.khudhurayaz</groupId>
    <artifactId>CropCircle</artifactId>
    <version>1.0.0</version>
</dependency>
```

2. JavaFX-Abhängigkeiten (Host-Projekt; multiplattform-spezifische Konfiguration empfohlen):

```xml
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>21.0.6</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>21.0.6</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-swing</artifactId>
        <version>21.0.6</version>
    </dependency>
</dependencies>
```


## Integration und Start (Host-Projekt)

1. CropCircle als Dependency hinzufügen (siehe Beispiele oben).
2. Sicherstellen, dass die JavaFX-Module auf dem Modulpfad oder Klassenpfad vorhanden sind.
3. Vom Host-Projekt aus die CropCircle-UI starten, z. B. durch Aufruf des Launchers oder Instanziierung der Klasse:

```java
// Beispiel aus dem Host-Projekt (JavaFX Application bzw. vorhandener Stage)
Stage stage = new Stage();
new de.khudhurayaz.cropCircle.CropCircle(stage);
```
---

## Hinweise zur Modularität

- Wenn das Host-Projekt modulare JVM (module-info.java) nutzt, müssen die Module entsprechend exportiert/erlaubt werden.
- In modularen Setups muss die Main-Class im Host-Projekt oder in CropCircle richtig als Hauptklasse konfiguriert werden (z. B. `module/name/de.khudhurayaz.cropCircle.Launcher` in plugin Konfigurationen).

---

## Empfohlene Projektstruktur (Host)

```
host-project/
├─ pom.xml oder build.gradle
└─ src/
   └─ main/
      └─ java/
         └─ host/...
```

CropCircle wird über Abhängigkeiten eingebunden und seine UI-Komponenten in den Host-Code integriert.

---

## .jar Datei
Die .jar Datei im release oder [hier](https://github.com/khudhurayaz/CropCircleTool/releases/download/stabil/CropCircle-1.0.0.jar) herunterladen und im Projekt einbinden!

---

## Troubleshooting

- Fehlende Icons oder CSS -> prüfen, ob Ressourcen im JAR im Pfad `de/khudhurayaz/cropCircle/` enthalten sind.
- JavaFX Module nicht gefunden -> JavaFX Abhängigkeiten in Host-Projekt hinzufügen und JVM-Args bzw. Plugin konfigurieren.
- Bei modularen Builds -> Modul-Exports und --add-mods/--add-exports entsprechend setzen.

---

## Versionen und Lizenz

- **Projektversion**: 1.0.0
- **Empfohlene Java**: 23
- **Empfohlene JavaFX**: 21.0.6

---

## Kontakt

Bei Fragen zur Einbindung oder Anpassung der UI direkt im Host-Projekt können Entwickler den Quellcode der Klassen View.java und Utils.java prüfen, um API-Punkte zur Integration zu identifizieren.
