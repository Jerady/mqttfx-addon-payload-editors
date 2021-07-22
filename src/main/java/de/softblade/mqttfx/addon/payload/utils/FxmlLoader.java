/*
 * Copyright 2013 Jens Deters.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.softblade.mqttfx.addon.payload.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Jens Deters
 */
public class FxmlLoader {

  private final FXMLLoader fxmlLoader = new FXMLLoader();

  public void load(URL location) throws IOException {
    fxmlLoader.setLocation(location);
    fxmlLoader.load();
  }

  public void load(URL location, ResourceBundle resources) throws IOException {
    fxmlLoader.setLocation(location);
    fxmlLoader.setResources(resources);
    fxmlLoader.load();
  }

  public Parent getRoot() {
    return fxmlLoader.getRoot();
  }

  public Object getRootController() {
    return fxmlLoader.getController();
  }

}
