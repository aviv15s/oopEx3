package src.image_char_matching;

import java.util.HashMap;
import java.util.Set;


/**
 * Class that calculates the brightness values of each character and assigns a charachter
 * to a given brightness level
 * @author aviv.shemesh, ram_3108
 */
public class SubImgCharMatcher {
    private final static boolean WHITE_PIXEL = true;
    private final HashMap<Character, Double> charSet;
    private double maxUnnormalizedBrightness;
    private double minUnnormalizedBrightness;

    /**
     * Constructor
     * @param charset charset used by the algorithm
     */
    public SubImgCharMatcher(char[] charset){
        charSet = new HashMap<Character, Double>();
        minUnnormalizedBrightness = 1;
        maxUnnormalizedBrightness = 0;
        for (int i = 0; i < charset.length; i++) {
            char c = charset[i];
            double charBrightness = calculateCharBrightness(c);
            this.charSet.put(c, charBrightness);

            minUnnormalizedBrightness = Math.min(minUnnormalizedBrightness, charBrightness);
            maxUnnormalizedBrightness = Math.max(maxUnnormalizedBrightness, charBrightness);
        }

        for (HashMap.Entry<Character, Double> entry : this.charSet.entrySet()) {
            double brightness = entry.getValue();
            double newBrightness = (brightness - minUnnormalizedBrightness) /
                    (maxUnnormalizedBrightness - minUnnormalizedBrightness);
            entry.setValue(newBrightness);
        }

    }

    /**
     * used to calculate the brightness of a single charachter
     * @param c character
     * @return brightness value
     */
    private float calculateCharBrightness(char c){
        boolean[][] pixelArray = CharConverter.convertToBoolArray(c);
        int numberOfWhitePixels = 0;
        int numberOfPixels = pixelArray.length * pixelArray[0].length;

        for (int i = 0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[i].length; j++) {
                if (pixelArray[i][j] == WHITE_PIXEL) {
                    numberOfWhitePixels++;
                }
            }
        }
        return (float) numberOfWhitePixels / numberOfPixels;
    }

    /**
     * used to get the char with the closest brightness to the given one
     * @param brightness brightness to match
     * @return a char
     */
    public char getCharByImageBrightness(double brightness){
        double maxDistance = 1;
        char matchingChar = ' ';
        for (HashMap.Entry<Character, Double> entry : this.charSet.entrySet()){
            double distance = Math.abs(brightness - entry.getValue());
            if (distance < maxDistance){
                maxDistance = distance;
                matchingChar = entry.getKey();
            } else if (distance == maxDistance && charSet.get(matchingChar) > entry.getValue()) {
                maxDistance = distance;
                matchingChar = entry.getKey();
            }
        }
        return matchingChar;
    }

    /**
     * adds a char to the charset
     * @param c char
     */
    public void addChar(char c){
        if (charSet.containsKey(c)){
            return;
        }
        float charBrightness = calculateCharBrightness(c);

        // check if needs to re-normalize all characters
        if (charBrightness > maxUnnormalizedBrightness || charBrightness < minUnnormalizedBrightness){
            double newMinUnnormalizedBrightness = Math.min(minUnnormalizedBrightness, charBrightness);
            double newMaxUnnormalizedBrightness = Math.max(maxUnnormalizedBrightness, charBrightness);

            for (HashMap.Entry<Character, Double> entry : this.charSet.entrySet()) {
                double brightness = entry.getValue();
                double unnormalizedCharBrightness = brightness *
                        (maxUnnormalizedBrightness - minUnnormalizedBrightness) +
                        minUnnormalizedBrightness;
                double newBrightness = (unnormalizedCharBrightness - newMinUnnormalizedBrightness) /
                        (newMaxUnnormalizedBrightness - newMinUnnormalizedBrightness);
                entry.setValue(newBrightness);
            }

            maxUnnormalizedBrightness = Math.max(maxUnnormalizedBrightness, charBrightness);
            minUnnormalizedBrightness = Math.min(minUnnormalizedBrightness, charBrightness);
        }

        double normalizedCharBrightness = (charBrightness - minUnnormalizedBrightness) /
                (maxUnnormalizedBrightness - minUnnormalizedBrightness);
        this.charSet.put(c, normalizedCharBrightness);

    }

    /**
     * removes a char from the charset
     * @param c char
     */
    public void removeChar(char c){
        if (!charSet.containsKey(c)){
            return;
        }
        double charBrightness = charSet.get(c);
        charSet.remove(c);

        // check if needs to re-normalize all characters
        if (charBrightness == maxUnnormalizedBrightness || charBrightness == minUnnormalizedBrightness){
            double newMinUnnormalizedBrightness = 1;
            double newMaxUnnormalizedBrightness = 0;
            for (HashMap.Entry<Character, Double> entry : this.charSet.entrySet()) {
                double unnormalizedBrightness = entry.getValue() *
                        (maxUnnormalizedBrightness - minUnnormalizedBrightness)
                        + minUnnormalizedBrightness;
                newMinUnnormalizedBrightness = Math.min(newMinUnnormalizedBrightness, unnormalizedBrightness);
                newMaxUnnormalizedBrightness = Math.max(newMaxUnnormalizedBrightness, unnormalizedBrightness);
            }
            minUnnormalizedBrightness = newMinUnnormalizedBrightness;
            maxUnnormalizedBrightness = newMaxUnnormalizedBrightness;

            for (HashMap.Entry<Character, Double> entry : this.charSet.entrySet()) {
                double brightness = entry.getValue();
                double newBrightness = (brightness - minUnnormalizedBrightness) /
                        (maxUnnormalizedBrightness - minUnnormalizedBrightness);
                entry.setValue(newBrightness);
            }
        }
    }

    /**
     *get charSet keys
     * @return charSet keys
     */
    public Set<Character> getCharSet(){
        return charSet.keySet();
    }
}
