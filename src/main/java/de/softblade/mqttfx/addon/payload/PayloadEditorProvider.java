package de.softblade.mqttfx.addon.payload;

import de.softblade.mqttfx.addon.payload.utils.FxmlView;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Optional;

public interface PayloadEditorProvider {
  Map<String, FxmlView> getEditors();
  Map<String, Stage> getDialogs();
  Optional<Map<String, String>> showEditor(String command);
}
