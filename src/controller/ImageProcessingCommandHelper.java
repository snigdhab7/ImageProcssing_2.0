package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import model.image.ImageFormats;
import model.image.ImageModelInterface;
import model.image.ImageUtilInterface;
import model.image.Pixel;
import model.imagetransformation.Methods;

/**
 * The ImageProcessingCommandHelper class assists in executing image processing commands.
 */
public class ImageProcessingCommandHelper {
  protected Map<String, Command> commandsMap;
  private ImageModelInterface imageModel;

  /**
   * Constructs an ImageProcessingCommandHelper.
   */
  public ImageProcessingCommandHelper() {
    commandsMap = new HashMap<>();
  }

  /**
   * Initializes the command map and sets the image model.
   *
   * @param imageModel The image model to be associated with this command helper.
   */
  public void create(ImageModelInterface imageModel) {
    this.imageModel = imageModel;

    // Command to load an image
    commandsMap.put("load", new Command() {
      @Override
      public void run(String[] commandParams) {
        load(commandParams);
      }
    });

    // Command to save an image
    commandsMap.put("save", new Command() {
      @Override
      public void run(String[] commandParams) {
        String imageFormat = commandParams[1].split("\\.")[1];
        fileOperation(imageFormat, FileOperations.WRITE, commandParams);
      }
    });

    // Command to convert an image to a greyscale version of it
    commandsMap.put("greyscale", new Command() {
      @Override
      public void run(String[] commandParams) {
        double splitPercentage = getSplitPercentage(commandParams);
        imageModel.processImage(commandParams[1], commandParams[2], Methods.GREYSCALE,
                splitPercentage);
      }
    });

    // Command to brighten an image with given intensity
    commandsMap.put("brighten", new Command() {
      @Override
      public void run(String[] commandParams) {
        imageModel.processImage(commandParams[2], commandParams[3], Methods.BRIGHTEN,
                Integer.parseInt(commandParams[1]));
      }
    });

    // Command to vertically flip an image
    commandsMap.put("vertical-flip", new Command() {
      @Override
      public void run(String[] commandParams) {
        imageModel.processImage(commandParams[1], commandParams[2], Methods.VERTICAL_FLIP);
      }
    });

    // Command to horizontally flip an image
    commandsMap.put("horizontal-flip", new Command() {
      @Override
      public void run(String[] commandParams) {
        imageModel.processImage(commandParams[1], commandParams[2], Methods.HORIZONTAL_FLIP);
      }
    });

    // Command to convert an image to a value component version of it
    commandsMap.put("value-component", new Command() {
      @Override
      public void run(String[] commandParams) {
        imageModel.processImage(commandParams[1], commandParams[2], Methods.VALUE_COMPONENT);
      }
    });

    // Command to split an image into three different versions of it based on red, green, and
    // blue components
    commandsMap.put("rgb-split", new Command() {
      @Override
      public void run(String[] commandParams) {
        imageModel.processImage(commandParams[1], commandParams[2], Methods.RED_COMPONENT);
        imageModel.processImage(commandParams[1], commandParams[3], Methods.GREEN_COMPONENT);
        imageModel.processImage(commandParams[1], commandParams[4], Methods.BLUE_COMPONENT);
      }
    });

    // Command to combine red, green, and blue components from three individual images and output
    // a single resulting image
    commandsMap.put("rgb-combine", new Command() {
      @Override
      public void run(String[] commandParams) {
        imageModel.processImage(commandParams[2], commandParams[1], Methods.RGB_COMBINE,
                imageModel.IMAGES_PIXEL_MATRICES.get(commandParams[2]),
                imageModel.IMAGES_PIXEL_MATRICES.get(commandParams[3]),
                imageModel.IMAGES_PIXEL_MATRICES.get(commandParams[4]));
      }
    });

    // Command to generate an image using just the red component of that image
    commandsMap.put("red-component", new Command() {
      @Override
      public void run(String[] commandParams) {
        imageModel.processImage(commandParams[1], commandParams[2], Methods.RED_COMPONENT);
      }
    });

    // Command to generate an image using just the green component of that image
    commandsMap.put("green-component", new Command() {
      @Override
      public void run(String[] commandParams) {
        imageModel.processImage(commandParams[1], commandParams[2], Methods.GREEN_COMPONENT);
      }
    });

    // Command to generate an image using just the blue component of that image
    commandsMap.put("blue-component", new Command() {
      @Override
      public void run(String[] commandParams) {
        imageModel.processImage(commandParams[1], commandParams[2], Methods.BLUE_COMPONENT);
      }
    });

    // Command to generate an image representing luma component of that image
    commandsMap.put("luma-component", new Command() {
      @Override
      public void run(String[] commandParams) {
        imageModel.processImage(commandParams[1], commandParams[2], Methods.LUMA_COMPONENT);
      }
    });
    // Command to generate an image representing intensity component of that image
    commandsMap.put("intensity-component", new Command() {
      @Override
      public void run(String[] commandParams) {
        imageModel.processImage(commandParams[1], commandParams[2], Methods.INTENSITY_COMPONENT);
      }
    });

    // Command to blur given image
    commandsMap.put("blur", new Command() {
      @Override
      public void run(String[] commandParams) {
        double splitPercentage = getSplitPercentage(commandParams);
        imageModel.processImage(commandParams[1], commandParams[2], Methods.BLUR, splitPercentage);
      }
    });

    // Command to sharpen the given image
    commandsMap.put("sharpen", new Command() {
      @Override
      public void run(String[] commandParams) {
        double splitPercentage = getSplitPercentage(commandParams);
        imageModel.processImage(commandParams[1], commandParams[2], Methods.SHARPEN,
                splitPercentage);
      }
    });
    // Command to generate a sepia toned image
    commandsMap.put("sepia", new Command() {
      @Override
      public void run(String[] commandParams) {
        double splitPercentage = getSplitPercentage(commandParams);
        imageModel.processImage(commandParams[1], commandParams[2], Methods.SEPIA, splitPercentage);
      }
    });

    // Command to generate a histogram for red, green, and blue components of given image
    commandsMap.put("histogram", new Command() {
      @Override
      public void run(String[] commandParams) {
        imageModel.processImage(commandParams[1], commandParams[2], Methods.HISTOGRAM);
      }
    });

    // Command to color correct given image
    commandsMap.put("color-correct", new Command() {
      @Override
      public void run(String[] commandParams) {
        double splitPercentage = getSplitPercentage(commandParams);
        imageModel.processImage(commandParams[1], commandParams[2],
                Methods.COLOR_CORRECT, splitPercentage);
      }
    });

    // Command to perform levels adjustment on given image
    commandsMap.put("levels-adjust", new Command() {
      @Override
      public void run(String[] commandParams) {
        double splitPercentage = getSplitPercentage(commandParams);
        imageModel.processImage(commandParams[4], commandParams[5], Methods.LEVELS_ADJUST,
                commandParams[1], commandParams[2], commandParams[3], splitPercentage);
      }
    });

    // command to compress a given image
    commandsMap.put("compress", new Command() {
      @Override
      public void run(String[] commandParams) {
        imageModel.processImage(commandParams[2], commandParams[3], Methods.IMAGE_COMPRESSION,
                Integer.parseInt(commandParams[1]));
      }
    });

    // Command to dither an image
    commandsMap.put("dither", new Command() {
      @Override
      public void run(String[] commandParams) {
        imageModel.processImage(commandParams[1], commandParams[2], Methods.DITHER);
      }
    });

    // command to run a script file containing various commands
    commandsMap.put("run", new Command() {
      @Override
      public void run(String[] commandParams) {
        runScript(commandParams[1]);
      }
    });

  }

