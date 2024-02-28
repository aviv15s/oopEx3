package image;

import java.awt.*;

public class SubPhoto {
    private static final int  MAX_RGB = 255;
    private double[][] pixelGreyArray;
    private double  greyLevel;


    /**
     * construct a subPhoto object
     * @param image to get from
     * @param size size of subphoto
     * @param startRow row to start looking from the color
     * @param startCol col to start looking from the color
     */
    public SubPhoto(Image image,int size,int startRow,int startCol) {
        pixelGreyArray = new double[size][size];
        changeToGrey(image, size, startRow, startCol);
        setGreyLevel();
    }

    /**
     * calculate greyness of each pixel in pixelGreyArray the grey level
     * @param image to look from
     * @param size size to look in col and row
     * @param startRow row to start looking from
     * @param startCol col to start looking from
     */
    private void changeToGrey(Image image,int size, int startRow,int startCol){
        for (int row = startRow; row < startRow+size; row++){
            for (int col = startCol; col < startCol+size; col++){
                Color currentPixel = image.getPixel(row,col);
                pixelGreyArray[row-startRow][col-startCol] = currentPixel.getRed()*0.2126 +
                        currentPixel.getGreen()*0.7152 +
                        currentPixel.getBlue()*0.0722;
            }
        }
    }

    /**
     * calculate Grey level of all the subImage
     */
    public void setGreyLevel(){
        double sum = 0;
        for (int row = 0; row < pixelGreyArray.length; row++){
            for (int col = 0; col < pixelGreyArray[row].length; col++){
                sum += pixelGreyArray[row][col];
            }
        }
        greyLevel = sum/(MAX_RGB* pixelGreyArray.length*pixelGreyArray[0].length);
    }

    /**
     *
     * @return greyLevel of the class
     */
    public double getGreyLevel() {
        return greyLevel;
    }

    /**
     *
     * @return pixelGreyArray of the class
     */
    public double[][] getPixelGreyArray() {
        return pixelGreyArray;
    }
}
