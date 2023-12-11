package view;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Component;
import javax.swing.SwingConstants;
import javax.swing.ScrollPaneConstants;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import javax.swing.JComboBox;
import javax.swing.JToggleButton;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

import controller.Features;
import model.image.Pixel;

/**
 * The ImageProcessingView class extends JFrame and implements the ViewInterface.
 * It represents the main GUI window for the image processing application. It provides a user
 * interface to open, process, and save images, applying various filters and displaying split and
 * original views of the images.
 */
public class ImageProcessingView extends JFrame implements ViewInterface {
  private BufferedImage originalImage;
  private BufferedImage histImage;
  private BufferedImage processedImage;
  private JPanel filterPanel;
  private JPanel imagePanel;
  private boolean isImageChanged = false;
  private JPanel imagePanelOriginal;
  private JPanel imagePanelProcessed;
  private JScrollPane scrollPaneOriginal;
  private JScrollPane scrollPaneProcessed;
  private JComboBox<String> filterComboBox;
  private File lastOpenedDirectory;
  private String fileExtension;
  private JToggleButton splitToggleButton;
  private JTextField splitPercentageField;
  private JButton openButton;
  private JButton saveButton;
  private JButton processButton;
  private JButton applyPreviewButton;
  private Features features;
  private boolean isOriginalImage = false;
  private String blackValue, midValue, whiteValue;

  /**
   * Constructs an instance of the ImageProcessingView.
   * Initializes the UI, components, layout, listeners, and sets main frame properties.
   */
  public ImageProcessingView() {
    initializeUI();
    initializeComponents();
    setupLayout();
    setupListeners();
    setMainFrameProperties();
  }

  /**
   * Initializes the UI of the main frame, setting the title and icon.
   */
  private void initializeUI() {
    setTitle("Image Processing App");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ImageIcon icon = new ImageIcon("res/icon.png");
    setIconImage(icon.getImage());
  }

  /**
   * Initializes various Swing components used in the application.
   */
  private void initializeComponents() {
    // Initialize and configure UI components
    splitToggleButton = new JToggleButton("Split Preview");
    applyPreviewButton = new JButton("Apply Filter");
    splitToggleButton.setEnabled(false);
    applyPreviewButton.setVisible(false);
    splitPercentageField = new JTextField("0");
    splitPercentageField.setEnabled(false);

    enableSplitToggleButton("LUMA_COMPONENT");

    openButton = new JButton("Open Image");
    saveButton = new JButton("Save Image");
    processButton = new JButton("Process Image");

    String[] options = new String[]{
        "LUMA_COMPONENT", "SEPIA", "BLUR", "SHARPEN", "VALUE_COMPONENT",
        "INTENSITY_COMPONENT", "RED_COMPONENT", "GREEN_COMPONENT",
        "BLUE_COMPONENT", "BRIGHTEN", "HORIZONTAL_FLIP", "VERTICAL_FLIP",
        "COLOR_CORRECT", "LEVELS_ADJUST", "IMAGE_COMPRESSION","DITHER"};
    filterComboBox = new JComboBox<>(options);
  }

  /**
   * Sets up the layout of the main frame, including filter panel, image panels, scroll panes,
   * and buttons.
   */
  private void setupLayout() {
    createFilterPanel();
    createImagePanels();
    setupScrollPane();
    createMainPanels();
    createButtonPanel();
  }

