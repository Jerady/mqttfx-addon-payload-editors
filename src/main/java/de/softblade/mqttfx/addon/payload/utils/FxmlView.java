package de.softblade.mqttfx.addon.payload.utils;

import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class FxmlView {

  private FxmlLoader loader;
  private ResourceBundle resourceBundle;

  public FxmlView(){

  }

  public FxmlView(ResourceBundle resourceBundle){
    this.resourceBundle = resourceBundle;
    init();
  }

  public void init() {
    loader = new FxmlLoader();
    String fxmlName = getFXMLName();
    final URL resource = getClass().getResource(fxmlName);
    if (resource == null) {
      throw new IllegalStateException("Cannot load " + getFXMLName() + ": check the FXML file name/location!");
    }
    try {
      if (resourceBundle != null) {
        loader.load(resource, resourceBundle);
      }
      else{
        loader.load(resource);
      }
    } catch (IOException ex) {
      throw new IllegalStateException("Cannot load " + getFXMLName(), ex);
    }
  }

  public Object getController() {
    return loader.getRootController();
  }

  public Parent getView() {
    return loader.getRoot();
  }

  public String getFXMLName() {
    String clazz = getClass().getSimpleName().toLowerCase();
    return "/fxml/".concat(clazz).concat(".fxml");
  }

}
