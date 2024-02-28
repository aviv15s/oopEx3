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
//    private ImageSuitability imageSuitability;
//    private SubImgCharMatcher subImgCharMatcher;
    private int resolution;
    private String imageName;
    private char[] charSet;

    /**
     * constructor for class
     *
     * @param resolution number of sum-images in each row
     */
    public AsciiArtAlgorithm(int resolution, String imageName, char[] charSet) throws IOException {
//        this.imageSuitability = new ImageSuitability(new Image(imageName), resolution);
//        this.subImgCharMatcher = new SubImgCharMatcher(charSet);
        this.resolution = resolution;
        this.imageName = imageName;
        this.charSet = charSet;
    }

    /**
     * runs the algorithm and returns the char array
     *
     * @return char array representing image
     */
    public char[][] run() {
        imageSuitability = new ImageSuitability(new Image(imageName), resolution);
        ImageSuitability imageSuitability = new ImageSuitability(image, resolution);
        SubPhoto[][] subPhotoArray = imageSuitability.getArraySubPhoto();
        char[][] letters = new char[imageSuitability.getresolution()][imageSuitability.getnumImagesCol()];
        for (int indexRow = 0; indexRow < imageSuitability.getresolution(); indexRow++) {
            for (int indexCol = 0; indexCol < imageSuitability.getnumImagesCol(); indexCol++) {
                letters[indexRow][indexCol] =
                        subImgCharMatcher.getCharByImageBrightness(
                                subPhotoArray[indexRow][indexCol].getGreyLevel());
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

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public void setCharSet(char[] charSet) {
        this.charSet = charSet;
    }
    //    public static void main(String[] args) {
//        int resolution = 2;
//        char[] charSet =new char[]{'m','o'};
//        String name = "examples/board.jpeg";
//        try:
//            AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(resolution, name, charSet);
//        catch{
//        System.out.println(asciiArtAlgorithm.run()[0][1]);
//    }
//}


}



