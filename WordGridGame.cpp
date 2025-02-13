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