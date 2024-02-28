package ascii_art;

import ascii_output.AsciiOutput;
import image.Image;
import image.ImageSuitability;
import image.SubPhoto;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;

/**
 * Class responsible on running the algorithm.
 * Obviously depends on the image given, resolution, char option.
 */
public class AsciiArtAlgorithm {
    private static final String IOEXCEPTION_MESSAGE = "Did not execute due to problem with image file.\n";

    /**
     * constructor for class
     * @param resolution number of sum-images in each row
     */
    public AsciiArtAlgorithm(int resolution){
        // resolution is the number of sub-images (chars) in each row. the number of
        // chars in each column is given by the resolution and the image size (each char is a square)
    }

    /**
     * runs the algorithm and returns the char array
     * @return char array representing image
     */
    public char [][] run(){
        String imageName = "examples/board.jpeg";
        char[] charSet =new char[]{'m','o'};
        int resolution = 2;
        SubImgCharMatcher subImgCharMatcher = new SubImgCharMatcher(charSet);
        try {
            Image image = new Image(imageName);
            ImageSuitability imageSuitability = new ImageSuitability(image,resolution);
            SubPhoto[][] subPhotoArray = imageSuitability.getArraySubPhoto();
            char[][] letters = new char[imageSuitability.getNumPhotosRow()][imageSuitability.getNumPhotosCol()];
            for(int indexRow = 0; indexRow<imageSuitability.getNumPhotosRow(); indexRow++){
                for (int indexCol = 0; indexCol<imageSuitability.getNumPhotosCol(); indexCol++){
                    letters[indexRow][indexCol] =
                            subImgCharMatcher.getCharByImageBrightness(
                                    subPhotoArray[indexRow][indexCol].getGreyLevel());
                }
            }
            return letters;
        }
        catch (IOException ioException){
            System.out.println(IOEXCEPTION_MESSAGE);
        }
        return new char[1][1];
    }
    public static void main(String[] args) {
        int resolution = 2;
        AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(resolution);
        System.out.println(asciiArtAlgorithm.run()[0][1]);
    }
}



