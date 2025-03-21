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
    private ArrayList<Integer> riskCount = new ArrayList<>();
    private int risk;
    
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

        ArrayList<Sensor> activeSensors = new ArrayList<>();
        for (Sensor sensor : sensors) {
            if (sensor.containsPoint(x, y)) {
                sensor.activationCount++;
                activeSensors.add(sensor);
            }
        }

        if (activeSensors.size() >= 3) {
            randomPoints.add(new Point(x, y));
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
        int humidity = rand.nextInt(41);
        int windSpeed = 10 + rand.nextInt(31);
        int temperature = rand.nextInt(51);

        
        if (humidity < 20) {
            risk += 2;
        } else if (humidity < 40) {
            risk += 1;
        }

        if (temperature > 35) {
            risk += 2;
        } else if (temperature >= 25) {
            risk += 1;
        }

        if (windSpeed > 30) {
            risk += 2;
        } else if (windSpeed >= 15) {
            risk += 1;
        }
        riskCount.add(risk);
        //System.out.println(risk + "\n");
        risk = 0;

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
            g.setColor(new Color(173, 216, 230, 150));
            g.fillOval(sensor.x - RADIUS, sensor.y - RADIUS, DIAMETER, DIAMETER);
            g.setColor(Color.BLUE);
            g.drawOval(sensor.x - RADIUS, sensor.y - RADIUS, DIAMETER, DIAMETER);
            g.setColor(Color.BLACK);
            g.fillOval(sensor.x - 3, sensor.y - 3, 6, 6);
        }
        
        g.setColor(Color.GREEN);
        for(int i = 0; i < randomPoints.size(); i++){
            Point p = randomPoints.get(i);
            int red = 50 + (riskCount.get(i) * 35); 
            int green = 100 - (riskCount.get(i) * 20); 
            int blue = 100 - (riskCount.get(i) * 20); 

            if(red > 255) red = 255;
            if(red < 0) red = 0;
            if(green > 255) green = 255;
            if(green < 0) green = 0;
            if(blue > 255) blue = 255;
            if(blue < 0) blue = 0;
            if(p.x <= 200 && p.y <= 200){
                red = 255;
                green = 0;
                blue = 0;
            } else{
                red = 50;
                green = 100;
                blue = 100;
            }
            System.out.println(red + " " + green + " " + blue);

            g.setColor(new Color(red, green, blue));
           // g.setColor(new Color(255, 0, 0, 50)); 
            g.fillOval(p.x - 8, p.y - 8, 16, 16); 
           // g.fillOval(p.x - 3, p.y - 3, 6, 6);
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