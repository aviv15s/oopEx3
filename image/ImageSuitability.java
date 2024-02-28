package image;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

import static java.lang.Math.max;

/**
 * class that helps us control on the grey object comfortably.
 */
public class ImageSuitability {
    private static final int MAX_RGB = 255;
    private final double[][] greyImage;
    private double[][] greyLevelsSubImages;
    private int sizeSquare;
    private int resolution;
    private int numImagesCol;

    /**
     * Constructor that get image and resolution and pad the photo seperates to
     * subimages and contains array of SubPhoto.
     *
     * @param originalImage any image
     * @param resolution    which resolution to separates with
     * @throws IOException exception return
     */
    public ImageSuitability(Image originalImage, int resolution) {
        greyImage = PaddingGreyImage.setgreyImage(PaddingGreyImage.createImageRightSizes(originalImage));
        this.resolution = resolution;
        this.sizeSquare = greyImage.length / resolution;
        this.numImagesCol = greyImage[0].length / sizeSquare;

        greyLevelsSubImages = new double[resolution][numImagesCol];
        setGreyLevelsSubImages();

    }

    /**
     * Sets a new resolution for the ImageSuitability instance.
     * If the new resolution differs from the current one, it recalculates the sizeSquare,
     * numImagesCol, and updates the greyLevelsSubImages array accordingly.
     *
     * @param newResolution The new resolution to be set.
     */
    public void setResolution(int newResolution) {
        if (newResolution != resolution) {
            resolution = newResolution;
            sizeSquare = greyImage.length / resolution;
            numImagesCol = greyImage[0].length / sizeSquare;
        }
        greyLevelsSubImages = new double[resolution][numImagesCol];
        setGreyLevelsSubImages();
    }

    /**
     * Populates the greyLevelsSubImages array by calculating the grey levels for each subimage.
     * This method iterates through the subimage grid and uses the greyLevelCalculatorSubImage method
     * to determine the grey level for each subimage and stores the result in the corresponding array element.
     */
    private void setGreyLevelsSubImages() {
        for (int indexRow = 0; indexRow < resolution; indexRow++) {
            for (int indexCol = 0; indexCol < numImagesCol; indexCol++) {
                greyLevelsSubImages[indexRow][indexCol] = greyLevelCalculatorSubImage(indexRow, indexCol);
            }
        }
    }

    /**
     * calculate for specific subImage the greyness
     *
     * @param indexRow row to start from
     * @param indexCol col to look from
     * @returngreness of that subImage
     */
    private double greyLevelCalculatorSubImage(int indexRow, int indexCol) {
        double sum = 0;
        for (int row = 0; row < sizeSquare; row++) {
            for (int col = 0; col < sizeSquare; col++) {
                sum += greyImage[row + indexRow * sizeSquare][col + indexCol * sizeSquare];
            }
        }
        return sum / (MAX_RGB * sizeSquare * sizeSquare);
    }

    /**
     * get resolution
     *
     * @return resolution
     */
    public int getresolution() {
        return resolution;
    }

    /**
     * get greyLevelsSubImages
     *
     * @return greyLevelsSubImages
     */
    public double[][] getGreyLevelsSubImages() {
        return greyLevelsSubImages;
    }

    /**
     * get numImagesCol
     *
     * @return numImagesCol
     */
    public int getnumImagesCol() {
        return numImagesCol;
    }

    public static class PaddingGreyImage {
        private static final Color DEFAULT_COLOR = Color.WHITE;
        private static final double RED_FACTOR = 0.2126;
        private static final double BLUE_FACTOR = 0.0722;
        private static final double GREEN_FACTOR = 0.7152;

        /**
         * Converts a given color image to a grayscale representation.
         *
         * @param image The input color image.
         * @return A 2D array representing the grayscale image.
         */
        public static double[][] setgreyImage(Image image) {
            double[][] greyImage = new double[image.getWidth()][image.getHeight()];
            for (int row = 0; row < image.getWidth(); row++) {
                for (int col = 0; col < image.getHeight(); col++) {
                    Color currentPixel = image.getPixel(row, col);
                    greyImage[row][col] = currentPixel.getRed() * RED_FACTOR +
                            currentPixel.getGreen() * GREEN_FACTOR +
                            currentPixel.getBlue() * BLUE_FACTOR;
                }
            }
            return greyImage;
        }

        /**
         * Creates a new image with dimensions adjusted to the nearest power of 2.
         * Pads the image with the default color if necessary.
         *
         * @param originalImage The input image to be adjusted.
         * @return A new image with adjusted dimensions.
         */
        public static Image createImageRightSizes(Image originalImage) {
            int[] sizes = rightSizes(originalImage.getWidth(), originalImage.getHeight());
            int width = sizes[0];
            int height = sizes[1];
            Color[][] pixelArray = new Color[width][height];
            fillingPixelArray(pixelArray,
                    originalImage,
                    width - originalImage.getWidth(),
                    height - originalImage.getHeight());
            return new Image(pixelArray, width, height);
        }

        /**
         * Fills a given pixel array with the default color, then copies the pixels from the
         * original image to the center of the array.
         *
         * @param pixelArray    The pixel array to be filled and modified.
         * @param originalImage The original image to copy pixels from.
         * @param diffCol       The difference in columns between arrays.
         * @param diffRow       The difference in rows between arrays.
         */
        private static void fillingPixelArray(Color[][] pixelArray, Image originalImage, int diffCol, int diffRow) {
            for (Color[] e : pixelArray) {
                Arrays.fill(e, DEFAULT_COLOR);
            }
            for (int row = diffRow / 2; row < pixelArray.length - diffRow / 2; row++) {
                for (int col = diffCol / 2; col < pixelArray[row].length - diffCol / 2; col++) {
                    pixelArray[row][col] = originalImage.getPixel(row - diffRow / 2, col - diffCol / 2);
                }
            }
        }

        /**
         * Calculates the nearest power of 2 for width and height and returns as an array.
         *
         * @param width  The original width.
         * @param height The original height.
         * @return An array containing the adjusted width and height.
         */
        private static int[] rightSizes(int width, int height) {
            int[] rightSizes = new int[2];
            int biggerNumber = 1;
            while (biggerNumber < width) {
                biggerNumber *= 2;
            }
            rightSizes[0] = biggerNumber;
            biggerNumber = 1;
            while (biggerNumber < height) {
                biggerNumber *= 2;
            }
            rightSizes[1] = biggerNumber;
            return rightSizes;
        }
    }
}
