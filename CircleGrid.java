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
    private static final double VERTICAL_SPACING = Math.sqrt(3) * RADIUS + PADDING;
    private static final int HORIZONTAL_SPACING = DIAMETER + PADDING;
    private static final int WIDTH = 580;
    private static final int HEIGHT = 496;
    
    private final ArrayList<Point> randomPoints;
    
    public CircleGrid() {
        randomPoints = new ArrayList<>();
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
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw all filled circles first
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = col * HORIZONTAL_SPACING - (row % 2) * 40;
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
                int x = col * HORIZONTAL_SPACING + (row % 2) * 40;
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
                int x = col * 40;
                int y = (int) (row * VERTICAL_SPACING) + RADIUS - 2;
                g.fillOval(x, y, 4, 4);
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