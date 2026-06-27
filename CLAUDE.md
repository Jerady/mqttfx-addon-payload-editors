# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

`de.softblade:mqttfx-addon-payload-editors` is the **base SDK library** for building custom Payload Editor addons for [MQTT.fx](https://www.softblade.de). It ships only interfaces and small JavaFX helpers — concrete editors live in separate addon projects that depend on this artifact. There is no runnable application here; the build produces a published Maven library (main jar + sources + javadoc).

## Build & publish

```bash
./gradlew build              # compile, test, assemble jars
./gradlew test               # run tests (JUnit 5 / Jupiter, useJUnitPlatform)
./gradlew test --tests '*ClassName.methodName'   # run a single test
./gradlew javadoc            # generate javadoc
./gradlew publishToMavenLocal    # install into local ~/.m2 for dependent addon projects
./gradlew publish            # publish (signed) to the configured Maven repo
```

Notes:
- Gradle wrapper is pinned to **7.3.3**.
- `publish` requires `signing` to be configured (GPG keys / signing properties); local development typically uses `publishToMavenLocal`.
- Coordinates and version are defined in `deploy.gradle` (`artifactGroupId`, `artifactVersion`), applied by `build.gradle`.

## Architecture

The library defines the contract an addon must fulfill to plug into the MQTT.fx host. Three interfaces in `de.softblade.mqttfx.addon.payload` are the integration surface:

- **`PayloadEditor`** — marker/metadata interface every editor implements. Exposes identity and presentation (`getId`, `getName`, `getVersion`, `getDescription`, `getPosition` for dropdown ordering), the target MQTT topic (`getTargetTopicName`), and its i18n `getResourceBundle`.
- **`PayloadEditorProvider`** — the entry point the host calls. Owns the editor registry (`getEditors()` → `Map<String, FxmlView>`), the live dialog stages (`getDialogs()` → `Map<String, Stage>`), and drives the UI via `showEditor(command)`, which returns the editor's result as `Optional<Map<String,String>>`.
- **`PayloadEditorResult`** — wraps an editor's output as a `Map<String,String>` (`getResultsMap()`), the same shape `showEditor` returns.

JavaFX UI helpers in `de.softblade.mqttfx.addon.payload.utils`:

- **`FxmlView`** — abstract base for a screen. Subclasses are loaded by convention: `getFXMLName()` derives the FXML path from the class's simple name lowercased → `/fxml/<classname>.fxml`. Construct with a `ResourceBundle` to auto-load on creation, or use the no-arg constructor and call `init()` later. Exposes `getView()` (the `Parent` root) and `getController()`.
- **`FxmlLoader`** — thin wrapper around JavaFX `FXMLLoader` used by `FxmlView`.

Because editors are discovered by the host through these interfaces, **changing an interface signature is a breaking change** for every dependent addon — treat `payload/*.java` as a public API.

## Conventions

- `build.gradle` adds `src/main/java` and `src/test/java` as resource roots (`include "**/*.*"`), so FXML files and resource bundles are expected to sit **alongside the Java sources** and are bundled into the jar. The `/fxml/<classname>.fxml` lookup in `FxmlView` depends on this.
- JavaFX is used but not declared as a dependency in `build.gradle` — it is provided by the MQTT.fx host runtime at execution time.
