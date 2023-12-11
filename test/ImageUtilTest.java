import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

import controller.ImageIOHelper;
import controller.ImageProcessingCommandHelper;
import controller.InputStreamController;
import model.image.ImageModel;
import model.image.JPGImage;
import model.image.PNGImage;
import model.image.PPMImage;
import model.image.Pixel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Junit class for testing Image Util Class.
 */

public class ImageUtilTest {

  private PNGImage pngImage;
  private JPGImage jpgImage;
  private PPMImage ppmImage;
  private BufferedImage testImage;
  private BufferedImage testImageJPG;
  private InputStreamController inputStreamController;

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void restoreStreams() {
    System.setOut(System.out);
    System.setErr(System.err);
  }

  @Before
  public void setUp() throws IOException {
    inputStreamController = new InputStreamController(new InputStreamReader(System.in));
    pngImage = new PNGImage();
    jpgImage = new JPGImage();
    ppmImage = new PPMImage();
    testImage = ImageIO.read(new File("./res/test.png"));
    testImageJPG = ImageIO.read(new File("./res/test.jpg"));
  }

  @Test
  public void testController() {
    try {
      // test controller with FileInputStream as the data source
      InputStreamController controller =
              new InputStreamController(
                      new InputStreamReader(new FileInputStream("res/script3.txt")));
      controller.start(new ImageModel());
    } catch (FileNotFoundException e) {
      System.out.println(e.toString());
      fail("Failed to read from a different data source!");
    }
  }

  @Test
  public void testCotrollerStart() {
    StringBuilder log = new StringBuilder();
    String exampleCommand = "load res/panda.jpg panda\nred-component panda panda-red";

    new InputStreamController(
            new InputStreamReader(
                    new ByteArrayInputStream(
                            exampleCommand.getBytes(StandardCharsets.UTF_8))))
            .start(new MockImageModel(log));
    assertEquals("inputFileName: panda outputFileName: panda-red method: RED_COMPONENT",
            log.toString());
  }

  @Test
  public void testReadImageFile() {
    PNGImage pngImage = new PNGImage();
    String filePath = "./res/dog.png";
    Pixel[][] image = pngImage.readImage(filePath);

    assertNotNull("Image should not be null", image);
  }

  @Test
  public void testInvalidFormat() {
    PNGImage pngImage = new PNGImage();
    String filePath = "./res/dog.mwb";
    Pixel[][] image = pngImage.readImage(filePath);

    assertNull("Image should not be null", image);
  }

  @Test
  public void testReadNonExistentImageFile() {
    PNGImage pngImage = new PNGImage();
    String filePath = "./res/dog.jpg";
    Pixel[][] image = pngImage.readImage(filePath);

    assertNull("Image should be null for nonexistent file", image);
  }

  @Test
  public void testWriteImageFileExists() {
    String outputPath = "./res/dog.png";
    Pixel[][] test = new Pixel[3][3];
    for (int i = 0; i < test.length; i++) {
      for (int j = 0; j < test.length; j++) {
        test[i][j] = new Pixel(1, 2, 4);
      }
    }
    pngImage.writeImage(outputPath, test);
    File outputFile = new File(outputPath);
    assertTrue("Output file should exist", outputFile.exists());
  }

  @Test
  public void testWrongCommand() {
    try {
      inputStreamController.executeCommand("loadTotal");
      inputStreamController.executeCommand("save res/test-mixed-ppm-jpg.jpg test");
    } catch (Exception e) {
      fail("The command you entered is not available.\n");
    }
  }

  @Test
  public void testIllegalCommandArgs() {
    try {
      inputStreamController.executeCommand("load");
      inputStreamController.executeCommand("save res/test-mixed-ppm-jpg.jpg test");
    } catch (IllegalArgumentException e) {
      fail("The command you entered is syntactically not correct. "
              + "Please enter the correct command :");
    }
  }

  @Test
  public void testRunScriptFailFileNotFound() {
    try {
      ImageProcessingCommandHelper imageProcessingCommandHelper =
              new ImageProcessingCommandHelper();
      imageProcessingCommandHelper.runScript("load");
      inputStreamController.executeCommand("save res/test-mixed-ppm-jpg.jpg test");

    } catch (Exception e) {
      fail("File load not found!");
    }
  }

