package model.image;

import java.util.HashMap;
import java.util.Map;

import model.imagetransformation.Methods;

/**
 * This interface defines method to perform various transformations an image represented
 * by a Pixel matrix using processImage method.
 */
public interface ImageModelInterface {
  /**
   * Holds the pixel matrices of various images for processing and manipulation.
   */
  public Map<String, Pixel[][]> IMAGES_PIXEL_MATRICES = new HashMap<>();

  /**
   * Processes the image based on the specified method and additional parameters.
   *
   * @param inputFileName  The input image file name.
   * @param outputFileName The output image file name.
   * @param method         The method to be applied for image processing (from the 'Methods' enum).
   * @param otherParams    Additional parameters required for the specified method, if any.
   */
  public void processImage(String inputFileName, String outputFileName, Methods method,
                           Object... otherParams);
}
