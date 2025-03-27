package workshop05code;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 *
 * @author sqlitetutorial.net
 */
public class App {
    // Start code for logging exercise
    private static final Logger logger = Logger.getLogger(App.class.getName());

    static {
        // must set before the Logger
        // loads logging.properties from the classpath
        try {// resources\logging.properties
            LogManager.getLogManager().readConfiguration(new FileInputStream("resources/logging.properties"));
        } catch (SecurityException | IOException e1) {
            // e1.printStackTrace();
            logger.log(Level.WARNING, "Error reading logging.properties", e1);
        }
    }

    // End code for logging exercise
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SQLiteConnectionManager wordleDatabaseConnection = new SQLiteConnectionManager("words.db");

        wordleDatabaseConnection.createNewDatabase("words.db");
        logger.info("Database created."); //Logged when the database is created.

        if (wordleDatabaseConnection.checkIfConnectionDefined()) {
            logger.log(Level.INFO, "Wordle created and connected.");
            // System.out.println("Wordle created and connected.");
        } else {
            logger.log(Level.INFO, "Not able to connect. Sorry!");
            return;
        }
        if (wordleDatabaseConnection.createWordleTables()) {
            // System.out.println("Wordle structures in place.");
            logger.log(Level.INFO, "Wordle structures in place.");
        } else {
            // System.out.println("Not able to launch. Sorry!");
            logger.log(Level.INFO, "Not able to launch. Sorry!");
            return;
        }

        // let's add some words to valid 4 letter words from the data.txt file

        try (BufferedReader br = new BufferedReader(new FileReader("resources/data.txt"))) {
            String line;
            int i = 1;
            while ((line = br.readLine()) != null) {
                // System.out.println(line);
                wordleDatabaseConnection.addValidWord(i, line);
                i++;
            }

        } catch (IOException e) {
            // logger.log(Level.WARNING, "Not able to load . Sorry!", e);
            logger.severe(e.getMessage());
            // System.out.println("Not able to load . Sorry!");
            // System.out.println(e.getMessage());
            return;
        }

        // let's get them to enter a word

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter a 4 letter word for a guess or q to quit: ");
            String guess = scanner.nextLine();

            while (!guess.equals("q")) {

                System.out.println("You've guessed '" + guess+"'.");

                

                if (wordleDatabaseConnection.isValidWord(guess)) { 
                    System.out.println("Success! It is in the the list.\n");
                    // Log successful/valid guesses read from data.
                    // logger.info("Success! It is in the the list.\n");
                }else{
                    System.out.println("Sorry. This word is NOT in the the list.\n");
                    // Log unsuccessful/invalid guesses read from data.
                    logger.info("Sorry, this word is not on the list.");
                }

                System.out.print("Enter a 4 letter word for a guess or q to quit: " );
                guess = scanner.nextLine();
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            // e.printStackTrace();
            logger.log(Level.WARNING, "Input cannot be read. Sorry!", e);
        }

    }
}