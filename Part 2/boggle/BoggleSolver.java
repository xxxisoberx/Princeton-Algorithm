import java.util.HashSet;

public class BoggleSolver {
    private final Node root;
    private HashSet<String> validWords;
    private boolean[][] marked;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        root = new Node();
        for (String s : dictionary) {
            put(s);
        }
    }

    private class Node {
        public boolean hasString;
        public Node[] next;

        public Node() {
            this.hasString = false;
            next = new Node[26];
        }
    }

    private void put(String s) {
        put(s, root);
    }

    private void put(String s, Node n) {
        if (s.equals("")) {
            n.hasString = true;
            return;
        }
        char curChar = s.charAt(0);
        int idxOfChar = curChar - 'A';
        if (n.next[idxOfChar] == null) n.next[idxOfChar] = new Node();
        put(s.substring(1), n.next[idxOfChar]);
    }

    private boolean get(String s) {
        return get(s, root);
    }

    private boolean get(String s, Node n) {
        if (s.equals("")) return n.hasString;
        char curChar = s.charAt(0);
        int idxOfChar = curChar - 'A';
        if (n.next[idxOfChar] == null) return false;
        return get(s.substring(1), n.next[idxOfChar]);
    }

    private boolean isInBoard(int row, int col, BoggleBoard board) {
        return row >= 0 && row < board.rows() && col >= 0 && col < board.cols();
    }
    
    // search all sets of string under a node
    private void dfs(BoggleBoard board, int row, int col, Node n, String preStr) {
        char c = board.getLetter(row, col);
        if (n.next[c - 'A'] == null) return;
        String curStr = preStr + c;
        Node nextNode = n.next[c - 'A'];
        if (c == 'Q') {
            curStr += 'U';
            nextNode = nextNode.next['U' - 'A'];
            if (nextNode == null) return;
        }
        if (curStr.length() >= 3 && nextNode.hasString) validWords.add(curStr);
        marked[row][col] = true;
        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {
                if (dRow == 0 && dCol == 0) continue;
                int curRow = row + dRow;
                int curCol = col + dCol;
                if (!isInBoard(curRow, curCol, board) || marked[curRow][curCol]) continue;
                dfs(board, curRow, curCol, nextNode, curStr);
            }
        }
        marked[row][col] = false;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        validWords = new HashSet<>();
        int rows = board.rows();
        int cols = board.cols();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                marked = new boolean[rows][cols];
                dfs(board, row, col, root, "");
            }
        }
        return validWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!get(word)) return 0;
        int len = word.length();
        if (len < 3) return 0;
        if (len <= 4) return 1;
        if (len == 5) return 2;
        if (len == 6) return 3;
        if (len == 7) return 5;
        return 11;
    }
}