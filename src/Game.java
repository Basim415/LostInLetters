public class Game {
    public static void main(String[] args) {
        LostInLetters game = new LostInLetters();
        boolean success = game.selectWords();

        if (!success) {
            System.out.println("Game setup failed. Please try again later.");
            return;
        }

        game.generateGrid();
        game.playGame();
    }
}
