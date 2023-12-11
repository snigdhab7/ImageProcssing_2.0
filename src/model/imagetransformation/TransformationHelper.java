package model.imagetransformation;

import model.image.Pixel;

/**
 * A helper class containing methods to apply transformations on image pixel matrices.
 */
public class TransformationHelper {
  /**
   * Applies the specified transformation or filtering effect to the pixel matrix.
   *
   * @param pixelMatrix  The input pixel matrix representing an image.
   * @param filterKernel The kernel matrix to be applied for the transformation or filtering.
   * @param type         The type of transformation: TRANSFORM or FILTER.
   * @return The transformed or filtered image as a 2D array of pixels.
   */
  public static Pixel[][] applyTransformation(Pixel[][] pixelMatrix, double[][] filterKernel,
                                              TransformationType type, double splitPercentage) {
    int pixelMatrixRows = pixelMatrix.length;
    int pixelMatrixCols = pixelMatrix[0].length;
    double splitViewWidth = (splitPercentage > 0) ?
            pixelMatrixCols * splitPercentage : pixelMatrixCols;
    Pixel[][] outputPixelMatrix = new Pixel[pixelMatrixRows][pixelMatrixCols];
    Pixel pixel;
    for (int i = 0; i < pixelMatrixRows; i++) {
      for (int j = 0; j < pixelMatrixCols; j++) {
        // process pixel
        pixel = pixelMatrix[i][j];
        if (j < splitViewWidth) {
          if (type == TransformationType.TRANSFORM) {
            outputPixelMatrix[i][j] = Pixel.processPixel(filterKernel, pixel);
          } else {
            outputPixelMatrix[i][j] = Pixel.processPixel(filterKernel, pixelMatrix, i, j);
          }
        } else {
          outputPixelMatrix[i][j] = new Pixel(pixel.getRed(), pixel.getGreen(),
                  pixel.getBlue(), pixel.getAlpha());
        }
      }
    }
    return outputPixelMatrix;
  }
}
