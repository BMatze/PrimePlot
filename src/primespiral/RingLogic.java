package primespiral;
import java.awt.Point;
import static java.lang.Math.PI;
import static java.lang.Math.atan2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author defykul7
 */
public class RingLogic {
    /**
     * Determines the ring the given place is in.
     * @param distance - number of nodes after the initial node.
     * @return the ring the node is in
     */
    public static int getRing(int distance) {
        assert (distance >= 0);

        if (distance == 0) {
            return 0;
        }

        return (int) (Math.ceil((Math.sqrt(4.0 * distance + 1) - 1.0) / 4.0));
    }
    /**
     * Converts a given distance from the initial node to an x,y coordinate pair
     * and returns them as a point object
     * @param distance - number of nodes after the initial node
     * @return a Point object with the x,y coordinate pair referring to the given node.
     */
    public static Point convertDistToCoordinates(int distance) {
        final Point retPoint = new Point();
        int ring = RingLogic.getRing(distance);
        int distIntoRing = distance - getMinRing(ring);
        int rightAndUpDist = 2*ring-1;
        int leftAndDownDist = 2*ring;
        if (distance == 0) {
            //(0,0)
            return retPoint;
        }
        
        if (distIntoRing < rightAndUpDist) {
            //right
            
            retPoint.setLocation(DirectionVector.RIGHT.getX(ring, distIntoRing),DirectionVector.RIGHT.getY(ring, distIntoRing));
            return retPoint;
        }
        distIntoRing -=rightAndUpDist;
        
        if (distIntoRing < rightAndUpDist) {
            //up
           
            retPoint.setLocation(DirectionVector.UP.getX(ring, distIntoRing),DirectionVector.UP.getY(ring, distIntoRing));
            return retPoint;
        }
        distIntoRing -=rightAndUpDist;
        if (distIntoRing < leftAndDownDist) {
            //left
           
            retPoint.setLocation(DirectionVector.LEFT.getX(ring, distIntoRing),DirectionVector.LEFT.getY(ring, distIntoRing));
            return retPoint;
        } else {
            distIntoRing-=leftAndDownDist;
            //down
            
            retPoint.setLocation(DirectionVector.DOWN.getX(ring, distIntoRing),DirectionVector.DOWN.getY(ring, distIntoRing));
            return retPoint;
        }
    }
    /**
     * Gets the ring the node represented by the coordinate pair.
     * @param x
     * @param y
     * @return 
     */
    public int getRing(int x, int y) {
        //ring 0, shortcut and edge case
        if ((x | y) == 0) { // (x==0) && (y==0)
            return 0;
        }

        final double shift = PI/180;
        double angleU, angleL, angleR, angleD;

        /**
         * Each angle starts somewhere relative to the initial node. The
         * initial(root) node is always at the origin (0,0). This block is
         * shifting the ring 1 element such that it is on the origin, then
         * computes the angle to the coordinate in the same relative manner. --
         * atan2 produces a result between -PI and PI. These are converted into
         * Degrees, and for all except the UP, they are made into their positive
         * representations between [0, 360) This makes it so each section has
         * one continuous region to compare. -- shift simply converts degrees to
         * radians by multiplying
         *
         */
        computationalShifts:
        {
            //Up starts at point (1,1) and runs diagonaly down-right.
            angleU = atan2(y - 1, x - 1);

            angleL = atan2(y - 1, x);
            if (angleL < 0) {
                angleL += 360;
            }

            angleR = atan2(y, x - 1);
            if (angleR < 0) {
                angleR += 360;
            }

            angleD = atan2(y, x + 1);
            if (angleD < 0) {
                angleD += 360;
            }
        }
        /**
         * With the angle to the coordinate shifted relative to each starting
         * position,we now check each quadrant to see if the angle is between
         * the extremes. This should only be true for exactly one, but possibly
         * could get an error if precision is off.
         */
        determination:
        {
            final double angleUMin = -45*shift;
            final double angleUMax = 45*shift;//UMax = LMin
            final double angleLMax = 135*shift;//LMax = DMin
            final double angleDMax = 225*shift;//DMax = RMin
            final double angleRMax = 315*shift;//RMax = UMin but UMin is kept negative
            if (angleUMin <= angleU && angleU < angleUMax) {
                //up
                return x;
            } else if (angleUMax <= angleL && angleL < angleLMax) {
                //left
                return y;
            } else if (angleLMax <= angleD && angleD < angleDMax) {
                //down
                return -x;
            } else if (angleDMax <= angleR && angleR < angleRMax) {
                //right
                return x;
            }
        }
        throw new RuntimeException("Not reachable");
    }
    /**
     * Gets the farthest distance in a ring
     * @param ring which ring you want the max of
     * @return the farthest distance in the ring.
     */
    public static int getMaxRing(int ring) {
        if (ring == 0) {
            return 0;
        }
        return (int) (4 * ring * ring + 2 * ring);
    }
    /**
     * Gets the closest distance within a ring.
     * @param ring - the ring you want the closest distance for.
     * @return the closest distance in the ring
     */
    public static int getMinRing(int ring) {
        if (ring == 0) {
            return 0;
        }
        return getMaxRing(ring - 1) + 1;
    }
    /**
     * Gets a vector that represents the direction the node at a given distance 
     * is facing.
     * @param distance how far from the initial node you are.
     * @return A DirectionVector describing the direction.
     */
    private DirectionVector getDirection(int distance) {
        int ring = RingLogic.getRing(distance);

        int maxRing = RingLogic.getMaxRing(ring);
        int nextStart = maxRing + 1;  //start at the next ring's "Right"  

        int oneBack = DirectionVector.DOWN.getLength(ring);
        int twoBack = DirectionVector.LEFT.getLength(ring) + oneBack;
        int threeBack = DirectionVector.UP.getLength(ring) + twoBack;

        if (nextStart - oneBack < distance) {
            //going down
            return DirectionVector.DOWN;
        } else if (nextStart - twoBack < distance) {
            //going left
            return DirectionVector.LEFT;
        } else if (nextStart - threeBack < distance) {
            //going up
            return DirectionVector.UP;
        } else {
            //going right
            return DirectionVector.RIGHT;
        }
    }

}
