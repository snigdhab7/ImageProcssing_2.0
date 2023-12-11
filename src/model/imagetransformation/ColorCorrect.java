package model.imagetransformation;

import java.util.Map;

import model.image.Pixel;

/**
 * The ColorCorrect class is responsible for performing color correction on an input image,
 * adjusting the color balance to achieve a more visually appealing result.
 * This transformation calculates the peak values of each color channel in the image's histogram
 * and adjusts the color levels to achieve a more balanced distribution.
 * The resulting image is stored in the specified output file.
 */
public class ColorCorrect extends AbstractImageTransformation {
  /**
   * The width of the image in pixels.
   */
  final int WIDTH = 256;
  /**
   * The height of the image in pixels.
   */
  final int HEIGHT = 256;

  /**
   * Returns the peak value of a given color channel based on its frequency list.
   *
   * @param frequencyList An array representing the frequency of each color channel value.
   * @return The peak value of the color channel.
   */
  private int getChannelPeakValue(int[] frequencyList) {
    int maxVal = 0;
    int maxFrequency = 0;

    for (int i = 0; i < frequencyList.length; i++) {
      if (frequencyList[i] > maxFrequency) {
        maxFrequency = frequencyList[i];
        maxVal = i;
      }
    }
    return maxVal;
  }

  /**
   * Generates a color-corrected image based on the given input image matrix, adjusting the
   * color balance to achieve a more visually appealing result.
   *
   * @param inputFileName       The name of the input image file.
   * @param outputFileName      The name of the output image file.
   * @param imagesPixelMatrices A map containing the pixel matrices of images indexed by
   *                            their filenames.
   * @param otherParams         Additional parameters that may be passed.
   * @return The color-corrected pixel matrix of the output image.
   */
  @Override
  public Pixel[][] generate(String inputFileName, String outputFileName,
                            Map<String, Pixel[][]> imagesPixelMatrices, Object... otherParams) {
    double splitPercentage = 0;
    if (otherParams.length > 0) {
      splitPercentage = (double) otherParams[0];
    }
    // color-correct image-name dest-image-name

    Pixel[][] pixelMatrix = imagesPixelMatrices.get(inputFileName);
    // create table for histogram with frequency of each channel value per pixel
    Map<String, int[]> histogramMap = HistogramHelper.prepareHistogramTableEntries(pixelMatrix);
    int redPeak = getChannelPeakValue(histogramMap.get("red"));
    int greenPeak = getChannelPeakValue(histogramMap.get("green"));
    int bluePeak = getChannelPeakValue(histogramMap.get("blue"));
    int divider = 3;
    if (redPeak <= 10 || redPeak >= 245) {
      redPeak = 0;
      divider--;
    }
    if (greenPeak <= 10 || greenPeak >= 245) {
      greenPeak = 0;
      divider--;
    }
    if (bluePeak <= 10 || bluePeak >= 245) {
      bluePeak = 0;
      divider--;
    }
    divider = divider == 0 ? 1 : divider;
    int averagePeak = (redPeak + greenPeak + bluePeak) / divider;
    int redOffset = averagePeak - redPeak;
    int greenOffset = averagePeak - greenPeak;
    int blueOffset = averagePeak - bluePeak;
    Pixel pixel;
    int pixelMatrixRows = pixelMatrix.length;
    int pixelMatrixColumns = pixelMatrix[0].length;
    Pixel[][] outputPixelMatrix = new Pixel[pixelMatrixRows][pixelMatrixColumns];
    double splitViewWidth = (splitPercentage > 0) ? pixelMatrixColumns
            * splitPercentage : pixelMatrixColumns;
    for (int i = 0; i < pixelMatrixRows; i++) {
      for (int j = 0; j < pixelMatrixColumns; j++) {
        pixel = pixelMatrix[i][j];
        if (j < splitViewWidth) {
          outputPixelMatrix[i][j] = new Pixel(Pixel.clamp(pixel.getRed() + redOffset),
                  Pixel.clamp(pixel.getGreen() + greenOffset),
                  Pixel.clamp(pixel.getBlue() + blueOffset));
        } else {
          outputPixelMatrix[i][j] = pixel;
        }
      }
    }

    return outputPixelMatrix;
  }
}
