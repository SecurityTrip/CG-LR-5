package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    public static void main(String[] args) {
        // Запуск приложения в потоке EDT
        SwingUtilities.invokeLater(() -> {
            // Создаем окно
            JFrame frame = new JFrame("3D Model Viewer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            
            

            // Добавляем панель с 3D-моделью
            ThreeDModel modelPanel = new ThreeDModel();
            frame.add(modelPanel);

            JTextField radiusInput = new JTextField(Double.toString(modelPanel.R), 5);
            JButton updateButton = new JButton("Обновить R");

            

            JPanel controlPanel = new JPanel();
            controlPanel.add(new JLabel("R: "));
            controlPanel.add(radiusInput);
            controlPanel.add(updateButton);

            // Отображаем окно
            frame.setLayout(new BorderLayout());
            frame.add(modelPanel, BorderLayout.CENTER);
            frame.add(controlPanel, BorderLayout.SOUTH);

            updateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        double newR = Double.parseDouble(radiusInput.getText());
                        if (newR > 0) {
                            modelPanel.R = newR;
                            modelPanel.calculateModel();
                            modelPanel.repaint();
                        } else {
                            JOptionPane.showMessageDialog(frame, "Радиус должен быть больше 0!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Введите корректное значение для R!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

    
            frame.setVisible(true);
        });
    }
}
