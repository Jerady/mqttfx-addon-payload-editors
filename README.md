# MQTT.fx Payload Editors Addon Base

The base library for building **custom Payload Editor addons** for [MQTT.fx](https://www.softblade.de).

This artifact ships only the contract — a small set of interfaces plus a couple of JavaFX
helpers — that lets your addon plug into the MQTT.fx host. You implement these interfaces in
your own project; MQTT.fx discovers your editor at runtime, shows it in the publish view, and
hands the user's input back as a payload.

- **Group:** `de.softblade`
- **Artifact:** `mqttfx-addon-payload-editors`
- **Version:** `1.0.0`
- **License:** Apache License 2.0

---

## Requirements

- **Java 8+** (Java 9+ is supported; javadoc is generated in HTML5 mode there)
- **JavaFX** — provided by the MQTT.fx host at runtime, so you do *not* bundle it yourself
- **Gradle** (a `7.3.3` wrapper is included) or Maven for your addon project

## Installation

Add the dependency to your addon project.

**Gradle**

```groovy
repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation 'de.softblade:mqttfx-addon-payload-editors:1.0.0'
}
```

**Maven**

```xml
<dependency>
    <groupId>de.softblade</groupId>
    <artifactId>mqttfx-addon-payload-editors</artifactId>
    <version>1.0.0</version>
</dependency>
```

> Not on a public repository yet? Build this library locally and install it into your local
> Maven cache, then depend on it as above:
>
> ```bash
> ./gradlew publishToMavenLocal
> ```

## Concepts

An addon provides one or more **editors**. Each editor is a JavaFX screen that lets the user
compose a message payload; when the user confirms, the editor returns a result map that MQTT.fx
turns into the outgoing payload.

The integration surface is three interfaces in `de.softblade.mqttfx.addon.payload`:

| Type                     | Role                                                                                          |
|--------------------------|-----------------------------------------------------------------------------------------------|
| `PayloadEditor`          | Metadata about a single editor: id, name, version, description, target topic, dropdown position, resource bundle. |
| `PayloadEditorProvider`  | The entry point MQTT.fx calls. Registers the available editors, owns their dialog windows, and shows an editor on demand. |
| `PayloadEditorResult`    | Wraps an editor's output as a `Map<String, String>`.                                          |

UI is built with FXML using two helpers in `de.softblade.mqttfx.addon.payload.utils`:

| Type         | Role                                                                                  |
|--------------|---------------------------------------------------------------------------------------|
| `FxmlView`   | Base class for a screen. Loads its FXML by convention and exposes the view + controller. |
| `FxmlLoader` | Thin wrapper around the JavaFX `FXMLLoader`.                                           |

## Getting started

### 1. Create an editor view

Subclass `FxmlView`. The FXML file is located **by convention** from the class's simple name,
lowercased: a class `MyEditor` loads `/fxml/myeditor.fxml` from the classpath.

```java
package com.example.myaddon;

import de.softblade.mqttfx.addon.payload.utils.FxmlView;
import java.util.ResourceBundle;

public class MyEditor extends FxmlView {

    // Passing a ResourceBundle loads the FXML immediately.
    public MyEditor(ResourceBundle bundle) {
        super(bundle);
    }
}
```

Place the FXML next to your sources so it ends up on the classpath at `/fxml/myeditor.fxml`,
and point it at your controller:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.layout.VBox?>
<?import javafx.scene.control.TextArea?>

<VBox xmlns:fx="http://javafx.com/fxml"
      fx:controller="com.example.myaddon.MyEditorController">
    <TextArea fx:id="payloadArea"/>
</VBox>
```

You can now get the root node and controller:

```java
MyEditor editor = new MyEditor(bundle);
Parent view = editor.getView();
MyEditorController controller = (MyEditorController) editor.getController();
```

> **No-arg variant:** `FxmlView` also has a no-arg constructor. If you use it, call `init()`
> yourself before `getView()` / `getController()` to trigger FXML loading.

### 2. Describe the editor with `PayloadEditor`

```java
import de.softblade.mqttfx.addon.payload.PayloadEditor;

public class MyPayloadEditor implements PayloadEditor {

    @Override public String getId()              { return "com.example.myaddon.MyEditor"; }
    @Override public String getName()            { return "My Editor"; }      // shown in dropdowns
    @Override public String getDescription()     { return "Builds a custom payload."; }
    @Override public String getVersion()         { return "1.0.0"; }
    @Override public int    getPosition()        { return 10; }               // order in the menu
    @Override public String getTargetTopicName() { return "my/target/topic"; }
    @Override public String getResourceBundle()  { return "com.example.myaddon.messages"; }
}
```

### 3. Wire it up with `PayloadEditorProvider`

The provider is MQTT.fx's entry point. It exposes the editors it offers, the dialog windows it
manages, and a `showEditor(command)` call that opens the right editor and returns the user's
result.

```java
import de.softblade.mqttfx.addon.payload.PayloadEditorProvider;
import de.softblade.mqttfx.addon.payload.utils.FxmlView;
import javafx.stage.Stage;

import java.util.*;

public class MyPayloadEditorProvider implements PayloadEditorProvider {

    private final Map<String, FxmlView> editors = new LinkedHashMap<>();
    private final Map<String, Stage>    dialogs = new HashMap<>();

    @Override public Map<String, FxmlView> getEditors() { return editors; }
    @Override public Map<String, Stage>    getDialogs() { return dialogs; }

    @Override
    public Optional<Map<String, String>> showEditor(String command) {
        // Show the matching dialog, wait for the user, and return the result map.
        // Optional.empty() means the user cancelled.
        Map<String, String> result = new HashMap<>();
        result.put("payload", "...");
        return Optional.of(result);
    }
}
```

The returned `Map<String, String>` is the same shape as `PayloadEditorResult.getResultsMap()` —
MQTT.fx reads it to build the outgoing message.

## Building this library

```bash
./gradlew build               # compile, test, assemble jars (main + sources + javadoc)
./gradlew test                # run tests (JUnit 5 / Jupiter)
./gradlew javadoc             # generate API docs
./gradlew publishToMavenLocal # install into ~/.m2 for local addon development
./gradlew publish             # publish a signed release to the configured repository
```

Coordinates and version live in `deploy.gradle`. `publish` requires GPG signing to be
configured; for everyday addon development use `publishToMavenLocal`.

## License

Licensed under the Apache License, Version 2.0. See
<http://www.apache.org/licenses/LICENSE-2.0> for details.

## Author

**Jens Deters** — [softblade.de](https://www.softblade.de) · jens.deters@softblade.de
