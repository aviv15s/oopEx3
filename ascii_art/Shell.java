package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.io.IOException;
import java.util.Set;

public class Shell {

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

    private int resolution = 128;
    private boolean printingToConsole = true;
    private final char[] defaultCharacters = {'0','1','2','3','4','5','6','7','8','9'};
    private Image image;


    private AsciiArtAlgorithm algorithm;

    /**
     * default constructor
     */
    public Shell() {
        try {
            image = new Image("C:\\Users\\t9092556\\IdeaProjects\\oopEx3\\ascii_art\\cat.jpeg");
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        algorithm = new AsciiArtAlgorithm(resolution, image, defaultCharacters); // Todo update me
    }

    public static void main(String[] args){
        Shell shell = new Shell();
        shell.run();
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
     * loads a new image file to the algorithm
     * @param userInput
     * @throws InvalidUserInputException if the image file is invalid and doesent load correctly
     */
    private void handleImageCommand(String[] userInput) throws InvalidUserInputException{
        if (userInput.length != 2){
            throw new InvalidUserInputException("Did not execute due to problem with image file.");
        }
        try{
            image = new Image(userInput[1]);
        }
        catch (IOException e){
            throw new InvalidUserInputException("Did not execute due to problem with image file.");
        }
    }

    /**
     * prints all the characters in ascii order
     * @param userInput
     */
    private void handleCharsCommand(String[] userInput){
        Set<Character> characters = algorithm.getCharSet();
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
                for (int i = 32; i < 126; i++) {
                    algorithm.removeChar((char)i);
                }
                break;
            case "space":
                algorithm.removeChar(' ');
                break;
            default:
                if (action.length() == 1){
                    char c = action.charAt(0);
                    algorithm.removeChar(c);
                } else if (action.length() == 3 && action.charAt(1) == '-') {
                    char c1 = (char) Math.min((int)action.charAt(0), (int)action.charAt(2));
                    char c2 = (char) Math.max((int)action.charAt(0), (int)action.charAt(2));
                    for (int i = (int)c1; i <= (int) c2 ; i++) {
                        algorithm.removeChar((char)i);
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
                for (int i = 32; i < 126; i++) {
                    algorithm.addChar((char)i);
                }
                break;
            case "space":
                algorithm.addChar(' ');
                break;
            default:
                if (action.length() == 1){
                    char c = action.charAt(0);
                    algorithm.addChar(c);
                } else if (action.length() == 3 && action.charAt(1) == '-') {
                    char c1 = (char) Math.min((int)action.charAt(0), (int)action.charAt(2));
                    char c2 = (char) Math.max((int)action.charAt(0), (int)action.charAt(2));
                    for (int i = (int)c1; i <= (int) c2 ; i++) {
                        algorithm.addChar((char)i);
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

        if (userInput[1].equals("up")){
            if (2*resolution <= image.getWidth()){
                algorithm.setResolution(2*resolution);
                resolution *= 2;
            }
            else{
                System.out.println("Did not change resolution due to exceeding boundaries.");
            }
        } else if (userInput[1].equals("down")){
            if (resolution/2 >= Math.max(1f,(float)image.getWidth()/image.getHeight())){
                algorithm.setResolution(resolution/2);
                resolution /= 2;
            }
            else{
                System.out.println("Did not change resolution due to exceeding boundaries.");
            }
        } else{
            throw new InvalidUserInputException("Did not change resolution due to incorrect format.");
        }
    }

    private void handleRunCommand(String[] userInput) throws InvalidUserInputException{
        if (algorithm.getCharSet().size() == 0){
            throw new InvalidUserInputException("Did not execute. Charset is empty.");
        }

        char[][] returnedImage = algorithm.run();

        if (printingToConsole){
            // print to console
            ConsoleAsciiOutput output = new ConsoleAsciiOutput();
            output.out(returnedImage);
        } else {
            // print to html
            HtmlAsciiOutput output = new HtmlAsciiOutput("C:\\Users\\t9092556\\IdeaProjects\\oopEx3\\ascii_art\\out.html", "Courier New");
            output.out(returnedImage);
        }
    }

    private class InvalidUserInputException extends Exception{
        public InvalidUserInputException(String errorMessage){
            super(errorMessage);
        }
    }

}
