package model.imagetransformation;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * A class responsible for drawing a histogram based on input data.
 */
public class HistogramDrawer {

  private int width; // Width of the histogram
  private int height; // Height of the histogram
  private int maxFrequency = 0; // Maximum frequency value among all channels
  private Graphics2D g2d = null; // Graphics2D object for drawing

  /**
   * Constructs a HistogramDrawer with the specified width and height.
   *
   * @param width  The width of the histogram.
   * @param height The height of the histogram.
   */
  public HistogramDrawer(int width, int height) {
    this.width = width;
    this.height = height;
  }

  /**
   * Gets the maximum frequency value among all channels in the histogram map.
   *
   * @param histogramMap A map containing histogram data for different color channels.
   * @return The maximum frequency value.
   */
  private int getMaxFrequency(Map<String, int[]> histogramMap) {
    int maxVal = 0;

    for (int i = 0; i < histogramMap.get("red").length; i++) {
      maxVal = Math.max(Math.max(Math.max(histogramMap.get("red")[i], histogramMap.get("green")[i]),
              histogramMap.get("blue")[i]), maxVal);
    }
    return maxVal;
  }

  /**
   * Draws lines for a specific color channel in the histogram.
   *
   * @param histogramData The histogram data for a specific color channel.
   * @param color         The color of the lines.
   */
  private void drawLines(int[] histogramData, Color color) {
    g2d.setColor(color);
    int barWidth = width / histogramData.length;
    int prevX = 0;
    int prevY = height;

    for (int i = 0; i < histogramData.length; i++) {
      int barHeight = (int) Math.ceil((double) histogramData[i] / maxFrequency * height);
      int x = i * barWidth;
      int y = height - barHeight;
      g2d.drawLine(prevX, prevY, x, y);
      prevX = x;
      prevY = y;
    }
  }

  /**
   * Draws the histogram based on the input histogram map.
   *
   * @param histogramMap A map containing histogram data for different color channels.
   * @return The BufferedImage representing the generated histogram image.
   */
  protected BufferedImage draw(Map<String, int[]> histogramMap) {
    BufferedImage histogramImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    g2d = histogramImage.createGraphics();

    // Fill background
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0, 0, width, height);

    // get maximum frequency value among all channels
    maxFrequency = getMaxFrequency(histogramMap);

    // draw lines for each channel
    drawLines(histogramMap.get("red"), Color.red);
    drawLines(histogramMap.get("green"), Color.green);
    drawLines(histogramMap.get("blue"), Color.blue);

    g2d.dispose();
    return histogramImage;
  }
}
