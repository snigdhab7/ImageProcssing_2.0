package model.image;

import controller.ImageIOHelper;

/**
 * Represents a PPM image implementing ImageUtilInterface for reading and writing PPM image files.
 */
public class PPMImage implements ImageUtilInterface {


  /**
   * Reads a PPM image file and returns the pixel matrix.
   *
   * @param filename The name of the PPM image file to be read.
   * @return A two-dimensional array of pixels representing the PPM image.
   */
  public Pixel[][] readImage(String filename) {
    return ImageIOHelper.readPPM(filename);
  }

  /**
   * Writes a PPM image file using the given pixel matrix.
   *
   * @param fileName    The name of the output PPM image file to be written.
   * @param pixelMatrix The pixel matrix representing the PPM image to be written.
   */
  public void writeImage(String fileName, Pixel[][] pixelMatrix) {
    ImageIOHelper.writePPM(fileName, pixelMatrix);
  }
}
