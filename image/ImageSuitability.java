package image;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

/**
 * class that contains array of subPhotos object to help us not calculate
 * for the same partition on the same photo twice.
 */
public class ImageSuitability {
    private static final Color DEFAULT_COLOR = Color.WHITE;
    private SubPhoto[][] arraySubPhoto;
    private final int sizeSquare;
    private final int numPhotosRow;
    private final int numPhotosCol;

    /**
     * Constructor that get image and resolution and pad the photo seperates to
     * subimages and contains array of SubPhoto.
     *
     * @param originalImage any image
     * @param resolution    which resolution to separates with
     * @throws IOException exception return
     */
    public ImageSuitability(Image originalImage, int resolution) throws IOException {
        Image image = setImageRightSizes(originalImage);
        numPhotosRow = resolution;
        numPhotosCol = image.getHeight() / (image.getWidth() / resolution);
        sizeSquare = image.getWidth() / resolution;
        arraySubPhoto = new SubPhoto[numPhotosRow][numPhotosCol];
        for (int indexRow = 0; indexRow < numPhotosRow; indexRow++) {
            for (int indexCol = 0; indexCol < numPhotosCol; indexCol++) {
                arraySubPhoto[indexRow][indexCol] = new SubPhoto(image,
                        sizeSquare,
                        indexRow * sizeSquare,
                        indexCol * sizeSquare);
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
    public static Image setImageRightSizes(Image originalImage) throws IOException {
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
     * get arraySubPhoto
     * @return arraySubPhoto
     */
    public SubPhoto[][] getArraySubPhoto() {
        return arraySubPhoto;
    }

    /**
     * get sizeSquare
     * @return sizeSquare
     */
    public int getSizeSquare() {
        return sizeSquare;
    }

    /**
     * get numPhotosRow
     * @return numPhotosRow
     */
    public int getNumPhotosRow() {
        return numPhotosRow;
    }

    /**
     * get numPhotosCol
     * @return numPhotosCol
     */
    public int getNumPhotosCol() {
        return numPhotosCol;
    }
}
