package ascii_output;

/**
 * Output a 2D array of chars to the console.
 * @author Dan Nirel
 */public class ConsoleAsciiOutput implements AsciiOutput{
     private static final String CHAR_BETWEEN_LETTERS = " ";
    /**
     * print the image to the console.
     * Pass through all the array and prints the letters in the array in the right order.
     * @param chars array to print
     */
    @Override
    public void out(char[][] chars) {
        for (int y = 0; y < chars.length ; y++) {
            for (int x = 0; x < chars[y].length; x++) {
                System.out.print(chars[y][x] + CHAR_BETWEEN_LETTERS);
            }
            System.out.println();
        }
    }
}
