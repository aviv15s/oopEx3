package image;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

import static java.lang.Math.max;

/**
 * class that helps us control on the grey object comfortably.
 */
public class ImageSuitability {
    private static final int  MAX_RGB = 255;
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
    public void setResolution(int newResolution){
        if (newResolution != resolution){
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
    private void setGreyLevelsSubImages(){
        for (int indexRow = 0; indexRow < resolution; indexRow++) {
            for (int indexCol = 0; indexCol < numImagesCol; indexCol++) {
                greyLevelsSubImages[indexRow][indexCol] = greyLevelCalculatorSubImage(indexRow,indexCol);
            }
        }
    }

    /**
     * calculate for specific subImage the greyness
     * @param indexRow row to start from
     * @param indexCol col to look from
     * @returngreness of that subImage
     */
    private double greyLevelCalculatorSubImage(int indexRow, int indexCol){
        double sum = 0;
        for (int row = 0; row < sizeSquare; row++){
            for (int col = 0; col < sizeSquare; col++){
                sum += greyImage[row+indexRow*sizeSquare][col+indexCol*sizeSquare];
            }
        }
        return sum/(MAX_RGB* sizeSquare*sizeSquare);
    }

    public int getresolution() {
        return resolution;
    }

    public double[][] getGreyLevelsSubImages() {
        return greyLevelsSubImages;
    }

    public int getnumImagesCol() {
        return numImagesCol;
    }

    static class PaddingGreyImage{
        private static final Color DEFAULT_COLOR = Color.WHITE;
        public static double[][] setgreyImage(Image image){
            double[][] greyImage = new double[image.getWidth()][image.getHeight()];
            for (int row = 0; row < image.getWidth(); row++){
                for (int col = 0 ; col < image.getHeight(); col++){
                    Color currentPixel = image.getPixel(row,col);
                    greyImage[row][col] = currentPixel.getRed()*0.2126 +
                            currentPixel.getGreen()*0.7152 +
                            currentPixel.getBlue()*0.0722;
                }
            }
            return greyImage;
        }
        public static Image createImageRightSizes(Image originalImage){
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


        private static void fillingPixelArray(Color[][] pixelArray, Image originalImage, int diffCol, int diffRow) {
            for (Color[] e : pixelArray) {
                Arrays.fill(e, DEFAULT_COLOR);
            }
            for (int row = diffRow / 2; row < pixelArray.length - diffRow / 2; row++) {
                for (int col = diffCol / 2; col < pixelArray[row].length - diffCol / 2; col++) {
                    pixelArray[row][col] = originalImage.getPixel(row-diffRow/2, col-diffCol/2);
                }
            }
        }


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
