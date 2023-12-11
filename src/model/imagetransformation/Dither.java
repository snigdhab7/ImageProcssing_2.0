package model.imagetransformation;

import java.util.Map;
import model.image.Pixel;

/**
 * Transformation class that applies dithering to convert an image to black and white.
 */
public class Dither extends AbstractImageTransformation {

    /**
     * Applies dithering to convert the input image to black and white.
     *
     * @param inputFileName       The name of the input image file.
     * @param outputFileName      The name of the output image file after dithering.
     * @param imagesPixelMatrices A map containing pixel matrices of different images.
     * @param otherParams         Additional parameters or configurations for the transformation.
     * @return A two-dimensional array of pixels representing the dithered image.
     */
    @Override
    public Pixel[][] generate(String inputFileName, String outputFileName,
                              Map<String, Pixel[][]> imagesPixelMatrices,
                              Object... otherParams) {

        Pixel[][] pixelMatrix = imagesPixelMatrices.get(inputFileName);
        int width = pixelMatrix.length;
        int height = pixelMatrix[0].length;
        Pixel[][] matrixToBeOperatedOn = new Pixel[width][height];



        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                matrixToBeOperatedOn[i][j] = new Pixel(
                        pixelMatrix[i][j].getRed(),
                        pixelMatrix[i][j].getGreen(),
                        pixelMatrix[i][j].getBlue());
            }

        }
        Pixel[][] ditheredImage = applyDithering(matrixToBeOperatedOn, width, height);

        Pixel[][] outputPixelMatrix = new Pixel[width][height];
        double splitPercentage = (double) otherParams[0];
        double splitViewWidth = (splitPercentage > 0) ? height
                * splitPercentage : height;
        Pixel pixel;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixel = pixelMatrix[i][j];
                if (j < splitViewWidth) {
                    outputPixelMatrix[i][j] = new Pixel(
                            Pixel.clamp(ditheredImage[i][j].getRed()),
                            Pixel.clamp(ditheredImage[i][j].getGreen()),
                            Pixel.clamp(ditheredImage[i][j].getBlue()));

                } else {
                    outputPixelMatrix[i][j] = pixel;
                }
            }
        }

        return outputPixelMatrix;
        //return ditheredImage;
    }

    private Pixel[][] applyDithering(Pixel[][] pixelMatrix, int width, int height) {
        Pixel[][] ditheredImage = new Pixel[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int oldColor = pixelMatrix[x][y].getBlue();
                int newColor = (oldColor < 128) ? 0 : 255;
                int quantError = oldColor - newColor;
                ditheredImage[x][y] = new Pixel(newColor, newColor, newColor);

                applyErrorDiffusion(pixelMatrix, x, y, width, height, quantError);
            }
        }

        return ditheredImage;
    }

    private void applyErrorDiffusion(Pixel[][] pixelMatrix, int x, int y, int width, int height, int quantError) {
        if (x + 1 < width) {
            distributeError(pixelMatrix, x + 1, y, quantError, 7, width, height);
        }
        if (x - 1 >= 0 && y + 1 < height) {
            distributeError(pixelMatrix, x - 1, y + 1, quantError, 3, width, height);
        }
        if (y + 1 < height) {
            distributeError(pixelMatrix, x, y + 1, quantError, 5, width, height);
        }
        if (x + 1 < width && y + 1 < height) {
            distributeError(pixelMatrix, x + 1, y + 1, quantError, 1, width, height);
        }
    }

    private void distributeError(Pixel[][] pixelMatrix, int x, int y, int quantError, int factor, int width, int height) {
        int temp = pixelMatrix[x][y].getBlue();
        temp += (quantError * factor) / 16;
        pixelMatrix[x][y] = new Pixel(temp, temp, temp);
    }


}
