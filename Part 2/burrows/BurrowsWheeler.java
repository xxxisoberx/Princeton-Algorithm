import java.util.Arrays;
import java.util.ArrayList;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform() {
        String oriStr = BinaryStdIn.readString();
        int len = oriStr.length();
        CircularSuffixArray CSA = new CircularSuffixArray(oriStr);
        char[] tmp = new char[len];
        boolean ifFindFirst = false;
        for (int i = 0; i < len; i++) {
            int oriIdx = CSA.index(i);
            char lastCol = oriStr.charAt((oriIdx - 1 + len) % len);
            if (oriIdx == 0) {
                ifFindFirst = true;
                BinaryStdOut.write(i);
                for (int j = 0; j < i; j++) {
                    BinaryStdOut.write(tmp[j]);
                }
                BinaryStdOut.write(lastCol);
            } else if (!ifFindFirst) {
                tmp[i] = lastCol;
            } else {
                BinaryStdOut.write(lastCol);
            }  
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int firstRow = BinaryStdIn.readInt();
        // construct "lastCol" and "firstCol"
        ArrayList<Character> lastCol = new ArrayList<>();
        while (!BinaryStdIn.isEmpty()) {
            lastCol.add(BinaryStdIn.readChar());
        }
        int len = lastCol.size();
        char[] firstCol = new char[len];
        for (int i = 0; i < len; i++) {
            firstCol[i] = lastCol.get(i);
        }
        Arrays.sort(firstCol);
        int[] next = new int[len];
        // construct next[i] from "firstCol" and "lastCol"
        char pre = 0;
        int preIdx = -1;
        for (int i = 0; i < len; i++) {
            char curChar = firstCol[i];
            if (curChar != pre) {
                pre = curChar;
                preIdx = -1;
            }
            for (int j = preIdx + 1; j < len; j++) {
                if (j == i) continue;
                if (lastCol.get(j) == curChar) {
                    next[i] = j;
                    preIdx = j;
                    break;
                }
            }
        }
        // construct original string from int "firstRow" and char[] "firstCol"
        char[] oriChars = new char[len];
        int row = firstRow;
        for (int i = 0; i < len; i++) {
            oriChars[i] = firstCol[row];
            row = next[row];
        }
        BinaryStdOut.write(new String(oriChars));
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args == null) throw new IllegalArgumentException();
        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();
    }

}