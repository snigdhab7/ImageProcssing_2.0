package view;

import controller.Features;

/**
 * The ViewInterface defines the contract for a view in the image processing application.
 * Views implementing this interface are expected to interact with the controller through
 * the provided methods.
 */
public interface ViewInterface {

  /**
   * Adds the specified Features instance to the view, allowing interaction with the
   * image processing features provided by the controller.
   *
   * @param features The Features instance to be added to the view.
   */
  void addFeatures(Features features);
}
