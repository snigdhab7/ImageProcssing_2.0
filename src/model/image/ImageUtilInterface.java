package model.image;

/**
 * This interface defines methods to read and write an image represented by a Pixel matrix.
 */
public interface ImageUtilInterface {
  /**
   * Reads an image from the given file and returns the pixel matrix.
   *
   * @param fileName The name of the file containing the image.
   * @return A Pixel matrix representing the image read from the file.
   */
  Pixel[][] readImage(String fileName);

  /**
   * Writes the provided Pixel matrix to an image file with the given output name.
   *
   * @param outputImgName The name of the output image file to be created.
   * @param pixelMatrix   The Pixel matrix representing the image to be written to the file.
   */
  void writeImage(String outputImgName, Pixel[][] pixelMatrix);

}
