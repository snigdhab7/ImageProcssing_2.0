package model.imagetransformation;

import java.util.Map;

import model.image.Pixel;

/**
 * An abstract class implementing the ImageTransformation interface, providing a foundation for
 * image transformation operations.
 */
public abstract class AbstractImageTransformation implements ImageTransformation {

  /**
   * A method to be implemented by subclasses to perform image transformation.
   *
   * @param inputFileName       The input file name of the image.
   * @param outputFileName      The output file name of the transformed image.
   * @param imagesPixelMatrices A map containing pixel matrices of different images.
   * @param otherParams         Additional parameters for the transformation (if required).
   * @return A two-dimensional array of pixels representing the transformed image.
   */
  public abstract Pixel[][] generate(String inputFileName, String outputFileName,
                                     Map<String, Pixel[][]> imagesPixelMatrices,
                                     Object... otherParams);
}
