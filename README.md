# Lost In Letters 🧩

A simple Java-based word puzzle game inspired by [FourWordGrid](https://fourwordgrid.web.app/#/), where players must uncover four hidden 4-letter words randomly placed in a 4x4 grid.

## 🎮 About the Game

In **Lost In Letters**, players are given a 4x4 grid of letters that contains exactly **four hidden 4-letter words**. These words are randomly selected from a dictionary and their letters are shuffled and placed into the grid.

You have **3 attempts** to guess the correct set of words. Guesses must be valid (using available letters) and match the exact hidden words chosen by the game.

There are two ways to play:
- **Command-Line Version**: Simple text interface
- **GUI Version (Swing)**: Visually enhanced desktop app

---

## 🗂️ Project Structure

📁 LostInLetters: 
LostInLetters.java # Core game logic: grid generation, word checking 
Game.java # Entry point for CLI version 
LostInLettersGUI.java # Swing GUI for an interactive version 
dictionary.txt # List of 4-letter English words

---

## 🖥️ GUI Features

- 🧠 4x4 grid display with randomized letters
- 🔤 Input fields for your 4 guesses
- 🚦 Real-time attempt counter and validation
- 💬 Feedback messages with colorful UI
- ❌ Game Over or 🎉 Victory popups!

![Preview](https://user-images.githubusercontent.com/your-screenshot-placeholder.png)

---

## 🧑‍💻 How to Run

### Prerequisites
- Java JDK 8 or higher
- A Java IDE (like IntelliJ or Eclipse) OR terminal access with `javac`/`java`

### 🖥️ GUI Version

javac LostInLetters.java LostInLettersGUI.java
java LostInLettersGUI

### 📟 Command-Line Version
bash
javac LostInLetters.java Game.java
java Game