  /**
   * Performs file read or write operations for image loading and saving.
   *
   * @param imageFormat   The format of the image to be read or written.
   * @param opName        The operation name (READ or WRITE).
   * @param commandParams Parameters related to the operation.
   * @return The pixel matrix read from the file, or null in case of a write operation.
   */
  private Pixel[][] fileOperation(String imageFormat, FileOperations opName,
                                  String[] commandParams) {
    ImageUtilInterface image = null;
    String className = "model.image." + imageFormat.toUpperCase() + "Image";
    Class<?> imageClass = null;
    try {
      imageClass = Class.forName(className);
      Constructor<?> ctor = imageClass.getConstructor();
      image = (ImageUtilInterface) ctor.newInstance();
    } catch (ClassNotFoundException e) {
      System.err.println("Class name " + className + " for the given model.image type not found. "
              + "Please check if your class name has a prefix as the model.image extension");
    } catch (InstantiationException e) {
      System.err.println("Could not create the instance of " + imageClass);
    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      System.err.println("Unable to access the instance of " + className);
    }
    if (image != null) {
      if (opName == FileOperations.READ) {
        return image.readImage(commandParams[1]);
      } else {
        image.writeImage(commandParams[1], imageModel.IMAGES_PIXEL_MATRICES.get(commandParams[2]));
      }
    }
    return null;
  }

  /**
   * Extracts the split percentage from command parameters.
   *
   * @param commandParams The command parameters.
   * @return The split percentage.
   */
  private double getSplitPercentage(String[] commandParams) {
    double splitPercentage = 0.0;
    int indexToCheck = 4;
    if (indexToCheck < commandParams.length && commandParams[3].equals("split")) {
      double splitVal = Double.parseDouble(commandParams[indexToCheck]);
      if (splitVal > 0 && splitVal < 100) {
        splitPercentage = splitVal / 100;
      } else {
        System.err.println("The split percentage values are invalid. Please enter valid values.");
      }
    }
    return splitPercentage;
  }

  /**
   * Loads an image into the image model.
   *
   * @param commandParams The command parameters.
   */
  private void load(String[] commandParams) {
    // load images/koala.ppm koala
    Pixel[][] pixelMatrix = null;
    String imageFormat = commandParams[1].split("\\.")[1];
    boolean flag = false;
    for (ImageFormats f : ImageFormats.values()) {
      if (imageFormat.equalsIgnoreCase(f.toString())) {
        flag = true;
        break;
      }
    }
    if (flag) {
      pixelMatrix = fileOperation(imageFormat, FileOperations.READ, commandParams);
      imageModel.IMAGES_PIXEL_MATRICES.put(commandParams[2], pixelMatrix);
    } else {
      System.err.println(imageFormat + " image format is currently not supported. "
              + "Please load different type of image");
    }
  }

  /**
   * Runs a script containing image processing commands.
   *
   * @param filename The name of the script file containing image processing commands.
   */
  public void runScript(String filename) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(filename));
      String line;
      while ((line = br.readLine()) != null) {
        // skip comments and empty lines
        if (line.isEmpty() || line.charAt(0) == '#') {
          continue;
        }
        String[] commandParams = line.split(" ");
        commandsMap.get(commandParams[0]).run(commandParams);
      }
    } catch (FileNotFoundException e) {
      System.err.println("File " + filename + " not found!");
    } catch (IOException e) {
      System.err.println("Error while opening " + filename);
    }
  }

  /**
   * Represents the available file operations: READ and WRITE.
   */
  private enum FileOperations {
    READ, WRITE
  }

}
