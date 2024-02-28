package image;

import java.awt.*;

public class SubPhoto {
    private static final int  MAX_RGB = 255;
    private double[][] pixelGreyArray;
    private double  greyLevel;



    public SubPhoto(Image image,int resolution,int startRow,int startCol) {

        pixelGreyArray = new double[resolution][resolution];
        changeToGrey(image, startRow,startCol);
        setGreyLevel();
    }


    private void changeToGrey(Image image,int startRow,int startCol){
        for (int row = 0; row < image.getWidth(); row++){
            for (int col = 0; col < image.getHeight(); col++){
                Color currentPixel = image.getPixel(row,col);
                pixelGreyArray[row][col] = currentPixel.getRed()*0.2126 +
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
        greyLevel = sum/MAX_RGB;
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
