package model.imagetransformation;

import java.util.Map;

import model.image.Pixel;

/**
 * Transformation class that flips an image horizontally.
 */
public class HorizontalFlip extends AbstractImageTransformation {

  /**
   * Flips the input image horizontally.
   *
   * @param inputFileName       The name of the input image file.
   * @param outputFileName      The name of the output image file after horizontal flipping.
   * @param imagesPixelMatrices A map containing pixel matrices of different images.
   * @param otherParams         Additional parameters or configurations for the transformation.
   * @return A two-dimensional array of pixels representing the horizontally flipped image.
   */
  @Override
  public Pixel[][] generate(String inputFileName, String outputFileName,
                            Map<String, Pixel[][]> imagesPixelMatrices,
                            Object... otherParams) {

    Pixel[][] pixelMatrix = imagesPixelMatrices.get(inputFileName);
    int pixelMatrixRows = pixelMatrix.length;
    int pixelMatrixCols = pixelMatrix[0].length;
    Pixel[][] outputPixelMatrix = new Pixel[pixelMatrixRows][pixelMatrixCols];
    for (int i = 0; i < pixelMatrixRows; i++) {
      for (int j = 0; j < pixelMatrixCols; j++) {
        outputPixelMatrix[i][pixelMatrixCols - 1 - j] = pixelMatrix[i][j];
      }
    }
    return outputPixelMatrix;
  }
}
