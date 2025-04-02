import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Collections;

public class LostInLetters {
    private ArrayList<String> selectedWords;
    private char[][] grid;
    private int attemptsLeft;

    public LostInLetters() {
        this.attemptsLeft = 3;
        this.selectedWords = new ArrayList<>();
        this.grid = new char[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                grid[i][j] = ' ';
            }
        }
    }

    // Fetch a random 4-letter word from the Random Word API
    public String fetchRandomFourLetterWord() {
        try {
            URL url = new URL("https://random-word-api.herokuapp.com/word?number=1&length=4");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);

            int status = conn.getResponseCode();
            if (status == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = in.readLine();
                in.close();

                if (line != null && line.length() > 2) {
                    line = line.replace("[", "").replace("]", "").replace("\"", "");
                    return line.toLowerCase();
                }
            }
        } catch (Exception e) {
            System.out.println("API error: " + e.getMessage());
        }
        return null;
    }

    // Select 4 unique words from the API
    public boolean selectWords() {
        selectedWords.clear();
        int tries = 50;

        while (selectedWords.size() < 4 && tries > 0) {
            String word = fetchRandomFourLetterWord();

            if (word != null &&
                    word.length() == 4 &&
                    word.matches("^[a-zA-Z]+$") &&  // ensure only alphabetic characters
                    !selectedWords.contains(word)) {

                selectedWords.add(word.toLowerCase());
            }

            tries--;
        }

        if (selectedWords.size() < 4) {
            System.out.println("❌ Error: Could not fetch enough words from API.");
            return false;
        }

        return true;
    }



    public void generateGrid() {
        ArrayList<Character> letters = new ArrayList<>();

        for (String word : selectedWords) {
            for (char letter : word.toCharArray()) {
                letters.add(letter);
            }
        }

        Collections.shuffle(letters);

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
            System.out.printf("%d | ", row + 1);
            for (int col = 0; col < 4; col++) {
                System.out.printf(" %c |", grid[row][col]);
            }
            System.out.println("\n  -----------------");
        }
    }

    // Validate a user guess
    public boolean isValidGuess(String word) {
        word = word.toLowerCase();

        if (!selectedWords.contains(word)) {
            return false;
        }

        if (!isRealWord(word)) {
            return false;
        }

        HashMap<Character, Integer> letterCount = new HashMap<>();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                char letter = Character.toLowerCase(grid[row][col]);
                letterCount.put(letter, letterCount.getOrDefault(letter, 0) + 1);
            }
        }

        for (char letter : word.toCharArray()) {
            if (letterCount.getOrDefault(letter, 0) > 0) {
                letterCount.put(letter, letterCount.get(letter) - 1);
            } else {
                return false;
            }
        }

        return true;
    }

    // Use Free Dictionary API to check if the word exists
    public boolean isRealWord(String word) {
        try {
            String apiUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word.toLowerCase();
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            int status = conn.getResponseCode();
            return status == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkWin(ArrayList<String> guesses) {
        if (guesses.size() != 4) return false;

        ArrayList<String> sortedGuesses = new ArrayList<>(guesses);
        ArrayList<String> sortedSelectedWords = new ArrayList<>(selectedWords);

        for (int i = 0; i < sortedGuesses.size(); i++) {
            sortedGuesses.set(i, sortedGuesses.get(i).toLowerCase());
        }

        for (int i = 0; i < sortedSelectedWords.size(); i++) {
            sortedSelectedWords.set(i, sortedSelectedWords.get(i).toLowerCase());
        }

        Collections.sort(sortedGuesses);
        Collections.sort(sortedSelectedWords);

        return sortedGuesses.equals(sortedSelectedWords);
    }

    public void playGame() {
        Scanner wordScanner = new Scanner(System.in);
        ArrayList<String> gameGuesses = new ArrayList<>();
        attemptsLeft = 3;

        System.out.println("4 x 4 Word Grid Game");
        System.out.println("Try to find the 4 words hidden in the grid!");
        System.out.println("You have " + attemptsLeft + " attempts.");

        displayGrid();

        while (attemptsLeft > 0) {
            gameGuesses.clear();
            System.out.println("\nEnter your guesses for the 4 words in the grid:");

            for (int i = 1; i <= 4; i++) {
                System.out.print("Word " + i + ": ");
                String word = wordScanner.nextLine().trim().toLowerCase();
                gameGuesses.add(word);
            }

            boolean allValid = true;
            for (String guess : gameGuesses) {
                if (!isValidGuess(guess)) {
                    allValid = false;
                    break;
                }
            }

            if (!allValid) {
                System.out.println("One or more words are invalid or not found. Try again.");
                attemptsLeft--;
                System.out.println("Attempts left: " + attemptsLeft);
                continue;
            }

            if (checkWin(gameGuesses)) {
                System.out.println("\n🎉 Congratulations! You found all the words. You Win!");
                return;
            }

            attemptsLeft--;
            if (attemptsLeft > 0) {
                System.out.println("\nIncorrect! You have " + attemptsLeft + " attempt(s) left.");
            }
        }

        System.out.println("\n❌ Game Over! The correct words were: " + selectedWords);
    }

    public char[][] getGrid() {
        return grid;
    }

    public ArrayList<String> getSelectedWords() {
        return selectedWords;
    }
}
