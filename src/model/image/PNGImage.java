package model.image;

import controller.ImageIOHelper;

/**
 * Represents a PNG image implementing ImageUtilInterface for reading and writing PNG image files.
 */
public class PNGImage implements ImageUtilInterface {

  /**
   * Reads a PNG image file and returns the pixel matrix.
   *
   * @param fileName The name of the PNG image file to be read.
   * @return A two-dimensional array of pixels representing the PNG image.
   */
  public Pixel[][] readImage(String fileName) {
    return ImageIOHelper.readJPGPNG(fileName);
  }

  /**
   * Writes a PNG image file using the given pixel matrix.
   *
   * @param outputImgName The name of the output PNG image file to be written.
   * @param pixelMatrix   The pixel matrix representing the PNG image to be written.
   */
  public void writeImage(String outputImgName, Pixel[][] pixelMatrix) {
    ImageIOHelper.writeJPGPNG(outputImgName, pixelMatrix);
  }
}
