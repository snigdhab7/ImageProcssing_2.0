package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import model.image.Pixel;

/**
 * The Helper class contains methods to assist in handling image file operations.
 */
public class ImageIOHelper {

  /**
   * Converts an input string to camelCase format.
   *
   * @param str The input string to be converted.
   * @return The input string in camelCase format.
   */
  public static String camelCase(String str) {
    String[] s = str.split("_");
    String output = "";
    for (int i = 0; i < s.length; i++) {
      output += (s[i].charAt(0) + "".toUpperCase() + s[i].substring(1).toLowerCase());
    }
    return output;
  }

  /**
   * Reads an image in JPG or PNG format and returns a matrix of pixels.
   *
   * @param fileName The name of the image file to be read.
   * @return A matrix of Pixel objects representing the image pixels.
   */
  public static Pixel[][] readJPGPNG(String fileName) {
    Pixel[][] pixelMatrix = null;
    try {
      BufferedImage img = ImageIO.read(new File(fileName));
      pixelMatrix = new Pixel[img.getHeight()][img.getWidth()];
      for (int i = 0; i < img.getHeight(); i++) {
        for (int j = 0; j < img.getWidth(); j++) {
          int pixelVal = img.getRGB(j, i);
          int alpha = (pixelVal >> 24) & 0xFF;
          int red = (pixelVal >> 16) & 0xFF;
          int green = (pixelVal >> 8) & 0xFF;
          int blue = pixelVal & 0xFF;
          pixelMatrix[i][j] = new Pixel(red, green, blue, alpha);
        }
      }
    } catch (FileNotFoundException e) {
      System.err.println("File " + fileName + " not found!");
    } catch (Exception e) {
      System.err.println("Could not open file - " + fileName);
    }
    return pixelMatrix;
  }

  /**
   * Writes a matrix of pixels to a file in JPG or PNG format.
   *
   * @param outputImgName The name of the output image file to be written.
   * @param pixelMatrix   The matrix of pixels to be written to the image file.
   */
  public static void writeJPGPNG(String outputImgName, Pixel[][] pixelMatrix) {
    try {
      int pixelMatrixRows = pixelMatrix.length;
      int pixelMatrixCols = pixelMatrix[0].length;
      String imageFormat = outputImgName.split("\\.")[1];
      BufferedImage img;
      if (imageFormat.equalsIgnoreCase("jpg")) {
        img = new BufferedImage(pixelMatrixCols, pixelMatrixRows,
                BufferedImage.TYPE_INT_RGB);
      } else {
        img = new BufferedImage(pixelMatrixCols, pixelMatrixRows,
                BufferedImage.TYPE_INT_ARGB);
      }
      Pixel pixel;
      for (int i = 0; i < pixelMatrixRows; i++) {
        for (int j = 0; j < pixelMatrixCols; j++) {
          pixel = pixelMatrix[i][j];
          int pixelVal = (pixel.getAlpha() << 24) | (pixel.getRed() << 16)
                  | (pixel.getGreen() << 8) | pixel.getBlue();
          img.setRGB(j, i, pixelVal);
        }
      }
      ImageIO.write(img, imageFormat, new File(outputImgName));
      img.flush();
    } catch (IOException e) {
      System.err.println("Error while writing " + outputImgName);
    }
  }

  /**
   * Reads a PPM image file and returns the pixel matrix.
   *
   * @param filename The name of the PPM image file to be read.
   * @return A two-dimensional array of pixels representing the PPM image.
   */
  public static Pixel[][] readPPM(String filename) {
    Scanner sc;
    try {
      sc = new Scanner(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      System.err.println("File " + filename + " not found!");
      return null;
    }
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (!s.isEmpty() && s.charAt(0) != '#') {
        String concat = s + System.lineSeparator();
        builder.append(concat);
      }
    }

    //now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());
    String token;
    token = sc.next();
    if (!token.equals("P3")) {
      System.out.println("Invalid PPM file: plain RAW file should begin with P3");
    }
    int pixelMatrixCols = sc.nextInt();
    int pixelMatrixRows = sc.nextInt();
    Pixel[][] pixelMatrix = new Pixel[pixelMatrixRows][pixelMatrixCols];
    int maxPixelValue = sc.nextInt();
    for (int i = 0; i < pixelMatrixRows; i++) {
      for (int j = 0; j < pixelMatrixCols; j++) {
        pixelMatrix[i][j] = new Pixel(sc.nextInt(), sc.nextInt(), sc.nextInt());
      }
    }
    return pixelMatrix;
  }


  /**
   * Writes a PPM image file using the given pixel matrix.
   *
   * @param fileName    The name of the output PPM image file to be written.
   * @param pixelMatrix The pixel matrix representing the PPM image to be written.
   */
  public static void writePPM(String fileName, Pixel[][] pixelMatrix) {
    FileOutputStream fout;
    Pixel pixel;
    int pixelMatrixRows = pixelMatrix.length;
    int pixelMatrixCols = pixelMatrix[0].length;
    try {
      // open output file to write to
      fout = new FileOutputStream(fileName, false);
      // write initial metadata
      fout.write("P3\n".getBytes());
      fout.write((pixelMatrixCols + " " + pixelMatrixRows + "\n").getBytes());
      fout.write(("255\n").getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      for (int i = 0; i < pixelMatrixRows; i++) {
        for (int j = 0; j < pixelMatrixCols; j++) {
          pixel = pixelMatrix[i][j];
          fout.write((pixel.getRed() + " " + pixel.getGreen() + " "
                  + pixel.getBlue() + "\n").getBytes());
        }
      }
      fout.close();
    } catch (IOException e) {
      System.err.println("Error while writing " + fileName);
    }
  }
}
