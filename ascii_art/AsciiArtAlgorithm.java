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
    private static final String IOEXCEPTION_MESSAGE = "Did not execute due to problem with image file.\n";
    private ImageSuitability imageSuitability;
    private SubImgCharMatcher subImgCharMatcher;
    private int resolution;
    private String imageName;
    private char[] charSet;

    /**
     * constructor for class
     *
     * @param resolution number of sum-images in each row
     */
    public AsciiArtAlgorithm(int resolution, String imageName, char[] charSet) throws IOException {
        this.imageSuitability = new ImageSuitability(new Image(imageName), resolution);
        this.subImgCharMatcher = new SubImgCharMatcher(charSet);
        this.resolution = resolution;
        this.imageName = imageName;
        this.charSet = charSet;
    }

    /**
     * runs the algorithm and returns the char array
     *
     * @return char array representing image
     */
    public char[][] run() throws IOException {
        imageSuitability = new ImageSuitability(new Image(imageName), resolution);
        subImgCharMatcher = new SubImgCharMatcher(charSet);
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
    public String getImageName() {
        return imageName;
    }

    public int getResolution() {
        return resolution;
    }

    public char[] getCharSet() {
        return charSet;
    }

    public void setImageName(String imageName) throws IOException {
        this.imageName = imageName;
        imageSuitability = new ImageSuitability(new Image(imageName), resolution);
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
        imageSuitability.setResolution(resolution);
    }

    public void addChar(char letter) {
        subImgCharMatcher.addChar(letter);
    }
    public void removeChar(char letter){
        subImgCharMatcher.removeChar(letter);
    }
    public static void main(String[] args) {
        int resolution = 2;
        char[] charSet =new char[]{'m','o'};
        String name = "examples/board.jpeg";
        try {
            AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(resolution, name, charSet);
        }
        catch (IOException ioException) {
            System.out.println(1);
        }
    }
}






