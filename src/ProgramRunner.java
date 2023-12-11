import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import controller.ControllerInterface;
import controller.GUIController;
import controller.InputStreamController;
import model.image.ImageModel;
import view.ImageProcessingView;
import view.ViewInterface;

/**
 * The ProgramRunner class serves as the entry point for the Image Processing application.
 * It initializes the ImageProcessingController to manage image processing functionalities.
 */
public class ProgramRunner {

  /**
   * The main method that initializes the image processing application.
   * It creates an ImageProcessingController and starts the application.
   *
   * @param args The command-line arguments (not used in this application)
   */
  public static void main(String[] args) {
    // Initialize imageModel
    ImageModel imageModel = new ImageModel();

    // Initialize the ImageProcessingController to begin the image processing application
    ControllerInterface imageProcessor;

    // check if script file is provided as input to command line argument
    if (args.length == 2 && args[0].equalsIgnoreCase("-file")) {
      String command = "run " + args[1];
      // Use InputStreamController if a script file is provided
      imageProcessor = new InputStreamController(new InputStreamReader(
              new ByteArrayInputStream(
                      command.getBytes(StandardCharsets.UTF_8))));
    } else if (args.length == 1 && args[0].equalsIgnoreCase("-text")) {
      // Use InputStreamController with System.in if text input is specified
      imageProcessor = new InputStreamController(new InputStreamReader(System.in));
    } else if (args.length == 0) {
      // Use GUIController if no command-line arguments are provided
      imageProcessor = new GUIController();
      // Initialize the view for GUI
      ViewInterface view = new ImageProcessingView();
      imageProcessor.setView(view);
    } else {
      // Display an error message for invalid arguments
      System.err.println("Invalid arguments!\nPlease input correct parameters as per the syntax.");
      return;
    }

    // Initiate the image processing application
    imageProcessor.start(imageModel);
  }
}
