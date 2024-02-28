package ascii_art;

public class Shell {

    private AsciiArtAlgorithm algorithm;
    /**
     * main function of the program
     */
    public void run(){
        String defaultImagePath = "cat.jpeg";
        int defaultResolution = 128;
        char[] defaultCharacters = {'0','1','2','3','4','5','6','7','8','9'};
        boolean defaultScreenAsOutput = true;
        algorithm = new AsciiArtAlgorithm(0); // Todo update me

        boolean exitFlag = false;
        while (!exitFlag){
            String userInput = requestInput();
            String[] wordsInUserInput = userInput.split(" ");
            switch (wordsInUserInput[0]){
                case "exit":
                    exitFlag = true;
                    break;
                case "chars":
                    handleCharsCommand(wordsInUserInput);
                    break;
                case "add":
                    try {
                        handleAddCommand(wordsInUserInput);
                    }
                    catch (InvalidUserInputException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "remove":
                    try {
                        handleRemoveCommand(wordsInUserInput);
                    }
                    catch (InvalidUserInputException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "res":
                    try{
                        handleResCommand(wordsInUserInput);
                    }
                    catch (InvalidUserInputException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "image":
                    // receives as input "image board.png"
                    // if the image couldn't be loaded prints: "Did not execute due to problem with image file."
                    // Image constructor throws IOException
                    break;
                case "output":
                    // "output html" prints to html file out.html with font Courier New.
                    // "output console" print to terminal
                    // any other input is incorrect and should print "Did not change output method due to incorrect format."
                    break;
                case "asciiArt":
                    // in case of using empty charSet prints: "Did not execute. Charset is empty."
                    break;
                default:
                    // Should be reached if input is invalid command
                    // prints "Did not execute due to incorrect command."
                    break;
            }
        }
    }

    private String requestInput(){
        System.out.println(">>>");
        String userInput = KeyboardInput.readLine();
        return userInput;
    }

    /**
     * prints all the characters in ascii order
     * @param userInput
     */
    private void handleCharsCommand(String[] userInput){
        char[] characters = algorithm.getCharSet();
        boolean[] isCharInSet = new boolean[128];
        for (int i = 0; i < characters.length; i++) {
            isCharInSet[(int)characters[i]] = true;
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
            if (!algorithm.updateRes()){
                System.out.println("Did not change resolution due to exceeding boundaries.");
            }
        } else if (userInput[1].equals("down")){
            if (!algorithm.updateRes()){
                System.out.println("Did not change resolution due to exceeding boundaries.");
            }
        } else{
            throw new InvalidUserInputException("Did not change resolution due to incorrect format.");
        }
    }

    private class InvalidUserInputException extends Exception{
        public InvalidUserInputException(String errorMessage){
            super(errorMessage);
        }
    }

}
