/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primespiral;

import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JFrame;
import matzie.primespiral.ui.ViewRegion;

/**
 *
 * @author d3fykul7
 */
public class PrimeSpiral {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        JFrame f = new JFrame("Testing");
        new Thread(()->PrimeTest.init()).start();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setMinimumSize(new Dimension(500, 500));
        ViewRegion vr = new ViewRegion();
        vr.addMouseMotionListener(vr);
        vr.addMouseListener(vr);
        f.setContentPane(vr);
        f.setVisible(true);
        vr.repaint();
        

    }

}
