package model.imagetransformation;

import java.util.Map;

import model.image.Pixel;

/**
 * Transformation class to convert an image to greyscale using specific RGB-weighted averages.
 */
public class Greyscale extends AbstractImageTransformation {

  /**
   * Converts the input image to greyscale by applying a greyscale filter matrix.
   *
   * @param inputFileName       The name of the input image file.
   * @param outputFileName      The name of the output image file after converting to greyscale.
   * @param imagesPixelMatrices A map containing pixel matrices of different images.
   * @param otherParams         Additional parameters or configurations for the transformation.
   * @return A two-dimensional array of pixels representing the greyscale image.
   */
  @Override
  public Pixel[][] generate(String inputFileName, String outputFileName,
                            Map<String, Pixel[][]> imagesPixelMatrices, Object... otherParams) {
    double[][] greyscaleFilter = {
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722}
    };
    Pixel[][] pixelMatrix = imagesPixelMatrices.get(inputFileName);
    return TransformationHelper.applyTransformation(pixelMatrix, greyscaleFilter,
            TransformationType.TRANSFORM, (double) otherParams[0]);
  }
}
