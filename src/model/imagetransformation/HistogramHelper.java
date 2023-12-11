package model.imagetransformation;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import model.image.Pixel;

/**
 * A helper class for handling histogram-related operations.
 */
public class HistogramHelper {
  /**
   * Maximum range value for histogram entries.
   */
  final static int RANGE_MAX = 256;


  /**
   * Prepares a histogram table based on the pixel matrix provided.
   *
   * @param pixelMatrix The pixel matrix representing an image.
   * @return A map containing histogram data for different color channels.
   */
  public static Map<String, int[]> prepareHistogramTableEntries(Pixel[][] pixelMatrix) {
    Map<String, int[]> histogramMap = new HashMap<>();
    int pixelMatrixRows = pixelMatrix.length;
    int pixelMatrixCols = pixelMatrix[0].length;
    histogramMap.put("red", new int[RANGE_MAX]);
    histogramMap.put("green", new int[RANGE_MAX]);
    histogramMap.put("blue", new int[RANGE_MAX]);
    for (int i = 0; i < pixelMatrixRows; i++) {
      for (int j = 0; j < pixelMatrixCols; j++) {
        Pixel pixel = pixelMatrix[i][j];
        int redVal = pixel.getRed();
        int greenVal = pixel.getGreen();
        int blueVal = pixel.getBlue();
        histogramMap.get("red")[redVal] += 1;
        histogramMap.get("green")[greenVal] += 1;
        histogramMap.get("blue")[blueVal] += 1;
      }
    }
    return histogramMap;
  }

  /**
   * Prepares a pixel matrix from the given BufferedImage.
   *
   * @param histogramImage The BufferedImage representing a histogram.
   * @param width          The width of the histogram image.
   * @param height         The height of the histogram image.
   * @return A pixel matrix representing the histogram image.
   */
  public static Pixel[][] preparePixelMatrixForImage(BufferedImage histogramImage,
                                                     int width, int height) {
    Pixel[][] outputPixelMatrix = new Pixel[width][height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int pixelVal = histogramImage.getRGB(j, i);
        int alpha = (pixelVal >> 24) & 0xFF;
        int red = (pixelVal >> 16) & 0xFF;
        int green = (pixelVal >> 8) & 0xFF;
        int blue = pixelVal & 0xFF;
        outputPixelMatrix[i][j] = new Pixel(red, green, blue, alpha);
      }
    }
    return outputPixelMatrix;
  }
}
