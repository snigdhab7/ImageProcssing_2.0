package model.imagetransformation;

import java.util.Map;

import model.image.Pixel;

/**
 * Represents an image transformation that retains the maximum value of the RGB components of
 * each pixel.
 */
public class ValueComponent extends AbstractImageTransformation {
  /**
   * Applies the transformation to the provided image pixel matrix.
   *
   * @param inputFileName       The input image file name.
   * @param outputFileName      The output image file name.
   * @param imagesPixelMatrices A map containing pixel matrices of images.
   * @param otherParams         Additional parameters, if required.
   * @return The transformed pixel matrix with maximum RGB component values retained.
   */
  @Override
  public Pixel[][] generate(String inputFileName, String outputFileName,
                            Map<String, Pixel[][]> imagesPixelMatrices, Object... otherParams) {
    Pixel[][] pixelMatrix = imagesPixelMatrices.get(inputFileName);
    int pixelMatrixRows = pixelMatrix.length;
    int pixelMatrixCols = pixelMatrix[0].length;
    Pixel[][] outputPixelMatrix = new Pixel[pixelMatrixRows][pixelMatrixCols];
    Pixel pixel;
    for (int i = 0; i < pixelMatrixRows; i++) {
      for (int j = 0; j < pixelMatrixCols; j++) {
        // process pixel
        pixel = pixelMatrix[i][j];
        int maxVal = Math.max(Math.max(pixel.getRed(), pixel.getGreen()), pixel.getBlue());
        outputPixelMatrix[i][j] = new Pixel(maxVal, maxVal, maxVal, pixel.getAlpha());
      }
    }
    return outputPixelMatrix;
  }
}
