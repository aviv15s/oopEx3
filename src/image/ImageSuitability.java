package src.image;

import java.awt.*;
import java.util.Arrays;

import static java.lang.Math.max;

/**
 * class that helps us control on the grey object comfortably.
 * @author aviv.shemesh, ram_3108
 */
public class ImageSuitability {
    private static final int MAX_RGB = 255;
    private final double[][] greyImage;
    private double[][] grayscaleSubimages;
    private int pixelsInChar;
    private int resolution;
    private int charsInColumn;

    /**
     * Constructor that get src.image and resolution and pad the photo seperates to
     * subimages and contains array of SubPhoto.
     *
     * @param originalImage any src.image
     * @param resolution    which resolution to separates with
     */
    public ImageSuitability(Image originalImage, int resolution) {
        greyImage = PaddingGreyImage.setGreyImage(PaddingGreyImage.createImageRightSizes(originalImage));
        this.resolution = resolution;
        this.pixelsInChar = greyImage[0].length / resolution;
        this.charsInColumn = greyImage.length / pixelsInChar;
        grayscaleSubimages = new double[charsInColumn][resolution];
        setGreyLevelsSubImages();

    }

    /**
     * Sets a new resolution for the ImageSuitability instance.
     * If the new resolution differs from the current one, it recalculates the sizeSquare,
     * numImagesCol, and updates the greyLevelsSubImages array accordingly.
     * only updates if the value is valid
     *
     * @param newResolution The new resolution to be set.
     * @return true if value is valid, else false
     */
    public boolean setResolution(int newResolution) {
        if (newResolution == resolution){
            return true;
        }
        if ((newResolution > greyImage[0].length) ||
                (newResolution < Math.max(1, greyImage[0].length /
                        greyImage.length))) {
            return false;
        }

        resolution = newResolution;
        pixelsInChar = greyImage[0].length / resolution;
        charsInColumn = greyImage.length / pixelsInChar;
        setGreyLevelsSubImages();

        return true;
    }

    /**
     * Returns the resolution
     *
     * @return resolution value
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * Populates the greyLevelsSubImages array by calculating the grey levels for each subimage.
     * This method iterates through the subimage grid and uses the greyLevelCalculatorSubImage method
     * to determine the grey level for each subimage and stores the result in the corresponding array element.
     */
    private void setGreyLevelsSubImages() {
        grayscaleSubimages = new double[charsInColumn][resolution];
        for (int verticalIndex = 0; verticalIndex < charsInColumn; verticalIndex++) {
            for (int horizontalIndex = 0; horizontalIndex < resolution; horizontalIndex++) {
                grayscaleSubimages[verticalIndex][horizontalIndex] = greyLevelCalculatorSubImage(verticalIndex, horizontalIndex);
            }
        }
    }

    /**
     * calculate for specific subImage the greyness
     *
     * @param vertical   row to start from
     * @param horizontal col to look from
     * @returngreness of that subImage
     */
    private double greyLevelCalculatorSubImage(int vertical, int horizontal) {
        double sum = 0;
        for (int verticalIndex = 0; verticalIndex < pixelsInChar; verticalIndex++) {
            for (int horizontalIndex = 0; horizontalIndex < pixelsInChar; horizontalIndex++) {
                sum += greyImage[verticalIndex + vertical * pixelsInChar][horizontalIndex + horizontal * pixelsInChar];
            }
        }
        return sum / (MAX_RGB * pixelsInChar * pixelsInChar);
    }

    /**
     * get greyLevelsSubImages
     *
     * @return greyLevelsSubImages
     */
    public double[][] getGrayscaleSubimages() {
        return grayscaleSubimages;
    }

    /**
     * get numImagesCol
     *
     * @return numImagesCol
     */
    public int getnumImagesCol() {
        return charsInColumn;
    }

    /**
     * get num Chars in column
     *
     * @return charsInColumn
     */
    public int getCharsInColumn() {
        return charsInColumn;
    }

    /**
     * A class the is static because there is no independence
     * on an instance and help us create our grey array of the image.
     */
    public static class PaddingGreyImage {
        private static final Color DEFAULT_COLOR = Color.WHITE;
        private static final double RED_FACTOR = 0.2126;
        private static final double BLUE_FACTOR = 0.0722;
        private static final double GREEN_FACTOR = 0.7152;

        /**
         * Converts a given color src.image to a grayscale representation.
         *
         * @param image The input color src.image.
         * @return A 2D array representing the grayscale src.image.
         */
        public static double[][] setGreyImage(Image image) {
            double[][] grayImage = new double[image.getHeight()][image.getWidth()];
            for (int verticalIndex = 0; verticalIndex < image.getHeight(); verticalIndex++) {
                for (int horizontalIndex = 0; horizontalIndex < image.getWidth(); horizontalIndex++) {
                    Color currentPixel = image.getPixel(verticalIndex, horizontalIndex);
                    grayImage[verticalIndex][horizontalIndex] = currentPixel.getRed() * RED_FACTOR +
                            currentPixel.getGreen() * GREEN_FACTOR +
                            currentPixel.getBlue() * BLUE_FACTOR;
                }
            }
            return grayImage;
        }

        /**
         * Creates a new src.image with dimensions adjusted to the nearest power of 2.
         * Pads the src.image with the default color if necessary.
         *
         * @param originalImage The input src.image to be adjusted.
         * @return A new src.image with adjusted dimensions.
         */
        public static Image createImageRightSizes(Image originalImage) {
            int[] sizes = rightSizes(originalImage.getWidth(), originalImage.getHeight());
            int width = sizes[0];
            int height = sizes[1];
            Color[][] pixelArray = new Color[height][width];
            fillingPixelArray(pixelArray,
                    originalImage,
                    width - originalImage.getWidth(),
                    height - originalImage.getHeight());
            return new Image(pixelArray, width, height);
        }

        /**
         * Fills a given pixel array with the default color, then copies the pixels from the
         * original src.image to the center of the array.
         * @param pixelArray    The pixel array to be filled and modified.
         * @param originalImage The original src.image to copy pixels from.
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
