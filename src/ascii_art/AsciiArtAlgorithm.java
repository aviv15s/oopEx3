package src.ascii_art;

import src.image.Image;
import src.image.ImageSuitability;
import src.image_char_matching.SubImgCharMatcher;

/**
 * Class responsible on running the algorithm.
 * Obviously depends on the src.image given, resolution, char option.
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
     *
     * @return char array representing src.image
     */
    public char[][] run() {
        char[][] letters = new char[imageSuitability.getnumImagesCol()][imageSuitability.getresolution()];
        double[][] greyLevelsSubImages = imageSuitability.getGrayscaleSubimages();
        for (int indexRow = 0; indexRow < imageSuitability.getresolution(); indexRow++) {
            for (int indexCol = 0; indexCol < imageSuitability.getnumImagesCol(); indexCol++) {
                letters[indexCol][indexRow] =
                        subImgCharMatcher.getCharByImageBrightness(greyLevelsSubImages[indexCol][indexRow]);
            }
        }
        return letters;
    }


    public boolean setResolution(int resolution) {
        return imageSuitability.setResolution(resolution);
    }

    public int getResolution(){
        return imageSuitability.getResolution();
    }

}






