package model.image;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import controller.ImageIOHelper;
import model.imagetransformation.AbstractImageTransformation;
import model.imagetransformation.Methods;

/**
 * This abstract class serves as a utility to process image transformation operations.
 * It provides methods to execute image processing methods based on the provided 'Methods' enum.
 */
public class ImageModel implements ImageModelInterface {

  /**
   * Processes the image based on the specified method and additional parameters.
   *
   * @param inputFileName  The input image file name.
   * @param outputFileName The output image file name.
   * @param method         The method to be applied for image processing (from the 'Methods' enum).
   * @param otherParams    Additional parameters required for the specified method, if any.
   */
  public void processImage(String inputFileName, String outputFileName, Methods method,
                           Object... otherParams) {

    AbstractImageTransformation transformer;
    String className = "model.imagetransformation." + ImageIOHelper.camelCase(method.toString());
    Class<?> transformerClass = null;
    try {
      transformerClass = Class.forName(className);
      Constructor<?> constructor = transformerClass.getConstructor();
      transformer = (AbstractImageTransformation) constructor.newInstance();
      Pixel[][] outputPixelMatrix = transformer.generate(inputFileName, outputFileName,
              IMAGES_PIXEL_MATRICES, otherParams);
      if (outputPixelMatrix != null) {
        IMAGES_PIXEL_MATRICES.put(outputFileName, outputPixelMatrix);
      }
    } catch (ClassNotFoundException e) {
      System.err.println("Class name " + className + " for the given model.image type not found. "
              + "Please check if your class name has a prefix as the model.image extension");
    } catch (InstantiationException e) {
      System.err.println("Could not create the instance of " + transformerClass);
    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      System.err.println("Unable to access the instance of " + className);
    }
  }
}

