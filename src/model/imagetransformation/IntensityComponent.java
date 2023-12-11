package model.imagetransformation;

import java.util.Map;

import model.image.Pixel;

/**
 * Transformation class that extracts intensity component from an image.
 */
public class IntensityComponent extends AbstractImageTransformation {
  /**
   * Extracts the intensity component from the input image.
   *
   * @param inputFileName       The name of the input image file.
   * @param outputFileName      The name of the output image file after intensity extraction.
   * @param imagesPixelMatrices A map containing pixel matrices of different images.
   * @param otherParams         Additional parameters or configurations for the transformation.
   * @return A two-dimensional array of pixels representing the intensity component of the image.
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
        int avg = (pixel.getRed() + pixel.getGreen() + pixel.getBlue()) / 3;
        outputPixelMatrix[i][j] = new Pixel(avg, avg, avg, pixel.getAlpha());
      }
    }
    return outputPixelMatrix;
  }
}
