package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class ThreeDModel extends JPanel {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private double[][] vertices;
    private int[][] edges;
    private double scale = 1.0;
    private double rotateX = 0;
    private double rotateY = 0;
    private double rotateZ = 0;
    private double translateX = 0;
    private double translateY = 0;
    private double translateZ = 0;

    public ThreeDModel() {
        calculateModel();
        setupKeyControls();
    }

    private void calculateModel() {
        int stepsAlpha = 30;
        int stepsBeta = 30;
        double R = 1.0;

        List<double[]> points = new ArrayList<>();
        List<int[]> connections = new ArrayList<>();
        for (int i = 0; i <= stepsAlpha; i++) {
            double alpha = Math.PI * i / stepsAlpha; // Угол α
            for (int j = 0; j <= stepsBeta; j++) {
                double beta = 2 * Math.PI * j / stepsBeta; // Угол β
        
                // Вычисление координат x, y
                double x = R * Math.sin(alpha) * Math.cos(beta);
                double y = R * Math.sin(alpha) * Math.sin(beta);
        
                // Вычисление координаты z с учетом условия
                double z;
                if (R * Math.cos(alpha) > 2) {
                    z = R * Math.cos(alpha) + 2.5 * R * Math.pow(Math.cos(alpha) - 0.5, 2);
                } else {
                    z = R * Math.cos(alpha);
                }
        
                // Добавляем точку в список
                points.add(new double[]{x, y, z});
        
                // Связываем точки, чтобы формировать ребра модели
                if (j > 0) connections.add(new int[]{(i * (stepsBeta + 1)) + j - 1, (i * (stepsBeta + 1)) + j});
                if (i > 0) connections.add(new int[]{((i - 1) * (stepsBeta + 1)) + j, (i * (stepsBeta + 1)) + j});
            }
        }
        

        vertices = points.toArray(new double[0][0]);
        edges = connections.toArray(new int[0][0]);
    }

    private void setupKeyControls() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> translateY -= 0.1;
                    case KeyEvent.VK_DOWN -> translateY += 0.1;
                    case KeyEvent.VK_LEFT -> translateX -= 0.1;
                    case KeyEvent.VK_RIGHT -> translateX += 0.1;
                    case KeyEvent.VK_W -> rotateX -= Math.PI / 18;
                    case KeyEvent.VK_S -> rotateX += Math.PI / 18;
                    case KeyEvent.VK_A -> rotateY -= Math.PI / 18;
                    case KeyEvent.VK_D -> rotateY += Math.PI / 18;
                    case KeyEvent.VK_Q -> rotateZ -= Math.PI / 18;
                    case KeyEvent.VK_E -> rotateZ += Math.PI / 18;
                    case KeyEvent.VK_PLUS, KeyEvent.VK_ADD -> scale *= 1.1;
                    case KeyEvent.VK_MINUS, KeyEvent.VK_SUBTRACT -> scale *= 0.9;
                }
                repaint();
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }

    private int[] project(double x, double y, double z) {
        double perspective = 5 / (5 - z);
        int screenX = (int) (WIDTH / 2 + x * scale * perspective * 100 + translateX * 100);
        int screenY = (int) (HEIGHT / 2 - y * scale * perspective * 100 + translateY * 100);
        return new int[]{screenX, screenY};
    }

    private double[] applyTransformations(double x, double y, double z) {
        double tmpY = y * Math.cos(rotateX) - z * Math.sin(rotateX);
        double tmpZ = y * Math.sin(rotateX) + z * Math.cos(rotateX);
        y = tmpY;
        z = tmpZ;

        double tmpX = x * Math.cos(rotateY) + z * Math.sin(rotateY);
        tmpZ = -x * Math.sin(rotateY) + z * Math.cos(rotateY);
        x = tmpX;
        z = tmpZ;

        tmpX = x * Math.cos(rotateZ) - y * Math.sin(rotateZ);
        tmpY = x * Math.sin(rotateZ) + y * Math.cos(rotateZ);
        x = tmpX;
        y = tmpY;

        return new double[]{x, y, z + translateZ};
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (int[] edge : edges) {
            double[] point1 = applyTransformations(vertices[edge[0]][0], vertices[edge[0]][1], vertices[edge[0]][2]);
            double[] point2 = applyTransformations(vertices[edge[1]][0], vertices[edge[1]][1], vertices[edge[1]][2]);

            int[] p1 = project(point1[0], point1[1], point1[2]);
            int[] p2 = project(point2[0], point2[1], point2[2]);

            g2d.draw(new Line2D.Double(p1[0], p1[1], p2[0], p2[1]));
        }
    }
}
