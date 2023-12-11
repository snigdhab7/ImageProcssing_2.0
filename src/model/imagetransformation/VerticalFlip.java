package model.imagetransformation;

import java.util.Map;

import model.image.Pixel;

/**
 * Represents an image transformation that flips the image vertically.
 */
public class VerticalFlip extends AbstractImageTransformation {
  /**
   * Applies the vertical flip transformation to the provided image pixel matrix.
   *
   * @param inputFileName       The input image file name.
   * @param outputFileName      The output image file name.
   * @param imagesPixelMatrices A map containing pixel matrices of images.
   * @param otherParams         Additional parameters, if required.
   * @return The transformed pixel matrix flipped vertically.
   */
  @Override
  public Pixel[][] generate(String inputFileName, String outputFileName, Map<String,
          Pixel[][]> imagesPixelMatrices, Object... otherParams) {

    Pixel[][] pixelMatrix = imagesPixelMatrices.get(inputFileName);
    int pixelMatrixRows = pixelMatrix.length;
    int pixelMatrixCols = pixelMatrix[0].length;
    Pixel[][] outputPixelMatrix = new Pixel[pixelMatrixRows][pixelMatrixCols];
    for (int i = 0; i < pixelMatrixRows; i++) {
      System.arraycopy(pixelMatrix[i], 0, outputPixelMatrix[pixelMatrixRows - 1 - i],
              0, pixelMatrixCols);
    }
    return outputPixelMatrix;
  }
}
