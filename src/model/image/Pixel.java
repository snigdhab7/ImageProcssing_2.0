package model.image;

/**
 * Represents a single pixel in an image with color values (red, green, blue), and an optional
 * alpha channel.
 */
public class Pixel {
  protected int red;
  protected int green;
  protected int blue;
  private int alpha = 255;


  /**
   * Constructs a Pixel with specified red, green, and blue values. Alpha defaults to
   * 255 (fully opaque).
   *
   * @param red   The red component of the pixel.
   * @param green The green component of the pixel.
   * @param blue  The blue component of the pixel.
   */
  public Pixel(int red, int green, int blue) {
    this.red = clamp(red);
    this.green = clamp(green);
    this.blue = clamp(blue);
  }

  /**
   * Constructs a Pixel with specified red, green, blue, and alpha values.
   *
   * @param red   The red component of the pixel.
   * @param green The green component of the pixel.
   * @param blue  The blue component of the pixel.
   * @param alpha The alpha value representing the pixel's transparency.
   */
  public Pixel(int red, int green, int blue, int alpha) {
    this.red = red;
    this.green = green;
    this.blue = blue;
    this.alpha = alpha;
  }

  /**
   * Limits a pixel value to the range [0, 255].
   *
   * @param pixelValue The pixel value to be clamped.
   * @return The clamped pixel value within the range [0, 255].
   */
  public static int clamp(int pixelValue) {
    int maxPixelValue = 255;
    if (pixelValue < 0) {
      pixelValue = 0;
    } else if (pixelValue > maxPixelValue) {
      pixelValue = maxPixelValue;
    }
    return pixelValue;
  }

  /**
   * Processes a single pixel with the given filter matrix.
   *
   * @param filterMatrix The filter matrix to apply to the pixel.
   * @param pixel        The pixel to be processed.
   * @return A new Pixel resulting from applying the filter matrix to the given pixel.
   */
  public static Pixel processPixel(double[][] filterMatrix, Pixel pixel) {
    int kernelMatrixRows = filterMatrix.length;
    int[][] singlePixelMatrix = {{pixel.getRed()}, {pixel.getGreen()}, {pixel.getBlue()}};
    final int singlePixelMatrixRows = 3; // 3 rows as one for Red, Green, and Blue respectively
    final int singlePixelMatrixColumns = 1;

    int[] newPixelValues = new int[3];
    for (int i = 0; i < kernelMatrixRows; i++) {
      for (int j = 0; j < singlePixelMatrixColumns; j++) {
        for (int k = 0; k < singlePixelMatrixRows; k++) {
          newPixelValues[i] += (int) (filterMatrix[i][k] * singlePixelMatrix[k][j]);
        }
      }
    }
    return new Pixel(clamp(newPixelValues[0]), clamp(newPixelValues[1]), clamp(newPixelValues[2]),
            pixel.getAlpha());
  }


  /**
   * Processes a pixel based on a kernel matrix in the context of a larger pixel matrix.
   *
   * @param kernelMatrix The kernel matrix to apply to the pixel matrix.
   * @param pixelMatrix  The matrix of pixels to process.
   * @param pixelRow     The row index of the pixel.
   * @param pixelColumn  The column index of the pixel.
   * @return A new Pixel resulting from applying the kernel matrix to the pixel matrix.
   */
  public static Pixel processPixel(double[][] kernelMatrix, Pixel[][] pixelMatrix,
                                   int pixelRow, int pixelColumn) {
    int pixelMatrixRows = pixelMatrix.length;
    int pixelMatrixCols = pixelMatrix[0].length;
    int kernelSize = kernelMatrix.length;
    int kernelCenter = (kernelSize - 1) / 2;
    int sumR = 0;
    int sumG = 0;
    int sumB = 0;
    int mappedRow;
    int mappedCol;
    for (int s = 0; s < kernelSize; s++) {
      for (int t = 0; t < kernelSize; t++) {
        // find mapped cell position of pixel matrix as per current kernel cell
        mappedRow = pixelRow + s - kernelCenter;
        mappedCol = pixelColumn + t - kernelCenter;

        // check if position is going out of pixelMatrix boundaries
        if ((mappedRow < 0 || mappedRow >= pixelMatrixRows)
                || (mappedCol < 0 || mappedCol >= pixelMatrixCols)) {
          continue;
        }
        Pixel currentPixel = pixelMatrix[mappedRow][mappedCol];
        sumR += (int) (kernelMatrix[s][t] * currentPixel.getRed());
        sumG += (int) (kernelMatrix[s][t] * currentPixel.getGreen());
        sumB += (int) (kernelMatrix[s][t] * currentPixel.getBlue());
      }
    }
    return new Pixel(clamp(sumR), clamp(sumG), clamp(sumB),
            pixelMatrix[pixelRow][pixelColumn].getAlpha());
  }

  /**
   * Gets the red component of the pixel.
   *
   * @return The red component value of the pixel.
   */
  public int getRed() {
    return red;
  }

  /**
   * Gets the green component of the pixel.
   *
   * @return The green component value of the pixel.
   */
  public int getGreen() {
    return green;
  }

  /**
   * Gets the blue component of the pixel.
   *
   * @return The blue component value of the pixel.
   */
  public int getBlue() {
    return blue;
  }

  /**
   * Gets the alpha component of the pixel.
   *
   * @return The alpha component value of the pixel (transparency).
   */
  public int getAlpha() {
    return alpha;
  }

  @Override
  public String toString() {
    return getRed() + " " + getGreen() + " " + getBlue();
  }
}
