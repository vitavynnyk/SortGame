package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class NumberSortApp {
    private JFrame frame;
    private JTextField numInputField;

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

        JPanel introPanel = createIntroPanel();
        JPanel sortPanel = createSortPanel();

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
        JButton enterButton = new JButton("Enter");
        enterButton.setBackground(new Color(100, 149, 237));
        enterButton.setMaximumSize(new Dimension(100, enterButton.getPreferredSize().height));


        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        numInputField.setAlignmentX(Component.CENTER_ALIGNMENT);
        enterButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkInput();
                initSortPanel();
                ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Sort");
            }

            private void checkInput() {
                try {
                    int value = Integer.parseInt(numInputField.getText());
                    if (value > 100) {
                        JOptionPane.showMessageDialog(panel, "Please select a value smaller or equal to 100.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        numInputField.setText("");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    numInputField.setText("");
                }
            }
        });

        panel.add(Box.createVerticalGlue());
        panel.add(label);
        panel.add(numInputField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(enterButton);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createSortPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JButton sortButton = new JButton("Sort");
        JButton resetButton = new JButton("Reset");

        sortButton.addActionListener(e -> handleSortButtonClick());

        resetButton.addActionListener(e -> {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Intro");
            if (!isDescendingOrder) {
                isDescendingOrder = true;
            }
        });

        sortButton.setPreferredSize(new Dimension(80, 30));
        sortButton.setBackground(new Color(0, 100, 0));
        sortButton.setForeground(Color.WHITE);
        resetButton.setPreferredSize(new Dimension(80, 30));
        resetButton.setBackground(new Color(0, 100, 0));
        resetButton.setForeground(Color.WHITE);

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
        int randomNumbersCount = Integer.parseInt(numInputField.getText());
        if (randomNumbersCount < 0) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid number greater than or equal to 0.");
        }

        Integer[] numbersArray = new Integer[randomNumbersCount];

        numbersArray[0] = new Random().nextInt(30) + 1;

        for (int i = 1; i < randomNumbersCount; i++) {
            numbersArray[i] = new Random().nextInt(1000) + 1;
        }
        List<Integer> shuffledList = Arrays.asList(numbersArray);
        Collections.shuffle(shuffledList);

        shuffledNumbers = new ArrayList<>(shuffledList);

        updateSortPanel();
    }

    private void updateSortPanel() {
        int maxNumbersPerColumn = 10;
        int totalNumbers = shuffledNumbers.size();

        numbersPanel.removeAll();
        numbersPanel.setLayout(new GridBagLayout());

        Integer[] numbersArray = shuffledNumbers.toArray(new Integer[totalNumbers]);

        GridBagConstraints gbc = new GridBagConstraints();
        int currentColumnCount = 0;
        int currentRowCount = 0;

        for (int i = 0; i < totalNumbers; i++) {
            JButton numberButton = new JButton(String.valueOf(numbersArray[i]));
            numberButton.setBackground(new Color(25, 25, 112));
            numberButton.addActionListener(e -> handleNumberButtonClick(Integer.parseInt(numberButton.getText())));
            numberButton.setPreferredSize(new Dimension(100, 50));
            numberButton.setForeground(Color.WHITE);

            gbc.gridx = currentColumnCount;
            gbc.gridy = currentRowCount;
            gbc.insets = new Insets(5, 5, 5, 5);

            numbersPanel.add(numberButton, gbc);

            currentRowCount++;

            if (currentRowCount == maxNumbersPerColumn) {
                currentRowCount = 0;
                currentColumnCount++;
            }
        }
        numbersPanel.revalidate();
        numbersPanel.repaint();
    }

    private void handleSortButtonClick() {
        Integer[] numbersArray = shuffledNumbers.toArray(new Integer[0]);
        quickSort(numbersArray, 0, numbersArray.length - 1);
        shuffledNumbers = Arrays.asList(numbersArray);
        if (isDescendingOrder) {
            Collections.reverse(shuffledNumbers);
        }
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
            if (!isDescendingOrder) {
                isDescendingOrder = true;
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a value smaller or equal to 30.");
        }
    }
}