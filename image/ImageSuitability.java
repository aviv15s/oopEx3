package image;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class ImageSuitability{
    private static final Color WHITE_COLOR = Color.WHITE;
    private SubPhoto[][] arraySubPhoto;
    public int numPhotosRow;
    public int numPhotosCol;
    /**
     * Constructor that get image and resoultion and pad the photo seperate to
     * subimages and contains array of SubPhoto.
     * @param originalImage any image
     * @param resolution which resolution to seperate with
     * @throws IOException exception return
     */
    public ImageSuitability(Image originalImage,int resolution) throws IOException{
        Image image = setImageRightSizes(originalImage);
        numPhotosRow = resolution;
        numPhotosCol = image.getHeight()/(image.getWidth()/resolution);
        int sizeSquare = image.getWidth()/resolution;
        arraySubPhoto = new SubPhoto[numPhotosRow][numPhotosCol];
        for (int indexRow = 0; indexRow<numPhotosRow; indexRow++){
            for (int indexCol = 0; indexCol<numPhotosCol; indexCol++){
                arraySubPhoto[indexRow][indexCol] =new SubPhoto(image,
                        sizeSquare,
                        indexRow*sizeSquare,
                        indexCol*sizeSquare);
            }
        }
    }

    /**
     *
     * @param originalImage
     * @return
     * @throws IOException
     */
    public static Image setImageRightSizes(Image originalImage) throws IOException{
        int[] sizes = rightSizes(originalImage.getWidth(), originalImage.getHeight());
        int width = sizes[0];
        int height = sizes[1];
        Color[][] pixelArray = new Color[width][height];
        fillingPixelArray(pixelArray,
                originalImage,
                width - originalImage.getWidth(),
                height - originalImage.getHeight());
        return new Image(pixelArray,width,height);
    }
    private static  void fillingPixelArray(Color[][] pixelArray, Image originalImage, int diffRow, int diffCol){
        for (Color[] e : pixelArray) {
            Arrays.fill(e, WHITE_COLOR);
        }
        for (int row = diffRow/2; row < pixelArray.length - diffRow/2; row++){
            for (int col = diffCol/2; col < pixelArray[row].length- diffCol/2; col++){
                pixelArray[row][col]  = originalImage.getPixel(row,col);
            }
        }
    }
    private static int[] rightSizes(int width, int height){
        int[] rightSizes = new int[2];
        int biggerNumber = 1;
        while(biggerNumber < width){
            biggerNumber *=2;
        }
        rightSizes[0] = biggerNumber;
        biggerNumber = 1;
        while(biggerNumber < height){
            biggerNumber *=2;
        }
        rightSizes[1] = biggerNumber;
        return rightSizes;
    }

    public SubPhoto[][] getArraySubPhoto() {
        return arraySubPhoto;
    }
}
