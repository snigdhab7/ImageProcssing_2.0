package controller;

import model.image.ImageModelInterface;
import view.ViewInterface;

/**
 * The ControllerInterface defines the contract for a controller in the image processing
 * application. It provides methods for starting the controller with an image model and setting
 * the associated view.
 */
public interface ControllerInterface {

  /**
   * Starts the controller with the provided image model.
   *
   * @param imageModel The image model to be associated with the controller.
   */
  public void start(ImageModelInterface imageModel);

  /**
   * Sets the view associated with the controller.
   *
   * @param view The view to be associated with the controller.
   */
  public void setView(ViewInterface view);
}
