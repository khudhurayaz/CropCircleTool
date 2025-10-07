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

- **Java**: 22
- **JavaFX**: 21.0.6
- **Build-Tool**: Maven oder Gradle (je nach Host-Projekt)

---

## Einbinden

Entwickler, die CropCircle in ihr Projekt einbinden, müssen sowohl CropCircle als Dependency als auch die JavaFX-Module (inkl. javafx-swing) in ihrem Host-Projekt verfügbar machen. Das bedeutet: die JavaFX-Abhängigkeiten müssen im Host-Projekt (Maven oder Gradle) deklariert sein.

### Beispiel: Maven

1. CropCircle als Dependency:

```xml
<dependency>
    <groupId>de.khudhurayaz</groupId>
    <artifactId>CropCircle</artifactId>
    <version>1.0.0</version>
</dependency>
```

2. JavaFX-Abhängigkeiten:

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
```java
// Beispiel aus dem Host-Projekt (JavaFX Application bzw. vorhandener Stage)
Stage stage = new Stage();
new de.khudhurayaz.cropCircle.CropCircle(stage).createApp();
```
---

## Troubleshooting

- Fehlende Icons oder CSS -> prüfen, ob Ressourcen im JAR im Pfad `de/khudhurayaz/cropCircle/` enthalten sind.
- JavaFX Module nicht gefunden -> JavaFX Abhängigkeiten in Host-Projekt hinzufügen und JVM-Args bzw. Plugin konfigurieren.
- Bei modularen Builds -> Modul-Exports und --add-mods/--add-exports entsprechend setzen.
