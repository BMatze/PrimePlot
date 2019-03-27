/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primespiral;

/**
 *
 * @author d3fykul7
 */
public class PrimeTest {

    private final static int[] vals = new int[Integer.MAX_VALUE / Integer.SIZE];
    private static boolean initialized = false;

    public static boolean isPrime(int v) {

        --v;
        synchronized (vals) {
            return (vals[v / Integer.SIZE] & (1 << (v % Integer.SIZE))) == 0;
        }
    }

    private static void setNotPrime(int v) {
        int arrIdx = v / Integer.SIZE;
        int offset = v % Integer.SIZE;
        synchronized (vals) {
            vals[arrIdx] |= 0x1 << offset;
        }
    }

    public static void init() {
        if (initialized)
            return;
        synchronized (vals) {
            vals[0] = 0xAFBAEBAD;

            for (int i = 1; i < vals.length; i++) {
                vals[i] |= 0xAAAAAAAA; //factors of 2

                //factors of 3
                switch (i % 3) {
                    case 0:
                        vals[i] |= 0x24924924;
                        break;
                    case 1:
                        vals[i] |= 0x49249249;
                        break;
                    case 2:
                        vals[i] |= 0x92492492;
                        break;
                    default:
                        break;
                }
                //factors of 5
                switch (i % 5) {
                    case 0:
                        vals[i] |= 0x21084210;
                        break;
                    case 2:
                        vals[i] |= 0x42108421;
                        break;
                    case 1:
                        vals[i] |= 0x84210842;
                        break;
                    case 3:
                        vals[i] |= 0x08421084;
                        break;
                    case 4:
                        vals[i] |= 0x10842108;
                        break;
                }
                //factors of 5
                switch (i % 7) {
                    case 0:
                        vals[i] |= 0x08102040;
                        break;
                    case 5:
                        vals[i] |= 0x10204081;
                        break;
                    case 3:
                        vals[i] |= 0x20408102;
                        break;
                    case 1:
                        vals[i] |= 0x40810204;
                        break;
                    case 6:
                        vals[i] |= 0x81020408;
                        break;
                    case 4:
                        vals[i] |= 0x02040810;
                        break;
                    case 2:
                        vals[i] |= 0x04081020;
                        break;
                }

            }
        }
        System.out.println("Primary case completed!");
        for (int i = 6; i < Integer.MAX_VALUE - 1; i++) {
            if (i % 1000000 == 0) {
                System.out.printf("Initializing prime vector.. %.4f\n", i / (double) Integer.MAX_VALUE);
            }

            if (isPrime(i + 1)) {
                //System.out.println("Prime!");
                for (int j = i; j > 0 && j < Integer.MAX_VALUE - 1; j += (i + 1)) {
                    if (j + 1 < Integer.SIZE) {
                        setNotPrime(j);
                    }

                }
            }
        }
        initialized = true;
    }
}
