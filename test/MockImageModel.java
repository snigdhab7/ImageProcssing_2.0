import model.image.ImageModelInterface;
import model.imagetransformation.Methods;

/**
 * The MockImageModel class is a mock implementation of the ImageModelInterface for
 * testing purposes.
 * It logs information about image processing operations for verification in testing scenarios.
 */
public class MockImageModel implements ImageModelInterface {
  
  /**
   * A StringBuilder used for logging information about image processing operations.
   */
  private StringBuilder log;

  /**
   * Constructs a MockImageModel instance with the specified StringBuilder for logging.
   *
   * @param log The StringBuilder used for logging information.
   */
  public MockImageModel(StringBuilder log) {
    this.log = log;
  }

  /**
   * Processes the image using the specified method and logs the operation details.
   *
   * @param inputFileName  The name of the input image file.
   * @param outputFileName The name of the output image file.
   * @param method         The image processing method to be applied.
   * @param otherParams    Additional parameters that may be passed.
   */
  @Override
  public void processImage(String inputFileName, String outputFileName,
                           Methods method, Object... otherParams) {
    log.append("inputFileName: " + inputFileName + " outputFileName: "
            + outputFileName + " method: " + method);
  }
}
