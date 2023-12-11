package model.imagetransformation;

import java.awt.image.BufferedImage;
import java.util.Map;

import model.image.Pixel;

/**
 * A class representing an image transformation for generating a histogram.
 * It extends the AbstractImageTransformation class.
 */
public class Histogram extends AbstractImageTransformation {
  /**
   * Width of the histogram image.
   */
  final int WIDTH = 256;
  /**
   * Height of the histogram image.
   */
  final int HEIGHT = 256;

  /**
   * Generates a histogram for the given image matrix.
   *
   * @param inputFileName       The input file name.
   * @param outputFileName      The output file name.
   * @param imagesPixelMatrices A map containing pixel matrices for different images.
   * @param otherParams         Additional parameters for the transformation.
   * @return The pixel matrix representing the generated histogram image.
   */
  @Override
  public Pixel[][] generate(String inputFileName, String outputFileName, Map<String,
          Pixel[][]> imagesPixelMatrices, Object... otherParams) {
    // create table for histogram with frequency of each channel value per pixel
    Map<String, int[]> histogramMap = HistogramHelper.prepareHistogramTableEntries(
            imagesPixelMatrices.get(inputFileName));

    // create histogram image out of histogram table
    HistogramDrawer histogramDrawer = new HistogramDrawer(WIDTH, HEIGHT);
    BufferedImage histogramImage = histogramDrawer.draw(histogramMap);

    // create pixel matrix for created histogram image and return it
    return HistogramHelper.preparePixelMatrixForImage(histogramImage, WIDTH, HEIGHT);
  }
}
