package view;

import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JDialog;

import controller.Features;
import model.image.Pixel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * JUnit test class for the ImageProcessingView class.
 */
public class ImageProcessingViewTest {

//  private ImageProcessingView view;
//  private FeaturesMock featuresMock;
//
//  @Before
//  public void setUp() {
//    view = new ImageProcessingView();
//    featuresMock = new FeaturesMock();
//    view.addFeatures(featuresMock);
//  }
//
//  @Test
//  public void testFilterSelection() {
//    // Select each filter from the combo box and check if corresponding actions are triggered
//    for (int i = 0; i < view.filterComboBox.getModel().getSize(); i++) {
//      view.filterComboBox.setSelectedIndex(i);
//      assertEquals(view.filterComboBox.getModel().getElementAt(i),
//          view.filterComboBox.getSelectedItem());
//    }
//  }
//
//  @Test
//  public void testImageProcessingViewInitialization() {
//    assertNotNull(view);
//    assertFalse(view.isOriginalImage);
//    assertNotNull(view.splitToggleButton);
//    assertFalse(view.splitToggleButton.isSelected());
//    assertTrue(view.applyPreviewButton.isEnabled());
//  }
//
//  @Test
//  public void testOpenImage() {
//    File imageFile = new File("test.jpg");
//    int[][][] expectedPixelMatrix = {
//        {{198, 182, 166}, {248, 232, 216}, {216, 197, 180},},
//        {{239, 223, 207}, {255, 252, 236}, {255, 249, 232},},
//        {{175, 158, 128}, {252, 235, 205}, {217, 198, 166}}};
//    featuresMock.setPixelMatrixForTesting(expectedPixelMatrix);
//
//    view.openImage();
//    assertNotNull(view.originalImage);
//    assertNotNull(view.processedImage);
//    assertNotNull(view.histImage);
//    assertFalse(view.splitToggleButton.isEnabled());
//    assertFalse(view.splitToggleButton.isSelected());
//    assertFalse(view.splitPercentageField.isEnabled());
//  }
//
//  @Test
//  public void testShowWarningDialogOnLoadError() {
//    // Mocking a non-existing file for the load operation
//    String nonExistingFileName = "nonExistingFile.jpg";
//    String[] loadCommandParams = {"load", nonExistingFileName, "image"};
//
//    // Mocking the Features.load method to throw an exception when trying to load non-existing file
//    featuresMock.setLoadException(new RuntimeException("File not found"));
//
//    // Creating a custom ActionListener for the openButton
//    ActionListener customOpenButtonListener = new ActionListener() {
//      @Override
//      public void actionPerformed(ActionEvent e) {
//        // Invoke the load operation, which will throw an exception in this case
//        try {
//          featuresMock.load(loadCommandParams);
//        } catch (Exception ex) {
//          System.err.println(ex);
//        }
//      }
//    };
//
//    // Setting the custom ActionListener for the openButton
//    view.openButton.addActionListener(customOpenButtonListener);
//
//    // Triggering the openButton click
//    customOpenButtonListener.actionPerformed(new ActionEvent(view.openButton,
//        ActionEvent.ACTION_PERFORMED, null));
//
//    // Verifying that the warning dialog is displayed
//    boolean warningDialogDisplayed = false;
//    JDialog[] dialogs = new JDialog[0];
//    for (JDialog dialog : dialogs) {
//      if (dialog.getTitle().equals("Invalid Input")) {
//        warningDialogDisplayed = true;
//        break;
//      }
//    }
//
//    // Asserting that the warning dialog is displayed
//    assertFalse("Warning dialog not displayed as expected.", warningDialogDisplayed);
//  }
//
//  @Test
//  public void testImageProcessing() {
//    view.originalImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//
//    // Select each filter from the combo box and check if corresponding actions are triggered
//    // Test each filter individually
//    for (int i = 0; i < view.filterComboBox.getModel().getSize(); i++) {
//      view.filterComboBox.setSelectedIndex(i);
//      view.processImage();
//      assertNotNull(view.processedImage);
//      assertEquals(view.filterComboBox.getModel().getElementAt(i),
//          view.filterComboBox.getSelectedItem());
//    }
//  }
//
//  @Test
//  public void testSaveImage() {
//    // Mock an image
//    view.originalImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//
//    view.processedImage = view.originalImage;
//    view.fileExtension = ".jpg";
//    // Save the image
//    File savedFile = new File("test.jpg");
//    view.saveImage();
//
//    // Check if the saved file is created and has the correct content
//    assertFalse(savedFile.exists());
//  }
//
//  @Test
//  public void testNonExistingImage() {
//    // Mock an image
//    view.originalImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//
//    // Save the image
//    File savedFile = new File("test.jpg");
//    view.saveImage();
//
//    // Check if the saved file is created and has the correct content
//    assertFalse(savedFile.exists());
//  }
//
//  @Test
//  public void testSplitViewFunctionality() {
//    view.originalImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//    view.processedImage = view.originalImage;
//    view.filterComboBox.setSelectedIndex(3);
//    view.toggleSplitView();
//    view.splitToggleButton.setSelected(true);
//    assertTrue(view.splitToggleButton.isSelected());
//    assertFalse(view.applyPreviewButton.isEnabled());
//
//    // Set a split percentage
//    view.splitPercentageField.setText("50");
//    view.updateSplitView();
//
//    // Add assertions for split view updates
//    assertTrue(view.splitToggleButton.isEnabled());
//    assertNotNull(view.processedImage);
//    assertNotNull(view.histImage);
//  }
//
//  @Test
//  public void testSplitViewFunctionalityInvalidValue() {
//    view.originalImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//    view.processedImage = view.originalImage;
//    view.filterComboBox.setSelectedIndex(3);
//    view.toggleSplitView();
//    view.splitToggleButton.setSelected(true);
//    assertTrue(view.splitToggleButton.isSelected());
//    assertFalse(view.applyPreviewButton.isEnabled());
//
//    // Set a valid split percentage
//    view.splitPercentageField.setText("50");
//    view.updateSplitView();
//
//    // Assertions for valid split percentage
//    assertTrue(view.splitToggleButton.isEnabled());
//    assertNotNull(view.processedImage);
//    assertNotNull(view.histImage);
//
//    // Set an invalid split percentage (less than 0)
//    view.splitPercentageField.setText("-10");
//    view.updateSplitView();
//
//    // Assertions for invalid split percentage
//    assertTrue(view.splitToggleButton.isEnabled());
//    assertNotNull(view.processedImage);
//    assertNotNull(view.histImage);
//
//    // Set an invalid split percentage (greater than 100)
//    view.splitPercentageField.setText("120");
//    view.updateSplitView();
//
//    // Assertions for invalid split percentage
//    assertTrue(view.splitToggleButton.isEnabled());
//    assertNotNull(view.processedImage);
//    assertNotNull(view.histImage);
//
//    // Set a valid split percentage again
//    view.splitPercentageField.setText("25");
//    view.updateSplitView();
//
//    // Assertions for valid split percentage
//    assertTrue(view.splitToggleButton.isEnabled());
//    assertNotNull(view.processedImage);
//    assertNotNull(view.histImage);
//  }
//
//  @Test
//  public void testImageCompressionFunctionality() {
//    view.originalImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//    view.processedImage = view.originalImage;
//    view.filterComboBox.setSelectedIndex(15); // Assuming "IMAGE_COMPRESSION" filter is at index 15
//
//    // Set a valid compression value
//    view.openButton.setEnabled(false); // To avoid triggering actual file open
//    view.saveButton.setEnabled(false); // To avoid triggering actual file save
//    view.splitToggleButton.setEnabled(false); // To avoid triggering split view
//
//    view.splitToggleButton.setSelected(false); // Ensure split view is disabled
//    view.splitPercentageField.setText("0"); // Ensure split percentage is zero
//
//    // Mocking the user input for compression value
//    String mockCompressionInput = "50";
//    featuresMock.setCompressionInput(mockCompressionInput);
//
//    // Set a valid compression value (50)
//    view.applyPreviewButton.setEnabled(true); // Enable applyPreviewButton for testing
//    view.applyPreviewButton.doClick(); // Simulate clicking the "Apply Filter" button
//
//    // Assertions for valid compression value
//    assertNotNull(view.processedImage);
//
//    // Set an invalid compression value (less than 0)
//    featuresMock.setCompressionInput("-10");
//    view.applyPreviewButton.setEnabled(true); // Enable applyPreviewButton for testing
//    view.applyPreviewButton.doClick(); // Simulate clicking the "Apply Filter" button
//    assertNotNull(view.processedImage);
//
//    // Set an invalid compression value (greater than 100)
//    featuresMock.setCompressionInput("120");
//    view.applyPreviewButton.setEnabled(true); // Enable applyPreviewButton for testing
//    view.applyPreviewButton.doClick(); // Simulate clicking the "Apply Filter" button
//    assertNotNull(view.processedImage);
//  }
//
//  @Test
//  public void testLevelsAdjustFunctionality() {
//    view.originalImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//    view.processedImage = view.originalImage;
//    view.filterComboBox.setSelectedIndex(15); // Assuming "LEVELS_ADJUST" filter is at index 15
//
//    // Set valid input values for levels adjust
//    view.openButton.setEnabled(false); // To avoid triggering actual file open
//    view.saveButton.setEnabled(false); // To avoid triggering actual file save
//    view.splitToggleButton.setEnabled(false); // To avoid triggering split view
//
//    view.splitToggleButton.setSelected(false); // Ensure split view is disabled
//    view.splitPercentageField.setText("0"); // Ensure split percentage is zero
//
//    // Mocking the user input for levels adjust values
//    String mockBlackValue = "10";
//    String mockMidValue = "50";
//    String mockWhiteValue = "90";
//    featuresMock.setLevelsAdjustInput(mockBlackValue, mockMidValue, mockWhiteValue);
//
//    // Set valid levels adjust values
//    view.applyPreviewButton.setEnabled(true); // Enable applyPreviewButton for testing
//    view.applyPreviewButton.doClick(); // Simulate clicking the "Apply Filter" button
//
//    // Assertions for valid levels adjust values
//    assertNotNull(view.processedImage);
//
//    // Set invalid levels adjust values (e.g., black value greater than white value)
//    featuresMock.setLevelsAdjustInput("100", "50", "10");
//    view.applyPreviewButton.setEnabled(true); // Enable applyPreviewButton for testing
//    view.applyPreviewButton.doClick(); // Simulate clicking the "Apply Filter" button
//    assertNotNull(view.processedImage);
//
//    // Set invalid levels adjust values (e.g., values out of range)
//    featuresMock.setLevelsAdjustInput("-10", "50", "110");
//    view.applyPreviewButton.setEnabled(true); // Enable applyPreviewButton for testing
//    view.applyPreviewButton.doClick(); // Simulate clicking the "Apply Filter" button
//    assertNotNull(view.processedImage);
//  }
//
//  @Test
//  public void testBrightenFunctionality() {
//    view.originalImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//    view.processedImage = view.originalImage;
//    view.filterComboBox.setSelectedIndex(10); // Assuming "BRIGHTEN" filter is at index 10
//
//    // Set valid input value for brighten
//    view.openButton.setEnabled(false); // To avoid triggering actual file open
//    view.saveButton.setEnabled(false); // To avoid triggering actual file save
//    view.splitToggleButton.setEnabled(false); // To avoid triggering split view
//
//    view.splitToggleButton.setSelected(false); // Ensure split view is disabled
//    view.splitPercentageField.setText("0"); // Ensure split percentage is zero
//
//    // Mocking the user input for brighten value
//    String mockBrightenValue = "30";
//    featuresMock.setBrightenInput(mockBrightenValue);
//
//    // Set valid brighten value
//    view.applyPreviewButton.setEnabled(true); // Enable applyPreviewButton for testing
//    view.applyPreviewButton.doClick(); // Simulate clicking the "Apply Filter" button
//
//    // Assertions for valid brighten value
//    assertNotNull(view.processedImage);
//
//    // Set invalid brighten value (greater than 100)
//    featuresMock.setBrightenInput("120");
//    view.applyPreviewButton.setEnabled(true); // Enable applyPreviewButton for testing
//    view.applyPreviewButton.doClick(); // Simulate clicking the "Apply Filter" button
//    assertNotNull(view.processedImage);
//
//    // Set invalid brighten value (negative value)
//    featuresMock.setBrightenInput("-30");
//    view.applyPreviewButton.setEnabled(true); // Enable applyPreviewButton for testing
//    view.applyPreviewButton.doClick(); // Simulate clicking the "Apply Filter" button
//    assertNotNull(view.processedImage);
//  }
//
//  @Test
//  public void testApplyFilterButton() {
//    view.originalImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//
//    // Select a filter
//    view.filterComboBox.setSelectedItem("BLUR");
//
//    // Click on the "Apply Filter" button
//    view.processImage();
//
//    // Check if the image is processed accordingly
//    assertNotNull(view.processedImage);
//  }
//
//  @Test
//  public void testToggleSplitViewNoImageLoaded() {
//    view.toggleSplitView();
//    assertFalse(view.splitToggleButton.isSelected());
//    assertFalse(view.applyPreviewButton.isEnabled());
//  }
//
//  /**
//   * Test opening an image and then toggling the split view.
//   */
//  @Test
//  public void testToggleSplitViewWithImage() {
//    view.originalImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//    view.openImage();
//    view.toggleSplitView();
//    assertTrue(view.splitToggleButton.isSelected());
//    assertTrue(view.applyPreviewButton.isEnabled());
//  }
//
//  /**
//   * Test updating split view with an invalid split percentage.
//   */
//  @Test
//  public void testUpdateSplitViewInvalidPercentage() {
//    view.originalImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//    view.openImage();
//    view.splitPercentageField.setText("abc"); // Invalid percentage
//    view.updateSplitView();
//    assertFalse(view.splitToggleButton.isEnabled());
//    assertNotNull(view.processedImage);
//    assertNotNull(view.histImage);
//  }
//
//  /**
//   * Test closing the application without unsaved changes.
//   */
//  @Test
//  public void testWindowClosingWithoutUnsavedChanges() {
//    view.handleWindowClosing();
//    assertFalse(featuresMock.isSaveCalled);
//  }
//
//  /**
//   * Test saving an image with a valid file name.
//   */
//  @Test
//  public void testSaveImageValidFileName() {
//    view.originalImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//    File savedFile = new File("validFileName.jpg");
//    view.saveImage();
//    assertTrue(savedFile.exists());
//  }
//
//  /**
//   * Test saving an image with an invalid file name.
//   */
//  @Test
//  public void testSaveImageInvalidFileName() {
//    view.originalImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//    File savedFile = new File("/invalid/path/invalidFileName.jpg"); // Invalid file path
//    view.saveImage();
//    assertFalse(savedFile.exists());
//  }
//
//  /**
//   * Test processing an image with an invalid filter.
//   */
//  @Test
//  public void testProcessImageInvalidFilter() {
//    view.originalImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//    view.filterComboBox.setSelectedItem("INVALID_FILTER");
//    view.processImage();
//    assertNull(view.processedImage); // Ensure no image processing occurs
//  }
//
//  /**
//   * Test updating split view with a valid split percentage.
//   */
//  @Test
//  public void testUpdateSplitViewValidPercentage() {
//    view.originalImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//    view.openImage();
//    view.splitPercentageField.setText("30"); // Valid percentage
//    view.updateSplitView();
//    assertTrue(view.splitToggleButton.isEnabled());
//    assertNotNull(view.processedImage);
//    assertNotNull(view.histImage);
//  }
//
//  /**
//   * Test closing the application with unsaved changes and canceling the save dialog.
//   */
//  @Test
//  public void testWindowClosingWithUnsavedChangesCanceled() {
//    view.originalImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//    featuresMock.setSaveConfirmation(false); // Simulate canceling the save dialog
//    view.handleWindowClosing();
//    assertTrue(featuresMock.isSaveCalled);
//  }
//
//  /**
//   * Test applying a filter with an invalid image.
//   */
//  @Test
//  public void testApplyFilterInvalidImage() {
//    view.originalImage = null;
//    view.filterComboBox.setSelectedItem("BLUR");
//    view.processImage();
//    assertNull(view.processedImage);
//  }
//
//
//  // Mock class for Features interface
//  private class FeaturesMock implements Features {
//    private boolean isSaveCalled;
//    private int[][][] pixelMatrixForTesting;
//    private String userInputForTesting;
//    private String userInput1ForTesting;
//    private String userInput2ForTesting;
//    private String userInput3ForTesting;
//
//    @Override
//    public void load(String[] commandParams) {
//      return;
//    }
//
//    @Override
//    public Pixel[][] getPixelMatrixForImage(String imageName) {
//      return new Pixel[1][1];
//    }
//
//    @Override
//    public BufferedImage createImageFromPixels(Pixel[][] pixelMatrix) {
//      return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
//    }
//
//    @Override
//    public void processImage(String inputFileName, String outputFileName, String filter,
//                             Object... params) {
//      return;
//    }
//
//    @Override
//    public void save(String[] commandParams) {
//      isSaveCalled = true;
//    }
//
//    public void setPixelMatrixForTesting(int[][][] pixelMatrix) {
//      // Set pixel matrix for testing
//      this.pixelMatrixForTesting = pixelMatrix;
//    }
//
//    public void setLoadException(RuntimeException fileNotFound) {
//      System.err.println(fileNotFound);
//    }
//
//    public void setCompressionInput(String mockCompressionInput) {
//      userInputForTesting = mockCompressionInput;
//    }
//
//    public void setLevelsAdjustInput(String number1, String number2, String number3) {
//      userInput1ForTesting = number1;
//      userInput2ForTesting = number2;
//      userInput3ForTesting = number3;
//    }
//
//    public void setBrightenInput(String number) {
//      userInputForTesting = number;
//    }
//  }
}
