package model.imagetransformation;

import java.util.Map;

import model.image.Pixel;

import static model.image.Pixel.clamp;

/**
 * Transformation class to brighten an image by increasing the RGB values of each pixel.
 */
public class Brighten extends AbstractImageTransformation {
  /**
   * Applies a brightening effect to the input image by changing RGB values.
   *
   * @param inputFileName       The name of the input image file.
   * @param outputFileName      The name of the output image file after applying the
   *                            brightening effect.
   * @param imagesPixelMatrices A map containing pixel matrices of different images.
   * @param otherParams         Additional parameters for the transformation
   *                            (brightness change constant).
   * @return A two-dimensional array of pixels representing the brightened image.
   */
  @Override
  public Pixel[][] generate(String inputFileName, String outputFileName,
                            Map<String, Pixel[][]> imagesPixelMatrices, Object... otherParams) {
    Pixel[][] pixelMatrix = imagesPixelMatrices.get(inputFileName);
    int pixelMatrixRows = pixelMatrix.length;
    int pixelMatrixCols = pixelMatrix[0].length;
    Pixel[][] outputPixelMatrix = new Pixel[pixelMatrixRows][pixelMatrixCols];
    Pixel pixel;
    int brighnessChangeConstant = (int) otherParams[0];
    for (int i = 0; i < pixelMatrixRows; i++) {
      for (int j = 0; j < pixelMatrixCols; j++) {
        // process pixel
        pixel = pixelMatrix[i][j];
        outputPixelMatrix[i][j] = new Pixel(
                clamp(pixel.getRed() + brighnessChangeConstant),
                clamp(pixel.getGreen() + brighnessChangeConstant),
                clamp(pixel.getBlue() + brighnessChangeConstant),
                pixel.getAlpha());
      }
    }

    return outputPixelMatrix;
  }
}
