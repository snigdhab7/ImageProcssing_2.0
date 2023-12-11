package model.imagetransformation;

import java.util.Arrays;
import java.util.Map;

import model.image.Pixel;

/**
 * Transformation class that extracts compressed image using haar wavelet transformation.
 */
public class ImageCompression extends AbstractImageTransformation {
  /**
   * Extracts the compressed Image using haar wave transformation.
   *
   * @param inputFileName       The name of the input image file.
   * @param outputFileName      The name of the output image file after luma extraction.
   * @param imagesPixelMatrices A map containing pixel matrices of different images.
   * @param otherParams         Additional parameters or configurations for the transformation.
   * @return A two-dimensional array of pixels representing the luma component of the image.
   */
  @Override
  public Pixel[][] generate(String inputFileName, String outputFileName,
                            Map<String, Pixel[][]> imagesPixelMatrices, Object... otherParams) {
    Pixel[][] outputPixelMatrix;
    Pixel[][] invPixelMatrix;
    Pixel[][] pixelMatrix = imagesPixelMatrices.get(inputFileName);

    // Original height and width of the image
    int height = pixelMatrix.length;
    int width = pixelMatrix[0].length;
    // Calculate the new size which is of power 2
    int newSize = calculateNewSize(Math.max(height, width));
    // Pad the remaining pixels to match the size of size 2
    Pixel[][] paddedPixels = padImage(pixelMatrix, newSize);
    double percentage = (int) otherParams[0];//compression percentage


    if (percentage > 0 && percentage < 100) {
      //Haar wave transformation logic
      Pixel[][] haar2DFWTransform = doHaar2DFWTransform(paddedPixels, percentage);
      // calculate threshold to replace min values with zeros
      double threshold = calculateThreshold(haar2DFWTransform, percentage);
      // apply threshold to replace min values with zeros
      Pixel[][] thresholdedPixels = applyThreshold(haar2DFWTransform, threshold);

      //Haar wave invert transformation logic
      invPixelMatrix = doHaar2DInvTransform(thresholdedPixels, percentage);

      //Unpad the pixel matrix to get the original image
      outputPixelMatrix = unpadImage(invPixelMatrix, height, width);
      return outputPixelMatrix;
    } else {
      System.err.println("The percentage values are invalid. Please enter valid values.");
      return null;
    }
  }

  // function to pad the image to so that it is a square power of 2.
  private static Pixel[][] padImage(Pixel[][] pixelMatrix, int newSize) {
    int height = pixelMatrix.length;
    int width = pixelMatrix[0].length;

    Pixel[][] paddedPixels = new Pixel[newSize][newSize];

    for (int i = 0; i < newSize; i++) {
      for (int j = 0; j < newSize; j++) {
        if (i < height && j < width) {
          paddedPixels[i][j] = pixelMatrix[i][j];
        } else {
          paddedPixels[i][j] = new Pixel(0, 0, 0); // Padding with black pixels
        }
      }
    }

    return paddedPixels;
  }

  // Function to unpad to get the original image.
  private static Pixel[][] unpadImage(Pixel[][] pixels, int originalHeight, int originalWidth) {
    Pixel[][] unpaddedPixels = new Pixel[originalHeight][originalWidth];
    for (int i = 0; i < originalHeight; i++) {
      System.arraycopy(pixels[i], 0, unpaddedPixels[i], 0, originalWidth);
    }
    return unpaddedPixels;
  }

  // function to calculate the new size of power 2
  private static int calculateNewSize(int size) {
    int newSize = 1;
    while (newSize < size) {
      newSize *= 2; // return the new size of power 2
    }
    return newSize;
  }

  // function to calculate the number of cycles for compression
  private static int calculateCycles(int maxCycle, double percentage) {
    return (int) Math.ceil(maxCycle * (percentage / 100.0));
  }

  private static int getHaarMaxCycles(int hw) {
    int cycles = 0;
    while (hw > 1) {
      cycles++;
      hw /= 2;
    }
    return cycles;
  }

  private static int[] uniqueValues(int[] flattenedPixels) {
    return Arrays.stream(flattenedPixels).filter(num -> num != 0).toArray();
  }

  private static boolean isCycleAllowed(int maxCycle, int cycles) {
    return cycles <= maxCycle;
  }

