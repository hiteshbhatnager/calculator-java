import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Calculator {
    int boardWidth = 360;
    int boardHeight = 540;

    Color lightGray = new Color(212, 212, 210);
    Color lightDark = new Color(80, 80, 80);
    Color black = new Color(28, 28, 28);
    Color orange = new Color(255, 149, 0);

    JFrame frame = new JFrame("Calculator");
    JLabel displayLabel = new JLabel();
    JPanel displayPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    String currentDisplay = "0";
    String previousValue = "";
    String operation = "";
    boolean shouldResetDisplay = false;

    Calculator() {
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        displayLabel.setBackground(lightGray);
        displayLabel.setForeground(Color.white);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);

        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel);
        displayPanel.setBackground(black);
        frame.add(displayPanel, BorderLayout.NORTH);

        createButtonPanel();
        frame.add(buttonPanel, BorderLayout.CENTER);
    }

    void createButtonPanel() {
        buttonPanel.setLayout(new GridLayout(4, 4, 10, 10));
        buttonPanel.setBackground(black);
        buttonPanel.setBorder(new LineBorder(black, 10));

        String[] buttons = {
                "C", "/", "*", "-",
                "7", "8", "9", "+",
                "4", "5", "6", "=",
                "1", "2", "3", "."
        };

        for (String btnText : buttons) {
            JButton button = new JButton(btnText);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setFocusable(false);

            if (btnText.equals("=")) {
                button.setBackground(orange);
                button.setForeground(Color.white);
            } else if (btnText.equals("C")) {
                button.setBackground(lightDark);
                button.setForeground(Color.white);
            } else if ("/-+*".contains(btnText)) {
                button.setBackground(orange);
                button.setForeground(Color.white);
            } else {
                button.setBackground(lightGray);
                button.setForeground(Color.black);
            }

            button.setOpaque(true);
            button.setBorderPainted(false);
            button.addActionListener(new ButtonClickListener(btnText));
            buttonPanel.add(button);
        }
    }

    class ButtonClickListener implements ActionListener {
        String buttonText;

        ButtonClickListener(String text) {
            this.buttonText = text;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (buttonText.equals("C")) {
                currentDisplay = "0";
                previousValue = "";
                operation = "";
                shouldResetDisplay = false;
                displayLabel.setText(currentDisplay);
            } else if ("/-+*".contains(buttonText)) {
                if (!previousValue.isEmpty() && !shouldResetDisplay) {
                    calculate();
                }
                previousValue = currentDisplay;
                operation = buttonText;
                shouldResetDisplay = true;
            } else if (buttonText.equals("=")) {
                calculate();
                operation = "";
                shouldResetDisplay = true;
            } else if (buttonText.equals(".")) {
                if (shouldResetDisplay) {
                    currentDisplay = "0.";
                    shouldResetDisplay = false;
                } else if (!currentDisplay.contains(".")) {
                    currentDisplay += ".";
                }
                displayLabel.setText(currentDisplay);
            } else {
                if (shouldResetDisplay) {
                    currentDisplay = buttonText;
                    shouldResetDisplay = false;
                } else {
                    if (currentDisplay.equals("0")) {
                        currentDisplay = buttonText;
                    } else {
                        currentDisplay += buttonText;
                    }
                }
                displayLabel.setText(currentDisplay);
            }
        }

        void calculate() {
            if (previousValue.isEmpty() || operation.isEmpty()) {
                return;
            }

            double prev = Double.parseDouble(previousValue);
            double current = Double.parseDouble(currentDisplay);
            double result = 0;

            switch (operation) {
                case "+":
                    result = prev + current;
                    break;
                case "-":
                    result = prev - current;
                    break;
                case "*":
                    result = prev * current;
                    break;
                case "/":
                    if (current != 0) {
                        result = prev / current;
                    } else {
                        displayLabel.setText("Error");
                        currentDisplay = "0";
                        previousValue = "";
                        operation = "";
                        shouldResetDisplay = true;
                        return;
                    }
                    break;
            }

            if (result == (long) result) {
                currentDisplay = String.format("%d", (long) result);
            } else {
                currentDisplay = String.format("%.2f", result);
            }
            displayLabel.setText(currentDisplay);
            previousValue = "";
        }
    }
}
