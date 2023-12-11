package model.imagetransformation;

import java.util.Map;

import model.image.Pixel;

/**
 * A class representing an image transformation for adjusting levels.
 * It extends the AbstractImageTransformation class.
 */
public class LevelsAdjust extends AbstractImageTransformation {

  /**
   * Applies the levels adjustment transformation to the given image matrix.
   *
   * @param inputFileName       The input file name.
   * @param outputFileName      The output file name.
   * @param imagesPixelMatrices A map containing pixel matrices for different images.
   * @param otherParams         Additional parameters for the transformation.
   *                            Expects three parameters in the order: black point (b),
   *                            midpoint (m), and white point (w).
   * @return The transformed pixel matrix.
   */
  @Override
  public Pixel[][] generate(String inputFileName, String outputFileName,
                            Map<String, Pixel[][]> imagesPixelMatrices, Object... otherParams)
          throws IllegalArgumentException {

    double splitPercentage = (double) otherParams[3];

    int b = Integer.parseInt((String) otherParams[0]);
    int m = Integer.parseInt((String) otherParams[1]);
    int w = Integer.parseInt((String) otherParams[2]);

    if (b < 0 || b > 255 || m < 0 || m > 255 || w < 0 || w > 255) {
      System.err.println("Params going out of bounds for B, M, or W");
    }
    if (!(b < m && m < w)) {
      System.err.println("Value of B, M, and W should be in increasing order!");
    }

    double localA = ((b * b) * (m - w)) - (b * ((m * m) - (w * w)))
            + ((w * (m * m)) - (m * (w * w)));
    double localAa = ((-b) * (128 - 255)) + (128 * w) - (255 * m);
    double localAb = ((b * b) * (128 - 255)) + (255 * m * m) - (128 * w * w);
    double localAc = ((b * b) * ((255 * m) - (128 * w))) - (b * ((255 * m * m) - (128 * w * w)));
    double a = (localAa / localA);
    double b2 = (localAb / localA);
    double c = (localAc / localA);

    Pixel[][] pixelMatrix = imagesPixelMatrices.get(inputFileName);
    int pixelMatrixRows = pixelMatrix.length;
    int pixelMatrixColumns = pixelMatrix[0].length;
    Pixel[][] outputPixelMatrix = new Pixel[pixelMatrixRows][pixelMatrixColumns];
    Pixel pixel;
    double splitViewWidth = (splitPercentage > 0) ? pixelMatrixColumns
            * splitPercentage : pixelMatrixColumns;
    for (int i = 0; i < pixelMatrixRows; i++) {
      for (int j = 0; j < pixelMatrixColumns; j++) {
        pixel = pixelMatrix[i][j];
        if (j < splitViewWidth) {
          outputPixelMatrix[i][j] = new Pixel(
                  Pixel.clamp(levelsAdjust(a, b2, c, pixel.getRed())),
                  Pixel.clamp(levelsAdjust(a, b2, c, pixel.getGreen())),
                  Pixel.clamp(levelsAdjust(a, b2, c, pixel.getBlue())));
        } else {
          outputPixelMatrix[i][j] = pixel;
        }
      }
    }

    return outputPixelMatrix;
  }

  /**
   * Applies the levels adjustment formula to a single color component.
   *
   * @param a Coefficient a.
   * @param b Coefficient b.
   * @param c Coefficient c.
   * @param x Input color component value.
   * @return Adjusted color component value.
   */
  private int levelsAdjust(double a, double b, double c, int x) {
    return (int) Math.round((a * x * x) + (b * x) + c);
  }
}
