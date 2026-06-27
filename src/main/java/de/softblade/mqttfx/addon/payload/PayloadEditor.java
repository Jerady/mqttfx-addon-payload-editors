package de.softblade.mqttfx.addon.payload;


/**
 * Marker interface for all payload editor implementations.
 * Author:
 * Jens Deters
 */
public interface PayloadEditor {

  /**
   * @return The ui resource bundle.
   */
  String getResourceBundle();

  /**
   *
   * @return the target topic name for the payload to puplish
   */
  String getTargetTopicName();

  /**
   *
   * @return The editor version.
   */
  String getVersion();


  /**
   * @return The logical id.
   */
  String getId();


  /**
   *
   * @return The editor description.
   */
  String getDescription();

  /**
   *
   * @return The presentation name of the editor (e.g. in Drop-Downs)
   */
  String getName();

  /**
   *
   * @return The desired position/index of the editor in the dropdown menu.
   */
  int getPosition();

}
