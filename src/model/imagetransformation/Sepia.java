package model.imagetransformation;

import java.util.Map;

import model.image.Pixel;

/**
 * A class that transforms an image into a sepia-tone version.
 */
public class Sepia extends AbstractImageTransformation {
  /**
   * Transforms the given image into a sepia-tone version.
   *
   * @param inputFileName       The input file name.
   * @param outputFileName      The output file name.
   * @param imagesPixelMatrices A map containing images as pixel matrices.
   * @param otherParams         Other optional parameters.
   * @return The transformed image as a 2D array of pixels.
   */
  @Override
  public Pixel[][] generate(String inputFileName, String outputFileName,
                            Map<String, Pixel[][]> imagesPixelMatrices, Object... otherParams) {
    double[][] sepiaFilter = {
            {0.393, 0.769, 0.189},
            {0.349, 0.686, 0.168},
            {0.272, 0.534, 0.131}
    };
    Pixel[][] pixelMatrix = imagesPixelMatrices.get(inputFileName);
    return TransformationHelper.applyTransformation(pixelMatrix, sepiaFilter,
            TransformationType.TRANSFORM, (double) otherParams[0]);
  }
}
