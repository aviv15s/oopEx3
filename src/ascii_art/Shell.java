package src.ascii_art;

import src.ascii_output.ConsoleAsciiOutput;
import src.ascii_output.HtmlAsciiOutput;
import src.image.Image;
import src.image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.Set;

public class Shell {

    public static final String DID_NOT_CHANGE_RESOLUTION_DUE_TO_EXCEEDING_BOUNDARIES = "Did not change resolution due to exceeding boundaries.";//Todo: change into the exception
    private static final String ADD_COMMAND = "add";
    private static final String OUTPUT_COMMAND = "output";
    private static final String EXIT = "exit";
    private static final String CHARS_COMMAND = "chars";
    private static final String REMOVE_COMMAND = "remove";
    private static final String RES_COMMAND = "res";
    private static final String IMAGE_COMMAND = "image";
    private static final String ASCII_ART_COMMAND = "asciiArt";
    private static final String DID_NOT_EXECUTE_DUE_TO_INCORRECT_COMMAND = "Did not execute due to incorrect command.";
    private static final String SPACE = " ";
    public static final String UP = "up";
    public static final String DOWN = "down";
    public static final String DID_NOT_CHANGE_RESOLUTION_DUE_TO_INCORRECT_FORMAT = "Did not change resolution due to incorrect format.";

    private int resolution = 128;
    private boolean printingToConsole = true;
    private final char[] defaultCharacters = {'0','1','2','3','4','5','6','7','8','9'};
    private Image image;


    private AsciiArtAlgorithm algorithm;
    private SubImgCharMatcher subImgCharMatcher;

    /**
     * default constructor
     */
    public Shell() {
        try {
            image = new Image("cat.jpeg");
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        subImgCharMatcher = new SubImgCharMatcher(defaultCharacters);
        algorithm = new AsciiArtAlgorithm(resolution, image, subImgCharMatcher); // Todo update me
    }
    /**
     * main function of the program
     */
    public void run(){

        boolean exitFlag = false;
        while (!exitFlag){
            String userInput = requestInput();
            String[] wordsInUserInput = userInput.split(SPACE);
            switch (wordsInUserInput[0]){
                case EXIT:
                    exitFlag = true;
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
        System.out.print(">>> ");
        String userInput = KeyboardInput.readLine();
        return userInput;
    }

    /**
     * changes the output of the algorithm
     * @param userInput the command the user inputted
     * @throws InvalidUserInputException in case of wrong use of command
     */
    private void handleOutputCommand(String[] userInput) throws InvalidUserInputException{
        if (userInput.length != 2){
            throw new InvalidUserInputException("Did not change output method due to incorrect format.");
        }

        if (userInput[1].equals("html")){
            printingToConsole = false;
        } else if (userInput[1].equals("console")) {
            printingToConsole = true;
        } else {
            throw new InvalidUserInputException("Did not change output method due to incorrect format.");
        }
    }

    /**
     * loads a new src.image file to the algorithm
     * @param userInput
     * @throws InvalidUserInputException if the src.image file is invalid and doesent load correctly
     */
    private void handleImageCommand(String[] userInput) throws InvalidUserInputException{
        if (userInput.length != 2){
            throw new InvalidUserInputException("Did not execute due to problem with src.image file.");
        }
        try{
            image = new Image(userInput[1]);
            algorithm = new AsciiArtAlgorithm(algorithm.getResolution(), image, subImgCharMatcher);
        }
        catch (IOException e){
            throw new InvalidUserInputException("Did not execute due to problem with src.image file.");
        }
    }

    /**
     * prints all the characters in ascii order
     * @param userInput
     */
    private void handleCharsCommand(String[] userInput){
        Set<Character> characters = subImgCharMatcher.getCharSet();
        boolean[] isCharInSet = new boolean[128];
        for (Character c: characters){
            isCharInSet[(int)c] = true;
        }
        for (int i = 0; i < 128; i++) {
            if (isCharInSet[i]){
                System.out.print((char) i + " ");
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
        if (userInput.length != 2){
            throw new InvalidUserInputException("Did not remove due to incorrect format.");
        }

        String action = userInput[1];

        switch (action){
            case "all":
                for (int i = 32; i <= 126; i++) {
                    subImgCharMatcher.removeChar((char)i);
                }
                break;
            case "space":
                subImgCharMatcher.removeChar(' ');
                break;
            default:
                if (action.length() == 1){
                    char c = action.charAt(0);
                    subImgCharMatcher.removeChar(c);
                } else if (action.length() == 3 && action.charAt(1) == '-') {
                    char c1 = (char) Math.min((int)action.charAt(0), (int)action.charAt(2));
                    char c2 = (char) Math.max((int)action.charAt(0), (int)action.charAt(2));
                    for (int i = (int)c1; i <= (int) c2 ; i++) {
                        subImgCharMatcher.removeChar((char)i);
                    }
                } else {
                    throw new InvalidUserInputException("Did not remove due to incorrect format.");
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
        if (userInput.length != 2){
            throw new InvalidUserInputException("Did not add due to incorrect format.");
        }

        String action = userInput[1];

        switch (action){
            case "all":
                for (int i = 32; i <= 126; i++) {
                    subImgCharMatcher.addChar((char)i);
                }
                break;
            case "space":
                subImgCharMatcher.addChar(' ');
                break;
            default:
                if (action.length() == 1){
                    char c = action.charAt(0);
                    subImgCharMatcher.addChar(c);
                } else if (action.length() == 3 && action.charAt(1) == '-') {
                    char c1 = (char) Math.min((int)action.charAt(0), (int)action.charAt(2));
                    char c2 = (char) Math.max((int)action.charAt(0), (int)action.charAt(2));
                    for (int i = (int)c1; i <= (int) c2 ; i++) {
                        subImgCharMatcher.addChar((char)i);
                    }
                } else {
                    throw new InvalidUserInputException("Did not add due to incorrect format.");
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

        if (userInput[1].equals(UP)){
            if (!algorithm.setResolution(algorithm.getResolution()*2)) {
                System.out.println(DID_NOT_CHANGE_RESOLUTION_DUE_TO_EXCEEDING_BOUNDARIES);
            } else{
                System.out.println("Resolution set to "+algorithm.getResolution()+".");
            }
        } else if (userInput[1].equals(DOWN)){
            if (!algorithm.setResolution(algorithm.getResolution()/2)) {
                System.out.println(DID_NOT_CHANGE_RESOLUTION_DUE_TO_EXCEEDING_BOUNDARIES);
            } else{
                System.out.println("Resolution set to "+algorithm.getResolution()+".");
            }
        } else{
            throw new InvalidUserInputException(DID_NOT_CHANGE_RESOLUTION_DUE_TO_INCORRECT_FORMAT);
        }
    }

    private void handleRunCommand(String[] userInput) throws InvalidUserInputException{
        if (subImgCharMatcher.getCharSet().size() == 0){
            throw new InvalidUserInputException("Did not execute. Charset is empty.");
        }

        char[][] returnedImage = algorithm.run();

        if (printingToConsole){
            // print to console
            ConsoleAsciiOutput output = new ConsoleAsciiOutput();
            output.out(returnedImage);
        } else {
            // print to html
            HtmlAsciiOutput output = new HtmlAsciiOutput("out.html", "Courier New");
            output.out(returnedImage);
        }
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
