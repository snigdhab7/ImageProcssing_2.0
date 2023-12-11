package controller;

import java.awt.image.BufferedImage;

import model.image.ImageModelInterface;
import model.image.Pixel;
import model.imagetransformation.Methods;
import view.ViewInterface;

/**
 * The GUIController class implements the ControllerInterface and Features interfaces,
 * serving as the controller for the image processing application's graphical user interface.
 * It manages interactions between the view and model components and provides image processing
 * features.
 */
public class GUIController implements ControllerInterface, Features {
  /**
   * The ImageModelInterface instance to interact with the underlying image model.
   */
  private ImageModelInterface imageModel;

  /**
   * The ImageProcessingCommandHelper instance to manage image processing commands.
   */
  private ImageProcessingCommandHelper imageProcessingCommandHelper;

  /**
   * Sets the view for the controller, adding the current instance as a feature to the view.
   *
   * @param imageProcessorView The view to be set for the controller.
   */
  public void setView(ViewInterface imageProcessorView) {
    imageProcessorView.addFeatures(this);
  }


  /**
   * Retrieves the pixel matrix for the specified image name from the image model.
   *
   * @param imageName The name of the image.
   * @return The pixel matrix representing the image.
   */
  @Override
  public Pixel[][] getPixelMatrixForImage(String imageName) {
    return this.imageModel.IMAGES_PIXEL_MATRICES.get(imageName);
  }


  /**
   * Initializes the controller with the provided image model and creates the
   * ImageProcessingCommandHelper if not already initialized.
   *
   * @param imageModel The ImageModelInterface instance to be set for the controller.
   */
  @Override
  public void start(ImageModelInterface imageModel) {
    this.imageModel = imageModel;
    if (imageProcessingCommandHelper == null) {
      imageProcessingCommandHelper = new ImageProcessingCommandHelper();
      imageProcessingCommandHelper.create(this.imageModel);
    }
  }

  /**
   * Delegates the "load" command to the ImageProcessingCommandHelper.
   *
   * @param commandParams The parameters for the "load" command.
   */
  @Override
  public void load(String[] commandParams) {
    imageProcessingCommandHelper.commandsMap.get("load").run(commandParams);
  }

  /**
   * Delegates the "save" command to the ImageProcessingCommandHelper.
   *
   * @param commandParams The parameters for the "save" command.
   */
  @Override
  public void save(String[] commandParams) {
    imageProcessingCommandHelper.commandsMap.get("save").run(commandParams);
  }

  /**
   * Processes an image using the specified method and parameters through the image model.
   *
   * @param inputFileName  The name of the input image file.
   * @param outputFileName The name of the output image file.
   * @param method         The processing method to be applied.
   * @param otherParams    Additional parameters required for the specified method.
   */
  @Override
  public void processImage(String inputFileName, String outputFileName, String method,
                           Object... otherParams) {
    imageModel.processImage(inputFileName, outputFileName, Methods.valueOf(method), otherParams);
  }

  /**
   * Creates a BufferedImage from the provided pixel matrix.
   *
   * @param pixelMatrix The pixel matrix representing the image.
   * @return The BufferedImage created from the pixel matrix.
   */
  @Override
  public BufferedImage createImageFromPixels(Pixel[][] pixelMatrix) {
    int pixelMatrixRows = pixelMatrix.length;
    int pixelMatrixCols = pixelMatrix[0].length;

    BufferedImage image = new BufferedImage(pixelMatrixCols, pixelMatrixRows,
            BufferedImage.TYPE_INT_RGB);

    Pixel pixel;
    for (int i = 0; i < pixelMatrixRows; i++) {
      for (int j = 0; j < pixelMatrixCols; j++) {
        pixel = pixelMatrix[i][j];
        int pixelVal = (pixel.getAlpha() << 24) | (pixel.getRed() << 16)
                | (pixel.getGreen() << 8) | pixel.getBlue();
        image.setRGB(j, i, pixelVal);
      }
    }
    return image;
  }
}
