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
     */
    public ImageSuitability(Image originalImage, int resolution) {
        greyImage = PaddingGreyImage.setgreyImage(PaddingGreyImage.createImageRightSizes(originalImage));
        this.resolution = resolution;
        this.sizeSquare = greyImage.length / resolution;
        this.numImagesCol = greyImage[0].length / sizeSquare;

        greyLevelsSubImages = new double[resolution][numImagesCol];
        setGreyLevelsSubImages();

    }

    public void setResolution(int newResolution){
        if (newResolution != resolution){
            resolution = newResolution;
            sizeSquare = greyImage.length / resolution;
            numImagesCol = greyImage[0].length / sizeSquare;
        }
        greyLevelsSubImages = new double[resolution][numImagesCol];
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
