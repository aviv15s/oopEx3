package ascii_art;

import image.Image;
import image.ImageSuitability;
import image_char_matching.SubImgCharMatcher;

/**
 * Class responsible on running the algorithm.
 * Obviously depends on the src.image given, resolution, char option.
 * @author aviv.shemesh, ram3108_
 */
public class AsciiArtAlgorithm {
    private ImageSuitability imageSuitability;
    private SubImgCharMatcher subImgCharMatcher;

    /**
     * constructor for class
     *
     * @param resolution number of sum-images in each row
     */
    public AsciiArtAlgorithm(int resolution, Image image, SubImgCharMatcher subImgCharMatcher) {
        this.imageSuitability = new ImageSuitability(image, resolution);
        this.subImgCharMatcher = subImgCharMatcher;
    }

    /**
     * runs the algorithm and returns the char array
     * @return char array representing src.image
     */
    public char[][] run() {
        char[][] letters = new char[imageSuitability.getnumImagesCol()][imageSuitability.getResolution()];
        double[][] greyLevelsSubImages = imageSuitability.getGrayscaleSubimages();
        for (int indexRow = 0; indexRow < imageSuitability.getResolution(); indexRow++) {
            for (int indexCol = 0; indexCol < imageSuitability.getnumImagesCol(); indexCol++) {
                letters[indexCol][indexRow] =
                        subImgCharMatcher.getCharByImageBrightness(greyLevelsSubImages[indexCol][indexRow]);
            }
        }
        return letters;
    }

    /**
     * set the resolution. only updates the value if the input is valid.
     * @param newResolution new resolution valid to change to
     * @return true if value is valid, else false
     */
    public boolean setResolution(int newResolution) {
        return imageSuitability.setResolution(newResolution);
    }

    /**
     * get resolution
     * @return resolution
     */
    public int getResolution(){
        return imageSuitability.getResolution();
    }

}






