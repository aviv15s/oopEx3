package image_char_matching;

import java.util.HashMap;


/**
 * שתהיה אחראית להתאים תו ASCII לתת תמונה עם בהירות נתונה. מחלקה זו תשמש את אלגוריתם
 * אומנות ה-ASCII שלנו בהמשך כדי להחליף תתי תמונות בתווים.
 */
public class SubImgCharMatcher {
    private final boolean WHITE_PIXEL = true;
    private final HashMap<Character, Float> charSet;
    private float maxUnnormalizedBrightness;
    private float minUnnormalizedBrightness;

    /**
     * Constructor
     * @param charset charset used by the algorithm
     */
    public SubImgCharMatcher(char[] charset){
        charSet = new HashMap<Character, Float>();

        minUnnormalizedBrightness = 0;
        maxUnnormalizedBrightness = 1;
        for (int i = 0; i < charset.length; i++) {
            char c = charset[i];
            float charBrightness = calculateCharBrightness(c);
            this.charSet.put(c, charBrightness);

            minUnnormalizedBrightness = Math.min(minUnnormalizedBrightness, charBrightness);
            maxUnnormalizedBrightness = Math.max(maxUnnormalizedBrightness, charBrightness);
        }

        for (HashMap.Entry<Character, Float> entry : this.charSet.entrySet()) {
            float brightness = entry.getValue();
            float newBrightness = (brightness - minUnnormalizedBrightness) / (maxUnnormalizedBrightness - minUnnormalizedBrightness);
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
        float maxDistance = 1;
        char matchingChar = ' ';
        for (HashMap.Entry<Character, Float> entry : this.charSet.entrySet()){
            float distance = Math.abs((float)brightness - entry.getValue());
            if (distance <= maxDistance){
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
        float charBrightness = calculateCharBrightness(c);

        // check if needs to re-normalize all characters
        if (charBrightness > maxUnnormalizedBrightness || charBrightness < minUnnormalizedBrightness){
            float newMinUnnormalizedBrightness = Math.min(minUnnormalizedBrightness, charBrightness);
            float newMaxUnnormalizedBrightness = Math.max(maxUnnormalizedBrightness, charBrightness);

            for (HashMap.Entry<Character, Float> entry : this.charSet.entrySet()) {
                float brightness = entry.getValue();
                float unnormalizedCharBrightness = brightness * (maxUnnormalizedBrightness - minUnnormalizedBrightness) + minUnnormalizedBrightness;
                float newBrightness = (unnormalizedCharBrightness - newMinUnnormalizedBrightness) / (newMaxUnnormalizedBrightness - newMinUnnormalizedBrightness);
                entry.setValue(newBrightness);
            }

            maxUnnormalizedBrightness = Math.max(maxUnnormalizedBrightness, charBrightness);
            minUnnormalizedBrightness = Math.min(minUnnormalizedBrightness, charBrightness);
        }

        float normalizedCharBrightness = (charBrightness - minUnnormalizedBrightness) / (maxUnnormalizedBrightness - minUnnormalizedBrightness);
        this.charSet.put(c, normalizedCharBrightness);

    }

    /**
     * removes a char from the charset
     * @param c char
     */
    public void removeChar(char c){
        float charBrightness = charSet.get(c);
        charSet.remove(c);

        // check if needs to re-normalize all characters
        if (charBrightness == maxUnnormalizedBrightness || charBrightness == minUnnormalizedBrightness){
            float newMinUnnormalizedBrightness = 0;
            float newMaxUnnormalizedBrightness = 1;
            for (HashMap.Entry<Character, Float> entry : this.charSet.entrySet()) {
                float unnormalizedBrightness = entry.getValue() * (maxUnnormalizedBrightness - minUnnormalizedBrightness) + minUnnormalizedBrightness;
                newMinUnnormalizedBrightness = Math.min(newMinUnnormalizedBrightness, unnormalizedBrightness);
                newMaxUnnormalizedBrightness = Math.max(newMaxUnnormalizedBrightness, unnormalizedBrightness);
            }
            minUnnormalizedBrightness = newMinUnnormalizedBrightness;
            maxUnnormalizedBrightness = newMaxUnnormalizedBrightness;

            for (HashMap.Entry<Character, Float> entry : this.charSet.entrySet()) {
                float brightness = entry.getValue();
                float newBrightness = (brightness - minUnnormalizedBrightness) / (maxUnnormalizedBrightness - minUnnormalizedBrightness);
                entry.setValue(newBrightness);
            }
        }
    }
}
