package src.ascii_art;

import src.ascii_output.AsciiOutput;
import src.ascii_output.ConsoleAsciiOutput;
import src.ascii_output.HtmlAsciiOutput;
import src.image.Image;
import src.image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.Set;

public class Shell {
    // ERROR MESSAGE STRINGS
    private static final String DID_NOT_EXECUTE_DUE_TO_INCORRECT_COMMAND = "Did not execute due to incorrect command.";
    private static final String DID_NOT_CHANGE_RESOLUTION_DUE_TO_INCORRECT_FORMAT = "Did not change resolution due to incorrect format.";
    private static final String DID_NOT_CHANGE_OUTPUT_METHOD_DUE_TO_INCORRECT_FORMAT = "Did not change output method due to incorrect format.";
    private static final String DID_NOT_REMOVE_DUE_TO_INCORRECT_FORMAT = "Did not remove due to incorrect format.";
    private static final String DID_NOT_ADD_DUE_TO_INCORRECT_FORMAT = "Did not add due to incorrect format.";
    private static final String DID_NOT_EXECUTE_DUE_TO_PROBLEM_WITH_IMAGE_FILE = "Did not execute due to problem with image file.";
    private static final String DID_NOT_EXECUTE_CHARSET_IS_EMPTY = "Did not execute. Charset is empty.";
    private static final String DID_NOT_CHANGE_RESOLUTION_DUE_TO_EXCEEDING_BOUNDARIES = "Did not change resolution due to exceeding boundaries.";//Todo: change into the exception


    // COMMAND KEYWORDS
    private static final String ADD_COMMAND = "add";
    private static final String OUTPUT_COMMAND = "output";
    private static final String EXIT_COMMAND = "exit";
    private static final String CHARS_COMMAND = "chars";
    private static final String REMOVE_COMMAND = "remove";
    private static final String RES_COMMAND = "res";
    private static final String IMAGE_COMMAND = "image";
    private static final String ASCII_ART_COMMAND = "asciiArt";
    private static final String RES_UP = "up";
    private static final String RES_DOWN = "down";
    private  static final String CONSOLE_USER_INTERACTION_STRING = ">>> ";
    private static final String CONSOLE = "console";
    private static final String HTML = "html";
    private static final String ALL = "all";
    private static final String SPACE1 = "space";

    // Default Values
    private static final int DEFAULT_RESOLUTION = 128;
    private static final char[] DEFAULT_CHARACTER_SET = {'0','1','2','3','4','5','6','7','8','9'};
    private static final String DEFAULT_IMAGE_PATH = "cat.jpeg";
    private static final String DEFAULT_OUTPUT_FILE_PATH = "out.html";
    private static final String DEFAULT_FONT = "Courier New";


    // Magic Numbers
    private static final int ASCII_SIZE = 128;
    private static final int MIN_CHAR_IN_SET = 32;
    private static final int MAX_CHAR_IN_SET = 126;
    private static final int VALID_COMMAND_LENGTH = 2;
    private static final int COMMAND_ARGUMENT_INDEX = 1;
    private static final char RANGE_CHAR = '-';
    private static final int MINIMUM_CHAR_IN_SET = 32;
    private static final int MAXIMUM_CHAR_IN_SET = 126;
    private static final char SPACE = ' ';

    private static final String RESOLUTION_SET_TO = "Resolution set to ";

    // Values
    private AsciiOutput outputMethod;
    private Image image;
    private AsciiArtAlgorithm algorithm;
    private final SubImgCharMatcher subImgCharMatcher;

