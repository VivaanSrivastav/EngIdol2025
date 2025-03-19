import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

class Sensor {
    int x, y;
    int activationCount = 0;

    public Sensor(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean containsPoint(int px, int py) {
        return Math.sqrt(Math.pow(px - x, 2) + Math.pow(py - y, 2)) <= CircleGrid.RADIUS;
    }
}

public class CircleGrid extends JPanel {
    public static final int ROWS = 13;
    public static final int COLS = 13;
    public static final int RADIUS = 40;
    public static final int DIAMETER = RADIUS * 2;
    public static final int PADDING = -40;
    public static final double VERTICAL_SPACING = (Math.sqrt(3)/2) * RADIUS;
    public static final int HORIZONTAL_SPACING = DIAMETER + PADDING;
    private static final int WIDTH = 560;
    private static final int HEIGHT = 560;
    private ArrayList<Point> randomPoints;
    private ArrayList<Sensor> sensors;
    
    public CircleGrid() {
        randomPoints = new ArrayList<>();
        sensors = new ArrayList<>();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = col * HORIZONTAL_SPACING + ((row % 2) * 20);
                int y = (int) (row * VERTICAL_SPACING);
                sensors.add(new Sensor(x + RADIUS, y + RADIUS));
            }
        }
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    addRandomPoint();
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }

    private void addRandomPoint() {
        Random rand = new Random();
        int x = rand.nextInt(WIDTH);
        int y = rand.nextInt(HEIGHT);
        randomPoints.add(new Point(x, y));

        ArrayList<Sensor> activeSensors = new ArrayList<>();
        for (Sensor sensor : sensors) {
            if (sensor.containsPoint(x, y)) {
                sensor.activationCount++;
                activeSensors.add(sensor);
            }
        }

        if (activeSensors.size() >= 3) {
            Sensor s1 = activeSensors.get(0);
            Sensor s2 = activeSensors.get(1);
            Sensor s3 = activeSensors.get(2);
            int d1 = RADIUS, d2 = RADIUS, d3 = RADIUS;
            Point estimatedPoint = triangulation(s1.x, s1.y, d1, s2.x, s2.y, d2, s3.x, s3.y, d3);
            
            System.out.println("Sensor at (" + s1.x + ", " + s1.y + "), " +
                               "sensor at (" + s2.x + ", " + s2.y + "), and " +
                               "sensor at (" + s3.x + ", " + s3.y + ") " +
                               "detect lightning at (" + estimatedPoint.x + ", " + estimatedPoint.y + ")");
        }
        repaint();
    }

    private Point triangulation(int c1x, int c1y, int c1d, int c2x, int c2y, int c2d, int c3x, int c3y, int c3d) {
        int k = (c1x * c1x - c2x * c2x + c1y * c1y - c2y * c2y + c2d * c2d - c1d * c1d) / 2;
        int l = (c1x * c1x - c3x * c3x + c1y * c1y - c3y * c3y + c3d * c3d - c1d * c1d) / 2;

        int x = (k * (c1y - c3y) - l * (c1y - c2y)) / ((c1x - c2x) * (c1y - c3y) - (c1x - c3x) * (c1y - c2y));
        int y = (l * (c1x - c2x) - k * (c1x - c3x)) / ((c1x - c2x) * (c1y - c3y) - (c1x - c3x) * (c1y - c2y));
        return new Point(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (Sensor sensor : sensors) {
            g.setColor(sensor.activationCount > 0 ? Color.RED : new Color(173, 216, 230, 150));
            g.fillOval(sensor.x - RADIUS, sensor.y - RADIUS, DIAMETER, DIAMETER);
            g.setColor(Color.BLUE);
            g.drawOval(sensor.x - RADIUS, sensor.y - RADIUS, DIAMETER, DIAMETER);
        }
        
        g.setColor(Color.GREEN);
        for (Point p : randomPoints) {
            g.fillOval(p.x - 3, p.y - 3, 6, 6);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hexagonal Circle Grid");
        CircleGrid panel = new CircleGrid();
        frame.add(panel);
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}