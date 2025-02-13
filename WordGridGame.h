//
// Created by Basim Shahzad on 2/12/25.
//

#ifndef WORD_GRID_GAME_H
#define WORD_GRID_GAME_H


#include <vector>
#include <string>
#include <fstream>
#include <iostream>

using namespace std;

class WordGridGame {
private:
    vector<string> dictionary;
    vector<string> selectedWords;
    char grid[4][4];
    int attemptsLeft;

public:
    // Constructor
    WordGridGame();

    // Methods
    void loadDictionary(string filename);
    void selectWords();
    void generateGrid();
    void displayGrid();
    bool isValidGuess(string word);
    void playGame();
    bool checkWin(vector<string> guesses);
};


#endif //WORD_GRID_GAME_H
