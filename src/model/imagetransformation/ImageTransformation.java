package model.imagetransformation;

import java.util.Map;

import model.image.Pixel;

/**
 * An interface representing an image transformation operation.
 */
public interface ImageTransformation {
  /**
   * Applies a specific transformation on an image and returns the modified pixel matrix.
   *
   * @param inputFileName       The input file name of the image.
   * @param outputFileName      The output file name of the transformed image.
   * @param imagesPixelMatrices A map containing pixel matrices of different images.
   * @param otherParams         Additional parameters for the transformation (if required).
   * @return A two-dimensional array of pixels representing the transformed image.
   */
  Pixel[][] generate(String inputFileName, String outputFileName,
                     Map<String, Pixel[][]> imagesPixelMatrices, Object... otherParams);
}
