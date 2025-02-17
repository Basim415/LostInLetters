//
// Created by Basim Shahzad on 2/12/25.
//

#include "WordGridGame.h"

#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <algorithm>
#include <unordered_map>
#include <cctype>
#include <cstdlib>

#include "WordGridGame.h"

WordGridGame::WordGridGame() {
    int attemptsLeft = 3;
    selectedWords.clear();
    dictionary.clear();

    // Load words from file automatically
    loadDictionary("Dictionary.txt");

    // Initialize the grid
    for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
            grid[i][j] = ' ';
        }
    }
}

void WordGridGame::loadDictionary(string filename) {
    string line;
    ifstream in_file(filename);
    if (!in_file) {
        cerr << "Error opening file" << endl;
        return;
    }

    while (getline(in_file, line)) {
        if (line.length() == 4) {
            // Convert word to lowercase before storing
            transform(line.begin(), line.end(), line.begin(), ::tolower);
            dictionary.push_back(line);
        }
    }

    in_file.close();
}

void WordGridGame::selectWords() {
    if (dictionary.empty() || dictionary.size() < 4) {
        cerr << "Error: Not enough words in dictionary!" << endl;
        return;
    }

    srand(time(0)); // Seed random number generator
    selectedWords.clear();

    while (selectedWords.size() < 4) {
        int randIndex = rand() % dictionary.size(); //picks a random index from dictionary
        string word = dictionary[randIndex];

        // Convert to lowercase for consistency
        transform(word.begin(), word.end(), word.begin(), ::tolower);

        // Ensure no duplicate words
        if (find(selectedWords.begin(), selectedWords.end(), word) == selectedWords.end()) {
            selectedWords.push_back(word);
        }
    }

    cout << "Selected words: ";
    for (const string &word : selectedWords) {
        cout << word << " ";
    }
    cout << endl;
}

void WordGridGame::generateGrid() {
    vector<char> letters;

    // Collect all 16 letters from selectedWords
    for (const string &word : selectedWords) {
        for (char letter : word) {
            letters.push_back(letter);
        }
    }

    // Shuffle the letters randomly
    srand(time(0));
    for (int i = letters.size() - 1; i > 0; i--) {
        int j = rand() % (i + 1);
        swap(letters[i], letters[j]);
    }

    // Fill the 4x4 grid with shuffled letters
    int index = 0;
    for (int row = 0; row < 4; row++) {
        for (int col = 0; col < 4; col++) {
            grid[row][col] = letters[index++];
        }
    }
}


void WordGridGame::displayGrid() {
    cout << "\n    A   B   C   D" << endl;
    cout << "  -----------------" << endl;

    for (int row = 0; row < 4; row++) {
        cout << row + 1 << " | ";  // Print row number
        for (int col = 0; col < 4; col++) {
            cout << grid[row][col] << " | ";
        }
        cout << "\n  -----------------" << endl;
    }
}


bool WordGridGame::isValidGuess(string word) {
    // Convert input to lowercase
    transform(word.begin(), word.end(), word.begin(), ::tolower);

    // Check if the word is in selectedWords
    if (find(selectedWords.begin(), selectedWords.end(), word) == selectedWords.end()) {
        return false;
    }

    // Check if the word can be formed from grid letters
    unordered_map<char, int> gridLetterCount;
    for (int row = 0; row < 4; row++) {
        for (int col = 0; col < 4; col++) {
            gridLetterCount[tolower(grid[row][col])]++;
        }
    }

    for (char letter : word) {
        if (gridLetterCount[letter] > 0) {
            gridLetterCount[letter]--;  // Use the letter
        } else {
            return false;  // If any letter is missing, the word is invalid
        }
    }

    return true;
}

bool WordGridGame::checkWin(vector<string> guesses) {
    if (guesses.size() != 4) {
        return false;
    }

    vector<string> sortedGuesses = guesses;
    vector<string> sortedSelectedWords = selectedWords;

    //Convert to lowercase for consistency
    for (string &word : sortedGuesses) {
        transform(word.begin(), word.end(), word.begin(), ::tolower);
    }
    for (string &word : sortedSelectedWords) {
        transform(word.begin(), word.end(), word.begin(), ::tolower);
    }

    //Sort both lists and compare
    sort(sortedGuesses.begin(), sortedGuesses.end());
    sort(sortedSelectedWords.begin(), sortedSelectedWords.end());

    return sortedGuesses == sortedSelectedWords;
}

void WordGridGame::playGame() {
    vector<string> gameGuesses;
    int attemptsLeft = 3;

    cout << "4x4 Word Grid Game" << endl;
    cout << "Try to find the 4 words hidden in the grid!" << endl;
    cout << "You have 3 attempts." << endl << endl;

    displayGrid();

    do {
        gameGuesses.clear(); // Clear previous guesses at the start of each round

        // Prompt user to enter 4 words
        cout << "\nEnter your guesses for the 4 words in the grid:" << endl;

        string word;
        for (int i = 1; i <= 4; i++) {
            cout << "Word " << i << ": ";
            cin >> word;
            gameGuesses.push_back(word);
        }

        // Ensure exactly 4 words were entered
        if (gameGuesses.size() != 4) {
            cout << "You must enter exactly 4 words!" << endl;
            continue;
        }

        // Check if all words are valid
        bool allValid = true;
        for (const string &guess : gameGuesses) {
            if (!isValidGuess(guess)) {
                allValid = false;
                break;
            }
        }

        if (!allValid) {
            cout << "One or more words are invalid. Try again." << endl;
            continue;
        }

        // Check if the player guessed all words correctly
        if (checkWin(gameGuesses)) {
            cout << "\nCongratulations! You found all the words. You win!" << endl;
            return;
        } else {
            attemptsLeft--;
            cout << "\nIncorrect! You have " << attemptsLeft << " attempt(s) left." << endl;
        }

    } while (attemptsLeft > 0);

    // Reveal the correct words
    cout << "\nGame Over! The correct words were: ";
    for (const string &word : selectedWords) {
        cout << word << " ";
    }
    cout << endl;
}
