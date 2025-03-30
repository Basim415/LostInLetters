import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class LostInLettersGUI extends JFrame {
    private LostInLetters game;
    private JLabel[][] gridLabels;
    private JTextField[] guessFields;
    private JLabel statusLabel;
    private int attemptsLeft;

    public LostInLettersGUI() {
        game = new LostInLetters();
        game.selectWords();
        game.generateGrid();
        attemptsLeft = 3;

        setTitle("Lost In Letters - GUI Version");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(250, 243, 224)); // main background

        // Top grid panel
        JPanel gridPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        gridPanel.setBorder(BorderFactory.createTitledBorder("Letter Grid"));
        gridPanel.setBackground(new Color(255, 248, 231)); // soft ivory

        gridLabels = new JLabel[4][4];
        char[][] grid = game.getGrid();

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                gridLabels[row][col] = new JLabel(String.valueOf(grid[row][col]), SwingConstants.CENTER);
                gridLabels[row][col].setFont(new Font("SansSerif", Font.BOLD, 28));
                gridLabels[row][col].setOpaque(true);
                gridLabels[row][col].setBackground(new Color(255, 248, 231));
                gridLabels[row][col].setForeground(new Color(62, 39, 35)); // dark brown
                gridLabels[row][col].setBorder(BorderFactory.createLineBorder(new Color(189, 189, 189)));
                gridPanel.add(gridLabels[row][col]);
            }
        }

        // Middle guess panel
        JPanel guessPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        guessPanel.setBackground(new Color(245, 245, 245)); // light gray
        guessFields = new JTextField[4];

        for (int i = 0; i < 4; i++) {
            guessPanel.add(new JLabel("Word " + (i + 1) + ":", SwingConstants.RIGHT));
            guessFields[i] = new JTextField();
            guessPanel.add(guessFields[i]);
        }

        JButton submitButton = new JButton("Submit Guesses");
        submitButton.setBackground(new Color(255, 213, 79)); // soft yellow
        submitButton.setForeground(Color.BLACK);
        submitButton.setFocusPainted(false);
        guessPanel.add(submitButton);

        statusLabel = new JLabel("<html><font color='blue'>Attempts left: 3</font></html>", SwingConstants.CENTER);
        guessPanel.add(statusLabel);

        add(gridPanel, BorderLayout.NORTH);
        add(guessPanel, BorderLayout.CENTER);

        // Button action
        submitButton.addActionListener(e -> {
            ArrayList<String> guesses = new ArrayList<>();
            for (JTextField field : guessFields) {
                guesses.add(field.getText().trim().toLowerCase());
            }

            boolean allValid = true;
            for (String guess : guesses) {
                if (!game.isValidGuess(guess)) {
                    allValid = false;
                    break;
                }
            }

            if (!allValid) {
                attemptsLeft--;
                if (attemptsLeft > 0) {
                    statusLabel.setText("<html><font color='red'>Invalid guess. Attempts left: " + attemptsLeft + "</font></html>");
                } else {
                    showGameOver();
                }
            } else if (game.checkWin(guesses)) {
                JOptionPane.showMessageDialog(this, "🎉 Congratulations! You found all the words!", "Victory", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            } else {
                attemptsLeft--;
                if (attemptsLeft > 0) {
                    statusLabel.setText("<html><font color='orange'>Incorrect! Attempts left: " + attemptsLeft + "</font></html>");
                } else {
                    showGameOver();
                }
            }
        });

        pack();
        setLocationRelativeTo(null); // Center
        setVisible(true);
    }

    private void showGameOver() {
        JOptionPane.showMessageDialog(this, "❌ Game Over!\nThe correct words were: " + game.getSelectedWords(), "Game Over", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LostInLettersGUI::new);
    }
}