  // function to perform haar wave transformation for image compression
  private static Pixel[][] doHaar2DFWTransform(Pixel[][] pixelMatrix, double percentage) {
    int w = pixelMatrix[0].length;
    int h = pixelMatrix.length;
    int maxCycle = getHaarMaxCycles(w);
    int cycles = calculateCycles(maxCycle, percentage);
    boolean isCycleAllowed = isCycleAllowed(maxCycle, cycles);
    if (isCycleAllowed) {
      Pixel[][] ds = new Pixel[h][w];
      Pixel[][] tempds = new Pixel[h][w];
      for (int i = 0; i < pixelMatrix.length; i++) {
        System.arraycopy(pixelMatrix[i], 0, ds[i], 0, pixelMatrix[i].length);
      }
      for (int i = 0; i < cycles; i++) {
        // for rows
        w /= 2;
        for (int j = 0; j < h; j++) {
          for (int k = 0; k < w; k++) {
            int redA = ds[j][2 * k].getRed();
            int greenA = ds[j][2 * k].getGreen();
            int blueA = ds[j][2 * k].getBlue();

            int redB = ds[j][2 * k + 1].getRed();
            int greenB = ds[j][2 * k + 1].getGreen();
            int blueB = ds[j][2 * k + 1].getBlue();

            int avgRedAdd = (redA + redB) / 2;
            int avgGreenAdd = (greenA + greenB) / 2;
            int avgBlueAdd = (blueA + blueB) / 2;

            int avgRedSub = (redA - redB) / 2;
            int avgGreenSub = (greenA - greenB) / 2;
            int avgBlueSub = (blueA - blueB) / 2;

            tempds[j][k] = new Pixel(avgRedAdd, avgGreenAdd, avgBlueAdd);
            tempds[j][k + w] = new Pixel(avgRedSub, avgGreenSub, avgBlueSub);
          }
        }
        for (int j = 0; j < h; j++) {
          for (int k = 0; k < w; k++) {
            ds[j][k] = tempds[j][k];
            ds[j][k + w] = tempds[j][k + w];
          }
        }
        // for columns
        h /= 2;
        for (int j = 0; j < w; j++) {
          for (int k = 0; k < h; k++) {
            int redA = ds[2 * k][j].getRed();
            int greenA = ds[2 * k][j].getGreen();
            int blueA = ds[2 * k][j].getBlue();

            int redB = ds[2 * k + 1][j].getRed();
            int greenB = ds[2 * k + 1][j].getGreen();
            int blueB = ds[2 * k + 1][j].getBlue();

            int avgRedAdd = (redA + redB) / 2;
            int avgGreenAdd = (greenA + greenB) / 2;
            int avgBlueAdd = (blueA + blueB) / 2;

            int avgRedSub = (redA - redB) / 2;
            int avgGreenSub = (greenA - greenB) / 2;
            int avgBlueSub = (blueA - blueB) / 2;

            tempds[k][j] = new Pixel(avgRedAdd, avgGreenAdd, avgBlueAdd);
            tempds[k + h][j] = new Pixel(avgRedSub, avgGreenSub, avgBlueSub);
          }
        }
        for (int j = 0; j < w; j++) {
          for (int k = 0; k < h; k++) {
            ds[k][j] = tempds[k][j];
            ds[k + h][j] = tempds[k + h][j];
          }
        }
      }
      return ds;
    }
    return null;
  }

