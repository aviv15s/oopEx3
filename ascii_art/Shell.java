package ascii_art;

public class Shell {

    /**
     * main function of the program
     */
    public void run(){
        String defaultImagePath = "cat.jpeg";
        int defaultResolution = 128;
        char[] defaultCharacters = {'0','1','2','3','4','5','6','7','8','9'};
        boolean defaultScreenAsOutput = true;

        boolean exitFlag = false;
        while (!exitFlag){
            String userInput = requestInput();
            switch (userInput){
                case "exit":
                    break;
                case "chars":
                    break;
                case "add":
                    break;
                case "remove":
                    break;
                case "res":
                    break;
                case "image":
                    break;
                case "output":
                    break;
                case "asciiArt":
                    break;
            }
        }
    }

    private String requestInput(){
        System.out.println(">>>");
        String userInput = KeyboardInput.readLine();
        return userInput;
    }
    private void handle
}
