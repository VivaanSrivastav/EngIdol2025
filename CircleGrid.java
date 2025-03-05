import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class CircleGrid extends JPanel {
    private static final int ROWS = 13;
    private static final int COLS = 13;
    private static final int RADIUS = 40;
    private static final int DIAMETER = RADIUS * 2;
    private static final int PADDING = -40;
    private static final double VERTICAL_SPACING = (Math.sqrt(3)/2) * RADIUS;
    private static final int HORIZONTAL_SPACING = DIAMETER + PADDING;
    private static final int WIDTH = 580;
    private static final int HEIGHT = 496;
    private ArrayList<Lightning> lightningStrikes;
    private final ArrayList<Point> randomPoints;
    
    public CircleGrid() {
        randomPoints = new ArrayList<>();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    addRandomPoint();
                    Lightning newLightning = new Lightning();
                    newLightning.humidity = 10;
                    newLightning.windSpeed = 5;
                    newLightning.temperature = 10;
                    lightningStrikes.add(newLightning);
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
        repaint();
    }

    // This function just does all the calculations, you can choose if you want to set the x and y to a variable, change the function type to int and return it, change parameter types, or something else. This is just all the calculations, you decide how to manage the data
    // c = circle, x = x of middle, y = y of middle, d = distance away from lightning (ie c1d is circle 1 distance away from lightning, c3x is x pos of middle of circle 3)
    private void triangulation(int c1x, int c1y, int c1d, int c2x, int c2y, int c2d, int c3x, int c3y, int c3d) {
        // Components of calculation to make the final x and y less crazy
        int k = (c1x*c1x - c2x*c2x + c1y*c1y - c2y*c2y + c2d*c2d - c1d*c1d)/2;
        int l = (c1x*c1x - c3x*c3x + c1y*c1y - c3y*c3y + c3d*c3d - c1d*c1d)/2;

        // x and y value of the lightning strike based on the given values, you can do what you want with these
        int x = (k*(c1y-c3y) - l*(c1y-c2y))/((c1x-c2x)*(c1y-c3y) - (c1x-c3x)*(c1y-c2y));
        int y = (l*(c1x-c2x) - k*(c1x-c3x))/((c1x-c2x)*(c1y-c3y) - (c1x-c3x)*(c1y-c2y));
        System.out.println(x + " " + y);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = col * HORIZONTAL_SPACING + ((row%2) * 20);
                int y = (int) (row * VERTICAL_SPACING);
                Point center = new Point(x + RADIUS, y + RADIUS);
                
                boolean highlight = false;
                for (Point p : randomPoints) {
                    if (center.distance(p) <= RADIUS) {
                        highlight = true;
                        break;
                    }
                }
                
                g.setColor(highlight ? Color.RED : new Color(173, 216, 230, 150)); 
                g.fillOval(x, y, DIAMETER, DIAMETER);
            }
        }
        
        
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = col * HORIZONTAL_SPACING + ((row%2) * 20);
                int y = (int) (row * VERTICAL_SPACING);
                Point center = new Point(x + RADIUS, y + RADIUS);
                
                boolean highlight = false;
                for (Point p : randomPoints) {
                    if (center.distance(p) <= RADIUS) {
                        highlight = true; 
                        break;
                    }
                }
                
                g.setColor(highlight ? Color.RED : Color.BLUE);
                g.drawOval(x, y, DIAMETER, DIAMETER);
            }
        }
            
        
        
        g.setColor(Color.BLACK);
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = (col * 40 + ((row%2) * 20)) - 2;
                int y = (int) (row * VERTICAL_SPACING) + RADIUS - 2;
                
               if(col != 0) g.fillOval(x, y, 4, 4);
            }
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
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}