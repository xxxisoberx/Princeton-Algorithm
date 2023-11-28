import java.util.Arrays;

public class CircularSuffixArray {
    private final int len;
    private final int[] index;
    
    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        len = s.length();
        CircularSuffix[] suffixes = new CircularSuffix[len];
        String str = s + s;
        for (int i = 0; i < len; i++) {
            suffixes[i] = new CircularSuffix(str.substring(i, i + len), i);
        }
        Arrays.sort(suffixes);
        index = new int[len];
        for (int i = 0; i < len; i++) {
            index[i] = suffixes[i].oriIdx();
        }
    }

    // construct new class to record original index and successfully implement Arrays.sort
    private class CircularSuffix implements Comparable<CircularSuffix> {
        private final String str;
        private final int oriIdx;

        public CircularSuffix(String str, int idx) {
            this.str = str;
            this.oriIdx = idx;
        }

        public int oriIdx() {
            return oriIdx;
        }
        
        public int compareTo(CircularSuffix that) {
            for (int i = 0; i < len; i++) {
                if (this.str.charAt(i) < that.str.charAt(i)) return -1;
                if (this.str.charAt(i) > that.str.charAt(i)) return 1;
            }
            return 0;
        }
    }

    // length of s
    public int length() {
        return len;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length()) throw new IllegalArgumentException();

        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray CSA = new CircularSuffixArray("BACD");
        System.out.println(CSA.length());
        System.out.println(CSA.index(0));
    }

}