  /**
   * Sets up listeners for various UI components, such as buttons and combo boxes.
   */
  private void setupListeners() {
    // Listener for split toggle button
    splitToggleButton.addActionListener(e -> {
      if (originalImage == null) {
        showDialog("Please load an image before processing.", "errorMessage");
        return;
      }
      toggleSplitView();
      updateSplitView();
    });

    // Listener for filter combo box
    filterComboBox.addItemListener(e -> {
      if (e.getStateChange() == ItemEvent.SELECTED) {
        splitToggleButton.setSelected(false);
        processButton.setEnabled(true);
        resetValues();
        enableSplitToggleButton((String) e.getItem());
      }
    });

    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    // Listener for window closing
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        handleWindowClosing();
      }
    });

    // Listener for split percentage field
    splitPercentageField.addActionListener(e -> splitToggleButton.setEnabled(true));

    // Listener for apply preview button
    applyPreviewButton.addActionListener(e -> {
      // Update the split view filter to the entire image
      if (splitToggleButton.isSelected()) {
        splitPercentageField.setText("0");
        String inputFileName = (isImageModified() && !isOriginalImage) ? "image-filter" : "image";
        processedImage = applyFilter((String) filterComboBox.getSelectedItem(), inputFileName,
            "image-filter");
        histImage = applyFilter("HISTOGRAM", "image-filter",
            "image-hist");
        repaint();
      }
      applyPreviewButton.setEnabled(false);
    });
  }

  /**
   * Sets properties for the main frame, such as size and visibility.
   */
  private void setMainFrameProperties() {
    setSize(new Dimension(1200, 600));
    setLocationRelativeTo(null);
    setVisible(true);
  }

  /**
   * Creates the filter panel containing filter options, split view controls, and apply filter
   * button.
   */
  private void createFilterPanel() {
    filterPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(5, 5, 5, 5);

    filterPanel.add(new JLabel("Filter Options:"), gbc);
    gbc.gridx++;
    filterPanel.add(filterComboBox, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    processButton.addActionListener(e -> processImage());
    filterPanel.add(processButton, gbc);

    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;

    gbc.gridy++;
    gbc.gridx = 0;
    filterPanel.add(new JLabel("Split View Percentage:"), gbc);

    gbc.gridx++;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    filterPanel.add(splitPercentageField, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    gbc.gridwidth = 1;
    filterPanel.add(splitToggleButton, gbc);

    gbc.gridx++;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    filterPanel.add(applyPreviewButton, gbc);
  }

  /**
   * Creates image panels for displaying the original, processed, and histogram images.
   */
  private void createImagePanels() {
    imagePanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (processedImage != null) {
          g.drawImage(processedImage, 0, 0, null);
        }
      }
    };

    imagePanel.setPreferredSize(new Dimension(500, 600));

    imagePanelOriginal = new JPanel(new BorderLayout());
    imagePanelOriginal.setPreferredSize(new Dimension(500, 600));

    JLabel histogramLabel = new JLabel("Histogram");
    histogramLabel.setHorizontalAlignment(SwingConstants.CENTER);
    imagePanelOriginal.add(histogramLabel, BorderLayout.NORTH);

    imagePanelOriginal.add(new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (histImage != null) {
          Graphics2D g2d = (Graphics2D) g;
          int x = (getWidth() - histImage.getWidth()) / 2;
          g2d.drawImage(histImage, x, 0, null);
        }
      }
    }, BorderLayout.CENTER);

    imagePanelProcessed = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (processedImage != null) {
          g.drawImage(processedImage, 0, 0, null);
        }
      }
    };
  }

  /**
   * Creates scroll panes for the image panels.
   */
  private void setupScrollPane() {
    createScrollPane(imagePanel);
    scrollPaneOriginal = createScrollPane(imagePanelOriginal);
    scrollPaneProcessed = createScrollPane(imagePanelProcessed);
  }

  /**
   * Creates the main panels containing image displays, filter controls, and buttons.
   */
  private void createMainPanels() {
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BorderLayout());
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BorderLayout());
    rightPanel.add(scrollPaneOriginal, BorderLayout.CENTER);
    rightPanel.add(filterPanel, BorderLayout.SOUTH);
    topPanel.add(scrollPaneProcessed, BorderLayout.CENTER);
    topPanel.add(rightPanel, BorderLayout.EAST);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(1, 2));
    JButton openButton = new JButton("Open Image");
    openButton.addActionListener(e -> openImage());
    buttonPanel.add(openButton);

    JButton saveButton = new JButton("Save Image");
    saveButton.addActionListener(e -> saveImage());
    buttonPanel.add(saveButton);

    add(topPanel, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);
  }

  /**
   * Opens an image file using a file chooser dialog.
   */
  private void openImage() {
    if (originalImage != null && isImageModified()) {
      int userChoice = JOptionPane.showConfirmDialog(
          this,
          "Do you want to save the current image before opening a new one?",
          "Save Current Image",
          JOptionPane.YES_NO_CANCEL_OPTION
      );

      if (userChoice == JOptionPane.YES_OPTION) {
        saveImage();
      } else if (userChoice == JOptionPane.CANCEL_OPTION) {
        return;
      }
    }

    JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Image Files", "jpg", "ppm", "png");
    fileChooser.setFileFilter(filter);

    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

    int result = fileChooser.showOpenDialog(this);

    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      lastOpenedDirectory = selectedFile.getParentFile();

      try {
        String[] commandParams = {"load", selectedFile.toString(), "image"};
        features.load(commandParams);
        isOriginalImage = true;
        Pixel[][] pixelMatrix = features.getPixelMatrixForImage("image");
        originalImage = features.createImageFromPixels(pixelMatrix);
        fileExtension = getFileExtension(selectedFile.getName());
        processedImage = originalImage;
        isImageChanged = false;
        histImage = applyFilter("HISTOGRAM", "image",
            "image-hist");

        updateImagePanelPreferredSize(imagePanelOriginal, histImage);
        updateImagePanelPreferredSize(imagePanelProcessed, processedImage);
        setSize(new Dimension(1201, 600));
        repaint();
      } catch (Exception e) {
        showDialog("Unsupported image format.", "errorMessage");
      }
    }
  }

  /**
   * Creates the button panel containing open and save buttons.
   */
  private void createButtonPanel() {
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(1, 2));
    buttonPanel.add(openButton);
    buttonPanel.add(saveButton);
  }

  /**
   * Toggles the split view mode and updates the UI accordingly.
   */
  private void toggleSplitView() {
    if (splitToggleButton.isSelected()) {
      applyPreviewButton.setEnabled(true);
      processButton.setEnabled(false);
      splitToggleButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    } else {
      applyPreviewButton.setEnabled(false);
      processButton.setEnabled(true);
      splitToggleButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
    }
  }

  /**
   * Handles window closing event, prompting the user to save the image before exiting.
   */
  private void handleWindowClosing() {
    int userChoice = JOptionPane.showConfirmDialog(
        this,
        "Do you want to save the current image before exiting?",
        "Save Current Image",
        JOptionPane.YES_NO_CANCEL_OPTION
    );

    if (userChoice == JOptionPane.YES_OPTION) {
      saveImage();
    } else if (userChoice == JOptionPane.CANCEL_OPTION) {
      return;
    }
    System.exit(0);
  }

  /**
   * Creates a scroll pane for a given view component.
   *
   * @param view The component to be displayed in the scroll pane.
   * @return A JScrollPane containing the specified view component.
   */
  private JScrollPane createScrollPane(Component view) {
    JScrollPane scrollPane = new JScrollPane(view);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    return scrollPane;
  }

  /**
   * Updates the preferred size of the given image panel based on the dimensions of the image.
   *
   * @param imagePanel The panel containing the image.
   * @param image      The image to be displayed.
   */
  private void updateImagePanelPreferredSize(JPanel imagePanel, BufferedImage image) {
    if (image != null) {
      Dimension imageSize = new Dimension(image.getWidth(), image.getHeight());
      imagePanel.setPreferredSize(imageSize);
    } else {
      imagePanel.setPreferredSize(new Dimension(0, 0));
    }
  }

  /**
   * Checks if the original image has been modified.
   *
   * @return True if the original image has been modified; false otherwise.
   */
  private boolean isImageModified() {
    return !originalImage.equals(processedImage);
  }

  /**
   * Saves the processed image to a file using a file chooser dialog.
   */
  private void saveImage() {
    if (processedImage != null) {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setDialogTitle("Save Image");

      if (lastOpenedDirectory != null) {
        fileChooser.setCurrentDirectory(lastOpenedDirectory);
      }

      int userSelection = fileChooser.showSaveDialog(this);

      if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();
        String originalFileExtension = fileExtension;
        if (!originalFileExtension.isEmpty()) {
          fileToSave = new File(fileToSave.getParent(), fileToSave.getName() + "."
              + originalFileExtension);
        }

        try {
          String[] commandParams = {"save", fileToSave.toString(), "image-filter"};
          features.save(commandParams);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Retrieves the file extension from a given file name.
   *
   * @param fileName The name of the file.
   * @return The file extension or an empty string if not found.
   */
  private String getFileExtension(String fileName) {
    int dotIndex = fileName.lastIndexOf('.');
    if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
      return fileName.substring(dotIndex + 1);
    }
    return "";
  }

  /**
   * Processes the image based on the selected filter and updates the display.
   */
  private void processImage() {
    isImageChanged = true;
    if (originalImage == null) {
      showDialog("Please load an image before processing.", "errorMessage");
      return;
    }

    if (splitToggleButton.isSelected()) {
      Double splitVal = Double.parseDouble(splitPercentageField.getText());
      if (splitVal != 0 && (splitVal < 0 || splitVal > 100)) {
        showDialog("Please enter split percentage value between 0 and 100.",
            "errorMessage");
        return;
      }
    }

    String selectedFilter = (String) filterComboBox.getSelectedItem();
    String inputFileName = (isImageModified() && !isOriginalImage) ? "image-filter" : "image";
    processedImage = applyFilter(selectedFilter, inputFileName, "image-filter");
    repaint();
    histImage = applyFilter("HISTOGRAM", "image-filter",
        "image-hist");
    repaint();
    isOriginalImage = false;
  }

  /**
   * Displays a dialog with the specified message and dialog type.
   *
   * @param message    The message to be displayed in the dialog.
   * @param dialogType The type of dialog, either "errorMessage" or "inputMessage".
   * @return The user input if the dialog type is "inputMessage"; otherwise, null.
   */
  private String showDialog(String message, String dialogType) {
    switch (dialogType) {
      case "errorMessage":
        JOptionPane.showMessageDialog(this, message,
            "Invalid Input", JOptionPane.WARNING_MESSAGE);
        return null;
      case "inputMessage":
        return JOptionPane.showInputDialog(this, message);
      default:
        return null;
    }
  }

  /**
   * Gets the split percentage from the user input.
   *
   * @return The split percentage as a double.
   */
  private Double getSplitPercentage() {
    Double splitPercentage = 0.0;
    if (splitToggleButton.isSelected()) {
      splitPercentage = (Double.parseDouble(splitPercentageField.getText()) != 0)
          ? Double.parseDouble(splitPercentageField.getText()) : 0.0;
    }

    if (splitPercentage > 0 && splitPercentage < 100) {
      splitPercentage = splitPercentage / 100;
    }
    return splitPercentage;
  }

  /**
   * Opens an input dialog box with the specified message and retrieves user input.
   *
   * @param message The message to be displayed in the input dialog.
   * @return The user input as a string.
   */
  private String openDialogBox(String message) {
    String userInput;
    do {
      userInput = showDialog(message, "inputMessage");
      if (userInput == null || userInput.trim().isEmpty()) {
        showDialog("Please enter a valid value.", "errorMessage");
      }
    }
    while (userInput == null || userInput.trim().isEmpty());
    return userInput;
  }

  /**
   * Applies the selected filter to the image and returns the processed image.
   *
   * @param filter         The selected filter.
   * @param inputFileName  The name of the input image file.
   * @param outputFileName The name of the output image file.
   * @return The processed image.
   */
  private BufferedImage applyFilter(String filter, String inputFileName, String outputFileName) {
    Pixel[][] pixelMatrix;
    String userInput;
    Double splitPercentage = getSplitPercentage();
    boolean checkValid;
    switch (filter) {
      case "BRIGHTEN":
          do {
            userInput = openDialogBox("Enter the brighten/darker value:");
            if ((Integer.parseInt(userInput) < -255 || Integer.parseInt(userInput) > 255)) {
              showDialog("Please enter values between -255 and 255", "errorMessage");
            }
          } while (Integer.parseInt(userInput) < -255 || Integer.parseInt(userInput) > 255);

          features.processImage(inputFileName, outputFileName, filter,
            Integer.parseInt(userInput.split("\\.")[0]));
          pixelMatrix = features.getPixelMatrixForImage(outputFileName);
          return features.createImageFromPixels(pixelMatrix);
      case "IMAGE_COMPRESSION":
          do {
            userInput = openDialogBox("Enter the compression factor:");
            if ((Integer.parseInt(userInput) < 0 || Integer.parseInt(userInput) > 100)) {
              showDialog("Please enter values between 0 and 100", "errorMessage");
            }
          } while (Integer.parseInt(userInput) < 0 || Integer.parseInt(userInput) > 100);

          features.processImage(inputFileName, outputFileName, filter,
            Integer.parseInt(userInput.split("\\.")[0]));
          pixelMatrix = features.getPixelMatrixForImage(outputFileName);
          return features.createImageFromPixels(pixelMatrix);
      case "LEVELS_ADJUST":

        if (checkValuesEmpty(blackValue, midValue, whiteValue)) {
          do {
            blackValue = openDialogBox("Enter black value:");
            midValue = openDialogBox("Enter mid value:");
            whiteValue = openDialogBox("Enter white value:");
            checkValid = checkValidInput(blackValue, midValue, whiteValue);
          }
          while (checkValid);
        }
        features.processImage(inputFileName, outputFileName, filter,
            blackValue,
            midValue,
            whiteValue, splitPercentage);

        pixelMatrix = features.getPixelMatrixForImage(outputFileName);
        return features.createImageFromPixels(pixelMatrix);
      default:
        features.processImage(inputFileName, outputFileName, filter,
            splitPercentage);
        pixelMatrix = features.getPixelMatrixForImage(outputFileName);
        return features.createImageFromPixels(pixelMatrix);
    }
  }

  /**
   * Checks the values of input parameters are empty or not.
   *
   * @param values parameters of the levels adjust filter.
   */
  private boolean checkValuesEmpty(String ...values) {
    boolean isEmpty = false;
    for(String val : values) {
      isEmpty = (Objects.equals(val, ""));
    }
    return isEmpty;
  }

  /**
   * Resets the values of input parameters.
   */
  private void resetValues() {
    blackValue = "";
    whiteValue = "";
    midValue = "";
  }

  /**
   * Checks the validity of input parameters for the levels adjust filter.
   *
   * @param otherParams Additional parameters for the levels adjust filter.
   */
  private boolean checkValidInput(String... otherParams) {
    boolean isNotValid = false;
    int b = Integer.parseInt((String) otherParams[0]);
    int m = Integer.parseInt((String) otherParams[1]);
    int w = Integer.parseInt((String) otherParams[2]);
    if (b < 0 || b > 255 || m < 0 || m > 255 || w < 0 || w > 255) {
      isNotValid = true;
      showDialog("Params going out of bounds for B, M, or W.", "errorMessage");
    }
    if (!(b <= m && m <= w)) {
      isNotValid = true;
      showDialog("Value of B, M, and W should be in increasing order!",
          "errorMessage");
    }
    return isNotValid;
  }

  /**
   * Enables or disables the split view controls based on the selected filter.
   *
   * @param selectedFilter The selected filter.
   */
  private void enableSplitToggleButton(String selectedFilter) {
    splitPercentageField.setText("0");

    String[] filtersToEnableSplitButton = {"SEPIA", "BLUR", "LUMA_COMPONENT", "SHARPEN", "LEVELS_ADJUST","COLOR_CORRECT","DITHER"};
    boolean enableView = false;

    for (String filter : filtersToEnableSplitButton) {
      if (filter.equals(selectedFilter)) {
        enableView = true;
        break;
      }
    }

    splitToggleButton.setEnabled(enableView);
    applyPreviewButton.setVisible(enableView);
    applyPreviewButton.setEnabled(false);
    splitPercentageField.setEnabled(enableView);
  }

  /**
   * Updates the split view based on the specified split percentage.
   */
  private void updateSplitView() {
    splitToggleButton.setEnabled(true);
    int splitVal = Integer.parseInt(splitPercentageField.getText());
    if (splitVal > 0 && splitVal < 100) {
      // Apply the filter for split view
      String selectedFilter = (String) filterComboBox.getSelectedItem();
      String inputFileName = (isImageModified() && isImageChanged) ? "image-filter" : "image";
      isOriginalImage = (inputFileName.equals("image"));
      processedImage = applyFilter(selectedFilter, inputFileName, "image-split");
      histImage = applyFilter("HISTOGRAM", "image-split",
          "image-hist");
      repaint();
    } else {
      showDialog("Please enter split percentage value between 0 and 100.",
          "errorMessage");
    }
  }

  /**
   * Overrides the method to add features to the view.
   *
   * @param features The features to be added to the view.
   */
  @Override
  public void addFeatures(Features features) {
    this.features = features;
  }
}