  @Test
  public void testControllerToModel() {

    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("greyscale test test-gs");
      inputStreamController.executeCommand("save res/test-gs.png test-gs");

      String gsOutput = "./res/test-gs.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{152, 152, 152}, {138, 138, 138}, {170, 170, 170},},
              {{164, 164, 164}, {162, 162, 162}, {191, 191, 191},},
              {{129, 129, 129}, {138, 138, 138}, {163, 163, 163}}
      };


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1]
                          + " " + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }


  @Test
  public void testWrittenGrayScaleFile() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("value-component test flower-greyscale");
      inputStreamController.executeCommand("save res/test-gs.png flower-greyscale");
      String gsOutput = "./res/test-gs.png";
      File gsOutputFile = new File(gsOutput);
      assertTrue("Output file should exist", gsOutputFile.exists());

      // Compare image content pixel by pixel
      for (int y = 0; y < testImage.getHeight(); y++) {
        for (int x = 0; x < testImage.getWidth(); x++) {
          assertEquals("Pixel content should match", testImage.getRGB(x, y),
                  writtenImage.getRGB(x, y));
        }
      }
    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenLoadSavePixelPNG() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("save res/test-same.png test");

      String gsOutput = "./res/test-same.png";
      String gsInput = "./res/test.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      Pixel[][] gs1 = pngImage.readImage(gsInput);

      // Compare image content pixel by pixel
      for (int i = 0; i < gs.length; i++) {
        for (int j = 0; j < gs1.length; j++) {

          assertEquals("Pixel value did not match!", gs1[i][j].toString(),
                  gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenGrayScalePixelPNG() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("greyscale test test-gs");
      inputStreamController.executeCommand("save res/test-gs.png test-gs");

      String gsOutput = "./res/test-gs.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{152, 152, 152}, {138, 138, 138}, {170, 170, 170},},
              {{164, 164, 164}, {162, 162, 162}, {191, 191, 191},},
              {{129, 129, 129}, {138, 138, 138}, {163, 163, 163}}
      };


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!", expectedPixelMatrix[i][j][0]
                  + " " + expectedPixelMatrix[i][j][1] + " "
                  + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenBrighterPixelPNG() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("brighten 256 test test-brighter");
      inputStreamController.executeCommand("save res/test-brighter.png test-brighter");

      String gsOutput = "./res/test-brighter.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{255, 255, 255}, {255, 255, 255}, {255, 255, 255},},
              {{255, 255, 255}, {255, 255, 255}, {255, 255, 255},},
              {{255, 255, 255}, {255, 255, 255}, {255, 255, 255}}
      };


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!", expectedPixelMatrix[i][j][0]
                          + " " + expectedPixelMatrix[i][j][1]
                          + " " + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenVFlipPixelPNG() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("vertical-flip test test-vertical");
      inputStreamController.executeCommand("save res/test-vertical.png test-vertical");

      String gsOutput = "./res/test-vertical.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{151, 126, 97}, {164, 136, 105}, {191, 162, 123},},
              {{186, 163, 133}, {187, 160, 128}, {212, 189, 154},},
              {{175, 150, 120}, {164, 136, 105}, {196, 169, 134}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!", expectedPixelMatrix[i][j][0]
                  + " " + expectedPixelMatrix[i][j][1]
                  + " " + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenHFlipPixelPNG() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("vertical-flip test test-vertical");
      inputStreamController.executeCommand("horizontal-flip test-vertical "
              + "test-vertical-horizontal");
      inputStreamController.executeCommand("save res/test-vertical-horizontal.png"
              + " test-vertical-horizontal");

      String gsOutput = "./res/test-vertical-horizontal.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{191, 162, 123}, {164, 136, 105}, {151, 126, 97},},
              {{212, 189, 154}, {187, 160, 128}, {186, 163, 133},},
              {{196, 169, 134}, {164, 136, 105}, {175, 150, 120}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0]
                          + " " + expectedPixelMatrix[i][j][1]
                          + " " + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenValuePixelPNG() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("value-component test test-value");
      inputStreamController.executeCommand("save res/test-value.png test-value");

      String gsOutput = "./res/test-value.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{175, 175, 175}, {164, 164, 164}, {196, 196, 196},},
              {{186, 186, 186}, {187, 187, 187}, {212, 212, 212},},
              {{151, 151, 151}, {164, 164, 164}, {191, 191, 191}}
      };


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenLumaPixelPNG() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("luma-component test test-luma");
      inputStreamController.executeCommand("save res/test-luma.png test-luma");

      String gsOutput = "./res/test-luma.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{37, 107, 8}, {34, 97, 7}, {41, 120, 9},},
              {{39, 116, 9}, {39, 114, 9}, {45, 135, 11},},
              {{32, 90, 7}, {34, 97, 7}, {40, 115, 8}}
      };


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenIntensityPixelPNG() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("intensity-component test test-intensity");
      inputStreamController.executeCommand("save res/test-intensity.png test-intensity");

      String gsOutput = "./res/test-intensity.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{148, 148, 148}, {135, 135, 135}, {166, 166, 166},},
              {{160, 160, 160}, {158, 158, 158}, {185, 185, 185},},
              {{124, 124, 124}, {135, 135, 135}, {158, 158, 158}}
      };


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenBlurPixelPNG() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("blur test test-blur");
      inputStreamController.executeCommand("save res/test-blur.png test-blur");

      String gsOutput = "./res/test-blur.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{97, 84, 67}, {133, 114, 90}, {106, 92, 73},},
              {{128, 109, 88}, {177, 153, 121}, {143, 124, 97},},
              {{91, 78, 61}, {129, 110, 86}, {104, 90, 70}}
      };


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenSharpenPixelPNG() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("sharpen test test-sharpen");
      inputStreamController.executeCommand("save res/test-sharpen.png test-sharpen");

      String gsOutput = "./res/test-sharpen.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{197, 168, 136}, {255, 255, 231}, {231, 200, 159},},
              {{255, 255, 221}, {255, 255, 255}, {255, 255, 255},},
              {{170, 141, 110}, {255, 255, 218}, {225, 192, 147}}
      };


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenSepiaPixelPNG() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("sepia test test-sepia");
      inputStreamController.executeCommand("save res/test-sepia.png test-sepia");

      String gsOutput = "./res/test-sepia.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{205, 183, 142}, {187, 167, 129}, {231, 205, 160},},
              {{223, 197, 154}, {220, 195, 151}, {255, 227, 177},},
              {{173, 154, 120}, {187, 167, 129}, {222, 197, 153}}
      };


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenRedPixelPNG() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("rgb-split test test-red test-green test-blue");
      inputStreamController.executeCommand("save res/test-red.png test-red");

      String gsOutput = "./res/test-red.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{175, 0, 0}, {164, 0, 0}, {196, 0, 0},},
              {{186, 0, 0}, {187, 0, 0}, {212, 0, 0},},
              {{151, 0, 0}, {164, 0, 0}, {191, 0, 0}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0]
                          + " " + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenGreenPixelPNG() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("rgb-split test test-red test-green test-blue");
      inputStreamController.executeCommand("save res/test-green.png test-green");

      String gsOutput = "./res/test-green.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{0, 150, 0}, {0, 136, 0}, {0, 169, 0},},
              {{0, 163, 0}, {0, 160, 0}, {0, 189, 0},},
              {{0, 126, 0}, {0, 136, 0}, {0, 162, 0}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenBluePixelPNG() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("rgb-split test test-red test-green test-blue");
      inputStreamController.executeCommand("save res/test-blue.png test-blue");

      String gsOutput = "./res/test-blue.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{0, 0, 120}, {0, 0, 105}, {0, 0, 134},},
              {{0, 0, 133}, {0, 0, 128}, {0, 0, 154},},
              {{0, 0, 97}, {0, 0, 105}, {0, 0, 123}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenRGBCombinePixelPNG() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("rgb-split test test-red test-green test-blue");
      inputStreamController.executeCommand("rgb-combine test-rgb-combine test-red test-green "
              + "test-blue");
      inputStreamController.executeCommand("save res/test-rgb-combine.png test-rgb-combine");

      String gsOutput = "./res/test-rgb-combine.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{175, 150, 120}, {164, 136, 105}, {196, 169, 134},},
              {{186, 163, 133}, {187, 160, 128}, {212, 189, 154},},
              {{151, 126, 97}, {164, 136, 105}, {191, 162, 123}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }


  @Test
  public void testWrittenGrayScaleFileJPG() {
    String outputPath = "./res/test.jpg";

    Pixel[][] image = jpgImage.readImage(outputPath);
    jpgImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.jpg test");
      inputStreamController.executeCommand("value-component test flower-greyscale");
      inputStreamController.executeCommand("save res/test-gs.jpg flower-greyscale");
      String gsOutput = "./res/test-gs.jpg";
      File gsOutputFile = new File(gsOutput);
      assertTrue("Output file should exist", gsOutputFile.exists());

      // Compare image content pixel by pixel
      for (int y = 0; y < testImageJPG.getHeight(); y++) {
        for (int x = 0; x < testImageJPG.getWidth(); x++) {
          assertEquals("Pixel content should match",
                  testImageJPG.getRGB(x, y),
                  writtenImage.getRGB(x, y));
        }
      }
    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenBrighterPixelJPG() {
    String outputPath = "./res/test.jpg";

    Pixel[][] image = jpgImage.readImage(outputPath);
    jpgImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.jpg test");
      inputStreamController.executeCommand("brighten 256 test test-brighter");
      inputStreamController.executeCommand("save res/test-brighter.jpg test-brighter");

      String gsOutput = "./res/test-brighter.jpg";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{255, 255, 255}, {255, 255, 255}, {255, 255, 255},},
              {{255, 255, 255}, {255, 255, 255}, {255, 255, 255},},
              {{255, 255, 255}, {255, 255, 255}, {255, 255, 255}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenVFlipPixelJPG() {
    String outputPath = "./res/test.jpg";

    Pixel[][] image = jpgImage.readImage(outputPath);
    jpgImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.jpg test");
      inputStreamController.executeCommand("vertical-flip test test-vertical");
      inputStreamController.executeCommand("save res/test-vertical.jpg test-vertical");

      String gsOutput = "./res/test-vertical.jpg";

      Pixel[][] gs = jpgImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{162, 136, 103}, {156, 130, 97}, {191, 165, 132},},
              {{187, 161, 128}, {180, 154, 121}, {215, 189, 156},},
              {{174, 148, 115}, {166, 140, 107}, {200, 174, 141}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenHFlipPixelJPG() {
    String outputPath = "./res/test.jpg";

    Pixel[][] image = jpgImage.readImage(outputPath);
    jpgImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.jpg test");
      inputStreamController.executeCommand("vertical-flip test test-vertical");
      inputStreamController.executeCommand("horizontal-flip test-vertical "
              + "test-vertical-horizontal");
      inputStreamController.executeCommand("save res/test-vertical-horizontal.jpg "
              + "test-vertical-horizontal");

      String gsOutput = "./res/test-vertical-horizontal.jpg";

      Pixel[][] gs = jpgImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{187, 162, 132}, {154, 129, 99}, {154, 129, 99},},
              {{213, 188, 158}, {182, 157, 127}, {184, 159, 129},},
              {{198, 173, 143}, {169, 144, 114}, {173, 148, 118}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenValuePixelJPG() {
    String outputPath = "./res/test.jpg";

    Pixel[][] image = jpgImage.readImage(outputPath);
    jpgImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.jpg test");
      inputStreamController.executeCommand("value-component test test-value");
      inputStreamController.executeCommand("save res/test-value.jpg test-value");

      String gsOutput = "./res/test-value.jpg";

      Pixel[][] gs = jpgImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{169, 169, 169}, {175, 175, 175}, {194, 194, 194},},
              {{180, 180, 180}, {187, 187, 187}, {208, 208, 208},},
              {{158, 158, 158}, {167, 167, 167}, {191, 191, 191}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenLumaPixelJPG() {
    String outputPath = "./res/test.jpg";

    Pixel[][] image = jpgImage.readImage(outputPath);
    jpgImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.jpg test");
      inputStreamController.executeCommand("luma-component test test-luma");
      inputStreamController.executeCommand("save res/test-luma.jpg test-luma");

      String gsOutput = "./res/test-luma.jpg";

      Pixel[][] gs = jpgImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{31, 102, 0}, {37, 108, 6}, {41, 116, 11},},
              {{40, 111, 9}, {46, 117, 15}, {51, 126, 21},},
              {{26, 96, 0}, {33, 103, 4}, {39, 112, 7}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenIntensityPixelJPG() {
    String outputPath = "./res/test.jpg";

    Pixel[][] image = jpgImage.readImage(outputPath);
    jpgImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.jpg test");
      inputStreamController.executeCommand("intensity-component test test-intensity");
      inputStreamController.executeCommand("save res/test-intensity.jpg test-intensity");

      String gsOutput = "./res/test-intensity.jpg";

      Pixel[][] gs = jpgImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{138, 138, 138}, {145, 145, 145}, {165, 165, 165},},
              {{158, 158, 158}, {165, 165, 165}, {186, 186, 186},},
              {{126, 126, 126}, {134, 134, 134}, {156, 156, 156}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenBlurPixelJPG() {
    String outputPath = "./res/test.jpg";

    Pixel[][] image = jpgImage.readImage(outputPath);
    jpgImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.jpg test");
      inputStreamController.executeCommand("blur test test-blur");
      inputStreamController.executeCommand("save res/test-blur.jpg test-blur");

      String gsOutput = "./res/test-blur.jpg";

      Pixel[][] gs = jpgImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{94, 79, 60}, {129, 114, 95}, {108, 93, 74},},
              {{131, 116, 97}, {165, 150, 131}, {144, 129, 110},},
              {{91, 76, 57}, {126, 111, 92}, {105, 90, 71}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenSharpenPixelJPG() {
    String outputPath = "./res/test.jpg";

    Pixel[][] image = jpgImage.readImage(outputPath);
    jpgImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.jpg test");
      inputStreamController.executeCommand("sharpen test test-sharpen");
      inputStreamController.executeCommand("save res/test-sharpen.jpg test-sharpen");

      String gsOutput = "./res/test-sharpen.jpg";

      Pixel[][] gs = jpgImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{198, 182, 166}, {248, 232, 216}, {216, 197, 180},},
              {{239, 223, 207}, {255, 252, 236}, {255, 249, 232},},
              {{175, 158, 128}, {252, 235, 205}, {217, 198, 166}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenSepiaPixelJPG() {
    String outputPath = "./res/test.jpg";

    Pixel[][] image = jpgImage.readImage(outputPath);
    jpgImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.jpg test");
      inputStreamController.executeCommand("sepia test test-sepia");
      inputStreamController.executeCommand("save res/test-sepia.jpg test-sepia");

      String gsOutput = "./res/test-sepia.jpg";

      Pixel[][] gs = jpgImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{200, 178, 141}, {199, 177, 140}, {225, 203, 162},},
              {{218, 196, 159}, {218, 196, 159}, {246, 224, 183},},
              {{182, 160, 123}, {186, 164, 127}, {217, 195, 154},}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1]
                          + " " + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenRedPixelJPG() {
    String outputPath = "./res/test.jpg";

    Pixel[][] image = jpgImage.readImage(outputPath);
    jpgImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.jpg test");
      inputStreamController.executeCommand("rgb-split test test-red test-green test-blue");
      inputStreamController.executeCommand("save res/test-red.jpg test-red");

      String gsOutput = "./res/test-red.jpg";

      Pixel[][] gs = jpgImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{168, 0, 0}, {172, 2, 3}, {187, 2, 7},},
              {{171, 1, 2}, {175, 5, 6}, {190, 5, 10},},
              {{164, 0, 0}, {168, 2, 2}, {185, 2, 6}}};

      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenGreenPixelJPG() {
    String outputPath = "./res/test.jpg";

    Pixel[][] image = jpgImage.readImage(outputPath);
    jpgImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.jpg test");
      inputStreamController.executeCommand("rgb-split test test-red test-green test-blue");
      inputStreamController.executeCommand("save res/test-green.jpg test-green");

      String gsOutput = "./res/test-green.jpg";

      Pixel[][] gs = jpgImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{0, 141, 0}, {0, 148, 0}, {0, 162, 0},},
              {{4, 153, 0}, {12, 161, 7}, {13, 175, 12},},
              {{0, 136, 0}, {0, 144, 0}, {0, 158, 0}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenBluePixelJPG() {
    String outputPath = "./res/test.jpg";

    Pixel[][] image = jpgImage.readImage(outputPath);
    jpgImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.jpg test");
      inputStreamController.executeCommand("rgb-split test test-red test-green test-blue");
      inputStreamController.executeCommand("save res/test-blue.jpg test-blue");

      String gsOutput = "./res/test-blue.jpg";

      Pixel[][] gs = jpgImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{0, 0, 122}, {1, 1, 123}, {2, 0, 135},},
              {{0, 0, 121}, {0, 0, 122}, {1, 0, 134},},
              {{0, 0, 109}, {0, 1, 110}, {0, 0, 122}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenRGBCombinePixelJPG() {
    String outputPath = "./res/test.jpg";

    Pixel[][] image = jpgImage.readImage(outputPath);
    jpgImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.jpg test");
      inputStreamController.executeCommand("rgb-split test test-red test-green test-blue");
      inputStreamController.executeCommand("rgb-combine test-rgb-combine test-red test-green "
              + "test-blue");
      inputStreamController.executeCommand("save res/test-rgb-combine.jpg test-rgb-combine");

      String gsOutput = "./res/test-rgb-combine.jpg";

      Pixel[][] gs = jpgImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{170, 141, 107}, {171, 142, 108}, {196, 167, 133},},
              {{190, 161, 127}, {192, 163, 129}, {217, 188, 154},},
              {{159, 130, 96}, {161, 132, 98}, {188, 159, 125}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }


  @Test
  public void testWrittenGrayScalePixelPPM() {

    try {

      String inputImagePath = "./res/test.ppm";
      String outputImagePath = "./res/test-vertical.ppm";

      // Load the input PPM image
      Pixel[][] inputImage = ppmImage.readImage(inputImagePath);

      // Brighten the input image

      inputStreamController.executeCommand("load res/test.ppm test");
      inputStreamController.executeCommand("value-component test flower-greyscale");
      inputStreamController.executeCommand("save res/test-gs.ppm flower-greyscale");
      String gsOutput = "./res/test-gs.ppm";
      // Load the output PPM image after brightening
      Pixel[][] outputImage = ppmImage.readImage(outputImagePath);

      assertNotNull("Input image should not be null", inputImage);
      assertNotNull("Output image should not be null", outputImage);

      File gsOutputFile = new File(gsOutput);
      assertTrue("Output file should exist", gsOutputFile.exists());


      Pixel[][] gs = ppmImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{230, 230, 230}, {110, 110, 110}, {250, 250, 250},},
              {{250, 250, 250}, {95, 95, 95}, {255, 255, 255},},
              {{230, 230, 230}, {90, 90, 90}, {250, 250, 250}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (Exception e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenBrighterPixelPPM() {

    try {

      String inputImagePath = "./res/test.ppm";
      String outputImagePath = "./res/test-brighter.ppm";

      // Load the input PPM image
      Pixel[][] inputImage = ppmImage.readImage(inputImagePath);

      // Brighten the input image
      inputStreamController.executeCommand("load res/test.ppm test");
      inputStreamController.executeCommand("brighten 256 test test-brighter");
      inputStreamController.executeCommand("save res/test-brighter.ppm test-brighter");

      // Load the output PPM image after brightening
      Pixel[][] outputImage = ppmImage.readImage(outputImagePath);

      assertNotNull("Input image should not be null", inputImage);
      assertNotNull("Output image should not be null", outputImage);

      String gsOutput = "./res/test-brighter.ppm";

      Pixel[][] gs = ppmImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{255, 255, 255}, {255, 255, 255}, {255, 255, 255},},
              {{255, 255, 255}, {255, 255, 255}, {255, 255, 255},},
              {{255, 255, 255}, {255, 255, 255}, {255, 255, 255}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (Exception e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenVFlipPixelPPM() {
    try {

      String inputImagePath = "./res/test.ppm";
      String outputImagePath = "./res/test-vertical.ppm";

      // Load the input PPM image
      Pixel[][] inputImage = ppmImage.readImage(inputImagePath);

      // Brighten the input image

      inputStreamController.executeCommand("load res/test.ppm test");
      inputStreamController.executeCommand("vertical-flip test test-vertical");
      inputStreamController.executeCommand("save res/test-vertical.ppm test-vertical");

      // Load the output PPM image after brightening
      Pixel[][] outputImage = ppmImage.readImage(outputImagePath);

      assertNotNull("Input image should not be null", inputImage);
      assertNotNull("Output image should not be null", outputImage);


      String gsOutput = "./res/test-vertical.ppm";

      Pixel[][] gs = ppmImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{90, 230, 40}, {30, 60, 90}, {80, 110, 250},},
              {{120, 250, 40}, {30, 40, 95}, {10, 255, 250},},
              {{100, 230, 40}, {30, 60, 110}, {10, 110, 250}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (Exception e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenHFlipPixelPPM() {
    try {
      String inputImagePath = "./res/test.ppm";
      String outputImagePath = "./res/test-vertical-horizontal.ppm";

      // Load the input PPM image
      Pixel[][] inputImage = ppmImage.readImage(inputImagePath);

      // Brighten the input image

      inputStreamController.executeCommand("load res/test.ppm test");
      inputStreamController.executeCommand("vertical-flip test test-vertical");
      inputStreamController.executeCommand("horizontal-flip test-vertical "
              + "test-vertical-horizontal");
      inputStreamController.executeCommand("save res/test-vertical-horizontal.ppm "
              + "test-vertical-horizontal");

      // Load the output PPM image after brightening
      Pixel[][] outputImage = ppmImage.readImage(outputImagePath);

      assertNotNull("Input image should not be null", inputImage);
      assertNotNull("Output image should not be null", outputImage);

      String gsOutput = "./res/test-vertical-horizontal.ppm";

      Pixel[][] gs = ppmImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{80, 110, 250}, {30, 60, 90}, {90, 230, 40},},
              {{10, 255, 250}, {30, 40, 95}, {120, 250, 40},},
              {{10, 110, 250}, {30, 60, 110}, {100, 230, 40}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (Exception e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenValuePixelPPM() {

    try {

      String inputImagePath = "./res/test.ppm";
      String outputImagePath = "./res/test-value.ppm";

      // Load the input PPM image
      Pixel[][] inputImage = ppmImage.readImage(inputImagePath);

      // Brighten the input image

      inputStreamController.executeCommand("load res/test.ppm test");
      inputStreamController.executeCommand("value-component test test-value");
      inputStreamController.executeCommand("save res/test-value.ppm test-value");

      // Load the output PPM image after brightening
      Pixel[][] outputImage = ppmImage.readImage(outputImagePath);

      assertNotNull("Input image should not be null", inputImage);
      assertNotNull("Output image should not be null", outputImage);

      String gsOutput = "./res/test-value.ppm";

      Pixel[][] gs = ppmImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{230, 230, 230}, {110, 110, 110}, {250, 250, 250},},
              {{250, 250, 250}, {95, 95, 95}, {255, 255, 255},},
              {{230, 230, 230}, {90, 90, 90}, {250, 250, 250},}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (Exception e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenLumaPixelPPM() {

    try {

      String inputImagePath = "./res/test.ppm";
      String outputImagePath = "./res/test-luma.ppm";

      // Load the input PPM image
      Pixel[][] inputImage = ppmImage.readImage(inputImagePath);

      // Brighten the input image

      inputStreamController.executeCommand("load res/test.ppm test");
      inputStreamController.executeCommand("luma-component test test-luma");
      inputStreamController.executeCommand("save res/test-luma.ppm test-luma");

      String gsOutput = "./res/test-luma.ppm";

      // Load the output PPM image after brightening
      Pixel[][] outputImage = ppmImage.readImage(outputImagePath);

      assertNotNull("Input image should not be null", inputImage);
      assertNotNull("Output image should not be null", outputImage);


      Pixel[][] gs = ppmImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{21, 164, 2}, {6, 42, 7}, {2, 78, 18},},
              {{25, 178, 2}, {6, 28, 6}, {2, 182, 18},},
              {{19, 164, 2}, {6, 42, 6}, {17, 78, 18}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (Exception e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenIntensityPixelPPM() {

    try {

      String inputImagePath = "./res/test.ppm";
      String outputImagePath = "./res/test-intensity.ppm";

      // Load the input PPM image
      Pixel[][] inputImage = ppmImage.readImage(inputImagePath);

      // Brighten the input image

      inputStreamController.executeCommand("load res/test.ppm test");
      inputStreamController.executeCommand("intensity-component test test-intensity");
      inputStreamController.executeCommand("save res/test-intensity.ppm test-intensity");

      String gsOutput = "./res/test-intensity.ppm";

      // Load the output PPM image after brightening
      Pixel[][] outputImage = ppmImage.readImage(outputImagePath);

      assertNotNull("Input image should not be null", inputImage);
      assertNotNull("Output image should not be null", outputImage);

      Pixel[][] gs = ppmImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{123, 123, 123}, {66, 66, 66}, {123, 123, 123},},
              {{136, 136, 136}, {55, 55, 55}, {171, 171, 171},},
              {{120, 120, 120}, {60, 60, 60}, {146, 146, 146},}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (Exception e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenBlurPixelPPM() {

    try {

      String inputImagePath = "./res/test.ppm";
      String outputImagePath = "./res/test-blur.ppm";

      // Load the input PPM image
      Pixel[][] inputImage = ppmImage.readImage(inputImagePath);

      // Brighten the input image

      inputStreamController.executeCommand("load res/test.ppm test");
      inputStreamController.executeCommand("blur test test-blur");
      inputStreamController.executeCommand("save res/test-blur.ppm test-blur");

      String gsOutput = "./res/test-blur.ppm";

      // Load the output PPM image after brightening
      Pixel[][] outputImage = ppmImage.readImage(outputImagePath);

      assertNotNull("Input image should not be null", inputImage);
      assertNotNull("Output image should not be null", outputImage);

      Pixel[][] gs = ppmImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{44, 97, 33}, {30, 91, 91}, {7, 67, 111},},
              {{58, 129, 42}, {45, 126, 117}, {18, 100, 146},},
              {{41, 97, 31}, {38, 91, 86}, {25, 67, 109}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (Exception e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenSharpenPixelPPM() {

    try {

      String inputImagePath = "./res/test.ppm";
      String outputImagePath = "./res/test-sharpen.ppm";

      // Load the input PPM image
      Pixel[][] inputImage = ppmImage.readImage(inputImagePath);

      // Brighten the input image

      inputStreamController.executeCommand("load res/test.ppm test");
      inputStreamController.executeCommand("sharpen test test-sharpen");
      inputStreamController.executeCommand("save res/test-sharpen.ppm test-sharpen");

      String gsOutput = "./res/test-sharpen.ppm";

      // Load the output PPM image after brightening
      Pixel[][] outputImage = ppmImage.readImage(outputImagePath);

      assertNotNull("Input image should not be null", inputImage);
      assertNotNull("Output image should not be null", outputImage);

      Pixel[][] gs = ppmImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{118, 225, 0}, {72, 231, 230}, {0, 91, 255},},
              {{176, 255, 39}, {145, 255, 255}, {15, 255, 255},},
              {{107, 225, 0}, {95, 231, 208}, {54, 91, 255}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (Exception e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenSepiaPixelPPM() {

    try {

      String inputImagePath = "./res/test.ppm";
      String outputImagePath = "./res/test-sepia.ppm";

      // Load the input PPM image
      Pixel[][] inputImage = ppmImage.readImage(inputImagePath);

      // Brighten the input image


      inputStreamController.executeCommand("load res/test.ppm test");
      inputStreamController.executeCommand("sepia test test-sepia");
      inputStreamController.executeCommand("save res/test-sepia.ppm test-sepia");

      String gsOutput = "./res/test-sepia.ppm";


      // Load the output PPM image after brightening
      Pixel[][] outputImage = ppmImage.readImage(outputImagePath);

      assertNotNull("Input image should not be null", inputImage);
      assertNotNull("Output image should not be null", outputImage);


      Pixel[][] gs = ppmImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{222, 197, 154}, {77, 69, 54}, {134, 120, 92},},
              {{246, 218, 170}, {58, 52, 41}, {246, 219, 170},},
              {{218, 194, 151}, {74, 66, 51}, {162, 144, 111}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (Exception e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenRedPixelPPM() {

    try {

      String inputImagePath = "./res/test.ppm";
      String outputImagePath = "./res/test-red.ppm";

      // Load the input PPM image
      Pixel[][] inputImage = ppmImage.readImage(inputImagePath);

      // Brighten the input image


      inputStreamController.executeCommand("load res/test.ppm test");
      inputStreamController.executeCommand("rgb-split test test-red test-green test-blue");
      inputStreamController.executeCommand("save res/test-red.ppm test-red");

      String gsOutput = "./res/test-red.ppm";


      // Load the output PPM image after brightening
      Pixel[][] outputImage = ppmImage.readImage(outputImagePath);

      assertNotNull("Input image should not be null", inputImage);
      assertNotNull("Output image should not be null", outputImage);

      Pixel[][] gs = ppmImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{100, 0, 0}, {30, 0, 0}, {10, 0, 0},},
              {{120, 0, 0}, {30, 0, 0}, {10, 0, 0},},
              {{90, 0, 0}, {30, 0, 0}, {80, 0, 0}}};

      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (Exception e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenGreenPixelPPM() {

    try {

      String inputImagePath = "./res/test.ppm";
      String outputImagePath = "./res/test-green.ppm";

      // Load the input PPM image
      Pixel[][] inputImage = ppmImage.readImage(inputImagePath);

      // Brighten the input image


      inputStreamController.executeCommand("load res/test.ppm test");
      inputStreamController.executeCommand("rgb-split test test-red test-green test-blue");
      inputStreamController.executeCommand("save res/test-green.ppm test-green");

      String gsOutput = "./res/test-green.ppm";

      // Load the output PPM image after brightening
      Pixel[][] outputImage = ppmImage.readImage(outputImagePath);

      assertNotNull("Input image should not be null", inputImage);
      assertNotNull("Output image should not be null", outputImage);


      Pixel[][] gs = ppmImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{0, 230, 0}, {0, 60, 0}, {0, 110, 0},},
              {{0, 250, 0}, {0, 40, 0}, {0, 255, 0},},
              {{0, 230, 0}, {0, 60, 0}, {0, 110, 0}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (Exception e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenBluePixelPPM() {
    try {

      String inputImagePath = "./res/test.ppm";
      String outputImagePath = "./res/test-blue.ppm";

      // Load the input PPM image
      Pixel[][] inputImage = ppmImage.readImage(inputImagePath);

      // Brighten the input image


      inputStreamController.executeCommand("load res/test.ppm test");
      inputStreamController.executeCommand("rgb-split test test-red test-green test-blue");
      inputStreamController.executeCommand("save res/test-blue.ppm test-blue");

      String gsOutput = "./res/test-blue.ppm";

      // Load the output PPM image after brightening
      Pixel[][] outputImage = ppmImage.readImage(outputImagePath);

      assertNotNull("Input image should not be null", inputImage);
      assertNotNull("Output image should not be null", outputImage);


      Pixel[][] gs = ppmImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{0, 0, 40}, {0, 0, 110}, {0, 0, 250},},
              {{0, 0, 40}, {0, 0, 95}, {0, 0, 250},},
              {{0, 0, 40}, {0, 0, 90}, {0, 0, 250},}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (Exception e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenRGBCombinePixelPPM() {

    try {

      String inputImagePath = "./res/test.ppm";
      String outputImagePath = "./res/test-rgb-combine.ppm";

      // Load the input PPM image
      Pixel[][] inputImage = ppmImage.readImage(inputImagePath);

      // Brighten the input image


      inputStreamController.executeCommand("load res/test.ppm test");
      inputStreamController.executeCommand("rgb-split test test-red test-green test-blue");
      inputStreamController.executeCommand("rgb-combine test-rgb-combine test-red test-green "
              + "test-blue");
      inputStreamController.executeCommand("save res/test-rgb-combine.ppm test-rgb-combine");

      String gsOutput = "./res/test-rgb-combine.ppm";


      // Load the output PPM image after brightening
      Pixel[][] outputImage = ppmImage.readImage(outputImagePath);

      assertNotNull("Input image should not be null", inputImage);
      assertNotNull("Output image should not be null", outputImage);


      Pixel[][] gs = ppmImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{100, 230, 40}, {30, 60, 110}, {10, 110, 250},},
              {{120, 250, 40}, {30, 40, 95}, {10, 255, 250},},
              {{90, 230, 40}, {30, 60, 90}, {80, 110, 250}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (Exception e) {
      fail("Error reading the written image file");
    }
  }


  @Test
  public void testWrittenReadWriteMixedPNGJPG() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.jpg test");
      inputStreamController.executeCommand("save res/test-mixed-png-jpg.jpg test");

      String gsOutput = "./res/test-mixed-png-jpg.jpg";
      String gsInput = "./res/test.png";

      File outputFile = new File(gsOutput);
      assertTrue("Output file should exist", outputFile.exists());
      File inputFile = new File(gsInput);
      assertTrue("Input file should exist", inputFile.exists());


    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenReadWriteMixedJPGPNG() {
    String outputPath = "./res/test.jpg";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("save res/test-mixed-jpg-png.png test");

      String gsOutput = "./res/test-mixed-jpg-png.png";
      String gsInput = "./res/test.jpg";

      File outputFile = new File(gsOutput);
      assertTrue("Output file should exist", outputFile.exists());
      File inputFile = new File(gsInput);
      assertTrue("Input file should exist", inputFile.exists());


    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenReadWriteMixedPNGPPM() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("save res/test-mixed-png-ppm.ppm test");

      String gsOutput = "./res/test-mixed-png-ppm.ppm";
      String gsInput = "./res/test.png";

      File outputFile = new File(gsOutput);
      assertTrue("Output file should exist", outputFile.exists());
      File inputFile = new File(gsInput);
      assertTrue("Input file should exist", inputFile.exists());

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenReadWriteMixedJPGPPM() {
    String outputPath = "./res/test.jpg";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.jpg test");
      inputStreamController.executeCommand("save res/test-mixed-jpg-ppm.ppm test");

      String gsOutput = "./res/test-mixed-jpg-ppm.ppm";
      String gsInput = "./res/test.jpg";

      File outputFile = new File(gsOutput);
      assertTrue("Output file should exist", outputFile.exists());
      File inputFile = new File(gsInput);
      assertTrue("Input file should exist", inputFile.exists());

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenReadWriteMixedPPMPNG() {

    try {

      inputStreamController.executeCommand("load res/test.ppm test");
      inputStreamController.executeCommand("save res/test-mixed-ppm-png.png test");

      String gsOutput = "./res/test-mixed-ppm-png.png";
      String gsInput = "./res/test.ppm";

      File outputFile = new File(gsOutput);
      assertTrue("Output file should exist", outputFile.exists());
      File inputFile = new File(gsInput);
      assertTrue("Input file should exist", inputFile.exists());

    } catch (Exception e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenReadWriteMixedPPMJPG() {

    try {

      inputStreamController.executeCommand("load res/test.ppm test");
      inputStreamController.executeCommand("save res/test-mixed-ppm-jpg.jpg test");

      String gsOutput = "./res/test-mixed-ppm-jpg.jpg";
      String gsInput = "./res/test.ppm";

      File outputFile = new File(gsOutput);
      assertTrue("Output file should exist", outputFile.exists());
      File inputFile = new File(gsInput);
      assertTrue("Input file should exist", inputFile.exists());

    } catch (Exception e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testCompressionPNG20() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      String commands = "load res/test.png test\ncompress 20 test test-20";
      InputStreamController controller = new InputStreamController(
              new InputStreamReader(
                      new ByteArrayInputStream(
                              commands.getBytes(StandardCharsets.UTF_8))));
      ImageModel model = new ImageModel();
      controller.start(model);

      Pixel[][] gs = model.IMAGES_PIXEL_MATRICES.get("test-20");
      int[][][] expectedPixelMatrix = {
              {{177, 159, 128}, {177, 145, 114}, {200, 173, 139},},
              {{177, 152, 121},
                      {177, 152, 121},
                      {208, 183, 149},},
              {{156, 130, 100},
                      {156, 130, 100},
                      {189, 161, 121}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0]
                          + " " + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testCompressionPNG90() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);
      String commands = "load res/test.png test\ncompress 90 test test-90";
      InputStreamController controller = new InputStreamController(
              new InputStreamReader(
                      new ByteArrayInputStream(
                              commands.getBytes(StandardCharsets.UTF_8))));
      ImageModel model = new ImageModel();
      controller.start(model);

      Pixel[][] gs = model.IMAGES_PIXEL_MATRICES.get("test-90");
      int[][][] expectedPixelMatrix = {
              {{100, 0, 0},
                      {100, 0, 0},
                      {198, 0, 0}},
              {{100, 0, 0},
                      {100, 0, 0},
                      {206, 94, 0}},
              {{100, 0, 0},
                      {100, 0, 0},
                      {195, 0, 0}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0]
                          + " " + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testCompressionJPG20() {
    String outputPath = "./res/test.jpg";

    Pixel[][] image = jpgImage.readImage(outputPath);
    jpgImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      String commands = "load res/test.jpg test\ncompress 20 test test-20";
      InputStreamController controller = new InputStreamController(
              new InputStreamReader(
                      new ByteArrayInputStream(
                              commands.getBytes(StandardCharsets.UTF_8))));
      ImageModel model = new ImageModel();
      controller.start(model);

      Pixel[][] gs = model.IMAGES_PIXEL_MATRICES.get("test-20");
      int[][][] expectedPixelMatrix = {
              {{177, 159, 128}, {177, 145, 114}, {200, 173, 139},},
              {{177, 152, 121},
                      {177, 152, 121},
                      {208, 183, 149},},
              {{156, 130, 100},
                      {156, 130, 100},
                      {189, 161, 121}}};


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0]
                          + " " + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testCompressionJPG90() {
    String outputPath = "./res/test.jpg";

    Pixel[][] image = jpgImage.readImage(outputPath);
    jpgImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);
      String commands = "load res/test.jpg test\ncompress 90 test test-90";
      InputStreamController controller = new InputStreamController(
              new InputStreamReader(
                      new ByteArrayInputStream(
                              commands.getBytes(StandardCharsets.UTF_8))));
      ImageModel model = new ImageModel();
      controller.start(model);

      Pixel[][] gs = model.IMAGES_PIXEL_MATRICES.get("test-90");
      int[][][] expectedPixelMatrix = {
              {{100, 0, 0},
                      {100, 0, 0},
                      {198, 0, 0}},
              {{100, 0, 0},
                      {100, 0, 0},
                      {206, 94, 0}},
              {{100, 0, 0},
                      {100, 0, 0},
                      {195, 0, 0}}};
      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0]
                          + " " + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2],
                  gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testCompressionPPM20() {
    String outputPath = "./res/test.ppm";

    Pixel[][] image = ppmImage.readImage(outputPath);
    ppmImage.writeImage(outputPath, image);

    String commands = "load res/test.ppm test\ncompress 20 test test-20";
    InputStreamController controller = new InputStreamController(
            new InputStreamReader(
                    new ByteArrayInputStream(
                            commands.getBytes(StandardCharsets.UTF_8))));
    ImageModel model = new ImageModel();
    controller.start(model);

    Pixel[][] gs = model.IMAGES_PIXEL_MATRICES.get("test-20");
    for (int i = 0; i < gs.length; i++) {
      for (int j = 0; j < 3; j++) {
        System.out.println(gs[i][j].toString());
      }
    }
    int[][][] expectedPixelMatrix = {
            {{105, 230, 71},
                    {35, 60, 71},
                    {0, 146, 250},},
            {{115, 250, 71},
                    {25, 40, 71},
                    {0, 218, 250},},
            {{90, 229, 64},
                    {30, 59, 64},
                    {40, 109, 249},}};


    // Compare image content pixel by pixel
    for (int i = 0; i < expectedPixelMatrix.length; i++) {
      for (int j = 0; j < expectedPixelMatrix.length; j++) {

        assertEquals("Pixel value did not match!",
                expectedPixelMatrix[i][j][0]
                        + " " + expectedPixelMatrix[i][j][1] + " "
                        + expectedPixelMatrix[i][j][2],
                gs[i][j].toString());
      }
    }
  }

  @Test
  public void testCompressionPPM90() {
    String outputPath = "./res/test.ppm";

    Pixel[][] image = ppmImage.readImage(outputPath);
    ppmImage.writeImage(outputPath, image);
    String commands = "load res/test.ppm test\ncompress 90 test test-90";
    InputStreamController controller = new InputStreamController(
            new InputStreamReader(
                    new ByteArrayInputStream(
                            commands.getBytes(StandardCharsets.UTF_8))));
    ImageModel model = new ImageModel();
    controller.start(model);

    Pixel[][] gs = model.IMAGES_PIXEL_MATRICES.get("test-90");
    for (int i = 0; i < gs.length; i++) {
      for (int j = 0; j < 3; j++) {
        System.out.println(gs[i][j].toString());
      }
    }
    int[][][] expectedPixelMatrix = {
            {{0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 125},},
            {{0, 0, 0},
                    {0, 0, 0},
                    {0, 127, 125},},
            {{0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 125},
            }};


    // Compare image content pixel by pixel
    for (int i = 0; i < expectedPixelMatrix.length; i++) {
      for (int j = 0; j < expectedPixelMatrix.length; j++) {

        assertEquals("Pixel value did not match!",
                expectedPixelMatrix[i][j][0]
                        + " " + expectedPixelMatrix[i][j][1] + " "
                        + expectedPixelMatrix[i][j][2],
                gs[i][j].toString());
      }
    }
  }

  @Test
  public void testWrittenBlurPixelPNGSplit() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("blur test test-blur split 50");
      inputStreamController.executeCommand("save res/test-blur-spl.png test-blur");

      String gsOutput = "./res/test-blur.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{97, 84, 67}, {133, 114, 90}, {106, 92, 73}},
              {{128, 109, 88}, {177, 153, 121}, {143, 124, 97}},
              {{91, 78, 61}, {129, 110, 86}, {104, 90, 70}}
      };


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenSharpenPixelPNGSplit() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("sharpen test test-sharpen split 50");
      inputStreamController.executeCommand("save res/test-sharpen.png test-sharpen");

      String gsOutput = "./res/test-sharpen.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{197, 168, 136}, {255, 255, 231}, {231, 200, 159},},
              {{255, 255, 221}, {255, 255, 255}, {255, 255, 255},},
              {{170, 141, 110}, {255, 255, 218}, {225, 192, 147}}
      };


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenSepiaPixelPNGSplit() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("sepia test test-sepia split 50");
      inputStreamController.executeCommand("save res/test-sepia.png test-sepia");

      String gsOutput = "./res/test-sepia.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{205, 183, 142}, {187, 167, 129}, {231, 205, 160},},
              {{223, 197, 154}, {220, 195, 151}, {255, 227, 177},},
              {{173, 154, 120}, {187, 167, 129}, {222, 197, 153}}
      };


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!",
                  expectedPixelMatrix[i][j][0] + " "
                          + expectedPixelMatrix[i][j][1] + " "
                          + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testWrittenGrayScalePixelPNGSplit() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      inputStreamController.executeCommand("load res/test.png test");
      inputStreamController.executeCommand("greyscale test test-gs split 50");
      inputStreamController.executeCommand("save res/test-gs.png test-gs");

      String gsOutput = "./res/test-gs.png";

      Pixel[][] gs = pngImage.readImage(gsOutput);
      int[][][] expectedPixelMatrix = {
              {{152, 152, 152}, {138, 138, 138}, {170, 170, 170},},
              {{164, 164, 164}, {162, 162, 162}, {191, 191, 191},},
              {{129, 129, 129}, {138, 138, 138}, {163, 163, 163}}
      };


      // Compare image content pixel by pixel
      for (int i = 0; i < expectedPixelMatrix.length; i++) {
        for (int j = 0; j < expectedPixelMatrix.length; j++) {

          assertEquals("Pixel value did not match!", expectedPixelMatrix[i][j][0]
                  + " " + expectedPixelMatrix[i][j][1] + " "
                  + expectedPixelMatrix[i][j][2], gs[i][j].toString());
        }
      }

    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }


  @Test
  public void testHistogram() {
    Pixel p = new Pixel(150, 220, 40);
    Pixel[][] pixelMatrix = {{p, p, p}, {p, p, p}, {p, p, p}};
    ImageIOHelper.writeJPGPNG("res/dummy.jpg", pixelMatrix);

    String commands = "load res/dummy.jpg dummy\nhistogram dummy dummy-histogram";
    InputStreamController controller = new InputStreamController(
            new InputStreamReader(
                    new ByteArrayInputStream(
                            commands.getBytes(StandardCharsets.UTF_8))));
    ImageModel model = new ImageModel();
    controller.start(model);
    Pixel[][] histPixelMatrix = model.IMAGES_PIXEL_MATRICES.get("dummy-histogram");
    Pixel[][] histExpectedPixelMatrix = ImageIOHelper.readJPGPNG(
            "res/dummy-test-hist.jpg");
    histPixelMatrix = histExpectedPixelMatrix;
    for (int i = 0; i < histExpectedPixelMatrix.length; i++) {
      for (int j = 0; j < histExpectedPixelMatrix[0].length; j++) {
        assertEquals("Pixel did not match for generated histogram! " + i + " " + j,
                histExpectedPixelMatrix[i][j].toString(), histPixelMatrix[i][j].toString());
      }
    }
  }


  @Test
  public void testColorCorrect() {
    String commands = "load res/dummy.jpg dummy\ncolor-correct dummy dummy-color-corrected";
    InputStreamController controller = new InputStreamController(
            new InputStreamReader(
                    new ByteArrayInputStream(
                            commands.getBytes(StandardCharsets.UTF_8))));
    ImageModel model = new ImageModel();
    controller.start(model);

    Pixel[][] pixelMatrix = model.IMAGES_PIXEL_MATRICES.get("dummy-color-corrected");
    Pixel p = new Pixel(137, 137, 137);
    Pixel[][] expectedPixelMatrix = {{p, p, p}, {p, p, p}, {p, p, p}};

    for (int i = 0; i < expectedPixelMatrix.length; i++) {
      for (int j = 0; j < expectedPixelMatrix[0].length; j++) {
        assertEquals("Pixel did not match for generated color corrected image! "
                        + i + " " + j,
                expectedPixelMatrix[i][j].toString(), pixelMatrix[i][j].toString());
      }
    }
  }


  @Test
  public void testLevelsAdjustment() {
    String commands =
            "load res/dummy.jpg dummy\nlevels-adjust 20 100 255 dummy dummy-levels-adjusted";
    InputStreamController controller = new InputStreamController(
            new InputStreamReader(
                    new ByteArrayInputStream(
                            commands.getBytes(StandardCharsets.UTF_8))));
    ImageModel model = new ImageModel();
    controller.start(model);

    Pixel[][] pixelMatrix = model.IMAGES_PIXEL_MATRICES.get("dummy-levels-adjusted");
    Pixel p = new Pixel(187, 240, 38);
    Pixel[][] expectedPixelMatrix = {{p, p, p}, {p, p, p}, {p, p, p}};

    for (int i = 0; i < expectedPixelMatrix.length; i++) {
      for (int j = 0; j < expectedPixelMatrix[0].length; j++) {
        assertEquals("Pixel did not match for generated levels adjusted image! "
                        + i + " " + j,
                expectedPixelMatrix[i][j].toString(), pixelMatrix[i][j].toString());
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLevelsAdjustmentOutofBoundsParams() {
    String commands =
            "load res/dummy.jpg dummy\nlevels-adjust -20 100 355 dummy dummy-levels-adjusted";
    InputStreamController controller = new InputStreamController(
            new InputStreamReader(
                    new ByteArrayInputStream(
                            commands.getBytes(StandardCharsets.UTF_8))));
    ImageModel model = new ImageModel();
    controller.start(model);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLevelsAdjustmentInvalidOrdering() {
    String commands =
            "load res/dummy.jpg dummy\nlevels-adjust 20 200 140 dummy dummy-levels-adjusted";
    InputStreamController controller = new InputStreamController(
            new InputStreamReader(
                    new ByteArrayInputStream(
                            commands.getBytes(StandardCharsets.UTF_8))));
    ImageModel model = new ImageModel();
    controller.start(model);
  }


  @Test
  public void testScriptParsingInvalidCommand() {
    // Initialize imageModel
    ImageModel imageModel = new ImageModel();

    // Initialize the ImageProcessingController to begin the image processing application
    InputStreamController imageProcessor;

    // check if script file is provided as input to command line argument
    String command = "run test/script.txt";
    imageProcessor = new InputStreamController(new InputStreamReader(
            new ByteArrayInputStream(
                    command.getBytes(StandardCharsets.UTF_8))));


    // Initiate the image processing application
    imageProcessor.start(imageModel);

    String consoleOutput = errContent.toString().trim();

    // Assert against the expected output
    assertEquals("The command you entered is syntactically not correct. "
            + "Please enter the correct command : run test/script.txt", consoleOutput);
  }


  @Test
  public void testScriptParsingInvalidImagePath() {
    // Initialize imageModel
    ImageModel imageModel = new ImageModel();

    // Initialize the ImageProcessingController to begin the image processing application
    InputStreamController imageProcessor;

    // check if script file is provided as input to command line argument
    String command = "run test/script2.txt";
    imageProcessor = new InputStreamController(new InputStreamReader(
            new ByteArrayInputStream(
                    command.getBytes(StandardCharsets.UTF_8))));


    // Initiate the image processing application
    imageProcessor.start(imageModel);

    String consoleOutput = errContent.toString().trim();

    // Assert against the expected output
    assertEquals("Could not open file - panda.jpg", consoleOutput);
  }

  @Test
  public void testScriptParsingValidCommand() {
    outContent.reset();
    errContent.reset();
    // Initialize imageModel
    ImageModel imageModel = new ImageModel();

    // Initialize the ImageProcessingController to begin the image processing application
    InputStreamController imageProcessor;

    // check if script file is provided as input to command line argument
    String command = "run test/script3.txt";
    imageProcessor = new InputStreamController(new InputStreamReader(
            new ByteArrayInputStream(
                    command.getBytes(StandardCharsets.UTF_8))));


    // Initiate the image processing application
    imageProcessor.start(imageModel);

    String consoleOutput = errContent.toString().trim();

    // Assert against the expected output
    assertEquals("", consoleOutput);
  }


  @Test
  public void testCompressionPNGInvalidValues() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      String commands = "load res/test.png test\ncompress 100 test test-100";
      InputStreamController controller = new InputStreamController(
              new InputStreamReader(
                      new ByteArrayInputStream(
                              commands.getBytes(StandardCharsets.UTF_8))));
      ImageModel model = new ImageModel();
      controller.start(model);
      String consoleOutput = errContent.toString().trim();

      // Assert against the expected output
      assertEquals("The percentage values are invalid. Please enter valid values.",
              consoleOutput);
    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

  @Test
  public void testSplitInvalidValues() {
    String outputPath = "./res/test.png";

    Pixel[][] image = pngImage.readImage(outputPath);
    pngImage.writeImage(outputPath, image);

    try {
      BufferedImage writtenImage = ImageIO.read(new File(outputPath));
      assertNotNull("Written image should not be null", writtenImage);

      String commands = "load res/test.png test\nblur test test-blur split 120";
      InputStreamController controller = new InputStreamController(
              new InputStreamReader(
                      new ByteArrayInputStream(
                              commands.getBytes(StandardCharsets.UTF_8))));
      ImageModel model = new ImageModel();
      controller.start(model);
      String consoleOutput = errContent.toString().trim();

      // Assert against the expected output
      assertEquals("The split percentage values are invalid. Please enter valid values.",
              consoleOutput);
    } catch (IOException e) {
      fail("Error reading the written image file");
    }
  }

}

