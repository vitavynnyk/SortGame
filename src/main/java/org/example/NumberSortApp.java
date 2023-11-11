package org.example;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NumberSortApp {
    private JFrame frame;
    private JPanel introPanel;
    private JPanel sortPanel;
    private JButton enterButton;
    private JTextField numInputField;
    private JButton sortButton;
    private JButton resetButton;

    private JPanel numbersPanel;
    private List<Integer> shuffledNumbers;
    private boolean isDescendingOrder = true;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new NumberSortApp().initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initialize() {
        frame = new JFrame("Number Sort App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);
        frame.setLayout(new CardLayout());

        introPanel = createIntroPanel();
        sortPanel = createSortPanel();

        frame.add(introPanel, "Intro");
        frame.add(sortPanel, "Sort");

        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Intro");

        frame.setVisible(true);
    }

    private JPanel createIntroPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("How many numbers to display? ");
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        numInputField = new JTextField();
        numInputField.setMaximumSize(new Dimension(100, numInputField.getPreferredSize().height));
        numInputField.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        numInputField.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        enterButton = new JButton("Enter");
        enterButton.setBackground(new Color(100, 149, 237));
        enterButton.setMaximumSize(new Dimension(100, enterButton.getPreferredSize().height));


        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        numInputField.setAlignmentX(Component.CENTER_ALIGNMENT);
        enterButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initSortPanel();
                ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Sort");
            }
        });
        // Добавляем слушатель для текстового поля
        numInputField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkInput();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkInput();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkInput();
            }

            private void checkInput() {
                try {
                    int value = Integer.parseInt(numInputField.getText());
                    if (value > 30) {
                        JOptionPane.showMessageDialog(panel, "Please select a value smaller or equal to 30.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        numInputField.setText("");
                    }
                } catch (NumberFormatException ex) {
                    // Ignore if the input is not a valid integer
                }
            }
        });

        panel.add(Box.createVerticalGlue());
        panel.add(label);
        panel.add(numInputField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(enterButton);
        panel.add(Box.createVerticalGlue());  // Прежний пружинный элемент

        return panel;
    }

    private JPanel createSortPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        sortButton = new JButton("Sort");
        resetButton = new JButton("Reset");

        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSortButtonClick();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Intro");
            }
        });

        sortButton.setPreferredSize(new Dimension(80, 30));
        sortButton.setBackground(new Color(0, 100, 0));
        resetButton.setPreferredSize(new Dimension(80, 30));
        resetButton.setBackground(new Color(0, 100, 0));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.add(sortButton);
        buttonsPanel.add(resetButton);

        panel.add(buttonsPanel, BorderLayout.EAST);

        numbersPanel = new JPanel();
        panel.add(new JScrollPane(numbersPanel), BorderLayout.CENTER);

        return panel;
    }

    private void initSortPanel() {

        try {
            int randomNumbersCount = Integer.parseInt(numInputField.getText());
            if (randomNumbersCount < 0) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number greater than or equal to 0.");
            }
            Integer[] numbersArray = new Integer[randomNumbersCount];
            Random random = new Random();
            for (int i = 0; i < randomNumbersCount; i++) {
                numbersArray[i] = random.nextInt(1000) + 1;
            }
            shuffledNumbers = Arrays.asList(numbersArray);

            numbersArray[random.nextInt(randomNumbersCount)] = random.nextInt(30) + 1;
            Collections.shuffle(shuffledNumbers);

            updateSortPanel();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid number.");
        }
    }

    private void updateSortPanel() {
        int maxNumbersPerColumn = 10;
        int totalNumbers = shuffledNumbers.size();
        int columns = (int) Math.ceil((double) totalNumbers / maxNumbersPerColumn);


        numbersPanel.removeAll();
        numbersPanel.setLayout(new GridLayout(maxNumbersPerColumn, columns, 5, 5));

        Integer[] numbersArray = shuffledNumbers.toArray(new Integer[totalNumbers]);
        quickSort(numbersArray, 0, totalNumbers - 1);

        if (!isDescendingOrder) {
            Arrays.sort(numbersArray, Collections.reverseOrder());
        }
        int numbersPerColumn = totalNumbers / columns;
        int remainder = totalNumbers % columns;
        for (int i = 0; i < columns; i++) {
            int currentColumnCount = numbersPerColumn + (i < remainder ? 1 : 0);
            for (int j = 0; j < currentColumnCount; j++) {
                int index = j * columns + i;

                JButton numberButton = new JButton(String.valueOf(numbersArray[index]));
                numberButton.setBackground(new Color(25, 25, 112));
                numberButton.addActionListener(e -> handleNumberButtonClick(Integer.parseInt(numberButton.getText())));
                numberButton.setForeground(Color.WHITE);
                numbersPanel.add(numberButton);
            }
        }
        numbersPanel.revalidate();
        numbersPanel.repaint();
    }

    private void handleSortButtonClick() {
        isDescendingOrder = !isDescendingOrder;
        updateSortPanel();
    }

    private void quickSort(Integer[] array, int low, int high) {
        if (low < high) {
            int partitionIndex = partition(array, low, high);

            quickSort(array, low, partitionIndex - 1);
            quickSort(array, partitionIndex + 1, high);
        }
    }

    private int partition(Integer[] array, int low, int high) {
        int pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array[j] < pivot) {
                i++;

                // swap arr[i] and arr[j]
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }

    private void handleNumberButtonClick(int clickedValue) {
        if (clickedValue <= 30) {
            initSortPanel();
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a value smaller or equal to 30.");
        }
    }
}
