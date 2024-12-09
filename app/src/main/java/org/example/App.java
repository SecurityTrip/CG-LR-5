package org.example;

import javax.swing.*;

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

            // Отображаем окно
            frame.setVisible(true);
        });
    }
}
