package model.imagetransformation;

import java.util.Map;

import model.image.Pixel;

/**
 * A class that combines three different image channels (RGB) into a single image.
 */
public class RgbCombine extends AbstractImageTransformation {
  /**
   * Combines individual Red, Green, and Blue channel images to produce a single image.
   *
   * @param inputFileName       The input file name.
   * @param outputFileName      The output file name.
   * @param imagesPixelMatrices A map containing images as pixel matrices.
   * @param otherParams         Other parameters - three separate Pixel matrices for
   *                            R, G, B channels.
   * @return The combined image as a 2D array of pixels.
   */
  @Override
  public Pixel[][] generate(String inputFileName, String outputFileName,
                            Map<String, Pixel[][]> imagesPixelMatrices, Object... otherParams) {
    Pixel[][] pixelMatrix = imagesPixelMatrices.get(inputFileName);
    int pixelMatrixRows = pixelMatrix.length;
    int pixelMatrixCols = pixelMatrix[0].length;
    Pixel[][] outputPixelMatrix = new Pixel[pixelMatrixRows][pixelMatrixCols];
    Pixel pixel;
    Pixel[][] pixelMatrixR = (Pixel[][]) otherParams[0];
    Pixel[][] pixelMatrixG = (Pixel[][]) otherParams[1];
    Pixel[][] pixelMatrixB = (Pixel[][]) otherParams[2];
    for (int i = 0; i < pixelMatrixRows; i++) {
      for (int j = 0; j < pixelMatrixCols; j++) {
        // process pixel
        outputPixelMatrix[i][j] = new Pixel(pixelMatrixR[i][j].getRed(),
                pixelMatrixG[i][j].getGreen(),
                pixelMatrixB[i][j].getBlue(),
                pixelMatrixR[i][j].getAlpha());
      }
    }
    return outputPixelMatrix;
  }
}