  // function to perform haar wave invert logic for image compression
  private static Pixel[][] doHaar2DInvTransform(Pixel[][] pixelMatrix, double percentage) {
    int w = pixelMatrix[0].length;
    int h = pixelMatrix.length;
    int maxCycle = getHaarMaxCycles(w);
    int cycles = calculateCycles(maxCycle, percentage);
    boolean isCycleAllowed = isCycleAllowed(maxCycle, cycles);
    if (isCycleAllowed) {
      Pixel[][] ds = new Pixel[h][w];
      Pixel[][] tempds = new Pixel[h][w];
      for (int i = 0; i < pixelMatrix.length; i++) {
        System.arraycopy(pixelMatrix[i], 0, ds[i], 0, pixelMatrix[i].length);
      }
      int hh = h / (int) Math.pow(2, cycles);
      int ww = w / (int) Math.pow(2, cycles);
      // for rows
      for (int i = cycles; i > 0; i--) {
        for (int j = 0; j < ww; j++) {
          for (int k = 0; k < hh; k++) {
            int redA = ds[k][j].getRed();
            int greenA = ds[k][j].getGreen();
            int blueA = ds[k][j].getBlue();

            int redB = ds[k + hh][j].getRed();
            int greenB = ds[k + hh][j].getGreen();
            int blueB = ds[k + hh][j].getBlue();

            int avgRedAdd = (redA + redB);
            int avgGreenAdd = (greenA + greenB);
            int avgBlueAdd = (blueA + blueB);

            int avgRedSub = (redA - redB);
            int avgGreenSub = (greenA - greenB);
            int avgBlueSub = (blueA - blueB);

            tempds[2 * k][j] = new Pixel(avgRedAdd, avgGreenAdd, avgBlueAdd);
            tempds[2 * k + 1][j] = new Pixel(avgRedSub, avgGreenSub, avgBlueSub);
          }
        }
        for (int j = 0; j < ww; j++) {
          for (int k = 0; k < hh; k++) {
            ds[2 * k][j] = tempds[2 * k][j];
            ds[2 * k + 1][j] = tempds[2 * k + 1][j];
          }
        }

        hh *= 2;
        // for columns
        for (int j = 0; j < hh; j++) {
          for (int k = 0; k < ww; k++) {
            int redA = ds[j][k].getRed();
            int greenA = ds[j][k].getGreen();
            int blueA = ds[j][k].getBlue();

            int redB = ds[j][k + ww].getRed();
            int greenB = ds[j][k + ww].getGreen();
            int blueB = ds[j][k + ww].getBlue();

            int avgRedAdd = (redA + redB);
            int avgGreenAdd = (greenA + greenB);
            int avgBlueAdd = (blueA + blueB);

            int avgRedSub = (redA - redB);
            int avgGreenSub = (greenA - greenB);
            int avgBlueSub = (blueA - blueB);

            tempds[j][2 * k] = new Pixel(avgRedAdd, avgGreenAdd, avgBlueAdd);
            tempds[j][2 * k + 1] = new Pixel(avgRedSub, avgGreenSub, avgBlueSub);
          }
        }
        for (int j = 0; j < hh; j++) {
          for (int k = 0; k < ww; k++) {
            ds[j][2 * k] = tempds[j][2 * k];
            ds[j][2 * k + 1] = tempds[j][2 * k + 1];
          }
        }
        ww *= 2;
      }
      return ds;
    }
    return null;
  }

  // Thresholding: find the unique absolute values for all three channels, use the percentage
  // to find the threshold and use the threshold for all three channels.
  // Make sure you are picking the smallest values properly when computing the threshold.
  private static double calculateThreshold(Pixel[][] pixelMatrix, double percentage) {
    int size = pixelMatrix.length * pixelMatrix[0].length * 3;
    int[] flattenedPixels = new int[size];
    int index = 0;
    for (Pixel[] row : pixelMatrix) {
      for (Pixel pixel : row) {
        flattenedPixels[index++] = Math.abs(pixel.getRed());
        flattenedPixels[index++] = Math.abs(pixel.getGreen());
        flattenedPixels[index++] = Math.abs(pixel.getBlue());
      }
    }
    int[] filteredArray = uniqueValues(flattenedPixels);
    Arrays.sort(filteredArray);
    int thresholdIndex = (int) (percentage / 100.0 * filteredArray.length);
    return filteredArray[thresholdIndex];
  }

  private static Pixel[][] applyThreshold(Pixel[][] pixelMatrix, double threshold) {
    Pixel[][] thresholdedPixels = new Pixel[pixelMatrix.length][pixelMatrix[0].length];
    for (int i = 0; i < pixelMatrix.length; i++) {
      for (int j = 0; j < pixelMatrix[0].length; j++) {
        thresholdedPixels[i][j] = new Pixel(
                pixelMatrix[i][j].getRed() >= threshold ? pixelMatrix[i][j].getRed() : 0,
                pixelMatrix[i][j].getGreen() >= threshold ? pixelMatrix[i][j].getGreen() : 0,
                pixelMatrix[i][j].getBlue() >= threshold ? pixelMatrix[i][j].getBlue() : 0
        );
      }
    }
    return thresholdedPixels;
  }
}