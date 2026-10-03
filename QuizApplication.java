

import javax.swing.*;
import java.awt.*;

public class QuizApplication extends JFrame {

    String[] questions = {
        "Which language is mainly used for Android development?",
        "Which of these is not a Java keyword?",
        "Which concept allows Java to achieve multiple inheritance?",
        "Which data structure follows FIFO?",
        "Which method is the starting point of a Java program?"
    };

    String[][] options = {
        {"Java", "HTML", "SQL", "CSS"},
        {"class", "static", "main", "integer"},
        {"Interfaces", "Classes", "Constructors", "Packages"},
        {"Stack", "Queue", "Tree", "Graph"},
        {"start()", "main()", "run()", "execute()"}
    };

    int[] correctAnswers = {0, 3, 0, 1, 1};

    int currentQuestion = 0;
    int score = 0;
    int timeLeft = 10;

    JLabel questionLabel;
    JLabel timerLabel;
    JRadioButton[] optionButtons;
    ButtonGroup buttonGroup;
    JButton submitButton;
    Timer timer;

    int[] userAnswers;

    public QuizApplication() {

        userAnswers = new int[questions.length];

        for (int i = 0; i < userAnswers.length; i++) {
            userAnswers[i] = -1;
        }

        setTitle("Quiz Application");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createQuizScreen();

        setVisible(true);
    }

    void createQuizScreen() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        );

        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel(
            "JAVA QUIZ",
            SwingConstants.CENTER
        );

        titleLabel.setFont(
            new Font("Arial", Font.BOLD, 28)
        );

        timerLabel = new JLabel(
            "Time: 10",
            SwingConstants.CENTER
        );

        timerLabel.setFont(
            new Font("Arial", Font.BOLD, 20)
        );

        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(timerLabel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(
            new BoxLayout(centerPanel, BoxLayout.Y_AXIS)
        );

        questionLabel = new JLabel();
        questionLabel.setFont(
            new Font("Arial", Font.BOLD, 18)
        );

        centerPanel.add(questionLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        optionButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {

            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(
                new Font("Arial", Font.PLAIN, 16)
            );

            buttonGroup.add(optionButtons[i]);

            centerPanel.add(optionButtons[i]);
            centerPanel.add(Box.createVerticalStrut(10));
        }

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        submitButton = new JButton("Submit Answer");

        submitButton.addActionListener(e -> submitAnswer());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(submitButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        loadQuestion();
    }

    void loadQuestion() {

        buttonGroup.clearSelection();

        questionLabel.setText(
            "Question " + (currentQuestion + 1) +
            " of " + questions.length +
            ": " + questions[currentQuestion]
        );

        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(
                options[currentQuestion][i]
            );
        }

        timeLeft = 10;
        timerLabel.setText("Time: " + timeLeft);

        startTimer();
    }

    void startTimer() {

        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(1000, e -> {

            timeLeft--;

            timerLabel.setText(
                "Time: " + timeLeft
            );

            if (timeLeft == 0) {

                timer.stop();

                JOptionPane.showMessageDialog(
                    this,
                    "Time's up!"
                );

                userAnswers[currentQuestion] = -1;

                nextQuestion();
            }
        });

        timer.start();
    }

    void submitAnswer() {

        timer.stop();

        int selectedAnswer = -1;

        for (int i = 0; i < 4; i++) {

            if (optionButtons[i].isSelected()) {
                selectedAnswer = i;
                break;
            }
        }

        if (selectedAnswer == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Please select an answer!"
            );

            timer.start();
            return;
        }

        userAnswers[currentQuestion] = selectedAnswer;

        if (selectedAnswer == correctAnswers[currentQuestion]) {
            score++;
        }

        nextQuestion();
    }

    void nextQuestion() {

        currentQuestion++;

        if (currentQuestion < questions.length) {
            loadQuestion();
        } else {
            showResult();
        }
    }

    void showResult() {

        if (timer != null) {
            timer.stop();
        }

        getContentPane().removeAll();

        JPanel resultPanel = new JPanel(
            new BorderLayout(20, 20)
        );

        JLabel resultTitle = new JLabel(
            "QUIZ RESULT",
            SwingConstants.CENTER
        );

        resultTitle.setFont(
            new Font("Arial", Font.BOLD, 28)
        );

        resultPanel.add(
            resultTitle,
            BorderLayout.NORTH
        );

        JTextArea resultArea = new JTextArea();

        resultArea.setEditable(false);

        StringBuilder result = new StringBuilder();

        result.append("Final Score: ")
              .append(score)
              .append(" / ")
              .append(questions.length)
              .append("\n\n");

        for (int i = 0; i < questions.length; i++) {

            result.append("Question ")
                  .append(i + 1)
                  .append(": ")
                  .append(questions[i])
                  .append("\n");

            if (userAnswers[i] == correctAnswers[i]) {

                result.append("Status: CORRECT\n");

            } else {

                result.append("Status: INCORRECT\n");

                if (userAnswers[i] == -1) {

                    result.append(
                        "Your Answer: Not Answered\n"
                    );

                } else {

                    result.append("Your Answer: ")
                          .append(options[i][userAnswers[i]])
                          .append("\n");
                }

                result.append("Correct Answer: ")
                      .append(options[i][correctAnswers[i]])
                      .append("\n");
            }

            result.append("\n");
        }

        resultArea.setText(result.toString());

        resultPanel.add(
            new JScrollPane(resultArea),
            BorderLayout.CENTER
        );

        JButton restartButton =
            new JButton("Restart Quiz");

        restartButton.addActionListener(
            e -> restartQuiz()
        );

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(restartButton);

        resultPanel.add(
            bottomPanel,
            BorderLayout.SOUTH
        );

        add(resultPanel);

        revalidate();
        repaint();
    }

    void restartQuiz() {

        currentQuestion = 0;
        score = 0;

        for (int i = 0; i < userAnswers.length; i++) {
            userAnswers[i] = -1;
        }

        getContentPane().removeAll();

        createQuizScreen();

        revalidate();
        repaint();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new QuizApplication();
        });
    }
}