package image;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

import static java.lang.Math.max;

/**
 * class that contains array of subPhotos object to help us not calculate
 * for the same partition on the same photo twice.
 */
public class ImageSuitability {
    private static final Color DEFAULT_COLOR = Color.WHITE;
    private static final int  MAX_RGB = 255;
    private Image image;
    private double[][] greyColors;
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
    public ImageSuitability(Image originalImage, int resolution) throws IOException {
        image = createImageRightSizes(originalImage);
        this.resolution = resolution;
        numImagesCol = image.getHeight() / (image.getWidth() / resolution);
        sizeSquare = image.getWidth() / resolution;
        greyColors = new double[image.getWidth()][image.getHeight()];
        setGreyColors();
        greyLevelsSubImages = new double[resolution][numImagesCol];
        setGreyColors();

    }


    public void setResolution(int newResolution){
        if (newResolution != resolution){
            resolution*=newResolution;
            numImagesCol = image.getHeight() / (image.getWidth() / resolution);
            sizeSquare = image.getWidth() / resolution;
        }
        setGreyLevelsSubImages();
        
    }

    public void setImage(String imageName) throws IOException {
        Image imageOriginal = new Image(imageName);
        image = createImageRightSizes(imageOriginal);
        setGreyColors();
        setGreyLevelsSubImages();
    }

    private void setGreyLevelsSubImages(){
        for (int indexRow = 0; indexRow < resolution; indexRow++) {
            for (int indexCol = 0; indexCol < numImagesCol; indexCol++) {
                greyLevelsSubImages[indexRow][indexCol] = greyLevelCalculatorSubImage(indexRow,indexCol);
            }
        }
    }
    private double greyLevelCalculatorSubImage(int indexRow, int indexCol){
        double sum = 0;
        for (int row = 0; row < sizeSquare; row++){
            for (int col = 0; col < sizeSquare; col++){
                sum += greyColors[row+indexRow*sizeSquare][col+indexCol*sizeSquare];
            }
        }
        return sum/(MAX_RGB* sizeSquare*sizeSquare);
    }

    /**
     *
     */
    private void setGreyColors(){
        for (int row = 0; row < image.getWidth(); row++){
            for (int col = 0 ; col < image.getHeight(); col++){
                Color currentPixel = image.getPixel(row,col);
                greyColors[row][col] = currentPixel.getRed()*0.2126 +
                        currentPixel.getGreen()*0.7152 +
                        currentPixel.getBlue()*0.0722;
            }
        }
    }

    /**
     * padds the image given sizes to be a power of two.
     *
     * @param originalImage image given.
     * @return image in right sizes.
     * @throws IOException
     */
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

    /**
     * fill color array with the image color array in the right
     * places and puts White in the places added.
     * @param pixelArray pixel array to fill
     * @param originalImage original image to take from
     * @param diffRow how much rows to put default values
     * @param diffCol how much cols to put default values
     */
    private static void fillingPixelArray(Color[][] pixelArray, Image originalImage, int diffRow, int diffCol) {
        for (Color[] e : pixelArray) {
            Arrays.fill(e, DEFAULT_COLOR);
        }
        for (int row = diffRow / 2; row < pixelArray.length - diffRow / 2; row++) {
            for (int col = diffCol / 2; col < pixelArray[row].length - diffCol / 2; col++) {
                pixelArray[row][col] = originalImage.getPixel(row, col);
            }
        }
    }

    /**
     * tell which sizes are fitting to pad to the array.
     * @param width width that need to find power of two closest.
     * @param height height that need to find power of two closest.
     * @return array of power of two sizes.
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


    /**
     * get sizeSquare
     * @return sizeSquare
     */
    public int getSizeSquare() {
        return sizeSquare;
    }

    /**
     * get resolution
     * @return resolution
     */
    public int getresolution() {
        return resolution;
    }

    /**
     * get numImagesCol
     * @return numImagesCol
     */
    public int getnumImagesCol() {
        return numImagesCol;
    }
}
