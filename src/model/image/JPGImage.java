package model.image;

import controller.ImageIOHelper;

/**
 * Represents an image in JPG format and provides methods to read and write JPG images.
 */
public class JPGImage implements ImageUtilInterface {

  /**
   * Reads a JPG image from the specified file.
   *
   * @param fileName The name of the file containing the JPG image.
   * @return A Pixel matrix representing the read JPG image.
   */
  public Pixel[][] readImage(String fileName) {
    return ImageIOHelper.readJPGPNG(fileName);
  }

  /**
   * Writes the provided Pixel matrix to a JPG image file.
   *
   * @param outputImgName The name of the output JPG image file to be created.
   * @param pixelMatrix   The Pixel matrix representing the image to be written to the file.
   */
  public void writeImage(String outputImgName, Pixel[][] pixelMatrix) {
    ImageIOHelper.writeJPGPNG(outputImgName, pixelMatrix);
  }
}
