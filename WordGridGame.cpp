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

