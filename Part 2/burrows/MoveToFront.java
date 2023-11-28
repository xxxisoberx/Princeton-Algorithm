import java.util.LinkedList;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> sequence = new LinkedList<>();
        for (char c = 0; c < 256; c++) {
            sequence.add(c);
        }
        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar();
            int idx = sequence.indexOf(ch);
            BinaryStdOut.write(idx, 8);
            sequence.remove(idx);
            sequence.addFirst(ch);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> sequence = new LinkedList<>();
        for (char c = 0; c < 256; c++) {
            sequence.add(c);
        }
        while (!BinaryStdIn.isEmpty()) {
            int idx = BinaryStdIn.readInt(8);
            char ch = sequence.get(idx);
            BinaryStdOut.write(ch);
            sequence.remove(idx);
            sequence.addFirst(ch);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args == null) throw new IllegalArgumentException();
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }

}