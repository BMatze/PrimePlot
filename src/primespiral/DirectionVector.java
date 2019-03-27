package primespiral;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author defykul7
 */
public abstract class DirectionVector {

    /**
     * Gets the distance from the initial node a ring is.
     *
     * @param ring which ring you want the start of.
     * @return the distance from the initial node to the one in this direction.
     */
    public abstract int getStart(int ring);

    /**
     *
     * @param ring
     * @return
     */
    public abstract int getLength(int ring);
    
    public int getEnd(int ring) {
        if (ring == 0) {
            return 0;
        }
        return getStart(ring) + getLength(ring) - 1;
    }

    /**
     * Gets the number of additional spaces after the current one.
     *
     * @param x
     * @param y
     * @return
     */
    public int getRemaining(int x, int y) {
        return getLength(getRing(x, y)) - getOffset(x, y) - 1;
    }
    public static DirectionVector getDirection(int x, int y) {

        if (RIGHT.isDirection(x, y)) {
            return RIGHT;
        } else if (UP.isDirection(x, y)) {
            return UP;
        } else if (LEFT.isDirection(x, y)) {
            return LEFT;
        } else if (DOWN.isDirection(x, y) || (x | y ) == 0) {
            return DOWN;
        }
        else return null;
    }

    public abstract boolean isDirection(int x, int y);

    //public abstract int getOffset(int distance);
    /**
     * How far into the vector the entry is.
     * @param x - x coord
     * @param y - y coord
     * @return number of entries into a direction in the current ring.
     */
    public abstract int getOffset(int x, int y);

    /**
     * Gets the ring that the coordinate is in. This is only correct if
     * isDirection() is true.
     *
     * @param x
     * @param y
     * @return
     */
    public abstract int getRing(int x, int y);

    public abstract int getX(int ring, int offset);

    public abstract int getY(int ring, int offset);
    public static final DirectionVector RIGHT = new DirectionVector() {
        @Override
        public int getStart(int ring) {
            return 4 * ring * ring - 6 * ring + 3;
        }
        
        @Override
        public int getLength(int ring) {
            return 2 * ring - 1;
        }

        @Override
        public boolean isDirection(int x, int y) {
            
            int ring = getRing(x, y);
            int offset = getOffset(x, y);
            //System.out.println("R"+ring+" "+offset);
            return (offset >= 0 && offset < getLength(ring));
        }

        @Override
        public int getOffset(int x, int y) {
            return x - y - 1;
        }

        @Override
        public int getRing(int x, int y) {
            return 1 - y;
        }

        @Override
        public int getX(int ring, int offset) {
            return 2 - ring + offset;
        }

        @Override
        public int getY(int ring, int offset) {
            return 1 - ring;
        }
        public String toString() {
            return "Right";
        }
    };
    public static final DirectionVector UP = new DirectionVector() {
        @Override
        public int getStart(int ring) {
            return (4 * ring) * (ring - 1) + 2;
        }

        @Override
        public int getLength(int ring) {
            return 2 * ring - 1;
        }

        @Override
        public boolean isDirection(int x, int y) {
            int ring = getRing(x, y);
            int offset = getOffset(x, y);
            //System.out.println("U"+ring+" "+offset);
            return (offset >= 0 && offset < getLength(ring));
        }

        @Override
        public int getOffset(int x, int y) {
            return y + x - 2;
        }

        @Override
        public int getRing(int x, int y) {
            return x;
        }

        @Override
        public int getX(int ring, int offset) {
            return ring;
        }

        @Override
        public int getY(int ring, int offset) {
            return 2 - ring + offset;
        }
        public String toString() {
            return "Up";
        }
    };
    public static final DirectionVector LEFT = new DirectionVector() {
        @Override
        public int getStart(int ring) {
            return 2 * ring * (2 * ring - 1) + 1;
        }

        @Override
        public int getLength(int ring) {
            return 2 * ring;
        }

        @Override
        public boolean isDirection(int x, int y) {
            int ring = getRing(x, y);
            int offset = getOffset(x, y);
            //System.out.println("L"+ring+" "+offset);
            return (offset >= 0 && offset < getLength(ring));
        }

        @Override
        public int getOffset(int x, int y) {
            return y - 1 - x;
        }

        @Override
        public int getRing(int x, int y) {
            return y;
        }

        @Override
        public int getX(int ring, int offset) {
            return ring - 1 - offset;
        }

        @Override
        public int getY(int ring, int offset) {
            return ring;
        }
        public String toString() {
            return "Left";
        }
    };
    public static final DirectionVector DOWN = new DirectionVector() {
        @Override

        public int getStart(int ring) {
            return 4 * ring * ring + 1;
        }

        @Override
        public int getLength(int ring) {
            return 2 * ring;
        }

        @Override
        public boolean isDirection(int x, int y) {
            int ring = getRing(x, y);
            int offset = getOffset(x, y);
            //System.out.println("D"+ring+" "+offset);
            return (offset >= 0 && offset < getLength(ring));
        }

        @Override
        public int getOffset(int x, int y) {
            return -(x + y + 1);
        }

        @Override
        public int getRing(int x, int y) {
            return -x;
        }

        @Override
        public int getX(int ring, int offset) {
            return -ring;
        }

        @Override
        public int getY(int ring, int offset) {
            return ring - 1 - offset;
        }
        public String toString() {
            return "Down";
        }
    };
}
