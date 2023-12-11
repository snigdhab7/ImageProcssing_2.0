package model.imagetransformation;

import java.util.Map;

import model.image.Pixel;

/**
 * A class that sharpens an image using a predefined kernel matrix.
 */
public class Sharpen extends AbstractImageTransformation {
  /**
   * Applies a sharpening effect to the image using a predefined kernel matrix.
   *
   * @param inputFileName       The input file name.
   * @param outputFileName      The output file name.
   * @param imagesPixelMatrices A map containing images as pixel matrices.
   * @param otherParams         Other optional parameters.
   * @return The sharpened image as a 2D array of pixels.
   */
  @Override
  public Pixel[][] generate(String inputFileName, String outputFileName,
                            Map<String, Pixel[][]> imagesPixelMatrices, Object... otherParams) {
    double[][] sharpenKernelMatrix = {
            {-0.125, -0.125, -0.125, -0.125, -0.125},
            {-0.125, 0.25, 0.25, 0.25, -0.125},
            {-0.125, 0.25, 1, 0.25, -0.125},
            {-0.125, 0.25, 0.25, 0.25, -0.125},
            {-0.125, -0.125, -0.125, -0.125, -0.125}
    };
    Pixel[][] pixelMatrix = imagesPixelMatrices.get(inputFileName);
    return TransformationHelper.applyTransformation(pixelMatrix, sharpenKernelMatrix,
            TransformationType.FILTER, (double) otherParams[0]);
  }
}
