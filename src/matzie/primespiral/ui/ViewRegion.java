/*
 * Class: ViewRegion.java
 * Description: Display a region of a number spiral.
 * Author: Brock Matzenbacher
 */
package matzie.primespiral.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.function.IntUnaryOperator;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import primespiral.DirectionVector;
import primespiral.PrimeTest;
import primespiral.RingLogic;

/**
 *
 * @author Brock Matzenbacher
 */
public class ViewRegion extends JPanel implements MouseInputListener, ActionListener {

    public ViewRegion() {
        super();

    }
    //adjusts how quickly the graph zoom in and out.
    private final double ZOOM_SENSITIVITY = 1;
    private int initialX = 0, initialY = 0;
    //dimension of a tile
    private int boxSize = 25;
    
    //holds coordinates of mouse for zoom and navigation
    private int lastX = 0, lastY = 0;
    private IntUnaryOperator colorSchemer = (int operand) -> PrimeTest.isPrime(operand) ? 0x00FF0000 : 0;
    
    @Override
    public void mouseDragged(MouseEvent e) {

        if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) > 0) { //left mouse button
            //move camera
            initialX += e.getX() - lastX;
            initialY += e.getY() - lastY;

        } else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) > 0) {//right mouse button
            //zoom camera
            
            //how much the boxsize will be adjusted
            double zoom = ZOOM_SENSITIVITY * (e.getY() - lastY);
             
            
            //calculate the tile in the center
            double centerX = (initialX + super.getWidth()/2.0)/boxSize;
            double centerY = (initialY + super.getHeight()/2.0)/boxSize;
           //System.out.println("B:"+initialX+","+initialY);
           
            //apply new boxSize
            boxSize += zoom;
            
            
            
            if (boxSize > .5 * super.getWidth()) {
                boxSize = (int) (.5 * super.getWidth());
            }
            if (boxSize > .5 * super.getHeight()) {
                boxSize = (int) (.5 * super.getHeight());
            }
            if (boxSize < 1) {
                boxSize = 1;
            }
            
            //[BROKEN] keep the center tile in the center
            initialX = (int) ((centerX*boxSize - super.getWidth()/2));
            initialY = (int) ((centerY*boxSize - super.getHeight()/2));
            System.out.println("A:"+initialX+","+initialY);
            
        }
        lastX = e.getX();
        lastY = e.getY();
        super.repaint();

    }

    //keep mouse coordinates up to date
    @Override
    public void mouseMoved(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();

    }

    
    @Override
    public void mouseClicked(MouseEvent e) {
        
        //System.out.println(e.getButton());
        
        if (e.getButton()==MouseEvent.BUTTON2) { // Middle mouse button
            //Reset camera to center
            initialX = initialY = 0;
            super.repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    public void setColorSchemer(IntUnaryOperator iuo) {
        if (iuo != null)
            this.colorSchemer = iuo;
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        int xOff, yOff;
        
        //gets the relative offset of each tile (needed for drawing the grid)
        xOff = (initialX) % boxSize;
        yOff = (initialY) % boxSize;

        //draw vertical portion of grid
        for (int idx = xOff; idx < super.getWidth(); idx += boxSize) {
            g2.drawLine(idx, 0, idx, super.getHeight());
        }

        //draw horizontal portion of grid
        for (int idx = yOff; idx < super.getHeight(); idx += boxSize) {
            g2.drawLine(0, idx, super.getWidth(), idx);
        }
        
        //gets the tile at each corner of the visible region
        int topLeft = coordToDistance(getXPoint(), getYPoint());
        int topRight = coordToDistance(getXPoint() + getGridWidth() - 1, getYPoint());
        int bottomLeft = coordToDistance(getXPoint(), getYPoint() - getGridHeight() + 1);
        int bottomRight = coordToDistance(getXPoint() + getGridWidth() - 1, getYPoint() - getGridHeight() + 1);
        
        
        Font f = g.getFont();
        int max = topLeft;
        //finding the largest value, this is to ensure the string's text fits within the tile
        if (topRight > max) {
            max = topRight;
        }
        if (bottomRight > max) {
            max = bottomRight;
        }
        if (bottomLeft > max) {
            max = bottomLeft;
        }
        Rectangle2D stringBounds = f.getStringBounds(Integer.toString(max), g2.getFontRenderContext());
        //scales the font to make sure the text stays within its tile
        if (stringBounds.getWidth() > stringBounds.getHeight()) {
            //limit by width
            g.setFont(f.deriveFont((float) (f.getSize2D() * (boxSize * .93) / stringBounds.getWidth())));
        } else {
            //limit by height
            g.setFont(f.deriveFont((float) (f.getSize2D() * (boxSize * .93) / stringBounds.getHeight())));
        }
        //iterate the visible grid, draw each tile.
        for (int x = getXPoint(); x <= getXPoint() + getGridWidth(); x++) {
            for (int y = getYPoint() - getGridHeight(); y <= getYPoint(); y++) {
                drawTile(g2, coordToDistance(x, y));
            }
        }
        //colors the center tile (for convinence) 
        g2.setColor(Color.green);
        g2.drawRect(initialX, initialY - boxSize, boxSize, boxSize);
    }

    public Point coordToPoint(int xPoint, int yPoint) {

        return new Point(xPoint, yPoint);
    }

    //top left x grid coordinate
    public int getXPoint() {
        return (int) Math.floor(-initialX / (double) boxSize);
    }

    //top left y grid coordinate
    public int getYPoint() {
        return (int) Math.floor(initialY / (double) boxSize);
    }

    public int getGridWidth() {
        return (int) Math.ceil(super.getWidth() / (double) boxSize);

    }

    public int getGridHeight() {
        return (int) Math.ceil(super.getHeight() / (double) boxSize);
    }

    public int coordToDistance(int xPoint, int yPoint) {

        DirectionVector dir = DirectionVector.getDirection(xPoint, yPoint);
        //System.out.println(dir + " (" + xPoint + ", " + yPoint + ")");
        int ring = dir.getRing(xPoint, yPoint);
        int start = dir.getStart(ring);

        return start + dir.getOffset(xPoint, yPoint);
    }

    public void drawTile(Graphics2D g, int index) {

        Point p = RingLogic.convertDistToCoordinates(index);

        //relative shift to edge of box
        int xOff = (initialX) % boxSize;
        int yOff = (initialY - boxSize + 1) % boxSize;
        if (initialX > 0) {
            xOff = (initialX + boxSize - 1) % boxSize - boxSize;
        }
        if (initialY > 0) {
            yOff = (initialY + boxSize) % boxSize - boxSize;
        }

        //g.drawString(p.x + " " + p.y, 30, 30);
        //g.drawString(xOff+" "+yOff,30,50);
        // g.drawString(initialX+" "+initialY,30,70);
        //string of index to draw
        String num = Integer.toString(index + 1);

        //draw the id of this tile
        g.setColor(new Color(colorSchemer.applyAsInt(index+1)));
        if (boxSize < 10)
            g.fillRect(initialX+p.x*boxSize, initialY-p.y*boxSize, boxSize, boxSize);
        else
            g.drawString(num, (int) (initialX + p.x * boxSize), (int) (initialY - ((p.y) * boxSize)));

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
