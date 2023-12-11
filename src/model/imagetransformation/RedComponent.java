package model.imagetransformation;

import java.util.Map;

import model.image.Pixel;

/**
 * Transformation class to extract the red component of an image.
 */
public class RedComponent extends AbstractImageTransformation {

  /**
   * Applies the red component transformation to the input image.
   *
   * @param inputFileName       The name of the input image file.
   * @param outputFileName      The name of the output image file after the red
   *                            component transformation.
   * @param imagesPixelMatrices A map containing pixel matrices of different images.
   * @param otherParams         Additional parameters for the transformation (if required).
   * @return A two-dimensional array of pixels representing the red component of the image.
   */
  @Override
  public Pixel[][] generate(String inputFileName, String outputFileName,
                            Map<String, Pixel[][]> imagesPixelMatrices, Object... otherParams) {
    Pixel[][] pixelMatrix = imagesPixelMatrices.get(inputFileName);
    if (pixelMatrix == null) {
      System.err.println("Image " + inputFileName + " is not loaded correctly. "
              + "Please load the image using load command.");
      return null;
    }
    int pixelMatrixRows = pixelMatrix.length;
    int pixelMatrixCols = pixelMatrix[0].length;
    Pixel[][] outputPixelMatrix = new Pixel[pixelMatrixRows][pixelMatrixCols];
    Pixel pixel;
    for (int i = 0; i < pixelMatrixRows; i++) {
      for (int j = 0; j < pixelMatrixCols; j++) {
        // process pixel
        pixel = pixelMatrix[i][j];
        outputPixelMatrix[i][j] = new Pixel(pixel.getRed(), 0, 0, pixel.getAlpha());
      }
    }
    return outputPixelMatrix;
  }
}
