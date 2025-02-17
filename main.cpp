#include <iostream>
#include "WordGridGame.h"

using namespace std;

int main() {
    // Create an instance of the game
    WordGridGame game;

    // Load the dictionary from a file
    game.loadDictionary("Dictionary.txt");

    // Select 4 random words for the game
    game.selectWords();

    // Generate the randomized 4x4 word grid
    game.generateGrid();

    // Start the game loop
    game.playGame();

    return 0;
}