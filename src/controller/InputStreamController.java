package controller;

import java.util.Scanner;

import model.image.ImageModelInterface;
import view.ViewInterface;

/**
 * The InputStreamController class manages the operations and functionalities of the image
 * processing application with input from user as InputStream.
 * It includes methods to execute image processing commands and run scripts.
 */
public class InputStreamController implements ControllerInterface {
  private ImageModelInterface imageModel;
  private ImageProcessingCommandHelper imageProcessingCommandHelper;
  private final Readable in;

  public InputStreamController(Readable in) {
    this.in = in;
  }

  public InputStreamController() {
    this.in = null;
  }

  /**
   * Executes an image processing command based on the provided string command.
   *
   * @param command The image processing command to execute.
   * @throws IllegalArgumentException if an invalid command or parameter is provided.
   */
  public void executeCommand(String command) throws IllegalArgumentException {
    try {
      if (imageProcessingCommandHelper == null) {
        imageProcessingCommandHelper = new ImageProcessingCommandHelper();
        imageProcessingCommandHelper.create(imageModel);
      }
      String[] commandParams = command.split(" ");
      imageProcessingCommandHelper.commandsMap.get(commandParams[0]).run(commandParams);
    } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
      System.err.println("The command you entered is syntactically not correct. "
              + "Please enter the correct command : " + command);
    }
  }

  /**
   * Initiates and controls the execution of image processing commands.
   */
  public void start(ImageModelInterface imageModel) {
    this.imageModel = imageModel;
    Scanner sc = new Scanner(this.in);
    while (sc.hasNextLine()) {
      String command = sc.nextLine();
      if (command.isEmpty() || command.charAt(0) == '#') {
        continue;
      }
      if (command.equals("exit")) {
        break;
      }
      executeCommand(command);
    }
  }

  @Override
  public void setView(ViewInterface view) {
    // Do nothing as this controller has a text based user interface which does not require
    // any setup.
  }
}
