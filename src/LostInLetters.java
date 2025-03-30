import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.HashMap;
import java.util.Collections;

public class LostInLetters {
    private ArrayList<String> dictionary;
    private ArrayList<String> selectedWords;
    private char[][] grid;
    private int attemptsLeft;

    public LostInLetters() {
        this.attemptsLeft = 3;
        this.dictionary = new ArrayList<>();
        this.selectedWords = new ArrayList<>();
        this.grid = new char[4][4];

        loadDictionary("dictionary.txt");

        // Initialize the grid with spaces
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                grid[i][j] = ' ';
            }
        }
    }

    public void loadDictionary(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File not found.");
            return;
        }

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (!line.isEmpty() && line.length() == 4) {
                    dictionary.add(line.toLowerCase()); // Convert and store
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file.");
        }
    }

    public void selectWords() {
        if (dictionary.isEmpty() || dictionary.size() < 4) {
            System.out.println("Error: Not enough words in dictionary.");
            return; // Prevent proceeding
        }

        Random random = new Random();
        selectedWords.clear();

        while (selectedWords.size() < 4) {
            int randIndex = random.nextInt(dictionary.size());
            String word = dictionary.get(randIndex);

            if (!selectedWords.contains(word)) {
                selectedWords.add(word);
            }
        }

    }

    public void generateGrid() {
        ArrayList<Character> letters = new ArrayList<>();

        // Collect all 16 letters from selectedWords
        for (String word : selectedWords) {
            for (char letter : word.toCharArray()) {
                letters.add(letter);
            }
        }

        // Shuffle the letters
        Collections.shuffle(letters);

        // Fill the 4x4 grid with shuffled letters
        int index = 0;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                grid[row][col] = letters.get(index++);
            }
        }
    }

    public void displayGrid() {
        System.out.println("\n    A   B   C   D");
        System.out.println("  -----------------");

        for (int row = 0; row < 4; row++) {
            System.out.printf("%d | ", row + 1); // Print row number
            for (int col = 0; col < 4; col++) {
                System.out.printf(" %c |", grid[row][col]);
            }
            System.out.println("\n  -----------------");
        }
    }

    public boolean isValidGuess(String word) {
        word = word.toLowerCase(); // Ensure lowercase consistency

        if (!selectedWords.contains(word)) {
            return false; // Word is not in the selected list
        }

        // Count available letters in the grid
        HashMap<Character, Integer> letterCount = new HashMap<>();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                char letter = Character.toLowerCase(grid[row][col]);
                letterCount.put(letter, letterCount.getOrDefault(letter, 0) + 1);
            }
        }

        // Verify if the word can be formed
        for (char letter : word.toCharArray()) {
            if (letterCount.getOrDefault(letter, 0) > 0) {
                letterCount.put(letter, letterCount.get(letter) - 1);
            } else {
                return false; // If any letter is missing, it's invalid
            }
        }

        return true;
    }

    public boolean checkWin(ArrayList<String> guesses) {
        if (guesses.size() != 4) {
            return false;
        }

        // Create new lists to avoid modifying the originals
        ArrayList<String> sortedGuesses = new ArrayList<>(guesses);
        ArrayList<String> sortedSelectedWords = new ArrayList<>(selectedWords);

        // Convert words to lowercase before sorting
        for (int i = 0; i < sortedGuesses.size(); i++) {
            sortedGuesses.set(i, sortedGuesses.get(i).toLowerCase());
        }

        for (int i = 0; i < sortedSelectedWords.size(); i++) {
            sortedSelectedWords.set(i, sortedSelectedWords.get(i).toLowerCase());
        }

        // Sort both lists
        Collections.sort(sortedGuesses);
        Collections.sort(sortedSelectedWords);

        return sortedGuesses.equals(sortedSelectedWords);
    }

    public void playGame() {
        Scanner wordScanner = new Scanner(System.in);
        ArrayList<String> gameGuesses = new ArrayList<>();
        attemptsLeft = 3; //  Ensure attempts start at 3

        System.out.println("4 x 4 Word Grid Game");
        System.out.println("Try to find the 4 words hidden in the grid!");
        System.out.println("You have " + attemptsLeft + " attempts.");

        displayGrid();

        while (attemptsLeft > 0) { // Ensures loop ends when attempts are used up
            gameGuesses.clear();
            System.out.println("\nEnter your guesses for the 4 words in the grid:");

            // Take input and store it in gameGuesses
            for (int i = 1; i <= 4; i++) {
                System.out.print("Word " + i + ": ");
                String word = wordScanner.nextLine().trim().toLowerCase();
                gameGuesses.add(word);
            }

            // Validate all words
            boolean allValid = true;
            for (String guess : gameGuesses) {
                if (!isValidGuess(guess)) {
                    allValid = false;
                    break;
                }
            }

            if (!allValid) {
                System.out.println("One or more words are invalid. Try again.");
                attemptsLeft--; //  Deduct an attempt for invalid guesses
                System.out.println("Attempts left: " + attemptsLeft); //  Show attempts after incorrect input
                continue;
            }

            if (checkWin(gameGuesses)) {
                System.out.println("\nCongratulations! You found all the words. You Win!");
                return; // Exit after winning
            }

            // Incorrect guess case
            attemptsLeft--;
            if (attemptsLeft > 0) {
                System.out.println("\nIncorrect! You have " + attemptsLeft + " attempt(s) left.");
            }
        }

        // Game Over, reveal correct words
        System.out.println("\nGame Over! The correct words were: " + selectedWords);

    }
}