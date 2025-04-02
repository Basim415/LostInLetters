# Lost In Letters 🧩

A simple Java-based word puzzle game inspired by [FourWordGrid](https://fourwordgrid.web.app/#/), where players must uncover four hidden 4-letter words randomly placed in a 4x4 grid.

## 🎮 About the Game

In **Lost In Letters**, players are presented with a 4x4 grid of shuffled letters that contains exactly **four hidden 4-letter English words**. These words are:
- Randomly fetched from a public API at runtime
- Checked for validity using a live dictionary lookup

You have **3 attempts** to guess the correct set of words. Guesses must:
- Use only letters available in the grid
- Be one of the four selected words
- Be valid English words

There are two ways to play:
- **Command-Line Version**: Simple text interface
- **GUI Version (Swing)**: Visually enhanced desktop app

---

## 🌐 Live Word API Integration

Unlike static word games, this project fetches **real, random 4-letter words** from:

- 🔤 [Random Word API (Vercel)](https://random-word-api.vercel.app/)
- 📚 [Free Dictionary API](https://dictionaryapi.dev/) — used to validate guesses

This means every game session is different and internet-powered.

---

## 🗂️ Project Structure
📁 LostInLetters: 
LostInLetters.java # Core game logic: API integration, grid, validation 
Game.java # Entry point for the command-line version 
LostInLettersGUI.java # Swing GUI for an interactive version 
dictionary.txt # (Optional) Legacy fallback word list

---

## 🖥️ GUI Features

- 🧠 4x4 grid display with randomized API-fetched letters
- 🔤 Input fields for your 4 guesses
- 🚦 Real-time attempt counter and validation
- ❌ Game Over or 🎉 Victory popups
- 🟦 Color-coded status feedback

![Preview](https://github.com/user-attachments/assets/f72ce65b-e89d-487b-ab5e-28eaf564b2db)

---

## 🧑‍💻 How to Run

### ✅ Prerequisites
- Java JDK 8 or higher
- A Java IDE (like IntelliJ or Eclipse) **or** terminal access with `javac` / `java`

###  🖥️ GUI Version

```bash
javac LostInLetters.java LostInLettersGUI.java
java LostInLettersGUI
```

### 📟  Command-Line Version
```bash
javac LostInLetters.java Game.java
java Game
```
