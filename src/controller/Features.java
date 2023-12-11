package controller;

import java.awt.image.BufferedImage;

import model.image.Pixel;

/**
 * The Features interface defines a set of features for image processing in the application.
 * It provides methods to retrieve pixel matrices, load and save images, process images, and create
 * BufferedImage from pixel matrices.
 */
public interface Features {

  /**
   * Retrieves the pixel matrix for the specified image.
   *
   * @param imageName The name of the image.
   * @return The pixel matrix representing the image.
   */
  Pixel[][] getPixelMatrixForImage(String imageName);

  /**
   * Loads an image based on the provided command parameters.
   *
   * @param commandParams The command parameters specifying the action and file details.
   */
  void load(String[] commandParams);

  /**
   * Saves an image based on the provided command parameters.
   *
   * @param commandParams The command parameters specifying the action and file details.
   */
  void save(String[] commandParams);

  /**
   * Processes an image using the specified method and parameters.
   *
   * @param inputFileName  The name of the input image file.
   * @param outputFileName The name of the output image file.
   * @param method         The processing method to be applied.
   * @param otherParams    Additional parameters required for the specified method.
   */
  void processImage(String inputFileName, String outputFileName, String method,
                    Object... otherParams);

  /**
   * Creates a BufferedImage from the provided pixel matrix.
   *
   * @param pixelMatrix The pixel matrix representing the image.
   * @return The BufferedImage created from the pixel matrix.
   */
  BufferedImage createImageFromPixels(Pixel[][] pixelMatrix);

}
