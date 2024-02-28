package ascii_art;

import image.Image;
import image.ImageSuitability;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;

/**
 * Class responsible on running the algorithm.
 * Obviously depends on the image given, resolution, char option.
 */
public class AsciiArtAlgorithm {
    private ImageSuitability imageSuitability;
    private SubImgCharMatcher subImgCharMatcher;
    private char[] charSet;

    /**
     * constructor for class
     *
     * @param resolution number of sum-images in each row
     */
    public AsciiArtAlgorithm(int resolution, Image image, char[] charSet){
        this.imageSuitability = new ImageSuitability(image, resolution);
        this.subImgCharMatcher = new SubImgCharMatcher(charSet);
        this.charSet = charSet;
    }

    /**
     * runs the algorithm and returns the char array
     *
     * @return char array representing image
     */
    public char[][] run() {
        char[][] letters = new char[imageSuitability.getresolution()][imageSuitability.getnumImagesCol()];
        double[][] greyLevelsSubImages = imageSuitability.getGreyLevelsSubImages();
        for (int indexRow = 0; indexRow < imageSuitability.getresolution(); indexRow++) {
            for (int indexCol = 0; indexCol < imageSuitability.getnumImagesCol(); indexCol++) {
                letters[indexRow][indexCol] =
                        subImgCharMatcher.getCharByImageBrightness(greyLevelsSubImages[indexRow][indexCol]);
            }
        }
        return letters;
    }
    public void addChar(char letter) {
        subImgCharMatcher.addChar(letter);
    }
    public void removeChar(char letter){
        subImgCharMatcher.removeChar(letter);
    }
    public char[] getCharSet() {
        return charSet;
    }

    public void setResolution(int resolution) {
        setResolution(resolution);
    }

    public static void main(String[] args) {
        int resolution = 2;
        char[] charSet =new char[]{'m','o'};
        String name = "examples/board.jpeg";
        try {
            AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(resolution, new Image(name), charSet);
        }
        catch (IOException ioException) {
            System.out.println(1);
        }
    }
}