    /**
     * default constructor
     */
    public Shell() {
        try {
            image = new Image(DEFAULT_IMAGE_PATH);
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        subImgCharMatcher = new SubImgCharMatcher(DEFAULT_CHARACTER_SET);
        algorithm = new AsciiArtAlgorithm(DEFAULT_RESOLUTION, image, subImgCharMatcher);
        outputMethod = new ConsoleAsciiOutput();
    }
    /**
     * main function of the program
     */
    public void run(){

        boolean exitFlag = false;
        while (!exitFlag){
            String userInput = requestInput();
            String[] wordsInUserInput = userInput.split(String.valueOf(SPACE));
            switch (wordsInUserInput[0]){
                case EXIT_COMMAND:
                    if (wordsInUserInput.length == 1){
                        exitFlag = true;
                    } else{
                        System.out.println(DID_NOT_EXECUTE_DUE_TO_INCORRECT_COMMAND);
                    }
                    break;
                case CHARS_COMMAND:
                    handleCharsCommand(wordsInUserInput);
                    break;
                case ADD_COMMAND:
                    try {
                        handleAddCommand(wordsInUserInput);
                    }
                    catch (InvalidUserInputException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case REMOVE_COMMAND:
                    try {
                        handleRemoveCommand(wordsInUserInput);
                    }
                    catch (InvalidUserInputException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case RES_COMMAND:
                    try{
                        handleResCommand(wordsInUserInput);
                    }
                    catch (InvalidUserInputException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case IMAGE_COMMAND:
                    try{
                        handleImageCommand(wordsInUserInput);
                    }
                    catch (InvalidUserInputException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case OUTPUT_COMMAND:
                    try {
                        handleOutputCommand(wordsInUserInput);
                    } catch (InvalidUserInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case ASCII_ART_COMMAND:
                    try {
                        handleRunCommand(wordsInUserInput);
                    } catch (InvalidUserInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println(DID_NOT_EXECUTE_DUE_TO_INCORRECT_COMMAND);
                    break;
            }
        }
    }

    private String requestInput(){
        System.out.print(CONSOLE_USER_INTERACTION_STRING);
        return KeyboardInput.readLine();
    }

    /**
     * changes the output of the algorithm
     * @param userInput the command the user inputted
     * @throws InvalidUserInputException in case of wrong use of command
     */
    private void handleOutputCommand(String[] userInput) throws InvalidUserInputException{
        if (userInput.length != 2){
            throw new InvalidUserInputException(DID_NOT_CHANGE_OUTPUT_METHOD_DUE_TO_INCORRECT_FORMAT);
        }

        if (userInput[1].equals(HTML)){
            outputMethod = new ConsoleAsciiOutput();
        } else if (userInput[1].equals(CONSOLE)) {
            outputMethod = new HtmlAsciiOutput(DEFAULT_OUTPUT_FILE_PATH, DEFAULT_FONT);
        } else {
            throw new InvalidUserInputException(DID_NOT_CHANGE_OUTPUT_METHOD_DUE_TO_INCORRECT_FORMAT);
        }
    }

    /**
     * loads a new src.image file to the algorithm
     * @param userInput
     * @throws InvalidUserInputException if the src.image file is invalid and doesent load correctly
     */
    private void handleImageCommand(String[] userInput) throws InvalidUserInputException{
        if (userInput.length != VALID_COMMAND_LENGTH){
            throw new InvalidUserInputException(DID_NOT_EXECUTE_DUE_TO_PROBLEM_WITH_IMAGE_FILE);
        }
        try{
            image = new Image(userInput[COMMAND_ARGUMENT_INDEX]);
            algorithm = new AsciiArtAlgorithm(algorithm.getResolution(), image, subImgCharMatcher);
        }
        catch (IOException e){
            throw new InvalidUserInputException(DID_NOT_EXECUTE_DUE_TO_PROBLEM_WITH_IMAGE_FILE);
        }
    }

    /**
     * prints all the characters in ascii order
     * @param userInput
     */
    private void handleCharsCommand(String[] userInput){
        Set<Character> characters = subImgCharMatcher.getCharSet();
        boolean[] isCharInSet = new boolean[ASCII_SIZE];
        for (Character c: characters){
            isCharInSet[(int)c] = true;
        }
        for (int i = 0; i < ASCII_SIZE; i++) {
            if (isCharInSet[i]){
                System.out.print((char) i);
                System.out.print(SPACE);
            }
        }
        System.out.println();
    }

    /**
     * exactly the same as add functionality
     * do nothing if one of the characters is not in the algorithm's storage
     * print "Did not remove due to incorrect format." in case of an error.
     * @param userInput
     * @throws InvalidUserInputException in case of incorrect use
     */
    private void handleRemoveCommand(String[] userInput) throws InvalidUserInputException {
        if (userInput.length != VALID_COMMAND_LENGTH){
            throw new InvalidUserInputException(DID_NOT_REMOVE_DUE_TO_INCORRECT_FORMAT);
        }

        String action = userInput[COMMAND_ARGUMENT_INDEX];

        switch (action){
            case ALL:
                for (int i = MIN_CHAR_IN_SET; i <= MAX_CHAR_IN_SET; i++) {
                    subImgCharMatcher.removeChar((char)i);
                }
                break;
            case SPACE1:
                subImgCharMatcher.removeChar(SPACE);
                break;
            default:
                int firstRangeArgument = 0;
                int rangeSymbolArgument = 1;
                int secondRangeArgument = 2;
                int rangeLength = 3;
                if (action.length() == COMMAND_ARGUMENT_INDEX){
                    char c = action.charAt(0);
                    subImgCharMatcher.removeChar(c);
                } else if (action.length() == rangeLength && action.charAt(rangeSymbolArgument) == RANGE_CHAR) {
                    char c1 = (char) Math.min(action.charAt(firstRangeArgument), action.charAt(secondRangeArgument));
                    char c2 = (char) Math.max(action.charAt(firstRangeArgument), action.charAt(secondRangeArgument));
                    for (int i = c1; i <= (int) c2 ; i++) {
                        subImgCharMatcher.removeChar((char) i);
                    }
                } else {
                    throw new InvalidUserInputException(DID_NOT_REMOVE_DUE_TO_INCORRECT_FORMAT);
                }
                break;
        }
    }


    /**
     *  can add a single char ('add c') or add all ascii characters 32-126 ('add all')
     *  'add space' adds the space character
     *  'add m-p' adds all characters in the range from m to p
     *  'add p-m' should work the same
     *  anything else should print "Did not add due to incorrect format."
     *  nothing needs to be done if user requests to add existing character
     * @param userInput String array of words in the user input
     * @throws InvalidUserInputException in case of incorrect use
     */
    private void handleAddCommand(String[] userInput) throws InvalidUserInputException {
        if (userInput.length != VALID_COMMAND_LENGTH){
            throw new InvalidUserInputException(DID_NOT_ADD_DUE_TO_INCORRECT_FORMAT);
        }

        String action = userInput[COMMAND_ARGUMENT_INDEX];

        switch (action){
            case ALL:
                for (int i = MINIMUM_CHAR_IN_SET; i <= MAXIMUM_CHAR_IN_SET; i++) {
                    subImgCharMatcher.addChar((char) i);
                }
                break;
            case SPACE1:
                subImgCharMatcher.addChar(SPACE);
                break;
            default:
                int firstRangeArgument = 0;
                int rangeSymbolArgument = 1;
                int secondRangeArgument = 2;
                int rangeLength = 3;
                if (action.length() == 1){
                    char c = action.charAt(firstRangeArgument);
                    subImgCharMatcher.addChar(c);
                } else if (action.length() == rangeLength && action.charAt(rangeSymbolArgument) == RANGE_CHAR) {
                    char c1 = (char) Math.min(action.charAt(firstRangeArgument), action.charAt(secondRangeArgument));
                    char c2 = (char) Math.max(action.charAt(firstRangeArgument), action.charAt(secondRangeArgument));
                    for (int i = c1; i <= (int) c2 ; i++) {
                        subImgCharMatcher.addChar((char)i);
                    }
                } else {
                    throw new InvalidUserInputException(DID_NOT_ADD_DUE_TO_INCORRECT_FORMAT);
                }
                break;
        }
    }

    /**
     *  'res up' multiplies resolution by 2
     *  'res down' divides resolution by 2
     *  after an update to resolution prints "Resolution set to <current resolution>."
     *  minCharsInRow = max(1, imgWidth/imgHeight)
     *  if after changing the resolution is lower than the minimum above, print:
     *  "Did not change resolution due to exceeding boundaries."
     *  in case of incorrect input print: "Did not change resolution due to incorrect format."
     * @param userInput
     * @throws InvalidUserInputException in case of incorrect use
     */
    private void handleResCommand(String[] userInput) throws InvalidUserInputException{
        if (userInput.length != 2)
        {
            throw new InvalidUserInputException("Did not change resolution due to incorrect format.");
        }

        if (userInput[COMMAND_ARGUMENT_INDEX].equals(RES_UP)){
            if (!algorithm.setResolution(algorithm.getResolution()* 2)) {
                System.out.println(DID_NOT_CHANGE_RESOLUTION_DUE_TO_EXCEEDING_BOUNDARIES);
            } else{
                System.out.println(RESOLUTION_SET_TO +algorithm.getResolution()+".");
            }
        } else if (userInput[1].equals(RES_DOWN)){
            if (!algorithm.setResolution(algorithm.getResolution()/2)) {
                System.out.println(DID_NOT_CHANGE_RESOLUTION_DUE_TO_EXCEEDING_BOUNDARIES);
            } else{
                System.out.println(RESOLUTION_SET_TO+algorithm.getResolution()+".");
            }
        } else{
            throw new InvalidUserInputException(DID_NOT_CHANGE_RESOLUTION_DUE_TO_INCORRECT_FORMAT);
        }
    }

    private void handleRunCommand(String[] userInput) throws InvalidUserInputException{
        if (subImgCharMatcher.getCharSet().isEmpty()){
            throw new InvalidUserInputException(DID_NOT_EXECUTE_CHARSET_IS_EMPTY);
        }

        char[][] returnedImage = algorithm.run();
        outputMethod.out(returnedImage);
    }

    private class InvalidUserInputException extends Exception{
        public InvalidUserInputException(String errorMessage){
            super(errorMessage);
        }
    }

    public static void main(String[] args){
        Shell shell = new Shell();
        shell.run();
    }

}
