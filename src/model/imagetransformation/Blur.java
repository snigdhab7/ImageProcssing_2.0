package model.imagetransformation;

import java.util.Map;

import model.image.Pixel;

/**
 * Transformation class to apply a blur effect to an image using a predefined blur kernel matrix.
 */
public class Blur extends AbstractImageTransformation {

  /**
   * Applies a blur effect to the input image.
   *
   * @param inputFileName       The name of the input image file.
   * @param outputFileName      The name of the output image file after applying the
   *                            blur effect.
   * @param imagesPixelMatrices A map containing pixel matrices of different images.
   * @param otherParams         Additional parameters for the transformation (if required).
   * @return A two-dimensional array of pixels representing the image with the blur effect applied.
   */
  @Override
  public Pixel[][] generate(String inputFileName, String outputFileName,
                            Map<String, Pixel[][]> imagesPixelMatrices, Object... otherParams) {
    double[][] blurKernelMatrix = {
            {0.0625, 0.125, 0.0625},
            {0.125, 0.25, 0.125},
            {0.0625, 0.125, 0.0625},
    };
    Pixel[][] pixelMatrix = imagesPixelMatrices.get(inputFileName);
    return TransformationHelper.applyTransformation(pixelMatrix, blurKernelMatrix,
            TransformationType.FILTER, (double) otherParams[0]);
  }
}
