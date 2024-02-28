package image_char_matching;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * שתהיה אחראית להתאים תו ASCII לתת תמונה עם בהירות נתונה. מחלקה זו תשמש את אלגוריתם
 * אומנות ה-ASCII שלנו בהמשך כדי להחליף תתי תמונות בתווים.
 */
public class SubImgCharMatcher {
    private final boolean WHITE_PIXEL = true;
    private HashMap<Character, Float> charSet;
    /**
     * Constructor
     * @param charset charset used by the algorithm
     */
    public SubImgCharMatcher(char[] charset){
        this.charSet = new HashMap<Character, Float>();
        for (int i = 0; i < charset.length; i++) {
            char c = charset[i];
            float charBrightness = calculateCharBrightness(c);
            this.charSet.put(c, charBrightness);
        }
    }

    private float calculateCharBrightness(char c){
        boolean[][] pixelArray = CharConverter.convertToBoolArray(c);
        int numberOfWhitePixels = 0;
        for (int i = 0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[i].length; j++) {
                if (pixelArray[i][j] == WHITE_PIXEL) {
                    numberOfWhitePixels++;
                }
            }
        }

    }

    /**
     * used to get the char with the closest brightness to the given one
     * @param brightness brightness to match
     * @return a char
     */
    public char getCharByImageBrightness(double brightness){

    }

    /**
     * adds a char to the charset
     * @param c char
     */
    public void addChar(char c){
        this.charSet.add(c);
    }

    /**
     * removes a char from the charset
     * @param c char
     */
    public void removeChar(char c){
        this.charSet.remove(c);
    }




}
